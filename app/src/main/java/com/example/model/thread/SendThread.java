package com.example.model.thread;


import com.example.utils.ConnectUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * Created by 高峰 on 2017/5/16.
 */

public class SendThread extends Thread {

    private String message;
    private Socket socket;
    public SendThread(String message){

        this.message = message;
    }

    @Override
    public void run() {

        try {
               synchronized(SendThread.class){
                socket = ConnectUtils.getSocket();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(message);
                bw.flush();    //刷新缓冲区
               }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
