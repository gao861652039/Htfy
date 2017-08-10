package com.example.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private ProgressDialog progressDialog;
    private String flag = null;

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("获取机房数据中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


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

                    progressDialog.show();
                    deviceInfoPresenter.getDeviceInfo(Integer.parseInt(tag));
                    flag = tag;
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
    public void onSuccess(List<String> deviceInfo, List<String> detailInfo) {
          progressDialog.dismiss();
          if(  MainActivity.deviceFragment == null){
            MainActivity.deviceFragment = new DeviceFragment();
          }else{
              FragmentManager manager = getFragmentManager();
              manager.beginTransaction().remove(MainActivity.deviceFragment).commitAllowingStateLoss();
              MainActivity.deviceFragment = new DeviceFragment();
          }

          Bundle bundle = new Bundle();
          bundle.putStringArrayList("deviceInfo", (ArrayList<String>) deviceInfo);
          bundle.putStringArrayList("detailInfo", (ArrayList<String>) detailInfo);
          bundle.putString("sel",flag);
          MainActivity.deviceFragment.setArguments(bundle);
          MainActivity.tb.switchContent(MainActivity.deviceFragment);
    }

    @Override
    public void onFailure(String error) {

    }
}











