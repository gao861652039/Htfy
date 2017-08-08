package com.example.model.inter;

import com.example.presenter.inter.OnDeviceInfoListener;

/**
 * Created by gaofeng on 2017/8/8.
 */

public interface DeviceModel {

    void loadDeviceInfo(int sel, OnDeviceInfoListener onDeviceInfoListener);
}
