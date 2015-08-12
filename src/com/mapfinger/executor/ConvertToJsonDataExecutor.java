/**
 * @author Administrator
 * @date 2015-6-8
 * @name JsonDataParseExecutor
 * @version 1.0
 * @description For the convience of transporting, all coors should be converted to json array.The converted array will
 *              send to the front-end(JSP page) and be shown on map after reconverting to coords.
 */
package com.mapfinger.executor;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mapfinger.entity.Coord;
import com.mapfinger.log.ConsoleLogger;

public class ConvertToJsonDataExecutor {
	
	public static JSONArray execute(List<Coord> coords) {
		if (coords == null || coords.size() == 0) {
			ConsoleLogger.log("no data to show");
			return null;
		}
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < coords.size(); i++) {
			array.put(new JSONObject(coords.get(i)));
		}
		
		ConsoleLogger.log("combinate " + coords.size() + " point to jsonarray");
		return array;
	}
	
}
