package com.example.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.presenter.impl.DeviceInfoPresenterImpl;
import com.example.presenter.inter.DeviceInfoPresenter;
import com.example.utils.tab.PopupWindows;
import com.example.view.activity.MainActivity;
import com.example.view.activity.R;
import com.example.view.adapter.DeviceAdapter;
import com.example.model.entity.DataInfo;
import com.example.model.entity.DeviceInfo;
import com.example.utils.tab.KCalendar;
import com.example.utils.MachineUtils;
import com.example.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import static com.example.utils.MachineUtils.getAlarmCode;
import static com.example.utils.MachineUtils.sxId;
import static com.example.utils.MachineUtils.sxMsgType;
import static com.example.utils.TimeUtils.beginFormat;
import static com.example.utils.TimeUtils.endFormat;
import static com.example.utils.TimeUtils.getBeforeForm;
import static com.example.utils.TimeUtils.getBeforeMonth;
import static com.example.utils.TimeUtils.getPresentMonth;
import static com.example.utils.TimeUtils.sameDate;
import static com.example.utils.TimeUtils.transform;
import static com.example.utils.TimeUtils.transform2;

/**
 * Created by gaofeng on 2017/2/11.
 */
public class DeviceFragment extends Fragment implements DeviceInfoPresenter.IDeviceView{

    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private ProgressDialog progressDialog = null;
    private Date date_start = null;
    private Date date_end = null;
    private List<String> list = new ArrayList<>();
    private StringBuilder sb = new StringBuilder(1024 * 1024);
    private int id;
    private RecyclerView recyclerView;
    private  PopupWindows pw;
    @BindView(R.id.bt)
    Button bt;
    @BindView(R.id.bt2)
    Button bt2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        System.out.println("onCreateView");
        View view = inflater.inflate(R.layout.fragment_dev, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(getActivity());
        bt = (Button) getActivity().findViewById(R.id.bt);
        bt2 = (Button) getActivity().findViewById(R.id.bt2);
        bt.setText("起始日期\n" + beginFormat(getBeforeMonth()));
        bt2.setText("结束日期\n" + endFormat(getPresentMonth()));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw = new PopupWindows(getContext(),v);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw = new PopupWindows(getContext(),v);
            }
        });
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout2);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
    }












    //得到机房Id
    public String getGdtmId() {
        String gdtm = null;
        try {
            gdtm = getArguments().getString("gdtm_id");
            return gdtm;
        } catch (NullPointerException e) {

            progressDialog.dismiss();
            dialog();

        }
        return gdtm;
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

            deviceInfos.add(new DeviceInfo(TimeUtils.disContent(time), "系统信息:" + MachineUtils.getAlarmCode(alarmInfo),
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
                deviceInfos.add(new DeviceInfo(TimeUtils.disContent(time), getAlarmCode(alarmInfo),
                        deviceId + "号机" + MachineUtils.getZsjError(param), true));
            } else {
                deviceInfos.add(new DeviceInfo(TimeUtils.disContent(time), getAlarmCode(alarmInfo),
                        sxId(deviceId) + MachineUtils.getSxError(param), true));
            }
        }

    }

    //处理上传数据信息
    public void handleUpLoadMsg(String time, String alarmInfo, DataInfo dataInfo) {

        if (dataInfo != null) {
            deviceInfos.add(new DeviceInfo(TimeUtils.disContent(time), getAlarmCode(alarmInfo),
                    "[PH:" + handlePH(dataInfo.getPh()) + "]" +
                            "[ORP:" + dataInfo.getOrp() + "mv]" +
                            "[有效氯:" + dataInfo.getYxl() + "mg/L]", false));
        }
    }

    public String handlePH(String ph) {
        float a = (Float.parseFloat(ph)) / 10;
        return String.valueOf(a);
    }

    @Override
    public void onSuccess(String deviceInfo) {

    }

    @Override
    public void onFailure(String error) {

    }
}






