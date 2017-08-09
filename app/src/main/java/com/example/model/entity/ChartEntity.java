package com.example.model.entity;

/**
 * Created by 高峰 on 2017/6/28.
 */

public class ChartEntity {
    private String deviceId;
    private String time;
    private float ph;
    private float orp;
    private float yxl;

    public ChartEntity(String deviceId, String time, float ph, float orp, float yxl) {
        this.deviceId = deviceId;
        this.time = time;
        this.ph = ph;
        this.orp = orp;
        this.yxl = yxl;
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

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public float getOrp() {
        return orp;
    }

    public void setOrp(float orp) {
        this.orp = orp;
    }

    public float getYxl() {
        return yxl;
    }

    public void setYxl(float yxl) {
        this.yxl = yxl;
    }
}
