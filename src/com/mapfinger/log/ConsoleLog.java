package com.mapfinger.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConsoleLog {
	public static void log(String msg) {
		System.out.println("[" + getTime() + "] " + Thread.currentThread().getId() + ": " + msg);
	}
	
	private static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(Calendar.getInstance().getTime());
	}
}
