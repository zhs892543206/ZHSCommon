package com.zhs.commonhelper.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.commonhelper.R;
import com.zhs.commonhelper.ZHSCommonApp;


/**
 * @author tjiang
 *
 */
public class ToastUtil {
	private static Toast mToast;
	public static void showToast(String string){
		showToast(string, Toast.LENGTH_SHORT);
	}
	/**
	 * 使用静态mToast，就不要传context。不然导致内存无法释放
	 * 通过Toast提示用户信息
	 */
	public static void showToast(String string, int type){
		
		if(mToast!=null){
			mToast.cancel();
		}
		LayoutInflater inflater = LayoutInflater.from(ZHSCommonApp.appContext);
        if (null == inflater) {
			return;
		}
        
		View view = inflater.inflate(R.layout.module_toast, null);
		if (null == view) {
			return;
		}
		
        TextView tv = (TextView) view.findViewById(R.id.module_toast_content);
        if (null == tv) {
			return;
		}
        
        tv.setText(string);
        
        //被cancel后，mToast虽然不是null但需要新建
	    mToast = new Toast(ZHSCommonApp.appContext);
        
        mToast.setView(view);
        if(type != -1){
        	mToast.setDuration(type);
        }
        mToast.show();
	}
	
	
	public static void showToast(int string,int type){
		if(mToast!=null){
			mToast.cancel();
		}
		
		LayoutInflater inflater = LayoutInflater.from(ZHSCommonApp.appContext);
        if (null == inflater) {
			return;
		}
        
		View view = inflater.inflate(R.layout.module_toast, null);
		if (null == view) {
			return;
		}
		
        TextView tv = (TextView) view.findViewById(R.id.module_toast_content);
        if (null == tv) {
			return;
		}
        
        tv.setText(string);
        
      //被cancel后，mToast虽然不是null但需要新建
	    mToast = new Toast(ZHSCommonApp.appContext);
       
        mToast.setView(view);
        if(type != -1){
	    mToast.setDuration(type);
        }
        mToast.show();
	}


	public static void canclToast(){
		if(mToast!=null){
			mToast.cancel();
		}
	}
	
	
	
	public static void showToast(Context context, String string, int type){

		LayoutInflater inflater = LayoutInflater.from(ZHSCommonApp.appContext);
        if (null == inflater) {
			return;
		}
        
		View view = inflater.inflate(R.layout.module_toast, null);
		if (null == view) {
			return;
		}
		
        TextView tv = (TextView) view.findViewById(R.id.module_toast_content);
        if (null == tv) {
			return;
		}
        
        tv.setText(string);
        
        //被cancel后，mToast虽然不是null但需要新建
	    Toast toast = new Toast(ZHSCommonApp.appContext);
        
	    toast.setView(view);
        if(type != -1){
        	toast.setDuration(type);
        }
        toast.show();
	}
	
	
	public static void showToast(Context context, int string, int type){
		LayoutInflater inflater = LayoutInflater.from(ZHSCommonApp.appContext);
        if (null == inflater) {
			return;
		}
        
		View view = inflater.inflate(R.layout.module_toast, null);
		if (null == view) {
			return;
		}
		
        TextView tv = (TextView) view.findViewById(R.id.module_toast_content);
        if (null == tv) {
			return;
		}
        
        tv.setText(string);
        
      //被cancel后，mToast虽然不是null但需要新建
        Toast toast = new Toast(ZHSCommonApp.appContext);
       
        toast.setView(view);
        if(type != -1){
        	toast.setDuration(type);
        }
        toast.show();
	}
}
