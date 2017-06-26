package com.example.utils;

/**
 * Created by 高峰 on 2017/6/26.
 */

public class MachineUtils {

    //得到制水机以外的设备名
    public static String getDeviceName(String name) {
        if (!"".equals(name) && name != null) {
            switch (name) {
                case "0":
                    return "原水箱";
                case "1":
                    return "纯水箱";
                case "2":
                    return "酸水箱";
                case "3":
                    return "碱水箱";
                case "4":
                    return "盐水箱";
                case "5":
                    return "搅拌箱";
                case "SYS":
                    return "系统设备";
            }


        }
        return null;
    }

    //得到外围错误
    public static String getAlarmCode(String alarm_code) {

        if (!"".equals(alarm_code) || alarm_code != null) {
            switch (alarm_code) {
                case "C0":
                    return "开始制水";
                case "C1":
                    return "停止制水";
                case "C2":
                    return "上传制水数据";
                case "CE":
                    return "制水机报警信息";
                case "CT":
                    return "水箱组报警信息";
                case "C6":
                    return "系统启动";
                default:
                    return null;
            }

        }
        return null;
    }

    //得到制水机错误类型
    public static String getZsjError(String error) {
        String str = Integer.toHexString(Integer.parseInt(error));
        switch (str) {
            case "ff":
                return "离线";
            case "40":
                return "过流保护";
            case "20":
                return "高水压保护";
            case "10":
                return "低水压保护";
            case "4":
                return "电解槽耗尽";
            default:
                return null;
        }
    }

    //得到水箱报警类型
    public static String getSxAlarmType(String alarm) {
        String str = Integer.toHexString(Integer.parseInt(alarm));
        switch (str) {
            case "ff":
                return "信号错误";
            case "0":
                return "低水位报警";
            default:
                return null;
        }
    }
    //得到水箱故障
    public static String getSxError(String error){
        switch (error){
            case "ZZ":
                return "未设置传感器";
            case "FF":
                return "信号故障";
            case "00":
                return "低水位报警";
            default:
                return null;
        }
    }


}
