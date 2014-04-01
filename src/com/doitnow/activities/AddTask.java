package com.doitnow.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import com.doitnow.database.DatabaseDataSource;
import com.doitnow.fragments.DatePickerfragment;
import com.doitnow.fragments.DatePickerfragment.DatePickerListener;
import com.toptaltodo.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddTask extends Activity implements OnItemSelectedListener,
		DatePickerListener {

	private String prioritySelected = "";
	private String titleEdited;
	private String dueDateEdited;
	private String priorityEdited;
	private EditText titleText;
	private Boolean isEdit = false;
	private String oldTitle = "";
	private TextView date;
	public static final int DATEPICKER_FRAGMENT = 1;
	ImageButton dateButton;
	private String dateSelected = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra("edit"))
			isEdit = getIntent().getExtras().getBoolean("edit");

		setContentView(R.layout.fragment_add_task);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		titleText = (EditText) findViewById(R.id.taskTitle);
		date = (TextView) findViewById(R.id.date_picked);
		Spinner spinner = (Spinner) findViewById(R.id.priority_spinner);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.priorities_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		dateButton = (ImageButton) findViewById(R.id.dateButton);
		if (Intent.ACTION_SEND.equals(getIntent().getAction())
				&& getIntent().getType().equals("text/plain")) {
			handleSendText(getIntent());
		}
		if (isEdit) {
			titleEdited = getIntent().getExtras().getString("title");
			oldTitle = titleText.getText().toString();
			titleText.setText(titleEdited);
			dueDateEdited = getIntent().getExtras().getString("date");
			date.setVisibility(View.VISIBLE);
			Date data = null;
			try {
				data = new SimpleDateFormat("yyyy/MM/dd").parse(dueDateEdited);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // like "HH:mm" or just "mm", whatever you want
			String formattedDate = new SimpleDateFormat("yyyy/MM/dd")
					.format(data);
			date.setText(formattedDate);
			priorityEdited = getIntent().getExtras().getString("priority");
			prioritySelected = priorityEdited;
			dateSelected = dueDateEdited;
			String[] priorities = getResources().getStringArray(
					R.array.priorities_array);
			int index = Arrays.asList(priorities).indexOf(priorityEdited);
			spinner.setSelection(index);
		}

		dateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEdit) {
					String[] parts = dueDateEdited.split("/");
					DialogFragment newFragment = DatePickerfragment
							.newInstance(Integer.parseInt(parts[0]),
									Integer.parseInt(parts[1]),
									Integer.parseInt(parts[2]));
					newFragment.show(getFragmentManager(), "Pick due date");
				} else {
					DialogFragment newFragment = DatePickerfragment
							.newInstance();
					newFragment.show(getFragmentManager(), "Pick due date");
				}
			}
		});

	}

	void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			titleText.setText(sharedText);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_submit:
			if (isEdit) {
				editTask();
			} else {
				addTask();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void editTask() {
		DatabaseDataSource db = new DatabaseDataSource(this);
		db.open();
		String title = titleText.getText().toString();
		db.editTask(titleEdited, title, prioritySelected, dateSelected,
				getDateTime());
		finish();
	}

	private void addTask() {
		DatabaseDataSource db = new DatabaseDataSource(this);
		db.open();

		String title = titleText.getText().toString();
		db.createTask(title, "", prioritySelected, dateSelected, getDateTime());
		finish();

	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.taskadd, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		prioritySelected = (String) parent.getItemAtPosition(position);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateResult(int year, String month, int day) {
		dateSelected = year + "-" + month + "-" + day;
		Date data = null;
		try {
			data = new SimpleDateFormat("yyyy-MM-dd").parse(dateSelected);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // like "HH:mm" or just "mm", whatever you want
		String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(data);
		date.setText(formattedDate);
		date.setVisibility(View.VISIBLE);

	}
}
