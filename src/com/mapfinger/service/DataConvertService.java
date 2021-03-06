package com.mapfinger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.FileLogger;
import com.mapfinger.task.DataConvertTask;

public class DataConvertService extends DataService {
	private DataConvertService() {
		this.logger = FileLogger.getLogger();
		
		logger.info("Initialization DataConvertService");
		
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
		
		this.internalThread = new Thread(target, "DataConvertService-internalThread");
		this.internalThread.start();
	}
	
	public static DataService getInstance() {
		if (service == null) {
			service = new DataConvertService();
		}
		
		return service;
	}
	
	@Override
	public boolean fire(UserData userData) {
		logger.info("Handle new data: " + userData.toString());
		
		synchronized (lock) {
			dataQueue.add(userData);
			lock.notifyAll();
		}
		
		return true;
	}
	
	@Override
	public boolean fire(List<UserData> list) {
		synchronized (lock) {
			dataQueue.addAll(list);
			lock.notifyAll();
		}
		
		return true;
	}
	
	@Override
	public void stop() {
		this.noStopRequest = false;
	}
	
	@Override
	protected void runWork() {
		logger.info("Start inner thread");
		
		boolean suspend = false;
		
		while (noStopRequest) {
			if (!suspend) {
				logger.info("On running...");
			}
			
			UserData userData = null;
			
			synchronized (lock) {
				if (dataQueue.size() > 0) {
					userData = dataQueue.remove(0);
				} else {
					try {
						lock.wait(WAIT_TIME_OUT);
						
						if (!suspend) {
							logger.info("No data need to handle");
							suspend = true;
						}
					} catch (InterruptedException e) {
						logger.warn("DataConvertService interrupted while waiting for lock");
					}
				}
			}
			
			if (userData != null) {
				suspend = false;
				logger.info("Convert to bd09ll: " + userData.toString());
				pool.execute(new DataConvertTask(userData));
			}
		}
	}
	
}
