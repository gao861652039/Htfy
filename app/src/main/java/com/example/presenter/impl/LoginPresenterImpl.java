package com.example.presenter.impl;

import com.example.model.entity.UserInfo;
import com.example.model.impl.LoginModelImpl;
import com.example.presenter.inter.LoginPresenter;
import com.example.presenter.inter.OnLoginListener;

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
    public void login(final String username, String password) {
           model.login(username, password, new OnLoginListener() {

               @Override
               public void onSuccess(String[] gdtmId, String[] userInfo) {

                   mloginView.onSuccess(gdtmId,userInfo);
               }

               @Override
               public void onFailure(String error) {
                   mloginView.onFailure(error);
               }
           });
    }
}
