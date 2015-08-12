package com.mapfinger.io;

import java.io.File;
import com.mapfinger.log.ConsoleLogger;

public class FileOperator {
	public static String getUserHome(String userName) {
		//String root = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ");
		String root = "resource/";
		String userHome = root + "userData/" + userName + "/";
		//userHome = userHome.replaceFirst("/", "");
		
		return userHome;
	}
	
	public static File getFile(String filePath) {
		ConsoleLogger.log("Get file " + filePath);
		
		File file = new File(filePath);
		
		if (!file.exists()) {
			file = null;
		}
		
		return file;
	}
	
}
