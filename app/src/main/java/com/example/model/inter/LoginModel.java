package com.example.model.inter;

import com.example.presenter.inter.OnLoginListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface LoginModel {


    void login(String username,String password,OnLoginListener onLoginListener);
}
