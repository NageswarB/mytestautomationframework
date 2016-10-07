package com.webmailhostopia.common.utils;

import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author nageswar.bodduri
 * This class is used to capture the events occurs during test run
 */

public class Log {

	private static Logger log = Logger.getLogger(Log.class.getName());
	
	public static void info(String message){
		log.info(message);
		ExtentTestManager.getTest().log(LogStatus.INFO, message);
	}
	
	public static void warn(String message){
		log.warn(message);
	}
	
	public static void error(String message){
		log.error(message);
	}

	public static void debug(String message) {
		log.debug(message);
	}
}