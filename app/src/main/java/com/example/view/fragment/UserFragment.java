package com.example.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.presenter.impl.DeviceInfoPresenterImpl;
import com.example.presenter.inter.DeviceInfoPresenter;
import com.example.view.activity.MainActivity;
import com.example.view.activity.R;
import com.example.view.adapter.UserAdapter;
import com.example.model.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by gaofeng on 2017/2/11.
 */
public class UserFragment extends Fragment  implements DeviceInfoPresenter.IDeviceView{

    private DeviceInfoPresenterImpl deviceInfoPresenter = null;
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deviceInfoPresenter = new DeviceInfoPresenterImpl(this);
          List<UserInfo> list = getUserInfos();
          Log.e("tag44444",list.get(0).getLocation());
          recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout);
          GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
          recyclerView.setLayoutManager(layoutManager);
          adapter = new UserAdapter(list);
          recyclerView.setAdapter(adapter);
          adapter.setOnItemClickListener(new UserAdapter.onRecyclerViewItemClickListener() {
              @Override
              public void onItemClick(View v, String tag) {
                  Toast.makeText(getContext(),tag,Toast.LENGTH_SHORT).show();
//                  MainActivity.deviceFragment = new DeviceFragment();
//                  MainActivity.tb.switchContent(MainActivity.deviceFragment);
                    deviceInfoPresenter.getDeviceInfo(Integer.parseInt(tag));

              }
          });
    }


    public void dialog(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("无法获取机房信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();

                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();

                        System.exit(0);
                    }
                });
        mMaterialDialog.show();
    }

    public String[] getGdtmId() {
        try {
            Intent intent = getActivity().getIntent();
            String[] gdtmid = intent.getStringArrayExtra("gdtm_id");
            return gdtmid;
        }catch (Exception e){

            dialog();
        }
        return null;
    }

    public List<UserInfo> getUserInfos(){
            List<UserInfo> list = new ArrayList<>();
            Intent intent = getActivity().getIntent();
            String[] userInfo = intent.getStringArrayExtra("userInfos");
            for(int i=0;i<userInfo.length;i++){

                String[] info =userInfo[i].split("\\|");
                String machNum = getGdtmId()[i];
                String user = info[0];
                String useraddress = info[1];
                String location= info[2];
                String date = info[3];
                list.add( new UserInfo(machNum,user,useraddress,location,date));
           }

           return list;
    }

    @Override
    public void onSuccess(String deviceInfo) {

    }

    @Override
    public void onFailure(String error) {

    }
}











