package com.zhs.commonhelper.string;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.Locale;

//中文转拼音  
public class StringUtil {
	public static void main(String[] args) {
		String str = "zg318";
		str = getStringNoPrefix(str, "zg");
		System.out.print(str);
	}

	public static String getStringNoPrefix(String sStr, String prefix) {
		if (sStr.toUpperCase(Locale.ROOT).startsWith(prefix.toUpperCase(Locale.ROOT))) {
			return sStr.substring(prefix.length()).trim();
		} else {
			return sStr.trim();
		}
	}

	/**
	 * String转byte[]
	 * @param hexString
	 * @return
	 */
	public static byte[] hex2Byte(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.ROOT);
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) ((char2Byte(hexChars[pos]) << 4 | char2Byte(hexChars[pos + 1])) & 0xff);
		}
		return d;
	}

	public static byte char2Byte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 把一个字节串转换成一个字符串，用以界面显示
	 */
	public static String xGetString(byte[] bs)
	{
		if (bs != null)
		{
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < bs.length; i++)
			{
				sBuffer.append(String.format("%02x", bs[i]));
			}
			return sBuffer.toString();
		}
		return null;
	}

	/**
	 * 这个是用于数字的
	 * 把一个字符串转换成一个字节串，用以处理界面中的数据，以供实现使用
	 */
	public static byte[] getBytesByHexString(	String string)
	{
		string = string.replaceAll(" ", "");// 去掉空格
		int len = string.length();
		if (len % 2 == 1)
		{
			return null;
		}
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = (byte) (Integer.valueOf(string.substring((i * 2), (i * 2 + 2)), 16) & 0xff);
		}
		return ret;
	}	
	
	
	  /**
     * 字节转换为M
     */
    public static BigDecimal byteToKb(long value){
    	BigDecimal result = new BigDecimal(Long.toString(value));
    	result = result.divide(new BigDecimal("1024.0"));//.divide(new BigDecimal("1024.0"))
    	double resultD2 =result.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  //保留两位有效小数
    	return new BigDecimal(Double.toString(resultD2));
    }


	/**
	 * 判断手机格式是否正确
	 * @param mobiles
	 * @return
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189、（1349卫通）
	 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	 */
	public static boolean isMobileNO(String mobiles) {
		//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		String telRegex = "[1][34578]\\d{9}" ;
		if (TextUtils.isEmpty(mobiles)) return false ;
		else return mobiles.matches( telRegex ) ;
	}
}