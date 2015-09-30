package com.mapfinger.executor.keypoint;

import java.io.*;
import java.util.*;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.executor.data.LocationDataExtractExecutor;
import com.mapfinger.io.FileOperator;

public class KeyPointClusterExecutor extends BaseExecutor {
	private String keyDataPath;
	private String keyCluPath;
	private String keyenhDataPath;
	private String keyenhCluPath;
	private Thread keyThread;
	private Thread enhKeyThread;
	private List<KeyPoint> keyPoints;
	private List<KeyPoint> enhKeyPoints;
	
	public KeyPointClusterExecutor(UserData userData) {
		super(userData);
		
		String home = FileOperator.getUserHome(userData.getUserName());
		
		this.keyDataPath = home + "key/" + userData.getFileName();
		this.keyCluPath = home + "keyclu/" + userData.getFileName();
		this.keyenhDataPath = home + "keyenh/" + userData.getFileName();
		this.keyenhCluPath = home + "keyenhclu/" + userData.getFileName();
		this.keyPoints = null;
		this.enhKeyPoints = null;
		
		Runnable keyTarget = new Runnable() {
			@Override
			public void run() {
				runKeyWork();
			}
		};
		
		Runnable enhKeyTarget = new Runnable() {
			@Override
			public void run() {
				runEnhKeyWork();
			}
		};
		
		this.keyThread = new Thread(keyTarget, "KeyPointClusterExecutor-keyThread");
		this.enhKeyThread = new Thread(enhKeyTarget, "KeyPointClusterExecutor-enhKeyThread");
	}
	
	@Override
	public boolean execute() {
		this.keyPoints = new LocationDataExtractExecutor(null).extractKeyPoint(keyDataPath);
		this.enhKeyPoints = new LocationDataExtractExecutor(null).extractKeyPoint(keyenhDataPath);
		this.keyThread.start();
		this.enhKeyThread.start();
		
		return true;
	}
	
	private void runKeyWork() {
		List<KeyPoint> points = keyPointCluster(keyPoints);
		
		if (persistance(points, keyCluPath)) {
			logger.info("Successed to persistance key points to " + keyCluPath);
		}
	}
	
	private void runEnhKeyWork() {
		List<KeyPoint> points = keyPointCluster(enhKeyPoints);
		
		if (persistance(points, keyenhCluPath)) {
			logger.info("Successed to persistance key points to " + keyenhCluPath);
		}
	}
	
	private List<KeyPoint> keyPointCluster(List<KeyPoint> points) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		
		// XXX 对已解析出的驻留点进行聚类处理[可能不需要]
		/**
		 * 聚类处理-待完成
		 */
		
		return keyPoints;
	}
	
	private boolean persistance(List<KeyPoint> keyPoints, String filePath) {
		boolean response;
		
		if (keyPoints == null) {
			response = false;
		} else {
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath));
				for (KeyPoint keyPoint : keyPoints) {
					writer.write(keyPoint.getLatitude() + "," + keyPoint.getLongitude() + ","
							+ keyPoint.getArriveTime() + "," + keyPoint.getLeaveTime() + ","
							+ keyPoint.getAddress() + "\r");
					writer.flush();
				}
				writer.close();
				
				response = true;
			} catch (Exception e) {
				response = false;
				logger.error("Failed to persistance " + filePath + ".Err: " + e.getMessage());
			}
			response = true;
		}
		
		return response;
	}
}
