package com.example.model;

import com.example.presenter.OnCheckListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface VertifyModel {

    void sendCode(String version);
    void loadStatus(OnCheckListener onCheckListener);

}
