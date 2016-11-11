package com.linksprite.io.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.linksprite.io.network.NetworkUtils;


/**
 * Created by Administrator on 2016/8/1.
 */
public class NetworkStateService extends Service {
    public static final String TAG = "NetworkStateService";

    private Thread loop;

    public static Boolean loopRunning;
    public static Boolean triggerCall;
    public static Boolean serviceEnabled = false;

    @Override
    public void onCreate() {
        loopRunning = false;
        serviceEnabled = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!loopRunning) {
            Log.d(TAG, "Starting loop");
            startLoop();
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent myIntent) {
        return null;
    }

    protected void startLoop() {
        loopRunning = true;
        loop = new Thread(new Runnable() {
            public void run() {

                while (true) {
                    //if (serviceEnabled) {
                    //Log.d(TAG, "Running network status loop");
                    NetworkUtils.refreshNetworkMode(getApplicationContext());

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        break; // onDestroy was called
                    }
                    //}
                }

            }
        });

        loop.start();
    }

    @Override
    public void onDestroy() {
        loopRunning = false;
        if (loop != null) {
            loop.interrupt();
            Log.d(TAG, "Destroying loop");
        }
    }
}
