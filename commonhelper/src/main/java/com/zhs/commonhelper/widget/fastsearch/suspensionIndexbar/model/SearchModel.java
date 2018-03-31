package com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model;

import java.io.Serializable;

/**
 * 被搜索的类
 * 2017/12/11.
 */

public class SearchModel implements Serializable {
    private String searchId;//选择id
    private String searchNameStr;//选择名称
    private String isRightImg;//是否右侧添加选择图标
    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchNameStr() {
        return searchNameStr;
    }

    public void setSearchNameStr(String searchNameStr) {
        this.searchNameStr = searchNameStr;
    }

    public String getIsRightImg() {
        return isRightImg;
    }

    public void setIsRightImg(String isRightImg) {
        this.isRightImg = isRightImg;
    }
}
