package com.example.view.activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.presenter.inter.CheckPresenter;
import com.example.presenter.inter.LoginPresenter;
import com.example.presenter.impl.CheckPresenterImpl;
import com.example.presenter.impl.LoginPresenterImpl;
import com.example.utils.StringUtils;


public class LoginActivity extends AppCompatActivity implements LoginPresenter.IloginView,CheckPresenter.IcheckView{
    private LoginPresenterImpl loginPresenter = null;
    private CheckPresenterImpl checkPresenter = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String flag = null;
    private EditText accountEt;
    private EditText pwdEt;
    private String message = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginPresenter = new LoginPresenterImpl(this);
        checkPresenter = new CheckPresenterImpl(this);

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

    public void login(View view) {

        String account = accountEt.getText().toString();
        String password = pwdEt.getText().toString();
        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        if (!"".equals(account)  && !"".equals(password) ) {
            message = "CA" + StringUtils.changeType(account.length()) + account + "|" + StringUtils.changeType(password.length()) + password;
            editor.putString("account", account);
            editor.putString("password", password);
            editor.apply();
            flag = "V50";
            checkPresenter.getStatus(flag);
        }else{
            Toast.makeText(LoginActivity.this,"账户或者密码不能为空",Toast.LENGTH_SHORT).show();
        }

    }


//账户登录结果
    @Override
    public void onSuccess(String[] gdtmId) {
        Log.e("tag","登陆成功");
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("gdtm_id",gdtmId);
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
//通信协议验证
    @Override
    public void onCheckedSuccess() {
        loginPresenter.getGdtmId(message);
    }

    @Override
    public void onCheckedFailure() {
        Log.e("tag","通信版本不正确");
    }
}
