package com.zhs.commonhelper.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 一个文字消失,到完全消失是目标文字慢慢显示，时间都是mDuration的一半
 * 目标文字不设置就是文字之间消失的动画时间是mDuration
 *透明度动画
 */
public class TextAlphaAnimation extends Animation {

    private View mAnimationView = null;
    private RelativeLayout.LayoutParams mViewLayoutParams = null;
    private int mStart = 0;
    private int mEnd = 0;
    public int mDuration = 400;//动画时间
    private String targetStr = "";//目标字符串
    public boolean isToVis = false;//当targetstr为空时，该属性控制文字变成可见。默认是false。

    public TextAlphaAnimation(View view, String targetStr){
        animationSettings(view, mDuration,targetStr);//动画持续时间

    }

    public TextAlphaAnimation(View view, int duration, String targetStr){
        animationSettings(view, duration, targetStr);
    }

    private void animationSettings(View view, int duration, String targetStr){
        setDuration(duration);
        mAnimationView = view;
        this.targetStr = targetStr;

    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if(targetStr!=null && targetStr.length()>0) {
            if (interpolatedTime < 0.5f) {
                mAnimationView.setAlpha(1 - 2 * interpolatedTime);

                // invalidate
                mAnimationView.requestLayout();

            } else if (interpolatedTime >= 0.5f && interpolatedTime < 1.0f) {
                //如果是文本视图
                if(mAnimationView instanceof TextView) {
                    if (!((TextView) mAnimationView).getText().toString().equals(targetStr)) {
                        ((TextView) mAnimationView).setText(targetStr);
                    }
                }
                mAnimationView.setAlpha(2 * (interpolatedTime - 0.5f));
            }
        }else{
            if(isToVis){
                //变到可见，要先设置显示
                mAnimationView.setVisibility(View.VISIBLE);
                mAnimationView.setAlpha(interpolatedTime);

                //变到可见的时候，如果动画结束时间则设置可点击
                if(interpolatedTime>=1.0f){
                    mAnimationView.setClickable(true);
                }
            }else {

                mAnimationView.setAlpha(1 - interpolatedTime);
                //设置不可见，当结束时要设置gone这样才不能点击
                if(interpolatedTime>=1.0f){
                    mAnimationView.setVisibility(View.GONE);
                }
                //设置不可见，当开始时，设置不可点击
                if(interpolatedTime<=0.0f){
                    mAnimationView.setClickable(false);
                }
            }

        }
    }

	@Override
	public void setAnimationListener(AnimationListener listener) {
		// TODO Auto-generated method stub
		super.setAnimationListener(listener);
	}
    
    
}