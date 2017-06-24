package com.example.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.activity.MainActivity;
import com.example.activity.R;
import com.example.adapter.UserAdapter;
import com.example.tab.BottomTabBar;
import com.example.tab.Connect;
import com.example.tab.UserInfo;
import com.example.thread.GdtmExitThread;
import com.example.thread.ReceiveGetUserInfo;
import com.example.thread.SendThread;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by gaofeng on 2017/2/11.
 */
public class UserFragment extends Fragment {
    private ProgressDialog progressDialog;
    private ArrayList<String> macNum = new ArrayList<>();
    private ArrayList<String> userInfo = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private  String[] gdtm_id ;
    private String message;
    private List<UserInfo> list = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
              switch (msg.what){
                  case 1:
                      String str = (String)msg.obj;
                      System.out.println("str:"+str);
                      userInfo.add(str);
                      break;
                  case 2:
                      System.out.println("进入case2");
                      try {
                          for (int i = 0; i < macNum.size(); i++) {
                              String[] user = userInfo.get(i).split("\\|");
                              String macRoom = macNum.get(i);
                              String userName = user[0].split("\\$")[1];
                              String roomAddress = user[1];
                              String userAddress = user[2];
                              String time = user[3];
                              list.add(new UserInfo(macRoom, userName, roomAddress, userAddress, time, View.VISIBLE));
                          }
                      }catch (Exception e){
                          if(userInfo.size()!=0){
                              userInfo.clear();
                              sendRequest();
                          }else{
                              progressDialog.dismiss();
                              dialog();
                          }
                      }finally {
                          adapter = new UserAdapter(list);
                          recyclerView.setAdapter(adapter);
                          adapter.setOnItemClickListener(new UserAdapter.onRecyclerViewItemClickListener() {

                              @Override
                              public void onItemClick(View v, String tag) {
                                  TextView textView = (TextView) v.findViewById(R.id.machineNumber);
                                  String res[] = textView.getText().toString().split(":");
                                  message = res[1];
                                  if (MainActivity.deviceFragment == null) {
                                      if(MainActivity.infoFragment!=null){
//                                          MainActivity.manager.beginTransaction().remove(MainActivity.infoFragment)
//                                                  .hide(MainActivity.infoFragment)
//                                                  .commit();
                                          MainActivity.infoFragment = null;
                                      }
                                      MainActivity.deviceFragment = new DeviceFragment();
                                      Bundle bundle = new Bundle();
                                      bundle.putString("gdtm_id", message);
                                      MainActivity.deviceFragment.setArguments(bundle);
                                      MainActivity.tb.switchContent(MainActivity.deviceFragment);
                                  } else {
                                      if(MainActivity.infoFragment!=null){
//                                          MainActivity.manager.beginTransaction().remove(MainActivity.infoFragment)
//                                                  .hide(MainActivity.infoFragment)
//                                                  .commit();
                                          MainActivity.infoFragment = null;
                                      }
                                      new GdtmExitThread(handler).start();
                                      MainActivity.manager.beginTransaction().remove(MainActivity.deviceFragment)
                                              .hide(MainActivity.deviceFragment)
                                              .commit();

                                      new SendThread("CZ").start();

                                  }
                              }
                          });
                          progressDialog.dismiss();
                      }
                      break;
                  case 3:

                              MainActivity.deviceFragment = new DeviceFragment();
                              Bundle bundle = new Bundle();
                              bundle.putString("gdtm_id", message);
                              MainActivity.deviceFragment.setArguments(bundle);
                              MainActivity.tb.switchContent(MainActivity.deviceFragment);
                      break;

              }
        }
    };
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
        ReceiveGetUserInfo rgu = new ReceiveGetUserInfo(handler);
        rgu.start();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载数据");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        gdtm_id = getGdtmId();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        sendRequest();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
       super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

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
                        new SendThread("CQ").start();
                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        new SendThread("CQ").start();
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

     public void sendRequest(){
         if(macNum.size()!=0){
             macNum.clear();
         }
         for(int i=0;i<gdtm_id.length;i++){

             new SendThread("CB"+gdtm_id[i]).start();
             try {
                 Thread.sleep(10);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             macNum.add(gdtm_id[i]);
         }

     }




}











