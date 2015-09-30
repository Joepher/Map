package com.mapfinger.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.mapfinger.entity.UserData;
import com.mapfinger.io.FileOperator;
import com.mapfinger.service.DataService;
import com.mapfinger.service.SequentialityService;

public class SequentialityServiceTest {
	public static void main(String[] args) {
		testCase();
		testSuite();
	}
	
	private static void testCase() {
		DataService service = SequentialityService.getInstance();
		service.fire(new UserData("000", "20081023025304.plt"));
	}
	
	private static void testSuite() {
		String username = "000";
		String[] files = getFiles(username);
		List<UserData> list = new ArrayList<UserData>();
		for (String file : files) {
			list.add(new UserData(username, file));
		}
		
		DataService service = SequentialityService.getInstance();
		service.fire(list);
	}
	
	private static String[] getFiles(String username) {
		String[] files;
		
		String path = FileOperator.getUserHome(username) + "wgs84/";
		File file = new File(path);
		files = file.list();
		
		return files;
	}
	
}
