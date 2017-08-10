package com.example.view.fragment;


import android.app.ProgressDialog;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.utils.tab.PopupWindows;
import com.example.view.activity.MainActivity;
import com.example.view.activity.R;
import com.example.view.adapter.DeviceAdapter;
import com.example.model.entity.DataInfo;
import com.example.model.entity.DeviceInfo;
import com.example.utils.MachineUtils;
import com.example.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static com.example.utils.MachineUtils.getAlarmCode;
import static com.example.utils.MachineUtils.sxId;
import static com.example.utils.MachineUtils.sxMsgType;
import static com.example.utils.TimeUtils.beginFormat;
import static com.example.utils.TimeUtils.disContent;
import static com.example.utils.TimeUtils.endFormat;

import static com.example.utils.TimeUtils.getBeforeWeek;
import static com.example.utils.TimeUtils.getPresentMonth;
import static com.example.utils.TimeUtils.transform2;


/**
 * Created by gaofeng on 2017/2/11.
 */
public class DeviceFragment extends Fragment implements DeviceInfoPresenter.IDeviceView {
    private DeviceRequestPresenterImpl deviceRequestPresenter;
    private ArrayList<ChartEntity> info = new ArrayList<>();
    private ArrayList<ChartEntity> chartEntities = new ArrayList<>();
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private List<DeviceInfo> infosAfterSelected = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("onCreateView","onCreateView");
        View view = inflater.inflate(R.layout.fragment_dev, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("onActivityCreated","onActivityCreated");
        EventBus.getDefault().register(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在筛选数据");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        deviceRequestPresenter = new DeviceRequestPresenterImpl(this);
        bt = (Button) getActivity().findViewById(R.id.bt);
        bt2 = (Button) getActivity().findViewById(R.id.bt2);
        bt.setText("起始日期\n" + beginFormat(getBeforeWeek()));
        bt2.setText("结束日期\n" + endFormat(getPresentMonth()));
        handleDeviceInfo(getDeviceInfo());
        handDetailInfo(getDetailInfo());
        sel = getSel();
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout2);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        sortByTime(deviceInfos);
        deviceAdapter = new DeviceAdapter(deviceInfos);
        recyclerView.setAdapter(deviceAdapter);

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


            }

        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDateEvent(String event) {
        if (event.equals(Flag.GETDATESUCCESS)) {
            Log.e("timeUtils",TimeUtils.toStart(start_date));
            bt.setText("起始日期\n" + beginFormat(transform2(start_date)));
            bt2.setText("结束日期\n" + endFormat(transform2(end_date)));

            int flag = handleTimeRequest(TimeUtils.toStart(start_date), TimeUtils.toEnd(end_date));
            if (flag == 1) {
                sortByTime(infosAfterSelected);
                Log.e("length",""+infosAfterSelected.size());
                deviceAdapter = new DeviceAdapter(infosAfterSelected);
                recyclerView.setAdapter(deviceAdapter);
                deviceAdapter.notifyDataSetChanged();
            }

        }else if(event.equals(Flag.SUCCESS)){

            deviceInfos.clear();
            handleDeviceInfo(deviceInfo);
            handDetailInfo(detailInfo);
            sortByTime(deviceInfos);
            deviceAdapter = new DeviceAdapter(deviceInfos);
            recyclerView.setAdapter(deviceAdapter);
            deviceAdapter.notifyDataSetChanged();
            progressDialog.dismiss();


        }
    }


    //筛选数据
    public int handleTimeRequest(String start, String end) {
        System.out.println(start);
        System.out.println(end);

        if (start.compareTo(TimeUtils.start_date())>=0 && start.compareTo(TimeUtils.end_date())<=0 && end.compareTo(TimeUtils.end_date())<=0) {
            //对原数据按照时间进行筛选
            infosAfterSelected.clear();
            for (int i = 0; i < deviceInfos.size(); i++) {
                String time = deviceInfos.get(i).getTime();
                if (time.compareTo(TimeUtils.disContent(start)) >= 0 && time.compareTo(TimeUtils.disContent(end))<=0) {
                    infosAfterSelected.add(deviceInfos.get(i));
                }
            }

            return 1;
        } else {
            //重新进行网络请求
            progressDialog.show();
            deviceRequestPresenter.getDeviceInfo(Integer.parseInt(sel), start_date, end_date);
            return 0;
        }


    }

    //对数据按日期顺序进行排序

    public void sortByTime(List<DeviceInfo> list) {
        Collections.sort(list);
    }


