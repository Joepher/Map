package com.mapfinger.action;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.executor.convert.ConvertToJsonDataExecutor;

public class MapShowAction {
	private String longitude = "0";
	private String latitude = "0";
	private JSONArray array;
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public JSONArray getArray() {
		return array;
	}
	
	public void setArray(JSONArray array) {
		this.array = array;
	}
	
	public String execute() {
		List<Coord> coords = new ArrayList<Coord>();
		if (!"0".equals(longitude) && !"0".equals(latitude))
			coords.add(new Coord(longitude, latitude));
		setArray(ConvertToJsonDataExecutor.execute(coords));
		
		return "success";
	}
}
