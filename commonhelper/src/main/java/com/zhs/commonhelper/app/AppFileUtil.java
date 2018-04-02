package com.zhs.commonhelper.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.zhs.commonhelper.ZHSCommonApp;
import com.zhs.commonhelper.widget.dialog.ToastUtil;

import java.io.File;

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

//    /**
//     * 获取应用各种文件存储路径
//     * @return
//     */
//    public static String getAppFilePath(){
//        String path;
//        //判断外置内存卡是否存在，并且是否具有读写权限
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            path = Environment.getExternalStorageDirectory().getPath()+"/"+AppPath;
//        }else{
//            path = "/sdcard/"+AppPath;
//
//        }
//        return path;
//    }

    /**
     * 外部存储
     * 获取该应用的公共目录
     * 就是外部存储的根目录
     * 应用卸载不会自动删除
     *
     * newFile(Environment.getExternalStoragePublicDirectory(
     Environment.DIRECTORY_PICTURES), albumName);
     在公共文件的相机文件目录下创建albumName文件
     * @return
     */
    public static File getPublicFile(){
        if(getExternalStoragePerm()>0){
            return Environment.getExternalStoragePublicDirectory(AppPath);
        }else{
            return null;
        }
    }

    /**
     * 外部存储
     * 获取私有文件目录
     * 应用卸载也会自动删除
     * getExternalFilesDir(fileName)即
     * /mnt/sdcard/android/data/包名/files/fileName
     * @param fileName
     * @return
     */
    public static File getPrivateFile(String fileName){
        if(getExternalStoragePerm()>0){
            return ZHSCommonApp.appContext.getExternalFilesDir(fileName);

        }else{
            return null;
        }
    }

    /**
     * 私有
     *  /sdcard/Android/data/<application package>/cache
     *
     *  getCacheDir()少个external返回的是 /data/data/<application package>/cache
     * @return
     */
    public static File getPrivateCache(){
        if(getExternalStoragePerm()>0){
            return ZHSCommonApp.appContext.getExternalCacheDir();

        }else{
            return null;
        }
    }

    /**
     *返回文件data/data/包名/files
     * @return
     */
    public static File getDataFile(){
        if(getExternalStoragePerm()>0){
            return ZHSCommonApp.appContext.getFilesDir();

        }else{
            return null;
        }
    }

    /**
     * 获取外部存储权限
     * 2是可用，可写，1是可读不可写，0是都不可
     * @return
     */
    public static int getExternalStoragePerm(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            return 2;
        } else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            return 1;
        } else{
            //没有外部存储权限时弹出提示
            ToastUtil.showToast("No ExternalStorage Permission", Toast.LENGTH_SHORT);
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            return 0;
        }
    }
}
