package com.example.model;

import com.example.presenter.OnLoginListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface LoginModel {

    void login(String verInfo);
    void loadGdtmId(OnLoginListener onLoginListener);
}
