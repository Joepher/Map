package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.KeyPointDetectionExecutor;
import com.mapfinger.service.DataAnalyzeService;

public class DataParseTask extends Task {
	public DataParseTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		KeyPointDetectionExecutor executor = new KeyPointDetectionExecutor(userData);
		boolean response = executor.execute();
		
		if (response) {
			DataAnalyzeService.getInstance().fire(userData);
		}
	}
	
}
