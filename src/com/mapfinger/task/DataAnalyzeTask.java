package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.keypoint.KeyPointClusterExecutor;

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
			// XXX ���Ѿ��ദ���פ������й�����ȡ[���ܲ���Ҫ]
			/**
			 * ���й�����ȡ������Sequentiality-�����
			 */
		}
	}
	
}
