/**
 * @author Administrator
 * @date 2015-6-8
 * @name LocationExtractExecutor
 * @version 1.0
 * @description Extract locations from file in which points are converted to bd09ll locations.
 */
package com.mapfinger.executor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.mapfinger.entity.Coord;
import com.mapfinger.entity.KeyPoint;
import com.mapfinger.entity.Location;
import com.mapfinger.io.FileOperator;
import com.mapfinger.log.ConsoleLog;

public class LocationDataExtractExecutor {
	public static List<Location> extractOrigLocationData(String origDataPath) {
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
						
						locations.add(new Location(timeLine, longitude, latitude, ""));
					}
				}
				
				ConsoleLog.log("Extract " + locations.size() + " points from " + origDataPath);
			} catch (Exception e) {
				locations = null;
				ConsoleLog.log("Failed to extract points from " + origDataPath + ".Err: "
						+ e.getMessage());
			}
		} else {
			locations = null;
			ConsoleLog.log("Failed to extract points from " + origDataPath + ".Err: File not exist");
		}
		
		return locations;
	}
	
	public static List<Location> extractConvLocationData(String convDataPath) {
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
				
				ConsoleLog.log("Extract " + locations.size() + " points from " + convDataPath);
			} catch (Exception e) {
				locations = null;
				ConsoleLog.log("Failed to extract points from " + convDataPath + ".Err: "
						+ e.getMessage());
			}
		} else {
			locations = null;
			ConsoleLog.log("Failed to extract points from " + convDataPath + ".Err: File not exist");
		}
		
		return locations;
	}
	
	public static List<KeyPoint> extractKeyPoint(String dataPath) {
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
				
				ConsoleLog.log("Extract " + keyPoints.size() + " keyPoints from " + dataPath);
			} catch (Exception e) {
				keyPoints = null;
				ConsoleLog.log("Failed to extract keyPoints from " + dataPath + ".Err: "
						+ e.getMessage());
			}
		} else {
			keyPoints = null;
			ConsoleLog.log("Failed to extract keyPoints from " + dataPath + ".Err: File not exist");
		}
		
		return keyPoints;
	}
	
	public static List<Coord> extractKeyPointsCoord(String username) {
		List<Coord> coords = new ArrayList<Coord>();
		// TODO 加载驻留点并提取经纬信息 [nr]
		
		return coords;
	}
}
