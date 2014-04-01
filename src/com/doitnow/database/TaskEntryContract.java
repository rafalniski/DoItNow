package com.doitnow.database;



import android.provider.BaseColumns;
import android.provider.SyncStateContract.Columns;

public class TaskEntryContract {
	private String id;
	private String title;
	private String desc;
	private String priority;
	private String due_date;
	private String creation_date;
	private String is_completed;
	
	public TaskEntryContract() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(String is_completed) {
		this.is_completed = is_completed;
	}

	public static abstract class TaskEntry implements BaseColumns {
		public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TASK_ID = "taskid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_CREATED = "created_at";
        public static final String COLUMN_NAME_COMPLETED = "is_completed";
	}
	public static final String TEXT_TYPE = " TEXT";
	public static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_TASKS =
	    "CREATE TABLE IF NOT EXISTS " + TaskEntry.TABLE_NAME + " (" +
	    TaskEntry._ID + 					" INTEGER PRIMARY KEY," 			  +
	    TaskEntry.COLUMN_NAME_TASK_ID + 	TEXT_TYPE + 			COMMA_SEP +
	    TaskEntry.COLUMN_NAME_TITLE + 		TEXT_TYPE + 			COMMA_SEP +
	    TaskEntry.COLUMN_NAME_DESC + 	TEXT_TYPE + 				COMMA_SEP +
	    TaskEntry.COLUMN_NAME_PRIORITY + 		TEXT_TYPE + 			COMMA_SEP +
	    TaskEntry.COLUMN_NAME_DUE_DATE + 		TEXT_TYPE + 			COMMA_SEP +
	    TaskEntry.COLUMN_NAME_CREATED + 		TEXT_TYPE + 			COMMA_SEP +
	    TaskEntry.COLUMN_NAME_COMPLETED + 		TEXT_TYPE + 		
	    ")";
	
	public static final String SQL_DELETE_TASKS =
	    "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;
	
	
}
