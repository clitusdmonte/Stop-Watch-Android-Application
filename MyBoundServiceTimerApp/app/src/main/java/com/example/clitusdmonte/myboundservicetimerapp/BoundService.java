package com.example.clitusdmonte.myboundservicetimerapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

/**
 * Created by clitus dmonte on 2/28/2018.
 */

public class BoundService extends Service {
    private static String LOG_SERVICE = "BoundService";
    private IBinder binder = new MyBinder();
    private Chronometer chronometer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_SERVICE, "in onCreate");
        chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_SERVICE, "BoundService in onBind");
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_SERVICE, "BoundService in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_SERVICE, "BoundService in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_SERVICE, "BoundService in onDestroy");
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        chronometer.stop();
    }

    public void stopChronometer() {
        chronometer.stop();
    }

    public String getTimestamp(long st) {
        long et = SystemClock.elapsedRealtime() - st;
        int h = (int) (et / 3600000);
        int m = (int) (et - h * 3600000) / 60000;
        int s = (int) (et - h * 3600000 - m * 60000) /
                1000;
        int ms = (int) (et - h * 3600000 - m * 60000 -
                s * 1000);
        return h + ":" + m + ":" + s + ":" + ms;
    }

    public class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
}
