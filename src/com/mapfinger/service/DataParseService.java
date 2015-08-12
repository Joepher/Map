package com.mapfinger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.Logger;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.FileLogger;
import com.mapfinger.task.DataParseTask;

public class DataParseService implements DataService {
	private Object lock;
	private Thread internalThread;
	private List<UserData> dataQueue;
	private ExecutorService pool;
	private boolean noStopRequest;
	private Logger logger;
	
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
		
		logger.info("Handle new data: " + userData.toString());
		
		synchronized (lock) {
			dataQueue.add(userData);
			lock.notifyAll();
		}
		
		response = true;
		
		return response;
	}
	
	@Override
	public boolean fire(List<UserData> list) {
		boolean response = false;
		
		synchronized (lock) {
			dataQueue.addAll(list);
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
		this.logger = FileLogger.getLogger();
		
		logger.info("Initialization DataParseService");
		
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
		
		this.internalThread = new Thread(target, "DataParseService-internalThread");
		this.internalThread.start();
	}
	
	private void runWork() {
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
						logger.warn("DataParseService interupted while waiting for lock");
					}
				}
			}
			
			if (userData != null) {
				suspend = false;
				logger.info("Extract key points: " + userData.toString());
				pool.execute(new DataParseTask(userData));
			}
		}
	}
}
