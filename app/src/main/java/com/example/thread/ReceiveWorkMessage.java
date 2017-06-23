package com.example.thread;


import android.os.Handler;
import android.os.Message;

import com.example.tab.Connect;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/6/6.
 */

public class ReceiveWorkMessage extends  Thread {

    private Socket socket;
    private Handler handler;
   public ReceiveWorkMessage(Handler handler){

        this.handler = handler;


   }


    @Override
    public void run() {
        try {
            socket = Connect.getSocket();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line= null;
            int len = 0;
            char[] buf = new char[1024];
            while((len = br.read(buf))!=-1){
                line = new String(buf,0,len);
                Message msg =new Message();
                if("#E".equals(line)){
                    msg.what = 2;
                    handler.sendMessage(msg);
                    break;
                }
                msg.what = 1;
                msg.obj = line;
                handler.sendMessage(msg);
            }
            System.out.println("没有更多数据接收，线程退出");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
