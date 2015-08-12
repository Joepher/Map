package com.mapfinger.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @description debug < info < warn < error < fatal
 * @author Administrator
 */
public class FileLogger {
	private static Logger logger;
	
	public static Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getLogger("com.logging.log");
		}
		
		return logger;
	}
}
