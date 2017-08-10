package com.example.presenter.impl;

import com.example.model.impl.DeviceModelImpl;
import com.example.model.impl.DeviceRequestAgainImpl;
import com.example.presenter.inter.DeviceInfoPresenter;
import com.example.presenter.inter.OnDeviceInfoListener;

import java.util.List;

/**
 * Created by 高峰 on 2017/8/10.
 */

public class DeviceRequestPresenterImpl implements DeviceInfoPresenter.IDeviceRequestPresenter{
    private DeviceRequestAgainImpl deviceModel = null;
    private DeviceInfoPresenter.IDeviceView deviceView = null;

    public DeviceRequestPresenterImpl(DeviceInfoPresenter.IDeviceView deviceView){
        this.deviceView = deviceView;
        deviceModel = new DeviceRequestAgainImpl();
    }

    @Override
    public void getDeviceInfo(int sel,String start,String end) {
        deviceModel.loadDeviceInfo(sel,start,end,new OnDeviceInfoListener() {


            @Override
            public void onSuccess(List<String> deviceInfo, List<String> detailInfo) {
                deviceView.onSuccess(deviceInfo,detailInfo);
            }

            @Override
            public void onFailure(String error) {
                deviceView.onFailure(error);
            }
        });
    }
}
