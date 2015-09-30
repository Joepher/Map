package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.keypoint.KeyPointDetectionExecutor;
import com.mapfinger.service.DataAnalyzeService;

public class DataParseTask extends Task {
	public DataParseTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		logger.info("Execute new DataParseTask: " + userData.toString());
		
		KeyPointDetectionExecutor executor = new KeyPointDetectionExecutor(userData);
		boolean response = executor.execute();
		
		if (response) {
			DataAnalyzeService.getInstance().fire(userData);
		}
	}
	
}
