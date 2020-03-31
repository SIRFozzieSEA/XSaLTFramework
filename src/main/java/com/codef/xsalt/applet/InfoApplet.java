package com.codef.xsalt.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import com.codef.xsalt.arch.XSaLTGenericLogger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com


 */
public class InfoApplet extends Applet
{
	/**
	 * The generated serial ID of the Applet
	 */
	static final long serialVersionUID = -7490532191423343365L;

	/**
	 * Flag if the applet can be run in stand alone mode
	 */
	boolean isStandalone = false;

	/**
	 * The connection URL for the XSaLTTask
	 */
	String isConnectionURL = "";

	String isTMALogoURL = "";

	/**
	 * HashMap of parameters to pass back and forth to XSaLTTask
	 */
	HashMap<String, String> ioParametersHashMap = new HashMap<String, String>();

	Image ioTmaLogoImage;

	/**
	 * Main constructor
	 */
	public InfoApplet()
	{

	}

	/**
	 * This method sets a given parameter for the XSaLTTask
	 * @param _sParamName The parameter name
	 * @param _sParamValue The parameter value
	 */
	public void setParameter(String _sParamName, String _sParamValue)
	{
		ioParametersHashMap.put(_sParamName, _sParamValue);
	}

	/**
	 * This method clears out any values in ioParametersHashMap map
	 */
	public void clearParameters()
	{
		ioParametersHashMap = new HashMap<String, String>();
	}

	/**
	 * This method gets a particular parameter value (applet only)
	 * @param key This is the key value
	 * @param def This the the definition
	 * @return The value of the key
	 */
	public String getParameter(String key, String def)
	{
		return isStandalone ? System.getProperty(key, def) : (getParameter(key) != null ? getParameter(key) : def);
	}

	/**
	 * Not really sure what the hell this does... It looks important, huh?
	 */
	public void doWork()
	{

	}

	/**
	 * Initialize the applet
	 */
	public void init()
	{
		this.setBackground(Color.gray);

		try
		{
			isConnectionURL = getParameter("sConnectionURL", "");
			ioTmaLogoImage = getImage(new URL(getParameter("sTMALogoURL", "")));
		}
		catch (Exception e)
		{
			XSaLTGenericLogger.error("", e);
		}
	}

	/**
	 * Start the applet
	 */
	public void start()
	{

	}

	/**
	 * Stop the applet
	 */
	public void stop()
	{
	}

	/**
	 * Destroy the applet
	 */
	public void destroy()
	{
	}

	/**
	 * Get Applet information
	 */
	public String getAppletInfo()
	{
		return "InfoApplet";
	}

	/**
	 * Get parameter info
	 */
	public String[][] getParameterInfo()
	{
		java.lang.String[][] asParameterInfo = { { "sConnectionURL", "String", "The server connection" }, { "sTMALogoURL", "String", "The URL to TMAs corner logo" },
				{ "sInfoAppletTask", "String", "The task to execute" }, };
		return asParameterInfo;
	}

	/**
	 * This method does the actual talking to the XSaLTTask via applet-to-servlet communication
	 * 
	 * @return The value that comes from the XSaLTTask
	 */
	public String doTaskReturnString()
	{
		String sReturnString = "";
		showStatus("Starting request to InfoApplet");

		try
		{
			URL oInfoTaskServlet = new URL(isConnectionURL + ioParametersHashMap.get("TASK_NAME").toString());
			URLConnection oServletConnection = oInfoTaskServlet.openConnection();

			oServletConnection.setDoInput(true);
			oServletConnection.setDoOutput(true);

			oServletConnection.setUseCaches(false);
			oServletConnection.setDefaultUseCaches(false);

			oServletConnection.setRequestProperty("Content-Type", "application/octet-stream");
			ObjectOutputStream outputToServlet = new ObjectOutputStream(oServletConnection.getOutputStream());
			outputToServlet.writeObject(ioParametersHashMap);
			outputToServlet.flush();
			outputToServlet.close();

			ObjectInputStream inputFromServlet = new ObjectInputStream(oServletConnection.getInputStream());
			sReturnString = (String) inputFromServlet.readObject();

		}
		catch (Exception e)
		{
			XSaLTGenericLogger.error("", e);
		}

		showStatus("Received data from InfoApplet");
		return sReturnString;

	}

	/**
	 * This method re-draws the applet.
	 */
	public void paint(Graphics g)
	{
		g.drawImage(ioTmaLogoImage, 0, 0, 65, 55, this);
	}

}
