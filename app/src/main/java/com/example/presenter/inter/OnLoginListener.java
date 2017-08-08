package com.example.presenter.inter;

import com.example.model.entity.UserInfo;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface OnLoginListener {

    void onSuccess(String gdtmId[],String[] userInfo);
    void onFailure(String error);
}
