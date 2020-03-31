package com.codef.xsalt.arch.special;

import java.util.HashMap;

import org.apache.log4j.Priority;

import com.codef.xsalt.arch.XSaLTGenericLogger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTAuthenticationObject
{
	/**
	 * The generated serial ID of the object
	 */
	static final long serialVersionUID = 1214881881422209441L;

	private HashMap<String, String> ioInternalHashMap = new HashMap<String, String>();
	private HashMap<String, String> ioInternalInventorySearchHashMap = new HashMap<String, String>();
	private HashMap<String, String> ioInternalUserSearchHashMap = new HashMap<String, String>();

	/**
	 * Default constructor - This is nothing but a glorified HashMap
	 */
	public XSaLTAuthenticationObject()
	{

	}

	/**
	 * This method puts the key-value pair in the internal HashMap.
	 * 
	 * @param _sKey
	 *            Key to add
	 * @param _sValue
	 *            Value to add
	 */
	public void putInAuthObjectMap(String _sKey, String _sValue)
	{
		if (ioInternalHashMap.containsKey(_sKey))
		{
			ioInternalHashMap.remove(_sKey);
		}
		ioInternalHashMap.put(_sKey, _sValue);
	}

	/**
	 * This method removes the key-value pair in the internal HashMap based on
	 * the key value.
	 * 
	 * @param _sKey
	 *            Key to remove
	 */
	public void removeFromAuthObjectMap(String _sKey)
	{
		if (ioInternalHashMap.containsKey(_sKey))
		{
			ioInternalHashMap.remove(_sKey);
		}
	}

	/**
	 * This method returns the value from the internal HashMap for the given key.
	 * 
	 * @param _sKey
	 *            Key to search for
	 * @return String value for key in internal HashMap
	 */
	public String getValueInAuthObjectMap(String _sKey)
	{
		return ioInternalHashMap.get(_sKey);
	}
	
	/**
	 * This method returns the HashMap containing saved inventory search criteria. 
	 * 
	 * @return HashMap containing saved search criteria
	 */
	public HashMap<String, String> getInventorySearchHashMap()
	{
		return ioInternalInventorySearchHashMap;
	}
	
	/**
	 * This method sets the saved inventory search criteria values.
	 * 
	 * @param _oSearchHashMap
	 *            Map of search criteria values
	 */
	public void setInventorySearchHashMap(HashMap<String, String> _oSearchHashMap)
	{
		ioInternalInventorySearchHashMap = _oSearchHashMap;
	}
	
	/** 
	 * This method returns the HashMap containing the saved user search criteria.
	 * 
	 * @return HashMap containing saved search criteria
	 */
	public HashMap<String, String> getInternalUserSearchHashMap()
	{
		return ioInternalUserSearchHashMap;
	}
	
	/**
	 * This method sets the saved user search criteria values.
	 * 
	 * @param _oSearchHashMap
	 *            Map of search criteria values
	 */
	public void setInternalUserSearchHashMap(HashMap<String, String> _oSearchHashMap)
	{
		ioInternalUserSearchHashMap = _oSearchHashMap;
	}

	/**
	 * The method to get the client prefix
	 * 
	 * @return The client database prefix
	 */
	public String getClientPrefix()
	{
		return ioInternalHashMap.get("DB_PREFIX").toString();
	}

	/**
	 * The method to get the client prefix and year, appended by an underscore "_"
	 * 
	 * @return The client database prefix and season, joined with an underscore "_"
	 */
	public String getClientPrefixAndSeasonYear()
	{
		return ioInternalHashMap.get("DB_PREFIX").toString() + "_" + ioInternalHashMap.get("CLIENT_SEASON").toString();
	}

	/**
	 * This method returns the internal HashMap as a String.
	 * 
	 * @return String value of internal HashMap
	 */
	public String getXSaLTAuthenticationObjectAsString()
	{
		return ioInternalHashMap.toString();
	}

	/**
	 * This method logs the interal HashMap as a String.
	 */
	public void enumerateObject()
	{
		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, ioInternalHashMap.toString());
	}

}
