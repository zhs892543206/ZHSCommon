package com.zhs.jarapplication;

import android.app.Application;

import com.zhs.commonhelper.ZHSCommonApp;

/**
 * Created by zhs89 on 2018/4/12.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ZHSCommonApp.initApp(this);
    }
}
