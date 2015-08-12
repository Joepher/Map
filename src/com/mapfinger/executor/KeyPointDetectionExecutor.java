/**
 * @author Administrator
 * @date 2015-6-8
 * @name KeyPointDetectionExecutor
 * @version 1.0
 * @description The core function is detecting keypoints from the oroginal locations.
 *              In keyPointDetection, algorithm that proposed by MSRA is used to detect key points. Theory
 *              that a
 *              geographic region where a user stayed over a certain time interval is considered in this
 *              algorithm.
 *              Certain amount of points will be regarded as key points by this theory. Thus may cause
 *              stituations as
 *              followed:
 *              key point that consist only one location will be expanded by server amount of locations; key
 *              point that
 *              consist amounts of locations will be partition to two key points.
 *              To solve the problem caused by that algorithm, an enhanced algorithm is proposed. Key points
 *              consist of
 *              Stay points and Interest points. Two rules are used to detect those points.
 */
package com.mapfinger.executor;

import java.io.*;
import java.text.*;
import java.util.*;
import com.mapfinger.entity.KeyPoint;
import com.mapfinger.entity.Location;
import com.mapfinger.entity.UserData;
import com.mapfinger.io.FileOperator;

public class KeyPointDetectionExecutor extends BaseExecutor {
	private String convDataPath;
	private String keyDataPath;
	private String keyenhDataPath;
	private Thread keyThread;
	private Thread enhKeyThread;
	private boolean keyFinish;
	private boolean enhkeyFinish;
	private List<Location> locations;
	
	private static double distThreshold = 100; // meter
	private static double timeThreshold = 60; // second
	private static double slopeRotateThreshold = 1.0;
	private static final double R = 6378.137; // kilometer
	
	public KeyPointDetectionExecutor(UserData userData) {
		super(userData);
		
		String home = FileOperator.getUserHome(userData.getUserName());
		
		this.convDataPath = home + "bd09ll/" + userData.getFileName();
		this.keyDataPath = home + "key/" + userData.getFileName();
		this.keyenhDataPath = home + "keyenh/" + userData.getFileName();
		this.keyFinish = false;
		this.enhkeyFinish = false;
		this.locations = null;
		
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
		
		this.keyThread = new Thread(keyTarget, "KeyPointDetectionExecutor-keyThread");
		this.enhKeyThread = new Thread(enhKeyTarget, "KeyPointDetectionExecutor-enhKeyThread");
	}
	
	@Override
	public boolean execute() {
		this.locations = new LocationDataExtractExecutor(null).extractConvLocationData(convDataPath);
		this.keyThread.start();
		this.enhKeyThread.start();
		
		while (!keyFinish || !enhkeyFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	private void runKeyWork() {
		List<KeyPoint> keyPoints = keyPointDetection(locations);
		
		if (persistance(keyPoints, keyDataPath)) {
			logger.info("Successed to persistance key points to " + keyDataPath);
		}
		
		keyFinish = true;
	}
	
	private void runEnhKeyWork() {
		List<KeyPoint> enhKeyPoints = enhancedKeyPointDetection(locations);
		
		if (persistance(enhKeyPoints, keyenhDataPath)) {
			logger.info("Successed to persistance enhanced key points to " + keyenhDataPath);
		}
		
		enhkeyFinish = true;
	}
	
	private List<KeyPoint> keyPointDetection(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		
		if (locations == null) {
			keyPoints = null;
		} else {
			int pointNum = locations.size(), i = 0, j = 0;
			double dist = 0.0, timeDiff = 0.0;
			Location la, lb;
			
			while (i < pointNum) {
				j = ++i;
				while (j < pointNum) {
					la = locations.get(i);
					lb = locations.get(j);
					
					dist = getDistance(la, lb);
					if (dist > distThreshold) {
						timeDiff = getTimeDiff(la, lb);
						if (timeDiff > timeThreshold) {
							keyPoints.add(computeMeanCoord(locations, i, j));
						}
						i = j;
						break;
					}
					j += 1;
				}
			}
			
			logger.info("Detected " + keyPoints.size() + " key points");
		}
		
		return keyPoints;
	}
	
	private List<KeyPoint> enhancedKeyPointDetection(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		
		if (locations == null) {
			keyPoints = null;
		} else {
			List<KeyPoint> stayPoints = stayPointDetectionRule(locations);
			List<KeyPoint> interestPoints = interestPointDetectionRule(locations);
			
			keyPoints.addAll(stayPoints);
			keyPoints.addAll(interestPoints);
			
			logger.info("Detected " + keyPoints.size() + " key points (enhanced)");
		}
		
		return keyPoints;
	}
	
	private boolean persistance(List<KeyPoint> keyPoints, String filePath) {
		boolean response;
		
		if (keyPoints == null) {
			response = false;
		} else {
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath));
				
				for (int i = 0; i < keyPoints.size(); i++) {
					KeyPoint keyPoint = keyPoints.get(i);
					
					writer.write(keyPoint.getLatitude() + "," + keyPoint.getLongitude() + ","
							+ keyPoint.getArriveTime() + "," + keyPoint.getLeaveTime() + ","
							+ keyPoint.getAddress() + "\r");
					writer.flush();
				}
				writer.close();
				
				response = true;
			} catch (Exception e) {
				response = false;
				logger.error("Failed to persistance " + filePath + ".Err:" + e.getMessage());
			}
			response = true;
		}
		
