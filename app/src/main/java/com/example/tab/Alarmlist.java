package com.example.tab;

/**
 * Created by 高峰 on 2017/2/15.
 */

public class Alarmlist {


    public String deviceSelect(String deviceId){

        switch (deviceId){
            case "0x01":
                return "原水箱";

            case "0x02":
                 return "原水泵";

            case "0x03":
                return "软化系统";

            case "0x04":
                return "RO反渗透";

            case "0x05":
                return "纯水箱";

            case "0x06":
                return "纯水泵";

            case "0x07":
                return "盐水箱";

            case "0x08":
                return "酸水箱";

            case "0x09":
                return "碱水箱";

            case "0x0a":
                return "工作站";

            case "0x0b":
                return "主控站";

            case "0x0c":
                return "GDTM";

            case "0x0d":
                return "化学监控站";

            case "0x0e":
                return "酸水总流量监测站";

            case "0x0f":
                return "碱水总流量监测站";

            case "0x10":
                return "原水总流量检测站";

            default:
                return "无此设备";

        }


    }

    public String alarmSelect(String alarmId){
        switch (alarmId.trim()){
            case "0x0000":
                return "系统开机";
            case "0x0001":
                return "系统关闭";
            case "0x0002":
                return "系统启动错误";
            case "0x0003":
                return "系统关闭错误";
            case "0x1010":
                return "原水箱低水位报警";
            case "0x1011":
                return "原水箱注水超时";
            case "0x1020":
                return "原水泵报警";
            case "0x1030":
                return "软化系统报警";
            case "0x1040":
                return "反渗透报警";
            case "0x1050":
                return "纯水箱低水位报警";
            case "0x1051":
                return "纯水箱注水超时";
            case "0x1060":
                return "纯水泵报警";
            case "0x1070":
                return "盐水箱报警";
            case "0x1080":
                return "酸水箱报警";
            case "0x1090":
                return "碱水箱报警";
            case "0x10a0":
                return "工作站报警";
            case "0x10b0":
                return "主控站报警";
            case "0x10c0":
                return "GDTM报警";
            case "0x10d0":
                return "化学监控站报警";
            case "0x10e0":
                return "酸水流量站报警";
            case "0x10f0":
                return "碱水流量站报警";
            case "0x1100":
                return "原水总流量站报警";
            case "0x1ff0":
                return "未定义报警";
            case "0x3010":
                return "原水箱错误";
            case "0x3020":
                return "原水泵错误";
            case "0x3030":
                return "软化系统错误";
            case "0x3040":
                return "反渗透错误";
            case "0x3050":
                return "纯水箱错误";
            case "0x3060":
                return "纯水泵错误";
            case "0x3070":
                return "盐水箱错误";
            case "0x3080":
                return "酸水箱错误";
            case "0x3090":
                return "碱水箱错误";
            case "0x30a0":
                return "工作站错误";
            case "0x30b0":
                return "主控站错误";
            case "0x30c0":
                return "GDTM错误";
            case "0x30d0":
                return "化学监控站错误";
            case "0x30e0":
                return "酸水流量站错误";
            case "0x30f0":
                return "碱水流量站错误";
            case "0x3100":
                return "原水流量站错误";
            case "0x3ff0":
                return "未定义错误";
            default:
                return "无此报警信息";

















        }


    }

}
