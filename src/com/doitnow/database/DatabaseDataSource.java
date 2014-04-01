package com.doitnow.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.doitnow.database.TaskEntryContract.TaskEntry;
import com.doitnow.database.UserEntryContract.UserEntry;
import com.doitnow.utils.DataUtilities;

public class DatabaseDataSource {

	// Database fields
	  private SQLiteDatabase database;
	  private ToDoDatabaseHelper dbHelper;
	  
	  private String[] taskColumns = { 
			  TaskEntry.COLUMN_NAME_TASK_ID,
			  TaskEntry.COLUMN_NAME_TITLE,
			  TaskEntry.COLUMN_NAME_DESC,
			  TaskEntry.COLUMN_NAME_PRIORITY,
			  TaskEntry.COLUMN_NAME_DUE_DATE,
			  TaskEntry.COLUMN_NAME_CREATED,
			  TaskEntry.COLUMN_NAME_COMPLETED
	 };
	  
	 private String[] userColumns = { 
			  UserEntry.COLUMN_NAME_USER_ID,
			  UserEntry.COLUMN_NAME_USER_LOGIN,
			  UserEntry.COLUMN_NAME_USER_PASSWORD,
			  UserEntry.COLUMN_NAME_USER_EMAIL,
			  UserEntry.COLUMN_NAME_USER_REG_TIME,
	 };


