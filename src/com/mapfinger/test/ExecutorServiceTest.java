package com.mapfinger.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class LogOut {
	public static void out(String msg) {
		System.out.println("[" + System.currentTimeMillis() + " " + Thread.currentThread().getName()
				+ "] " + msg);
	}
}

class MyTask implements Runnable {
	private static int taskId = 0;
	
	public MyTask() {
		++taskId;
	}
	
	@Override
	public void run() {
		LogOut.out("start task: " + taskId);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LogOut.out("stop task: " + taskId);
	}
}

public class ExecutorServiceTest {
	private Thread internalThread;
	private ExecutorService pool;
	private boolean noStopRequest;
	
	private ExecutorServiceTest() {
		LogOut.out("start executor");
		
		this.pool = Executors.newFixedThreadPool(5);
		this.noStopRequest = true;
		
		Runnable target = new Runnable() {
			@Override
			public void run() {
				execute();
			}
		};
		
		this.internalThread = new Thread(target, "ExecutorServiceInternalThread");
		this.internalThread.start();
	}
	
	private void stop() {
		LogOut.out("stop executor");
		
		this.noStopRequest = false;
	}
	
	private void execute() {
		int id = 0;
		
		while (noStopRequest) {
			LogOut.out("current id: " + (++id));
			
			pool.execute(new MyTask());
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis(), INTERVAL = 1000;
		
		ExecutorServiceTest eTest = new ExecutorServiceTest();
		
		while (true) {
			if ((System.currentTimeMillis() - start) > INTERVAL) {
				eTest.stop();
				break;
			}
		}
		
		LogOut.out("exit main");
	}
	
}
