package com.example.smsapp;

import android.app.Application;
import android.util.Log;

import utils.NetworkUtils;

public class MyApplication extends Application {
    public static String host;
    public static String phone;

    @Override
    public void onCreate() {
        super.onCreate();

        String ip = NetworkUtils.getIPAddress(this);
        Log.i("MyApplication", "ip->" + ip);
    }
}

