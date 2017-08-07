package com.example.utils;


import java.io.IOException;
import java.net.Socket;


/**
 * Created by 高峰 on 2017/5/8.
 */

public class ConnectUtils {

    private  static Socket socket;
    static{
        try {
            socket = new Socket("114.115.218.206",8631);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private static ConnectUtils connectUtils = new ConnectUtils();

    private ConnectUtils(){


    }



    public static ConnectUtils getInstance(){

        return connectUtils;
    }

    public static Socket getSocket(){
        return socket;
    }



}
