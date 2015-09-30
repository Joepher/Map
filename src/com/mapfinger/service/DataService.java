package com.mapfinger.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.Logger;
import com.mapfinger.entity.UserData;

public abstract class DataService {
	protected Logger logger;
	protected Object lock;
	protected boolean noStopRequest;
	protected ExecutorService pool;
	protected Thread internalThread;
	protected List<UserData> dataQueue;
	
	protected static DataService service;
	
	protected static final long WAIT_TIME_OUT = 5000; // ms
	
	public abstract boolean fire(UserData userData);
	
	public abstract boolean fire(List<UserData> list);
	
	public abstract void stop();
	
	protected abstract void runWork();
}
