package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.DeviceModel;
import com.example.model.thread.SocketThread;
import com.example.presenter.inter.OnDeviceInfoListener;
import com.example.utils.NumberUtils;
import com.example.utils.tab.GetGdtmInfoUtils;

import java.util.List;

/**
 * Created by gaofeng on 2017/8/8.
 */

public class DeviceModelImpl implements DeviceModel {
   private List<String> deviceInfo ;
   private List<String> detailInfo ;
   private SocketThread st =  SocketThread.getInstance();
   private OnDeviceInfoListener listener;

     public DeviceModelImpl(){

     }

    @Override
    public void loadDeviceInfo(int sel, OnDeviceInfoListener onDeviceInfoListener) {


        listener = onDeviceInfoListener;
        st.socket_mode = 0x2;
        st.gdtm_sel = NumberUtils.toHex(sel);
        st.gdtm_start_date = "201708010000";
        st.gdtm_end_date = "201708030000";
        st.start();
        try {
            st.join();
            deviceInfo = GetGdtmInfoUtils.getDeviceInfo(st.gdtm_data);
            detailInfo = GetGdtmInfoUtils.getDetailInfo(st.gdtm_data);
            Log.e("msg",detailInfo.get(0));
            listener.onSuccess(deviceInfo, detailInfo);
        } catch (InterruptedException e) {
            listener.onFailure(e.getMessage());
        } catch (NullPointerException e2){
            Log.e("tag","链表到头了");
        }





    }




}
