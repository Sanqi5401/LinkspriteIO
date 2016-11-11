package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.linksprite.io.R;
import com.linksprite.io.database.Account;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/11/7.
 */
public class SetupActivity extends AppCompatActivity {

    protected Toolbar mscToolbar;
    protected Account account = null;
    protected String titleString;

    public void nextActivity(Intent intent) {
        startActivity(intent);
    }

    public void previousActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mscToolbar = (Toolbar) findViewById(R.id.msc_toolbar);
        if (mscToolbar != null) {
            setSupportActionBar(mscToolbar);
            getSupportActionBar().setTitle(titleString);
            mscToolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            mscToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    public void showErrMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SetupActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setContentText(message)
                            .setTitleText("Error")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    errDialogClick();
                                }
                            })
                            .show();
                }
            }
        });
    }

    public void errDialogClick() {
        finish();
    }
}
