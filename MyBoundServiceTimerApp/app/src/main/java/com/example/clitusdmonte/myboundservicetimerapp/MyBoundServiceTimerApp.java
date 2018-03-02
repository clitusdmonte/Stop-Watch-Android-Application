package com.example.clitusdmonte.myboundservicetimerapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by clitus dmonte on 2/28/2018.
 */

public class MyBoundServiceTimerApp extends AppCompatActivity {
    Context context;
    Button startServButton, resetButton, stopButton, startButton, stopServButton;
    TextView stopWatch;
    long startTime;
    Handler handler;
    BoundService boundService;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bound_service_timer_app);

        context = this;
        stopWatch = (TextView) findViewById(R.id.stopWatch);
        startServButton = (Button) findViewById(R.id.startServButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        startButton = (Button) findViewById(R.id.startButton);
        stopServButton = (Button) findViewById(R.id.stopServButton);
        handler = new Handler();

        resetButton.setEnabled(false);
        stopButton.setEnabled(false);
        startButton.setEnabled(false);

        startServButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BoundService.class);
                startService(intent);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                startButton.setEnabled(true);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    resetButton.setEnabled(true);
                }

            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
                resetButton.setEnabled(true);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                stopWatch.setText("0:0:0:00");
                stopButton.setEnabled(false);
                resetButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        });
        stopServButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                if (flag) {
                    unbindService(serviceConnection);
                    flag = false;
                }
                Intent intent = new Intent(context, BoundService.class);
                stopService(intent);
                stopButton.setEnabled(false);
                resetButton.setEnabled(false);
                startButton.setEnabled(false);
            }
        });
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            stopWatch.setText(boundService.getTimestamp(startTime));
            handler.postDelayed(this, 0);
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            flag = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
            boundService = myBinder.getService();
            flag = true;
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        if (flag) {
            unbindService(serviceConnection);
            flag = false;
        }
    }

}
