package com.mapfinger.action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.executor.convert.ConvertToJsonDataExecutor;

public class DataListAction {
	private String key;
	private JSONArray array;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public JSONArray getArray() {
		return array;
	}
	
	public void setArray(JSONArray array) {
		this.array = array;
	}
	
	public String execute() {
		try {
			List<Coord> coords = new ArrayList<Coord>();
			String read = "";
			String filepath = getFilePath();
			
			if (filepath == null) {
				throw new Exception("key miss");
			}
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(filepath)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			while ((read = reader.readLine()) != null) {
				String[] cont = read.split(",");
				
				if (cont.length == 3) {
					coords.add(new Coord(cont[1], cont[2]));
				} else {
					break;
				}
			}
			
			reader.close();
			
			setArray(ConvertToJsonDataExecutor.execute(coords));
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return "success";
	}
	
	private String getFilePath() {
		String home = "D:/Data/", filepath = "";
		
		if ("bd".equals(key)) {
			filepath = home + "bd09ll.txt";
		} else if ("sp".equals(key)) {
			filepath = home + "sp.txt";
		} else if ("spc".equals(key)) {
			filepath = home + "sp_cluster.txt";
		} else {
			filepath = null;
		}
		
		return filepath;
	}
}
