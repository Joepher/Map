package com.mapfinger.task;

import com.mapfinger.entity.UserData;

public class Task implements Runnable {
	protected UserData userData;
	
	public Task(UserData userData) {
		this.userData = userData;
	}
	
	@Override
	public void run() {}
}
