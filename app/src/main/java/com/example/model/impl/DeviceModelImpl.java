package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.DeviceModel;
import com.example.model.thread.SocketThread;
import com.example.presenter.inter.OnDeviceInfoListener;
import com.example.utils.NumberUtils;
import com.example.utils.TimeUtils;
import com.example.utils.tab.GetGdtmInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
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
         EventBus.getDefault().register(this);
     }

    @Override
    public void loadDeviceInfo(int sel, OnDeviceInfoListener onDeviceInfoListener) {


        listener = onDeviceInfoListener;
        st.socket_mode = 0x2;
        st.gdtm_sel = NumberUtils.toHex(sel);
        st.gdtm_start_date = TimeUtils.start_date();     //7天的数据
        st.gdtm_end_date = TimeUtils.end_date();       //7天的数据
        st.start();

    }

    @Subscribe(threadMode = ThreadMode.ASYNC,sticky = true)
    public void test(String event){
        try {
            deviceInfo = GetGdtmInfoUtils.getDeviceInfo(st.gdtm_data);
            detailInfo = GetGdtmInfoUtils.getDetailInfo(st.gdtm_data);
            listener.onSuccess(deviceInfo, detailInfo);
        }catch (NullPointerException e){
             listener.onFailure(e.getMessage());
        }
    }





}
