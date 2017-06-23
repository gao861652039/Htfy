package com.example.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.service.MyService;
import com.example.thread.SendThread;



public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEt;
    private EditText pwdEt;
    public static String message = null;
    public static String data = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("RECEIVEMESSAGESUCCESS1".equals(action)) {
                String str1 = intent.getStringExtra("data");
                if ("#0".equals(str1)) {
                    new SendThread(message).start();
                }else{
                    Toast.makeText(LoginActivity.this,"通信版本错误",Toast.LENGTH_SHORT).show();
                }
            }else if("RECEIVEMESSAGESUCCESS2".equals(action)){
                String str2 = intent.getStringExtra("data");
                if (!"#E".equals(str2) && !"#F".equals(str2)) {
                    String[] data = str2.split("\\$");
                    int num = Integer.valueOf(data[0].substring(2,4));
                    String[] gdtm_id = new String[num];
                    for(int i=0;i<gdtm_id.length;i++){
                        gdtm_id[i] = data[i+1];
                    }
                    Intent intent2 = new Intent(context,MainActivity.class);
                    intent2.putExtra("number",num);
                    intent2.putExtra("gdtm_id",gdtm_id);
                    context.startActivity(intent2);

                }else if ("#E".equals(str2)){
                    Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show();

                }else if("#F".equals(str2)){
                    Toast.makeText(context,"ID不存在",Toast.LENGTH_SHORT).show();
                }


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        accountEt = (EditText) findViewById(R.id.accountEt);
        pwdEt = (EditText) findViewById(R.id.pwdEt);
        String account = pref.getString("account", "");
        final String password = pref.getString("password", "");
        if (!"".equals(account) && !"".equals(password)) {
            accountEt.setText(account);
            pwdEt.setText(password);
        }
       Intent bindIntent = new Intent(this,MyService.class);
       startService(bindIntent);


    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RECEIVEMESSAGESUCCESS1");
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("RECEIVEMESSAGESUCCESS2");
        registerReceiver(mReceiver, intentFilter);
        registerReceiver(mReceiver, intentFilter2);
    }
    //解绑服务
    public void unBindService(){
        Intent unBind =  new Intent(this, MyService.class);
        stopService(unBind);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
        unregisterReceiver(mReceiver);

    }

    public void login(View view) {
        String account = accountEt.getText().toString();
        String password = pwdEt.getText().toString();
        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        if (account != null && password != null) {
            message = "CA" + changeType(account.length()) + account + "|" + changeType(password.length()) + password;
            editor.putString("account", account);
            editor.putString("password", password);
            editor.apply();
            new SendThread("V50").start();
        }


    }

    public String changeType(int num) {
        String str = "" + num;
        if (str.length() == 1) {
            return "0" + str;
        } else {
            return str;
        }
    }

}
