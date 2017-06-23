package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.thread.ReceiveMessageThread;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/5/24.
 */

public class MyService extends Service {

    private ReceiveMessageThread rwt;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand executed");
        rwt = new ReceiveMessageThread(this);
        rwt.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestory executed");

        rwt.exit = true;



    }


}
