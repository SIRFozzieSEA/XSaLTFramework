package com.codef.xsalt.utils;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.codef.xsalt.arch.XSaLTTripleStringLinkedHashMap;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTXMLUtils {
	/**
	 * This method loads a give file into an XML Document
	 * 
	 * @param _sPathToXmlFile The path to the file you wish to load
	 * @return The XML document
	 */
	public static Document loadXMLDocumentFromFile(String _sPathToXmlFile) {
		Document o_doc = XSaLTFileSystemUtils.getXMLFromPathFile(_sPathToXmlFile);
		return o_doc;
	}

	/**
	 * This method gets the first element with the given name and returns the value
	 * of that node.
	 * 
	 * @param _oElement     Element to search under
	 * @param _sElementName Element to search for
	 * @return Value of first instance of given element
	 */
	public static String getElementFirstStringValue(Element _oElement, String _sElementName) {
		String sTempString = "";
		if (_oElement.getElementsByTagName(_sElementName).item(0).getFirstChild() != null) {
			sTempString = _oElement.getElementsByTagName(_sElementName).item(0).getFirstChild().getNodeValue()
					.toString();
		}
		return sTempString;
	}

	/**
	 * This method gets the tag for the label name
	 * 
	 * @param _oNode      Node to search under
	 * @param _sLabelName Element to search for
	 * @return Value of first instance of given element
	 */
	public static String getLabelEntry(Node _oNode, String _sLabelName) {
		if (_oNode.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) _oNode;
			return getTagValue(element, _sLabelName);
		}
		return null;
	}

	/**
	 * This method gets the tag for the label name
	 * 
	 * @param _oElement  Node to search under
	 * @param _sTagsName Element to search for
	 * @return Value of first instance of given element
	 */
	public static String getTagValue(Element _oElement, String _sTagsName) {
		NodeList nodeList = _oElement.getElementsByTagName(_sTagsName).item(0).getChildNodes();
		if (nodeList.item(0) == null) {
			return "";
		} else {
			return nodeList.item(0).getNodeValue();
		}
	}

	/**
	 * This method adds a simple text node to a given element and also returns the
	 * element it creates
	 * 
	 * @param _oDoc           The XML document you want to attatch to
	 * @param _oParentElement The parent element you wish to attatch to
	 * @param _sNodeName      The name of the new element
	 * @param _sNodeValue     The value of the new element
	 * @return The new text element node that was created
	 */
	public static Element addSimpleTextNode(Document _oDoc, Element _oParentElement, String _sNodeName,
			String _sNodeValue) {
		Element oElement = _oDoc.createElement(_sNodeName);
		Text oText = _oDoc.createTextNode(_sNodeValue);
		oElement.appendChild(oText);
		if (_oParentElement == null) {
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.appendChild(oElement);
		} else {
			_oParentElement.appendChild(oElement);
		}
		return oElement;
	}

	/**
	 * This method adds a (relatively) simple text node to a given element with the
	 * desired attribute and returns the element it creates.
	 * 
	 * @param _oDoc            The XML document you want to attach to.
	 * @param _oParentElement  The parent element you wish to attach to.
	 * @param _sNodeName       The name of the new element.
	 * @param _sNodeValue      The text value of the new element.
	 * @param _sAttributeName  The name of the attribute for the element.
	 * @param _sAttributeValue The text value of the attribute.
	 * @return The text element that was created
	 */
	public static Element addTextNodeWithAttribute(Document _oDoc, Element _oParentElement, String _sNodeName,
			String _sNodeValue, String _sAttributeName, String _sAttributeValue) {
		Element oElement = _oDoc.createElement(_sNodeName);
		oElement.setAttribute(_sAttributeName, _sAttributeValue);
		Text oText = _oDoc.createTextNode(_sNodeValue);
		oElement.appendChild(oText);
		if (_oParentElement == null) {
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.appendChild(oElement);
		} else {
			_oParentElement.appendChild(oElement);
		}

		return oElement;
	}

	/**
	 * This method adds an attribute node to a given element and also returns the
	 * element it creates
	 * 
	 * @param _oDoc            The XML document you want to attatch to
	 * @param _oParentElement  The parent element you wish to attatch to
	 * @param _sAttributeName  The name of the new element
	 * @param _sAttributeValue The value of the new element
	 */
	public static void addSimpleAttributeNode(Document _oDoc, Element _oParentElement, String _sAttributeName,
			String _sAttributeValue) {
		Attr oAttribute = _oDoc.createAttribute(_sAttributeName);
		oAttribute.setValue(_sAttributeValue);
		if (_oParentElement == null) {
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.setAttributeNode(oAttribute);
		} else {
			_oParentElement.setAttributeNode(oAttribute);
		}
	}

	/**
	 * This method returns the column names, types and counts
	 * 
	 * @param _oRs The ResultSet to examine
	 * @return The HashMap of special column information
	 * @throws SQLException
	 */
	private static HashMap<String, Serializable> getColumnNamesAndCountHashMap(ResultSet _oRs) throws SQLException {
		ResultSetMetaData oRsmd = _oRs.getMetaData();
		HashMap<Integer, String> oColumnNameHashmap = new HashMap<Integer, String>();
		HashMap<Integer, String> oColumnTypeHashmap = new HashMap<Integer, String>();

		int n_column_count = oRsmd.getColumnCount();
		if (n_column_count > 0) {
			for (int i = 1; i <= n_column_count; i++) {
				Integer oColumnIndex = Integer.valueOf(i);
				String sColumnName = oRsmd.getColumnName(i).toUpperCase();
				String sColumnTypename = oRsmd.getColumnTypeName(i).toUpperCase();
				oColumnNameHashmap.put(oColumnIndex, sColumnName);
				oColumnTypeHashmap.put(oColumnIndex, sColumnTypename);
			}

		}

		HashMap<String, Serializable> oNamesAndCountHashMap = new HashMap<String, Serializable>();
		oNamesAndCountHashMap.put("COLUMN_NAMES", oColumnNameHashmap);
		oNamesAndCountHashMap.put("COLUMN_TYPES", oColumnTypeHashmap);
		oNamesAndCountHashMap.put("COLUMN_COUNT", Integer.valueOf(n_column_count).toString());
		return oNamesAndCountHashMap;

	}

	/**
	 * This method attaches the column name, tye and data onto a given parent leaf
	 * node
	 * 
	 * @param _oRs                The ResultSet to query
	 * @param _oColumnNameHashmap The HashMap of column names
	 * @param _oColumnTypeHashmap The HashMap of column types
	 * @param _nColumnPosition    The column position (starting at 1, as JDBC does)
	 * @param _oDoc               The XML document you wish to attach to
	 * @param _oParentNode        The parent node for the data leaf
	 * @param _bMakeHeaderRow     Flag if this is a header row
	 * @return The created element with the data attached
	 * @throws SQLException
	 */
	private static Element attachColumnDataToRecordNode(ResultSet _oRs, HashMap<Integer, String> _oColumnNameHashmap,
			HashMap<Integer, String> _oColumnTypeHashmap, int _nColumnPosition, Document _oDoc, Element _oParentNode,
			boolean _bMakeHeaderRow) throws SQLException {

		Element oReturnElement = null;
		String sColumnName = _oColumnNameHashmap.get(Integer.valueOf(_nColumnPosition)).toString();
		String sValueOfColumn = _oRs.getString(_nColumnPosition);

		if (_bMakeHeaderRow) {
			sColumnName = "COLUMN_NAME";
		}

		if (_oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase()
				.indexOf(".TIMESTAMP") != -1
				|| _oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase()
						.indexOf("TIMESTAMP") != -1) {
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatTimeStampFromMySQLToReadableTimeStamp(sValueOfColumn));
		} else if (_oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase()
				.indexOf(".DATETIME") != -1
				|| _oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase()
						.indexOf("DATETIME") != -1) {
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatTimeStampFromMySQLToReadableTimeStamp(sValueOfColumn));
		} else if (_oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase()
				.indexOf(".DATE") != -1
				|| _oColumnTypeHashmap.get(Integer.valueOf(_nColumnPosition)).toString().toUpperCase().equals("DATE")) {
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatDateFromMySQLToReadableDate(sValueOfColumn));
		} else {
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName, sValueOfColumn);
		}
		return oReturnElement;
	}

	/**
	 * This method creates a standard select data node, only first two values are
	 * recorded as the key and value
	 * 
	 * @param _oRs               The ResultSet to use as the data
	 * @param _oDoc              The XML document you wish to append to
	 * @param _sSelectNodeName   The name of the select node
	 * @param _oAdditionalFields The additional fields you wish append
	 * @throws SQLException
	 */
	public static void createSelectNode(ResultSet _oRs, Document _oDoc, String _sSelectNodeName,
			ArrayList<String> _oAdditionalFields, boolean _bAddBlankNode) throws SQLException {
		Element oSelectNodeElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, _sSelectNodeName, null);
		int nRowCount = 0;

		if (_bAddBlankNode) {
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = "";
			String sValue = "";
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder",
					Integer.valueOf(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			nRowCount = nRowCount + 1;
		}

		while (_oRs.next()) {
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = XSaLTStringUtils.getEmptyStringIfNull(_oRs.getString(1));
			String sValue = XSaLTStringUtils.getEmptyStringIfNull(_oRs.getString(2));
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder",
					Integer.valueOf(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			if (_oAdditionalFields != null) {
				for (int i = 0; i < _oAdditionalFields.size(); i++) {
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, _oAdditionalFields.get(i).toString(),
							_oRs.getString(_oAdditionalFields.get(i).toString()));
				}
			}
			nRowCount = nRowCount + 1;
		}
	}

	/**
	 * This method creates a node to represent an HTML SELECT input.
	 * 
	 * @param _oDoc                       XML document to add node to
	 * @param _sSelectNodeName            Name for created SELECT input
	 * @param _oTripleStringLinkedHashMap Data to use to populate the SELECT input
	 * @param _bAddBlankNode              Flag if a blank OPTION should be created
	 *                                    before the actual options
	 * @throws SQLException
	 */
	public static void createSelectNode(Document _oDoc, String _sSelectNodeName,
			XSaLTTripleStringLinkedHashMap _oTripleStringLinkedHashMap, boolean _bAddBlankNode) throws SQLException {
		Element oSelectNodeElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, _sSelectNodeName, null);
		int nRowCount = 0;

		if (_bAddBlankNode) {
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = "";
			String sValue = "";
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder",
					Integer.valueOf(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			nRowCount = nRowCount + 1;
		}

		ArrayList<String> oKeyArrayList = _oTripleStringLinkedHashMap.getKeyArrayList();
		for (int i = 0; i < oKeyArrayList.size(); i++) {
			String sKey = oKeyArrayList.get(i).toString();
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey2 = XSaLTStringUtils.getEmptyStringIfNull(_oTripleStringLinkedHashMap.getValueOneValue(sKey));
			String sValue = XSaLTStringUtils.getEmptyStringIfNull(_oTripleStringLinkedHashMap.getValueTwoValue(sKey));
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder", sValue);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey2);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sKey);
		}

	}

	/**
	 * This method grabs the first record from a ResultSet and creates a data tree
	 * 
	 * @param _oRs         The ResultSet to query
	 * @param _oDoc        The XML document to attatch the data to
	 * @param _oParentNode The parent node to attatch the data to
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultFirstRecordFromResultSetToXMLTree(ResultSet _oRs, Document _oDoc, Element _oParentNode)
			throws SQLException {
		if (_oRs.next()) {
			HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
			HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
					.get("COLUMN_NAMES");
			HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
					.get("COLUMN_TYPES");
			int n_column_count = Integer.valueOf(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();
			for (int i = 1; i <= n_column_count; i++) {
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, _oParentNode,
						false);
			}
		}
	}

	/**
	 * This method grabs all records from a ResultSet and creates a data tree
	 * 
	 * @param _oRs                   The ResultSet to query
	 * @param _oDoc                  The XML document to attatch the data to
	 * @param _oParentNode           The parent node to attatch the data to
	 * @param _sResultRowName        The name you want the row/record to be labelled
	 *                               in the tree
	 * @param _sPrimaryKeyColumnName The name of the primary key column in the
	 *                               database
	 * @param _bMakeHeaderRow        Flag if this is a header row
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultAllRecordsFromResultSetToXMLTree(ResultSet _oRs, Document _oDoc, Element _oParentNode,
			String _sResultRowName, String _sPrimaryKeyColumnName, boolean _bMakeHeaderRow) throws SQLException {

		HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
		HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
				.get("COLUMN_NAMES");
		HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
				.get("COLUMN_TYPES");
		int n_column_count = Integer.valueOf(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();

		Element oHeaderRow = null;
		if (_bMakeHeaderRow) {
			oHeaderRow = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName + "_HEADER", "");
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_COUNT",
					Integer.valueOf(n_column_count).toString());
		}

		if (n_column_count > 0) {
			for (int i = 1; i <= n_column_count; i++) {
				String sColumnName = oColumnNameHashmap.get(Integer.valueOf(i)).toString().toUpperCase();
				if (_bMakeHeaderRow) {
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_HEADER_NAME", sColumnName);
				}
			}
		}

		int nRecordCount = 1;

		while (_oRs.next()) {
			Element oRowValueOne = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName, "");
			if (_sPrimaryKeyColumnName != null) {
				XSaLTXMLUtils.addSimpleAttributeNode(_oDoc, oRowValueOne, _sPrimaryKeyColumnName,
						_oRs.getString(_sPrimaryKeyColumnName));
			}

			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, "SORT_ORDER",
					XSaLTStringUtils.padLeftWithCharacter(Integer.valueOf(nRecordCount).toString(), '0', 9));

			for (int i = 1; i <= n_column_count; i++) {
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, oRowValueOne,
						_bMakeHeaderRow);
			}

			_oParentNode.appendChild(oRowValueOne);
			nRecordCount = nRecordCount + 1;
		}
	}

	/**
	 * This method grabs all records from a ResultSet and creates a data tree
	 * 
	 * @param _oRs                   The ResultSet to query
	 * @param _oDoc                  The XML document to attatch the data to
	 * @param _oParentNode           The parent node to attatch the data to
	 * @param _sResultRowName        The name you want the row/record to be labelled
	 *                               in the tree
	 * @param _sPrimaryKeyColumnName The name of the primary key column in the
	 *                               database
	 * @param _bMakeHeaderRow        Flag if this is a header row
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultAllRecordsFromResultSetToXMLTreeWithExtraNodes(ResultSet _oRs, Document _oDoc,
			Element _oParentNode, String _sResultRowName, String _sPrimaryKeyColumnName, boolean _bMakeHeaderRow,
			HashMap<String, String> _oExtraNodesMap) throws SQLException {

		HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
		HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
				.get("COLUMN_NAMES");
		HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap
				.get("COLUMN_TYPES");
		int n_column_count = Integer.valueOf(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();

		Element oHeaderRow = null;
		if (_bMakeHeaderRow) {
			oHeaderRow = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName + "_HEADER", "");
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_COUNT",
					Integer.valueOf(n_column_count).toString());
		}

		if (n_column_count > 0) {
			for (int i = 1; i <= n_column_count; i++) {
				String sColumnName = oColumnNameHashmap.get(Integer.valueOf(i)).toString().toUpperCase();
				if (_bMakeHeaderRow) {
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_HEADER_NAME", sColumnName);
				}
			}
		}

		int nRecordCount = 1;

		while (_oRs.next()) {
			Element oRowValueOne = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName, "");
			if (_sPrimaryKeyColumnName != null) {
				XSaLTXMLUtils.addSimpleAttributeNode(_oDoc, oRowValueOne, _sPrimaryKeyColumnName,
						_oRs.getString(_sPrimaryKeyColumnName));
			}

			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, "SORT_ORDER",
					XSaLTStringUtils.padLeftWithCharacter(Integer.valueOf(nRecordCount).toString(), '0', 9));

			for (int i = 1; i <= n_column_count; i++) {
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, oRowValueOne,
						_bMakeHeaderRow);
			}

			for (Iterator<String> j = _oExtraNodesMap.keySet().iterator(); j.hasNext();) {
				String sKeyName = (String) j.next();
				String sValue = _oExtraNodesMap.get(sKeyName);
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, sKeyName, sValue);
			}

			_oParentNode.appendChild(oRowValueOne);
			nRecordCount = nRecordCount + 1;
		}
	}

	// TODO: XSaLTXMLUtils Fix (when time permits)

//	/**
//	 * This method grabs all information from a result set and puts it into a Excel-type
//	 * XML tree.
//	 * 
//	 * @param _oRs
//	 *            The result set to put into XML
//	 * @param _sSheetName
//	 *            The name to give to the Excel work sheet
//	 * @param _bWriteHeader
//	 *            Flag if column headers should be written
//	 * @return An XML document for Excel
//	 * @throws SQLException
//	 */
//	public static Document exportSQLAsExcelXMLTree(ResultSet _oRs,  String _sSheetName, boolean _bWriteHeader) throws SQLException
//	{
//		DOMImplementation oDomImp = new DOMImplementationImpl();		
//		Document oDoc = oDomImp.createDocument("urn:schemas-microsoft-com:office:spreadsheet", "ss:Workbook", null);
//		
//		ArrayList<String> oColumnNames = new ArrayList<String>();
//		ArrayList<String> oColumnTypes = new ArrayList<String>();
//		
//		ResultSetMetaData oRsMd = _oRs.getMetaData();
//		// ResultSet & ResultSetMetaData column indices start at 1
//		for (int i = 1; i <= oRsMd.getColumnCount(); i++)
//		{
//			oColumnNames.add(oRsMd.getColumnName(i));
//			oColumnTypes.add(oRsMd.getColumnTypeName(i));
//		}
//		
//		Element oRootElement = oDoc.getDocumentElement();
//		oRootElement.setAttribute("xmlns:ss", "urn:schemas-microsoft-com:office:spreadsheet");
//		
//		Element oWorksheet = addTextNodeWithAttribute(oDoc, oRootElement, "ss:Worksheet", null, "ss:Name",
//				(_sSheetName == null || _sSheetName.trim().equals("") ? "Sheet1" : _sSheetName));		
//		Element oTable = XSaLTXMLUtils.addSimpleTextNode(oDoc, oWorksheet, "ss:Table", null);
//		
//		if (_bWriteHeader)
//		{
//			Element oRow = XSaLTXMLUtils.addSimpleTextNode(oDoc, oTable, "ss:Row", null);
//			for (int i = 0; i < oColumnNames.size(); i++)
//			{
//				Element oCell = addSimpleTextNode(oDoc, oRow, "ss:Cell", null);
//				
//				addTextNodeWithAttribute(oDoc, oCell, "ss:Data", oColumnNames.get(i).toUpperCase(), "ss:Type", "String");
//			}
//		}
//		
//		while (_oRs.next())
//		{
//			Element oRow = addSimpleTextNode(oDoc, oTable, "ss:Row", null);
//			for (int i = 0; i < oColumnNames.size(); i++)
//			{
//				Element oCell = addSimpleTextNode(oDoc, oRow, "ss:Cell", null);
//				
//				String sColumnType = oColumnTypes.get(i).toUpperCase();
//				String sDataType = "String";
//				if (sColumnType.contains("INT") || sColumnType.contains("DOUBLE"))
//				{
//					sDataType = "Number";
//				}
//				else if (sColumnType.contains("DATE"))
//				{
//					sDataType = "DateTime";
//				}
//				
//				addTextNodeWithAttribute(oDoc, oCell, "ss:Data", _oRs.getString(oColumnNames.get(i)), "ss:Type", sDataType);				
//			}
//		}
//		
//		return oDoc;
//	}

