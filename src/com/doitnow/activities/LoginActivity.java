package com.doitnow.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.doitnow.database.DatabaseDataSource;
import com.toptaltodo.R;

public class LoginActivity extends Activity {
	
	private EditText login;
	private EditText password;
	private Button loginButton;
	private TextView errorMsg;
	private Button regButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = getSharedPreferences("login_prefs",MODE_PRIVATE);
        String loginText = prefs.getString("login", null);
        String passwordText = prefs.getString("password", null);
        if(loginText !=null) {
    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    		startActivity(intent);
    		finish();
        }
		setContentView(R.layout.activity_login);
		getWindow().getDecorView().setSystemUiVisibility(
		          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_IMMERSIVE);
		login = (EditText) findViewById(R.id.loginText);
		password = (EditText) findViewById(R.id.passwordText);
		loginButton = (Button) findViewById(R.id.loginButton);
		errorMsg = (TextView) findViewById(R.id.badCredentials);
		regButton = (Button) findViewById(R.id.regButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleLoginAction();
			}
		});
		regButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
				startActivityForResult(intent, 1);
				finish();
			}
		});
	}
	private boolean handleLoginAction() {
		String loginText = login.getText().toString();
		String passwordText = password.getText().toString();
		if(loginText.isEmpty()) {
			errorMsg.setText("Login cannot be empty!");
			return false;
		} else if (passwordText.isEmpty()) {
			errorMsg.setText("Password cannot be empty!");
			return false;
		}
		DatabaseDataSource db = new DatabaseDataSource(this);
    	db.open();
    	boolean areCredentialsCorrect = db.loginUser(loginText, passwordText);
    	if(areCredentialsCorrect) {
    		//storing in SharedPreferences
    		SharedPreferences.Editor editor = getSharedPreferences("login_prefs",MODE_PRIVATE).edit();
    		editor.putString("login", loginText);
    		editor.putString("password", passwordText);
    		editor.commit();
    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    		Bundle extras = new Bundle();
    		extras.putString("loggedin", "Hi " + loginText + ", welcome to TotalTODO app!");
    		intent.putExtras(extras);
    		startActivity(intent);
    		finish();
    	} else {
    		errorMsg.setText("Login or password is incorrect!");
			return false;
    	}
		return true;
		
	}
}
