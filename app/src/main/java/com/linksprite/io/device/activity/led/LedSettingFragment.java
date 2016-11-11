package com.linksprite.io.device.activity.led;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.linksprite.io.R;
import com.linksprite.io.network.model.LedUpdateRequest;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/11/11.
 */
public class LedSettingFragment extends DialogFragment {

    //count
    EditText edtCount;

    //time
    LinearLayout linerTime;
    DiscreteSeekBar timeSeekbar;
    TextView breathingTime;
    //num
    LinearLayout linerNum;
    EditText edtNumberStart;
    EditText edtNumberEnd;

    //color
    Spinner ledColorSelect;
    LinearLayout linerColor;

    CardView btn_confirm;
    CardView btn_cancel;

    private int type;

    private LedSettingCallBack callBack;
    private String[] colors = new String[]{"Red", "Blue", "Green", "white"};

    public LedSettingFragment(int type, LedSettingCallBack callBack) {
        this.type = type;
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_led_setting, container, false);
        initView(view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, colors);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ledColorSelect.setAdapter(adapter);

        timeSeekbar.setMin(50);
        timeSeekbar.setMax(5000);

        timeSeekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                breathingTime.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        switch (type) {
            case LedActivity.MODE_WATER:
                linerNum.setVisibility(View.GONE);
                linerColor.setVisibility(View.GONE);
                linerTime.setVisibility(View.VISIBLE);
                if (LedActivity.led == null || TextUtils.isEmpty(LedActivity.led.getTime())) {
                    timeSeekbar.setProgress(50);
                } else {
                    timeSeekbar.setProgress(Integer.parseInt(LedActivity.led.getTime()));
                }
                break;
            case LedActivity.MODE_BLINK:
                linerNum.setVisibility(View.GONE);
                linerColor.setVisibility(View.GONE);
                linerTime.setVisibility(View.VISIBLE);

                if (LedActivity.led == null || TextUtils.isEmpty(LedActivity.led.getTime())) {
                    timeSeekbar.setProgress(50);
                } else {
                    timeSeekbar.setProgress(Integer.parseInt(LedActivity.led.getTime()));
                }
                break;
            case LedActivity.MODE_CUSTOM:
                linerNum.setVisibility(View.VISIBLE);
                linerColor.setVisibility(View.VISIBLE);
                linerTime.setVisibility(View.GONE);
                if (LedActivity.led != null) {
                    if (TextUtils.isEmpty(LedActivity.led.getColor())) {
                        ledColorSelect.setSelection(0, true);
                    } else {
                        ledColorSelect.setSelection(Integer.parseInt(LedActivity.led.getColor()), true);
                    }
                    if (TextUtils.isEmpty(LedActivity.led.getTime())) {
                        edtNumberStart.setText("01");
                        edtNumberEnd.setText("10");
                    } else {
                        edtNumberStart.setText(LedActivity.led.getNum().substring(0, 2));
                        edtNumberEnd.setText(LedActivity.led.getNum().substring(2, 4));
                    }
                } else {
                    edtNumberStart.setText("01");
                    edtNumberEnd.setText("10");
                    ledColorSelect.setSelection(0, true);
                }
                break;
            case LedActivity.MODE_BREATHING:
                linerNum.setVisibility(View.GONE);
                linerColor.setVisibility(View.GONE);
                linerTime.setVisibility(View.GONE);
                break;
        }

        if (LedActivity.led == null || TextUtils.isEmpty(LedActivity.led.getCount())) {
            edtCount.setText("30");
        } else {
            edtCount.setText(LedActivity.led.getCount());
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                LedActivity.led.setMode("" + type);
                String count = edtCount.getText().toString();
                if (!TextUtils.isEmpty(count)) {
                    LedActivity.led.setCount(count);
                } else {
                    callBack.onFail("Count is null");
                    return;
                }
                if (linerTime.getVisibility() == View.VISIBLE) {
                    int time = timeSeekbar.getProgress();
                    LedActivity.led.setTime("" + time);
                }

                if (linerColor.getVisibility() == View.VISIBLE) {
                    String color = "" + ledColorSelect.getSelectedItemId();
                    LedActivity.led.setColor(color);
                }
                if (linerNum.getVisibility() == View.VISIBLE) {
                    String start = edtNumberStart.getText().toString();
                    String end = edtNumberEnd.getText().toString();
                    if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
                        callBack.onFail("Start or end number is null");
                        return;
                    } else if (start.length() > 2 || end.length() > 2) {
                        callBack.onFail("Start or end number is illegal");
                        return;
                    } else {
                        LedActivity.led.setNum(parseNum(start) + parseNum(end));
                    }
                }
                LedActivity.led.setCount(edtCount.getText().toString());
                callBack.onLedSetting();
            }
        });
        return view;
    }

    public String parseNum(String num) {
        if (num.length() == 0)
            return "00";
        else if (num.length() == 1) {
            return "0" + num;
        }
        return num;
    }


    public void initView(View view) {
        timeSeekbar = (DiscreteSeekBar) view.findViewById(R.id.time_seekbar);
        edtCount = (EditText) view.findViewById(R.id.edt_count);
        linerTime = (LinearLayout) view.findViewById(R.id.liner_time);
        edtNumberStart = (EditText) view.findViewById(R.id.edt_number_start);
        linerNum = (LinearLayout) view.findViewById(R.id.liner_num);
        edtNumberEnd = (EditText) view.findViewById(R.id.edt_number_end);
        ledColorSelect = (Spinner) view.findViewById(R.id.led_color_select);
        linerColor = (LinearLayout) view.findViewById(R.id.liner_color);
        breathingTime = (TextView) view.findViewById(R.id.breathing_time);
        btn_confirm = (CardView) view.findViewById(R.id.confirm_button);
        btn_cancel = (CardView) view.findViewById(R.id.cancel_button);
    }


    public interface LedSettingCallBack {
        public void onLedSetting();

        public void onFail(String err);
    }

}