//	
//	
//	/**
//	 * This method converts an XML node to a String
//	 * 
//	 * @param _oDoc The XML node you want to convert
//	 * @return The String representation of the node
//	 * @throws IOException
//	 */
//	public static String nodeToString(Node node) throws Exception {
//		StringWriter sw = new StringWriter();
//
//		Transformer t = TransformerFactory.newInstance().newTransformer();
//		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//		t.setOutputProperty(OutputKeys.INDENT, "yes");
//		t.transform(new DOMSource(node), new StreamResult(sw));
//
//		return sw.toString();
//	}
//	
//	
//
//	/**
//	 * This method converts an XML document to a StringBuffer
//	 * 
//	 * @param _oDoc The XML document you want to convert
//	 * @return The String representation of the document
//	 * @throws IOException
//	 */
//	public static String xmlDocumentToString(Document _oDoc)
//	{
//		OutputFormat oOutputFormat = new OutputFormat(_oDoc);
//		StringWriter oStringWriter = new StringWriter();
//
//		try
//		{
//			XMLSerializer oXmlSerializer = new XMLSerializer(oStringWriter, oOutputFormat);
//			oXmlSerializer.asDOMSerializer();
//			oXmlSerializer.serialize(_oDoc.getDocumentElement());
//		}
//		catch (IOException e)
//		{
//			XSaLTGenericLogger.logXSaLT(Priority.WARN_INT, "XML Document may have extended characters in it.", e);
////			
//		}
//
//		return oStringWriter.getBuffer().toString();
//	}
//

//	/**
//	 * This method returns an XPathResult of an XPath query
//	 * 
//	 * @param _oDoc The XML document you are querying
//	 * @param _oElement The element you wish to begin at
//	 * @param _sXPathQueryString The XPath query
//	 * @return The XPathResult (nodes and such)
//	 */
//	public static XPathResult getXPathResult(Document _oDoc, Element _oElement, String _sXPathQueryString)
//	{
//		XPathEvaluator oXpathEvaluator = new XPathEvaluatorImpl(_oDoc);
//		XPathNSResolver oXpathResolver = oXpathEvaluator.createNSResolver(_oElement);
//		XPathResult oXpathResult = (XPathResult) oXpathEvaluator.evaluate(_sXPathQueryString, _oDoc, oXpathResolver,
//				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
//		return oXpathResult;
//	}
//

}
