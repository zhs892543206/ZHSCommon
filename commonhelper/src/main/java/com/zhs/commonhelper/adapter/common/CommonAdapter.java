package com.zhs.commonhelper.adapter.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * listview的通用适配器
 * @author zhs
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	  
    /** 
     * 上下文 
     */  
    protected Context mContext;
    /** 
     * 数据源 
     */  
    protected List<T> datasList;
    /** 
     * Item布局ID 
     */  
    protected int layoutId;  
  
    public CommonAdapter(Context context, List<T> datasList, int layoutId) {
        this.mContext = context;  
        this.datasList = datasList;  
        this.layoutId = layoutId;  
    }  
  
    @Override
    public int getCount() {  
        return datasList == null ? 0 : datasList.size();  
    }  
  
    @Override
    /** 
     * 获取当前点击的Item的数据时用 
     * 在onItemClick中 parent.getAdapter().getItem(),获取当前点击的Item的数据 
     */  
    public Object getItem(int position) {
        return datasList.get(position);  
    }  
  
    @Override
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override
    /** 
     * 只关心这一个方法 
     */  
    public View getView(int position, View convertView, ViewGroup parent) {
   
        ListViewHolder holder = ListViewHolder.getViewHolder(mContext, convertView,  
                parent, layoutId);  
        fillData(holder, position);  
        return holder.getMConvertView();  
    }  
  
    /** 
     *  
     * 抽象方法，用于子类实现，填充数据 
     * @param holder 
     * @param position 
     */  
    protected abstract void fillData(ListViewHolder holder, int position);  
  
    //更新list并刷新
    public void setMyModel(List<T> list){
    	datasList = list;
        notifyDataSetChanged();
       
    }

    public List<T> getMyModel(){
    	return datasList;
       
    }
}  
