package com.codef.xsalt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTObjectUtils {

	/*
	
	public static void introspectObject(String _sClassURI) throws ClassNotFoundException
	{

		Class cls = Class.forName(_sClassURI);
		Constructor ctorlist[] = cls.getDeclaredConstructors();
		for (int i = 0; i < ctorlist.length; i++)
		{
			Constructor ct = ctorlist[i];
			XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "name  = " + ct.getName());
			XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "decl class = " + ct.getDeclaringClass());
			Class pvec[] = ct.getParameterTypes();
			for (int j = 0; j < pvec.length; j++)
				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "param #" + j + " " + pvec[j]);
			Class evec[] = ct.getExceptionTypes();
			for (int j = 0; j < evec.length; j++)
				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "exc #" + j + " " + evec[j]);
			XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "-----");
		}

	}
	
	*/
	
	/**
	 * This method concatenates each string from an array in to a single delimited
	 * string.
	 * 
	 * @param _asString   Array of strings to make into CSV
	 * @param _sDelimiter Delimiter character for CSV
	 * @return Delimited string containing each member of the array
	 */
	public static String getStringArrayWithDelimiter_String(String _asString[], String _sDelimiter) {
		StringBuffer oTempBuffer = new StringBuffer();
		for (int j = 0; j < _asString.length; j++) {
			oTempBuffer.append(_asString[j] + _sDelimiter);
		}
		oTempBuffer = new StringBuffer(
				oTempBuffer.toString().substring(0, oTempBuffer.toString().length() - _sDelimiter.length()));
		return oTempBuffer.toString();
	}

	/**
	 * This method casts the specified object to an ArrayList<String>.
	 * 
	 * @param _oConvertObject Object to convert
	 * @return ArrayList<String> converted from the incoming object
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getObjectAsArrayList_String(Object _oConvertObject) {
		ArrayList<String> oTempArrayList = (ArrayList<String>) _oConvertObject;
		return oTempArrayList;
	}

	/**
	 * This method casts the specified object to a HashMap<String, Long>.
	 * 
	 * @param _oConvertObject Object to convert
	 * @return HashMap<String, Long> converted from incoming object
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Long> getObjectAsHashMap_String_Long(Object _oConvertObject) {
		HashMap<String, Long> oTempHashMap = (HashMap<String, Long>) _oConvertObject;
		return oTempHashMap;
	}

	/**
	 * This method casts the specified object to a HashMap<String, String>.
	 * 
	 * @param _oConvertObject Object to convert
	 * @return HashMap<String, String> converted from incoming object
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getObjectAsHashMap_String_String(Object _oConvertObject) {
		HashMap<String, String> oTempHashMap = (HashMap<String, String>) _oConvertObject;
		return oTempHashMap;
	}

	/**
	 * This method casts the specified object to a HashMap<String, HashMap<String,
	 * String>>.
	 * 
	 * @param _oConvertObject Object to convert
	 * @return HashMap<String, HashMap<String, String>> converted from incoming
	 *         object
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, String>> getObjectAsHashMap_String_HashMapStringString(
			Object _oConvertObject) {
		HashMap<String, HashMap<String, String>> oTempHashMap = (HashMap<String, HashMap<String, String>>) _oConvertObject;
		return oTempHashMap;
	}

	/**
	 * This method iterates through a HashMap, putting each key-value pair into a
	 * string.
	 * 
	 * @param _oTestMap    Map to list
	 * @param _sIndentChar Character to put at the beginning of each line
	 * @return String containing
	 */
	public static String printHashMap(HashMap<String, String> _oTestMap, String _sIndentChar) {
		StringBuffer oTestBuffer = new StringBuffer("\n");
		for (Iterator<String> j = _oTestMap.keySet().iterator(); j.hasNext();) {
			String sKey = (String) j.next();
			String sValue = (String) _oTestMap.get(sKey);
			oTestBuffer.append(_sIndentChar + sKey + " = '" + sValue + "'\n");
		}
		return oTestBuffer.toString();
	}

	/**
	 * This method iterates through a ArrayListHashMap, printing the size and the
	 * rows on a separate line each
	 * 
	 * @param _oAlhm       Map to list
	 * @param _sIndentChar Character to put at the beginning of each line
	 * @return String containing
	 */
	public static String printArrayListHashMap(ArrayList<HashMap<String, String>> _oAlhm, String _sIndentChar) {

		StringBuffer oTestBuffer = new StringBuffer("ArrayListHashMap size: " + _oAlhm.size() + "\n");
		for (HashMap<String, String> line : _oAlhm) {
			oTestBuffer.append(_sIndentChar + line + "'\n");
		}
		return oTestBuffer.toString();
	}

}
