package com.zhs.commonhelper.widget.fastsearch.searchedt;

import com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model.SearchModel;

import java.io.Serializable;

/**
 * Created by zhs89 on 2017/12/13.
 */

public class SortModel extends SearchModel implements Serializable {
    public SortModel() {
        super();
    }

    public String sortLetters;//显示数据拼音的首字母
    public SortToken sortToken= new SortToken();//中文全名,全拼,简拼
}
