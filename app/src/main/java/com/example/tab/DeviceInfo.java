package com.example.tab;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class DeviceInfo {

    private String devicename;
    private String deviceId;
    private String time;
    private String alarminfo;

    public DeviceInfo(String devicename, String deviceId, String time, String alarminfo) {
        this.devicename = devicename;
        this.deviceId = deviceId;
        this.time = time;
        this.alarminfo = alarminfo;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}