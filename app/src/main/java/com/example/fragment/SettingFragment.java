package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.activity.LoginActivity;
import com.example.activity.R;
import com.example.tab.Connect;
import com.example.thread.ReceiveExitMessage;
import com.example.thread.SendThread;

import java.io.IOException;

/**
 * Created gaofeng on 2017/2/11.
 */
public class SettingFragment extends Fragment {
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


         button = (Button) getActivity().findViewById(R.id.exit_login);
         button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendThread("CQ").start();
                System.exit(0);
            }
        });
    }
}
