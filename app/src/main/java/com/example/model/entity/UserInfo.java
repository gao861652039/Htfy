package com.example.model.entity;

import java.io.Serializable;


/**
 * Created by 高峰 on 2017/2/12.
 */

public class UserInfo {
    private String machNum;
    private String username;
    private String useraddress;
    private String location;
    private String date;


    public UserInfo(String machNum, String username, String useraddress, String location, String date) {
        this.machNum = machNum;
        this.username = username;
        this.useraddress = useraddress;
        this.location = location;
        this.date = date;
    }


    public String getMachNum() {
        return machNum;
    }

    public void setMachNum(String machNum) {
        this.machNum = machNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }





}
