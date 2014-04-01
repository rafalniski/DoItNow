package com.doitnow.database;

import com.doitnow.database.TaskEntryContract.TaskEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TODODB2.db";
    
	public ToDoDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TaskEntryContract.SQL_CREATE_TASKS);
		db.execSQL(UserEntryContract.SQL_CREATE_USERS);
		
	}
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TaskEntryContract.SQL_DELETE_TASKS);
		db.execSQL(UserEntryContract.SQL_DELETE_USERS);
		
	}

}
