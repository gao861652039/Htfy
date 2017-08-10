package com.example.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.utils.ActivityManager;
import com.example.view.fragment.DeviceFragment;
import com.example.view.fragment.InfoFragment;
import com.example.view.fragment.SettingFragment;
import com.example.view.fragment.UserFragment;
import com.example.utils.tab.BarEntity;
import com.example.utils.tab.BottomTabBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements BottomTabBar.OnSelectListener {
    public static BottomTabBar tb;
    public static  List<BarEntity> bars;
    public static UserFragment userFragment;
    public static DeviceFragment deviceFragment;
    private SettingFragment settingFragment;
    public static FragmentManager manager;
    public static InfoFragment infoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        manager = getSupportFragmentManager();
        tb = (BottomTabBar) findViewById(R.id.tb);
        bars = new ArrayList<>();
        bars.add(new BarEntity("用户", R.drawable.pic_user, R.drawable.pic_user));
        bars.add(new BarEntity("日志", R.drawable.pic_device, R.drawable.pic_device));
        bars.add(new BarEntity("数据", R.drawable.pic_info, R.drawable.pic_info));
        bars.add(new BarEntity("设置", R.drawable.pic_setting, R.drawable.pic_setting));
        tb.setManager(manager).setOnSelectListener(this).setBars(bars);
    }

    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                if (userFragment == null) {
                    userFragment = new UserFragment();
                }
                tb.switchContent(userFragment);
                break;
            case 1:
                if (deviceFragment == null) {
                    deviceFragment = new DeviceFragment();
                }
                tb.switchContent(deviceFragment);
                break;
            case 2:
                if (infoFragment == null) {
                    infoFragment = new InfoFragment();
                }
                tb.switchContent(infoFragment);
                break;

            case 3:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                }
                tb.switchContent(settingFragment);
                break;
            default:
                break;
        }
    }
    //--------------使用onKeyUp()干掉他--------------

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    ActivityManager.removeAllActivity();
                    this.finish();
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}


