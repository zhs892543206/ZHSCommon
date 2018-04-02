package com.zhs.commonhelper.log;

import android.util.Log;

import com.zhs.commonhelper.app.AppFileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

/**
 * 公共的日志类
 * 现在的一些自定义widget和部分帮助类是依然是原来自己的log
 *
 * stacktrace[i].toString();//全部信息
 stacktrace[i].getClassName();//类名
 stacktrace[i].getFileName();//文件名
 stacktrace[i].getLineNumber();//代码位于第几行
 stacktrace[i].getMethodName();//方法名
 
  Log.e在message为空或长度0时是不会有输出的所以
 if(message==null || message.length()<=0){
 message = tag;
 }
 */
public class LogUtil {

	public static boolean isPrint = true;//false是打印日志，true是不打印
	private static boolean isWriteFile = true;//true是写入到手机内存，false不写入手机内存
	private static String dirPath;//保存路径
	private static FileOutputStream fos;

    public static String getTag(){
        //0,1是vm,和thread调用的方法，从2开始才是我们调用的方法。例如这里gettag方法这里是2，调用gettag的比如e()是3。调用LogUtil.e()的地方是4
        StackTraceElement tagStack = Thread.currentThread().getStackTrace()[4];//
        String tag = tagStack.getClassName() + "->" + tagStack.getMethodName()+"->"+tagStack.getLineNumber();;
        return tag;
    }

	public static void e(String message, Exception exception){
        String tag = getTag();
        if(isPrint){
            Log.e(tag, message+ "\n"+ exception.toString());
        }

        if(isWriteFile){
            writeLog2File(tag, message+ "\n"+ exception.toString());
        }
	}

	public static void d(String message){
        String tag = getTag();
		if(isPrint){
            if(message==null || message.length()<=0){
                message = tag;
            }
			Log.d(tag, message);
		}

		if(isWriteFile){
            writeLog2File(tag, message);
		}
	}

    public static void i(String message){
        String tag = getTag();
        if(isPrint){
            if(message==null || message.length()<=0){
                message = tag;
            }
            Log.i(tag, message);
        }

        if(isWriteFile){
            writeLog2File(tag, message);
        }
    }

    public static void e(String message){
        String tag = getTag();
        if(isPrint){
            if(message==null || message.length()<=0){
                message = tag;
            }
            Log.e(tag, message);
        }

        if(isWriteFile){
            writeLog2File(tag, message);
        }
    }

    public static void w(String message){
        String tag = getTag();
        if(isPrint){
            if(message==null || message.length()<=0){
                message = tag;
            }
            Log.w(tag, message);
        }

        if(isWriteFile){
            writeLog2File(tag, message);
        }
    }

    public static void v(String message){
        String tag = getTag();
        if(isPrint){
            if(message==null || message.length()<=0){
                message = tag;
            }
            Log.v(tag, message);
        }

        if(isWriteFile){
            writeLog2File(tag, message);
        }
    }



    /**
     * 将log写入手机文件夹
     * @param tag
     * @param message
     */
    private static void writeLog2File(String tag, String message){
        message = tag + "\r\n"+ message;
        String msg = genLogInfo(message);
        performWriteLog(msg);
    }


	//格式化日志信息
	private static String genLogInfo(String message){
		return FormatDate.getFormatTime()+":"+message+"\r\n";
	}
	
	/**
	 * 获取当前日期
	 * @author zhs
	 *
	 */
	private static class FormatDate{
		
		public static String getFormatDate(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//"yyyyMMddHH"我把小时去掉了
			return sdf.format(System.currentTimeMillis());
		}
		
		public static String getFormatTime(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(System.currentTimeMillis());
		}
	}

	/**
	 * 获取文件路径dirpath，没有则创建
	 */
	private static void dirPathCheck() {
		File dir = AppFileUtil.getPrivateFile(AppFileUtil.LogFileName);
		if(dir!=null) {
			dirPath = dir.getPath();
			if (!dir.exists()) {
				boolean result = dir.mkdirs();
				if (!result) {

				}
			}
		}
	}
	
	
	/**
	 * log文件不存在，新建log文件，并获取文件输出流
	 * @param dirPath
	 */
	public static void getOutputStream(String dirPath) {

		try {
			File file = new File(dirPath, FormatDate.getFormatDate()+".txt");
			if(!file.exists()){
				file.createNewFile();
			}
			fos = new FileOutputStream(file,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将log日志写入文件
	 * 执行写入log请执行performWriteLog方法
	 * @param logStr
	 */
	public static void writeLog(String logStr){
		if(fos != null && logStr!=null){
			try {
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//防止中文乱码,如果发现还是乱码可能是用来打开的日志文件的软件有问题
				BufferedWriter writer=new BufferedWriter(osw);
				writer.write(logStr);
				writer.flush();
				//fos.write((logStr).getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{	
//				try {
//					if(fos != null){
//						fos.close();
//						fos = null;
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

			}
		}
		
	}
	
	/**
	 * 执行写入log都执行这个方法
	 * @param logStr
	 */
	public static void performWriteLog(String logStr){
		dirPathCheck();
		if(dirPath!=null && dirPath.length()>0) {
			getOutputStream(dirPath);
			writeLog(logStr);
		}
	}
}
