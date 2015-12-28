package com.mapfinger.executor;

import org.apache.logging.log4j.Logger;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.FileLogger;

public abstract class BaseExecutor {
	protected Logger logger;
	
	public BaseExecutor(UserData userData) {
		this.logger = FileLogger.getLogger();
	}
	
	public abstract boolean execute();
	
	protected void runwork() {};
}
