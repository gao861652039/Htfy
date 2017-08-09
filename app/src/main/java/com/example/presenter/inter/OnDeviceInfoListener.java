package com.example.presenter.inter;

import java.util.List;

/**
 * Created by gaofeng on 2017/8/8.
 */

public interface OnDeviceInfoListener {

     void onSuccess(List<String> deviceInfo,List<String> detailInfo);
     void onFailure(String error);
}
