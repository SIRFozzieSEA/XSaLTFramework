package com.codef.xsalt.arch;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTGenericLogger
{
	private static Logger XO_LOGGER;
	static
	{
		@SuppressWarnings("rawtypes")
		Enumeration appenders = Logger.getRootLogger().getAllAppenders();
		if (!appenders.hasMoreElements())
		{
			DOMConfigurator.configure("C:/SVN/XSaLTWebs/XSaLTAssets/WEB-INF/classes/xmllog4jconfig.xml");
		}
		XO_LOGGER = Logger.getRootLogger();
	}

	/**
	 * This method writes to the log using the specified level and text.
	 * 
	 * @param _nPriority
	 *            Level to log at
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of calling class.
	 * @param _oThrowable
	 *            Throwable object to show stracktrace for
	 */
	public static void logXSaLT(int _nPriority, String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (_nPriority == Priority.DEBUG_INT)
		{
			debug(_sLogText, _sCallingClassName, _oThrowable);
		}
		else if (_nPriority == Priority.INFO_INT)
		{
			info(_sLogText, _sCallingClassName, _oThrowable);
		}
		else if (_nPriority == Priority.WARN_INT)
		{
			warn(_sLogText, _sCallingClassName, _oThrowable);
		}
		else if (_nPriority == Priority.ERROR_INT)
		{
			error(_sLogText, _sCallingClassName, _oThrowable);
		}
		else if (_nPriority == Priority.FATAL_INT)
		{
			fatal(_sLogText, _sCallingClassName, _oThrowable);
		}
	}

	/**
	 * This method writes the passed text to SystemOut
	 * !!! DO NOT USE IN PRODUCTION !!!
	 * @param _sLogText
	 *            Text to write
	 * @return String of information printed to SystemOut
	 */
	public static String logXSaLTLogTextOnly(String _sLogText)
	{
		System.out.println("\t" + _sLogText);
		return ("\t" + _sLogText);
	}

	/**
	 * This method writes to the log using the specified level and text.
	 * 
	 * @param _nPriority
	 *            Level to log at
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stracktrace for
	 * @return String of information printed to log
	 */
	public static void logXSaLT(int _nPriority, String _sLogText, Exception _oException)
	{
		logXSaLT(_nPriority, _sLogText, "", _oException);
	}

	/**
	 * This method writes to the log using the specified level and text.
	 * 
	 * @param _nPriority
	 *            Level to log at
	 * @param _sLogText
	 *            Text to log
	 */
	public static void logXSaLT(int _nPriority, String _sLogText)
	{
		logXSaLT(_nPriority, _sLogText, "", null);
	}

	/**
	 * This method writes to the log using the specified level and text.
	 * 
	 * @param _nPriority
	 *            Level to log at
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of calling class.
	 */
	public static void logXSaLT(int _nPriority, String _sLogText, String _sCallingClassName)
	{
		logXSaLT(_nPriority, _sLogText, _sCallingClassName, null);
	}

	/**
	 * This method writes to the log using the specified level and text.
	 * 
	 * @param _nPriority
	 *            Level to log at
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stracktrace for
	 */
	public static void logXSaLT(int _nPriority, String _sLogText, Throwable _oThrowable)
	{
		logXSaLT(_nPriority, _sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "DEBUG" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 */
	public static void debug(String _sLogText)
	{
		debug(_sLogText, "", null);
	}

	/**
	 * This method logs at the "DEBUG" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 */
	public static void debug(String _sLogText, String _sCallingClassName)
	{
		debug(_sLogText, _sCallingClassName, null);
	}

	/**
	 * This method logs at the "DEBUG" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void debug(String _sLogText, Throwable _oThrowable)
	{
		debug(_sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "DEBUG" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void debug(String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (!_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.debug("(" + _sCallingClassName + ") " + _sLogText);
		}
		else if (!_sCallingClassName.trim().equals("") && _oThrowable != null)
		{
			XO_LOGGER.debug("(" + _sCallingClassName + ") " + _sLogText, _oThrowable);
		}
		else if (_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.debug(_sLogText);
		}
		else
		{
			XO_LOGGER.debug(_sLogText, _oThrowable);
		}
	}

	/**
	 * This method logs at the "INFO" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 */
	public static void info(String _sLogText)
	{
		info(_sLogText, "", null);
	}

	/**
	 * This method logs at the "INFO" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 */
	public static void info(String _sLogText, String _sCallingClassName)
	{
		info(_sLogText, _sCallingClassName, null);
	}

	/**
	 * This method logs at the "INFO" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void info(String _sLogText, Throwable _oThrowable)
	{
		info(_sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "INFO" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void info(String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (!_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.info("(" + _sCallingClassName + ") " + _sLogText);
		}
		else if (!_sCallingClassName.trim().equals("") && _oThrowable != null)
		{
			XO_LOGGER.info("(" + _sCallingClassName + ") " + _sLogText, _oThrowable);
		}
		else if (_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.info(_sLogText);
		}
		else
		{
			XO_LOGGER.info(_sLogText, _oThrowable);
		}
	}

	/**
	 * This method logs at the "WARN" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 */
	public static void warn(String _sLogText)
	{
		warn(_sLogText, "", null);
	}

	/**
	 * This method logs at the "WARN" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 */
	public static void warn(String _sLogText, String _sCallingClassName)
	{
		warn(_sLogText, _sCallingClassName, null);
	}

	/**
	 * This method logs at the "WARN" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void warn(String _sLogText, Throwable _oThrowable)
	{
		warn(_sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "WARN" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void warn(String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (!_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.warn("(" + _sCallingClassName + ") " + _sLogText);
		}
		else if (!_sCallingClassName.trim().equals("") && _oThrowable != null)
		{
			XO_LOGGER.warn("(" + _sCallingClassName + ") " + _sLogText, _oThrowable);
		}
		else if (_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.warn(_sLogText);
		}
		else
		{
			XO_LOGGER.warn(_sLogText, _oThrowable);
		}
	}

	/**
	 * This method logs at the "ERROR" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 */
	public static void error(String _sLogText)
	{
		error(_sLogText, "", null);
	}

	/**
	 * This method logs at the "ERROR" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 */
	public static void error(String _sLogText, String _sCallingClassName)
	{
		error(_sLogText, _sCallingClassName, null);
	}

	/**
	 * This method logs at the "ERROR" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void error(String _sLogText, Throwable _oThrowable)
	{
		error(_sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "ERROR" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void error(String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (!_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.error("(" + _sCallingClassName + ") " + _sLogText);
		}
		else if (!_sCallingClassName.trim().equals("") && _oThrowable != null)
		{
			XO_LOGGER.error("(" + _sCallingClassName + ") " + _sLogText, _oThrowable);
		}
		else if (_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.error(_sLogText);
		}
		else
		{
			XO_LOGGER.error(_sLogText, _oThrowable);
		}
	}

	/**
	 * This method logs at the "FATAL" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 */
	public static void fatal(String _sLogText)
	{
		fatal(_sLogText, "", null);
	}

	/**
	 * This method logs at the "FATAL" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 */
	public static void fatal(String _sLogText, String _sCallingClassName)
	{
		fatal(_sLogText, _sCallingClassName, null);
	}

	/**
	 * This method logs at the "FATAL" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void fatal(String _sLogText, Throwable _oThrowable)
	{
		fatal(_sLogText, "", _oThrowable);
	}

	/**
	 * This method logs at the "FATAL" priority level.
	 * 
	 * @param _sLogText
	 *            Text to log
	 * @param _sCallingClassName
	 *            Name of the calling class or method
	 * @param _oThrowable
	 *            Throwable object to show stack trace for
	 */
	public static void fatal(String _sLogText, String _sCallingClassName, Throwable _oThrowable)
	{
		if (!_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.fatal("(" + _sCallingClassName + ") " + _sLogText);
		}
		else if (!_sCallingClassName.trim().equals("") && _oThrowable != null)
		{
			XO_LOGGER.fatal("(" + _sCallingClassName + ") " + _sLogText, _oThrowable);
		}
		else if (_sCallingClassName.trim().equals("") && _oThrowable == null)
		{
			XO_LOGGER.fatal(_sLogText);
		}
		else
		{
			XO_LOGGER.fatal(_sLogText, _oThrowable);
		}
	}
}
