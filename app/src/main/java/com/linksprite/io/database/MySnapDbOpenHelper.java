/*
 * ************************************************************************
 *  *
 *  * MySnapCam CONFIDENTIAL
 *  * FILE: MySnapDbOpenHelper.java
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
 *  * Written: Nate Ridderman
 *  * Updated: 6/23/2015
 *
 */

package com.linksprite.io.database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.linksprite.io.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MySnapDbOpenHelper extends SQLiteOpenHelper {

	// Deprecate global instance?
	private static MySnapDbOpenHelper instance;

	private Context context;

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "linksprite_io";
	private String createSQL;

	public static final String TABLE_DEVICE = "Device";
	public static final String TABLE_ACCOUNT = "Account";
	public static final String TABLE_APP_SETTINGS = "Application";
	public static final String TABLE_WEATER = "Weater";
	// Deprecate global instance?
	public static synchronized MySnapDbOpenHelper getHelper(Context context) {
		if (instance == null) {
			instance = new MySnapDbOpenHelper(context);
		}
		return instance;
	}
	
	public MySnapDbOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		this.context = context;

		Resources res = context.getResources();
        InputStream inputStream = res.openRawResource(R.raw.create_weather_station);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder createSQLBuilder = new StringBuilder();
        try {
            while (( line = reader.readLine()) != null) {
            	createSQLBuilder.append(line);
            	createSQLBuilder.append('\n');
              }
        } catch (IOException e) {
        	Log.e("SettingsOpenHelper", "error creating database");
        }
        
        createSQL = createSQLBuilder.toString();
	}

	private String getMigration(Context context, int migrationResource) {
		Resources res = context.getResources();
		InputStream inputStream = res.openRawResource(migrationResource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder createSQLBuilder = new StringBuilder();
		try {
			while (( line = reader.readLine()) != null) {
				createSQLBuilder.append(line);
				createSQLBuilder.append('\n');
			}
		} catch (IOException e) {
			Log.e("SettingsOpenHelper", "error migrating database");
		}

		return createSQLBuilder.toString();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] statements = createSQL.split(";");
		for (String statement : statements ) {
			if (statement.trim().length() > 0)
				db.execSQL(statement.trim() + ";");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.i("MySnapDbOpenHelper", "Upgrading database from " + oldVersion + " to " + newVersion);


	}


}
