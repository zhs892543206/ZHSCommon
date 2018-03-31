package com.zhs.commonhelper.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

/**
 * Created by zhs89 on 2018/3/27.
 */

public class AppFileUtil {
    //在新的应用的application里修改
    public static String AppPath = "ZHS";//默认应用地址
    public static String LogFileName ="logo";//默认普通日志文件夹名
    public static String CrashFileName =  "crash";
    public static String PACKAGE_NAME = "";//包名

    /**
     * 获取当前进程名
     * context context这个还是用传入的好
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 获取应用各种文件存储路径
     * @return
     */
    public static String getAppFilePath(){
        String path;
        //判断外置内存卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath()+"/"+AppPath;
        }else{
            path = "/sdcard/"+AppPath;

        }
        return path;
    }
}
