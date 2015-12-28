package com.mapfinger.executor.keypoint;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.entity.location.Location;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.executor.data.LocationDataExtractExecutor;
import com.mapfinger.io.FileOperator;
import com.mapfinger.io.MapfingerConfiguration;

public class KeyPointDetectOldExecutor extends BaseExecutor {
	
	private String convDataPath, keyDataPath;
	private Thread keyThread;
	private boolean detectFinished;
	private List<Location> locations;
	private MapfingerConfiguration configuration;
	
	public KeyPointDetectOldExecutor(UserData userData) {
		super(userData);
		String home = FileOperator.getUserHome(userData.getUserName());
		this.convDataPath = home + configuration.getConvDataPath() + userData.getFileName();
		this.keyDataPath = home + configuration.getOldKeyDataPath() + userData.getFileName();
		this.detectFinished = false;
		this.locations = null;
		
		Runnable target = new Runnable() {
			@Override
			public void run() {
				runwork();
			}
		};
		this.keyThread = new Thread(target, "KeyPointDetectOldExecutor-keyThread");
	}
	
	@Override
	public boolean execute() {
		this.locations = new LocationDataExtractExecutor(null).extractConvLocationData(convDataPath);
		this.keyThread.start();
		
		while (!detectFinished) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected void runwork() {
		if (persistance(detectKeyPoint(locations), keyDataPath)) {
			logger.info("Succeed in saving key points to " + keyDataPath);
		}
		
		detectFinished = true;
	}
	
	private List<KeyPoint> detectKeyPoint(List<Location> locations) {
		if (locations == null) {
			return null;
		} else {
			List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
			int pointNum = locations.size(), i = 0, j = 0;
			Location la, lb;
			
			while (i < pointNum) {
				j = ++i;
				while (j < pointNum) {
					la = locations.get(i);
					lb = locations.get(j);
					if (configuration.getDistThreshold() < getDistance(la, lb)) {
						if (configuration.getTimeThreshold() < getTimeDiff(la, lb)) {
							keyPoints.add(computeMeanCoord(locations, i, j));
						}
						i = j;
						break;
					}
					j += 1;
				}
			}
			logger.info("Detected " + keyPoints.size() + " key points");
			
			return keyPoints;
		}
	}
	
	private boolean persistance(List<KeyPoint> keyPoints, String filePath) {
		if (keyPoints == null) {
			return false;
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
			} catch (Exception e) {
				logger.error("Failed in saving " + filePath + ". ErrRef:" + e.getMessage());
				return false;
			}
			
			return true;
		}
	}
	
	private double getDistance(Location la, Location lb) {
		double[] lat = { Double.parseDouble(la.getLatitude()), Double.parseDouble(lb.getLatitude()) };
		double[] lon = { Double.parseDouble(la.getLongitude()), Double.parseDouble(lb.getLongitude()) };
		
		double delta = Math.sin(lat[0]) * Math.sin(lat[1]) + Math.cos(lat[0]) * Math.cos(lat[1])
				* Math.cos(lon[0] - lon[1]);
		double dist = Math.PI * configuration.getR() * Math.acos(delta) * 1000 / 180;
		
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
	
}
