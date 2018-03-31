package com.zhs.commonhelper.widget.fastsearch.searchedt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.zhs.commonhelper.R;


/**
 * 自定义的搜索框
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-08  18:20
 */

public class SearchEditText extends EditText {
    private static final String TAG = "SearchEditText";

    /**
     * 是否点击软键盘搜索
     */
    private boolean pressSearch = false;
    /**
     * 软键盘搜索键监听
     */
    private OnSearchClickListener listener;

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    private int viewWidth;
    private int viewHeight;
    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.listener = listener;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view, String keyword);
    }

    public SearchEditText(Context context) {
        this(context, null);
    }


    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //当宽度变化，说明是布局画好了，计算出高度了
        if(viewWidth!=getMeasuredWidth()) {
            //px
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
            init();
        }
    }
    private void init() {
        int rightDrawablePadding = getResources().getDimensionPixelSize(R.dimen.widget_margin);
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.mipmap.search_gray);
        }

        /**
         *  第一个属性只是为了给右边加点边距，则第三个属性需要加等量的值。三四属性默认-rightDrawablePadding是为了让图片小点
         *  rightDrawablePadding*2之所以乘2是因为有上下变个边距
         */
        mClearDrawable.setBounds(0, 0, (int) viewHeight-rightDrawablePadding*2, viewHeight-rightDrawablePadding*2);
        setClearIconVisible(true);
    }
    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        super.onTextChanged(s, start, count, after);
        if(listener!=null){
            listener.onSearchClick(this, s.toString());
        }
    }
}
