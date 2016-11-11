package com.linksprite.io.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/8/1.
 */
public class NetworkUtils {

    public static void refreshNetworkMode(Context context){
        if(!checkNet(context)){
            EventBus.getDefault().post(false);
        }else
            EventBus.getDefault().post(true);

    }

    public static boolean checkNet(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
