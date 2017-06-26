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
            InputStream is = socket.getInputStream();
            String line= null;
            int len = 0;
            byte[] buf = new byte[1024];
            while((len = is.read(buf))!=-1){
                line = new String(buf,0,len);
                System.out.println("line:"+line);
                if(line.contains("#E")){
                    throw new IOException();
                }else {
                    Message msg =new Message();
                    msg.what = 1;
                    msg.obj = line;
                    handler.sendMessage(msg);
                }
            }

        } catch (IOException e) {
            System.out.println("没有更多数据接收，线程退出");
            Message msg =new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }

    }
}
