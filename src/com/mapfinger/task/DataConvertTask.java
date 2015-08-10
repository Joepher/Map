package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.ConvertToBDLocationExecutor;
import com.mapfinger.service.DataParseService;

public class DataConvertTask extends Task {
	
	public DataConvertTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		ConvertToBDLocationExecutor executor = new ConvertToBDLocationExecutor(userData);
		boolean response = executor.execute();
		
		if (response) {
			DataParseService.getInstance().fire(userData);
		}
	}
}
