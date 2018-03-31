package com.zhs.commonhelper.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.zhs.commonhelper.ZHSCommonApp;
import com.zhs.commonhelper.log.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class SystemUtil extends Activity {
	private final String TAG = "CQM";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					ZHSCommonApp.appContext.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			LogUtil.e("getVerCode error:"+e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					ZHSCommonApp.appContext.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			LogUtil.e("getVerName error:"+e.getMessage());
		}
		return verName;
	}
//	// IMEI号，IESI号，手机型号：
//	private void getInfo() {
//		TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//		String imei = mTm.getDeviceId();
//		String imsi = mTm.getSubscriberId();
//		String mtype = android.os.Build.MODEL; // 手机型号
//		String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
//	}

	// 获取手机屏幕高度：
	private void getWeithAndHeight() {
		// 这种方式在service中无法使用，
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 宽
		int height = dm.heightPixels; // 高

		// 在service中也能得到高和宽
		WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = mWindowManager.getDefaultDisplay().getWidth();
		height = mWindowManager.getDefaultDisplay().getHeight();
	}

	/**
	 * 获取状态栏高度
	 *
	 * @return
	 */
	public static int getstatusHeight() {
		/**
		 * 获取状态栏高度——方法1
		 */
		int statusBarHeight1 = -1;
		// 获取status_bar_height资源的ID
		int resourceId = ZHSCommonApp.appContext.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = ZHSCommonApp.appContext.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight1;
	}

	// 获取手机MAC地址：
	private String getMacAddress(Context context) {
		String result = "";
		WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		LogUtil.e("macAdd:" + result);
		return result;
	}

	// 手机CPU信息
	private String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		LogUtil.e("cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
		return cpuInfo;
	}

	// 获取手机可用内存和总内存：
	private String[] getTotalMemory() {
		String[] result = { "", "" }; // 1-total 2-avail
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		long mTotalMem = 0;
		long mAvailMem = mi.availMem;
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			mTotalMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		result[0] = Formatter.formatFileSize(this, mTotalMem);
		result[1] = Formatter.formatFileSize(this, mAvailMem);
		LogUtil.e("meminfo total:" + result[0] + " used:" + result[1]);
		return result;
	}

	// 获取手机安装的应用信息（排除系统自带）：
	private String getAllApp() {
		String result = "";
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				result += i.applicationInfo.loadLabel(getPackageManager()).toString() + ",";
			}
		}
		return result.substring(0, result.length() - 1);
	}

	public static final String OS_ANDROID = "Android";

	/**
	 * 获取当前手机语言设置类别，调用android的local类实现
	 */
	public static final String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取当前手机语言设置类别，调用android的local类实现 中文简体与繁体是通过countryCode来区分
	 */
	public static final String getCountryCode() {
		return Locale.getDefault().getCountry();
	}

	/**
	 * 获取手机品牌
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机型号
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取操作系统型号
	 */
	public static String getOS() {
		return OS_ANDROID;
	}

	/**
	 * 获取操作系统版本
	 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取当前系统时间，单位：毫秒
	 */
	public static long getCurTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前系统的SDK版本
	 */
	public static int getSdkLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 版本名
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取应用名
	 *
	 * @return
	 */
	public static String getAppName(Context context) {
		try {
			PackageInfo pkg = context.getPackageManager()
					.getPackageInfo(context.getApplicationContext().getPackageName(), 0);

			String appName = pkg.applicationInfo.loadLabel(context.getPackageManager()).toString();
			return appName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 版本号
	 *
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

	/**
	 * 获取archiveFilePath该文件的版本号
	 *
	 * @param context
	 * @param archiveFilePath
	 *            为apk的路径
	 * @return 版本号
	 */
	public static String getApkVersion(Context context, String archiveFilePath) {
		if (null == context) {
			return null;
		}

		PackageManager pkgManager = context.getPackageManager();
		if (null == pkgManager) {
			return null;
		}

		try {
			PackageInfo info = pkgManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
			if (null == info) {
				return null;
			}

			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 安装APK
	 */
	@SuppressWarnings("unused")
	public static Intent getInstallApkIntent(String path) {
		if (null == path || path.length() <= 0) {
			return null;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (null == intent) {
			return null;
		}

		intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");

		return intent;
	}

	/**
	 * 获取粘贴板管理类
	 */
	public static ClipboardManager getSystemClipboardManager(Context context) {
		ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		if (null == manager) {
			return null;
		}

		return manager;
	}

	/**
	 * 判断是否为平板
	 *
	 * @return
	 */
	public static boolean isPad(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		float screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取ip地址 ip在网络能通的时候肯定能获取的只是2,3,4g和wife这两种的区别。
	 *
	 * @return
	 */
	public static String getIp(Context context) {

		{// 获取 Wifi IP的方法

			WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

			if (wifiManager.isWifiEnabled() && wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (wifiInfo != null) {
					int ipAddress = wifiInfo.getIpAddress();
					if (ipAddress == 0)
						return "";
					return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "."
							+ (ipAddress >> 24 & 0xff));
				}
			}
		}

		// {//获取 以太网ip 的方法, 需要源码环境编译, 文章后面有下载地址;
		// ConnectivityManager connectivityManager = (ConnectivityManager) this
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// LinkProperties properties = connectivityManager
		// .getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
		// if (properties != null) {
		// String ipString = properties.getAddresses().toString();
		//
		// Pattern pattern = Pattern.compile("\\d+.\\d+.\\d+.\\d+");
		// Matcher matcher = pattern.matcher(ipString);
		// if (matcher.find()) {
		// return matcher.group();
		// }
		// }
		// }

		{// 获取 2,3,4G网络 ip的方法
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
						.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						// 这里需要注意：这里增加了一个限定条件( inetAddress instanceof
						// Inet4Address
						// ),主要是在Android4.0高版本中可能优先得到的是IPv6的地址。参考：http://blog.csdn.net/stormwy/article/details/8832164
						if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException ex) {
				LogUtil.e("获取 2,3,4G网络 ip 失败", ex);
			}
		}
		return "";

	}

	/**
	 * IMEI可以通过烧写变化，而且山寨机的imei可能就是15个0 需要
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	 * 手机如果是权限没设置允许，设置提问也不行估计
	 * 权限用户不给返回null，我转换为返回""了 网上还有一种方法是获取WLAN当也需要相应权限，而且可能被伪造。
	 * 还有一种是把各种信息进行拼接，产生的是32位的16进制字符串。这样就肯定有返回值了
	 *
	 * @return
	 */
	public static String getIMEI(Context context) {

		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
		if (TelephonyMgr == null) {
			return "";
		}
		String szImei = TelephonyMgr.getDeviceId();
		if (szImei == null) {
			return "";
		}
		return szImei;
	}

	public static String getDeviceOnlyId(Context context){
		 //The IMEI: 仅仅只对Android手机有效.采用此种方法，需要在AndroidManifest.xml中加入一个许可：android.permission.READ_PHONE_STATE，并且用户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356（除非你有一个没有量产的手机（水货）它可能有无效的IMEI，如：0000000000000）。

		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

		//通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。但应当明白的是，出现类似情况的可能性基本可以忽略
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI
		Build.BOARD.length()%10+ Build.BRAND.length()%10 + Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + Build.DISPLAY.length()%10 + Build.HOST.length()%10 + Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + Build.TAGS.length()%10 + Build.TYPE.length()%10 + Build.USER.length()%10 ; //13 digits

		//The Android ID  , 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。并且，如果某个Andorid手机被Root过的话，这个ID也可以被任意改变。
		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		//The WLAN MAC Address string, 是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE 权限，否则这个地址会为null。
		WifiManager wm = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		//The BT MAC Address string, 只在有蓝牙的设备上运行。并且要加入android.permission.BLUETOOTH 权限.Returns: 43:25:78:50:93:38 . 蓝牙没有必要打开，也能读取。
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = m_BluetoothAdapter.getAddress();

		String m_szLongID = m_szImei + m_szDevIDShort
				+ m_szAndroidID+ m_szWLANMAC + m_szBTMAC;
			// compute md5
		MessageDigest m = null;
		try {
			 m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(),0,m_szLongID.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i=0;i<p_md5Data.length;i++) {
		     int b =  (0xFF & p_md5Data[i]);
		// if it is a single digit, make sure it have 0 in front (proper padding)
		    if (b <= 0xF)
		        m_szUniqueID+="0";
		// add number to string
		    m_szUniqueID+= Integer.toHexString(b);
		}   // hex string to uppercase
		m_szUniqueID = m_szUniqueID.toUpperCase(Locale.ROOT);

		return m_szUniqueID;
	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public static boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) ZHSCommonApp.appContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = ZHSCommonApp.appContext.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/** 获取虚拟功能键高度 */
	public static int getVirtualBarHeigh(Context context) {
		int vh = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		try {
			@SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vh;
	}

	/**
	 * 获取服务是否在运行,只能判断主进程的服务
	 *
	 * @param serviceClass
	 *            传入什么.class就好
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if (serviceList == null || serviceList.size() == 0)
			return false;
		for (ActivityManager.RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(serviceClass.getName()))
				return true;
		}
		return false;
	}

	/**
	 * 设置在休眠情况下网络不断开，但其实17版本以后已经没用了
	 * http://blog.csdn.net/wwwwap2008/article/details/51783138
	 * 所以只能手动在wife的高级里设置，休眠情况下保持网络连接
	 * @param mContext
	 */
	public static void WifiNeverDormancy(Context mContext) {
		ContentResolver resolver = mContext.getContentResolver();

		int value = Settings.System.getInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
				Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

		Editor editor = prefs.edit();
		editor.putInt("wifeNerver", value);

		editor.commit();
		if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
			Settings.System.putInt(resolver, Settings.System.WIFI_SLEEP_POLICY,
					Settings.System.WIFI_SLEEP_POLICY_NEVER);

		}
		System.out.println("wifi value:" + value);
	}

	/**
     * 是否开启 wifi true：开启 false：关闭
     *
     * 一定要加入权限： <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
     *
     *
     * @param isEnable
     */
    public static void setWifi(Context context, boolean isEnable) {

    	WifiManager mWm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        System.out.println("wifi===="+mWm.isWifiEnabled());
        if (isEnable) {// 开启wifi

            if (!mWm.isWifiEnabled()) {

                mWm.setWifiEnabled(true);

            }
        } else {
            // 关闭 wifi
            if (mWm.isWifiEnabled()) {
                mWm.setWifiEnabled(false);
            }
        }

    }

    /**
     * 设置手机的移动数据
     *需要系统权限
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />，

<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
     *
     */
    public static void setMobileData(Context context, boolean pBoolean) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;

            /**
             * 放射获取方法
             * 在5.0上都不行了，
             * Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
        	method.invoke(mConnectivityManager, pBoolean);
        	因为变成两个参数了，多传了个包信息
        	不过测试发现还是不行
             */
            Method method = ownerClass.getMethod("setMobileDataEnabled",String.class,boolean.class);

            method.invoke(mConnectivityManager, context.getPackageName(), pBoolean);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("移动数据设置错误: " + e.toString());
        }
    }


    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg
     *            默认填null
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {

        try {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            Class ownerClass = mConnectivityManager.getClass();

            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }

            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);

            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

            return isOpen;

        } catch (Exception e) {
            // TODO: handle exception

            System.out.println("得到移动数据状态出错");
            return false;
        }

    }

    /**
     * 是否插入sim卡
     * @param context
     * @return true 有sim卡
     */
    public static boolean getIsSim(Context context){
    	TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String simSer = tm.getSimSerialNumber();
        if(simSer == null || simSer.equals("")) {
        	return false;
        }else{
        	return true;
        }
    }

    /**
     * 获取屏幕尺寸
     * @param context
     * @return
     */
    public static double getScreenSizeOfDevice2(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double x = Math.pow(point.x/ dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

    /**
     * 删除当前应用的桌面快捷方式
     *
     * @param cx
     */
    public static void delShortcut(Context cx, int appNameId, int imgId, Class<? extends Activity> startActivity) {
        Intent shortcut = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, cx.getString(appNameId));
		// 图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(cx,
						imgId));

		// 设置关联程序
		Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
		launcherIntent.setClass(cx, startActivity);
		launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		shortcut
				.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        cx.sendBroadcast(shortcut);
    }


    /**
     * 在桌面添加快捷方式
     * @param context
     */
    public static void addShortcut(Context context, int appNameId, int imgId, Class<? extends Activity> startActivity) {
    	String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", true);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(appNameId));
        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", true);
        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
						imgId));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(context, startActivity);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        context.sendBroadcast(addShortcutIntent);
    }

   /**
    * list深复制
    * 使用List<Person> destList=deepCopy(srcList);
    * @param src
    * @return
    * @throws IOException
    * @throws ClassNotFoundException
    */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }


    /**
     * 判断service是否已经运行
     * 必须判断uid,因为可能有重名的Service,所以要找自己程序的Service,不同进程只要是同一个程序就是同一个uid,个人理解android系统中一个程序就是一个用户
     * 用pid替换uid进行判断强烈不建议,因为如果是远程Service的话,主进程的pid和远程Service的pid不是一个值,在主进程调用该方法会导致Service即使已经运行也会认为没有运行
     * 如果Service和主进程是一个进程的话,用pid不会出错,但是这种方法强烈不建议,如果你后来把Service改成了远程Service,这时候判断就出错了
     *
     * @param className Service的全名,例如PushService.class.getName()
     * @return true:Service已运行 false:Service未运行
     */
    public static boolean isServiceExisted(String className, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = am.getRunningServices(Integer.MAX_VALUE);
        int myUid = android.os.Process.myUid();
        for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceList) {
            if (runningServiceInfo.uid == myUid && runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否在主进程,这个方法判断进程名或者pid都可以,如果进程名一样那pid肯定也一样
     *
     * @return true:当前进程是主进程 false:当前进程不是主进程
     */
    public boolean isUIProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置系统时间
     * 需要权限
     * @param dateStr
     */
    public static void setSystemDate(String dateStr)
    {
        try {
            LogUtil.e("开始设置setSystemDate");
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String writeStr = "date -s "+ dateStr +"; \n";
            os.writeBytes(writeStr);
            LogUtil.e(writeStr);
        } catch (Exception e) {
            LogUtil.e("error=="+e.toString());
            e.printStackTrace();
        }
    }

}