package com.example.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.model.entity.ChartEntity;
import com.example.presenter.impl.DeviceRequestPresenterImpl;
import com.example.presenter.inter.DeviceInfoPresenter;
import com.example.utils.Flag;
import com.example.utils.HandleInfoUtils;
import com.example.utils.tab.PopupWindows;
import com.example.view.activity.MainActivity;
import com.example.view.activity.R;

import com.example.model.entity.DataInfo;
import com.example.model.entity.DeviceInfo;

import com.example.utils.TimeUtils;
import com.example.view.adapter.DeviceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import expandablerecyclerview.bean.RecyclerViewData;
import me.drakeet.materialdialog.MaterialDialog;

import static com.example.utils.HandleInfoUtils.handlePH;
import static com.example.utils.MachineUtils.getAlarmCode;
import static com.example.utils.TimeUtils.beginFormat;
import static com.example.utils.TimeUtils.disContent;
import static com.example.utils.TimeUtils.endFormat;

import static com.example.utils.TimeUtils.endFormat2;
import static com.example.utils.TimeUtils.getBeforeWeek;
import static com.example.utils.TimeUtils.transform2;


/**
 * Created by gaofeng on 2017/2/11.
 */
public class DeviceFragment extends Fragment implements DeviceInfoPresenter.IDeviceView {
    private DeviceRequestPresenterImpl deviceRequestPresenter;
    private ArrayList<ChartEntity> info = new ArrayList<>();
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private RecyclerView recyclerView;
    private PopupWindows pw_start;
    private PopupWindows pw_end;
    private Button bt;
    private Button bt2;
    public static String start_date;
    public static String end_date;
    private String sel;
    private ProgressDialog progressDialog;
    private List<String> deviceInfo;
    private List<String> detailInfo;
    private boolean flag = true;
    private List<RecyclerViewData> datas = new ArrayList<>();
    private List<DeviceInfo> subItem;
    private DeviceAdapter deviceAdapter;
    private RecyclerViewData<DeviceInfo,DeviceInfo> recyclerViewData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_dev, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //用来恢复数据

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        EventBus.getDefault().register(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在筛选数据");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        deviceRequestPresenter = new DeviceRequestPresenterImpl(this);
        bt = (Button) getActivity().findViewById(R.id.bt);
        bt2 = (Button) getActivity().findViewById(R.id.bt2);
        bt.setText("起始日期\n" + beginFormat(getBeforeWeek()));
        bt2.setText("结束日期\n" + endFormat2());
        deviceInfo = getDeviceInfo();
        detailInfo = getDetailInfo();
        if( null == deviceInfo && null == detailInfo ){
            dialog();
        }
        handlerDetail(detailInfo);
        handlerDevice(deviceInfo);


        MainActivity.tb.select(1,MainActivity.bars);
        sel = getSel();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout2);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        sortByTime(datas);
        deviceAdapter = new DeviceAdapter(getContext(),datas);
        recyclerView.setAdapter(deviceAdapter);
        deviceAdapter.notifyRecyclerViewData();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_start = new PopupWindows(getContext(), v);

            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_end = new PopupWindows(getContext(), v);
                if(MainActivity.infoFragment != null) {
                    android.support.v4.app.FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .remove(MainActivity.infoFragment)
                            .commitAllowingStateLoss();
                    MainActivity.infoFragment = null;
                }
            }

        });



    }


    //处理detail
    public void handlerDetail(List<String> detail){
        if(detail!=null){
            for(int i=0;i<detail.size();i++){
                if(detail.get(i).contains("C0")) {
                    DeviceInfo groupData = HandleInfoUtils.handleSingleSxInfo(detail.get(i));
                    subItem = new ArrayList<>();
                    for (int j = i + 1; j < detail.size(); j++) {
                        if (detail.get(j).contains("C2")) {
                            String s = detail.get(j);
                            subItem.add(HandleInfoUtils.handleSingleDetail(s));
                        } else {
                            recyclerViewData = new RecyclerViewData<DeviceInfo, DeviceInfo>(groupData,subItem);
                            datas.add(recyclerViewData);
                            break;
                        }
                    }

                }
            }
        }
    }



    //处理device
    public void handlerDevice(List<String> device){
         if(device!=null){
             for(int i=0;i<device.size();i++){
                 DeviceInfo groupData = HandleInfoUtils.handlerSingleAlarmInfo(device.get(i));
                 recyclerViewData = new RecyclerViewData<DeviceInfo, DeviceInfo>(groupData,new ArrayList<DeviceInfo>(){});
                 datas.add(recyclerViewData);
             }
         }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDateEvent(String event) {

        if (event.equals(Flag.GETDATESUCCESS)) {
            bt.setText("起始日期\n" + beginFormat(transform2(start_date)));
            bt2.setText("结束日期\n" + endFormat(transform2(end_date)));
            handleTimeRequest(start_date, end_date);
        }else if(event.equals(Flag.SUCCESS)){
            HandleInfoUtils.chartEntities.clear();
            datas.clear();
            handlerDevice(deviceInfo);
            handlerDetail(detailInfo);
            sortByTime(datas);
            deviceAdapter = new DeviceAdapter(getContext(),datas);
            recyclerView.setAdapter(deviceAdapter);
            deviceAdapter.notifyRecyclerViewData();
            progressDialog.dismiss();

        }
    }


    //筛选数据
    public void handleTimeRequest(String start, String end) {
            //重新进行网络请求
            progressDialog.show();
            Log.e("tagfffff","++++++++++");
            deviceRequestPresenter.getDeviceInfo(Integer.parseInt(sel), start_date, end_date);

    }

    //对数据按日期顺序进行排序

    public void sortByTime(List list) {
        Collections.sort(list);
    }



    //得到制水机组以外的数据
    public List<String> getDeviceInfo() {
        try {
            List<String> deviceInfo = getArguments().getStringArrayList("deviceInfo");
            return deviceInfo;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //得到制水机组数据
    public List<String> getDetailInfo() {
        try {
            List<String> detailInfo = getArguments().getStringArrayList("detailInfo");
            return detailInfo;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //获取机房下标
    public String getSel() {
        try {
            String str = getArguments().getString("sel");
            return str;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    //没有机房信息时显示弹窗
    public void dialog() {


        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("请先选择机房信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        android.support.v4.app.FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .remove(MainActivity.deviceFragment)
                                .commitAllowingStateLoss();
                        MainActivity.tb.select(0,MainActivity.bars);
                        MainActivity.tb.switchContent(MainActivity.userFragment);
                        MainActivity.deviceFragment=null;

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        android.support.v4.app.FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .remove(MainActivity.deviceFragment)
                                .commitAllowingStateLoss();
                        MainActivity.tb.select(0,MainActivity.bars);
                        MainActivity.tb.switchContent(MainActivity.userFragment);
                        MainActivity.deviceFragment=null;

                    }
                });
        mMaterialDialog.show();

    }

    //处理水箱信息
//    public void handleSxMessage(String time, String alarmInfo, String sx) {
//        //水箱顺序
//        //原水箱，纯水箱，酸水箱，碱水箱，盐水箱，搅拌箱
//        //Z：离线，3：高水位，2：中水位，1：补水位，0：警戒位
//        if ((!"".equals(sx)) && sx != null) {
//            String ysx = sx.substring(0, 1);
//            String csx = sx.substring(1, 2);
//            String ssx = sx.substring(2, 3);
//            String jsx = sx.substring(3, 4);
//            String yansx = sx.substring(4, 5);
//            String jbx = sx.substring(5, 6);
//
//            deviceInfos.add(new DeviceInfo(disContent(time), "系统信息:" + MachineUtils.getAlarmCode(alarmInfo),
//                    "原水箱:" + sxMsgType(ysx) +
//                            "纯水箱:" + sxMsgType(csx) +
//                            "酸水箱:" + sxMsgType(ssx) + "\n" +
//                            "碱水箱:" + sxMsgType(jsx) +
//                            "盐水箱:" + sxMsgType(yansx) +
//                            "搅拌箱:" + sxMsgType(jbx), false));
//        }
//    }

    //处理报警和故障信息
//    public void handleAlarmInfo(String time, String alarmInfo, String deviceId, String param) {
//        if ((!"".equals(param)) && param != null) {
//            if ("CE".equals(alarmInfo)) {
//                deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
//                        deviceId + "号机" + MachineUtils.getZsjError(param), true));
//            } else {
//                deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
//                        sxId(deviceId) + MachineUtils.getSxError(param), true));
//            }
//        }
//
//    }

    //处理上传数据信息
    public void handleUpLoadMsg(String time, String deviceId, String alarmInfo, DataInfo dataInfo) {

        if (dataInfo != null) {
            deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                    "[PH:" + handlePH(dataInfo.getPh()) + "]" +
                            "[ORP:" + dataInfo.getOrp() + "mv]" +
                            "[有效氯:" + dataInfo.getYxl() + "mg/L]", false));

        }
    }



    public void setter(ArrayList<ChartEntity> list) {

        this.info = list;
    }

    public ArrayList<ChartEntity> getter() {
        return this.info;
    }


    @Override
    public void onSuccess(List<String> deviceInfo, List<String> detailInfo) {

        this.deviceInfo = deviceInfo;
        this.detailInfo = detailInfo;
        EventBus.getDefault().postSticky(Flag.SUCCESS);


    }

    @Override
    public void onFailure(String error) {
        Looper.prepare();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}






