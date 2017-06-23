package com.example.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.activity.LoginActivity;
import com.example.activity.MainActivity;
import com.example.thread.SendThread;



/**
 * Created by 高峰 on 2017/5/24.
 */

public class MyHandler  extends Handler {
       private Context context;

       public MyHandler(){

       }

       public MyHandler(Context context){
           this.context = context;
       }



    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){

            case 1:
                System.out.println("this is myHandler");
                String str = (String) msg.obj;
                System.out.println("Received:" + str);
                if ("#0".equals(str)) {
                    new SendThread(LoginActivity.message).start();
                }else{
                    Toast.makeText(context,"通信版本错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                String str2 = (String) msg.obj;
                if (!"#E".equals(str2) && !"#F".equals(str2)) {
                    String[] data = str2.split("\\$");
                    int num = Integer.valueOf(data[0].substring(2,4));
                    String[] gdtm_id = new String[num];
                    for(int i=0;i<gdtm_id.length;i++){
                        gdtm_id[i] = data[i+1];
                    }
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("number",num);
                    intent.putExtra("gdtm_id",gdtm_id);
                    context.startActivity(intent);
                }else if ("#E".equals(str2)){
                    Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();

                }else if("#F".equals(str2)){
                    Toast.makeText(context,"ID不存在",Toast.LENGTH_SHORT).show();
                }
                break;







        }
    }



}
