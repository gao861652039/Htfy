package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.DeviceModel;
import com.example.model.thread.SocketThread;
import com.example.presenter.inter.OnDeviceInfoListener;
import com.example.utils.NumberUtils;

/**
 * Created by gaofeng on 2017/8/8.
 */

public class DeviceModelImpl implements DeviceModel {
    @Override
    public void loadDeviceInfo(int sel, OnDeviceInfoListener onDeviceInfoListener) {
        SocketThread st =  SocketThread.getInstance();
        st.socket_mode = 0x2;
        st.gdtm_sel = NumberUtils.toHex(sel);
        st.gdtm_start_date = "201708010000";
        st.gdtm_end_date = "201708030000";
        st.start();
        try {
            st.join();
            SocketThread.gdtm_t gdtm_t = st.gdtm_data;
            do {
                gdtm_t = gdtm_t.next;
                Log.e("deviceInfo",gdtm_t.data);
                if(gdtm_t.next == null){
                    break;
                }
            }while(true);




            onDeviceInfoListener.onSuccess("success");
        } catch (InterruptedException e) {
            onDeviceInfoListener.onFailure(e.getMessage());
        } catch (NullPointerException e2){
            Log.e("tag","链表到头了");
        }
    }
}
