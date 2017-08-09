package com.example.presenter.impl;

import com.example.model.impl.DeviceModelImpl;
import com.example.presenter.inter.DeviceInfoPresenter;
import com.example.presenter.inter.OnDeviceInfoListener;

import java.util.List;

/**
 * Created by gaofeng on 2017/8/8.
 */

public class DeviceInfoPresenterImpl implements DeviceInfoPresenter.IDevicePresenter {
    private DeviceModelImpl deviceModel = null;
    private DeviceInfoPresenter.IDeviceView deviceView = null;

    public DeviceInfoPresenterImpl(DeviceInfoPresenter.IDeviceView deviceView){
         this.deviceView = deviceView;
         deviceModel = new DeviceModelImpl();
    }

    @Override
    public void getDeviceInfo(int sel) {
         deviceModel.loadDeviceInfo(sel, new OnDeviceInfoListener() {


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
