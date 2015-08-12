package com.mapfinger.service;

import java.util.List;
import com.mapfinger.entity.UserData;

public interface DataService {
	
	public boolean fire(UserData userData);
	
	public boolean fire(List<UserData> list);
	
	public void stop();
	
}
