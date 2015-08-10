package com.mapfinger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.ConsoleLog;
import com.mapfinger.task.DataAnalyzeTask;

public class DataAnalyzeService implements DataService {
	private Object lock;
	private Thread internalThread;
	private List<UserData> dataQueue;
	private ExecutorService pool;
	private boolean noStopRequest;
	
	private static DataAnalyzeService service;
	
	private static final long WAIT_TIME_OUT = 5000;
	
	public static DataAnalyzeService getInstance() {
		if (service == null) {
			service = new DataAnalyzeService();
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
	
	private DataAnalyzeService() {
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
		
		this.internalThread = new Thread(target, "DataAnalyzeService-InternalThread");
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
						ConsoleLog.log("DataAnalyzeService interupted while waiting for lock");
					}
				}
			}
			
			pool.execute(new DataAnalyzeTask(userData));
		}
	}
}
