package com.codef.xsalt.utils;

//import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

//import javax.servlet.http.HttpServletRequest;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTRequestUtils 
{
	
//	public static iterateParameterMap() {
//		Map<String, String[]> requestParameterMap = request.getParameterMap();
//		for (Object key : requestParameterMap.keySet()) {
//			String keyStr = (String) key;
//	
//			String[] value = (String[]) map.get(keyStr);
//			System.out.println(keyStr + " = " + Arrays.toString(value));
//			
//			String valueTwo = value[0];
//			System.out.println(keyStr + " = " + valueTwo);
//	
//		}
//	}
	
	
	/**
	 * This method will grab the request and form a special hashmap of all names, values and operations for a data table
	 * 
	 * {TABLENAME}__{OPERATION}__{PK_OR_SEQUENCE_FOR_NEW}__{FIELDNAME}__{REG_OR_PK} - OPERATION WILL BE ADD, EDIT OR DELETE
	 * 
	 * All samples below are using a table named BOBO, with the fields BOBO_PK, BOBO_NAME and BOBO_NICK
	 * 
	 * To add a record, do not specify any PK and use *ADD* as the operation, below are field names that should be on HTML page
	 * 
	 * 		BOBO__NEW__1__BOBO_NAME__REG  - The '1' here is merely a sequence
	 * 		BOBO__NEW__1__BOBO_NICK__REG
	 *
	 * To add a record, but check to see if the record is in the database before inserting, add a "UNIQUE" to the end of the
	 * field name (see example below)
	 * 
	 *      BOBO__NEW__1__BOBO_NAME__UNIQUE  - this is the field that will be checked for existing values
	 *      BOBO__NEW__1__BOBO_COLOR__REG
	 * 
	 * To edit a record, specify any PK and use *EDIT* as the operation, below are field names that should be on HTML page
	 * 
	 * 		BOBO__EDIT__1__BOBO_NAME__REG  - The '1' here is the PK value
	 * 		BOBO__EDIT__1__BOBO_NICK__REG
	 * 		BOBO__EDIT__1__BOBO_PK__PK
	 * 
	 * To delete a record, specify any PK and use *DELETE* as the operation, below are field names that should be on HTML page
	 * 
	 * 		BOBO__DELETE__1__BOBO_PK__PK  - The '1' here is the PK value
	 * 
	 * @param _oRequest The HttpRequest object to create the XSaLTDatabaseHashMap from. This is then passed onto XSaLTDataUtils.processXSaLTDatabaseHashMap() for semi-automagic processing
	 * @return The XSaLTDatabaseHashMap - this is just a glorified HashMap (really!)
	 */

//	public static HashMap<String, HashMap<String, HashMap<String, String>>> getXSaLTDatabaseHashMapFromRequest(HttpServletRequest _oRequest)
//	{
//		HashMap<String, HashMap<String, HashMap<String, String>>> oTableDataHashMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
//		
//		for (Enumeration<?> e = _oRequest.getParameterNames(); e.hasMoreElements();)
//		{
//			String sTabColPkValue = e.nextElement().toString();
//			if (sTabColPkValue.indexOf("__") != -1)
//			{
//				String as[] = sTabColPkValue.split("__");
//				if (as.length == 5)
//				{
//					String sUniqueName = as[0] + "__" + as[1] + "__" + as[2];
//					HashMap<String, String> oValueHashMap = new HashMap<String, String>();
//
//					/*
//					 oValueHashMap.put("TABLE_TO_EDIT", XSaLTStringUtils.getEmptyStringIfNull(as[0]));
//					 oValueHashMap.put("EDIT_MODE", XSaLTStringUtils.getEmptyStringIfNull(as[1]));
//					 oValueHashMap.put("EDIT_PK_OR_SEQUENCE", XSaLTStringUtils.getEmptyStringIfNull(as[2]));
//					 */
//
//					oValueHashMap.put("FIELD_NAME", XSaLTStringUtils.getEmptyStringIfNull(as[3]));
//					if (as[4].equalsIgnoreCase("PK"))
//					{
//						oValueHashMap.put("IS_PK_FIELD_FLAG", "true");
//					}
//					else
//					{
//						oValueHashMap.put("IS_PK_FIELD_FLAG", "false");
//					}
//					if (as[4].equalsIgnoreCase("UNIQUE"))
//					{
//						oValueHashMap.put("IS_UNIQUE_FLAG", "true");
//					}
//					else
//					{
//						oValueHashMap.put("IS_UNIQUE_FLAG", "false");
//					}
//					oValueHashMap.put("VALUE_OF_REQUEST_ITEM", XSaLTStringUtils
//							.getEmptyStringIfNull(_oRequest.getParameter(sTabColPkValue)).trim());
//					oValueHashMap.put("USE_QUOTES_FLAG", "true");
//
//					if (oTableDataHashMap.containsKey(sUniqueName))
//					{
//						HashMap<String, HashMap<String, String>> oTempHashMap = XSaLTObjectUtils
//								.getObjectAsHashMap_String_HashMapStringString(oTableDataHashMap.get(sUniqueName));
//						oTempHashMap.put(XSaLTStringUtils.getEmptyStringIfNull(as[3]), oValueHashMap);
//					}
//					else
//					{
//						HashMap<String, HashMap<String, String>> oTempHashMap = new HashMap<String, HashMap<String, String>>();
//						oTempHashMap.put(XSaLTStringUtils.getEmptyStringIfNull(as[3]), oValueHashMap);
//						oTableDataHashMap.put(sUniqueName, oTempHashMap);
//					}
//				}
//			}
//		}
//		return oTableDataHashMap;
//	}

	/**
	 * This method gets a particular value from a XSaLTDatabaseHashMap (glorified HashMap)
	 * 
	 * @param _oXSaLTDatabaseHashMap The XSaLTDatabaseHashMap to process
	 * @param _sSpecificInputToProcess The specific input to process
	 * @param _sFieldName The specific field name to retrieve
	 * @return The String value of the field designated in _sFieldName
	 */
	public static String getValueFromXSaLTDatabaseHashMapRequest(HashMap<String, HashMap<String, HashMap<String, String>>> _oXSaLTDatabaseHashMap, String _sSpecificInputToProcess,
			String _sFieldName)
	{
		for (Iterator<String> i = _oXSaLTDatabaseHashMap.keySet().iterator(); i.hasNext();)
		{
			String sKeyOne = (String) i.next();
			if (sKeyOne.toUpperCase().indexOf(_sSpecificInputToProcess.toUpperCase()) != -1)
			{
				HashMap<String, HashMap<String, String>> oTempHashMapOne = (HashMap<String, HashMap<String, String>>) _oXSaLTDatabaseHashMap.get(sKeyOne);
				HashMap<String, String> oTempHashMapTwo = (HashMap<String, String>) oTempHashMapOne.get(_sFieldName);
				return oTempHashMapTwo.get("VALUE_OF_REQUEST_ITEM").toString();
			}
		}
		return null;
	}

	/**
	 * This method adds a new value into the XSaLTDatabaseHashMap (glorified HashMap)
	 * 
	 * @param _oXSaLTDatabaseHashMap The XSaLTDatabaseHashMap to process
	 * @param _sSpecificInputToProcess The specific input to process
	 * @param _sFieldName The specific field name to designate
	 * @param _sValueOfField The specific field value to designate
	 * @param _bIsPkFieldFlag Whether the field is a primary key field
	 * @param _bUseQuotesFlag Whether the field uses quotes to terminate the value
	 */
	public static void addToXSaLTDatabaseHashMapRequest(HashMap<String, HashMap<String, HashMap<String, String>>> _oXSaLTDatabaseHashMap, String _sSpecificInputToProcess,
			String _sFieldName, String _sValueOfField, boolean _bIsPkFieldFlag, boolean _bUseQuotesFlag)
	{
		HashMap<String, String> oValueHashMap = new HashMap<String, String>();
		oValueHashMap.put("FIELD_NAME", XSaLTStringUtils.getEmptyStringIfNull(_sFieldName));
		if (_bIsPkFieldFlag)
		{
			oValueHashMap.put("IS_PK_FIELD_FLAG", "true");
		}
		else
		{
			oValueHashMap.put("IS_PK_FIELD_FLAG", "false");
		}
		oValueHashMap.put("VALUE_OF_REQUEST_ITEM", XSaLTStringUtils.getEmptyStringIfNull(_sValueOfField));
		if (_bUseQuotesFlag)
		{
			oValueHashMap.put("USE_QUOTES_FLAG", "true");
		}
		else
		{
			oValueHashMap.put("USE_QUOTES_FLAG", "false");
		}
		for (Iterator<String> i = _oXSaLTDatabaseHashMap.keySet().iterator(); i.hasNext();)
		{
			String sKeyOne = (String) i.next();
			if (_sSpecificInputToProcess == null || sKeyOne.toUpperCase().indexOf(_sSpecificInputToProcess.toUpperCase()) != -1)
			{
				HashMap<String, HashMap<String, String>> oTempHashMapOne = XSaLTObjectUtils
						.getObjectAsHashMap_String_HashMapStringString(_oXSaLTDatabaseHashMap.get(sKeyOne));
				oTempHashMapOne.put(_sFieldName, oValueHashMap);
			}
		}
	}
	
	

}
