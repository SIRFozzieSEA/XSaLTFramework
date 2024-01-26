package com.codef.xsalt.arch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XSaLTLoggerWrapper {

	public static void main(String[] args) {
		info(XSaLTLoggerWrapper.class.getName(), "This is the main class");
	}

	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static void error(String className, String message, Exception e) {
		showLog("ERROR", className, message, e);
	}

	public static void error(String className, String message) {
		showLog("ERROR", className, message, null);
	}

	public static void info(String className, String message) {
		showLog("INFO", className, message, null);
	}

	public static void warn(String className, String message) {
		showLog("WARN", className, message, null);
	}

	public static void fatal(String className, String message) {
		showLog("FATAL", className, message, null);
	}

	private static void showLog(String level, String className, String message, Exception e) {
		System.out.println(LocalDateTime.now().format(formatter) + ":" + level + ":" + className + ":" + message
				+ (e == null ? "" : ":" + e.toString()));
	}

}
