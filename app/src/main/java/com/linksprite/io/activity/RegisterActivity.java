package com.linksprite.io.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.linksprite.io.R;
import com.linksprite.io.database.Account;
import com.linksprite.io.database.AppSetting;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.LoginRequest;
import com.linksprite.io.network.model.LoginResponse;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/5.
 */
public class RegisterActivity extends BaseActivity {


    @InjectView(R.id.confirmPassword)
    EditText confirmPassword;
    @InjectView(R.id.email)
    EditText email;
    @InjectView(R.id.password)
    EditText password;

    private String editPassword;
    private AppSetting appSetting = null;
    private Account account = null;

    private SweetAlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        titleString = getString(R.string.register);
        appSetting = new AppSetting(this);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Loading...");

    }


    public void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    dialog.setTitleText("Loading...");
                }
                dialog.show();
            }
        });
    }

    @OnClick({R.id.next_button, R.id.back_to_login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                showDialog();
                login();
                break;
            case R.id.back_to_login_button:
                finish();
                break;
        }
    }

    private void login() {
        String editEmail = email.getText().toString().trim();
        String editPassword = password.getText().toString().trim();
        String editConfirmPassword = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(editEmail) || TextUtils.isEmpty(editPassword) || TextUtils.isEmpty(editConfirmPassword)) {

            if (dialog != null)
                dialog.dismiss();
            showErrDialog(getString(R.string.err_null_email));
            return;
        }

        if (editPassword.equals(editConfirmPassword)) {
            startLogin(editEmail, editPassword);
        } else {
            if (dialog != null)
                dialog.dismiss();
            showErrDialog(getString(R.string.err_password_fail));
        }
    }

    public void startLogin(String email, String password) {
        ApiManager.getRegisterService().Register(new LoginRequest(email, password))
                .filter(new Func1<LoginResponse, Boolean>() {
                    @Override
                    public Boolean call(LoginResponse loginResponse) {
                        String error = loginResponse.getError();
                        Log.e("Tag", "" + loginResponse.getUser().getEmail() + "  " + error);
                        boolean is = (error != null && !TextUtils.isEmpty(error));
                        if (is) {
                            if (dialog != null)
                                dialog.dismiss();
                            showErrDialog(loginResponse.getError());
                        }
                        return !is;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog != null)
                            dialog.dismiss();
                        showErrDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        addUser(loginResponse);
                    }
                });
    }

    public void addUser(LoginResponse loginResponse) {
        if (appSetting.getAutoLoginRemote()) {
            ContentValues values = new ContentValues();
            values.put(Account._ID, loginResponse.getUser().get_id());
            values.put(Account.APIKEY, loginResponse.getUser().getApikey());
            values.put(Account.JWT, "Bearer " + loginResponse.getJwt());
            values.put(Account.CREATEAT, loginResponse.getUser().getCreatedAt());
            values.put(Account.EMAIL, loginResponse.getUser().getEmail());
            values.put(Account.IAT, loginResponse.getUser().getIat());
            values.put(Account.USERPASSWORD, editPassword);
            account = new Account(RegisterActivity.this, values);
        }
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        nextActivity(intent);

        finish();
    }

}
