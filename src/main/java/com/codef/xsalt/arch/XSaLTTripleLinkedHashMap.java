package com.codef.xsalt.arch;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class XSaLTTripleLinkedHashMap<KeyClass, Value1Class, Value2Class> {

	/**
	 * List of keys for the internal HashMaps.
	 */
	ArrayList<KeyClass> ioKeyArrayList = null;
	
	/**
	 * The two internal HashMaps for holding values.
	 */
	LinkedHashMap<KeyClass, Value1Class> ioLinkedHashMapOne = null;
	LinkedHashMap<KeyClass, Value2Class> ioLinkedHashMapTwo = null;
	
	/**
	 * Default constructor.  Initializes key list and HashMaps.
	 */
	public XSaLTTripleLinkedHashMap()
	{
		ioKeyArrayList = new ArrayList<KeyClass>();
		ioLinkedHashMapOne = new LinkedHashMap<KeyClass, Value1Class>();
		ioLinkedHashMapTwo = new LinkedHashMap<KeyClass, Value2Class>();
	}
	
	/**
	 * This method puts a key-value-value triplet into the data holders.
	 * 
	 * @param _sKey
	 *            The data key
	 * @param _sValue1
	 *            First value
	 * @param _sValue2
	 *            Second value
	 */
	public void put(KeyClass _oKey, Value1Class _oValue1, Value2Class _oValue2)
	{
		ioKeyArrayList.add(_oKey);
		ioLinkedHashMapOne.put(_oKey, _oValue1);
		ioLinkedHashMapTwo.put(_oKey, _oValue2);
	}
	
	/**
	 * This method puts a key-value-value triplet into the data holders with a
	 * null value in the second value.
	 * 
	 * @param _sKey
	 *            Data key
	 * @param _sValue1
	 *            First value
	 */
	public void put(KeyClass _oKey, Value1Class _oValue1)
	{
		put(_oKey, _oValue1, null);
	}
	
	/**
	 * This method returns the value from the first internal HashMap.
	 * 
	 * @param _sKey
	 *            Key to search for
	 * @return Value from first internal HashMap for the _sKey
	 */
	public Value1Class getValueOne(KeyClass _oKey)
	{
		return ioLinkedHashMapOne.get(_oKey);
	}
	
	/**
	 * This method returns the value from the second internal HashMap.
	 * 
	 * @param _sKey
	 *            Key to search for
	 * @return Value from second internal HashMap for the _sKey
	 */
	public Value2Class getValueTwo(KeyClass _oKey)
	{
		return ioLinkedHashMapTwo.get(_oKey);
	}
	
	/**
	 * This method returns the ArrayList of keys.
	 * 
	 * @return The ArrayList of keys
	 */
	public ArrayList<KeyClass> getKeyArrayList()
	{
		return ioKeyArrayList;
	}
	
	/**
	 * This method returns the first internal HashMap.
	 * 
	 * @return The first internal HashMap
	 */
	public LinkedHashMap<KeyClass, Value1Class> getHashMapOne()
	{
		return ioLinkedHashMapOne;
	}
	
	/**
	 * This method returns the second internal HashMap.
	 * 
	 * @return The second internal HashMap
	 */
	public LinkedHashMap<KeyClass, Value2Class> getHashMapTwo()
	{
		return ioLinkedHashMapTwo;
	}
	
	
}
