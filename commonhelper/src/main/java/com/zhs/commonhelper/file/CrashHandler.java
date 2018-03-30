package com.zhs.commonhelper.file;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.zhs.commonhelper.app.AppFileUtil;
import com.zhs.commonhelper.log.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	//系统默认的UncaughtException处理类 
	private UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static CrashHandler INSTANCE;
	//程序的Context对象
	private Context mContext;
	//用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	//用于格式化日期,作为日志文件名的一部分
	public DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public CrashCallBack crashCallBack;
	
	//private static String mAppPath;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler(CrashCallBack crashCallBack) {
		this.crashCallBack = crashCallBack;
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance(CrashCallBack crashCallBack) {
		if(INSTANCE ==null){
			new CrashHandler(crashCallBack);
		}
		//mAppPath = appPath;
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LogUtil.e("error : ", e);
			}
			
			//重启要放在退出程序前
			crashCallBack.restartApp();
			crashCallBack.endApp();
//			MyApplication.getInstance().onTerminate();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将重启.", Toast.LENGTH_LONG).show();//退出
				Looper.loop();
			}
		}.start();
		//收集设备参数信息 
		collectDeviceInfo(mContext);
		//保存日志文件 
		saveCrashInfo2File(ex);
		return true;
	}
	
	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			LogUtil.e("an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				//LogUtil.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				LogUtil.e("an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";//还是.log虽然有些手机需要该后缀才能看
			 LogUtil.e("程序异常请查看文件" + fileName);
			String path = AppFileUtil.getAppFilePath();
			path = path+"/" + AppFileUtil.CrashFileName + "/";

			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(path + fileName);
//			fos.write(sb.toString().getBytes());
//			fos.close();
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//防止中文乱码
			BufferedWriter bufferedWriter=new BufferedWriter(osw);
			bufferedWriter.write(sb.toString());
			bufferedWriter.flush();
			
//			/**
//		 * 写在写奔溃文件后面，防止奔溃下面语句也有问题，结果下面奔溃时，上面还是没有写成功，下面注释了就成功了
//		 * 太长截取一部分传了，数据库看了下该参数最多存长度500的字符串
//		 */
//		 if(result!=null && result.length()>400){
//           CommonUtil.exceptLog("4", "程序出现异常！"
//                   , result.substring(0, 400), 0);
//		   }else if(result!=null){
//			   CommonUtil.exceptLog("4", "程序出现异常！"
//					   , result, 0);
//		   }else{
//			   CommonUtil.exceptLog("4", "程序出现异常！"
//					   , "", 0);
//		   }
			crashCallBack.crashLog(result);
			return fileName;
		} catch (Exception e) {
			LogUtil.e("an error occured while writing file...", e);
		}
		
		
		return null;
	}

//	/**
//	 * 重启程序
//	 */
//	public void restartApp(){
////	        Intent intent = new Intent(mContext,WelcomeActivity.class);
////	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////	        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //该方法是关闭中途的所以activity前提你重启的activity是起源
////
////	        mContext.startActivity(intent);
//		Intent intent = new Intent( MyApplication.getInstance(), MainActivity.class);
//		PendingIntent restartIntent = PendingIntent.getActivity(
//				MyApplication.getInstance(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		//退出程序
//		AlarmManager mgr = (AlarmManager)MyApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
//		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//				restartIntent); // 1秒钟后重启应用
//
//
//	}
	/**
	 * 奔溃帮助类对外接口
	 */
	public interface CrashCallBack{
	 	public void restartApp();//重启程序
	 	public void endApp();//结束程序
		public void crashLog(String errorStr);//奔溃日志除了存入crash文件外的处理
	 }
}
