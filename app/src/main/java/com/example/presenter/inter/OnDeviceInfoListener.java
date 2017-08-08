package com.example.presenter.inter;

/**
 * Created by gaofeng on 2017/8/8.
 */

public interface OnDeviceInfoListener {

     void onSuccess(String deviceInfo);
     void onFailure(String error);
}