	  public DatabaseDataSource(Context context) {
	    dbHelper = new ToDoDatabaseHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public boolean editTask(String oldTitle, String title, String priority, String due_date, String created_at) {
		  ContentValues values = new ContentValues();
		    values.put(TaskEntry.COLUMN_NAME_TITLE,title);
		    values.put(TaskEntry.COLUMN_NAME_PRIORITY,priority);
		    Date date = null;
		    try {
		    	if(due_date.indexOf("-") > 0) {
		    		date = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
		    	} else {
		    		date = new SimpleDateFormat("yyyy/MM/dd").parse(due_date);
		    	}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //like "HH:mm" or just "mm", whatever you want
		    String formattedDate = null;
		    String newdate = date.toString();
		    if(date.toString().indexOf("-") > 0) {
		    	 formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		    } else {
		    	 formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);

		    }
		   
		    values.put(TaskEntry.COLUMN_NAME_DUE_DATE,formattedDate);
		    values.put(TaskEntry.COLUMN_NAME_CREATED,created_at);
		    long updateId = database.update(TaskEntry.TABLE_NAME,values, TaskEntry.COLUMN_NAME_TITLE +"="+ "\"" +oldTitle + "\"" ,
		        null);
		    return true;
	  }
	  
	  public boolean loginUser(String login, String password) {
		  String selection = UserEntry.COLUMN_NAME_USER_LOGIN + "=" + "\"" + login + "\"" + "AND " + 
				  			 UserEntry.COLUMN_NAME_USER_PASSWORD + "=" + "\"" + password + "\"";
		
		    Cursor cursor = database.query(UserEntry.TABLE_NAME, userColumns, selection, null, null, null, null);
		    if(cursor.moveToFirst()) {
		    	return true;
		    }
		    return false;
	  }
	  
	  public boolean registerUser(String login, String password, String email) {
		  ContentValues values = new ContentValues(); 
		    values.put(UserEntry.COLUMN_NAME_USER_LOGIN,login);
		    values.put(UserEntry.COLUMN_NAME_USER_PASSWORD,password);
		    values.put(UserEntry.COLUMN_NAME_USER_EMAIL,email);
		    String selection = UserEntry.COLUMN_NAME_USER_LOGIN + "=" + "\"" + login + "\"";
		    Cursor cursor = database.query(UserEntry.TABLE_NAME, userColumns, selection, null, null, null, null);
		    if(cursor.moveToFirst()) {
		    	System.out.println("Taki user juz istnieje!");
		    	return false;
		    } 
		   long insertId = database.insert(UserEntry.TABLE_NAME, null, values);
		   return true;
	  }
	  
	  public boolean completeTask(String title) {
		  ContentValues values = new ContentValues();
		    values.put(TaskEntry.COLUMN_NAME_COMPLETED,1);
		    long updateId = database.update(TaskEntry.TABLE_NAME,values, TaskEntry.COLUMN_NAME_TITLE +"="+ "\"" +title + "\"" ,
		        null);
		    return true;
	  }
	  
	  public boolean createTask(String title, String desc, String priority, String due_date, String created_at) {
	    ContentValues values = new ContentValues(); 
	    values.put(TaskEntry.COLUMN_NAME_TITLE,title);
	    values.put(TaskEntry.COLUMN_NAME_DESC,desc);
	    values.put(TaskEntry.COLUMN_NAME_PRIORITY,priority);
	    Date date = null;
	    try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //like "HH:mm" or just "mm", whatever you want
	    String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
	    values.put(TaskEntry.COLUMN_NAME_DUE_DATE,formattedDate);
	    values.put(TaskEntry.COLUMN_NAME_CREATED,created_at);
	    values.put(TaskEntry.COLUMN_NAME_COMPLETED,0);
	    long insertId = database.insert(TaskEntry.TABLE_NAME, null,
	        values);
	    Cursor cursor = database.query(TaskEntry.TABLE_NAME,
	        taskColumns, TaskEntry.COLUMN_NAME_TASK_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    //TaskEntryContract task = cursorToTask(cursor);
	    cursor.close();
	    return true;
	  }
	  
	  private TaskEntryContract cursorToTask(Cursor cursor) {
		  if(cursor != null) {
		  TaskEntryContract task = new TaskEntryContract();
		  task.setTitle(cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_NAME_TITLE)));
		  task.setId(cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_NAME_TASK_ID)));
		  task.setDesc(cursor.getString(2));
		  task.setPriority(cursor.getString(3));
		  task.setDue_date(cursor.getString(4));
		  task.setCreation_date(cursor.getString(5));
		  task.setIs_completed(cursor.getString(6));
		  return task;
		  }
		  return null;
		  
	  }
	  
	  public void deleteTask(String taskName) {
	    database.delete(TaskEntry.TABLE_NAME, TaskEntry.COLUMN_NAME_TITLE
	        + " = " + "\"" + taskName + "\"", null);
	    
	  }

	  public List<TaskEntryContract>getAllTasks(int order) {
	    List<TaskEntryContract> tasks = new ArrayList<TaskEntryContract>();
	    String selection = TaskEntry.COLUMN_NAME_COMPLETED + "!=" + "1";
	    Cursor cursor;
	    if(order == 0) {
	    	cursor = database.query(TaskEntry.TABLE_NAME,
	    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_DUE_DATE);
	    } else if(order == 1) {
	    	cursor = database.query(TaskEntry.TABLE_NAME,
	    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_PRIORITY  + " DESC");
	    } else if(order == 2){
	    	cursor = database.query(TaskEntry.TABLE_NAME,
	    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_CREATED  + " DESC");
	    } else {
	    	cursor = database.query(TaskEntry.TABLE_NAME,
	    			taskColumns, selection, null, null, null, null);
	    }
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      TaskEntryContract task = cursorToTask(cursor);
	      tasks.add(task);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return tasks;
	  }
	  
	  public List<TaskEntryContract> getDateTasks(int order, String comparisonDate) {
		    List<TaskEntryContract> tasks = new ArrayList<TaskEntryContract>();
		    String selection = null;
		    if(comparisonDate.equals(DataUtilities.getTodayDate())) {
		    	 selection = TaskEntry.COLUMN_NAME_COMPLETED + "!=" + "1 AND " 
		    			 + TaskEntry.COLUMN_NAME_DUE_DATE + " = " + "\"" + comparisonDate + "\"";
		    } else {
		    	 selection = TaskEntry.COLUMN_NAME_COMPLETED + "!=" 
		    			 + "1 AND " + TaskEntry.COLUMN_NAME_DUE_DATE + " < " + "\"" + comparisonDate + "\"";
		    }
		    
		    Cursor cursor;
		    if(order == 0) {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_DUE_DATE);
		    } else if(order == 1) {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_PRIORITY  + " DESC");
		    } else if(order == 2){
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_CREATED  + " DESC");
		    } else {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, null);
		    }
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      TaskEntryContract task = cursorToTask(cursor);
		      tasks.add(task);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return tasks;
		  }
	  
	  public List<TaskEntryContract> getCompletedTasks(int order) {
		    List<TaskEntryContract> tasks = new ArrayList<TaskEntryContract>();
		    String selection = TaskEntry.COLUMN_NAME_COMPLETED + "= 1";
		    Cursor cursor;
		    if(order == 0) {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_DUE_DATE);
		    } else if(order == 1) {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_PRIORITY + " DESC");
		    } else if(order == 2) {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, TaskEntry.COLUMN_NAME_CREATED  + " DESC");
		    } else {
		    	cursor = database.query(TaskEntry.TABLE_NAME,
		    			taskColumns, selection, null, null, null, null);
		    }
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      TaskEntryContract task = cursorToTask(cursor);
		      tasks.add(task);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return tasks;
		  }
	  
	} 
