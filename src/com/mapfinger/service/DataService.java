package com.mapfinger.service;

import com.mapfinger.entity.UserData;

public interface DataService {
	
	public boolean fire(UserData userData);
	
	public void stop();
	
}
