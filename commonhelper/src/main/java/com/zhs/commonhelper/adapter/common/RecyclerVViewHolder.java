package com.zhs.commonhelper.adapter.common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/** 
 *  
 * recyclerview的通用容器
 *  
 */  
public class RecyclerVViewHolder  extends RecyclerView.ViewHolder{
      
    /** 
     * View容器，用于存放Holer中的View 
     * 照顾下小白 SparseArray 是Android推荐使用的一个优化容器，相当于一个Map<integer,View>   
     */  
    private SparseArray<View> mViews;
      
    /** 
     * Item布局View convertView 
     */  
    private View mConvertView;

  
    public RecyclerVViewHolder(View view){
		super(view);
		mViews = new SparseArray<View>();
		mConvertView = view;
		mConvertView.setTag(this);  
		
	}

    /** 
     * 获取ViewHolder 

     * @return 
     */  
    public static RecyclerVViewHolder getViewHolder(View view) {
  
        if (view == null)  
            return new RecyclerVViewHolder(view);  
        return (RecyclerVViewHolder) view.getTag();  
    }  
  
    /** 
     * 获取Holder中的ItemView 
     *  
     * @param viewId 
     * @return 
     */  
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
  
        View item = mViews.get(viewId);
        if (item == null) {  
            item = mConvertView.findViewById(viewId);  
            mViews.put(viewId, item);  
        }  
        return (T) item;  
    }  
  
    /** 
     * 获取convertView 
     *  
     * @return 
     */  
    public View getMConvertView() {
        return mConvertView;  
    }

}  