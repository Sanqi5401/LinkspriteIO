package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linksprite.io.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectScanActivity extends SetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scan);
        ButterKnife.inject(this);
        titleString = getString(R.string.select_scan_title);
    }


    @OnClick({R.id.add_button, R.id.scan_button})
    public void onClick(View view) {
        Intent intent = SelectScanActivity.this.getIntent();
        ;
        switch (view.getId()) {
            case R.id.add_button:
                intent.setClass(SelectScanActivity.this, CreateDeviceActivity.class);
                break;
            case R.id.scan_button:
                intent.setClass(SelectScanActivity.this,CaptureActivity.class);
                break;
        }
        nextActivity(intent);
    }
}
