package com.example.model.entity;

/**
 * Created by 高峰 on 2017/6/26.
 */

public class DataInfo {
    private String ph;
    private String orp;
    private String yxl;
    private String dl;
    private String djc;
    private String yb;

    public DataInfo(String ph, String orp, String yxl, String dl, String djc, String yb) {
        this.ph = ph;
        this.orp = orp;
        this.yxl = yxl;
        this.dl = dl;
        this.djc = djc;
        this.yb = yb;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getOrp() {
        return orp;
    }

    public void setOrp(String orp) {
        this.orp = orp;
    }

    public String getYxl() {
        return yxl;
    }

    public void setYxl(String yxl) {
        this.yxl = yxl;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getDjc() {
        return djc;
    }

    public void setDjc(String djc) {
        this.djc = djc;
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb;
    }
}
