package com.linksprite.io.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import com.linksprite.io.BuildConfig;
import com.linksprite.io.Globals;
import com.linksprite.io.R;
import com.linksprite.io.database.Account;
import com.linksprite.io.database.AppSetting;
import com.linksprite.io.database.Device;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.DevListRespone;
import com.linksprite.io.network.model.LoginRequest;
import com.linksprite.io.network.model.LoginResponse;
import com.linksprite.io.service.NetworkStateService;
import com.linksprite.io.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.login_email_field)
    EditText loginEmailField;
    @InjectView(R.id.login_password_field)
    EditText loginPasswordField;
    @InjectView(R.id.remember_me_switch)
    SwitchCompat rememberMeSwitch;
    @InjectView(R.id.tv_version)
    TextView tvVersion;

    private Account account = null;
    private String email, password;
    private SweetAlertDialog loginDialog = null;

    private boolean isShow = false;
    private boolean isDestery = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        loginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loginDialog.setTitleText("Loading...");
        tvVersion.setText(getString(R.string.Version) + BuildConfig.VERSION_NAME);
        startService(new Intent(this, NetworkStateService.class));
    }


    @Subscribe
    public void onEvent(final Boolean isConnect) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isConnect && !isShow) {
                    isShow = true;
                    SweetAlertDialog alertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                    alertDialog.setTitleText(getString(R.string.network_connection_title));
                    alertDialog.setContentText(getString(R.string.network_connection_body));
                    alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (Globals._appSettings.getAutoLoginRemote() == null) {
            ContentValues values = new ContentValues();
            values.put(AppSetting.AUTOLOGINREMOTE, false);
            values.put(AppSetting.TYPE, AppSetting.TYPE_ALL);
            Globals._appSettings = new AppSetting(this, values);
        }


        rememberMeSwitch.setChecked(Globals._appSettings.getAutoLoginRemote());
        rememberMeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {
                Globals._appSettings.setAutoLoginRemote(isOn);
            }
        });
        account = new Account(this);

        if (account.getEmail() != null) {
            loginEmailField.setText(account.getEmail());
        }
        if (account.getUserPassword() != null)
            loginPasswordField.setText(account.getUserPassword());

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @OnClick({R.id.sign_in_button, R.id.create_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                showLoginDialog();
                login();
                break;
            case R.id.create_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }


    public void login() {
        email = loginEmailField.getText().toString();
        password = loginPasswordField.getText().toString();
        //if no account

        ApiManager.getLoginService().Login(new LoginRequest(email, password))
                .filter(new Func1<LoginResponse, Boolean>() {
                    @Override
                    public Boolean call(LoginResponse loginResponse) {
                        String error = loginResponse.getError();
                        Log.e("Tag", "" + loginResponse.getUser().getEmail() + "  " + error);
                        boolean is = (error != null && !TextUtils.isEmpty(error));
                        if (is) {
                            showError(loginResponse.getError());
                        }
                        return !is;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                        addDevice();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("tag", e.getMessage());
                        showError(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Log.e("tag", " login " + loginResponse.toString());
                        addUser(loginResponse);
                    }
                });

    }

    public void addUser(LoginResponse loginResponse) {

        ContentValues values = new ContentValues();
        values.put(Account._ID, loginResponse.getUser().get_id());
        values.put(Account.APIKEY, loginResponse.getUser().getApikey());
        values.put(Account.JWT, "Bearer " + loginResponse.getJwt());
        values.put(Account.CREATEAT, loginResponse.getUser().getCreatedAt());
        values.put(Account.EMAIL, loginResponse.getUser().getEmail());
        values.put(Account.IAT, loginResponse.getUser().getIat());
        if (Globals._appSettings.getAutoLoginRemote()) {
            values.put(Account.USERPASSWORD, password);
        }
        account = new Account(LoginActivity.this, values);


    }

    public void addDevice() {
        ApiManager.getDevListService().getDevList(account.getJwt())
                .flatMap(new Func1<List<DevListRespone>, Observable<DevListRespone>>() {
                    @Override
                    public Observable<DevListRespone> call(List<DevListRespone> devListRespones) {
                        for (Device device : Globals._deviceList) {
                            device.removeDevice();
                        }
                        return Observable.from(devListRespones);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DevListRespone>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "onCompleted");
                        nextActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage());
                    }

                    @Override
                    public void onNext(DevListRespone devListRespone) {
                        ContentValues values = new ContentValues();
                        values.put(Device.NAME, devListRespone.getName());
                        values.put(Device.APIKEY, devListRespone.getApikey());
                        values.put(Device.DEVICEID, devListRespone.getDeviceid());
                        values.put(Device.TYPE, Integer.parseInt(devListRespone.getType()));
                        values.put(Device.ONLINE, devListRespone.isOnline());
                        values.put(Device.GROUP, devListRespone.getGroup());
                        new Device(LoginActivity.this, values);
                        Log.e("Tag", "add a device :" + devListRespone.getDeviceid());
                    }
                });
    }

    public void showError(final String err) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loginDialog != null) {
                    loginDialog.dismiss();
                }
                SweetAlertDialog alertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Error");
                alertDialog.setContentText(err);
                if (!isDestery)
                    alertDialog.show();
            }
        });
    }

    public void showLoginDialog() {
        if (loginDialog == null) {
            loginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            loginDialog.setTitleText("Loading...");
        }

        loginDialog.show();
    }

    public void nextActivity() {
        loginDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestery = true;
        EventBus.getDefault().unregister(this);
    }
}
