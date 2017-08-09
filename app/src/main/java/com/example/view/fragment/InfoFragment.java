package com.example.view.fragment;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.db.chart.model.LineSet;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;
import com.example.model.entity.CharDataEntity;
import com.example.model.entity.ChartEntity;
import com.example.utils.exception.MyException;
import com.example.utils.tab.BottomTabBar;
import com.example.view.activity.MainActivity;
import com.example.view.activity.R;
import com.mylhyl.superdialog.SuperDialog;
import java.util.ArrayList;
import java.util.List;
import me.drakeet.materialdialog.MaterialDialog;



/**
 * Created by gaofeng on 2017/2/11.
 */
public class InfoFragment extends Fragment {

     private BottomTabBar tb;
     private Button button;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_info,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button = (Button) getActivity().findViewById(R.id.select_zsj);
        tb = (BottomTabBar) getActivity().findViewById(R.id.tb);
        final ArrayList<ChartEntity> list = getList();
        try {
        drawPh( new CharDataEntity(getPhData(list,"1"),getXzb(list,"1")));
        drawOrp(new CharDataEntity(getOrpData(list,"1"),getXzb(list,"1")));
        drawYxl(new CharDataEntity(getYxlData(list,"1"),getXzb(list,"1")));
        }catch (Exception e){

            return;

        }
        button.setText("当前编号:1");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] zsjNum = getZsjNum(list);
                List<String> zsj = new ArrayList<String>();
                ArrayList<CharDataEntity> ph = new ArrayList<>();
                ArrayList<CharDataEntity> orp = new ArrayList<>();
                ArrayList<CharDataEntity> yxl = new ArrayList<>();
                for(int i=0;i<zsjNum.length;i++) {
                    ph.add(new CharDataEntity(getPhData(list, zsjNum[i]), getXzb(list, zsjNum[i])));
                }
                for(int i=0;i<zsjNum.length;i++) {
                    orp.add(new CharDataEntity(getOrpData(list, zsjNum[i]), getXzb(list, zsjNum[i])));
                }
                for(int i=0;i<zsjNum.length;i++) {
                    yxl.add(new CharDataEntity(getYxlData(list, zsjNum[i]), getXzb(list, zsjNum[i])));
                }

                for(int i=0;i<zsjNum.length;i++){
                     zsj.add(zsjNum[i]);
                }

                showDialog(zsj,list);

            }
        });

    }
    public void drawPh(CharDataEntity dataEntity){
        LineChartView mChart1 = (LineChartView)getActivity().findViewById(R.id.linechart1);
        LineSet dataset = new LineSet(dataEntity.getXzb(), dataEntity.getValue());

        dataset.setColor(Color.RED)//设置直线颜色
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF58C674"))//设置 圈圈颜色
                .setDotsColor(Color.parseColor("#eef1f6"));
        dataset.setDotsRadius(0);
        mChart1.reset();
        mChart1.addData(dataset);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#308E9196"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        mChart1.setAxisBorderValues(0,10);
        mChart1.setBorderSpacing(1)
                .setAxisBorderValues(2, 3, (float)0.1);
        mChart1.show();
    }

    public void drawOrp(CharDataEntity dataEntity){
        LineChartView  mChart2 = (LineChartView)getActivity().findViewById(R.id.linechart2);
        LineSet dataset = new LineSet(dataEntity.getXzb(), dataEntity.getValue());
        dataset.setColor(Color.BLUE)//设置直线颜色
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF58C674"))//设置 圈圈颜色
                .setDotsColor(Color.parseColor("#eef1f6"));
        dataset.setDotsRadius(0);
        mChart2.reset();
        mChart2.addData(dataset);
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#308E9196"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        mChart2.setAxisBorderValues(0,10);
        mChart2.setBorderSpacing(1)
                .setAxisBorderValues(1100, 1180, 10);
        mChart2.show();
    }
    public void drawYxl(CharDataEntity dataEntity){
        LineChartView  mChart3 = (LineChartView)getActivity().findViewById(R.id.linechart3);
        LineSet dataset = new LineSet(dataEntity.getXzb(), dataEntity.getValue());
        dataset.setColor(Color.GREEN)//设置直线颜色
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF58C674"))//设置 圈圈颜色
                .setDotsColor(Color.parseColor("#eef1f6"));
        dataset.setDotsRadius(0);
        mChart3.reset();
        mChart3.addData(dataset);
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#308E9196"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));
        mChart3.setAxisBorderValues(0,10);
        mChart3.setBorderSpacing(1)
                .setAxisBorderValues(40, 100, 10);
        mChart3.show();
    }
   public void showDialog(final List<String> list,final  ArrayList<ChartEntity> chartEntities){


       new SuperDialog.Builder(getActivity())
               //.setAlpha(0.5f)
               //.setGravity(Gravity.CENTER)
               //.setTitle("上传头像", ColorRes.negativeButton)
               .setCanceledOnTouchOutside(false)
               .setItems(list, new SuperDialog.OnItemClickListener() {
                   @Override
                   public void onItemClick(int position) {
                         String x = list.get(position);
                         drawPh( new CharDataEntity(getPhData(chartEntities,x),getXzb(chartEntities,x)));
                         drawOrp(new CharDataEntity(getOrpData(chartEntities,x),getXzb(chartEntities,x)));
                         drawYxl(new CharDataEntity(getYxlData(chartEntities,x),getXzb(chartEntities,x)));
                         button.setText("当前编号:"+x);
                   }
               })
               .setNegativeButton("取消", null)
               .build();


   }




    //开头空数据监测
    public  void showDialog2(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("请先选择机房信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                        MainActivity.tb.select(0,MainActivity.bars);
                        tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                        MainActivity.tb.select(0,MainActivity.bars);
                        tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();


    }
    //开头空数据监测
    public  void showDialog3(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("警告")
                .setMessage("未检测到制水机信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                        MainActivity.tb.select(0,MainActivity.bars);
                        tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                        MainActivity.tb.select(0,MainActivity.bars);
                        tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();


    }
    //开头空数据监测
    public void checkNull(){
        try {
           ArrayList<ChartEntity> list = MainActivity.deviceFragment.getter();
            if(list.size()==0){
                throw new MyException("未检测到制水机信息");
            }
        }catch (NullPointerException e){
            showDialog2();
        } catch (MyException e) {
            showDialog3();
        }

    }

    //得到图表数据
    public ArrayList<ChartEntity> getList(){
        try {
            ArrayList<ChartEntity> list = MainActivity.deviceFragment.getter();
            if(list.size()==0){
                throw new MyException("未检测到制水机信息");
            }
            return list;
        }catch (NullPointerException e){
            showDialog2();
            return null;
        } catch (MyException e) {
            showDialog3();
            return null;
        }
    }

  //得到制水机编号以及数量
    public String[] getZsjNum(ArrayList<ChartEntity> list){
        ArrayList<String>  deviceId = new ArrayList<>();
        deviceId.add(list.get(0).getDeviceId());
        for(int i=1;i<list.size();i++){
                if(deviceId.contains(list.get(i).getDeviceId())){
                     continue;
                }else{
                    deviceId.add(list.get(i).getDeviceId());
                }
            }

        String[] sArr = new String[deviceId.size()];
        deviceId.toArray(sArr);

        return sArr;
    }

  //按制水机编号得到PH数据
    public float[]  getPhData(ArrayList<ChartEntity> list,String zsj){
              ArrayList<Float> ph = new ArrayList<>();

               for(int j=0;j<list.size();j++){
                   if(zsj.equals(list.get(j).getDeviceId())){
                          ph.add(list.get(j).getPh());
                   }
               }
               float[] a = new float[ph.size()];
               for(int i=0;i<a.length;i++){
                   a[i] = ph.get(i);
               }
              return  a;
    }
    //按制水机编号得到ORP数据
    public float[]  getOrpData(ArrayList<ChartEntity> list,String zsj){
        ArrayList<Float> orp = new ArrayList<>();

        for(int j=0;j<list.size();j++){
            if(zsj.equals(list.get(j).getDeviceId())){
                orp.add(list.get(j).getOrp());
            }
        }
        float[] a = new float[orp.size()];
        for(int i=0;i<a.length;i++){
            a[i] = orp.get(i);
        }
        return  a;
    }

    //按制水机编号得到yxl数据
    public float[]  getYxlData(ArrayList<ChartEntity> list,String zsj){
        ArrayList<Float> yxl = new ArrayList<>();

        for(int j=0;j<list.size();j++){
            if(zsj.equals(list.get(j).getDeviceId())){
               yxl.add(list.get(j).getYxl());
            }
        }
        float[] a = new float[yxl.size()];
        for(int i=0;i<a.length;i++){
            a[i] = yxl.get(i);
        }
        return  a;
    }
    //按制水机编号得到X轴坐标
    public String[]  getXzb(ArrayList<ChartEntity> list,String zsj){
        int count = 0;

        for(int j=0;j<list.size();j++){
            if(zsj.equals(list.get(j).getDeviceId())){
                 count++;
            }
        }
        String[] str = new String[count];
        for(int i=0;i<str.length;i++){
            str[i] = ""+i;
        }
        return  str;
    }


}
