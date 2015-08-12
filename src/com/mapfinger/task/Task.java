package com.mapfinger.task;

import org.apache.logging.log4j.Logger;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.FileLogger;

public class Task implements Runnable {
	protected UserData userData;
	protected Logger logger;
	
	public Task(UserData userData) {
		this.userData = userData;
		this.logger = FileLogger.getLogger();
	}
	
	@Override
	public void run() {}
}
