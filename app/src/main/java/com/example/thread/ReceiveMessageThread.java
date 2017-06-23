package com.example.thread;

import android.content.Context;
import android.content.Intent;
import com.example.tab.Connect;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by 高峰 on 2017/5/24.
 */

public class ReceiveMessageThread extends  Thread {
    private Socket socket;
    private Context context;
    public boolean exit = false;
    public static int count = 0;
    public ReceiveMessageThread(Context context){
           this.context = context;
    }

    @Override
    public void run() {
        try {
            socket = Connect.getSocket();
            Intent intent;
            InputStream is = socket.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            String data = null;
            while ((len = is.read(b))!=-1) {
                if(exit == true){
                    break;
                }
                data = new String(b, 0, len);
                System.out.println(data);
                if (data != null) {
                    intent = new Intent("RECEIVEMESSAGESUCCESS"+ ++count);
                    System.out.println("count:"+count);
                    intent.putExtra("data",data);
                    context.sendBroadcast(intent);
                }

            }
            System.out.println("退出登录线程");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
