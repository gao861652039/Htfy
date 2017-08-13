package com.example.utils;

import android.util.Log;

import com.example.model.thread.SocketThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高峰 on 2017/8/9.
 */

public class GetGdtmInfoUtils {



    public static List<String> getDeviceInfo(SocketThread.gdtm_t gdtm_t){
        List<String> deviceInfo = new ArrayList<>();
        SocketThread.gdtm_t gdtm = gdtm_t;
            if(gdtm_t.next == null){
                return null;
            }
            do {
                gdtm = gdtm.next;
                if(gdtm.next == null){
                    break;
                }else{
                    if(gdtm.data.contains("C0")){
                        continue;
                    }
                    deviceInfo.add(gdtm.data);
                }
            }while(true);

         return deviceInfo;

    }

    public static List<String> getDetailInfo(SocketThread.gdtm_t gdtm_t){
        List<String> detailInfo = new ArrayList<>();
        SocketThread.gdtm_t gdtm = gdtm_t;
        SocketThread.gdtm_t detail = null;

        do{
            if(gdtm.next == null){
                return null;
            }else{
                 gdtm = gdtm.next;
                if(gdtm.data.contains("C0")){
                    detailInfo.add(gdtm.data);
                    Log.e("co1",gdtm.data);
                    if(gdtm.detail == null){
                        continue;
                    }else{
                        detail = gdtm.detail;
                        break;
                    }
                }else{
                    continue;
                }
            }
        }while(true);

        do{
               if(detail == null){
                     break;
                }
                if(detail.data.contains("C2")){
                    detailInfo.add(detail.data);
                    detail = detail.next;
                }
               else if(detail.data.contains("C0")){
                   detailInfo.add(detail.data);
                   detail = detail.detail;
               }else{
                    detail = detail.next;
                }
               if(detail.detail == null && detail.next ==null)
                     break;

        }while(true);

          return detailInfo;

    }

    public static void  getAllData(SocketThread.gdtm_t gdtm_t){
        SocketThread.gdtm_t gdtm = gdtm_t;
        if(gdtm_t.next == null){
            return;
        }
        do {
            gdtm = gdtm.next;
            if(gdtm.next == null){
                break;
            }else{

               Log.e("test",gdtm.data);
            }
        }while(true);

    }
}
