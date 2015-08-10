package com.mapfinger.executor;

import com.mapfinger.entity.UserData;

public abstract class BaseExecutor {
	public BaseExecutor(UserData userData) {}
	
	public abstract boolean execute();
}
