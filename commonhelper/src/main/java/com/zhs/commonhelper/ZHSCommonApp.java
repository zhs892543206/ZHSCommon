package com.zhs.commonhelper;

import android.content.Context;

/**
 * 应用使用这个库时myapplication的oncreate开头先进行初始化，
 * Created by zhs89 on 2018/3/30.
 */

public class ZHSCommonApp {

    public static Context appContext;
    public void initApp(Context context){
        appContext = context;
    }
}
