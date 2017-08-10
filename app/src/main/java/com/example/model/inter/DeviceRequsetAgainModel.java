package com.example.model.inter;

import com.example.presenter.inter.OnDeviceInfoListener;

/**
 * Created by 高峰 on 2017/8/10.
 */

public interface DeviceRequsetAgainModel {

    void loadDeviceInfo(int sel, String start,String end,OnDeviceInfoListener onDeviceInfoListener);
}
