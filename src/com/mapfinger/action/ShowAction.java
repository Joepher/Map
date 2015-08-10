package com.mapfinger.action;

import java.util.List;
import org.json.JSONArray;
import com.mapfinger.entity.Coord;
import com.mapfinger.executor.ConvertToJsonDataExecutor;
import com.mapfinger.executor.LocationDataExtractExecutor;

public class ShowAction {
	/* input param */
	private String username;
	private String password;
	/* output param */
	private JSONArray array;
	
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	public ShowAction() {
		this.username = null;
		this.array = null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public JSONArray getArray() {
		return array;
	}
	
	public void setArray(JSONArray array) {
		this.array = array;
	}
	
	public String execute() {
		String result = "";
		
		if (loginValidate()) {
			List<Coord> coords = LocationDataExtractExecutor.extractKeyPointsCoord(username);
			setArray(ConvertToJsonDataExecutor.execute(coords));
			
			result = SUCCESS;
		} else {
			setArray(null);
			
			result = FAIL;
		}
		
		return result;
	}
	
	private boolean loginValidate() {
		boolean result = false;
		// TODO 验证用户名有效性 [nr]
		
		return result;
	}
}
