package com.example.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.view.activity.R;

/**
 * Created gaofeng on 2017/2/11.
 */
public class SettingFragment extends Fragment {
    private Button button;
    private TextView company_info;

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
         company_info = (TextView) getActivity().findViewById(R.id.company_info);
         company_info.setText("宏泰丰业消毒制水监控 v1.1\n" +
                 "\n" +
                 "河北宏泰丰业医疗器械有限公司 版权所有\n" +
                 "www.hbhtfy.com.cn\n" +
                 "联系电话 4006270630");
         button = (Button) getActivity().findViewById(R.id.exit_login);
         button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });
    }
}
