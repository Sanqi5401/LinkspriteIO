package com.linksprite.io.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.Expose;
import com.linksprite.io.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Device {

    /**
     * _id : 56e26d35b37380cc19e2f6df
     * name : Light
     * type : 02
     * deviceid : 0200000002
     * apikey : f300225b-5e33-49a6-9ee2-cb44f59e4805
     * __v : 0
     * params : {"light":"off"}
     * online : true
     * createdAt : 2016-03-11T07:01:09.268Z
     * group : LinkNode
     */

    @Expose
    private SQLiteDatabase database;
    @Expose
    private MySnapDbOpenHelper dbHelper;
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DEVICEID = "deviceid";
    public static final String APIKEY = "apikey";
    public static final String ONLINE = "online";
    public static final String GROUP = "devicegroup";

    private int id;
    private String name;
    private Integer type;
    private String deviceid;
    private String apikey;
    private boolean online;
    private String group;

    private Context mContext;

    public Device() {

    }

    public Device(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        dbHelper = new MySnapDbOpenHelper(mContext);
        close();
    }

    public Device(Context mContext, String deviceid) {
        this.mContext = mContext.getApplicationContext();
        dbHelper = new MySnapDbOpenHelper(mContext);
        open();
        Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_DEVICE, null, DEVICEID + "=?", new String[]{deviceid}, null, null, null);
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            List<String> columnNamesList = Arrays.asList(columnNames);
            setId(cursor.getInt(columnNamesList.indexOf(ID)), false);
            setName(cursor.getString(columnNamesList.indexOf(NAME)), false);
            setType(cursor.getInt(columnNamesList.indexOf(TYPE)), false);
            setDeviceid(cursor.getString(columnNamesList.indexOf(DEVICEID)), false);
            setApikey(cursor.getString(columnNamesList.indexOf(APIKEY)), false);
            setOnline(cursor.getInt(columnNamesList.indexOf(ONLINE)) == 1, false);
            setGroup(cursor.getString(columnNamesList.indexOf(GROUP)), false);
        }
        cursor.close();
        close();
    }

    public Device(Context mContext, ContentValues values) {
        this.mContext = mContext.getApplicationContext();

        dbHelper = new MySnapDbOpenHelper(this.mContext);
        open();

        long result = -1;
        if (values != null) {
            result = database.insertOrThrow(MySnapDbOpenHelper.TABLE_DEVICE, null, values);
            //Log.i("INSERTING", Long.toString(result));
        }

        if (result != -1) {
            Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_DEVICE, null, ID + "=?", new String[]{Long.toString(result)}, null, null, null);
            if (cursor.getCount() != 0 && cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                List<String> columnNamesList = Arrays.asList(columnNames);
                setId(cursor.getInt(columnNamesList.indexOf(ID)), false);
                setName(cursor.getString(columnNamesList.indexOf(NAME)), false);
                setType(cursor.getInt(columnNamesList.indexOf(TYPE)), false);
                setDeviceid(cursor.getString(columnNamesList.indexOf(DEVICEID)), false);
                setApikey(cursor.getString(columnNamesList.indexOf(APIKEY)), false);
                setOnline(cursor.getInt(columnNamesList.indexOf(ONLINE)) == 1, false);
                setGroup(cursor.getString(columnNamesList.indexOf(GROUP)), false);
                LoadDeviceList(mContext);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        setId(id, true);
    }

    public void setId(Integer id, Boolean updateDB) {
        this.id = id;
        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(ID, id);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        setName(name, true);
    }

    public void setName(String name, boolean updateDB) {
        this.name = name;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.NAME, name);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        setType(type, true);
    }

    public void setType(Integer type, boolean updateDB) {
        this.type = type;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.TYPE, type);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public void setDeviceid(String deviceid, boolean updateDB) {
        this.deviceid = deviceid;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.DEVICEID, deviceid);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        setApikey(apikey, true);
    }

    public void setApikey(String apikey, boolean updateDB) {
        this.apikey = apikey;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.APIKEY, apikey);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }


    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        setOnline(online, true);
    }

    public void setOnline(boolean online, boolean updateDB) {
        this.online = online;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.ONLINE, online);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        setGroup(group, true);
    }

    public void setGroup(String group, boolean updateDB) {
        this.group = group;

        if (updateDB) {
            ContentValues values = new ContentValues();
            values.put(Device.GROUP, group);
            open();
            database.update(MySnapDbOpenHelper.TABLE_DEVICE, values, Device.DEVICEID + "=?", new String[]{getDeviceid()});
            close();
            Device.LoadDeviceList(mContext);
        }
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public static void LoadDeviceList(Context mContext) {
        Device device = new Device(mContext);
        Globals._deviceList = device.getAllDevices(mContext);
    }


    public int removeDevice() {
        open();
        int deleted = database.delete(MySnapDbOpenHelper.TABLE_DEVICE, Device.ID + "=?", new String[]{getId().toString()});
        close();
        return deleted;
    }


    public List<Device> getAllDevices(Context context) {
        dbHelper = new MySnapDbOpenHelper(context);
        open();

        List<Device> allDevices = new ArrayList<>();

        Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_DEVICE, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            return allDevices;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allDevices.add(cursorToDevice(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return allDevices;
    }

    private Device cursorToDevice(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        List<String> columnNamesList = Arrays.asList(columnNames);
        Device device = new Device(mContext);
        device.setId(cursor.getInt(columnNamesList.indexOf(ID)), false);
        device.setName(cursor.getString(columnNamesList.indexOf(NAME)), false);
        device.setType(cursor.getInt(columnNamesList.indexOf(TYPE)), false);
        device.setDeviceid(cursor.getString(columnNamesList.indexOf(DEVICEID)), false);
        device.setApikey(cursor.getString(columnNamesList.indexOf(APIKEY)), false);
        device.setOnline(cursor.getInt(columnNamesList.indexOf(ONLINE)) == 1, false);
        device.setGroup(cursor.getString(columnNamesList.indexOf(GROUP)), false);
        return device;
    }

    @Override
    public String toString() {
        return "Device{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", apikey='" + apikey + '\'' +
                ", online=" + online +
                ", group='" + group + '\'' +
                '}';
    }
}
