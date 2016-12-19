package com.linksprite.io.device.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.linksprite.io.R;
import com.linksprite.io.activity.BaseActivity;
import com.linksprite.io.activity.CustomActivity;
import com.linksprite.io.device.mode.CustomResponse;
import com.linksprite.io.device.mode.FlowResponse;
import com.linksprite.io.device.mode.UpdateFlower;
import com.linksprite.io.fragment.BaseFragment;
import com.linksprite.io.network.ApiManager;
import com.linksprite.io.network.model.BaseDevRespone;
import com.linksprite.io.network.model.BaseUpdateRequest;
import com.linksprite.io.network.model.UpdateRespone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.os.Looper.loop;
import static com.linksprite.io.device.activity.led.LedActivity.led;
import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2016/12/19.
 */

public class CustomFlowerFragment extends BaseFragment {

    private final String TAG = CustomFlowerFragment.class.getSimpleName();
    private final static int STATUS_LOOP = 0X02;
    private final static int STATUS_UPDATE_ON = 0X03;
    private final static int STATUS_UPDATE_OFF = 0X04;

    private SweetAlertDialog dialog;
    private ToggleButton toggleButton;
    private TextView tvTem, tvHum, tvLight, tvSoil;
    private Handler handler;
    private int type;
    private LoopAsyncTask loopAsyncTask;
    private boolean isCompleted = true;


    public CustomFlowerFragment(String deviceID) {
        super(deviceID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        tvTem = (TextView) view.findViewById(R.id.tv_tem);
        tvHum = (TextView) view.findViewById(R.id.tv_hum);
        tvLight = (TextView) view.findViewById(R.id.tv_light);
        tvSoil = (TextView) view.findViewById(R.id.tv_soil);
        toggleButton = (ToggleButton) view.findViewById(R.id.tog_btn);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton.setClickable(false);
                Log.e(TAG, "onClick = " + toggleButton.isChecked());
                showDialog();
                if (toggleButton.isChecked()) {
                    type = STATUS_UPDATE_ON;
                } else {
                    type = STATUS_UPDATE_OFF;
                }
            }
        });
        type = STATUS_LOOP;
        handler = new Handler();
        loopAsyncTask = new LoopAsyncTask();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        showDialog();
        if (loopAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
            loopAsyncTask.execute();
    }

    @UiThread
    public void showUI(final FlowResponse flowResponse) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTem.setText(flowResponse.temperature + "℃");
                tvHum.setText(flowResponse.humidity + "%");
                tvLight.setText(flowResponse.light + "%");
                tvSoil.setText(flowResponse.Soil_Mois.equals("Dry") ? getString(R.string.dry) : getString(R.string.drippy));
                toggleButton.setChecked(flowResponse.water.equals("0") ? false : true);
            }
        });

    }

    public void getFlowerInfo() {
        isCompleted = false;
        ApiManager.getDevInfoService().getFlowerInfo(account.getJwt(), device.getDeviceid())
                .filter(new Func1<BaseDevRespone<FlowResponse>, Boolean>() {
                    @Override
                    public Boolean call(BaseDevRespone<FlowResponse> ledResponseBaseDevRespone) {
                        String error = ledResponseBaseDevRespone.getError();
                        boolean is = (error != null && !TextUtils.isEmpty(error));
                        if (is) {
                            showErrDialog(error);
                        }
                        return !is;
                    }
                }).map(new Func1<BaseDevRespone<FlowResponse>, FlowResponse>() {
            @Override
            public FlowResponse call(BaseDevRespone<FlowResponse> customResponseBaseDevRespone) {
                return customResponseBaseDevRespone.getParams();
            }
        })
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlowResponse>() {
                    @Override
                    public void onCompleted() {
                        isCompleted = true;
                        dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(FlowResponse customResponse) {
                        Log.i(TAG, customResponse.toString());
                        if (customResponse != null)
                            showUI(customResponse);
                    }
                });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateDevice(boolean is) {

        isCompleted = false;
        UpdateFlower flowResponse = new UpdateFlower();
        flowResponse.water = is ? "1" : "0";
        BaseUpdateRequest<UpdateFlower> baseUpdateRequest = new BaseUpdateRequest<>();
        baseUpdateRequest.setAction("update");
        baseUpdateRequest.setApikey(account.getApikey());
        baseUpdateRequest.setDeviceid(device.getDeviceid());
        baseUpdateRequest.setParams(flowResponse);
        ApiManager.getDevUpdateService().updateFlower(baseUpdateRequest)
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateRespone>() {
                    @Override
                    public void onCompleted() {
                        isCompleted = true;
                        type = STATUS_LOOP;
                        toggleButton.setClickable(true);
                        dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrDialog(e.getMessage());
                        toggleButton.setClickable(true);
                    }

                    @Override
                    public void onNext(UpdateRespone updateRespone) {
                        Log.e(TAG, updateRespone.toString());
                    }
                });
    }

    public class LoopAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (isCompleted) {
                    switch (type) {
                        case STATUS_LOOP:
                            getFlowerInfo();
                            break;
                        case STATUS_UPDATE_ON:
                            updateDevice(true);
                            break;
                        case STATUS_UPDATE_OFF:
                            updateDevice(false);
                            break;
                    }
                }
                if (isHidden()) {
                    return null;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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
