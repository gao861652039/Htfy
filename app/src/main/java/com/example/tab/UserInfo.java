package com.example.tab;

import android.graphics.drawable.Drawable;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class UserInfo {
    private String machNum;
    private String username;
    private String useraddress;
    private String location;
    private String date;
    private int ISVISBLE;

    public UserInfo(String machNum, String username, String useraddress, String location, String date, int ISVISBLE) {
        this.machNum = machNum;
        this.username = username;
        this.useraddress = useraddress;
        this.location = location;
        this.date = date;
        this.ISVISBLE = ISVISBLE;
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

    public int getISVISBLE() {
        return ISVISBLE;
    }

    public void setISVISBLE(int ISVISBLE) {
        this.ISVISBLE = ISVISBLE;
    }
}
