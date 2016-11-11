package com.linksprite.io.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/29.
 */
public class LogUtils {

    private final static boolean DEBUG = true;


    public static void v(String tag,String msg){
        if(DEBUG){
            Log.v(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(DEBUG){
            Log.i(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if(DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(DEBUG){
            Log.e(tag,msg);
        }
    }

}
