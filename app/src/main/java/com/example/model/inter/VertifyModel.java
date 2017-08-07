package com.example.model.inter;

import com.example.presenter.inter.OnCheckListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface VertifyModel {

    void sendCode(String version);
    void loadStatus(OnCheckListener onCheckListener);

}
