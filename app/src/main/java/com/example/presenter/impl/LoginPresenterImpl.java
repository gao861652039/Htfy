package com.example.presenter.impl;

import com.example.model.LoginModel;
import com.example.model.impl.LoginModelImpl;
import com.example.presenter.LoginPresenter;
import com.example.presenter.OnLoginListener;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class LoginPresenterImpl  implements LoginPresenter.IloginPresenter{
     private LoginPresenter.IloginView mloginView;
     private LoginModelImpl model = null;
     public LoginPresenterImpl(LoginPresenter.IloginView mloginView){

               this.mloginView = mloginView;
               model = new LoginModelImpl();

     }

    @Override
    public void getGdtmId(String verInfo) {
        model.login(verInfo);
        model.loadGdtmId(new OnLoginListener() {
            @Override
            public void onSuccess(String[] gdtmId) {
                mloginView.onSuccess(gdtmId);
            }

            @Override
            public void onFailure(String error) {
                mloginView.onFailure(error);
            }
        });

    }


}
