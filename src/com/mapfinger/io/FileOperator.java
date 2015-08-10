package com.mapfinger.io;

import java.io.File;
import com.mapfinger.log.ConsoleLog;

public class FileOperator {
	public static String getUserHome(String userName) {
		String root = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace(
				"%20", " ");
		String userHome = root + "/userData/" + userName + "/";
		
		return userHome;
	}
	
	public static File getFile(String filePath) {
		ConsoleLog.log("Get file - " + filePath);
		
		File file = new File(filePath);
		
		if (!file.exists()) {
			file = null;
		}
		
		return file;
	}
	
}
