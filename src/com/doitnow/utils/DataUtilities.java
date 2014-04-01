package com.doitnow.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtilities {

	public static String getTodayDate() {
    	Calendar cal = Calendar.getInstance();
	    String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
		return formattedDate;
    }
    public static String getSevenDaysDate() {
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,7);
	    String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
		return formattedDate;
    }
}
