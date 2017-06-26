package com.example.entity;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class DeviceInfo {

    private String time;
    private String alarminfo;
    private String otherInfo;

    public DeviceInfo( String time, String alarminfo,String otherInfo) {

        this.time = time;
        this.alarminfo = alarminfo;
        this.otherInfo = otherInfo;
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
}