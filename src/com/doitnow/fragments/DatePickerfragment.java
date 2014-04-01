package com.doitnow.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerfragment extends DialogFragment implements OnDateSetListener {

	private static int year;
	private static int day;
	private static int month;
	
	private int yearEdited;
	private int monthEdited;
	private int dayEdited;
	
	public static DatePickerfragment newInstance() {
		return new DatePickerfragment();
	}
	public static DatePickerfragment newInstance(int yearEdited, int monthEdited, int dayEdited) {
		DatePickerfragment f = new DatePickerfragment();
		Bundle args = new Bundle();
		args.putInt("yearEdited", yearEdited);
		args.putInt("monthEdited", monthEdited);
		args.putInt("dayEdited", dayEdited);
		f.setArguments(args);
		return f;
	}
	public interface DatePickerListener {
		void updateResult(int year, String month, int day);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getArguments()!=null) {
			year = getArguments().getInt("yearEdited");
			month = getArguments().getInt("monthEdited")-1;
			day = getArguments().getInt("dayEdited");
		}
		
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		final Calendar c = Calendar.getInstance();
		
		if(year == 0) {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day =  c.get(Calendar.DAY_OF_MONTH);
		}
		
		return new DatePickerDialog(getActivity(), this, year,month, day);
	}
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		DatePickerListener activity = (DatePickerListener) getActivity();
		String month = String.format("%02d", monthOfYear+1);
		activity.updateResult(year, month, dayOfMonth);
		dismiss();
	}
	

}
