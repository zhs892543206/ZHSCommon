package com.zhs.commonhelper.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
	//大小写影响很大的小心点
	public final static String DATE_FORMAT_SYSTEM = "yyyyMMdd.HHmmss";//android系统时间设置用到20120419.024012
	public final static String DATE1 = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE2 = "yyyyMMdd";
	public final static String DATE3 = "yyyy-MM-dd";
	public final static String DATE4 = "MM-dd HH:mm:ss";
	public final static String DATE5 = "MM-dd HH:mm";
	public final static String DATE6 = "yyyy年MM月dd日 HH:mm";
	/**
	 HH是24小时制度，hh是12小时制度
	 * 为了便于比较大小，转换为20150621的long类型
	 * @param date
	 * @return
	 */
	public static long fromDate2Long(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			return Long.valueOf(sdf.format(date));
		} catch (Exception e) {
			return -1;
		}
	}

	public static String dateToStryyyyMMdd(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static Date fromLong2Date(long date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			return sdf.parse(Long.toString(date));
		} catch (Exception e) {
			return null;
		}
	}

	public static String fromDate2YYYYMMDDHHMMSS(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE1, Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static String fromDate2MMDDHHMMSS(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date fromYYMMDDHHMMSS2Date(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	public static String fromDate2YYYYMMDD(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}



	/**
	 * 将Date转成字符串,字符串格式"yyyyMMdd.HHmmss"
	 */
	public static String dateToSystemStr(Date date){
		if(null == date){
			return null;
		}

		SimpleDateFormat dateFromat = new SimpleDateFormat(DATE_FORMAT_SYSTEM, Locale.getDefault());

		String str = null;
		try{
			str = dateFromat.format(date);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	//获取本周第一天
	public static Date getFirstDayOnWeek(){
		Calendar calendar = Calendar.getInstance();
		int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
		int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
		calendar.add(Calendar.DAY_OF_WEEK, min-current+1); //当天-基准，获取周开始日期
		return calendar.getTime();
	}

	//获取本周最后一天
	public static Date getLastDayOnWeek(){
		Calendar calendar = Calendar.getInstance();
		int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
		int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
		calendar.add(Calendar.DAY_OF_WEEK, min-current+1); //当天-基准，获取周开始日期
		calendar.add(Calendar.DAY_OF_WEEK, 6); //开始+6，获取周结束日期
		return calendar.getTime();
	}

	//获取本月第一天
	public static Date getFirstDayOnMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		return calendar.getTime();
	}

	//获取本月最后一天
	public static Date getLastDayOnMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	//获取本年第一天
	public static Date getFirstDayOnYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 0);
		calendar.set(Calendar.DAY_OF_YEAR,1);
		return calendar.getTime();
	}

	//获取本年最后一天
	public static Date getLastDayOnYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		return calendar.getTime();
	}

	//获取当前时间好像date传过去是否24制度看服务器
	public static Date getSysdate(){
		return new Date();
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//设置日期格式
		//return fromYYMMDDHHMMSS2Date(df.format(new Date()));
	}

	//获取当前时间
	public static String getSysdateYYYYMMDDHHMMSS(){

		SimpleDateFormat df = new SimpleDateFormat(DATE1, Locale.getDefault());//设置日期格式
		return df.format(new Date());
	}

	//格式化时间
	public static String formatDate(Date date, String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 获取昨天时间
	 * @return
	 */
	public static String getFormatToday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());//"yyyyMMddHH"我把小时去掉了
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * 获取昨天时间
	 * @return
	 */
	public static String getFormatYesterday(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());//"yyyyMMddHH"我把小时去掉了
		return sdf.format(System.currentTimeMillis()-24*60*60*1000);
	}

	/**
	 * 获取今天的day后时间，负数往过去时间
	 * format转换为字符串的格式
	 * @return
	 */
	public static String getOtherDayStr(int day, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());//"yyyyMMddHH"我把小时去掉了
		return sdf.format(System.currentTimeMillis() + day*24*60*60*1000);
	}

	/**
	 * 获取dateStr的day后时间，负数往过去时间
	 * format转换为字符串的格式
	 * @return
	 */
	public static String getOtherDayStr(String dateStr, int day, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);//"yyyyMMddHH"我把小时去掉了
		Date date = fromString2Date(dateStr, format);
		return sdf.format(date.getTime() + day*24*60*60*1000);
	}

	/**
	 * string转date
	 * @param date
	 * @param type
	 * @return
	 */
	public static Date fromString2Date(String date, String type){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.getDefault());
			return sdf.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * date转string
	 * @param date
	 * @param type
	 * @return
	 */
	public static String fromDate2String(Date date, String type){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将时间字符串从fromType转换为toType格式
	 * @param dateStr
	 * @param fromType
	 * @param toType
	 * @return
	 */
	public static String fromString2String(String dateStr, String fromType, String toType){
		Date date;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fromType, Locale.getDefault());
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			date = null;
			return  dateStr;
		}

		return fromDate2String(date, toType);
	}
}
