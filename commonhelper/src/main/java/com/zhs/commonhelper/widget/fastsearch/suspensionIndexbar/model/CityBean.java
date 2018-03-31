package com.zhs.commonhelper.widget.fastsearch.suspensionIndexbar.model;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * 这里是用来存
 * 部门选择和医院选择的item
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityBean extends BaseIndexPinyinBean {

    private String cityId;//城市Id
    private String city;//城市名字
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的 默认false。true就是上面特殊的选项，如最近选项
    private String isRightImg;//是否右侧添加选择图标
    public CityBean() {
    }

    public CityBean(String city, String cityId, String isRightImg) {
        this.city = city;
        this.cityId = cityId;
        this.isRightImg = isRightImg;
    }
    public CityBean(String city, String cityId) {
        this.city = city;
        this.cityId = cityId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public CityBean setCity(String city) {
        this.city = city;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public CityBean setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return city;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    public String getIsRightImg() {
        return isRightImg;
    }

    public void setIsRightImg(String isRightImg) {
        this.isRightImg = isRightImg;
    }
}
