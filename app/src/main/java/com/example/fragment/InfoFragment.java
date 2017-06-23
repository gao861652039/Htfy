package com.example.fragment;


import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.activity.MainActivity;
import com.example.activity.R;
import com.example.tab.BottomTabBar;
import com.mylhyl.superdialog.SuperDialog;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;


/**
 * Created by gaofeng on 2017/2/11.
 */
public class InfoFragment extends Fragment {

     private Button button;
     private TextView textView;
     private BottomTabBar tb;

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
        checkNull();
        button = (Button) getActivity().findViewById(R.id.selectType);
        textView = (TextView) getActivity().findViewById(R.id.currentMsg);
        tb = (BottomTabBar) getActivity().findViewById(R.id.tb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaglog();
            }
        });


    }

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
                        tb.switchContent(MainActivity.userFragment);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().remove(MainActivity.infoFragment).commit();
                        tb.switchContent(MainActivity.userFragment);

                    }
                });
        mMaterialDialog.show();


    }
    public void checkNull(){
        try {
            ArrayList<String> test = getArguments().getStringArrayList("ph");
        }catch (NullPointerException e){
            showDialog2();
        }

    }




    //得到ph
    public float[] getPh(){

            ArrayList<String> ph = getArguments().getStringArrayList("ph");
            float[] p = new float[ph.size()];
            for (int i = 0; i < ph.size(); i++) {
                p[i] = (Float.parseFloat(ph.get(i))) / (float) 100;
            }
            return p;
    }
    //得到orp
    public float[] getOrp(){
        ArrayList<String> orp = getArguments().getStringArrayList("orp");
        float[] o = new float[orp.size()];
        for(int i=0;i<orp.size();i++){
            o[i] = Integer.parseInt(orp.get(i));
        }
        return o;
    }

    //得到yxl
    public float[] getYxl(){
        ArrayList<String> yxl = getArguments().getStringArrayList("yxl");
        float[] y =  new float[yxl.size()];
        for(int i=0;i<yxl.size();i++){
            y[i] = (Float.parseFloat(yxl.get(i)))/(float)10;
        }
        return y;
    }

    //得到current
    public float[] getCurrent(){
        ArrayList<String> current = getArguments().getStringArrayList("current");
        float[] c =  new float[current.size()];
        for(int i=0;i<current.size();i++){
            c[i] = (Float.parseFloat(current.get(i)))/(float)10;
        }
        return c;
    }



    public void showDiaglog(){
        final List<String> list = new ArrayList<>();
        list.add("PH");
        list.add("ORP");
        list.add("有效氯");
        list.add("电流");
        new SuperDialog.Builder(getActivity())
                //.setAlpha(0.5f)
                //.setGravity(Gravity.CENTER)
                //.setTitle("上传头像", ColorRes.negativeButton)
                .setCanceledOnTouchOutside(false)
                .setItems(list, new SuperDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                         String str = list.get(position).toString();
                         Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();

                         SuitLines suitLines = (SuitLines) getActivity().findViewById(R.id.suitlines);
                         suitLines.setLineStyle(SuitLines.SOLID);
                         suitLines.setLineType(SuitLines.CURVE);
                         List<Unit> lines = new ArrayList<>();

                         if(str.equals("PH")){
                             textView.setText("当前类型:"+str);
                             float[] f = getPh();
                             for(int i=0;i<f.length;i++)
                                 lines.add(new Unit(f[i],i+""));
                          }
                        if(str.equals("ORP")){
                            textView.setText("当前类型:"+str+" "+"单位 mV");
                            float[] f = getOrp();
                            for(int i=0;i<f.length;i++)
                                lines.add(new Unit(f[i],i+""));
                        }
                        if(str.equals("有效氯")){
                            textView.setText("当前类型:"+str+" "+"范围40.0~99.9 单位 mg/L");
                            float[] f = getYxl();
                            for(int i=0;i<f.length;i++)
                                lines.add(new Unit(f[i],i+""));
                        }
                        if(str.equals("电流")){
                            textView.setText("当前类型:"+str+" "+"范围00.0~20.0 单位A");
                            float[] f = getCurrent();
                            for(int i=0;i<f.length;i++)
                                lines.add(new Unit(f[i],i+""));
                        }

                        suitLines.feedWithAnim(lines);
                    }
                })
                .setNegativeButton("取消", null)
                .build();
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}
