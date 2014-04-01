package com.doitnow;

import com.doitnow.activities.MainActivity;
import com.parse.Parse;
import com.parse.PushService;

public class Application extends android.app.Application {
	public Application() {
		
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, "mx2jSxInUmET4bNwItz5JpUJpheDBMnt8yl5SOBb", "61wmIKqT3GRpTp21kdKpaDkwUEV9ipnKbBbT8U6n");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
	}
}
