package com.codef.xsalt.arch;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTConstants
{

	/**
	 * The static String name for the client table
	 */
	public static final String XS_APP_TABLE_CLIENT = "xapp_valid_clients";

	/**
	 * The static String XSaLT version
	 */
	public static final String XS_XSALT_FRAMEWORK_VERSION = "4.0";

	/**
	 * The static constant for the date format MMddyyyy_HHmmss
	 */
	public static final String XS_DATETIME_FORMATTER_NO_SLASHES = "MMddyyyy_HHmmss";

	/**
	 * The static constant for the date format MMddyyyyHHmmssS
	 */
	public static final String XS_DATETIME_FORMATTER_NO_SLASHES_NO_UNDERSCORE = "MMddyyyyHHmmssS";
	
	/**
	 * The static constant for the date format MMddyyyyHHmmss
	 */
	public static final String XS_DATETIME_FORMATTER_NO_SLASHES_NO_UNDERSCORE_WITH_MILLI = "MMddyyyyHHmmssSS";

	/**
	 * The static constant for the date format MMddyyyy
	 */
	public static final String XS_DATE_FORMATTER_NO_SLASHES = "MMddyyyy";

	/**
	 * The static constant for the date format yyyyMMdd
	 */
	public static final String XS_DATE_FORMATTER_NO_SLASHES_YMD = "yyyyMMdd";

	/**
	 * The static constant for the date format MMddyy
	 */
	public static final String XS_DATE_FORMATTER_NO_SLASHES_SHORT = "MMddyy";

	/**
	 * The static constant for the date format MM/dd/yyyy
	 */
	public static final String XS_DATE_FORMATTER_WITH_SLASHES = "MM/dd/yyyy";

	/**
	 * The static constant for the date format MM/dd/yyyy HH:mm:ss
	 */
	public static final String XS_DATETIME_FORMATTER_WITH_SLASHES = "MM/dd/yyyy HH:mm:ss";

	/**
	 * The static constant for the compressed file extension
	 */
	public static final String XS_EXTENSION_FOR_COMPRESSED_FILES = "zip";

	/**
	 * The static constant for the encrypted files
	 */
	public static final String XS_EXTENSION_FOR_ENCRYPTED_FILES = "enc";

	/**
	 * The static constant for the key files
	 */
	public static final String XS_EXTENSION_FOR_KEY_FILES = "key";

	/**
	 * Non-evolving constant for the current smart task
	 */
	public static final String XS_SESSION_CURRENT_SMART_TASK = "XS_SESSION_CURRENT_SMART_TASK";

	/**
	 * Non-evolving constant for the last smart task
	 */
	public static final String XS_SESSION_LAST_SMART_TASK = "XS_SESSION_LAST_SMART_TASK";

	/**
	 * Non-evolving constant for the last/current recordset page
	 */
	public static final String XS_SESSION_LAST_RECORDSET_PAGE = "XS_SESSION_LAST_RECORDSET_PAGE";

	/**
	 * Non-evolving prefix constant for the scrollset parameter
	 */
	public static final String XS_SCROLLSET_PARAM = "XS_SCROLLSET_PARAM_";

	/**
	 * Non-evolving constant for the scrollset viewport
	 */
	public static final String XS_SCROLLSET_VIEWPORT = "XS_SCROLLSET_VIEWPORT";

	/**
	 * Non-evolving constant for the default recordset size
	 */
	public static final String XS_SESSION_DISPLAY_RECORDSET_SIZE = "XS_SESSION_DISPLAY_RECORDSET_SIZE";

	/**
	 * Non-evolving constant for the privs arraylist
	 */
	public static final String XS_SESSION_PRIVS_ARRAYLIST = "XS_SESSION_PRIVS_ARRAYLIST";

	/**
	 * Non-evolving constant for the sequence hashmap
	 */
	public static final String XS_SESSION_SEQUENCE_GROUP_SEQUENCE_HASHMAP = "XS_SESSION_SEQUENCE_GROUP_SEQUENCE_HASHMAP";

	/**
	 * Non-evolving constant for the authentication object
	 */
	public static final String XS_SESSION_AUTHENTICATION_OBJECT = "XS_SESSION_AUTHENTICATION_OBJECT";

	/**
	 * Non-evolving constant for the purchase hashmap
	 */
	public static final String XS_SESSION_PURCHASE_HASHMAP = "XS_SESSION_PURCHASE_HASHMAP";

	/**
	 * Non-evolving constant for the last transaction id
	 */
	public static final String XS_SESSION_LAST_TRANSACTIONID = "XS_SESSION_LAST_TRANSACTIONID";

	/**
	 * Non-evolving constant for the last "sort by" criteria
	 */
	public static final String XS_SESSION_LAST_SORT_BY = "XS_SESSION_LAST_SORT_BY";

	/**
	 * Non-evolving constant for the last "search" field name/type
	 */
	public static final String XS_SESSION_LAST_SEARCH_NAME = "XS_SESSION_LAST_SEARCH_NAME";

	/**
	 * Non-evolving constant for the last "search" criteria
	 */
	public static final String XS_SESSION_LAST_SEARCH_CRITERIA = "XS_SESSION_LAST_SEARCH_CRITERIA";

	/**
	 * Non-evolving constant for the last "search" status (purchase, not purchased, pending, etc.)
	 */
	public static final String XS_SESSION_LAST_SEARCH_STATUS = "XS_SESSION_LAST_SEARCH_STATUS";

	/**
	 * Non-evolving constant for XSL redirection
	 */
	public final static String XS_PARAMETER_FOR_REDIRECT_XSL = "XS_PARAMETER_FOR_REDIRECT_XSL";

	/**
	 * Non-evolving constant for the debug flag
	 */
	public static final String XS_REQUEST_DEBUG_PARAMETER = "DEBUG";
	
	/**
	 * Non-evolving constant for the debug flag
	 */
	public static final String XS_REQUEST_DEBUG_WRITE_PARAMETER = "DEBUG_WRITE";

	/**
	 * The evolving constant for the default language
	 */
	public static String XS_DEFAULT_LANGUAGE = "XS_DEFAULT_LANGUAGE";

	/**
	 * The evolving constant for the default time of day
	 */
	public static String XS_STATIC_TIME = "12:00:59";

}
