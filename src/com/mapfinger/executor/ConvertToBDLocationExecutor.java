/**
 * @author Joepher
 * @date 2015-6-8
 * @name ConvertToBDLocationExecutor
 * @version 1.0
 * @description The core function is converting the WGS locations to baidu bd09ll locations.
 *              Given a set of locations, by sending spicified number(default 10) locations(unconverted) to baidu
 *              server, same number(as sended) locations(converted) will received from the server.
 */
package com.mapfinger.executor;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.json.*;
import com.mapfinger.entity.Coord;
import com.mapfinger.entity.Location;
import com.mapfinger.entity.UserData;
import com.mapfinger.io.FileOperator;
import com.mapfinger.log.ConsoleLog;

public class ConvertToBDLocationExecutor extends BaseExecutor {
	private String origDataPath;
	private String convDataPath;
	
	private static final String CONVERT_URL = "http://api.map.baidu.com/geoconv/v1/?";
	private static final String COORDS = "coords=";
	private static final String TYPE = "from=1&to=5";
	private static final String AK = "ak=sRbH11iRIGFPVthjnBGw5uMG";
	private static final String OUTPUT = "output=json";
	
	public ConvertToBDLocationExecutor(UserData userData) {
		super(userData);
		
		String home = FileOperator.getUserHome(userData.getUserName());
		
		this.origDataPath = home + "wgs84/" + userData.getFileName();
		this.convDataPath = home + "b909ll/" + userData.getFileName();
	}
	
	@Override
	public boolean execute() {
		boolean response = false;
		
		List<Location> origLocations = LocationDataExtractExecutor.extractOrigLocationData(origDataPath);
		List<Location> convLocations = convert(origLocations);
		
		if (persistance(convLocations)) {
			ConsoleLog.log("Successed to persistance converted points to " + convDataPath);
			
			response = true;
		}
		
		return response;
	}
	
	private List<Location> convert(List<Location> origLocations) {
		List<Location> convLocations = null;
		
		if (origLocations != null) {
			ConsoleLog.log("Converting " + origLocations.size() + " points to bd09ll");
			
			int pos = 0, diff = 10, total = origLocations.size();
			convLocations = new ArrayList<Location>(total);
			
			while (pos < total) {
				if ((pos + diff) > total) {
					diff = total - pos;
				}
				
				convLocations.addAll(doConvert(origLocations.subList(pos, pos + diff)));
				pos += diff;
			}
		} else {
			ConsoleLog.log("No points need to be converted to bd09ll");
		}
		
		return convLocations;
	}
	
	private boolean persistance(List<Location> convLocations) {
		boolean response;
		
		if (convLocations != null) {
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(convDataPath));
				
				for (int i = 0; i < convLocations.size(); i++) {
					Location location = convLocations.get(i);
					writer.write(location.getTimeline() + "," + location.getLatitude() + ","
							+ location.getLongitude() + "," + location.getAddress() + "\r");
					writer.flush();
				}
				writer.close();
				
				response = true;
			} catch (Exception e) {
				response = false;
				ConsoleLog.log("Failed to persistance " + convDataPath + ".Err:" + e.getMessage());
			}
		} else {
			response = false;
		}
		
		return response;
	}
	
	private List<Location> doConvert(List<Location> list) {
		String response = send(getAckUrl(list));
		List<Coord> coords = convertToList(response);
		
		for (int i = 0; i < coords.size(); i++) {
			list.get(i).setLongitude(coords.get(i).getLongitude());
			list.get(i).setLatitude(coords.get(i).getLatitude());
		}
		
		return list;
	}
	
	private String getAckUrl(List<Location> list) {
		String coords = COORDS;
		
		for (int i = 0; i < list.size(); i++) {
			coords += list.get(i).getLongitude() + "," + list.get(i).getLatitude();
			if (i != list.size() - 1) {
				coords += ";";
			}
		}
		
		String ackUrl = CONVERT_URL + coords + "&" + TYPE + "&" + AK + "&" + OUTPUT;
		
		return ackUrl;
	}
	
	private String send(String ackUrl) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int read = -1;
		
		try {
			URL url = new URL(ackUrl);
			InputStream in = url.openStream();
			
			while ((read = in.read()) != -1) {
				out.write(read);
			}
		} catch (Exception e) {
			ConsoleLog.log("An exception occured while sending coords to baidu. Err:" + e.getMessage());
		}
		
		return out.toString();
	}
	
	private List<Coord> convertToList(String response) {
		List<Coord> coords = new ArrayList<Coord>();
		
		JSONObject object = new JSONObject(response);
		JSONArray array = object.getJSONArray("result");
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject locObj = new JSONObject(array.get(i).toString());
			
			coords.add(new Coord(locObj.get("x").toString(), locObj.get("y").toString()));
		}
		
		return coords;
	}
}
