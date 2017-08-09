package com.example.model.entity;

/**
 * Created by 高峰 on 2017/6/28.
 */

public class CharDataEntity {
    private float[] value;
    private String[] xzb;

    public CharDataEntity(float[] value, String[] xzb) {
        this.value = value;
        this.xzb = xzb;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }

    public String[] getXzb() {
        return xzb;
    }

    public void setXzb(String[] xzb) {
        this.xzb = xzb;
    }
}
