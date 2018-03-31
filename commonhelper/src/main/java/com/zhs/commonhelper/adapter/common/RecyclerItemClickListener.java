package com.zhs.commonhelper.adapter.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    
        public void OnItemLongClick(View view, int position);
    }
    GestureDetector mGestureDetector;

//    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
//        mListener = listener;
//        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//            @Override public boolean onSingleTapUp(MotionEvent e) {
//                return true;
//            }
//            
//        });
//    }
//    
    /**
     * 多了个长按监听
     * @param context
     * @param recyclerView
     * @param listener
     */
    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override
        	public  void onLongPress(MotionEvent event){
        		// 根据findChildViewUnder(float x, float y)来算出哪个item被选择了
        		View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());
        		// 有item被选则且监听器不为空触发长按事件
        		if (childView != null && mListener != null) {
        			mListener.OnItemLongClick(childView, recyclerView.getChildPosition(childView));
        		}
            }  
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

}