		return response;
	}
	
	private List<KeyPoint> stayPointDetectionRule(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		int pointNum = locations.size(), i = 0;
		double timeDiff = 0.0;
		Location preloc, curloc;
		
		preloc = locations.get(i);
		
		while (i < pointNum) {
			curloc = locations.get(i);
			
			timeDiff = getTimeDiff(preloc, curloc);
			if (timeDiff >= timeThreshold) {
				keyPoints.add(new KeyPoint(preloc.getLongitude(), preloc.getLatitude(), preloc.getTimeline(),
						curloc.getTimeline(), preloc.getAddress()));
			}
			
			preloc = curloc;
			++i;
		}
		
		return keyPoints;
	}
	
	private List<KeyPoint> interestPointDetectionRule(List<Location> locations) {
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		List<Location> interestPoints = new ArrayList<Location>();
		int pointNum = locations.size(), i = 0, posA = 0, posB = 0, posC = 0, posD = 0;
		double slopeA2B = 0.0, slopeB2C = 0.0, slopeC2D = 0.0, rotateAB2BC = 0.0, rotateBC2CD = 0.0;
		
		while (i < pointNum) {
			if ((posD - posA) == 3) {
				Location locA = locations.get(posA);
				Location locB = locations.get(posB);
				Location locC = locations.get(posC);
				Location locD = locations.get(posD);
				
				slopeA2B = getSlope(locA, locB);
				slopeB2C = getSlope(locB, locC);
				
				rotateAB2BC = getSlopeRotate(slopeA2B, slopeB2C);
				
				if (Math.abs(rotateAB2BC) >= slopeRotateThreshold) {
					interestPoints.add(locA);
					interestPoints.add(locB);
					
					slopeC2D = getSlope(locC, locD);
					rotateBC2CD = getSlopeRotate(slopeB2C, slopeC2D);
					
					if (Math.abs(rotateBC2CD) < slopeRotateThreshold) {
						KeyPoint keyPoint = computeMeanCoord(interestPoints);
						if (keyPoint != null) {
							keyPoints.add(keyPoint);
						}
						interestPoints = new ArrayList<Location>();
					}
					
					posA = posC;
					posB = posD;
					posC = posD = ++i;
				} else {
					KeyPoint keyPoint = computeMeanCoord(interestPoints);
					if (keyPoint != null) {
						keyPoints.add(keyPoint);
					}
					interestPoints = new ArrayList<Location>();
					
					posA = posB;
					posB = posC;
					posC = posD;
					posD = ++i;
				}
			} else {
				posA = posB;
				posB = posC;
				posC = posD;
				posD = ++i;
			}
		}
		
		return keyPoints;
	}
	
	private double getDistance(Location la, Location lb) {
		double[] lat = { Double.parseDouble(la.getLatitude()), Double.parseDouble(lb.getLatitude()) };
		double[] lon = { Double.parseDouble(la.getLongitude()), Double.parseDouble(lb.getLongitude()) };
		
		double delta = Math.sin(lat[0]) * Math.sin(lat[1]) + Math.cos(lat[0]) * Math.cos(lat[1])
				* Math.cos(lon[0] - lon[1]);
		double dist = Math.PI * R * Math.acos(delta) * 1000 / 180;
		
		return dist;
	}
	
	private double getTimeDiff(Location la, Location lb) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		double diff = 0.0;
		
		try {
			Date[] date = { format.parse(la.getTimeline()), format.parse(lb.getTimeline()) };
			
			diff = Math.abs((date[0].getTime() - date[1].getTime()) / 1000 * 1.0);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return diff;
	}
	
	private KeyPoint computeMeanCoord(List<Location> locations, int beginPos, int endPos) {
		KeyPoint meanCoord = new KeyPoint();
		double latSum = 0.0, lonSum = 0.0;
		
		for (int i = beginPos; i <= endPos; i++) {
			lonSum += Double.parseDouble(locations.get(i).getLongitude());
			latSum += Double.parseDouble(locations.get(i).getLatitude());
		}
		
		meanCoord.setLongitude(lonSum / (endPos - beginPos + 1) + "");
		meanCoord.setLatitude(latSum / (endPos - beginPos + 1) + "");
		meanCoord.setArriveTime(locations.get(beginPos).getTimeline());
		meanCoord.setLeaveTime(locations.get(endPos).getTimeline());
		meanCoord.setAddress("UNHANDLE");
		
		return meanCoord;
	}
	
	private double getSlope(Location preLoc, Location curLoc) {
		double slope = 361;
		
		if (!curLoc.getLongitude().equals(preLoc.getLongitude())) {
			slope = (Double.parseDouble(curLoc.getLatitude()) - Double.parseDouble(preLoc.getLatitude()))
					/ (Double.parseDouble(curLoc.getLongitude()) - Double.parseDouble(preLoc.getLongitude()))
					* 1.0;
		}
		
		return slope;
	}
	
	private double getSlopeRotate(double preSlope, double curSlope) {
		return (curSlope - preSlope) / preSlope * 1.0;
	}
	
	private KeyPoint computeMeanCoord(List<Location> interests) {
		KeyPoint meanCoord = new KeyPoint();
		double latSum = 0.0, lonSum = 0.0;
		
		if (interests.size() >= 4) {
			for (Location location : interests) {
				latSum += Double.parseDouble(location.getLatitude());
				lonSum += Double.parseDouble(location.getLongitude());
			}
			latSum = latSum / interests.size() * 1.0;
			lonSum = lonSum / interests.size() * 1.0;
			
			meanCoord.setLatitude(latSum + "");
			meanCoord.setLongitude(lonSum + "");
			meanCoord.setArriveTime(interests.get(0).getTimeline());
			meanCoord.setLeaveTime(interests.get(interests.size() - 1).getTimeline());
			meanCoord.setAddress("UNHANDLE");
		} else {
			meanCoord = null;
		}
		
		return meanCoord;
	}
}
