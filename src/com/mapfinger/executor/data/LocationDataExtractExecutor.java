/**
 * @author Administrator
 * @date 2015-6-8
 * @name LocationExtractExecutor
 * @version 1.0
 * @description Extract locations from file in which points are converted to bd09ll locations.
 */
package com.mapfinger.executor.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.entity.location.Location;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.io.FileOperator;

public class LocationDataExtractExecutor extends BaseExecutor {
	
	public LocationDataExtractExecutor(UserData userData) {
		super(userData);
	}
	
	@Override
	public boolean execute() {
		// FIXME 重新设计该类
		return false;
	}
	
	public List<Location> extractOrigLocationData(String origDataPath) {
		String read;
		String[] data;
		List<Location> locations = new ArrayList<Location>();
		File file = FileOperator.getFile(origDataPath);
		
		if (file != null) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while ((read = reader.readLine()) != null) {
					data = read.split(",");
					
					if (data.length == 7) {
						String latitude = data[0];
						String longitude = data[1];
						String timeLine = data[5] + " " + data[6];
						
						locations.add(new Location(timeLine, longitude, latitude, "MISS"));
					}
				}
				reader.close();
				
				logger.info("Extract " + locations.size() + " points from " + origDataPath);
			} catch (Exception e) {
				locations = null;
				logger.error("Failed to extract points from " + origDataPath + ".\nErr: " + e.getMessage());
			}
		} else {
			locations = null;
			logger.error("Failed to extract points from " + origDataPath + ". Err: File not exist");
		}
		
		return locations;
	}
	
	public List<Location> extractConvLocationData(String convDataPath) {
		String read;
		String[] data;
		List<Location> locations = new ArrayList<Location>();
		File file = FileOperator.getFile(convDataPath);
		
		if (file != null) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(convDataPath));
				while ((read = reader.readLine()) != null) {
					data = read.split(",");
					
					String timeline = data[0];
					String latitude = data[1];
					String longitude = data[2];
					String address = data[3];
					
					locations.add(new Location(timeline, longitude, latitude, address));
				}
				reader.close();
				
				logger.info("Extract " + locations.size() + " points from " + convDataPath);
			} catch (Exception e) {
				locations = null;
				logger.error("Failed to extract points from " + convDataPath + ".\nErr: " + e.getMessage());
			}
		} else {
			locations = null;
			logger.error("Failed to extract points from " + convDataPath + ". Err: File not exist");
		}
		
		return locations;
	}
	
	public List<KeyPoint> extractKeyPoint(String dataPath) {
		String read;
		String[] data;
		List<KeyPoint> keyPoints = new ArrayList<KeyPoint>();
		File file = FileOperator.getFile(dataPath);
		
		if (file != null) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(dataPath));
				while ((read = reader.readLine()) != null) {
					data = read.split(",");
					
					String latitude = data[0];
					String longitude = data[1];
					String arriveTime = data[2];
					String leaveTime = data[3];
					String address = data[4];
					
					keyPoints.add(new KeyPoint(longitude, latitude, arriveTime, leaveTime, address));
				}
				reader.close();
				
				logger.info("Extract " + keyPoints.size() + " keyPoints from " + dataPath);
			} catch (Exception e) {
				keyPoints = null;
				logger.error("Failed to extract keyPoints from " + dataPath + ".\n Err: " + e.getMessage());
			}
		} else {
			keyPoints = null;
			logger.error("Failed to extract keyPoints from " + dataPath + ". Err: File not exist");
		}
		
		return keyPoints;
	}
	
	public List<Coord> extractKeyPointsCoord(String username) {
		List<Coord> coords = new ArrayList<Coord>();
		// TODO 加载驻留点并提取经纬信息 [最后显示处理]
		
		return coords;
	}
}
