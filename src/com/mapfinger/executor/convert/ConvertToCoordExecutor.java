/**
 * @author Administrator
 * @date 2015-6-8
 * @name ConvertToCoordExecutor
 * @version 1.0
 * @description Converting the processed locations to coords. Coord is the only position type that can be
 *              shown on map.
 *              Whatever kind of positions(keypoint or location) is, they shall extends Coord.The longitude
 *              and latitude
 *              will be extracted.
 */
package com.mapfinger.executor.convert;

import java.util.*;
import com.mapfinger.entity.UserData;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.entity.location.KeyPoint;
import com.mapfinger.entity.location.Location;
import com.mapfinger.executor.BaseExecutor;
import com.mapfinger.log.ConsoleLogger;

public class ConvertToCoordExecutor extends BaseExecutor {
	
	public ConvertToCoordExecutor(UserData userData) {
		super(userData);
		// TODO ConvertToCoordExecutor initialization [最后显示处理]
	}
	
	@Override
	public boolean execute() {
		// TODO ConvertToCoordExecutor execute method [最后显示处理]
		return false;
	}
	
	public static List<Coord> execute(List<?> objects) {
		List<Coord> coords = new ArrayList<Coord>();
		
		if (objects != null && objects.size() > 0) {
			Object obj = objects.get(0);
			
			if (obj instanceof Location) {
				coords = convertLocationToCoord(objects);
			} else if (obj instanceof KeyPoint) {
				coords = convertKeyPointToCoord(objects);
			} else {
				coords = null;
				ConsoleLogger.log("convert error: cannot convert " + obj.getClass().getName());
			}
		} else {
			coords = null;
			ConsoleLogger.log("no data to convert");
		}
		
		return coords;
	}
	
	private static List<Coord> convertLocationToCoord(List<?> locations) {
		List<Coord> coords = new ArrayList<Coord>();
		
		for (int i = 0; i < locations.size(); i++) {
			Location location = (Location) locations.get(i);
			
			coords.add(new Coord(location.getLongitude(), location.getLatitude()));
		}
		
		ConsoleLogger.log("converted " + coords.size() + " point");
		
		return coords;
	}
	
	private static List<Coord> convertKeyPointToCoord(List<?> keyPoints) {
		List<Coord> coords = new ArrayList<Coord>();
		
		for (int i = 0; i < keyPoints.size(); i++) {
			KeyPoint keyPoint = (KeyPoint) keyPoints.get(i);
			
			coords.add(new Coord(keyPoint.getLongitude(), keyPoint.getLatitude()));
		}
		
		ConsoleLogger.log("converted " + coords.size() + " point");
		
		return coords;
	}
}
