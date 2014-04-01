package com.doitnow.fragments;

import com.doitnow.utils.DataUtilities;
import com.mattyork.colours.Colour;

public class TaskItem {
	private String name;
	private String dueDate;
	private int color;
	private String priority;
	
	public TaskItem(String name, String dueDate, String color) {
		this.setName(name);
		this.setDueDate(dueDate);
		this.setColor(color);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.isEmpty())
			name = "Default name";
		this.name = name;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		if(dueDate.isEmpty())
			dueDate = DataUtilities.getTodayDate();
		this.dueDate = dueDate;
	}

	public int getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color.equals("Urgent")) {
			this.color = Colour.strawberryColor();
			this.priority = "High";
		}
		else if(color.equals("Medium")) {
			this.color = Colour.tomatoColor();
			this.priority = "Normal";
		}
		else if(color.equals("Low")) {
			this.color = Colour.seafoamColor();
			this.priority = "Low";
		}
		else {
			this.color = Colour.tomatoColor();
			this.priority = "Medium";
		}
		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Name: " + getName() + " Due Date: " + getDueDate(); 
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		if(priority.isEmpty())
			priority = "medium";
		this.priority = priority;
	}
}
