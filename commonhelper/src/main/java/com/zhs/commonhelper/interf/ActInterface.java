package com.zhs.commonhelper.interf;

import android.os.Bundle;

/**
 * activity需要重写的方法
 * Created by zhs89 on 2018/3/26.
 */

public interface ActInterface {
    public void initData();//界面控件加载前加载的初始数据
    public void initHeader();//初始化头部显示
    public void initView(Bundle savedInstanceState);//初始化界面
    public void showView();//导入界面数据
}
