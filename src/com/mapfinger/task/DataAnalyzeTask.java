package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.KeyPointClusterExecutor;

public class DataAnalyzeTask extends Task {
	
	public DataAnalyzeTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		logger.info("Execute new DataAnalyzeTask: " + this.userData.toString());
		
		KeyPointClusterExecutor executor = new KeyPointClusterExecutor(userData);
		boolean response = executor.execute();
		
		if (response) {
			// TODO 对已聚类处理的驻留点进行规则提取
		}
	}
	
}
