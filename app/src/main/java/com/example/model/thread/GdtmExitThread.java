package com.example.model.thread;

import android.os.Handler;
import android.os.Message;

import com.example.utils.ConnectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/6/24.
 */

public class GdtmExitThread extends Thread {
    private Socket socket;
    private String data = "";
    private Handler handler;

    public GdtmExitThread(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        try {
            socket = ConnectUtils.getSocket();
            InputStream is = socket.getInputStream();
            int len = 0;

            byte[] b = new byte[1024];
            byte[] c = new byte[1];

            while ((len = is.read(b)) != -1) {
                data = new String(b, 0, len);
                if ("#0".equals(data)) {
                    throw new IOException();
                }
            }
        } catch (IOException e) {
            System.out.println("当前机房退出");
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }
}
