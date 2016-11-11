package com.linksprite.io.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.linksprite.io.Globals;
import com.linksprite.io.R;
import com.linksprite.io.database.AppSetting;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingActivity extends BaseActivity implements TagFlowLayout.OnTagClickListener {


    @InjectView(R.id.type_layout)
    TagFlowLayout typeLayout;
    private TagAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        titleString = getString(R.string.global_settings_title);
        String[] types = getResources().getStringArray(R.array.type);
        mAdapter = new TagAdapter<String>(types) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(SettingActivity.this).inflate(R.layout.setting_type_container, typeLayout, false);
                textView.setText(s);
                return textView;
            }
        };
        typeLayout.setOnTagClickListener(this);
        typeLayout.setMaxSelectCount(1);
        typeLayout.setAdapter(mAdapter);

        int type = Globals._appSettings.getType();
        if (type != AppSetting.TYPE_ALL) {
            mAdapter.setSelectedList(type);
        } else {
            mAdapter.setSelectedList(6);
        }
//        for (int i = 0; i < types.length; i++) {
//            LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.setting_type_container, typeLayout, false);
//            TextView tv = (TextView) view.findViewById(R.id.tv_type);
//            CardView card = (CardView) view.findViewById(R.id.card_type);
//            tv.setText(types[i]);
//            card.setId(i);
//            card.setOnClickListener(cardSelected);
//            int type = Globals._appSettings.getType();
//            if (i != 6) {
//                if (type == i)
//                    card.requestFocus();
//            } else {
//                if (type == AppSetting.TYPE_ALL)
//                    card.requestFocus();
//            }
//            typeLayout.addView(view);
//        }

    }


    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        Log.e("Tag", "on tag click:" + position);
        if(position != 6){
            Globals._appSettings.setType(position,true);
        }else {
            Globals._appSettings.setType(AppSetting.TYPE_ALL,true);
        }
        return true;
    }

}
