package com.example.presenter.inter;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface OnLoginListener {

    void onSuccess(String gdtmId[]);
    void onFailure(String error);
}
