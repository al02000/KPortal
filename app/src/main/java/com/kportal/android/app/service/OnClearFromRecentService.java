package com.kportal.android.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by KR8 on 2017-01-02.
 */

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
    }
}
