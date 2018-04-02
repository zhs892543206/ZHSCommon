package com.zhs.commonhelper.file;

/*  * 文 件 名:  DataCleanManager.java  * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录  */

import android.content.Context;
import android.os.Environment;

import com.zhs.commonhelper.app.AppFileUtil;
import com.zhs.commonhelper.date.TimeUtil;
import com.zhs.commonhelper.log.LogUtil;

import java.io.File;
import java.util.Date;


/** * 本应用数据清除管理器 */
public class DataCleanManager {
    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
        //new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 定期清理log文件执行这个方法就好
     * longTime   删除一个星期以前的，传7
     */
    public static void deleteLogFiles(int longTime) {
        String time = TimeUtil.dateToStryyyyMMdd(new Date(new Date().getTime()-longTime*24*60*60*1000));

        deleteFilesByDirectory(time);
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     * *fileName参数
     * String path;
     //判断外置内存卡是否存在
     if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
     path = Environment.getExternalStorageDirectory().getPath()+"/"+MyApplication.AppName;
     }else{
     path = "/sdcard/"+MyApplication.AppName;

     }
     time参数可以 DateFormatUtil.dateToStryyyyMMdd(startDate = new Date(new Date().getTime()-7*24*60*60*1000); ) //删除一个星期前的所有日志文件
     * */
    private static void deleteFilesByDirectory(String time) {
        LogUtil.e("删除,time为"+time+"以前的日志文件");

        //删除logo文件夹里面日志20161006
        File logFile = AppFileUtil.getPrivateFile(AppFileUtil.LogFileName);
        if (logFile != null && logFile.exists() && logFile.isDirectory()) {
            for (File item : logFile.listFiles()) {
                //删除日期小于等于指定时间的log
                if(item.getName().compareTo(time)<=0){
                    item.delete();
                }

            }
        }

        //删除crash文件夹里面日志crash-2016-10-25-15-50-2-1477381820513
        File crashFile = AppFileUtil.getPrivateFile(AppFileUtil.CrashFileName);
        if (crashFile!= null && crashFile.exists() && crashFile.isDirectory()) {
            for (File item : crashFile.listFiles()) {
                String name = item.getName();
                name = name.replace("-", "");
                name = name.substring(5, 13);

                //删除日期小于等于指定时间的log
                if(name.compareTo(time)<=0){
                    item.delete();
                }

            }
        }

//        //删除ANR文件夹里面日志ANR-2016-10-25-15-50-2-1477381820513
//        String anrFileName = fileName +"/ANR";
//        File anrFile = new File(anrFileName);
//        if (anrFile != null && anrFile.exists() && anrFile.isDirectory()) {
//            for (File item : anrFile.listFiles()) {
//                String name = item.getName();
//                name = name.replace("-", "");
//                name = name.substring(3, 11);
//                //删除日期小于等于指定时间的log
//                if(name.compareTo(time)<=0){
//                    item.delete();
//                }
//
//            }
//        }
    }
}