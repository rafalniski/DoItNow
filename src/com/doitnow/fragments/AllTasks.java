package com.doitnow.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListFragment;
import android.app.SearchManager;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.doitnow.activities.AddTask;
import com.doitnow.database.DatabaseDataSource;
import com.doitnow.database.TaskEntryContract;
import com.doitnow.utils.DataUtilities;
import com.mattyork.colours.Colour;
import com.toptaltodo.R;

@SuppressLint("ValidFragment")
public class AllTasks extends ListFragment implements OnNavigationListener{
	
	private DatabaseDataSource db;
    private AllTasksAdapter adapter;
    private ActionMode mActionMode;
    private int order = 0;
    private static final int REQUEST_EDIT = 1;
    private SharedPreferences prefs;
    private int position = 0;
    public AllTasks(){}
	public AllTasks(int position){
		this.position = position;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	prefs = getActivity().getSharedPreferences("login_prefs",getActivity().MODE_PRIVATE);
    	order = prefs.getInt("order", 0);
    	getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    	final String[] dropdownValues = getResources().getStringArray(R.array.priority_list);
    	ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getActionBar().getThemedContext(),
    	        android.R.layout.simple_spinner_item, android.R.id.text1,
    	        dropdownValues);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getActivity().getActionBar().setListNavigationCallbacks(spinnerAdapter, mNavigationCallback);
    }
    
    OnNavigationListener mNavigationCallback = new OnNavigationListener() {
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences("login_prefs",getActivity().MODE_PRIVATE).edit();
		editor.putInt("order", itemPosition);
		order = itemPosition;
		loadAdapter(position);
		return false;
	}
};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	 return super.onCreateView(inflater, container, savedInstanceState);    	    
    }
    private void loadAdapter(int position) {
    	ArrayList<TaskItem> items = new ArrayList<TaskItem>();
    	db = new DatabaseDataSource(getActivity());
    	db.open();
    	List<TaskEntryContract> tasks = null;
    	switch(position) {
    	case 0:
    		tasks = db.getAllTasks(order);
    		break;
    	case 1:
    		tasks = db.getDateTasks(order,DataUtilities.getTodayDate());
    		break;
    	case 2:
    		tasks = db.getDateTasks(order,DataUtilities.getSevenDaysDate());
    		break;
    	case 3:
    		tasks = db.getCompletedTasks(order);
    		break;
    	}
    	
    	if(tasks.size() != 0) {
	    	for(TaskEntryContract task : tasks) {
	    		items.add(new TaskItem(task.getTitle(), task.getDue_date(), task.getPriority()));
	    	}
    	}
    	adapter = new AllTasksAdapter(getActivity(), items);
    	getListView().setSelector(R.drawable.list_task_selector);
    	setListAdapter(adapter);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub  
    	loadAdapter(position);
    	super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    }
   
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	loadAdapter(position);
    	adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	TaskItem item = (TaskItem) getListAdapter().getItem(position);
    	//getListView().setItemChecked(position, true);
        //getListView().setSelection(position);
    	mActionMode = getActivity().startActionMode(new ActionModeCallback(item));
    	

    }
    
    private class ActionModeCallback implements Callback {
    	
    	private TaskItem taskItem;
        public ActionModeCallback(TaskItem item) {
			this.taskItem = item;
		}

		@Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.task_modify, menu);
            return true;
        }
 
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        	if(position == 3) {
        		menu.findItem(R.id.menu_completed).setEnabled(false);
        	} else {
        		menu.findItem(R.id.menu_completed).setEnabled(true);
        	}
            return false;
        }
 
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        	DatabaseDataSource db = new DatabaseDataSource(getActivity());
            switch (item.getItemId()) {
            case R.id.menu_delete:
            	db.open();
            	db.deleteTask(taskItem.getName());
            	loadAdapter(position);
            	db.close();
            	mode.finish();
            	break;
            case R.id.menu_edit:
            	Intent intent = new Intent(getActivity(), AddTask.class);
            	Bundle extras = new Bundle();
            	extras.putBoolean("edit", true);
            	extras.putString("title", taskItem.getName());
            	extras.putString("date", taskItem.getDueDate());
            	extras.putString("priority", taskItem.getPriority());
            	intent.putExtras(extras);
            	startActivityForResult(intent, REQUEST_EDIT);
            	break;
            case R.id.menu_completed:
            	db.open();
            	db.completeTask(taskItem.getName());
            	loadAdapter(position);
            	db.close();
            	mode.finish();
            	break;
            default:
                return false;
            }
			return true;
 
        }
 
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection
            mActionMode = null;
        }
    }

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}
}
