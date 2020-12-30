package com.codef.xsalt.arch;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTTripleStringLinkedHashMap
{

	/**
	 * List of keys for the internal HashMaps.
	 */
	ArrayList<String> ioKeyArrayList = null;
	/**
	 * The two internal HashMaps for holding values.
	 */
	LinkedHashMap<String, String> ioLinkedHashMapOne = null;
	LinkedHashMap<String, String> ioLinkedHashMapTwo = null;

	/**
	 * Default constructor.  Initializes key list and HashMaps.
	 */
	public XSaLTTripleStringLinkedHashMap()
	{
		ioKeyArrayList = new ArrayList<String>();
		ioLinkedHashMapOne = new LinkedHashMap<String, String>();
		ioLinkedHashMapTwo = new LinkedHashMap<String, String>();
	}

	/**
	 * This method puts a key-value-value triplet into the data holders.
	 * 
	 * @param _sKey
	 *            The data key
	 * @param _sValueOne
	 *            First value
	 * @param _sValueTwo
	 *            Second value
	 */
	public void put(String _sKey, String _sValueOne, String _sValueTwo)
	{
		ioKeyArrayList.add(_sKey);
		ioLinkedHashMapOne.put(_sKey, _sValueOne);
		ioLinkedHashMapTwo.put(_sKey, _sValueTwo);
	}
	
	/**
	 * This method puts a key-value-value triplet into the data holders with a
	 * blank value in the second value.
	 * 
	 * @param _sKey
	 *            Data key
	 * @param _sValueOne
	 *            First value
	 */
	public void put(String _sKey, String _sValueOne)
	{
		ioKeyArrayList.add(_sKey);
		ioLinkedHashMapOne.put(_sKey, _sValueOne);
		ioLinkedHashMapTwo.put(_sKey, "");
	}

	/**
	 * This method returns the value from the first internal HashMap.
	 * 
	 * @param _sKey
	 *            Key to search for
	 * @return Value from first internal HashMap for the _sKey
	 */
	public String getValueOneValue(String _sKey)
	{
		return ioLinkedHashMapOne.get(_sKey);
	}

	/**
	 * This method returns the value from the second internal HashMap.
	 * 
	 * @param _sKey
	 *            Key to search for
	 * @return Value from second internal HashMap for the _sKey
	 */
	public String getValueTwoValue(String _sKey)
	{
		return ioLinkedHashMapTwo.get(_sKey);
	}

	/**
	 * This method returns the ArrayList of keys.
	 * 
	 * @return The ArrayList of keys
	 */
	public ArrayList<String> getKeyArrayList()
	{
		return ioKeyArrayList;
	}

	/**
	 * This method returns the first internal HashMap.
	 * 
	 * @return The first internal HashMap
	 */
	public LinkedHashMap<String, String> getHashMapOne()
	{
		return ioLinkedHashMapOne;
	}

	/**
	 * This method returns the second internal HashMap.
	 * 
	 * @return The second internal HashMap
	 */
	public LinkedHashMap<String, String> getHashMapTwo()
	{
		return ioLinkedHashMapTwo;
	}

}
