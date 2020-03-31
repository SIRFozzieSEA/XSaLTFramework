package com.codef.xsalt.arch;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFrameworkProperties
{

	public static boolean XB_USE_PORT_IN_URL_FLAG = true;

	/**
	 * The evolving constants for connection to the database server
	 */
	public static String XS_DEFAULT_MYSQL_HOSTNAME = "localhost";
	public static String XS_DEFAULT_MYSQL_PORT = "3306";
	public static String XS_DEFAULT_USERNAME = "root";
	public static String XS_DEFAULT_PASSWORD = "5920Bab";

	/**
	 * The evolving constant for the application URL
	 */
	public static String XS_SERVER_APPLICATION_URL = null;

	public static String XS_SERVER_DOMAIN = null;

	/**
	 * The evolving constant for the default context name
	 */
	public static String XS_DEFAULT_CONTEXT_NAME = "XSaLTWebs";

	/**
	 * The evolving constant for the path to the task files
	 */
	public static String XS_URI_CONTEXT_TO_XSALTTASKS = "com.tma.xsalttasks";

	/**
	 * The evolving constant for which port the application runs off of
	 */
	public static String XS_HTTP_OR_HTTPS_PORT = null;

	/**
	 * The evolving constant for which port number application runs off of
	 */
	public static String XS_HTTP_OR_HTTPS_PORT_NUMBER = null;

	/**
	 * The evolving constant for the path of the default xsl file
	 */
	public static String XS_DEFAULT_XSL_TO_CALL_PREF = "DefaultTask.xsl";

	/**
	 * The evolving constant for the path of the default task file
	 */
	public static String XS_DEFAULT_TASK_TO_CALL_PREF = "DefaultTask";

	/**
	 * The evolving constant for the path of the error xsl file
	 */
	public static String XS_ERROR_XSL_TO_CALL_PREF = "ErrorTask.xsl";

	/**
	 * The evolving constant for the path of the error task file
	 */
	public static String XS_ERROR_TASK_TO_CALL_PREF = "ErrorTask";

	/**
	 * The evolving constant for the maximum number of records in a scrollable resultset
	 */
	public static Long XL_MAXIMUM_RECORDS_RETRIEVED = new Long(0);

	public static String XS_PATH_TO_XSALT_XSLS = null;

	public static String XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY = null;

	/**
	 * The evolving constant flag if this is a test instance.
	 */
	public static boolean XB_IS_TEST_SERVER = true;
	public static boolean XB_IS_SSL_SERVER = true;

	/**
	 * The evolving constant List of available applications.
	 */
	public static ArrayList<String> XO_APPLICATIONS_AVAILABLE = null;

	/**
	 * The evolving constant String of available applications.
	 */
	public static String XS_APPLICATIONS_AVAILABLE = "";

	/**
	 * Area for defining constant values not already initialized.
	 */
	static
	{
		ResourceBundle oResourceBundle = ResourceBundle.getBundle("XSaLTFramework", Locale.ENGLISH);

		XS_SERVER_APPLICATION_URL = oResourceBundle.getString("XS_SERVER_APPLICATION_URL");
		XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY = oResourceBundle.getString("XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY");
		XL_MAXIMUM_RECORDS_RETRIEVED = new Long(oResourceBundle.getString("XL_MAXIMUM_RECORDS_RETRIEVED"));
		XS_APPLICATIONS_AVAILABLE = oResourceBundle.getString("XS_APPLICATIONS_AVAILABLE");

		XO_APPLICATIONS_AVAILABLE = new ArrayList<String>();
		String[] asApplications = XS_APPLICATIONS_AVAILABLE.split(",");
		for (int i = 0; i < asApplications.length; i++)
		{
			XO_APPLICATIONS_AVAILABLE.add(asApplications[i]);
		}

		if (XS_SERVER_APPLICATION_URL.toLowerCase().startsWith("https"))
		{
			XS_HTTP_OR_HTTPS_PORT = "https";
			XB_IS_SSL_SERVER = true;
		}
		else
		{
			XS_HTTP_OR_HTTPS_PORT = "http";
			XB_IS_SSL_SERVER = false;
		}

		if (XS_SERVER_APPLICATION_URL.indexOf(":", 7) == -1)
		{
			if (XS_SERVER_APPLICATION_URL.toLowerCase().startsWith("https"))
			{
				XS_HTTP_OR_HTTPS_PORT_NUMBER = "443";
			}
			else
			{
				XS_HTTP_OR_HTTPS_PORT_NUMBER = "8080";
			}
			XS_SERVER_DOMAIN = XS_SERVER_APPLICATION_URL.split("/")[2];
			XB_USE_PORT_IN_URL_FLAG = false;
		}
		else
		{
			XS_HTTP_OR_HTTPS_PORT_NUMBER = XS_SERVER_APPLICATION_URL.split(":")[2].split("/")[0];
			XS_SERVER_DOMAIN = XS_SERVER_APPLICATION_URL.split("/")[2].split(":")[0];
			XB_USE_PORT_IN_URL_FLAG = true;
		}

		if (XS_SERVER_APPLICATION_URL.indexOf("localhost") == -1)
		{
			XB_IS_TEST_SERVER = false;
			XS_PATH_TO_XSALT_XSLS = XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY + "/webapps/XSaLTWebs";
		}
		else
		{
			XB_IS_TEST_SERVER = true;
			XS_PATH_TO_XSALT_XSLS = "C:/SVN/XSaLTWebs/XSaLTAssets";
		}

		if (oResourceBundle.containsKey("XS_DEFAULT_MYSQL_HOSTNAME"))
		{
			XS_DEFAULT_MYSQL_HOSTNAME = oResourceBundle.getString("XS_DEFAULT_MYSQL_HOSTNAME");
		}

		if (oResourceBundle.containsKey("XS_DEFAULT_USERNAME"))
		{
			XS_DEFAULT_USERNAME = oResourceBundle.getString("XS_DEFAULT_USERNAME");
		}

		if (oResourceBundle.containsKey("XS_DEFAULT_PASSWORD"))
		{
			XS_DEFAULT_PASSWORD = oResourceBundle.getString("XS_DEFAULT_PASSWORD");
		}

		if (oResourceBundle.containsKey("XS_DEFAULT_MYSQL_PORT"))
		{
			XS_DEFAULT_MYSQL_PORT = oResourceBundle.getString("XS_DEFAULT_MYSQL_PORT");
		}

		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "XSaLTFrameworkProperties:");
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XL_MAXIMUM_RECORDS_RETRIEVED = " + XL_MAXIMUM_RECORDS_RETRIEVED);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_CONTEXT_NAME = " + XS_DEFAULT_CONTEXT_NAME);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_MYSQL_HOSTNAME = " + XS_DEFAULT_MYSQL_HOSTNAME);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_MYSQL_PORT = " + XS_DEFAULT_MYSQL_PORT);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_PASSWORD = " + XS_DEFAULT_PASSWORD);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_TASK_TO_CALL_PREF = " + XS_DEFAULT_TASK_TO_CALL_PREF);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_USERNAME = " + XS_DEFAULT_USERNAME);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_DEFAULT_XSL_TO_CALL_PREF = " + XS_DEFAULT_XSL_TO_CALL_PREF);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_ERROR_TASK_TO_CALL_PREF = " + XS_ERROR_TASK_TO_CALL_PREF);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_ERROR_XSL_TO_CALL_PREF = " + XS_ERROR_XSL_TO_CALL_PREF);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_HTTP_OR_HTTPS_PORT = " + XS_HTTP_OR_HTTPS_PORT);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_HTTP_OR_HTTPS_PORT_NUMBER = " + XS_HTTP_OR_HTTPS_PORT_NUMBER);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY = " + XS_PATH_TO_TOMCAT_HOME_FOLDER_PRODUCTION_ONLY);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_PATH_TO_XSALT_XSLS = " + XS_PATH_TO_XSALT_XSLS);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_SERVER_APPLICATION_URL = " + XS_SERVER_APPLICATION_URL);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XS_URI_CONTEXT_TO_XSALTTASKS = " + XS_URI_CONTEXT_TO_XSALTTASKS);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XB_IS_TEST_SERVER = " + XB_IS_TEST_SERVER);
		//				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "\t XB_USE_PORT_IN_URL_FLAG = " + XB_USE_PORT_IN_URL_FLAG);

	}

}
