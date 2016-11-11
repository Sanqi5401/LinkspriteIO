package com.linksprite.io.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.linksprite.io.R;
import com.linksprite.io.database.Device;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.CreateDevRequest;
import com.linksprite.io.network.model.BaseDevRespone;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class CreateDeviceActivity extends SetupActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.edit_device_name)
    EditText editDeviceName;
    @InjectView(R.id.edit_group_name)
    EditText editGroupName;
    int type;
    @InjectView(R.id.device_type)
    TextView deviceType;

    private SweetAlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);
        ButterKnife.inject(this);
        titleString = getString(R.string.create_device_title);
        type = getIntent().getIntExtra("type", 0);
        if (type >= 0 && type < 6) {
            String[] types = getResources().getStringArray(R.array.device_type);
            deviceType.setText("" + types[type]);
        }
    }

    @OnClick({R.id.cancel_button, R.id.confirm_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                finish();
                break;
            case R.id.confirm_button:
                String name = editDeviceName.getText().toString();
                String group = editGroupName.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(group)) {
                    showErrMessage(getString(R.string.err_null_name_or_group_name));
                    return;
                }
                showProgressDialog();
                ApiManager.getCreateDevService().createrDev(account.getJwt(), new CreateDevRequest(name, group, "0" + type))
                        .filter(new Func1<BaseDevRespone, Boolean>() {
                            @Override
                            public Boolean call(BaseDevRespone baseDevRespone) {
                                String error = baseDevRespone.getError();
                                boolean is = (error != null && !TextUtils.isEmpty(error));
                                if (is) {
                                    showErrMessage(baseDevRespone.getError());
                                }
                                return !is;
                            }
                        })
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseDevRespone>() {
                            @Override
                            public void onCompleted() {
                                if (loading != null)
                                    loading.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showErrMessage(e.getMessage());
                            }

                            @Override
                            public void onNext(BaseDevRespone baseDevRespone) {
                                Log.e("TAG", baseDevRespone.toString());
                                ContentValues values = new ContentValues();
                                values.put(Device.APIKEY, baseDevRespone.getApikey());
                                values.put(Device.DEVICEID, baseDevRespone.getDeviceid());
                                values.put(Device.GROUP, baseDevRespone.getGroup());
                                values.put(Device.NAME, baseDevRespone.getName());
                                values.put(Device.ONLINE, baseDevRespone.getType());
                                values.put(Device.TYPE, baseDevRespone.getType());
                                new Device(CreateDeviceActivity.this, values);

                                Intent intent = new Intent(CreateDeviceActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                nextActivity(intent);
                            }
                        });

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.device_type)
    public void onClick() {
    }

    public void showProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (loading == null) {
                        loading = new SweetAlertDialog(CreateDeviceActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    } else {
                        loading.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    }
                    loading.setTitleText("Loading...")
                            .show();
                }
            }
        });

    }
}
