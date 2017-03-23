package com.linksprite.io.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linksprite.io.R;
import com.linksprite.io.database.Account;

/**
 * Created by Administrator on 2016/11/8.
 *
 */
public class BaseDeviceSettingActivity extends AppCompatActivity {
    protected Toolbar mscToolbar;
    protected Account account;
    protected String titleString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mscToolbar = (Toolbar) findViewById(R.id.msc_toolbar);
        if(mscToolbar != null){
            setSupportActionBar(mscToolbar);
            getSupportActionBar().setTitle(titleString);
            mscToolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_material);
            mscToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
