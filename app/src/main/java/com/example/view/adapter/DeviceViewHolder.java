package com.example.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.view.activity.R;

import expandablerecyclerview.holder.BaseViewHolder;


/**
 * Created by 高峰 on 2017/8/15.
 */

public class DeviceViewHolder extends BaseViewHolder {
    public CardView card_parent;
    public TextView time;
    public TextView alarm_info;
    public TextView other_info;
    public CardView card_item;
    public TextView time_item;
    public TextView alarm_info_item;
    public TextView other_info_item;



    public DeviceViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
        card_parent = (CardView) itemView.findViewById(R.id.card_parent);
        time = (TextView) itemView.findViewById(R.id.time);
        alarm_info = (TextView) itemView.findViewById(R.id.alarm_info);
        other_info = (TextView) itemView.findViewById(R.id.other_info);
        card_item = (CardView) itemView.findViewById(R.id.card_item);
        time_item = (TextView) itemView.findViewById(R.id.time_item);
        alarm_info_item = (TextView) itemView.findViewById(R.id.alarm_info_item);
        other_info_item = (TextView) itemView.findViewById(R.id.other_info_item);
    }

    @Override
    public int getChildViewResId() {
        return R.id.card_item;
    }

    @Override
    public int getGroupViewResId() {
        return R.id.card_parent;
    }
}
