package com.example.presenter.inter;

/**
 * Created by gaofeng on 2017/8/8.
 */

public interface DeviceInfoPresenter {
    interface IDevicePresenter{
        void getDeviceInfo(int sel);

    }
    interface IDeviceView{
        void onSuccess(String deviceInfo);
        void onFailure(String error);
    }

}
