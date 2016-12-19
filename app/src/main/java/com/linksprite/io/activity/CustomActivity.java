package com.linksprite.io.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.linksprite.io.R;
import com.linksprite.io.database.Device;
import com.linksprite.io.device.fragment.CustomFlowerFragment;
import com.linksprite.io.device.mode.CustomResponse;
import com.linksprite.io.device.mode.LEDResponse;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.BaseDevRespone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.http.PUT;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.linksprite.io.utils.Constant.TYPE_CUSTOM_FLOWER;

/**
 * Created by Administrator on 2016/12/19.
 */

public class CustomActivity extends BaseActivity {

    private SweetAlertDialog dialog;
    private Device device;
    private FragmentManager fragmentManager;
    private CustomFlowerFragment customFlowerFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        titleString = getString(R.string.custom_title);
        String deviceID = getIntent().getStringExtra("deviceID");
        fragmentManager = getSupportFragmentManager();
        device = new Device(this, deviceID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialog();
        ApiManager.getDevInfoService().getCustomInfo(account.getJwt(), device.getDeviceid())
                .filter(new Func1<BaseDevRespone<CustomResponse>, Boolean>() {
                    @Override
                    public Boolean call(BaseDevRespone<CustomResponse> ledResponseBaseDevRespone) {
                        String error = ledResponseBaseDevRespone.getError();
                        boolean is = (error != null && !TextUtils.isEmpty(error));
                        if (is) {
                            showErrDialog(error);
                        }
                        return !is;
                    }
                }).map(new Func1<BaseDevRespone<CustomResponse>, CustomResponse>() {
            @Override
            public CustomResponse call(BaseDevRespone<CustomResponse> customResponseBaseDevRespone) {
                return customResponseBaseDevRespone.getParams();
            }
        })
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CustomResponse>() {
                    @Override
                    public void onCompleted() {
                        dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        showErrDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(CustomResponse customResponse) {
                        if (customResponse.type != null) {
                            setTabSelection(Integer.parseInt(customResponse.type));
                        }
                    }
                });
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (customFlowerFragment != null) {
            transaction.hide(customFlowerFragment);
        }
    }

    private void setTabSelection(int type) {

        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (type) {
            case TYPE_CUSTOM_FLOWER:
                if (customFlowerFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上

                    customFlowerFragment = new CustomFlowerFragment(device.getDeviceid());
                    transaction.add(R.id.frameLayout, customFlowerFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(customFlowerFragment);
                }
                break;
        }
        transaction.commit();
    }


    public void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new SweetAlertDialog(CustomActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                } else {
                    dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                }
                dialog.setTitleText("Loading...");
                dialog.show();
            }
        });
    }

    public void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }
}
