package com.example.presenter.inter;

import java.util.List;

/**
 * Created by gaofeng on 2017/8/8.
 */

public interface DeviceInfoPresenter {
    interface IDevicePresenter{
        void getDeviceInfo(int sel);

    }

    interface IDeviceRequestPresenter{
        void getDeviceInfo(int sel,String start,String end);

    }

    interface IDeviceView{
        void onSuccess(List<String> deviceInfo,List<String> detailInfo);
        void onFailure(String error);
    }

}
