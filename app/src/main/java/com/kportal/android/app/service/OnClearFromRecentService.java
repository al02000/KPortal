package com.kportal.android.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by KR8 on 2017-01-02.
 */

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent){
        Log.i("TEST", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("TEST", "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Log.i("TEST", "onTaskRemoved");
    }
}
