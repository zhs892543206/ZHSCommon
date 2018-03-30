package com.zhs.commonhelper.app;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by hszhang on 2016/3/9.
 */
public class KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 需要配置里activity属性android:windowSoftInputMode="adjustPan|stateHidden|stateUnchanged"
     * @param mContext
     */
    public static void closeKeybord(Context mContext)
    {
        View view = ((Activity)mContext).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    //只要传入父view就内监听其和所有费edt的子控件，来点击关闭键盘
    public static void setupUI(View view, final Context mContext) {
    	//Set up touch listener for non-text box views to hide keyboard.
    	if(!(view instanceof EditText)) {
    	view.setOnTouchListener(new OnTouchListener() {
    	     public boolean onTouch(View v, MotionEvent event) {
    	            closeKeybord(mContext);
    	             return false;
    	           }
    	         });
    	    }
    	
    	         //If a layout container, iterate over children and seed recursion.
    	      if (view instanceof ViewGroup) {
    	            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
    	                 View innerView = ((ViewGroup) view).getChildAt(i);
    	                setupUI(innerView, mContext);
    	            }
    	         }
    	     }
}
