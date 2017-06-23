package com.example.tab;


import java.io.IOException;
import java.net.Socket;


/**
 * Created by 高峰 on 2017/5/8.
 */

public class Connect{

    private  static Socket socket;
    static{
        try {
            socket = new Socket("114.115.218.206",8631);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private static Connect connect = new Connect();

    private Connect(){


    }



    public static Connect getInstance(){

        return  connect;
    }

    public static Socket getSocket(){
        return socket;
    }



}
