package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activity.R;
import com.example.tab.DeviceInfo;


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
              holder.device_name.setText("设备名:"+deviceInfo.getDevicename());
              holder.device_id.setText("设备ID:"+deviceInfo.getDeviceId());
              holder.time.setText("日期:"+deviceInfo.getTime());
              holder.alarm_info.setText("报警类型:"+deviceInfo.getAlarminfo());

    }

    @Override
    public int getItemCount() {
        return mDeviceInfo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView device_name;
        TextView device_id;
        TextView time;
        TextView alarm_info;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            device_id = (TextView) itemView.findViewById(R.id.device_id);
            time = (TextView) itemView.findViewById(R.id.time);
            alarm_info =(TextView) itemView.findViewById(R.id.alarm_info);
        }
    }

    public  interface onRecyclerViewItemClickListener {

        void onItemClick(View v, String tag);
    }
    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
