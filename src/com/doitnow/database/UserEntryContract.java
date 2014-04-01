package com.doitnow.database;

import com.doitnow.database.TaskEntryContract.TaskEntry;

import android.provider.BaseColumns;

public class UserEntryContract {
	public static abstract class UserEntry implements BaseColumns {
		public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_USER_LOGIN = "login";
        public static final String COLUMN_NAME_USER_PASSWORD= "password";
        public static final String COLUMN_NAME_USER_EMAIL = "email";
        public static final String COLUMN_NAME_USER_REG_TIME = "regtime";
	}
	public static final String TEXT_TYPE = " TEXT";
	public static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_USERS =
	"CREATE TABLE IF NOT EXISTS " + UserEntry.TABLE_NAME + " (" +
	    UserEntry._ID + 					" INTEGER PRIMARY KEY," 			  +
	    UserEntry.COLUMN_NAME_USER_ID + 	TEXT_TYPE + 			COMMA_SEP +
	    UserEntry.COLUMN_NAME_USER_LOGIN + 		TEXT_TYPE + 			COMMA_SEP +
	    UserEntry.COLUMN_NAME_USER_PASSWORD + 		TEXT_TYPE + 			COMMA_SEP +
	    UserEntry.COLUMN_NAME_USER_EMAIL + 		TEXT_TYPE + 			COMMA_SEP +
	    UserEntry.COLUMN_NAME_USER_REG_TIME + 		TEXT_TYPE + 	
    ")";
	
	public static final String SQL_DELETE_USERS =
		    "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
	
	private long id;
	private String login;
	private String password;
	private String email;
	private String regTime;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

}
