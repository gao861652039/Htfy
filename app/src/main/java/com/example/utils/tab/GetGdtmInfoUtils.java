package com.example.utils.tab;

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
                   deviceInfo.add(gdtm.data);
                }
            }while(true);

         return deviceInfo;

    }

    public static List<String> getDetailInfo(SocketThread.gdtm_t gdtm_t){
        List<String> detailInfo = new ArrayList<>();
        SocketThread.gdtm_t gdtm = gdtm_t;
        SocketThread.gdtm_t detail = null;
        if(gdtm_t.next == null){
            return null;
        }
        do{
            gdtm = gdtm.next;
            if(gdtm.next == null){
                break;
            }else{
                if(gdtm.data.contains("C0")){
                    detail = gdtm.detail;
                    break;
                }
            }
        }while(true);

        do{
            if(detail == null){
                return null;
            }
            detailInfo.add(detail.data);
            if(detail.next!=null){
                detail = detail.next;
            }else{
                break;
            }
        }while(true);


          return detailInfo;

    }
}
