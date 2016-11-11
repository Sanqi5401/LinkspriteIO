package com.linksprite.io;

import android.app.Application;

/**
 * Created by Administrator on 2016/11/3.
 */
public class LinkspriteIOApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Globals.load(getApplicationContext());
    }
}
