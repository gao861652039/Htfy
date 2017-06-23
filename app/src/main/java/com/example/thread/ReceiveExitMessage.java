package com.example.thread;

import android.os.Handler;
import android.os.Message;

import com.example.tab.Connect;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/6/12.
 */

public class ReceiveExitMessage extends Thread{
    private Handler handler;
    private Socket socket;
    public  ReceiveExitMessage(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            socket = Connect.getSocket();
            InputStream is = socket.getInputStream();
            byte[] b  = new byte[1024];
            int len = 0;
            String data = null;
            while((len =is.read(b))!=-1){
                data = new String(b,0,len);
                Message msg =new Message();
                if("#0".equals(data)){
                    msg.what = 1;
                    handler.sendMessage(msg);
                    break;
                }else if("#F".equals(data)){
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

            }
            System.out.println("退出登录");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }

