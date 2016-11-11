package com.linksprite.io.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linksprite.io.R;
import com.linksprite.io.database.AppSetting;
import com.linksprite.io.database.Device;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MasonryView> {

    private List<GroupSort> mlist = new ArrayList<>();
    private Map<String, List<Device>> map = new HashMap<>();
    private Context context;
    private OnDeviceItemClickListener onDeviceItemClickListener;

    public HomeAdapter(Context context, OnDeviceItemClickListener onDeviceItemClickListener) {
        this.onDeviceItemClickListener = onDeviceItemClickListener;
        this.context = context;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_group_container, parent, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        String mGroupName = mlist.get(position).getGroupName();
        List<Device> devices = map.get(mGroupName);
        holder.groupName.setText(mGroupName);
        holder.flowLayout.removeAllViews();
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            LinearLayout thing_item = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.home_thing_container, holder.flowLayout, false);
            TextView tv2 = (TextView) thing_item.findViewById(R.id.thingLabel);
            ImageView iv = (ImageView) thing_item.findViewById(R.id.thingTypeIcon);
            switch (device.getType()) {
                case AppSetting.TYPE_COSTOM_DEVICE_TYPE:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.custom));
                    break;
                case AppSetting.TYPE_GPS_TRACKER:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.gps_tracker));
                    break;
                case AppSetting.TYPE_LED_BAR:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.led_bar));
                    break;
                case AppSetting.TYPE_POWER_SWITCH:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.powerswitch));
                    break;
                case AppSetting.TYPE_SAMPLE_LIGHT:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.light));
                    break;
                case AppSetting.TYPE_WEATHER_STATION:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.weather));
                    break;
            }

            tv2.setText(device.getName());
            thing_item.setId(i);
            thing_item.setOnClickListener(new OnClickListener(device));
            holder.flowLayout.addView(thing_item);
        }
    }


    public void setList(List<GroupSort> mlist) {
        this.mlist = mlist;
    }

    public void setMap(Map<String, List<Device>> map) {
        this.map = map;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MasonryView extends RecyclerView.ViewHolder {

        TextView groupName;
        FlowLayout flowLayout;

        public MasonryView(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group);
            flowLayout = (FlowLayout) itemView.findViewById(R.id.devices);
        }

    }


    public class OnClickListener implements View.OnClickListener {
        private Device device;

        public OnClickListener(Device device) {
            this.device = device;
        }

        @Override
        public void onClick(View v) {
            onDeviceItemClickListener.onClick(v, device);
        }
    }

    public interface OnDeviceItemClickListener {
        public void onClick(View v, Device device);
    }
}
