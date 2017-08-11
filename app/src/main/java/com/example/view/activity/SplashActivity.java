package com.example.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.utils.ActivityManager;
import com.nanchen.crashmanager.UncaughtExceptionHandlerImpl;

public class SplashActivity extends AppCompatActivity {
    private static int splashScreenTimeOut=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActivityManager.addActivity(this,"a");
        UncaughtExceptionHandlerImpl.getInstance().init(this, BuildConfig.DEBUG, true, 0, SplashActivity.class);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intentMain = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intentMain);
                finish();
            }
        }, splashScreenTimeOut);


    }
}
