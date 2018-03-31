package com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.adapter;

import android.content.Context;

import com.zhs.commonhelper.R;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.MeiTuanBean;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.utils.SearchCommonAdapter;
import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.utils.SearchViewHolder;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class MeituanAdapter extends SearchCommonAdapter<MeiTuanBean> {
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(SearchViewHolder holder, final MeiTuanBean cityBean) {
        holder.setText(R.id.tvCity, cityBean.getCity());
    }
}