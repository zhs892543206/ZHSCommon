package com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhs.commonhelper.R;
import com.zhs.commonhelper.adapter.common.CommonRecyclerVAdapter;
import com.zhs.commonhelper.adapter.common.RecyclerVViewHolder;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.ConfirmCallback;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.CityBean;

import java.util.List;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter extends CommonRecyclerVAdapter<CityBean> {
    protected Context mContext;
    private ConfirmCallback confirmCallback;
    private boolean isVisibilityBindNfc = false;//是否显示绑定过nfc.默认false
    private int type = 0;//0是右侧显示签到时间，1是右侧显示签到次数
    public CityAdapter(Context context, List<CityBean> datasList, int layoutId) {
        super(context, datasList, layoutId);
        mContext = context;

    }

    @Override
    protected void fillData(RecyclerVViewHolder holder, int position) {
        //需要添加这两句背景色才会随点击改变，其实是通过RecyclerItemClickListener设置监听才导致的问题
        View view = holder.getMConvertView();
        //view.setClickable(true);//要设置里面控件的点击这里就不能true

        TextView tvCity = (TextView) view.findViewById(R.id.tvCity);
        ImageView avatar = (ImageView) view.findViewById(R.id.ivAvatar);
        View content = (View) view.findViewById(R.id.content);

        final int finalPos = position;
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCallback.onClickConfirm(finalPos);
            }
        });

        if(isVisibilityBindNfc){
            if(datasList.get(position).getIsRightImg()!=null && datasList.get(position).getIsRightImg().length()>0) {
                avatar.setVisibility(View.VISIBLE);
            }else {
                avatar.setVisibility(View.GONE);
            }
        }else{
            avatar.setVisibility(View.GONE);
        }
        CityBean cityBean = datasList.get(position);
        tvCity.setText(cityBean.getCity());
    }

    public void setConfirmCallback(ConfirmCallback confirmCallback){
        this.confirmCallback = confirmCallback;
    }

    public void isVisibilityBindNfc(boolean isVisibilityBindNfc){
        this.isVisibilityBindNfc = isVisibilityBindNfc;
    }
}
