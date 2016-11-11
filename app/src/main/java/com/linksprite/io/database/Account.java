/*
 * ************************************************************************
 *  *
 *  * MySnapCam CONFIDENTIAL
 *  * FILE: Account.java
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Account {

	private SQLiteDatabase database;
	private MySnapDbOpenHelper dbHelper;
	
	public static final String _ID = "_id";
    public static final String JWT = "jwt";
	public static final String EMAIL      = "email";
	public static final String CREATEAT  = "createdAt";
	public static final String APIKEY = "apikey";
	public static final String IAT  = "iat";
	public static final String USERPASSWORD = "password";

	private Context context;

	private String  _id,jwt,email,createat,apikey,iat,userPassword;

	
	public Account(Context context) {
		this.context = context;
		
		dbHelper = new MySnapDbOpenHelper(context);
		open();

		Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_ACCOUNT, null, null, null, null, null, null);
		if (cursor.getCount() != 0 && cursor.moveToFirst()) {
			String[] columnNames = cursor.getColumnNames();
			List<String> columnNamesList = Arrays.asList(columnNames);
			set_ID(cursor.getString(columnNamesList.indexOf(_ID)), false);
			setJwt(cursor.getString(columnNamesList.indexOf(JWT)), false);
			setEmail(cursor.getString(columnNamesList.indexOf(EMAIL)) , false);
            setCreateAt(cursor.getString(columnNamesList.indexOf(CREATEAT)), false);
			setApikey(cursor.getString(columnNamesList.indexOf(APIKEY)), false);
			setIat(cursor.getString(columnNamesList.indexOf(IAT)), false);
			setUserPassword(cursor.getString(columnNamesList.indexOf(USERPASSWORD)), false);
		}
		cursor.close();
		close();
	}
	
	public Account(Context context, ContentValues values) {
		dbHelper = new MySnapDbOpenHelper(context);
		open();
		
		if (values != null) {
			database.execSQL("DELETE FROM " + MySnapDbOpenHelper.TABLE_ACCOUNT);
			long result = database.insertOrThrow(MySnapDbOpenHelper.TABLE_ACCOUNT, null, values);
			//Log.i("INSERTING", Long.toString(result));
		}

		Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_ACCOUNT, null, null, null, null, null, null);
		if (cursor.getCount() != 0 && cursor.moveToFirst()) {
			String[] columnNames = cursor.getColumnNames();
			List<String> columnNamesList = Arrays.asList(columnNames);
			set_ID(cursor.getString(columnNamesList.indexOf(_ID)), false);
			setJwt(cursor.getString(columnNamesList.indexOf(JWT)), false);
			setEmail(cursor.getString(columnNamesList.indexOf(EMAIL)) , false);
			setCreateAt(cursor.getString(columnNamesList.indexOf(CREATEAT)), false);
			setApikey(cursor.getString(columnNamesList.indexOf(APIKEY)), false);
			setIat(cursor.getString(columnNamesList.indexOf(IAT)), false);
			setUserPassword(cursor.getString(columnNamesList.indexOf(USERPASSWORD)), false);
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
	
	public String get_ID() { return _id; }
	public void set_ID(String userId) { set_ID(userId, true); }
	public void set_ID(String userId, Boolean updateDB) {
		this._id = userId;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account._ID, userId);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	
	public String getJwt() { return jwt; }
	public void setJwt(String jwt) { setJwt(jwt, true); }
	public void setJwt(String jwt, Boolean updateDB) {
		this.jwt = jwt;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.JWT, jwt);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	
	public String getUserPassword() { return userPassword; }
	public void setUserPassword(String userPassword) { setUserPassword(userPassword, true); }
	public void setUserPassword(String userPassword, Boolean updateDB) {
		this.userPassword = userPassword;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.USERPASSWORD, userPassword);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	
	public String getEmail() { return email; }
	public void setUserFirstName(String email) { setEmail(email, true); }
	public void setEmail(String email, Boolean updateDB) {
		this.email = email;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.EMAIL, email);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	
	public String getCreateat() { return createat; }
	public void setCreateAt(String createat) { setCreateAt(createat, true); }
	public void setCreateAt(String createat, Boolean updateDB) {
		this.createat = createat;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.CREATEAT, createat);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	
	public String getApikey() { return apikey; }
	public void setApikey(String userEmail) { setApikey(userEmail, true); }
	public void setApikey(String apikey, Boolean updateDB) {
		this.apikey = apikey;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.APIKEY, apikey);
			open();
			int result = database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			Log.i("DB UPDATE", Integer.toString(result));
			close();
		}
	}
	
	public String getIat() { return iat; }
	public void setIat(String iat) { setIat(iat, true); }
	public void setIat(String iat, Boolean updateDB) {
		this.iat = iat;
		
		if (updateDB) {
			ContentValues values = new ContentValues();
			values.put(Account.IAT, iat);
			open();
			database.update(MySnapDbOpenHelper.TABLE_ACCOUNT, values, null, null);
			close();
		}
	}
	


	
	public void dump(Boolean toFile) {
		Log.i("Account", "Dumping account table");
		open();
		Cursor cursor = database.query(MySnapDbOpenHelper.TABLE_ACCOUNT, null, null, null, null, null, null);
		
		String output = "{";
		if (cursor != null && cursor.moveToFirst()) {
			for (int i=0; i < cursor.getColumnCount(); i++) {
				output += cursor.getColumnName(i) +":";
				if (!cursor.isNull(i)) {
					
					if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
						output += cursor.getString(i);
					}
					else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
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
			Log.i("Account Database Dump", output);
		}
		else {
			String storageState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(storageState)) {					
				try {
					String root = Environment.getExternalStorageDirectory().toString();
					String fileName =  "DB_Account_" + System.currentTimeMillis() + ".txt";
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
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
