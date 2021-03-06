package com.example.model.impl;




import com.example.model.inter.LoginModel;
import com.example.model.thread.SocketThread;
import com.example.presenter.inter.OnLoginListener;
import com.example.utils.Flag;

/**
 * Created by 高峰 on 2017/8/7.
 */

public class LoginModelImpl implements LoginModel {
    public static String  flag = null;

    @Override
    public void login(String username, String password, OnLoginListener onLoginListener) {

         SocketThread st = SocketThread.getInstance();
         st.user_name = username;
         st.user_pass = password;
         st.socket_mode = 0x1;
         flag = Flag.LOGIN;
         st.start();
         try {
             st.join();
             if(st.gdtm_id.length!=0){
                 onLoginListener.onSuccess(st.gdtm_id,st.gdtm_info);
             }else{
                 onLoginListener.onFailure("获取用户下挂的gdtm失败");

             }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
