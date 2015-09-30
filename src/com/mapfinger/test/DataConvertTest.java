package com.mapfinger.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.mapfinger.entity.UserData;
import com.mapfinger.io.FileOperator;
import com.mapfinger.service.DataConvertService;
import com.mapfinger.service.DataService;

/**
 * @description 该测试用例用于测试DataConvertService
 * @author Administrator
 */
public class DataConvertTest {
	public static void main(String[] args) {
		testCase();
		testSuit();
	}
	
	private static void testCase() {
		DataService service = DataConvertService.getInstance();
		service.fire(new UserData("000", "20081023025304.plt"));
	}
	
	private static void testSuit() {
		String username = "000";
		String[] files = getFiles(username);
		List<UserData> list = new ArrayList<UserData>();
		
		System.out.println("total " + files.length + " files");
		
		for (int i = 0; i < files.length; i++) {
			list.add(new UserData(username, files[i]));
		}
		
		DataService service = DataConvertService.getInstance();
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
