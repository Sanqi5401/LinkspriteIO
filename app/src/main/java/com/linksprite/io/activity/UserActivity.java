package com.linksprite.io.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.linksprite.io.R;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.BaseRespone;
import com.linksprite.io.network.model.ResetPasswordRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserActivity extends BaseActivity {

    @InjectView(R.id.oldPassword)
    EditText oldPassword;
    @InjectView(R.id.newPassword)
    EditText newPassword;
    @InjectView(R.id.confirmPassword)
    EditText confirmPassword;


    private SweetAlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.inject(this);
        titleString = getString(R.string.users_title);

    }


    @OnClick(R.id.update_password_button)
    public void onClick() {
        String oldpassword = oldPassword.getText().toString();
        final String newpassword = newPassword.getText().toString();
        String confirmpassowrd = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(oldpassword) || TextUtils.isEmpty(newpassword) || TextUtils.isEmpty(confirmpassowrd)
                || oldpassword.equals(newpassword) || !newpassword.equals(confirmpassowrd)
                || !account.getUserPassword().equals(oldpassword)) {
            showErrDialog(getString(R.string.err_password_fail));
            return;
        }
        showDialog();
        ApiManager.getPasswordService().ResetPassword(account.getJwt(),new ResetPasswordRequest(oldpassword, newpassword, confirmpassowrd))
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseRespone>() {
                    @Override
                    public void onCompleted() {
                        account.setUserPassword(newpassword);
                        showSuccessDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.dismiss();
                        showErrDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseRespone baseRespone) {
                        if (!TextUtils.isEmpty(baseRespone.getError())) {
                            loading.dismiss();
                            showErrDialog(baseRespone.getError());
                        }
                    }
                });
    }

    public void showDialog() {
        if (!isDestroyed())
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (loading == null) {
                        loading = new SweetAlertDialog(UserActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        loading.setTitleText("Loading...");
                    }
                    loading.show();
                }
            });
    }

    public void showSuccessDialog() {
        if (!isDestroyed()) {
            oldPassword.setText("");
            newPassword.setText("");
            confirmPassword.setText("");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (loading == null) {
                        loading = new SweetAlertDialog(UserActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        loading.setTitleText("Success");
                    } else {
                        loading.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        loading.setTitleText("Success");
                    }
                    loading.show();
                }
            });
        }
    }

}
