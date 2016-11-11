package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linksprite.io.R;
import com.linksprite.io.database.AppSetting;

import org.apmem.tools.layouts.FlowLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectModeActivity extends SetupActivity {

    @InjectView(R.id.thingsGridContainer)
    FlowLayout thingsGridContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        ButterKnife.inject(this);
        titleString = getString(R.string.select_mode_title);
        String[] types = getResources().getStringArray(R.array.device_type);
        for (int i = 0; i < types.length; i++) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.home_thing_container, thingsGridContainer, false);
            TextView tv = (TextView) view.findViewById(R.id.thingLabel);
            ImageView iv = (ImageView) view.findViewById(R.id.thingTypeIcon);
            switch (i) {
                case AppSetting.TYPE_COSTOM_DEVICE_TYPE:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.custom));
                    break;
                case AppSetting.TYPE_GPS_TRACKER:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.gps_tracker));
                    break;
                case AppSetting.TYPE_LED_BAR:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.led_bar));
                    break;
                case AppSetting.TYPE_POWER_SWITCH:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.powerswitch));
                    break;
                case AppSetting.TYPE_SAMPLE_LIGHT:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.light));
                    break;
                case AppSetting.TYPE_WEATHER_STATION:
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.weather));
                    break;
            }
            tv.setText(types[i]);
            view.setId(i);
            view.setOnClickListener(typeClick);
            thingsGridContainer.addView(view);
        }
    }


    private View.OnClickListener typeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SelectModeActivity.this,SelectScanActivity.class);
            intent.putExtra("type", v.getId());
            nextActivity(intent);
        }
    };
}
