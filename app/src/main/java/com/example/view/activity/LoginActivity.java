package com.example.view.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.entity.UserInfo;
import com.example.presenter.inter.LoginPresenter;
import com.example.presenter.impl.LoginPresenterImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements LoginPresenter.IloginView{
    private LoginPresenterImpl loginPresenter = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEt;
    private EditText pwdEt;
    private Intent  intent ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginPresenter = new LoginPresenterImpl(this);


        pref = getSharedPreferences("data", MODE_PRIVATE);
        accountEt = (EditText) findViewById(R.id.accountEt);
        pwdEt = (EditText) findViewById(R.id.pwdEt);
        String username = pref.getString("username", "");
        final String password = pref.getString("password", "");
        if (!"".equals(username) && !"".equals(password)) {
            accountEt.setText(username);
            pwdEt.setText(password);
        }

    }

    public void login(View view) {

        String username = accountEt.getText().toString();
        String password = pwdEt.getText().toString();
        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        if (!"".equals(username)  && !"".equals(password) ) {
           // message = "CA" + StringUtils.changeType(account.length()) + account + "|" + StringUtils.changeType(password.length()) + password;
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
            loginPresenter.login(username,password);

        }else{
            Toast.makeText(LoginActivity.this,"账户或者密码不能为空",Toast.LENGTH_SHORT).show();
        }

    }


//账户登录结果
    @Override
    public void onSuccess(String[] gdtmId,String[] userInfo) {
        Log.e("tag","登陆成功");

        intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("gdtm_id",gdtmId);
        intent.putExtra("userInfos",userInfo);
        startActivity(intent);


    }

    @Override
    public void onFailure(String s) {
        if ("#E".equals(s)) {
            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
        }
        if("#F".equals(s)){
            Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
        }
    }



}
