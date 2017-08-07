package com.example.presenter;

/**
 * Created by 高峰 on 2017/8/7.
 */

public interface LoginPresenter {

      interface IloginPresenter{
          void getGdtmId(String verInfo);
      }

      interface IloginView{
           void onSuccess(String[] gdtmId);
           void onFailure(String s);

      }

}
