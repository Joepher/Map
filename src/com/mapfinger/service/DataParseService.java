package com.mapfinger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.ConsoleLog;
import com.mapfinger.task.DataParseTask;

public class DataParseService implements DataService {
	private Object lock;
	private Thread internalThread;
	private List<UserData> dataQueue;
	private ExecutorService pool;
	private boolean noStopRequest;
	
	private static DataParseService service;
	
	private static final long WAIT_TIME_OUT = 5000; // ms
	
	public static DataService getInstance() {
		if (service == null) {
			service = new DataParseService();
		}
		
		return service;
	}
	
	@Override
	public boolean fire(UserData userData) {
		boolean response = false;
		
		synchronized (lock) {
			dataQueue.add(userData);
			lock.notifyAll();
		}
		
		response = true;
		
		return response;
	}
	
	@Override
	public void stop() {
		this.noStopRequest = false;
	}
	
	private DataParseService() {
		this.lock = new Object();
		this.noStopRequest = true;
		this.pool = Executors.newFixedThreadPool(5);
		this.dataQueue = new ArrayList<UserData>();
		
		Runnable target = new Runnable() {
			@Override
			public void run() {
				runWork();
			}
		};
		
		this.internalThread = new Thread(target, "DataParseServiceInternalThread");
		this.internalThread.start();
	}
	
	private void runWork() {
		while (noStopRequest) {
			UserData userData = null;
			
			synchronized (lock) {
				if (dataQueue.size() > 0) {
					userData = dataQueue.remove(0);
				} else {
					try {
						lock.wait(WAIT_TIME_OUT);
					} catch (InterruptedException e) {
						ConsoleLog.log("DataParseService interupted while waiting for lock");
					}
				}
			}
			
			pool.execute(new DataParseTask(userData));
		}
	}
}
