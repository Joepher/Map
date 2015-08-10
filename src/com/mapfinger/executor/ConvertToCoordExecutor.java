/**
 * @author Administrator
 * @date 2015-6-8
 * @name ConvertToCoordExecutor
 * @version 1.0
 * @description Converting the processed locations to coords. Coord is the only position type that can be shown on map.
 *              Whatever kind of positions(keypoint or location) is, they shall extends Coord.The longitude and latitude
 *              will be extracted.
 */
package com.mapfinger.executor;

import java.util.*;
import com.mapfinger.entity.KeyPoint;
import com.mapfinger.entity.Coord;
import com.mapfinger.entity.Location;
import com.mapfinger.entity.UserData;
import com.mapfinger.log.ConsoleLog;

public class ConvertToCoordExecutor extends BaseExecutor {
	
	public ConvertToCoordExecutor(UserData userData) {
		super(userData);
		// TODO ConvertToCoordExecutor initialization [nr]
	}
	
	@Override
	public boolean execute() {
		// TODO ConvertToCoordExecutor execute method [nr]
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
				ConsoleLog.log("convert error: cannot convert " + obj.getClass().getName());
			}
		} else {
			coords = null;
			ConsoleLog.log("no data to convert");
		}
		
		return coords;
	}
	
	private static List<Coord> convertLocationToCoord(List<?> locations) {
		List<Coord> coords = new ArrayList<Coord>();
		
		for (int i = 0; i < locations.size(); i++) {
			Location location = (Location) locations.get(i);
			
			coords.add(new Coord(location.getLongitude(), location.getLatitude()));
		}
		
		ConsoleLog.log("converted " + coords.size() + " point");
		
		return coords;
	}
	
	private static List<Coord> convertKeyPointToCoord(List<?> keyPoints) {
		List<Coord> coords = new ArrayList<Coord>();
		
		for (int i = 0; i < keyPoints.size(); i++) {
			KeyPoint keyPoint = (KeyPoint) keyPoints.get(i);
			
			coords.add(new Coord(keyPoint.getLongitude(), keyPoint.getLatitude()));
		}
		
		ConsoleLog.log("converted " + coords.size() + " point");
		
		return coords;
	}
}
