package com.linksprite.io.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.linksprite.io.database.Account;
import com.linksprite.io.database.Device;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BaseFragment extends Fragment {

    protected Account account = null;
    protected Device device;
    protected String deviceID;

    private boolean isShow = false;
    private boolean isDestroy = false;

    public BaseFragment(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(getActivity());
        device = new Device(getActivity(),deviceID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void showErrDialog(final String err) {
        if(!isDetached())
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Error");
                    dialog.setContentText(err);
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
    }
}
