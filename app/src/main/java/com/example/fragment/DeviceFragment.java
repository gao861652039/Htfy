package com.example.fragment;

import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.example.activity.MainActivity;
import com.example.activity.R;
import com.example.adapter.DeviceAdapter;
import com.example.tab.BottomTabBar;
import com.example.tab.DeviceInfo;
import com.example.tab.KCalendar;
import com.example.thread.ReceiveWorkMessage;
import com.example.thread.SendThread;
import com.mylhyl.superdialog.SuperDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by gaofeng on 2017/2/11.
 */
public class DeviceFragment extends Fragment {
    private ReceiveWorkMessage rwm;
    private ArrayList<String> ph = new ArrayList<>();
    private ArrayList<String> orp = new ArrayList<>();
    private ArrayList<String> yxl = new ArrayList<>();
    private ArrayList<String> current = new ArrayList<>();
    private BottomTabBar tb;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private ProgressDialog progressDialog;
    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private Date date_start = null;
    private Date date_end = null;
    private List<String> list = new ArrayList<>();
    private StringBuilder sb = new StringBuilder(1024*1024);
    private int id;
    private Button bt;
    private Button bt2;
    private RecyclerView recyclerView;
    private String gdtmId = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
             switch (msg.what){
                 case 1:
                     String str = (String)msg.obj;
                     System.out.println("str:"+str);
                     if("#0".equals(str)){
                         new SendThread("C5$"+getBeforeForm()+"0000").start();
                     }else if("#F".equals(str)){
                          progressDialog.dismiss();
                          dialog2();
                     }else {
                         sb.append(str);
                         new SendThread("CC").start();
                     }
                     break;
                 case 2:

                     date_start = getBeforeMonth();
                     date_end = getPresentMonth();
                     String[] s = sb.toString().split("#@");

                     System.out.println(0+":"+s[0]);
                     for(int i=1;i<s.length;i++){
                         System.out.println(i+":"+s[i]);
                         String[] arr = s[i].split("\\$");
                         try {
                             list.add(arr[1]);
                         }catch (ArrayIndexOutOfBoundsException e){
                             System.out.println("本条数据异常");
                             continue;
                         }
                     }
                     for(int i=0;i<list.size();i++){
                         System.out.println(list.get(i));
                     }
                     deviceInfos.clear();
                     ph.clear();
                     orp.clear();
                     yxl.clear();
                     current.clear();
                     for(int i=0;i<list.size();i++){
                         String[] arr = list.get(i).split("\\|");
                         String time = arr[0];
                         String deviceId = arr[1];
                         String alarmInfo = arr[2];
                         Date subTime = transform(time.substring(0,8));

                         if((subTime.before(date_end) && subTime.after(date_start))|| sameDate(subTime,date_start) || sameDate(subTime,date_end)){

                             if("C2".equals(alarmInfo)){
                                 ph.add(arr[3]);
                                 orp.add(arr[4]);
                                 yxl.add(arr[5]);
                                 current.add(arr[6]);
                                 deviceInfos.add(new DeviceInfo("制水机",deviceId,time,getAlarmCode(alarmInfo)));
                             }else{
                                 if("CE".equals(alarmInfo)){
                                     deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getZsjError(arr[3])));
                                 }else if("CT".equals(alarmInfo)){
                                     deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getSxAlarmType(arr[3])));
                                 }else{
                                     deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getAlarmCode(alarmInfo)));
                                 }

                             }
                         }
                     }

                     deviceAdapter = new DeviceAdapter(deviceInfos);
                     deviceAdapter.setOnItemClickListener(new DeviceAdapter.onRecyclerViewItemClickListener() {
                         @Override
                         public void onItemClick(View v, String tag) {

                              TextView textView1 = (TextView) v.findViewById(R.id.device_id);
                              TextView textView2 = (TextView) v.findViewById(R.id.time);
                              TextView textView3 = (TextView) v.findViewById(R.id.alarm_info);
                              String deviceName  = textView1.getText().toString();
                              String time = textView2.getText().toString();
                              String alarmInfo = textView3.getText().toString();
                             if(MainActivity.infoFragment==null){
                                 MainActivity.infoFragment = new InfoFragment();
                             }else{
                                 FragmentManager fm = getFragmentManager();
                                 fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                                 MainActivity.infoFragment = new InfoFragment();
                             }
                             if("运行数据".equals(alarmInfo.split(":")[1])){
                                 Bundle bundle = new Bundle();
                                 bundle.putStringArrayList("ph", ph);
                                 bundle.putStringArrayList("orp", orp);
                                 bundle.putStringArrayList("yxl", yxl);
                                 bundle.putStringArrayList("current", current);
                                 MainActivity.infoFragment.setArguments(bundle);
                                 tb.switchContent(MainActivity.infoFragment);
                             }else{
                                final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                                 mMaterialDialog.setTitle("具体内容")
                                         .setMessage(deviceName+"\n"+time+"\n"+alarmInfo)
                                         .setPositiveButton("确定", new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 mMaterialDialog.dismiss();

                                             }
                                         })
                                         .setNegativeButton("取消", new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 mMaterialDialog.dismiss();

                                             }
                                         });

                                 mMaterialDialog.show();



                             }

                         }
                     });
                     recyclerView.setAdapter(deviceAdapter);
                     deviceAdapter.notifyDataSetChanged();
                     progressDialog.dismiss();
                     break;
                 case 3:
                      System.out.println("case3");
                      System.out.println("datestart:"+date_start);
                      System.out.println("dateend:"+date_end);
                      deviceInfos.clear();
                      ph.clear();
                      orp.clear();
                      yxl.clear();
                      current.clear();
                      for(int i=0;i<list.size();i++){
                          String[] arr = list.get(i).split("\\|");
                          String time = arr[0];
                          String deviceId = arr[1];
                          String alarmInfo = arr[2];
                          Date subTime = transform(time.substring(0,8));
                          if((subTime.before(date_end) && subTime.after(date_start))|| sameDate(subTime,date_start) || sameDate(subTime,date_end)){
                              if("C2".equals(alarmInfo)){
                                  ph.add(arr[3]);
                                  orp.add(arr[4]);
                                  yxl.add(arr[5]);
                                  current.add(arr[6]);
                                  deviceInfos.add(new DeviceInfo("制水机",deviceId,time,getAlarmCode(alarmInfo)));
                              }else{
                                  if("CE".equals(alarmInfo)){
                                      deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getZsjError(arr[3])));
                                  }else if("CT".equals(alarmInfo)){
                                      deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getSxAlarmType(arr[3])));
                                  }else{
                                      deviceInfos.add(new DeviceInfo(getDeviceName(deviceId),deviceId,time,getAlarmCode(alarmInfo)));
                                  }

                              }
                          }
                      }
                     deviceAdapter = new DeviceAdapter(deviceInfos);
                     deviceAdapter.setOnItemClickListener(new DeviceAdapter.onRecyclerViewItemClickListener() {
                         @Override
                         public void onItemClick(View v, String tag) {
                             TextView textView1 = (TextView) v.findViewById(R.id.device_id);
                             TextView textView2 = (TextView) v.findViewById(R.id.time);
                             TextView textView3 = (TextView) v.findViewById(R.id.alarm_info);
                             String deviceName  = textView1.getText().toString();
                             String time = textView2.getText().toString();
                             String alarmInfo = textView3.getText().toString();
                             if(MainActivity.infoFragment==null){
                                 MainActivity.infoFragment = new InfoFragment();
                             }else{
                                 FragmentManager fm = getFragmentManager();
                                 fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                                 MainActivity.infoFragment = new InfoFragment();
                             }
                             if("运行数据".equals(alarmInfo.split(":")[1])){
                                 Bundle bundle = new Bundle();
                                 bundle.putStringArrayList("ph", ph);
                                 bundle.putStringArrayList("orp", orp);
                                 bundle.putStringArrayList("yxl", yxl);
                                 bundle.putStringArrayList("current", current);
                                 MainActivity.infoFragment.setArguments(bundle);
                                 tb.switchContent(MainActivity.infoFragment);
                             }else{
                                 final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                                 mMaterialDialog.setTitle("具体内容")
                                         .setMessage(deviceName+"\n"+time+"\n"+alarmInfo)
                                         .setPositiveButton("确定", new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 mMaterialDialog.dismiss();

                                             }
                                         })
                                         .setNegativeButton("取消", new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 mMaterialDialog.dismiss();

                                             }
                                         });

                                 mMaterialDialog.show();

                             }
                         }
                     });
                     recyclerView.setAdapter(deviceAdapter);
                     deviceAdapter.notifyDataSetChanged();
                     break;


             }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dev, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tb = (BottomTabBar) getActivity().findViewById(R.id.tb);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在加载数据!");
        progressDialog.show();
         gdtmId = getGdtmId();
        System.out.println("gdtmId:"+gdtmId);
        if(gdtmId != null) {
         new SendThread("C9" + gdtmId).start();
        }

        rwm = new ReceiveWorkMessage(handler);
        rwm.start();

        bt = (Button) getActivity().findViewById(R.id.bt);
        bt2 = (Button) getActivity().findViewById(R.id.bt2);

        //起始
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PopupWindows(getContext(),bt);
                id = v.getId();
            }
        });
         //结束
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupWindows(getContext(),bt);
                id = v.getId();
            }
        });
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardLayout2);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

    }
     @Override
     public void onResume() {
         super.onResume();
     }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rwm.interrupt();
    }

    public String getGdtmId(){
        String gdtm = null;
        try {
               gdtm = getArguments().getString("gdtm_id");
               return gdtm;
            }catch (NullPointerException e){
                System.out.println("未正常进入页面");
                progressDialog.dismiss();
            }finally {
                 if(gdtm == null){
                     dialog();
                 }
             }
        return gdtm;
    }


    public void dialog(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("请先选择机房信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();
    }

    public void dialog2(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("机房不存在")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.deviceFragment).commit();
                        tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();
    }




    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.popupwindow_calendar,
                    null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_1));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            final TextView popupwindow_calendar_month = (TextView) view
                    .findViewById(R.id.popupwindow_calendar_month);
            final KCalendar calendar = (KCalendar) view
                    .findViewById(R.id.popupwindow_calendar);
            Button popupwindow_calendar_bt_enter = (Button) view
                    .findViewById(R.id.popupwindow_calendar_bt_enter);

            popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                    + calendar.getCalendarMonth() + "月");

            if (null != date) {

                int years = Integer.parseInt(date.substring(0,
                        date.indexOf("-")));
                int month = Integer.parseInt(date.substring(
                        date.indexOf("-") + 1, date.lastIndexOf("-")));
                popupwindow_calendar_month.setText(years + "年" + month + "月");

                calendar.showCalendar(years, month);
                calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
            }

            List<String> list = new ArrayList<String>(); //设置标记列表
            list.add("2014-04-01");
            list.add("2014-04-02");
            calendar.addMarks(list, 0);

            //监听所选中的日期
            calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

                public void onCalendarClick(int row, int col, String dateFormat) {
                    int month = Integer.parseInt(dateFormat.substring(
                            dateFormat.indexOf("-") + 1,
                            dateFormat.lastIndexOf("-")));

                    if (calendar.getCalendarMonth() - month == 1//跨年跳转
                            || calendar.getCalendarMonth() - month == -11) {
                        calendar.lastMonth();

                    } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                            || month - calendar.getCalendarMonth() == -11) {
                        calendar.nextMonth();

                    } else {
                        calendar.removeAllBgColor();
                        calendar.setCalendarDayBgColor(dateFormat,
                                R.drawable.calendar_date_focused);
                        date = dateFormat;//最后返回给全局 date
                    }
                }
            });

            //监听当前月份
            calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
                public void onCalendarDateChanged(int year, int month) {
                    popupwindow_calendar_month
                            .setText(year + "年" + month + "月");
                }
            });

            //上月监听按钮
            RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                    .findViewById(R.id.popupwindow_calendar_last_month);
            popupwindow_calendar_last_month
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            calendar.lastMonth();
                        }

                    });

            //下月监听按钮
            RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                    .findViewById(R.id.popupwindow_calendar_next_month);
            popupwindow_calendar_next_month
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            calendar.nextMonth();
                        }
                    });

            //关闭窗口
            popupwindow_calendar_bt_enter
                    .setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            Message message = new Message();
                           if(id==bt.getId()){
                                  System.out.println("bt:");
                               if(date ==null){
                                   date_start = calendar.getThisday();

                               }else{
                                   date_start = transform2(date);
                               }

                            }
                            if(id == bt2.getId()){
                                  System.out.println("bt2:");
                                if(date ==null){
                                    date_end= calendar.getThisday();

                                }else{
                                    date_end = transform2(date);
                                }

                                  message.what = 3;
                                  handler.sendMessage(message);
                            }

                            dismiss();

                        }
                    });
        }
    }

    /**
     * 获取两个日期之间的日期
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期集合
     */
    public List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        result.add(start);
        result.add(end);
        return result;
    }


    public Date getBeforeMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);    //得到前一个月
        return calendar.getTime();

    }

    public Date getPresentMonth() {

        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();

    }

    public String getAlarmType(String type) {

        if (type.equals("显示全部")) {
            return null;
        } else if (type.equals("设备一般警告")) {
            return "1";
        } else if (type.equals("设备严重警告")) {
            return "2";
        } else {
            return "3";
        }
    }
