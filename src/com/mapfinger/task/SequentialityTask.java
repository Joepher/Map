package com.mapfinger.task;

import com.mapfinger.entity.UserData;
import com.mapfinger.executor.seq.SequentialityExecutor;

public class SequentialityTask extends Task {
	
	public SequentialityTask(UserData userData) {
		super(userData);
	}
	
	@Override
	public void run() {
		logger.info("Execute new SequentialityTask: " + userData.toString());
		
		SequentialityExecutor executor = new SequentialityExecutor(userData);
		
		if (executor.execute()) {
			// TODO �ѹ�����Sequentiality���������Ĺ�����
		}
		super.run();
	}
	
}
