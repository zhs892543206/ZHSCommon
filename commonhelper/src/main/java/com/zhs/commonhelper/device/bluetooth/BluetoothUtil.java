package com.zhs.commonhelper.device.bluetooth;

import android.bluetooth.BluetoothAdapter;

import com.zhs.commonhelper.widget.dialog.ToastUtil;


/**
 * 蓝牙帮助类
 * Created by zhs89 on 2018/4/3.
 */

public class BluetoothUtil {

    /**
     * 检查蓝牙是否开启
     */
    public static void checkIbceacon(){
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //获取蓝牙适配器
        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.enable();  //开启蓝牙（还有一种方法开启，我就不说了，自己查去）
                    }
                }).start();
            } else {
                if (!bluetoothAdapter.isDiscovering()) {  //如果没有在扫描设备
                    bluetoothAdapter.startDiscovery();//扫描附近蓝牙设备，然后做接下来的操作，比如扫描附近蓝牙等
                } else {
                    //ToastUtil.showToast("正在扫描");  //弹出Toast提示
                }
            }
        } else {  //无蓝牙功能
            ToastUtil.showToast("当前设备未找到蓝牙功能");  //弹出Toast提示
        }
    }
}
