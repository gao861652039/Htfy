package com.example.presenter;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface CheckPresenter {
    interface IcheckPresenter{
        void getStatus(String version);
    }
    interface IcheckView{
        void  onCheckedSuccess();
        void  onCheckedFailure();
    }
}
