package com.example.model.entity;

import android.support.annotation.NonNull;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class DeviceInfo {

    private String time;
    private String alarminfo;
    private String otherInfo;
    private boolean flag = false;
    public DeviceInfo( String time, String alarminfo,String otherInfo,boolean flag) {

        this.time = time;
        this.alarminfo = alarminfo;
        this.otherInfo = otherInfo;
        this.flag = flag;
    }




    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarminfo() {
        return alarminfo;
    }

    public void setAlarminfo(String alarminfo) {
        this.alarminfo = alarminfo;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}