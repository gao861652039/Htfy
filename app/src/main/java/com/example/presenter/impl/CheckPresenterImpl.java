package com.example.presenter.impl;

import com.example.model.impl.CheckModelImpl;
import com.example.presenter.inter.CheckPresenter;
import com.example.presenter.inter.OnCheckListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class CheckPresenterImpl implements CheckPresenter.IcheckPresenter {
    private CheckPresenter.IcheckView mCheckView;
    private CheckModelImpl model = null;
    public CheckPresenterImpl(CheckPresenter.IcheckView mCheckView){
         this.mCheckView = mCheckView;
         model = new CheckModelImpl();
    }


    @Override
    public void getStatus(String version) {
        model.sendCode(version);
        model.loadStatus(new OnCheckListener() {
            @Override
            public void onSuccess() {
                mCheckView.onCheckedSuccess();
            }

            @Override
            public void onFailure() {
               mCheckView.onCheckedFailure();
            }
        });
    }
}