//得到制水机以外的设备名
    public String getDeviceName(String name){
          if(!"".equals(name) && name!=null){
              switch (name){
                  case "1":
                       return "原水箱";
                  case "2":
                      return "纯水箱";
                  case "3":
                      return "酸水箱";
                  case "4":
                      return "碱水箱";
                  case "5":
                      return "电解剂箱";
                  case "6":
                      return "搅拌箱";
                  case "SYS":
                      return "系统设备";
              }


          }
        return null;
    }

//得到外围错误
    public String getAlarmCode(String alarm_code) {

        if (!"".equals(alarm_code) || alarm_code != null) {
               switch (alarm_code){
                   case "C0":
                       return "开始制水";
                   case "C1":
                       return "停止制水";
                   case "C2":
                       return "运行数据";
                   case "CE":
                       return "制水机组错误及报警";
                   case "CT":
                        return "水箱系统错误及报警";
                   case "C6":
                       return "系统启动";
               }
        }
        return null;

    }

 //得到制水机错误类型
    public String getZsjError(String error){
       String str = Integer.toHexString(Integer.parseInt(error));
       switch (str){
           case "ff":
               return "制水机离线";
           case "40":
                return "制水机过流保护";
           case "20":
                return "制水机高水压保护";
           case "10":
                return "制水机低水压保护";
           case "4":
                return  "制水机电解槽失效";
           default:
               return null;
        }
    }
    //得到水箱报警类型
    public String getSxAlarmType(String alarm){
        String str = Integer.toHexString(Integer.parseInt(alarm));
        switch (str){
            case "ff":
                return "液位计错误";
            case "0":
                 return "低水位报警";
            default:
                  return null;
        }
    }


    public Date transform(String s) {
        Date d = null;

        try {
             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
             d = simpleDateFormat.parse(s);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public Date transform2(String s) {
        Date d = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            d = simpleDateFormat.parse(s);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public String getBeforeForm(){
            Date d = getBeforeMonth();
            String str = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            str = simpleDateFormat.format(d);
            return str;



    }


    public static boolean sameDate(Date d1, Date d2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }




}


