package com.zhs.commonhelper.adapter.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * recyclerview的通用适配器,每次使用新建个
 * @author zhs
 *
 * @param <T>
 */
public abstract class CommonRecyclerVAdapter<T> extends RecyclerView.Adapter<RecyclerVViewHolder>{

	/**
	 * 业务用的东西
	 */
	public TypedArray mColorArray;
	public int[] statusColors = new int[4];//各种状态的颜色
	//数据量比较多.ArrayMap替代HashMap。ArrayMap相比传统的HashMap速度要慢
	public Map<String, Integer> mWpflColorMap = new ArrayMap<String, Integer>();//存放所有物品的颜色
	

	
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
  
    public CommonRecyclerVAdapter(Context context, List<T> datasList, int layoutId) {
        this.mContext = context;  
        this.datasList = datasList;  
        this.layoutId = layoutId;
    }  
  
    /**
     * 创建新View，被LayoutManager所调用
      */
     @Override
     public RecyclerVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	 LayoutInflater layoutInflater = LayoutInflater.from(mContext);
         View view = layoutInflater.inflate(mContext.getResources().getLayout(layoutId), parent, false);
         RecyclerVViewHolder vh = new RecyclerVViewHolder(view);
         return vh;
     }
     /**
      * 将数据与界面进行绑定的操作
      */
     @Override
     public void onBindViewHolder(RecyclerVViewHolder holder,int position) {
    	 
         fillData(holder, position);  
     }

    /**
     * 获取数据的数量
     */
    @Override
    public int getItemCount() {
        return datasList.size();
    }
    /** 
     *  
     * 抽象方法，用于子类实现，填充数据 
     * @param holder 
     * @param position 
     */  
    protected abstract void fillData(RecyclerVViewHolder holder, int position);  
  
    //更新list并刷新,需要传入改变的范围，如果是改变所有，如情况情况，其实应该传入总长度，是只可见长度。平时刷新用notifyDataSetChanged就好。尽量不要改变List<T>的指针
    public void setMyModel(List<T> list, int start, int end){
    	//list和datasList指向一个地址，clear了就全没了
//    	datasList.clear();
//    	datasList.addAll(list);
//    	List<T> myList = new ArrayList<T>();
//    	myList.addAll(list);
//    	datasList.clear();
//    	datasList.addAll(myList);
//    	notifyItemRangeChanged(0, datasList.size());

    	datasList = list;
    	notifyItemRangeChanged(start, end);//用这个才有增加减少的动画
    	//notifyDataSetChanged();//如果notifyItemRangeChanged奔溃，可以用这个不过没动画，notifyall不行
    }
    //更新list并刷新
    public void setMyModel(List<T> list){
    	datasList = list;
    	notifyDataSetChanged();
    }


    public List<T> getMyModel(){
    	return datasList;
       
    }

    
}  
