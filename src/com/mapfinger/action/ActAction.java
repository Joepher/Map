package com.mapfinger.action;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import com.mapfinger.entity.location.Coord;
import com.mapfinger.executor.convert.ConvertToJsonDataExecutor;

public class ActAction {
	private JSONArray positionArray, pathArray, curArray;
	
	public JSONArray getPositionArray() {
		return positionArray;
	}
	
	public void setPositionArray(JSONArray positionArray) {
		this.positionArray = positionArray;
	}
	
	public JSONArray getPathArray() {
		return pathArray;
	}
	
	public void setPathArray(JSONArray pathArray) {
		this.pathArray = pathArray;
	}
	
	public JSONArray getCurArray() {
		return curArray;
	}
	
	public void setCurArray(JSONArray curArray) {
		this.curArray = curArray;
	}
	
	public String execute() {
		try {
			List<Coord> positions = getPositions(), paths = getPaths(), cur = getCurPos();
			
			setPositionArray(ConvertToJsonDataExecutor.execute(positions));
			setPathArray(ConvertToJsonDataExecutor.execute(paths));
			setCurArray(ConvertToJsonDataExecutor.execute(cur));
			
			return "ok";
		} catch (Exception e) {
			return "error";
		}
	}
	
	private List<Coord> getPositions() throws Exception {
		List<Coord> positions = new ArrayList<Coord>();
		
		positions.add(new Coord("113.407745", "23.058560"));
		positions.add(new Coord("113.413247", "23.051645"));
		
		return positions;
	}
	
	private List<Coord> getPaths() throws Exception {
		List<Coord> paths = new ArrayList<Coord>();
		
		paths.add(new Coord("113.414777", "23.053549"));
		
		return paths;
	}
	
	private List<Coord> getCurPos() {
		List<Coord> curpos = new ArrayList<Coord>();
		
		curpos.add(new Coord("113.414777", "23.053549"));
		
		return curpos;
	}
}
