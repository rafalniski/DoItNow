package com.doitnow.activities;

import com.doitnow.database.DatabaseDataSource;
import com.toptaltodo.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class SignUpActivity extends Activity {
	
	private EditText login;
	private EditText password;
	private EditText repassword;
	private TextView errorMsg;
	private EditText email;
	private Button regButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		login = (EditText) findViewById(R.id.regloginText);
		password = (EditText) findViewById(R.id.regpasswordText);
		repassword = (EditText) findViewById(R.id.regrepasswordText);
		errorMsg = (TextView) findViewById(R.id.badCredentials);
		email = (EditText) findViewById(R.id.emailText);
		regButton = (Button) findViewById(R.id.registerButton);
		regButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isSignedUp = signUpUser();
				if(isSignedUp) {
					SharedPreferences.Editor editor = getSharedPreferences("login_prefs",MODE_PRIVATE).edit();
		    		editor.putString("login", login.getText().toString());
		    		editor.putString("password", password.getText().toString());
		    		editor.commit();
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		    		Bundle extras = new Bundle();
		    		extras.putString("loggedin", "Hi " + login.getText().toString() + ", welcome to TotalTODO app!");
		    		intent.putExtras(extras);
		    		startActivity(intent);
		    		finish();
				}
				
			}
		});
		
	}
	private boolean signUpUser() {
		String loginText = login.getText().toString();
		String passwordText = password.getText().toString();
		String rePasswordText = repassword.getText().toString();
		String emailText = email.getText().toString();
		if(loginText.isEmpty() || passwordText.isEmpty() || rePasswordText.isEmpty() || emailText.isEmpty()) {
			errorMsg.setText("All the inputs must be filled!");
			return false;
		}else if(!passwordText.equals(rePasswordText)) {
			errorMsg.setText("Passwords must be identical!");
			return false;
		} else if(!emailText.contains("@")) {
			errorMsg.setText("Email is incorrect!");
			return false;
		}
		DatabaseDataSource db = new DatabaseDataSource(this);
    	db.open();
    	boolean isRegistered  = db.registerUser(loginText, passwordText, emailText);
    	if(!isRegistered) {
    		errorMsg.setText("This login is already in use, try another one!");
    		return false;
    	}
    	return isRegistered;
	}
}
