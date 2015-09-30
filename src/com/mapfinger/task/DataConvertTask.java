package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.convert.ConvertToBDLocationExecutor;
import com.mapfinger.service.DataParseService;

public class DataConvertTask extends Task {
	
	public DataConvertTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		logger.info("Execute new DataConvertTask: " + this.userData.toString());
		
		ConvertToBDLocationExecutor executor = new ConvertToBDLocationExecutor(userData);
		boolean response = executor.execute();
		
		if (response) {
			DataParseService.getInstance().fire(userData);
		}
	}
}
