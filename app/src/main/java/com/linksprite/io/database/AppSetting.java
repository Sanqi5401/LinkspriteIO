/*
 * ************************************************************************
 *  *
 *  * MySnapCam CONFIDENTIAL
 *  * FILE: AppSettings.java
 *  *
 *  *  [2009] - [2015] MySnapCam, LLC
 *  *  All Rights Reserved.
 *
 * NOTICE:
 *  * All information contained herein is, and remains the property of MySnapCam LLC.
 *  * The intellectual and technical concepts contained herein are proprietary to MySnapCam
 *  * and may be covered by U.S. and Foreign Patents,patents in process, and are protected by
 *  * trade secret or copyright law.
 *  * Dissemination of this information or reproduction of this material
 *  * is strictly forbidden unless prior written permission is obtained
 *  * MySnapCam, LLC.
 *  *
 *  *
 *  * Written: Chad Sarles
 *  * Updated: 8/26/2015
 *
 */

package com.linksprite.io.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.linksprite.io.Globals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class AppSetting {

    private SQLiteDatabase database;
    private MySnapDbOpenHelper dbHelper;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final int TYPE_ALL = 0X06;
    public static final int TYPE_POWER_SWITCH = 0X01;
    public static final int TYPE_SAMPLE_LIGHT = 0X02;
    public static final int TYPE_WEATHER_STATION = 0X03;
    public static final int TYPE_GPS_TRACKER = 0X04;
    public static final int TYPE_LED_BAR = 0X05;
    public static final int TYPE_COSTOM_DEVICE_TYPE = 0X00;

    public static final String AUTOLOGINREMOTE = "autoLoginRemote";
    public static final String TYPE = "showType";


    private Context context;
    private Boolean autoLoginRemote;
    private Integer type;


    public static void loadInstance(Context context) {
        Log.i("AppSettings", "Loading app settings instance");
        Globals._appSettings = new AppSetting(context);
    }

    public AppSetting(Context context) {
        this.context = context.getApplicationContext();

        dbHelper = new MySnapDbOpenHelper(this.context);
        open();
        Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_APP_SETTINGS, null, null, null, null, null, null);
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            List<String> columnNamesList = Arrays.asList(columnNames);
            setAutoLoginRemote(cursor.getInt(columnNamesList.indexOf(AUTOLOGINREMOTE)) == 1, false);
            setType(cursor.getInt(columnNamesList.indexOf(TYPE)), false);
        }
        cursor.close();
        close();
    }

    public AppSetting(Context context, ContentValues values) {
        dbHelper = new MySnapDbOpenHelper(context.getApplicationContext());
        open();

        if (values != null) {
            database.execSQL("DELETE FROM " + MySnapDbOpenHelper.TABLE_APP_SETTINGS);
            long result = database.insertOrThrow(MySnapDbOpenHelper.TABLE_APP_SETTINGS, null, values);
            //Log.i("INSERTING", Long.toString(result));
        }

        Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_APP_SETTINGS, null, null, null, null, null, null);
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            List<String> columnNamesList = Arrays.asList(columnNames);
            setAutoLoginRemote(cursor.getInt(columnNamesList.indexOf(AUTOLOGINREMOTE)) == 1, false);
            setType(cursor.getInt(columnNamesList.indexOf(TYPE)),false);
        }
        cursor.close();
        close();
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Boolean getAutoLoginRemote() {
        return autoLoginRemote;
    }

    public void setAutoLoginRemote(Boolean autoLoginRemote) {
        setAutoLoginRemote(autoLoginRemote, true);
    }

    public void setAutoLoginRemote(Boolean autoLoginRemote, Boolean updateDB) {
        this.autoLoginRemote = autoLoginRemote;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(AppSetting.AUTOLOGINREMOTE, autoLoginRemote);
            open();
            database.update(MySnapDbOpenHelper.TABLE_APP_SETTINGS, values, null, null);
            close();
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        setType(type, true);
    }

    public void setType(Integer type, Boolean updateDB) {
        this.type = type;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(AppSetting.TYPE, type);
            open();
            database.update(MySnapDbOpenHelper.TABLE_APP_SETTINGS, values, null, null);
            close();
        }
    }

    public void dump(Boolean toFile) {
        Log.i("AppSettings", "Dumping app settings table");
        open();
        Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_APP_SETTINGS, null, null, null, null, null, null);

        String output = "{";
        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                output += cursor.getColumnName(i) + ":";
                if (!cursor.isNull(i)) {

                    if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                        output += cursor.getString(i);
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                        output += cursor.getInt(i);
                    }

                    output += ",";
                }
            }
            output += "}";
        }
        cursor.close();
        close();

        if (!toFile) {
            Log.i("AppSettings DB Dump", output);
        } else {
            String storageState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(storageState)) {
                try {
                    String root = Environment.getExternalStorageDirectory().toString();
                    String fileName = "DB_Application_" + System.currentTimeMillis() + ".txt";
                    File path = new File(root + "/mscsecure");
                    if (!path.exists()) path.mkdirs();

                    File file = new File(path, fileName);
                    try {
                        FileWriter writer = new FileWriter(file);
                        writer.write(output + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}