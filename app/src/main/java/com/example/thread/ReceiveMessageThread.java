package com.example.thread;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.tab.Connect;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/5/24.
 */

public class ReceiveMessageThread extends  Thread {
    private Socket socket;
    private Handler handler;
    private int count =0;
    public ReceiveMessageThread(Handler handler){

       this.handler = handler;
    }

    @Override
    public void run() {
        try {
            socket = Connect.getSocket();
            InputStream is = socket.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            String data = null;
            while ((len = is.read(b))!=-1) {
                data = new String(b, 0, len);
                System.out.println(data);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = data;
                handler.sendMessage(msg);
                count++;
                if(count==2){
                    throw new IOException();
                }
            }
        } catch (IOException e) {
            System.out.println("退出登录线程");
        }


    }
}
