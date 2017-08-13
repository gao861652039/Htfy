package com.example.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.view.activity.R;
import com.example.model.entity.UserInfo;

import java.util.List;

/**
 * Created by 高峰 on 2017/2/12.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<UserInfo> mUserInfo;
    private onRecyclerViewItemClickListener itemClickListener = null;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.userinfo, parent, false);
        view.setOnClickListener(new View.OnClickListener() {   //为每一个item绑定监听
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(v,(String)v.getTag());
            }
        });

       return new ViewHolder(view);

    }
    public UserAdapter(List<UserInfo> userInfoList) {

        mUserInfo = userInfoList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(String.valueOf(position));
        UserInfo userInfo = mUserInfo.get(position);
        holder.machnum.append(userInfo.getMachNum());
        holder.username.append(userInfo.getUsername());
        holder.useraddress.append(userInfo.getUseraddress());
        holder.location.append(userInfo.getLocation());
        holder.data_time.append(userInfo.getDate());

    }

    @Override
    public int getItemCount() {
        return mUserInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView machnum;
        TextView username;
        TextView useraddress;
        TextView location;
        TextView data_time;
        ImageView item_select;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            machnum = (TextView) itemView.findViewById(R.id.machineNumber);
            username = (TextView) itemView.findViewById(R.id.user_name);
            useraddress = (TextView) itemView.findViewById(R.id.user_address);
            location = (TextView) itemView.findViewById(R.id.location);
            data_time = (TextView) itemView.findViewById(R.id.data_time);
            item_select = (ImageView) itemView.findViewById(R.id.item_select);
            item_select.setVisibility(View.INVISIBLE);
        }


    }
    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }



    public  interface onRecyclerViewItemClickListener {

        void onItemClick(View v, String tag);
    }


}
