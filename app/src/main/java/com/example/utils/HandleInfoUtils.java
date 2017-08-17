package com.example.utils;



import com.example.model.entity.ChartEntity;
import com.example.model.entity.DeviceInfo;
import com.example.view.activity.MainActivity;
import com.example.view.fragment.DeviceFragment;


import java.util.ArrayList;
import java.util.List;

import static com.example.utils.MachineUtils.getAlarmCode;
import static com.example.utils.MachineUtils.sxId;
import static com.example.utils.MachineUtils.sxMsgType;
import static com.example.utils.TimeUtils.disContent;

/**
 * Created by 高峰 on 2017/8/15.
 */

public class HandleInfoUtils {
    public static ArrayList<ChartEntity> chartEntities = new ArrayList<>();
    //单独处理一个C2指令
    public static DeviceInfo handleSingleDetail(String detailInfo){
        if(detailInfo!=null) {
            String[] info = detailInfo.split("\\|");
            String time = info[0].split("\\$")[1];
            String alarmInfo = info[1];
            String deviceId = info[2];
            String ph = info[3];
            String orp = info[4];
            String yxl = info[5];
            String dl = info[6];
            String djc = info[7];
            String yb = info[8];

            chartEntities.add(new ChartEntity(deviceId
                    , disContent(time)
                    , transToFloat(handlePH(ph))
                    , transToFloat(orp)
                    , transToFloat(yxl)));
           MainActivity.deviceFragment.setter(chartEntities);


            return new DeviceInfo(disContent(time), deviceId+"号机"+getAlarmCode(alarmInfo),
                      "[PH:" + handlePH(ph) + "]" +
                            "[ORP:" + orp + "mv]" +
                            "[有效氯:" + yxl + "mg/L]", false);
        }
        return null;
    }
    //处理水箱信息
    public static DeviceInfo handleSingleSxInfo(String sxInfo){

        String[] info = sxInfo.split("\\|");
        String time = info[0].split("\\$")[1];
        String alarmInfo = info[1];
        String sx = info[9];

        //水箱顺序
        //原水箱，纯水箱，酸水箱，碱水箱，盐水箱，搅拌箱
        //Z：离线，3：高水位，2：中水位，1：补水位，0：警戒位
        if ((!"".equals(sx)) && sx != null) {
            String ysx = sx.substring(0, 1);
            String csx = sx.substring(1, 2);
            String ssx = sx.substring(2, 3);
            String jsx = sx.substring(3, 4);
            String yansx = sx.substring(4, 5);
            String jbx = sx.substring(5, 6);

            return new DeviceInfo(disContent(time), "系统信息:" + MachineUtils.getAlarmCode(alarmInfo),
                    "原水箱:" + sxMsgType(ysx) +
                            "纯水箱:" + sxMsgType(csx) +
                            "酸水箱:" + sxMsgType(ssx) + "\n" +
                            "碱水箱:" + sxMsgType(jsx) +
                            "盐水箱:" + sxMsgType(yansx) +
                            "搅拌箱:" + sxMsgType(jbx), false);
        }
        return null;
    }

    public static String handlePH(String ph) {
        float a = (Float.parseFloat(ph)) / 10;
        return String.valueOf(a);
    }

    //将String转换为float
    public static float transToFloat(String s) {
        float a = Float.parseFloat(s);
        return a;
    }


    //处理单独报警信息

    public static DeviceInfo handlerSingleAlarmInfo(String deviceInfo) {
        if (deviceInfo != null) {
            String[] info = deviceInfo.split("\\|");
            String time = info[0].split("\\$")[1];
            String alarmInfo = info[1];
            String deviceId = info[2];
            if (deviceInfo.contains("C1") || deviceInfo.contains("C6")) {
                return HandleInfoUtils.handleSingleSxInfo(deviceInfo);
            }

            if (deviceInfo.contains("CE")) {
                String param = info[3];
                return new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                        deviceId + "号机" + MachineUtils.getZsjError(param), true);
            }
            if (deviceInfo.contains("CT")) {
                String param = info[3];
                return new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                        sxId(deviceId) + MachineUtils.getSxError(param), true);

            }

        }

        return null;
    }


}
