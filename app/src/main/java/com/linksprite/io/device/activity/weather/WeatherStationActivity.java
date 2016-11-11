package com.linksprite.io.device.activity.weather;

import android.os.Bundle;

import com.linksprite.io.R;
import com.linksprite.io.activity.SetupActivity;
import com.linksprite.io.device.view.TemperatureView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WeatherStationActivity extends SetupActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_station);
        ButterKnife.inject(this);

    }
}
