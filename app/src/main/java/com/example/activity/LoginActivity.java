package com.example.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.thread.ReceiveMessageThread;
import com.example.thread.SendThread;



public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SendThread st;
    private SharedPreferences.Editor editor;
    private String flag = null;
    private EditText accountEt;
    private EditText pwdEt;
    public static String message = null;
    public static String data = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String str = (String)msg.obj;
                    if("V50".equals(flag)) {
                        if ("#0".equals(str)) {
                            flag = message;
                             new SendThread(message).start();

                        } else {
                            Toast.makeText(LoginActivity.this, "通信版本错误", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (!"#E".equals(str) && !"#F".equals(str)) {
                            String[] data = str.split("\\$");
                            String[] gdtm_id = new String[data.length-1];
                            for(int i=0;i<gdtm_id.length;i++){
                                    gdtm_id[i] = data[i + 1];
                            }
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("number",data.length-1);
                            intent.putExtra("gdtm_id",gdtm_id);
                            startActivity(intent);
                        }else if ("#E".equals(str)){
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();

                        }else if("#F".equals(str)){
                            Toast.makeText(LoginActivity.this,"ID不存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ReceiveMessageThread  rmt = new ReceiveMessageThread(handler);
        rmt.start();
        pref = getSharedPreferences("data", MODE_PRIVATE);
        accountEt = (EditText) findViewById(R.id.accountEt);
        pwdEt = (EditText) findViewById(R.id.pwdEt);
        String account = pref.getString("account", "");
        final String password = pref.getString("password", "");
        if (!"".equals(account) && !"".equals(password)) {
            accountEt.setText(account);
            pwdEt.setText(password);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();



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
            flag = "V50";
            new SendThread(flag).start();
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
