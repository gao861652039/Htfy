package com.example.thread;

import android.os.Handler;
import android.os.Message;

import com.example.tab.Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;

/**
 * Created by 高峰 on 2017/6/14.
 */

public class ReceiveGetUserInfo extends Thread {
    private Socket socket;
    private Handler handler;
    public boolean exit = false;
    private  String data = "";
    public ReceiveGetUserInfo(Handler handler){
        this.handler = handler;
    }


    public void run() {
        try {
            socket = Connect.getSocket();
            InputStream is = socket.getInputStream();
            socket.setSoTimeout(2000);
            int len = 0;

            byte[] b = new byte[1024];
            byte[] c = new byte[1];

              while ((len = is.read(b)) != -1) {
                  data += new String(b, 0, len);
                  if (!(b[5] == c[0])) {
                      Message msg = new Message();
                      msg.what = 1;
                      msg.obj = data;
                      handler.sendMessage(msg);
                      data = "";
                  }
              }
        } catch (IOException e) {
            System.out.println("没有更多数据接收，线程退出");
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }


    }
}
