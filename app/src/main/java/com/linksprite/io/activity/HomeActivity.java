package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linksprite.io.Globals;
import com.linksprite.io.R;
import com.linksprite.io.adapter.GroupSort;
import com.linksprite.io.adapter.HomeAdapter;
import com.linksprite.io.database.AppSetting;
import com.linksprite.io.database.Device;
import com.linksprite.io.device.activity.led.LedActivity;
import com.linksprite.io.device.activity.weather.WeatherStationActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements HomeAdapter.OnDeviceItemClickListener {

    private final static String TAG = HomeActivity.class.getSimpleName();
    @InjectView(R.id.noThingsMessage)
    TextView noThingsMessage;
    @InjectView(R.id.thingsGridContainer)
    RecyclerView thingsGridContainer;
    private int type;
    private HomeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        titleString = getString(R.string.home);
        type = Globals._appSettings.getType();
        mAdapter = new HomeAdapter(this, this);
        thingsGridContainer.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        thingsGridContainer.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable observable = Observable.create(new Observable.OnSubscribe<Map<String, List<Device>>>() {
            @Override
            public void call(Subscriber<? super Map<String, List<Device>>> subscriber) {
                List<Device> deivces = Globals._deviceList;
                Map<String, List<Device>> map = new HashMap<String, List<Device>>();
                for (Device deivce : deivces) {
                    List<Device> list = map.get(deivce.getGroup());
                    if (list == null) {
                        if (type == AppSetting.TYPE_ALL || type == deivce.getType()) {
                            list = new ArrayList<Device>();
                            list.add(deivce);
                            map.put(deivce.getGroup(), list);
                        }
                    } else {
                        boolean is = false;
                        for (Device mydevice : list) {
                            if (mydevice.getDeviceid().equals(deivce.getDeviceid())) {
                                is = true;
                            }
                        }
                        if (!is) {
                            if (type == AppSetting.TYPE_ALL || type == deivce.getType())
                                list.add(deivce);
                        }
                    }
                }
                Log.e(TAG, "start show view");
                subscriber.onNext(map);
                subscriber.onCompleted();
            }
        });

//        Subscriber<List<Device>> sub = new Subscriber<List<Device>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<Device> devices) {
//                LinearLayout group_item = (LinearLayout) getLayoutInflater().inflate(R.layout.home_group_container, thingsGridContainer, false);
//                TextView tv2 = (TextView) group_item.findViewById(R.id.group);
//                FlowLayout group = (FlowLayout) group_item.findViewById(R.id.devices);
//                tv2.setText(devices.get(0).getGroup());
//                boolean is = false;
//                for (int i = 0; i < devices.size(); i++) {
//                    Device device = devices.get(i);
//                    if (type == AppSetting.TYPE_ALL || type == device.getType()) {
//                        LinearLayout thing_item = (LinearLayout) getLayoutInflater().inflate
//                                (R.layout.home_thing_container, group, false);
//                        TextView tv = (TextView) thing_item.findViewById(R.id.thingLabel);
//                        ImageView iv = (ImageView) thing_item.findViewById(R.id.thingTypeIcon);
//                        tv.setText(device.getName());
//                        thing_item.setOnClickListener(deviceClickHandler);
//                        thing_item.setId(i);
//                        group.addView(thing_item);
//                        Log.e(device.getGroup(), "" + device.getDeviceid());
//                        is = true;
//                    }
//                }
//                if (is)
//                    thingsGridContainer.addView(group_item);
//
//            }
//        };

        Subscriber<Map<String, List<Device>>> sub = new Subscriber<Map<String, List<Device>>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                showErrDialog(e.getMessage());
            }

            @Override
            public void onNext(Map<String, List<Device>> stringListMap) {
                List<GroupSort> mlist = new ArrayList<>();
                for (String s : stringListMap.keySet()) {
                    mlist.add(new GroupSort(s, stringListMap.get(s).size()));
                }
                Collections.sort(mlist);
                mAdapter.setList(mlist);
                mAdapter.setMap(stringListMap);
                mAdapter.notifyDataSetChanged();
            }
        };
        observable.observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sub);
    }

    @Override
    public void onClick(View v, Device device) {
        Intent intent;
        switch (device.getType()) {
            case AppSetting.TYPE_LED_BAR:
                intent = new Intent(HomeActivity.this, LedActivity.class);
                break;
            case AppSetting.TYPE_COSTOM_DEVICE_TYPE:
                intent = new Intent(HomeActivity.this, CustomActivity.class);
                break;
            default:

                // TODO: Add other device setting
                Toast.makeText(HomeActivity.this,getString(R.string.home_default),Toast.LENGTH_SHORT).show();
                return;
        }
        intent.putExtra("deviceID", device.getDeviceid());
        nextActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(HomeActivity.this, SelectModeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
