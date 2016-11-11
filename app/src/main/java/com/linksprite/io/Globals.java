package com.linksprite.io;

import android.content.Context;


import com.linksprite.io.database.AppSetting;
import com.linksprite.io.database.Device;

import java.util.List;

public class Globals {


    public static AppSetting _appSettings = null;
    public static List<Device> _deviceList = null;


    public static void load(Context context) {
        AppSetting.loadInstance(context);
        Device.LoadDeviceList(context);
    }

}

