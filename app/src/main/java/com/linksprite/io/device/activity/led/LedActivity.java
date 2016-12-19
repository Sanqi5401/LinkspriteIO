package com.linksprite.io.device.activity.led;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.linksprite.io.R;
import com.linksprite.io.activity.HomeActivity;
import com.linksprite.io.activity.SetupActivity;
import com.linksprite.io.database.Device;
import com.linksprite.io.device.fragment.LedSettingFragment;
import com.linksprite.io.device.mode.LEDResponse;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.BaseDevRespone;
import com.linksprite.io.network.model.BaseUpdateRequest;
import com.linksprite.io.network.model.UpdateRespone;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LedActivity extends SetupActivity {


    public final static int MODE_WATER = 0X01;
    public final static int MODE_BREATHING = 0X02;
    public final static int MODE_BLINK = 0X03;
    public final static int MODE_CUSTOM = 0X04;

    @InjectView(R.id.test_breathing)
    TextView testBreathing;
    @InjectView(R.id.test_blink)
    TextView testBlink;
    @InjectView(R.id.test_custom)
    TextView testCustom;
    private Device device;
    public static LEDResponse led;

    private int clo = 0;
    private static final int RED = 0xffFF8080;
    private static final int BLUE = 0xff8080FF;
    private static final int CYAN = 0xff80ffff;
    private static final int GREEN = 0xff80ff80;

    private SweetAlertDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        ButterKnife.inject(this);
        titleString = getString(R.string.led_title);
        String deviceID = getIntent().getStringExtra("deviceID");
        device = new Device(this, deviceID);
        spark();
        testCustom.setTextColor(getResources().getColor(R.color.color_middle));
        breathing();
        showLoadingDialog();
        init();
    }

    public void init() {
        ApiManager.getDevInfoService().getLedInfo(account.getJwt(), device.getDeviceid())
                .filter(new Func1<BaseDevRespone<LEDResponse>, Boolean>() {
                    @Override
                    public Boolean call(BaseDevRespone<LEDResponse> ledResponseBaseDevRespone) {
                        String error = ledResponseBaseDevRespone.getError();
                        boolean is = (error != null && !TextUtils.isEmpty(error));
                        if (is) {
                            showErrMessage(error);
                        }
                        return !is;
                    }
                }).map(new Func1<BaseDevRespone<LEDResponse>, LEDResponse>() {
            @Override
            public LEDResponse call(BaseDevRespone<LEDResponse> ledResponseBaseDevRespone) {
                return ledResponseBaseDevRespone.getParams();
            }
        })
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LEDResponse>() {
                    @Override
                    public void onCompleted() {
                        if (loading != null)
                            loading.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loading != null)
                            loading.dismiss();
                        showErrMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LEDResponse ledResponse) {
                        led = ledResponse;
                    }
                });
    }

    public void breathing() {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(testBreathing, "textColor", RED, BLUE);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }


    public void spark() {// 获取页面textview对象
        Timer timer = new Timer();
        TimerTask taskcc = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (clo == 0) {
                            clo = 1;
                            testBlink.setTextColor(Color.TRANSPARENT); // 透明
                        } else {
                            if (clo == 1) {
                                clo = 2;
                                testBlink.setTextColor(Color.YELLOW);
                            } else {
                                clo = 0;
                                testBlink.setTextColor(Color.GRAY);
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(taskcc, 1, 500);
        // 参数分别是delay（多长时间后执行），duration（执行间隔）
    }


    @OnClick({R.id.card_water, R.id.card_breathing, R.id.card_blink, R.id.card_custom})
    public void onClick(View view) {

        int type = MODE_WATER;
        switch (view.getId()) {
            case R.id.card_water:
                type = MODE_WATER;
                break;
            case R.id.card_breathing:
                type = MODE_BREATHING;
                break;
            case R.id.card_blink:
                type = MODE_BLINK;
                break;
            case R.id.card_custom:
                type = MODE_CUSTOM;
                break;
        }
        showSettingDialog(type);
    }

    public void showSettingDialog(int type) {
        if (led != null) {
            LedSettingFragment ledFragment = new LedSettingFragment(type, callBack);
            ledFragment.setCancelable(false);
            ledFragment.show(getFragmentManager(), "LedSetting");
        } else {
            led = new LEDResponse();
            showErrMessage("Not get led info");
        }
    }

    private LedSettingFragment.LedSettingCallBack callBack = new LedSettingFragment.LedSettingCallBack() {
        @Override
        public void onLedSetting() {
            showLoadingDialog();
            update();
        }

        @Override
        public void onFail(String err) {
            showErrMessage(err);
        }
    };

    public void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed()) {
                    if (loading != null) {
                        loading.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                    } else {
                        loading = new SweetAlertDialog(LedActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    }
                    loading.setTitleText("Loading...");
                    loading.show();
                }
            }
        });
    }


    public void update() {
        BaseUpdateRequest<LEDResponse> update = new BaseUpdateRequest<>();
        update.setAction("update");
        update.setApikey(account.getApikey());
        update.setDeviceid(device.getDeviceid());
        update.setParams(led);
        ApiManager.getDevUpdateService().updateLed(update)
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateRespone>() {
                    @Override
                    public void onCompleted() {
                        if (loading != null)
                            loading.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loading != null)
                            loading.dismiss();
                        showErrMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateRespone updateRespone) {
                        Log.e("Tag", "" + updateRespone.toString());
                    }
                });
    }


    public void errDialogClick() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteDevice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteDevice() {
        showLoadingDialog();
        ApiManager.getDevInfoService().deleteDevice(account.getJwt(), device.getDeviceid())
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseDevRespone>() {
                    @Override
                    public void onCompleted() {
                        if (loading != null)
                            loading.dismiss();
                        device.removeDevice();
                        Device.LoadDeviceList(LedActivity.this);
                        Intent intent = new Intent(LedActivity.this, HomeActivity.class);
                        nextActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loading != null)
                            loading.dismiss();
                        showErrMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseDevRespone baseDevRespone) {

                    }
                });
    }

}