    //处理C2以外指令
    public void handleDeviceInfo(List<String> deviceInfo) {
      if(deviceInfo!=null) {
          for (String str : deviceInfo) {

              String[] info = str.split("\\|");
              String time = info[0].split("\\$")[1];
              String alarmInfo = info[1];
              String deviceId = info[2];
              if (str.contains("C0") || str.contains("C1") || str.contains("C6")) {
                  String sx = info[9];
                  handleSxMessage(time, alarmInfo, sx);
              }

              if (str.contains("CT") || str.contains("CE")) {
                  String param = info[3];
                  handleAlarmInfo(time, alarmInfo, deviceId, param);
              }
          }
      }
    }

    //处理C2指令
    public void handDetailInfo(List<String> detailInfo) {

        if(detailInfo!=null) {

            for (String str : detailInfo) {
                String[] info = str.split("\\|");
                String time = info[0].split("\\$")[1];
                String alarmInfo = info[1];
                String deviceId = info[2];
                String ph = info[3];
                String orp = info[4];
                String yxl = info[5];
                String dl = info[6];
                String djc = info[7];
                String yb = info[8];
                handleUpLoadMsg(time, deviceId, alarmInfo, new DataInfo(ph, orp, yxl, dl, djc, yb));
            }
        }
    }


    //得到制水机组以外的数据
    public List<String> getDeviceInfo() {
        try {
            List<String> deviceInfo = getArguments().getStringArrayList("deviceInfo");
            return deviceInfo;
        } catch (NullPointerException e) {
            dialog();
        }
        return null;
    }

    //得到制水机组数据
    public List<String> getDetailInfo() {
        try {
            List<String> detailInfo = getArguments().getStringArrayList("detailInfo");
            return detailInfo;
        } catch (NullPointerException e) {
            dialog();
        }
        return null;
    }

    //获取机房下标
    public String getSel() {
        try {
            String str = getArguments().getString("sel");
            return str;
        } catch (NullPointerException e) {
            dialog();
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
                        MainActivity.manager.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        MainActivity.tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        MainActivity.manager.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        MainActivity.tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();
    }

    //处理水箱信息
    public void handleSxMessage(String time, String alarmInfo, String sx) {
        //水箱顺序
        //原水箱，纯水箱，酸水箱，碱水箱，盐水箱，搅拌箱
        //Z：离线，3：高水位，2：中水位，1：补水位，0：警戒位
        if ((!"".equals(sx)) && sx != null) {
            String ysx = sx.substring(0, 1);
            String csx = sx.substring(1, 2);
            String ssx = sx.substring(2, 3);
            String jsx = sx.substring(3, 4);
            String yansx = sx.substring(4, 5);
            String jbx = sx.substring(5, 6);

            deviceInfos.add(new DeviceInfo(disContent(time), "系统信息:" + MachineUtils.getAlarmCode(alarmInfo),
                    "原水箱:" + sxMsgType(ysx) +
                            "纯水箱:" + sxMsgType(csx) +
                            "酸水箱:" + sxMsgType(ssx) + "\n" +
                            "碱水箱:" + sxMsgType(jsx) +
                            "盐水箱:" + sxMsgType(yansx) +
                            "搅拌箱:" + sxMsgType(jbx), false));
        }
    }

    //处理报警和故障信息
    public void handleAlarmInfo(String time, String alarmInfo, String deviceId, String param) {
        if ((!"".equals(param)) && param != null) {
            if ("CE".equals(alarmInfo)) {
                deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                        deviceId + "号机" + MachineUtils.getZsjError(param), true));
            } else {
                deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                        sxId(deviceId) + MachineUtils.getSxError(param), true));
            }
        }

    }

    //处理上传数据信息
    public void handleUpLoadMsg(String time, String deviceId, String alarmInfo, DataInfo dataInfo) {

        if (dataInfo != null) {
            deviceInfos.add(new DeviceInfo(disContent(time), getAlarmCode(alarmInfo),
                    "[PH:" + handlePH(dataInfo.getPh()) + "]" +
                            "[ORP:" + dataInfo.getOrp() + "mv]" +
                            "[有效氯:" + dataInfo.getYxl() + "mg/L]", false));
            chartEntities.add(new ChartEntity(deviceId
                    , disContent(time)
                    , transToFloat(handlePH(dataInfo.getPh()))
                    , transToFloat(dataInfo.getOrp())
                    , transToFloat(dataInfo.getYxl())));
            setter(chartEntities);
        }
    }

    public String handlePH(String ph) {
        float a = (Float.parseFloat(ph)) / 10;
        return String.valueOf(a);
    }

    //将String转换为float
    public float transToFloat(String s) {
        float a = Float.parseFloat(s);
        return a;
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






