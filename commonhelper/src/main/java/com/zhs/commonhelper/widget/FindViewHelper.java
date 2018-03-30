package com.zhs.commonhelper.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 控件使用帮助类
 *
 * @author hezutao@fengmap.com
 * @version 2.0.0
 */
public class FindViewHelper {

    /**
     * 获取控件
     *
     * @param activity Activity
     * @param id       控件id
     * @param <T>      控件继承于View
     * @return id对应的控件
     */
    public static <T extends View> T getView(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    /**
     * 获取控件
     *
     * @param view 视图
     * @param id   控件id
     * @param <T>  继承于View
     * @return id对应的控件
     */
    public static <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    /**
     * 设置控件的点击事件
     *
     * @param activity Activity
     * @param id       控件id
     * @param listener 点击监听事件
     */
    public static <T extends View> T  setViewClickListener(Activity activity, int id, View.OnClickListener listener) {
        View view = getView(activity, id);
        view.setOnClickListener(listener);
        return (T)view;
    }

    public static <T extends View> T setViewClickListener(Context context, int id, View.OnClickListener listener) {
        View view = getView((Activity)context, id);
        view.setOnClickListener(listener);
        return (T)view;
    }

    /**
     * 设置控件文字
     *
     * @param activity Activity
     * @param id       控件id
     * @param text     文字
     */
    public static <T extends View> T setViewText(Activity activity, int id, String text) {
        TextView view = getView(activity, id);
        if(view!=null) {
            view.setText(text);
        }
        return (T)view;
    }

    /**
     * 设置控件是否可用
     *
     * @param activity Activity
     * @param id       控件id
     * @param enabled  true 可以使用 false 不可使用
     */
    public static <T extends View> T setViewEnable(Activity activity, int id, boolean enabled) {
        View view = getView(activity, id);
        view.setEnabled(enabled);
        return (T)view;
    }

}
