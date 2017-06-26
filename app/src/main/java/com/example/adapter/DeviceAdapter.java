package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activity.R;
import com.example.entity.DeviceInfo;


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

              holder.time.setText(deviceInfo.getTime());
              holder.alarm_info.setText(deviceInfo.getAlarminfo());
              holder.other_info.setText(deviceInfo.getOtherInfo());

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
