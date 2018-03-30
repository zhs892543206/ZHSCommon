package com.zhs.commonhelper.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;


/**
 * 缩放动画只支持RelativeLayout.LayoutParams，要支持其他就要 改
 */
public class RLyExpandAnimation extends Animation {

    private View mAnimationView = null;
    private RelativeLayout.LayoutParams mViewLayoutParams = null;
    private int mStart = 0;
    private int mEnd = 0;
    public int mDuration = 400;
    
    public RLyExpandAnimation(View view){
        animationSettings(view, mDuration);//动画持续时间
    }

    public RLyExpandAnimation(View view, int duration){
        animationSettings(view, duration);
    }

    private void animationSettings(View view, int duration){
        setDuration(duration);
        mAnimationView = view;
        mViewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        mStart = mViewLayoutParams.bottomMargin;
        mEnd = (mStart == 0 ? (0 - view.getHeight()) : 0);
        view.setVisibility(View.VISIBLE);

    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        
        if(interpolatedTime < 1.0f){
            mViewLayoutParams.bottomMargin = mStart + (int) ((mEnd - mStart) * interpolatedTime);
            // invalidate
            mAnimationView.requestLayout();
        }else{
            mViewLayoutParams.bottomMargin = mEnd;
            mAnimationView.requestLayout();
            if(mEnd != 0){
                mAnimationView.setVisibility(View.GONE);
            }
        }
    }

	@Override
	public void setAnimationListener(AnimationListener listener) {
		// TODO Auto-generated method stub
		super.setAnimationListener(listener);
	}
    
    
}