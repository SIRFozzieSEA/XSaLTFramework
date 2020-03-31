package com.codef.xsalt.arch;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Priority;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.codef.xsalt.arch.special.XSaLTAuthenticationObject;
import com.codef.xsalt.arch.special.XSaLTDataTableDefinition;
import com.codef.xsalt.arch.special.XSaLTSystemTables;
import com.codef.xsalt.utils.XSaLTDataUtils;
import com.codef.xsalt.utils.XSaLTFileSystemUtils;
import com.codef.xsalt.utils.XSaLTObjectUtils;
import com.codef.xsalt.utils.XSaLTStringUtils;
import com.codef.xsalt.utils.XSaLTXMLUtils;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTGenericWebServlet extends HttpServlet implements XSaLTGenericWebServletInterface
{

	/**
	 * The generated serial ID of the object
	 */
	static final long serialVersionUID = -8310724851500781618L;

	/**
	 * The immutable application version.
	 */
	public static final String XS_TMAXAPP_VERSION = "4.0";

	/**
	 * This method creates the XApp root document with standard nodes.
	 * 
	 * @param _oRequest
	 *            Http request object (from servlet)
	 * @param _oXAuthObject
	 *            Authorization object with session values
	 * @param _oDoc
	 *            Document before adding/creating standard nodes
	 * @return Standard root XML document
	 */
	protected Document createTmaXappRootDocument(HttpServletRequest _oRequest, XSaLTAuthenticationObject _oXAuthObject, Document _oDoc)
	{

		if (_oDoc == null)
		{
			_oDoc = createXSaLTRootDocument(_oRequest);
		}

		if (_oXAuthObject != null)
		{

			if (_oXAuthObject.getValueInAuthObjectMap("APPLICATION") == null)
			{
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "DB_PREFIX", _oXAuthObject.getValueInAuthObjectMap("DB_PREFIX"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "MUNI_NAME", _oXAuthObject.getValueInAuthObjectMap("MUNI_NAME"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "CLIENT_SEASON", _oXAuthObject.getValueInAuthObjectMap("CLIENT_SEASON"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "CLIENT_PREFIX_AND_SEASON", _oXAuthObject.getClientPrefixAndSeasonYear());
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "FIRST_NAME", _oXAuthObject.getValueInAuthObjectMap("FIRST_NAME"));

			}
			else
			{

				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "CLIENT_PK", _oXAuthObject.getValueInAuthObjectMap("CLIENT_PK"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "MUNI_NAME", _oXAuthObject.getValueInAuthObjectMap("MUNI_NAME"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "USER_PK", _oXAuthObject.getValueInAuthObjectMap("USER_PK"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "ROLE_NAME", _oXAuthObject.getValueInAuthObjectMap("ROLE_NAME"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "FIRST_NAME", _oXAuthObject.getValueInAuthObjectMap("FIRST_NAME"));
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "LOGIN_NAME", _oXAuthObject.getValueInAuthObjectMap("LOGIN_NAME"));

			}

		}

		return _oDoc;
	}

	/**
	 * This method adds an event to the client_tmag_users_log table.
	 * 
	 * @param _oConnection
	 *            Database connection object
	 * @param _sClientPk
	 *            The client_pk associated with the event
	 * @param _sUserPk
	 *            The user_pk associated with the event
	 * @param _sAdminUserPk
	 *            The administrative user_pk associated with the event
	 * @param _sClassName
	 *            Class name for the event 
	 * @param _sLogEvent
	 *            The event description
	 * @throws SQLException
	 */
	protected static void logTMAGEvent(Connection _oConnection, String _sClientPk, String _sUserPk, String _sAdminUserPk, String _sClassName, String _sLogEvent)
			throws SQLException
	{

		if (_sClientPk != null && _sUserPk != null)
		{

			if (_sAdminUserPk == null)
			{
				_sAdminUserPk = "null";
			}
			else
			{
				_sAdminUserPk = "'" + _sAdminUserPk + "'";
			}

			String sSQL = "INSERT INTO tmaxapp.client_tmag_users_log VALUES (null, '" + _sClientPk + "', '" + _sUserPk + "', " + _sAdminUserPk + ", '" + _sClassName + "', '"
					+ _sLogEvent + "', now())";

			XSaLTDataUtils.executeSQL(_oConnection, sSQL);

		}
	}

	/**
	 * This method returns the string value for the given preference and client.
	 * 
	 * @param _oConnection
	 *            Database connection object
	 * @param _sClientPk
	 *            The client_pk to get preference for
	 * @param _sClientPrefix
	 *            The client_prefix to get the preference for
	 * @param sPrefName
	 *            The name of the preference to get
	 * @return The string value of the desired preference
	 * @throws SQLException
	 */
	protected static String getStringPreference(Connection _oConnection, String _sClientPk, String _sClientPrefix, String sPrefName) throws SQLException
	{
		String sPrefValue = "";
		String sSQL = "SELECT PREF_VALUE_STRING FROM client_" + _sClientPrefix + "_preferences WHERE PREF_NAME = '" + sPrefName + "' AND CLIENT_PK = '" + _sClientPk + "'";
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConnection, sSQL);
		while (oRs.next())
		{
			sPrefValue = oRs.getString("PREF_VALUE_STRING");
		}
		return sPrefValue;
	}

	/**
	 * This is the service method class for all XSaLT Tasks
	 * 
	 * @param _oRequest The HttpServletRequest 
	 * @param _oResponse The HttpServletResponse 
	 */
	protected void service(HttpServletRequest _oRequest, HttpServletResponse _oResponse) throws ServletException, IOException
	{

		// see if the privs are loaded, load up defaults if not found
		checkAndLoadPrivs(_oRequest);

		// set the default language, check request to see it needs to be changed
		checkAndLoadDefaultLanguage(_oRequest);

		// set the current task in the session
		_oRequest.getSession().setAttribute(XSaLTConstants.XS_SESSION_CURRENT_SMART_TASK, getPathToTaskFileFromRequest(_oRequest));

		// lets see if the debugging is on or asked for, then set it depending on that
		checkAndLoadDebugIfRequested(_oRequest);

		Document oDoc = createXSaLTRootDocument(_oRequest);

		String sRequestedOriginalXSL = getPathToXSaLTXSLFile(_oRequest);
		String sRequestedTask = getPathToTaskFileFromRequest(_oRequest);

		//		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "");
		//		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sRequestedOriginalXSL);
		//		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sRequestedTask);
		//		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "");

		String sApplicationRequested = "";
		String sTaskAndSessionDetailsForError = "";
		boolean bRightServer = false;

		for (int i = 0; i < XSaLTFrameworkProperties.XO_APPLICATIONS_AVAILABLE.size(); i++)
		{
			String sAvailableApplication = XSaLTFrameworkProperties.XO_APPLICATIONS_AVAILABLE.get(i);
			if (sRequestedOriginalXSL.contains(sAvailableApplication))
			{
				sApplicationRequested = sAvailableApplication;
				bRightServer = true;
			}
		}

		if (bRightServer)
		{

			if (sTaskAndSessionDetailsForError.equalsIgnoreCase(""))
			{
				try
				{
					tryDoingTask(_oRequest, _oResponse, oDoc, sRequestedTask);
				}
				catch (Exception e)
				{
					sTaskAndSessionDetailsForError = "\n\t\n\t" + "XSaLT Error: XSaLTGeneralException  ---> \n\t\n\t" + sRequestedTask + "\n\t" + sRequestedOriginalXSL + "\n\t"
							+ "\n\t" + XSaLTObjectUtils.enumerateRequestToString(_oRequest) + "\n\t\n\t"
							+ XSaLTStringUtils.getStackMessage(e).replaceAll("\t", "\t\t").replaceAll("Caused by", "\n\tCaused by");
					XSaLTXMLUtils.addSimpleTextNode(oDoc, oDoc.getDocumentElement(), "ERRORNODE", sTaskAndSessionDetailsForError);
					XSaLTGenericLogger.logXSaLT(Priority.FATAL_INT, sTaskAndSessionDetailsForError);
				}
			}
		}
		else
		{
			sTaskAndSessionDetailsForError = "\n\t\n\t" + "XSaLT Error: XSaLTWrongServerException  ---> \n\t\n\t" + sRequestedTask + "\n\t" + sRequestedOriginalXSL + "\n\t"
					+ "\n\t" + "Someone at " + _oRequest.getRemoteAddr() + " is using a link on the wrong server." + "\n\t\n\t";
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oDoc.getDocumentElement(), "ERRORNODE", sTaskAndSessionDetailsForError);
			XSaLTGenericLogger.logXSaLT(Priority.FATAL_INT, sTaskAndSessionDetailsForError);
		}

		if (_oRequest.getParameter("XS_REPORT_FLAG") == null)
		{
			_oResponse.setHeader("pragma", "no-cache");
			_oResponse.setHeader("Cache-Control", "no-cache");
			_oResponse.setHeader("Cache-Control", "no-store");
			_oResponse.setDateHeader("Expires", 0);
		}

		try
		{

			if (bRightServer)
			{

				if (_oRequest.getAttribute(XSaLTConstants.XS_PARAMETER_FOR_REDIRECT_XSL) != null)
				{
					String sRequestedXSL = _oRequest.getAttribute(XSaLTConstants.XS_PARAMETER_FOR_REDIRECT_XSL).toString();
					sRequestedOriginalXSL = getPathToXSaLTXSLFile(_oRequest, sRequestedXSL);
				}

				if (!sTaskAndSessionDetailsForError.equalsIgnoreCase(""))
				{
					sRequestedOriginalXSL = sRequestedOriginalXSL.substring(0, sRequestedOriginalXSL.indexOf(sApplicationRequested) + sApplicationRequested.length() + 1)
							+ "/ErrorTask.xsl";
				}

			}
			else
			{
				String sApplicationRedirect = "";
				if (sRequestedOriginalXSL.contains("/tmaXapp"))
				{
					sRequestedOriginalXSL = sRequestedOriginalXSL.replaceAll("/tmaXapp", "/tmaGateway");
					sApplicationRedirect = "tmaGateway";
				}
				else
				{
					sRequestedOriginalXSL = sRequestedOriginalXSL.replaceAll("/tmaGateway", "/tmaXapp");
					sApplicationRedirect = "tmaXapp";
				}

				sRequestedOriginalXSL = sRequestedOriginalXSL.substring(0, sRequestedOriginalXSL.indexOf(sApplicationRedirect) + sApplicationRedirect.length() + 1)
						+ "/WrongServerTask.xsl";
			}

			if (_oRequest.getAttribute("XS_REPORT_FLAG_DISPLAY") == null)
			{
				printTransformedHTML(_oRequest, _oResponse, oDoc, sRequestedOriginalXSL);
			}
			else
			{
				if (_oRequest.getAttribute("XS_REPORT_FLAG_DISPLAY").equals("yes"))
				{
					printTransformedHTML(_oRequest, _oResponse, oDoc, sRequestedOriginalXSL);
				}
			}

		}
		catch (Exception e)
		{
			//			XSaLTGenericLogger.error("", e);
		}

		_oRequest.getSession().setAttribute(XSaLTConstants.XS_SESSION_LAST_SMART_TASK, sRequestedTask);

		if (_oRequest.getSession().getAttribute("XS_LOGOFF_FLAG") != null)
		{
			_oRequest.getSession().invalidate();
		}

	}

	/**
	 * This method attempts to execute the desired task.
	 * 
	 * @param _oRequest
	 *            Http request object- from Servlet
	 * @param _oResponse
	 *            Http response object- from Servlet
	 * @param _oDoc
	 *            XML document to hold data
	 * @param _sRequestedTask
	 *            Name of task to execute
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws XSaLTGeneralException
	 * @throws XSaLTNoPrivsException
	 */
	protected void tryDoingTask(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc, String _sRequestedTask) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, XSaLTGeneralException, XSaLTNoPrivsException
	{

		if (!hasPrivileges(_oRequest, _sRequestedTask))
		{
			throw new XSaLTNoPrivsException("User does not have privileges for this task.");
		}

		Class<?> oServletClass = Class.forName(_sRequestedTask);
		XSaLTGenericWebServlet oXsaltServlet = (XSaLTGenericWebServlet) oServletClass.newInstance();
		_oDoc = oXsaltServlet.doTask(_oRequest, _oResponse, _sRequestedTask, _oDoc, null);

	}

	/**
	 * This method attempts to execute the "WrongServerTask"
	 * 
	 * @param _oRequest
	 *            Http request object- from Servlet (NOT USED)
	 * @param _oResponse
	 *            Http response object- from Servlet (NOT USED)
	 * @param _oDoc
	 *            XML document to hold data (NOT USED)
	 * @param _sRequestedTask
	 *            Name of task to execute (NOT USED)
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws XSaLTGeneralException
	 */
	protected void tryDoingWrongServerTask(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc, String _sRequestedTask) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, XSaLTGeneralException
	{
		_oRequest.setAttribute(XSaLTConstants.XS_PARAMETER_FOR_REDIRECT_XSL, "/../WrongServerTask");

	}

	/**
	 * 
	 * The technical "MAIN" task for all XSaLT servlets - This will be overridden by individual servlets
	 * 
	 * @param _oRequest The HttpServletRequest object
	 * @param _oResponse The HttpServletResponseresponse object
	 * @param _oDoc The XSaLT task XML document
	 * @return org.w3c.dom.Document - An XML document
	 * @throws Exception
	 */
	public Document doTask(HttpServletRequest _oRequest, HttpServletResponse _oResponse, String _sJavaTask, Document _oDoc, Connection _oConnection) throws XSaLTGeneralException
	{
		return null;
	}

	/**
	 * 
	 * Executes the "next task" via a URI
	 * 
	 * @param _oRequest The HttpServletRequest object
	 * @param _oResponse The HttpServletResponseresponse object
	 * @param _oDoc The XSaLT task XML document
	 * @param _sTaskUri The String URI to the task
	 * @return org.w3c.dom.Document - An XML document
	 * @throws Exception
	 */
	protected Document doNextTaskURI(HttpServletRequest _oRequest, HttpServletResponse _oResponse, String _sTaskUri, Document _oDoc, Connection _oConnection)
			throws XSaLTGeneralException
	{
		try
		{
			Class<?> oServletClass = Class.forName(getPathToTaskFileFromURI(_sTaskUri));
			XSaLTGenericWebServlet oXsaltServlet = (XSaLTGenericWebServlet) oServletClass.newInstance();
			_oDoc = oXsaltServlet.doTask(_oRequest, _oResponse, _sTaskUri, _oDoc, null);
		}
		catch (Exception e)
		{
			throw new XSaLTGeneralException("Cannot execute doNextTaskURI(" + _sTaskUri + ").", e);
		}
		return _oDoc;
	}

	/**
	 * 
	 * Executes the "next task" via a java path
	 * 
	 * @param _oRequest The HttpServletRequest object
	 * @param _oResponse The HttpServletResponseresponse object
	 * @param _oDoc The XSaLT task XML document
	 * @param _sTaskJavaPath The String java path to the task
	 * @return org.w3c.dom.Document - An XML document
	 * @throws Exception
	 */
	protected Document doNextTaskJavaPath(HttpServletRequest _oRequest, HttpServletResponse _oResponse, String _sTaskJavaPath, Document _oDoc, Connection _oConnection)
			throws XSaLTGeneralException
	{
		try
		{
			Class<?> oServletClass = Class.forName(_sTaskJavaPath);
			XSaLTGenericWebServlet oXsaltServlet = (XSaLTGenericWebServlet) oServletClass.newInstance();
			_oDoc = oXsaltServlet.doTask(_oRequest, _oResponse, _sTaskJavaPath, _oDoc, _oConnection);
		}
		catch (Exception e)
		{
			throw new XSaLTGeneralException("Cannot execute doNextTaskJavaPath(" + _sTaskJavaPath + ").", e);
		}
		return _oDoc;
	}

	/**
	 * This method checks and loads the default privs for the user. Further privs are loaded at login tasks
	 * 
	 * @param _oRequest The HttpServletRequest to examine and modify
	 */
	protected void checkAndLoadPrivs(HttpServletRequest _oRequest)
	{
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_PRIVS_ARRAYLIST) == null)
		{
			XSaLTSystemTables oSystemTables = new XSaLTSystemTables();
			ArrayList<String> oPrivsArrayList = oSystemTables.getStandardXAppPrivs(true, true);
			_oRequest.getSession().setAttribute(XSaLTConstants.XS_SESSION_PRIVS_ARRAYLIST, oPrivsArrayList);
		}
	}

	/**
	 * This method checks and loads the default language for the user, it also checks the request parameter to see if the user could be changing it
	 * 
	 * @param _oRequest The HttpServletRequest to examine and modify
	 */
	protected void checkAndLoadDefaultLanguage(HttpServletRequest _oRequest)
	{
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE) == null)
		{
			_oRequest.getSession().setAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE, "EN");
		}
		if (_oRequest.getParameter(XSaLTConstants.XS_DEFAULT_LANGUAGE) != null)
		{
			_oRequest.getSession().setAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE, _oRequest.getParameter(XSaLTConstants.XS_DEFAULT_LANGUAGE));

		}
	}

	/**
	 * This method checks and loads the debugging features if the request parameter has the debug flag in it
	 * 
	 * @param _oRequest The HttpServletRequest to examine and modify
	 */
	protected void checkAndLoadDebugIfRequested(HttpServletRequest _oRequest)
	{
		if (!XSaLTStringUtils.isStringNullOrEmpty(_oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER)))
		{
			if (_oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER).equals("1") || _oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER).equals("true"))
			{
				_oRequest.getSession().setAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER, "on");
			}
			else
			{
				_oRequest.getSession().setAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER, "off");
			}
		}

		if (!XSaLTStringUtils.isStringNullOrEmpty(_oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER)))
		{
			if (_oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER).equals("1")
					|| _oRequest.getParameter(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER).equals("true"))
			{
				_oRequest.getSession().setAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER, "on");
			}
			else
			{
				_oRequest.getSession().setAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER, "off");
			}
		}

	}

	/**
	 * This method creats the XSaLTRootDocument for all XSaLT Tasks
	 * 
	 * @param _oRequest The HttpServletRequest to base the XML document upon
	 * @return The XSaLT root XML document
	 */
	protected Document createXSaLTRootDocument(HttpServletRequest _oRequest)
	{
		DOMImplementation oDomImp = new DOMImplementationImpl();
		Document oDoc = oDomImp.createDocument(null, "XSaLTDocument", null);
		Element oRoot = oDoc.getDocumentElement();

		Element oXSaLTPrefsElement = XSaLTXMLUtils.addSimpleTextNode(oDoc, oRoot, "XSaLTPrefs", null);

		StringBuffer sClientContext = new StringBuffer();
		try
		{
			sClientContext = new StringBuffer(_oRequest.getRequestURI().substring(0,
					_oRequest.getRequestURI().indexOf("/", XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME.length() + 2)));
		}
		catch (Exception e)
		{
			sClientContext = new StringBuffer(_oRequest.getRequestURI());
		}

		StringBuffer sApplicationURI = new StringBuffer(sClientContext.toString());
		if (sApplicationURI.toString().endsWith("/"))
		{
			sApplicationURI.append("");
		}
		else
		{
			sApplicationURI.append("/");
		}

		String sShortURI = sApplicationURI.substring(0, sApplicationURI.length() - 1);
		sShortURI = sShortURI.substring(sShortURI.lastIndexOf("/") + 1, sShortURI.length());

		StringBuffer sIncludesURI = null;

		if (XSaLTFrameworkProperties.XB_USE_PORT_IN_URL_FLAG == true)
		{
			sIncludesURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER);
		}
		else
		{
			sIncludesURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "");
		}

		sIncludesURI.append("/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "/EN/" + sShortURI + "/includes/");
		XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "IncludesURI", sIncludesURI.toString());

		if (XSaLTFrameworkProperties.XB_USE_PORT_IN_URL_FLAG == true)
		{
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppURI", XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + sApplicationURI);

			StringBuffer sAppletURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + "/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME);
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppletURI", sAppletURI.toString());

			StringBuffer sAppletConnectionURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + "/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "");
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppletConnectionURI", sAppletConnectionURI.toString());
		}
		else
		{
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppURI", XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName()
					+ sApplicationURI);

			StringBuffer sAppletURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "/"
					+ XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME);
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppletURI", sAppletURI.toString());

			StringBuffer sAppletConnectionURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "/"
					+ XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "");
			XSaLTXMLUtils.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "AppletConnectionURI", sAppletConnectionURI.toString());

		}

		java.util.Random r = new java.util.Random();
		long lRandomLong = r.nextLong();

		String sSecureSessionID = (String) _oRequest.getAttribute("javax.servlet.request.ssl_session");

		if (sSecureSessionID == null)
		{
			sSecureSessionID = "";
		}

		XSaLTXMLUtils
				.addSimpleTextNode(oDoc, oXSaLTPrefsElement, "TransactionID", sSecureSessionID + "_" + XSaLTStringUtils.getDatetimeString() + new Long(lRandomLong).toString());

		oRoot.appendChild(oXSaLTPrefsElement);

		return oDoc;
	}

	/**
	 * This method looks at the HttpServletRequest to determine root values
	 * for the application.
	 * 
	 * @param _oRequest
	 *            Http request object- from Servlet
	 * @return HashMap of values pertaining to application.
	 *          Keys are: IncludesURI
	 *                    AppURI
	 *                    AppletURI        
	 */
	protected HashMap<String, String> getXSaLTRootDocumentValues(HttpServletRequest _oRequest)
	{

		HashMap<String, String> oRootDocumentValues = new HashMap<String, String>();

		StringBuffer sClientContext = new StringBuffer();
		try
		{
			sClientContext = new StringBuffer(_oRequest.getRequestURI().substring(0,
					_oRequest.getRequestURI().indexOf("/", XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME.length() + 2)));
		}
		catch (Exception e)
		{
			sClientContext = new StringBuffer(_oRequest.getRequestURI());
		}

		StringBuffer sApplicationURI = new StringBuffer(sClientContext.toString());
		if (sApplicationURI.toString().endsWith("/"))
		{
			sApplicationURI.append("");
		}
		else
		{
			sApplicationURI.append("/");
		}

		String sShortURI = sApplicationURI.substring(0, sApplicationURI.length() - 1);
		sShortURI = sShortURI.substring(sShortURI.lastIndexOf("/") + 1, sShortURI.length());

		StringBuffer sIncludesURI = null;

		if (XSaLTFrameworkProperties.XB_USE_PORT_IN_URL_FLAG == true)
		{
			sIncludesURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER);
		}
		else
		{
			sIncludesURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "");
		}

		sIncludesURI.append("/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "/EN/" + sShortURI + "/includes/");

		oRootDocumentValues.put("IncludesURI", sIncludesURI.toString());

		if (XSaLTFrameworkProperties.XB_USE_PORT_IN_URL_FLAG == true)
		{

			oRootDocumentValues.put("AppURI", XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + sApplicationURI);

			StringBuffer sAppletURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + "/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME);
			oRootDocumentValues.put("AppletURI", sAppletURI.toString());

			StringBuffer sAppletConnectionURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + "/" + XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "");
			oRootDocumentValues.put("IncludesURI", sAppletConnectionURI.toString());

		}
		else
		{

			oRootDocumentValues.put("AppURI", XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + sApplicationURI);

			StringBuffer sAppletURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "/"
					+ XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME);
			oRootDocumentValues.put("AppletURI", sAppletURI.toString());

			StringBuffer sAppletConnectionURI = new StringBuffer(XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + "/"
					+ XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME + "");
			oRootDocumentValues.put("IncludesURI", sAppletConnectionURI.toString());

		}

		return oRootDocumentValues;
	}

	/**
	 * This method creates a scrollset session (for paging data, forward and back data buttons)
	 * 
	 * @param _oRequest The HttpServletRequest to base the scrollset upon
	 * @param _bManuallyRecreate The flag to tell the method to manually recreate (clearing)
	 */
	protected void createScrollsetSession(HttpServletRequest _oRequest, boolean _bManuallyRecreate)
	{
		String sJavaTask = getPathToTaskFileFromRequest(_oRequest).replace('.', '_').toUpperCase();
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask) == null || _bManuallyRecreate)
		{
			HashMap<String, String> oScrollSetParams = new HashMap<String, String>();
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_RECORDSET_PAGE, new Long(0).toString());
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_DISPLAY_RECORDSET_SIZE, XSaLTFrameworkProperties.XL_MAXIMUM_RECORDS_RETRIEVED.toString());
			oScrollSetParams.put(XSaLTConstants.XS_SCROLLSET_VIEWPORT, "Default");
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SORT_BY, "Default");
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_NAME, "Default");
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_CRITERIA, "Default");
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_STATUS, "All");
			_oRequest.getSession().setAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask, oScrollSetParams);
		}
	}

	/**
	 * This method gets the current transaction ID of the request
	 * 
	 * @param _oRequest The HttpServletRequest to enquire upon
	 * @return The transaction ID
	 */
	protected String getTransactionID(HttpServletRequest _oRequest)
	{
		if (_oRequest.getParameter(XSaLTConstants.XS_SESSION_LAST_TRANSACTIONID) != null)
		{
			return _oRequest.getParameter(XSaLTConstants.XS_SESSION_LAST_TRANSACTIONID);
		}
		else
		{
			return "";
		}
	}

	/**
	 * This method evaluates if the current transaction ID of the request matches a new one, eliminating double posting
	 * 
	 * @param _oRequest The HttpServletRequest to enquire upon
	 * @param _sThisTransactionId The transaction ID to compare with
	 * @return If the request is going to be double posted
	 */
	protected boolean isRequestDoublePosted(HttpServletRequest _oRequest, String _sThisTransactionId)
	{
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_LAST_TRANSACTIONID) != null)
		{
			if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_LAST_TRANSACTIONID).toString().equals(_sThisTransactionId))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			// no transaction id
			return false;
		}
	}

	/**
	 * This method evaluates if the form is "dirty" and has not been double posted
	 * 
	 * @param _oRequest The HttpServletRequest to enquire upon
	 * @param _bRequestDoublePosted Flag if the form has been double posted, you can use isRequestDoublePosted() for that.
	 * @return If the form is dirty and has not been double posted.
	 */
	protected boolean isFormDirtyAndNotDoublePosted(HttpServletRequest _oRequest, boolean _bRequestDoublePosted)
	{
		if (_oRequest.getParameter("IS_FORM_DIRTY") != null && _oRequest.getParameter("IS_FORM_DIRTY").equals("true") && !_bRequestDoublePosted)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method is a convenience method for looking at the value parameter for any work task that is named "DO"
	 * 
	 * @param _oRequest The HttpServletRequest to enquire upon
	 * @param _sOperation The String operation your looking for
	 * @return If the operation has been specified in the HttpServletRequest
	 */
	protected boolean isFormOperation(HttpServletRequest _oRequest, String _sOperation)
	{
		if (_oRequest.getParameter("DO") != null && _oRequest.getParameter("DO").equalsIgnoreCase(_sOperation))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * This method updates an existing scrollset.
	 * 
	 * @param _oRequest
	 *            Http request object- from servlet
	 * @return HashMap with updated values for new scrollset
	 */
	protected HashMap<String, String> updateScrollsetSession(HttpServletRequest _oRequest)
	{

		if (_oRequest.getParameter("DO") != null && _oRequest.getParameter("DO").equalsIgnoreCase("CLEARSEARCH"))
		{
			createScrollsetSession(_oRequest, true);
		}
		else
		{
			createScrollsetSession(_oRequest, false);
		}

		String sJavaTask = getPathToTaskFileFromRequest(_oRequest).replace('.', '_').toUpperCase();

		HashMap<String, String> oScrollSetParams = XSaLTObjectUtils.getObjectAsHashMap_String_String(_oRequest.getSession().getAttribute(
				XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask));

		_oRequest.getSession().removeAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask);

		oScrollSetParams.remove(XSaLTConstants.XS_SESSION_LAST_RECORDSET_PAGE);
		oScrollSetParams.remove(XSaLTConstants.XS_SESSION_DISPLAY_RECORDSET_SIZE);

		oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_RECORDSET_PAGE,
				new Long(XSaLTStringUtils.getDefaultLongIfParameterIsNull(_oRequest.getParameter("PageNo"), 1)).toString());
		oScrollSetParams.put(
				XSaLTConstants.XS_SESSION_DISPLAY_RECORDSET_SIZE,
				new Long(XSaLTStringUtils.getDefaultLongIfParameterIsNull(_oRequest.getParameter("RecordsPerPage"),
						XSaLTFrameworkProperties.XL_MAXIMUM_RECORDS_RETRIEVED.longValue())).toString());

		if (_oRequest.getParameter("DO") != null && _oRequest.getParameter("DO").equalsIgnoreCase("VIEWPORT"))
		{
			oScrollSetParams.put(XSaLTConstants.XS_SCROLLSET_VIEWPORT, _oRequest.getParameter("VIEWPORT"));
		}

		if (_oRequest.getParameter("DO") != null && _oRequest.getParameter("DO").equalsIgnoreCase("SEARCH"))
		{
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_NAME, XSaLTStringUtils.getDefaultStringIfNullOrEmpty(_oRequest.getParameter("SEARCHNAME"), "Default"));
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_CRITERIA, XSaLTStringUtils.getDefaultStringIfNullOrEmpty(_oRequest.getParameter("SEARCHCRIT"), "Default"));
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SEARCH_STATUS, XSaLTStringUtils.getDefaultStringIfNullOrEmpty(_oRequest.getParameter("SEARCHSTATUS"), "All"));
		}

		if (_oRequest.getParameter("ORDERBY") != null)
		{
			oScrollSetParams.put(XSaLTConstants.XS_SESSION_LAST_SORT_BY, _oRequest.getParameter("ORDERBY"));
		}

		_oRequest.getSession().setAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask, oScrollSetParams);
		return oScrollSetParams;
	}

	/**
	 * This method gets the default scrollset viewport.
	 * 
	 * @param _oRequest
	 *            Http request object- from servlet
	 * @param _oDoc
	 *            XML document that will be returned to the transform page
	 * @return Default string of the scrollset viewport
	 */
	protected String getDefaultScrollSetViewPort(HttpServletRequest _oRequest, Document _oDoc)
	{
		String sJavaTask = getPathToTaskFileFromRequest(_oRequest).replace('.', '_').toUpperCase();
		Object oTempObject = _oRequest.getSession().getAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask);
		@SuppressWarnings("unchecked")
		HashMap<String, String> oScrollSetParams = (HashMap<String, String>) oTempObject;
		XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "VIEWPORT_VIEW", oScrollSetParams.get(XSaLTConstants.XS_SCROLLSET_VIEWPORT).toString());
		return oScrollSetParams.get(XSaLTConstants.XS_SCROLLSET_VIEWPORT).toString();
	}

	/**
	 * This method returns a flag for if the user has the needed privileges for
	 * the specified task.
	 * 
	 * @param _oRequest
	 *            Http request object- from the servlet
	 * @param _sTaskJavaPath
	 *            Name of the task to determine user permissions for
	 * @return Flag if the user has privileges for the given task
	 */
	protected static boolean hasPrivileges(HttpServletRequest _oRequest, String _sTaskJavaPath)
	{

		boolean bHasPrivileges = false;

		if (_sTaskJavaPath.indexOf("UTask") != -1)
		{
			bHasPrivileges = true;
		}
		else
		{
			if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_PRIVS_ARRAYLIST) != null)
			{
				Object oTempObject = _oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_PRIVS_ARRAYLIST);
				@SuppressWarnings("unchecked")
				ArrayList<String> oPrivilegesArrayList = (ArrayList<String>) oTempObject;
				for (int j = 0; j < oPrivilegesArrayList.size(); j++)
				{

					String sRegExPatternTest = oPrivilegesArrayList.get(j).toString();

					if (sRegExPatternTest.endsWith("."))
					{
						sRegExPatternTest = sRegExPatternTest.substring(0, sRegExPatternTest.length() - 1);
					}
					Pattern oPattern = Pattern.compile("^" + sRegExPatternTest + ".+");
					Matcher oMatcher = oPattern.matcher(_sTaskJavaPath);

					if (oMatcher.find())
					{
						bHasPrivileges = true;
					}
				}
			}
		}

		return bHasPrivileges;
	}

	protected String getPathToErrorXSL(HttpServletRequest _oRequest)
	{
		String sPathToXslFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString() + "/"
				+ XSaLTFrameworkProperties.XS_ERROR_XSL_TO_CALL_PREF;
		return sPathToXslFile;
	}

	/**
	 * 
	 * Gets the path to the task file for the requested task from the request object
	 * 
	 * @param _oRequest The http request
	 * @return - Path to the task file
	 */
	protected String getPathToTaskFileFromRequest(HttpServletRequest _oRequest)
	{

		String sPathToTaskFileStart = XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getRequestURI().substring(_oRequest.getRequestURI().indexOf("/", 1),
				_oRequest.getRequestURI().length()));

		String sPathToTaskFile = "";
		if (sPathToTaskFileStart.equalsIgnoreCase(""))
		{
			sPathToTaskFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString() + "/"
					+ XSaLTFrameworkProperties.XS_ERROR_XSL_TO_CALL_PREF;
		}
		else if (sPathToTaskFileStart.equalsIgnoreCase("/tmaGateway") || sPathToTaskFileStart.equalsIgnoreCase("/tmaXapp") || sPathToTaskFileStart.equalsIgnoreCase("/tmaGateway/")
				|| sPathToTaskFileStart.equalsIgnoreCase("/tmaXapp/"))
		{
			sPathToTaskFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(sPathToTaskFileStart);
			if (sPathToTaskFileStart.endsWith("/"))
			{
				sPathToTaskFile = sPathToTaskFile + "DefaultTask";
			}
			else
			{
				sPathToTaskFile = sPathToTaskFile + ".DefaultTask";
			}
		}
		else if (!sPathToTaskFileStart.endsWith("Task"))
		{

			if (sPathToTaskFileStart.endsWith("Gateway"))
			{
				sPathToTaskFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(sPathToTaskFileStart);

			}
			else
			{
				sPathToTaskFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(sPathToTaskFileStart);
				if (sPathToTaskFileStart.endsWith("/"))
				{
					sPathToTaskFile = sPathToTaskFile + "DefaultTask";
				}
				else
				{
					sPathToTaskFile = sPathToTaskFile + ".DefaultTask";
				}
			}

		}
		else
		{
			sPathToTaskFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(sPathToTaskFileStart);
		}

		return sPathToTaskFile;

	}

	/**
	 * 
	 * Gets the path to the XSL file for the requested task from the request object
	 * 
	 * @param _oRequest The http request
	 * @return - Path to the XSL file
	 */
	protected String getPathToXSaLTXSLFile(HttpServletRequest _oRequest)
	{

		String sPathToTaskXSLFileStart = XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getRequestURI().substring(_oRequest.getRequestURI().indexOf("/", 1),
				_oRequest.getRequestURI().length()));

		String sPathToXslFile = "";
		if (sPathToTaskXSLFileStart.equalsIgnoreCase(""))
		{
			sPathToXslFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString() + "/"
					+ XSaLTFrameworkProperties.XS_ERROR_XSL_TO_CALL_PREF;
		}
		else if (sPathToTaskXSLFileStart.equalsIgnoreCase("/tmaGateway") || sPathToTaskXSLFileStart.equalsIgnoreCase("/tmaXapp")
				|| sPathToTaskXSLFileStart.equalsIgnoreCase("/tmaGateway/") || sPathToTaskXSLFileStart.equalsIgnoreCase("/tmaXapp/"))
		{
			sPathToXslFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString()
					+ sPathToTaskXSLFileStart;
			if (sPathToTaskXSLFileStart.endsWith("/"))
			{
				sPathToXslFile = sPathToXslFile + "DefaultTask.xsl";
			}
			else
			{
				sPathToXslFile = sPathToXslFile + "/DefaultTask.xsl";
			}
		}
		else if (!sPathToTaskXSLFileStart.endsWith("Task"))
		{

			if (sPathToTaskXSLFileStart.endsWith("Gateway"))
			{
				sPathToXslFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString()
						+ sPathToTaskXSLFileStart + ".xsl";
			}
			else
			{
				sPathToXslFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(sPathToTaskXSLFileStart);
				if (sPathToTaskXSLFileStart.endsWith("/"))
				{
					sPathToXslFile = sPathToXslFile + "DefaultTask.xsl";
				}
				else
				{
					sPathToXslFile = sPathToXslFile + "/DefaultTask.xsl";
				}
			}

		}
		else
		{
			sPathToXslFile = XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString()
					+ sPathToTaskXSLFileStart + ".xsl";
		}

		return sPathToXslFile.replaceAll("\\.", "/").replaceAll("/xsl", ".xsl");

	}

	/**
	 * 
	 * Gets the path to the XSL file for the requested task from the request object
	 * 
	 * @param _oRequest The http request
	 * @return - Path to the XSL file
	 */
	protected String getPathToXSaLTXSLFile(HttpServletRequest _oRequest, String _sPartialPathToXSL)
	{

		StringBuffer sClientContext = new StringBuffer(_oRequest.getRequestURI().substring(XSaLTFrameworkProperties.XS_DEFAULT_CONTEXT_NAME.length() + 1,
				_oRequest.getRequestURI().length()));
		sClientContext = new StringBuffer(sClientContext.toString().substring(0, sClientContext.toString().indexOf("/", 1)));
		sClientContext = new StringBuffer(XSaLTFrameworkProperties.XS_PATH_TO_XSALT_XSLS + "/" + _oRequest.getSession().getAttribute(XSaLTConstants.XS_DEFAULT_LANGUAGE).toString()
				+ sClientContext + _sPartialPathToXSL + ".xsl");
		return sClientContext.toString().replaceAll("\\.", "/").replaceAll("/xsl", ".xsl");
	}

	/**
	 * 
	 * Gets the URL path to the task file for the requested task from the request object
	 * 
	 * @param _oRequest
	 * @return The URL to task
	 */
	protected String getURLToTaskFile(HttpServletRequest _oRequest)
	{

		if (XSaLTFrameworkProperties.XB_USE_PORT_IN_URL_FLAG == true)
		{
			String sPathToXslFile = XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + ":"
					+ XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT_NUMBER + _oRequest.getRequestURI();
			return sPathToXslFile;
		}
		else
		{
			String sPathToXslFile = XSaLTFrameworkProperties.XS_HTTP_OR_HTTPS_PORT + "://" + _oRequest.getServerName() + _oRequest.getRequestURI();
			return sPathToXslFile;
		}

	}

	/**
	 * 
	 * Gets the path to the task file for the requested task from the URI
	 * 
	 * @param _sUri
	 * @return - The path to the task file
	 */
	protected String getPathToTaskFileFromURI(String _sUri)
	{
		String sPathToTaskFile = XSaLTFrameworkProperties.XS_URI_CONTEXT_TO_XSALTTASKS + XSaLTStringUtils.regExReplaceForwardSlashesWithPeriods(_sUri);
		return sPathToTaskFile;
	}

	/**
	 * 
	 * Transform the XSaLT root document with the XSL file for the task, using the task in the request specified
	 * 
	 * @param _oRequest The http request
	 * @param _oResponse The http response
	 * @param _oDoc The XSaLT root document
	 * @throws IOException
	 * @throws TransformerException
	 */
	protected void printTransformedHTML(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc, String _sManualPathToXSL) throws IOException,
			TransformerException
	{

		if (!_oResponse.isCommitted())
		{
			_oResponse.setContentType("text/html");
			Source oSourceXSL = null;
			PrintWriter oPrintWriter = _oResponse.getWriter();
			TransformerFactory oTransFactory = TransformerFactory.newInstance();
			if (_sManualPathToXSL == null)
			{
				oSourceXSL = new StreamSource(new File(getPathToXSaLTXSLFile(_oRequest)));
			}
			else
			{
				oSourceXSL = new StreamSource(new File(_sManualPathToXSL));
			}
			Transformer oTransformer = oTransFactory.newTransformer(oSourceXSL);
			Source oSourceXML = null;

			if (_oDoc != null)
			{
				oSourceXML = new DOMSource(_oDoc);
			}
			else
			{
				oSourceXML = new DOMSource(createXSaLTRootDocument(_oRequest));
			}

			oTransformer.transform(oSourceXML, new StreamResult(oPrintWriter));

			if (_oDoc != null)
			{

				if ((_oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER) != null && _oRequest.getSession()
						.getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER).equals("on"))
						|| (_oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER) != null && _oRequest.getSession()
								.getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER).equals("on")))
				{
					String sDocument = XSaLTXMLUtils.xmlDocumentToString(_oDoc);

					if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER) != null
							&& _oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_PARAMETER).equals("on"))
					{

						oPrintWriter.write("\n<!--\n\n" + sDocument + "\n\n-->");
					}

					if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER) != null
							&& _oRequest.getSession().getAttribute(XSaLTConstants.XS_REQUEST_DEBUG_WRITE_PARAMETER).equals("on"))
					{

						XSaLTFileSystemUtils.writeStringToFile(sDocument, "C:/XSaLTDataDebug.xml");

					}
				}

			}

			oPrintWriter.close();
		}

	}

	/**
	 * 
	 * Gets the name of the current XSaLT task specified in the request/session
	 * 
	 * @param _oRequest The http request
	 * @return - The current task to be executed
	 */
	protected String getSessionCurrentXSaLTTask(HttpServletRequest _oRequest)
	{
		return XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(XSaLTConstants.XS_SESSION_CURRENT_SMART_TASK));
	}

	/**
	 * 
	 * Gets the name of the last XSaLT task specified in the request/session
	 * 
	 * @param _oRequest The http request
	 * @return - The last task to be executed
	 */
	protected String getSessionLastXSaLTTask(HttpServletRequest _oRequest)
	{
		return XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(XSaLTConstants.XS_SESSION_LAST_SMART_TASK));
	}

	/**
	 * 
	 * Gets the maximum resultset size specified in the request/session
	 * 
	 * @param _oRequest The http request
	 * @return long - The maximum resultset size
	 */

	protected long getMaxResultSetSize(HttpServletRequest _oRequest)
	{
		String sJavaTask = getPathToTaskFileFromRequest(_oRequest).replace('.', '_').toUpperCase();
		Object oTempObject = _oRequest.getSession().getAttribute(XSaLTConstants.XS_SCROLLSET_PARAM + sJavaTask);
		@SuppressWarnings("unchecked")
		HashMap<String, String> oScrollSetParams = (HashMap<String, String>) oTempObject;
		return XSaLTStringUtils.getDefaultLongIfParameterIsNull(oScrollSetParams.get(XSaLTConstants.XS_SESSION_DISPLAY_RECORDSET_SIZE).toString(),
				new Long(oScrollSetParams.get(XSaLTConstants.XS_SESSION_DISPLAY_RECORDSET_SIZE).toString()).longValue());
	}

	/**
	 * This method returns the UserTransaction object for this context.
	 * 
	 * @return UserTransaction object for this context
	 */
	protected UserTransaction getUserTransaction()
	{
		try
		{
			Context oInitialContext = new InitialContext();
			UserTransaction ut = (UserTransaction) oInitialContext.lookup("java:comp/UserTransaction");
			return ut;
		}
		catch (NamingException e)
		{

			return null;
		}

	}

	/**
	 * This method rolls back the specified database transaction.
	 * 
	 * @param _oTransaction
	 *            Database transaction object
	 */
	protected void rollBackTransaction(UserTransaction _oTransaction)
	{
		if (_oTransaction != null)
		{
			try
			{
				_oTransaction.rollback();
			}
			catch (IllegalStateException e)
			{

			}
			catch (SecurityException e)
			{

			}
			catch (SystemException e)
			{

			}
		}
	}

	/**
	 * This method returns a flag if the user is logged in.
	 * @param _oRequest
	 *            Http request object- from the servlet
	 * @return Flag if the user is currently logged in
	 */
	protected boolean isUserLoggedIn(HttpServletRequest _oRequest)
	{
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_AUTHENTICATION_OBJECT) == null)
		{
			return false;
		}
		else
		{

			XSaLTAuthenticationObject oXAuthObject = (XSaLTAuthenticationObject) _oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_AUTHENTICATION_OBJECT);

			String sUserPk = oXAuthObject.getValueInAuthObjectMap("USER_PK");

			if (sUserPk == null)
			{
				return false;
			}

			return true;
		}
	}

	/**
	 * This method looks in a session object to determine if the municipality
	 * has been set and returns a flag if the value is in the session.
	 * 
	 * @param _oRequest
	 *            Http request object- from the servlet
	 * @return Flag if municipality is set in the authorization object
	 */
	protected boolean isMuniInSession(HttpServletRequest _oRequest)
	{
		if (_oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_AUTHENTICATION_OBJECT) == null)
		{
			return false;
		}
		else
		{

			XSaLTAuthenticationObject oXAuthObject = (XSaLTAuthenticationObject) _oRequest.getSession().getAttribute(XSaLTConstants.XS_SESSION_AUTHENTICATION_OBJECT);

			String sMuniPrefix = oXAuthObject.getValueInAuthObjectMap("DB_PREFIX");
			String sClientPk = oXAuthObject.getValueInAuthObjectMap("CLIENT_PK");

			if (sMuniPrefix == null)
			{
				return false;
			}

			if (sClientPk == null)
			{
				return false;
			}

			return true;
		}
	}

	/**
	 * This method generates a report and returns an XML document with report
	 * information.  The report may be exported into a downloadable format, in
	 * which case the report will be placed in the _oResponse object.
	 * 
	 * @param _oRequest
	 *            Http request object- from servlet
	 * @param _oResponse
	 *            Http response object- from servlet
	 * @param _sJavaTask
	 *            Task name
	 * @param _oDoc
	 *            XML document to add report into
	 * @param _oConnection
	 *            Database connection object
	 * @param _sReportType
	 *            Type of report to generate (e.g. comma-delimited, tab-delimited, etc)
	 * @param _sReportQuery
	 *            SQL query statement to generate report
	 * @param _sReportFileName
	 *            File to export report results to (not needed for HTML report)
	 * @param _sReportTitle
	 *            Title of report
	 * @param _sDropTableName
	 *            Name of table to drop (currently not used)
	 * @param _bWriteHeaders
	 *            Flag if column headers should be written to report
	 * @return XML document with report information (if file is to be downloaded, it is in the response object)
	 */
	protected Document processReport(HttpServletRequest _oRequest, HttpServletResponse _oResponse, String _sJavaTask, Document _oDoc, Connection _oConnection, String _sReportType,
			String _sReportQuery, String _sReportFileName, String _sReportTitle, String _sDropTableName, boolean _bWriteHeaders)
	{
		if (_oDoc == null)
		{
			_oDoc = createXSaLTRootDocument(_oRequest);
		}

		try
		{
			if (_oConnection == null)
			{
				_oConnection = XSaLTDataUtils.getPooledClientXAPPConnection();
			}

			if (_sReportType != null || isFormOperation(_oRequest, "EXPORT_QUERY"))
			{

				XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_TITLE", _sReportTitle);

				if (_sReportType.equals("XS_REPORT_EXCEL_FLAG") || _sReportType.equals("XS_REPORT_EXCEL_XML_FLAG"))
				{
					_oResponse.setContentType("application/vnd.ms-excel");
					_oResponse.setHeader("content-disposition", "attachment; filename=" + _sReportFileName + ".xls");
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "yes");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", _sReportType);

				}
				else if (_sReportType.equals("XS_REPORT_XML_FLAG"))
				{
					_oResponse.setContentType("text/xml");
					_oResponse.setHeader("content-disposition", "attachment; filename=" + _sReportFileName + ".xml");
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "no");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", "XS_REPORT_XML_FLAG");
				}
				else if (_sReportType.equals("XS_REPORT_COMMA_DELIMITED_FLAG"))
				{
					_oResponse.setContentType("text/plain");
					_oResponse.setHeader("content-disposition", "attachment; filename=" + _sReportFileName + ".csv");
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "no");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", "XS_REPORT_COMMA_DELIMITED_FLAG");
				}
				else if (_sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG"))
				{
					_oResponse.setContentType("text/plain");
					_oResponse.setHeader("content-disposition", "attachment; filename=" + _sReportFileName + ".tsv");
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "no");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", "XS_REPORT_TAB_DELIMITED_FLAG");
				}
				else if (_sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG_STD"))
				{
					_oResponse.setContentType("text/plain");
					_oResponse.setHeader("content-disposition", "attachment; filename=" + _sReportFileName + ".tab");
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "no");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", "XS_REPORT_TAB_DELIMITED_FLAG_STD");
				}
				else
				{
					_oRequest.setAttribute("XS_REPORT_FLAG_DISPLAY", "yes");
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "XS_REPORT_FLAG", "XS_REPORT_HTML_FLAG");

				}

				if (_sReportType.equals("XS_REPORT_COMMA_DELIMITED_FLAG") || _sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG_STD")
						|| _sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG") || _sReportType.equals("XS_REPORT_XML_FLAG") || _sReportType.equals("XS_REPORT_EXCEL_FLAG")
						|| _sReportType.equals("XS_REPORT_EXCEL_XML_FLAG"))
				{
					try
					{
						PrintWriter oPrintWriter = _oResponse.getWriter();
						StringBuffer oReportStringBuffer = new StringBuffer();
						if (_sReportType.equals("XS_REPORT_COMMA_DELIMITED_FLAG"))
						{
							oReportStringBuffer = XSaLTDataUtils.exportSQLAsCommaDelimitedDataFile(_oConnection, _sReportQuery, null, _bWriteHeaders);
						}
						else if (_sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG") || _sReportType.equals("XS_REPORT_TAB_DELIMITED_FLAG_STD")
								|| _sReportType.equals("XS_REPORT_EXCEL_FLAG"))
						{
							oReportStringBuffer = XSaLTDataUtils.exportSQLAsTabDelimitedDataFile(_oConnection, _sReportQuery, null, _bWriteHeaders);
						}
						else if (_sReportType.equals("XS_REPORT_EXCEL_XML_FLAG"))
						{
							Document oDoc = XSaLTXMLUtils.exportSQLAsExcelXMLTree(XSaLTDataUtils.querySQL(_oConnection, _sReportQuery), _sReportTitle, _bWriteHeaders);
							oReportStringBuffer = new StringBuffer(XSaLTXMLUtils.xmlDocumentToString(oDoc));
						}
						else if (_sReportType.equals("XS_REPORT_XML_FLAG"))
						{
							Document oReportDoc = createDataReportRootDocument();
							Element oDataNode = XSaLTXMLUtils.addSimpleTextNode(oReportDoc, null, "REPORT_DATA", null);

							XSaLTXMLUtils.resultAllRecordsFromResultSetToXMLTree(XSaLTDataUtils.querySQL(_oConnection, _sReportQuery), oReportDoc, oDataNode, "DATA_ROW", null,
									_bWriteHeaders);

							oReportStringBuffer.append(XSaLTXMLUtils.xmlDocumentToString(oReportDoc));
						}
						oPrintWriter.print(oReportStringBuffer);
						oPrintWriter.flush();
						oPrintWriter.close();
					}
					catch (Exception e)
					{
						XSaLTGenericLogger.error("Unable to get report.", e);
					}
				}
				else
				{
					Element oDataNode = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, "REPORT_DATA", null);
					XSaLTXMLUtils.resultAllRecordsFromResultSetToXMLTree(XSaLTDataUtils.querySQL(_oConnection, _sReportQuery), _oDoc, oDataNode, "DATA_ROW", null, true);
				}

			}

			if (_sDropTableName != null)
			{
				//XSaLTDataUtils.dropTableInDatabase(_oConnection, _sDropTableName);
			}

		}
		catch (SQLException e)
		{
			XSaLTGenericLogger.error("", e);
		}
		finally
		{
			XSaLTDataUtils.breakDownPooledConnection(_oConnection);
		}

		_oRequest.setAttribute(XSaLTConstants.XS_PARAMETER_FOR_REDIRECT_XSL, "/reports/ReportViewerTask");

		return _oDoc;
	}

	/**
	 * This method creates a blank DataReportDocument
	 * @return A new DataReportDocument
	 */
	protected Document createDataReportRootDocument()
	{
		DOMImplementation oDomImp = new DOMImplementationImpl();
		Document oDoc = oDomImp.createDocument(null, "XSaLTReportDocument", null);
		return oDoc;
	}

	/**
	 * This method parses the given string and returns the additional field (if found)
	 * 
	 * @param _sSearchFieldNameType
	 *            String to parse
	 * @return Additional field String (if found)
	 */
	protected String getAdditionalFieldFromWhereClause(String _sSearchFieldNameType)
	{
		String sAddlColumnName = "";
		String[] sSearchNames = _sSearchFieldNameType.split("~");
		if (sSearchNames.length == 2)
		{
			sAddlColumnName = sSearchNames[0];
		}
		return sAddlColumnName;
	}

	/**
	 * This method generates and returns an SQL WHERE clause based on the given
	 * criteria from the request.
	 * 
	 * @param _oXAuthObject
	 *            Session-level authentication object
	 * @param _sSearchFieldNameType
	 *            Field name and type
	 * @param _sSearchFieldCriteria
	 *            Criteria for field
	 * @param _sSearchStatusCriteria
	 *            Criteria to search for in 'STATUS' field
	 * @param _oTableOne
	 *            Table to search
	 * @return SQL WHERE clause for searching for records
	 */
	protected String getWhereClauseFromRequest(XSaLTAuthenticationObject _oXAuthObject, String _sSearchFieldNameType, String _sSearchFieldCriteria, String _sSearchStatusCriteria,
			XSaLTDataTableDefinition _oTableOne)
	{

		String sAutoWildCardFlag = _oXAuthObject.getValueInAuthObjectMap("AUTOWILDCARD_FLAG");
		boolean bAutoWildCardFlag = false;
		if (sAutoWildCardFlag != null && sAutoWildCardFlag.equals("1"))
		{
			bAutoWildCardFlag = true;
		}

		if (!_sSearchFieldNameType.equals("Default"))
		{

			String[] sSearchNames = _sSearchFieldNameType.split("~");
			StringBuffer oWhereClause = new StringBuffer();

			oWhereClause.append(" WHERE ");

			if (sSearchNames.length == 2)
			{

				String sFieldNameToSearch = sSearchNames[0];
				String sFieldNameType = sSearchNames[1];

				if (isLikeStringPopulated(_sSearchFieldCriteria))
				{
					//oWhereClause.append(" WHERE ");

					if (_oTableOne != null)
					{
						if (_oTableOne.hasAsColumnName(sFieldNameToSearch))
						{
							oWhereClause.append(_oTableOne.getAsColumnValue(sFieldNameToSearch));
						}
						else
						{
							oWhereClause.append(sFieldNameToSearch);
						}
					}
					else
					{
						oWhereClause.append(sFieldNameToSearch);
					}

					getLikeString(oWhereClause, sFieldNameType, _sSearchFieldCriteria, bAutoWildCardFlag);
				}

				if (!_sSearchStatusCriteria.equalsIgnoreCase("all"))
				{
					if (isLikeStringPopulated(_sSearchFieldCriteria))
					{
						oWhereClause.append(" AND ");
					}

					oWhereClause.append(" STATUS = '" + _sSearchStatusCriteria + "'");
				}

				return oWhereClause.toString();

			}
			else
			{

				if (isLikeStringPopulated(_sSearchFieldCriteria))
				{

					if (sSearchNames[0].startsWith("@"))
					{

						//oWhereClause.append(" WHERE ");
						oWhereClause.append("(");

						oWhereClause.append("concat_ws(''");

						for (int i = 0; i < (sSearchNames.length - 1); i++)
						{
							String sNewName = sSearchNames[i];
							if (sNewName.startsWith("@"))
							{
								sNewName = sNewName.substring(1, sNewName.length());
							}
							oWhereClause.append(", " + sNewName);
						}

						oWhereClause.append(")");

						String sFieldNameType = sSearchNames[sSearchNames.length - 1];
						getLikeString(oWhereClause, sFieldNameType, _sSearchFieldCriteria, bAutoWildCardFlag);
						oWhereClause.append(")");

					}
					else
					{

						//oWhereClause.append(" WHERE ");
						oWhereClause.append("(");

						for (int i = 0; i < (sSearchNames.length - 1); i++)
						{
							oWhereClause.append(sSearchNames[i]);
							String sFieldNameType = sSearchNames[sSearchNames.length - 1];
							getLikeString(oWhereClause, sFieldNameType, _sSearchFieldCriteria, bAutoWildCardFlag);
							oWhereClause.append(" OR ");
						}
						oWhereClause = new StringBuffer(oWhereClause.substring(0, oWhereClause.length() - 4));
						oWhereClause.append(")");

					}

				}

				if (!_sSearchStatusCriteria.equalsIgnoreCase("all"))
				{
					if (isLikeStringPopulated(_sSearchFieldCriteria))
					{
						oWhereClause.append(" AND ");
					}

					oWhereClause.append(" STATUS = '" + _sSearchStatusCriteria + "'");
				}

				return oWhereClause.toString();
			}

		}
		return null;
	}

	/**
	 * This method generates the LIKE string for a given field type and appends
	 * it to the specified StringBuffer.
	 * 
	 * @param _oWhereClause
	 *            StringBuffer to populate with generated SQL LIKE clause
	 * @param _sFieldNameType
	 *            Data type for field
	 * @param _sSearchFieldCriteria
	 *            Criteria to match in field
	 * @param _bOverrideDoLike
	 *            Flag if LIKE clause should contain criteria or do a case-insensitive match
	 */
	protected void getLikeString(StringBuffer _oWhereClause, String _sFieldNameType, String _sSearchFieldCriteria, boolean _bOverrideDoLike)
	{
		String sValue = XSaLTStringUtils.regExReplaceStringForInsert(_sSearchFieldCriteria);

		if (sValue == null || sValue.equals(""))
		{
			sValue = "%";
		}
		else
		{
			sValue = sValue.replace('*', '%');
		}

		if (_oWhereClause.toString().indexOf("STICKER_NOS") != -1)
		{
			if (_sSearchFieldCriteria.startsWith("="))
			{
				_oWhereClause.append(" like '" + sValue.substring(1, sValue.length()) + "'");
			}
			else
			{
				_oWhereClause.append(" like '%" + sValue + "%'");
			}
		}
		else if (_oWhereClause.toString().indexOf("LICENSE_NOS") != -1 && _sSearchFieldCriteria.startsWith("="))
		{
			if (_sSearchFieldCriteria.startsWith("="))
			{
				_oWhereClause.append(" like '" + sValue.substring(1, sValue.length()) + "'");
			}
			else
			{
				_oWhereClause.append(" like '%" + sValue + "%'");
			}
		}
		else
		{
			if (_sFieldNameType.equalsIgnoreCase("STRING"))
			{
				if (_bOverrideDoLike)
				{
					_oWhereClause.append(" like '%" + sValue + "%'");
				}
				else
				{
					_oWhereClause.append(" like '" + sValue + "'");
				}
			}
			else if (_sFieldNameType.equalsIgnoreCase("STRINGLIKE"))
			{
				_oWhereClause.append(" like '%" + sValue + "%'");
			}
			else if (_sFieldNameType.equalsIgnoreCase("BIGINT"))
			{
				_oWhereClause.append(" = '" + sValue + "'");
			}
			else if (_sFieldNameType.equalsIgnoreCase("SMALLINT"))
			{
				_oWhereClause.append(" = '" + sValue + "'");
			}
			else if (_sFieldNameType.equalsIgnoreCase("DOUBLE"))
			{
				_oWhereClause.append(" = '" + sValue + "'");
			}
		}

	}

	/**
	 * This method tests a search criteria string to see if it is empty or null
	 * and returns the results of the test (true if not empty or null).
	 * 
	 * @param _sSearchFieldCriteria
	 *            Criteria string to test
	 * @return Flag if criteria string is not empty or null
	 */
	protected boolean isLikeStringPopulated(String _sSearchFieldCriteria)
	{
		String sValue = XSaLTStringUtils.regExReplaceStringForInsert(_sSearchFieldCriteria);

		if (sValue == null || sValue.equals(""))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
