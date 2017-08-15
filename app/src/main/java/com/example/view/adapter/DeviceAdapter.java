package com.example.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.view.activity.R;
import com.example.model.entity.DeviceInfo;
import com.example.utils.StringUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
     private Context context;
     private List<DeviceInfo> mDeviceInfo;
    private onRecyclerViewItemClickListener itemClickListener = null;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.deviceinfo,parent,false);
        view.setOnClickListener(new View.OnClickListener() {   //为每一个item绑定监听
            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v,(String)v.getTag());
            }
        });
        return  new ViewHolder(view);
    }
    public DeviceAdapter(List<DeviceInfo> deviceInfoList){
           mDeviceInfo =deviceInfoList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            DeviceInfo deviceInfo = mDeviceInfo.get(position);
            SpannableStringBuilder builder = new SpannableStringBuilder(deviceInfo.getOtherInfo());
            String str = deviceInfo.getOtherInfo().toString();
            ArrayList<Integer>  bsw = new ArrayList<>();
            ArrayList<Integer>  jjw = new ArrayList<>();
            if(deviceInfo.getFlag()==false){
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
            if (deviceInfo.getFlag() == true){
                holder.other_info.setTextColor(Color.RED);
            }else{
                holder.other_info.setTextColor(Color.BLACK);
            }
            holder.time.setText(deviceInfo.getTime());
            holder.alarm_info.setText(deviceInfo.getAlarminfo());
            holder.other_info.setText(builder);
        }











    @Override
    public int getItemCount() {
        return mDeviceInfo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView time;
        TextView alarm_info;
        TextView other_info;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            time = (TextView) itemView.findViewById(R.id.time);
            alarm_info =(TextView) itemView.findViewById(R.id.alarm_info);
            other_info = (TextView) itemView.findViewById(R.id.other_info);
        }
    }

    public  interface onRecyclerViewItemClickListener {

        void onItemClick(View v, String tag);
    }
    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
