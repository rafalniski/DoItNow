package com.doitnow.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;  

import com.toptaltodo.R;

public class AllTasksAdapter extends BaseAdapter{

	private Context context; 
	private final ArrayList<TaskItem> items;
	public AllTasksAdapter(Context context, ArrayList<TaskItem> items) {
		this.context = context;
		this.items = items;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1. Create inflater 
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.all_tasks_list_item, parent, false);
        TextView taskPriority = (TextView) rowView.findViewById(R.id.taskPriority);
        TextView taskName = (TextView) rowView.findViewById(R.id.taskName);
        TextView taskDueDate = (TextView) rowView.findViewById(R.id.taskDueDate);
        
        taskPriority.setBackgroundColor(items.get(position).getColor());
        taskName.setText(items.get(position).getName());
        taskDueDate.setText(items.get(position).getDueDate());
        return rowView;
	}

}
