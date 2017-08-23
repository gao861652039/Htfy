package com.example.model.impl;

import android.util.Log;

import com.example.model.inter.DeviceRequsetAgainModel;
import com.example.model.thread.SocketThread;
import com.example.presenter.inter.OnDeviceInfoListener;
import com.example.utils.Flag;
import com.example.utils.NumberUtils;
import com.example.utils.TimeUtils;
import com.example.utils.GetGdtmInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by 高峰 on 2017/8/10.
 */

public class DeviceRequestAgainImpl implements DeviceRequsetAgainModel {
    private List<String> deviceInfo;
    private List<String> detailInfo;
    private SocketThread st = SocketThread.getInstance();
    private OnDeviceInfoListener listener;

    public DeviceRequestAgainImpl() {
        EventBus.getDefault().register(this);

    }

    @Override
    public void loadDeviceInfo(int sel, String start, String end, OnDeviceInfoListener onDeviceInfoListener) {


        listener = onDeviceInfoListener;
        st.socket_mode = 0x2;
        st.gdtm_sel = NumberUtils.toHex(sel);
        st.gdtm_start_date =TimeUtils.toStart(start) ;
        st.gdtm_end_date =  TimeUtils.toEnd(end);
        LoginModelImpl.flag = Flag.GETDATAAGAIN;
        new Thread(new Runnable() {
            @Override
            public void run() {
                st.run();
            }
        }).start();

    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void test(String event) {
        try {
            if(event.equals(Flag.GETDATAAGAIN)) {
                deviceInfo = GetGdtmInfoUtils.getDeviceInfo(st.gdtm_data);
                detailInfo = GetGdtmInfoUtils.getDetailInfo(st.gdtm_data);
                listener.onSuccess(deviceInfo, detailInfo);

            }
        } catch (NullPointerException e) {
            listener.onFailure("获取数据失败");
        }
    }


}

