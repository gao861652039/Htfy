package com.example.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.model.entity.DeviceInfo;
import com.example.model.inter.LoginModel;
import com.example.utils.StringUtils;
import com.example.view.activity.R;

import java.util.ArrayList;
import java.util.List;

import expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import expandablerecyclerview.bean.RecyclerViewData;


/**
 * Created by 高峰 on 2017/8/15.
 */

public class MyAdapter extends BaseRecyclerViewAdapter<DeviceInfo,DeviceInfo,DeviceViewHolder> {
    private Context ctx;
    private List<RecyclerViewData> datas;
    private LayoutInflater mInflater;
    public MyAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.datas = datas;
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.deviceinfo,parent,false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.deviceinfo_item,parent,false);
    }

    @Override
    public DeviceViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new DeviceViewHolder(ctx,view,viewType);
    }

    @Override
    public void onBindGroupHolder(DeviceViewHolder holder, int groupPos, int position, DeviceInfo groupData) {

        SpannableStringBuilder builder = new SpannableStringBuilder(groupData.getOtherInfo());
        String str = groupData.getOtherInfo().toString();
        ArrayList<Integer> bsw = new ArrayList<>();
        ArrayList<Integer>  jjw = new ArrayList<>();
        if(groupData.getFlag()==false){
            StringUtils.getIndexOf(str.toCharArray(),"补水位".toCharArray(),0,bsw);
            StringUtils.getIndexOf(str.toCharArray(),"警戒位".toCharArray(),0,jjw);
        }
        if(bsw.size()!=0) {
            for (int i = 0; i < bsw.size(); i++) {
                builder.setSpan(new ForegroundColorSpan(Color.RED), bsw.get(i), bsw.get(i) + 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        if(jjw.size()!=0) {
            for (int i = 0; i < jjw.size(); i++) {
                builder.setSpan(new ForegroundColorSpan(Color.RED), jjw.get(i), jjw.get(i) + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (groupData.getFlag() == true){
            holder.other_info.setTextColor(Color.RED);
        }else{
            holder.other_info.setTextColor(Color.BLACK);
        }
        holder.time.setText(groupData.getTime());
        holder.alarm_info.setText(groupData.getAlarminfo());
        holder.other_info.setText(builder);


//
//                holder.time.setText(groupData.getTime());
//                holder.alarm_info.setText(groupData.getAlarminfo());
//                holder.other_info.setText(groupData.getOtherInfo());
    }

    @Override
    public void onBindChildpHolder(DeviceViewHolder holder, int groupPos, int childPos, int position, DeviceInfo childData) {

        holder.other_info_item.setTextColor(Color.BLACK);
        holder.time_item.setText(childData.getTime());
        holder.alarm_info_item.setText(childData.getAlarminfo());
        holder.other_info_item.setText(childData.getOtherInfo());
    }
}
