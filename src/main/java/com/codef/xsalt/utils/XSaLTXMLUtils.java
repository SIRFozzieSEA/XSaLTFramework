package com.codef.xsalt.utils;

import java.io.File;
import org.apache.log4j.Priority;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.domapi.XPathEvaluatorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;
import org.xml.sax.SAXException;

import com.codef.xsalt.arch.XSaLTGenericLogger;
import com.codef.xsalt.arch.special.XSaLTDataColumnDefinition;
import com.codef.xsalt.arch.special.XSaLTTripleStringLinkedHashMap;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTXMLUtils
{
	/**
	 * This method loads a give file into an XML Document
	 * 
	 * @param _sPathToXmlFile The path to the file you wish to load
	 * @return The XML document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static Document loadXMLDocumentFromFile(String _sPathToXmlFile) throws ParserConfigurationException, SAXException, IOException
	{
		Document o_doc = null;
		DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		oDocumentBuilderFactory.setNamespaceAware(true);
		File oFile = new File(_sPathToXmlFile);
		DocumentBuilder oDocBuilder = oDocumentBuilderFactory.newDocumentBuilder();
		o_doc = oDocBuilder.parse(oFile);
		return o_doc;
	}

	/**
	 * This method gets the first element with the given name and returns the
	 * value of that node.
	 * 
	 * @param _oElement
	 *            Element to search under
	 * @param _sElementName
	 *            Element to search for
	 * @return Value of first instance of given element
	 */
	public static String getElementFirstStringValue(Element _oElement, String _sElementName)
	{
		String sTempString = "";
		if (_oElement.getElementsByTagName(_sElementName).item(0).getFirstChild() != null)
		{
			sTempString = _oElement.getElementsByTagName(_sElementName).item(0).getFirstChild().getNodeValue().toString();
		}
		return sTempString;
	}
	
	
	/**
	 * This method converts an XML node to a String
	 * 
	 * @param _oDoc The XML node you want to convert
	 * @return The String representation of the node
	 * @throws IOException
	 */
	public static String nodeToString(Node node) throws Exception {
		StringWriter sw = new StringWriter();

		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));

		return sw.toString();
	}
	
	

	/**
	 * This method converts an XML document to a StringBuffer
	 * 
	 * @param _oDoc The XML document you want to convert
	 * @return The String representation of the document
	 * @throws IOException
	 */
	public static String xmlDocumentToString(Document _oDoc)
	{
		OutputFormat oOutputFormat = new OutputFormat(_oDoc);
		StringWriter oStringWriter = new StringWriter();

		try
		{
			XMLSerializer oXmlSerializer = new XMLSerializer(oStringWriter, oOutputFormat);
			oXmlSerializer.asDOMSerializer();
			oXmlSerializer.serialize(_oDoc.getDocumentElement());
		}
		catch (IOException e)
		{
			XSaLTGenericLogger.logXSaLT(Priority.WARN_INT, "XML Document may have extended characters in it.", e);
//			
		}

		return oStringWriter.getBuffer().toString();
	}

	/**
	 * This method adds a simple text node to a given element and also returns the element it creates
	 * 
	 * @param _oDoc	The XML document you want to attatch to
	 * @param _oParentElement The parent element you wish to attatch to
	 * @param _sNodeName The name of the new element
	 * @param _sNodeValue The value of the new element
	 * @return The new text element node that was created
	 */
	public static Element addSimpleTextNode(Document _oDoc, Element _oParentElement, String _sNodeName, String _sNodeValue)
	{
		Element oElement = _oDoc.createElement(_sNodeName);
		Text oText = _oDoc.createTextNode(_sNodeValue);
		oElement.appendChild(oText);
		if (_oParentElement == null)
		{
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.appendChild(oElement);
		}
		else
		{
			_oParentElement.appendChild(oElement);
		}
		return oElement;
	}
	
	/**
	 * This method adds a (relatively) simple text node to a given element with the desired
	 * attribute and returns the element it creates.
	 * 
	 * @param _oDoc
	 *            The XML document you want to attach to.
	 * @param _oParentElement
	 *            The parent element you wish to attach to.
	 * @param _sNodeName
	 *            The name of the new element.
	 * @param _sNodeValue
	 *            The text value of the new element.
	 * @param _sAttributeName
	 *            The name of the attribute for the element.
	 * @param _sAttributeValue
	 *            The text value of the attribute.
	 * @return The text element that was created
	 */
	public static Element addTextNodeWithAttribute(Document _oDoc, Element _oParentElement, String _sNodeName, String _sNodeValue,
			String _sAttributeName, String _sAttributeValue)
	{
		Element oElement = _oDoc.createElement(_sNodeName);
		oElement.setAttribute(_sAttributeName, _sAttributeValue);
		Text oText = _oDoc.createTextNode(_sNodeValue);
		oElement.appendChild(oText);
		if (_oParentElement == null)
		{
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.appendChild(oElement);
		}
		else
		{
			_oParentElement.appendChild(oElement);
		}		
		
		return oElement;
	}

	/**
	 * This method adds an attribute node to a given element and also returns the element it creates
	 * 
	 * @param _oDoc	The XML document you want to attatch to
	 * @param _oParentElement The parent element you wish to attatch to
	 * @param _sAttributeName The name of the new element
	 * @param _sAttributeValue The value of the new element
	 */
	public static void addSimpleAttributeNode(Document _oDoc, Element _oParentElement, String _sAttributeName, String _sAttributeValue)
	{
		Attr oAttribute = _oDoc.createAttribute(_sAttributeName);
		oAttribute.setValue(_sAttributeValue);
		if (_oParentElement == null)
		{
			Element oRootNode = _oDoc.getDocumentElement();
			oRootNode.setAttributeNode(oAttribute);
		}
		else
		{
			_oParentElement.setAttributeNode(oAttribute);
		}
	}

	/**
	 * This method returns an XPathResult of an XPath query
	 * 
	 * @param _oDoc The XML document you are querying
	 * @param _oElement The element you wish to begin at
	 * @param _sXPathQueryString The XPath query
	 * @return The XPathResult (nodes and such)
	 */
	public static XPathResult getXPathResult(Document _oDoc, Element _oElement, String _sXPathQueryString)
	{
		XPathEvaluator oXpathEvaluator = new XPathEvaluatorImpl(_oDoc);
		XPathNSResolver oXpathResolver = oXpathEvaluator.createNSResolver(_oElement);
		XPathResult oXpathResult = (XPathResult) oXpathEvaluator.evaluate(_sXPathQueryString, _oDoc, oXpathResolver,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		return oXpathResult;
	}

	/**
	 * This method creates the DTD for the standard Java Logging API
	 * 
	 * @param _sLoggerDTDPath The path path to write the DTD
	 * @throws IOException 
	 */
	public static void writeLoggerDTD(String _sLoggerDTDPath) throws IOException
	{
		StringBuffer oDtdSb = new StringBuffer();
		oDtdSb.append("<!-- DTD used by the java.util.logging.XMLFormatter -->");
		oDtdSb.append("\n<!-- This provides an XML formatted log message. -->");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- The document type is \"log\" which consists of a sequence of record elements -->");
		oDtdSb.append("\n<!ELEMENT log (record*)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Each logging call is described by a record element. -->");
		oDtdSb.append("\n<!ELEMENT record (date, millis, sequence, logger?, level, class?, method?, thread?, ");
		oDtdSb.append("message, key?, catalog?, param*, exception?)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Date and time when LogRecord was created in ISO 8601 format -->");
		oDtdSb.append("\n<!ELEMENT date (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Time when LogRecord was created in milliseconds since midnight January 1st, 1970, UTC. -->");
		oDtdSb.append("\n<!ELEMENT millis (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Unique sequence number within source VM. -->");
		oDtdSb.append("\n<!ELEMENT sequence (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Name of source Logger object. -->");
		oDtdSb.append("\n<!ELEMENT logger (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Logging level, may be either one of the constant names from java.util.logging.Constants ");
		oDtdSb.append("(such as \"SEVERE\" or \"WARNING\") or an integer value such as \"20\". -->");
		oDtdSb.append("\n<!ELEMENT level (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Fully qualified name of class that issued logging call, e.g. \"javax.marsupial.Wombat\". -->");
		oDtdSb.append("\n<!ELEMENT class (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Name of method that issued logging call.");
		oDtdSb.append("\nIt may be either an unqualified method name such as" + "\"fred\" or it may include argument ");
		oDtdSb.append("type information in parenthesis, for example \"fred(int,String)\". -->");
		oDtdSb.append("\n<!ELEMENT method (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- Integer thread ID. -->");
		oDtdSb.append("\n<!ELEMENT thread (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- The message element contains the text string of a log message. -->");
		oDtdSb.append("\n<!ELEMENT message (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- If the message string was localized, the key element provides the original localization message key. -->");
		oDtdSb.append("\n<!ELEMENT key (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- If the message string was localized, the catalog element provides the logger's ");
		oDtdSb.append("localization resource bundle name. -->");
		oDtdSb.append("\n<!ELEMENT catalog (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- If the message string was localized, each of the param elements provides the ");
		oDtdSb.append("String value (obtained using Object.toString()) of the corresponding LogRecord parameter. -->");
		oDtdSb.append("\n<!ELEMENT param (#PCDATA)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- An exception consists of an optional message string followed by a series of StackFrames. ");
		oDtdSb.append("Exception elements are used for Java exceptions and other java Throwables. -->");
		oDtdSb.append("\n<!ELEMENT exception (message?, frame+)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- A frame describes one line in a Throwable backtrace. -->");
		oDtdSb.append("\n<!ELEMENT frame (class, method, line?)>");
		oDtdSb.append("\n");
		oDtdSb.append("\n<!-- an integer line number within a class's source file. -->");
		oDtdSb.append("\n<!ELEMENT line (#PCDATA)>");
		XSaLTFileSystemUtils.writeStringToFile(oDtdSb.toString(), _sLoggerDTDPath);

	}

	/**
	 * This method returns the column names, types and counts
	 * 
	 * @param _oRs The ResultSet to examine
	 * @return The HashMap of special column information
	 * @throws SQLException
	 */
	private static HashMap<String, Serializable> getColumnNamesAndCountHashMap(ResultSet _oRs) throws SQLException
	{
		ResultSetMetaData oRsmd = _oRs.getMetaData();
		HashMap<Integer, String> oColumnNameHashmap = new HashMap<Integer, String>();
		HashMap<Integer, String> oColumnTypeHashmap = new HashMap<Integer, String>();

		int n_column_count = oRsmd.getColumnCount();
		if (n_column_count > 0)
		{
			for (int i = 1; i <= n_column_count; i++)
			{
				Integer oColumnIndex = new Integer(i);
				String sColumnName = oRsmd.getColumnName(i).toUpperCase();
				String sColumnTypename = oRsmd.getColumnTypeName(i).toUpperCase();
				oColumnNameHashmap.put(oColumnIndex, sColumnName);
				oColumnTypeHashmap.put(oColumnIndex, sColumnTypename);
			}

		}

		HashMap<String, Serializable> oNamesAndCountHashMap = new HashMap<String, Serializable>();
		oNamesAndCountHashMap.put("COLUMN_NAMES", oColumnNameHashmap);
		oNamesAndCountHashMap.put("COLUMN_TYPES", oColumnTypeHashmap);
		oNamesAndCountHashMap.put("COLUMN_COUNT", new Integer(n_column_count).toString());
		return oNamesAndCountHashMap;

	}

	/**
	 * This method attaches the column name, tye and data onto a given parent leaf node
	 * @param _oRs The ResultSet to query
	 * @param _oColumnNameHashmap The HashMap of column names
	 * @param _oColumnTypeHashmap The HashMap of column types
	 * @param _nColumnPosition The column position (starting at 1, as JDBC does)
	 * @param _oDoc The XML document you wish to attach to
	 * @param _oParentNode The parent node for the data leaf
	 * @param _bMakeHeaderRow Flag if this is a header row
	 * @return The created element with the data attached
	 * @throws SQLException
	 */
	private static Element attachColumnDataToRecordNode(ResultSet _oRs, HashMap<Integer, String> _oColumnNameHashmap,
			HashMap<Integer, String> _oColumnTypeHashmap, int _nColumnPosition, Document _oDoc, Element _oParentNode, boolean _bMakeHeaderRow)
			throws SQLException
	{

		Element oReturnElement = null;
		String sColumnName = _oColumnNameHashmap.get(new Integer(_nColumnPosition)).toString();
		String sValueOfColumn = _oRs.getString(_nColumnPosition);

		if (_bMakeHeaderRow)
		{
			sColumnName = "COLUMN_NAME";
		}

		if (_oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().indexOf(".TIMESTAMP") != -1
				|| _oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().indexOf("TIMESTAMP") != -1)
		{
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatTimeStampFromMySQLToReadableTimeStamp(sValueOfColumn));
		}
		else if (_oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().indexOf(".DATETIME") != -1
				|| _oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().indexOf("DATETIME") != -1)
		{
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatTimeStampFromMySQLToReadableTimeStamp(sValueOfColumn));
		}
		else if (_oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().indexOf(".DATE") != -1
				|| _oColumnTypeHashmap.get(new Integer(_nColumnPosition)).toString().toUpperCase().equals("DATE"))
		{
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName,
					XSaLTStringUtils.formatDateFromMySQLToReadableDate(sValueOfColumn));
		}
		else
		{
			oReturnElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, sColumnName, sValueOfColumn);
		}
		return oReturnElement;
	}

	/**
	 * This method creates a standard select data node, only first two values are recorded as the key and value
	 * 
	 * @param _oRs The ResultSet to use as the data
	 * @param _oDoc The XML document you wish to append to
	 * @param _sSelectNodeName The name of the select node
	 * @param _oAdditionalFields The additional fields you wish append
	 * @throws SQLException
	 */
	public static void createSelectNode(ResultSet _oRs, Document _oDoc, String _sSelectNodeName, ArrayList<String> _oAdditionalFields,
			boolean _bAddBlankNode) throws SQLException
	{
		Element oSelectNodeElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, _sSelectNodeName, null);
		int nRowCount = 0;

		if (_bAddBlankNode)
		{
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = "";
			String sValue = "";
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder", new Integer(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			nRowCount = nRowCount + 1;
		}

		while (_oRs.next())
		{
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = XSaLTStringUtils.getEmptyStringIfNull(_oRs.getString(1));
			String sValue = XSaLTStringUtils.getEmptyStringIfNull(_oRs.getString(2));
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder", new Integer(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			if (_oAdditionalFields != null)
			{
				for (int i = 0; i < _oAdditionalFields.size(); i++)
				{
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
	 * @param _oDoc
	 *            XML document to add node to
	 * @param _sSelectNodeName
	 *            Name for created SELECT input
	 * @param _oTripleStringLinkedHashMap
	 *            Data to use to populate the SELECT input
	 * @param _bAddBlankNode
	 *            Flag if a blank OPTION should be created before the actual options
	 * @throws SQLException
	 */
	public static void createSelectNode(Document _oDoc, String _sSelectNodeName, XSaLTTripleStringLinkedHashMap _oTripleStringLinkedHashMap,
			boolean _bAddBlankNode) throws SQLException
	{
		Element oSelectNodeElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, _sSelectNodeName, null);
		int nRowCount = 0;

		if (_bAddBlankNode)
		{
			Element oOptionElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oSelectNodeElement, "DisplayOption", null);
			String sKey = "";
			String sValue = "";
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayOrder", new Integer(nRowCount).toString());
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayValue", sKey);
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oOptionElement, "DisplayHTML", sValue);
			nRowCount = nRowCount + 1;
		}

		ArrayList<String> oKeyArrayList = _oTripleStringLinkedHashMap.getKeyArrayList();
		for (int i = 0; i < oKeyArrayList.size(); i++)
		{
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
	 * This method calculates the maximum record set size for the current page.
	 * 
	 * @param _oRequest
	 *            HttpServletRequest object from servlet
	 * @param _nRecordsetSize
	 *            Record set size
	 * @return Max number of records for scrollable view
	 */
	public static int getMaxScrollableXMLRecordSetSize(HttpServletRequest _oRequest, long _nRecordsetSize)
	{
		int lPageNo = XSaLTStringUtils.getDefaultIntegerIfParameterIsNull(_oRequest.getParameter("PageNo"), 1);
		int nMaxResultSize = (lPageNo * new Long(_nRecordsetSize).intValue() + 20);
		//		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "max result: " + nMaxResultSize);
		return nMaxResultSize;
	}

	/**
	 * This method grabs a scrolling set from a ResultSet and creates a data tree
	 * 
	 * @param _oRs The ResultSet to use as the data
	 * @param _oDoc The XML document you wish to append to
	 * @param _oRequest The HttpServletRequest (used for scrolling and such)
	 * @param _sRecordsetName The name of the recordset in the data tree
	 * @param _oHeaderLinkedHashMap The header LinkedHashMap (if any)
	 * @param _nRecordsetSize The size/quantity of records shown
	 * @param _bUseAbsoluteScrolling Flag if absolute scrolling is used
	 * @throws SQLException
	 */

	@SuppressWarnings("unchecked")
	public static void makeScrollableXMLRecordSetTree(String _sSQL, ResultSet _oRs, Document _oDoc, HttpServletRequest _oRequest,
			String _sRecordsetName, LinkedHashMap<String, XSaLTDataColumnDefinition> _oHeaderLinkedHashMap, long _nRecordsetSize,
			boolean _bUseAbsoluteScrolling) throws SQLException
	{

		Element oElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, null, _sRecordsetName, null);
		HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
		HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_NAMES");
		HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_TYPES");
		int n_column_count = new Integer(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();

		long lPageNo = new Long(XSaLTStringUtils.getDefaultLongIfParameterIsNull(_oRequest.getParameter("PageNo"), 1)).longValue();

		Element oRecordsElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oElement, "ResultSet", null);

		String sDrillColor = "BLUE";
		if (_sSQL.indexOf("STATUS = 'Purchased'") != -1)
		{
			sDrillColor = "GREEN";
		}
		else if (_sSQL.indexOf("STATUS = 'Not Purchased'") != -1)
		{
			sDrillColor = "RED";
		}

		XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "DrillColor", sDrillColor);
		XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "PageNo", new Long(lPageNo).toString());
		XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "RecordsPerPage", new Long(_nRecordsetSize).toString());

		if (_oHeaderLinkedHashMap != null)
		{
			Element oRowHeaderElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "RecordHeaders", null);
			for (Iterator<String> i = _oHeaderLinkedHashMap.keySet().iterator(); i.hasNext();)
			{
				Element oRowElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowHeaderElement, "RecordHeader", null);
				String sKey = (String) i.next();
				XSaLTDataColumnDefinition oTempXDCD = (XSaLTDataColumnDefinition) _oHeaderLinkedHashMap.get(sKey);
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "HTMLDisplay", oTempXDCD.getDataColumnHtmlDisplayName());
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "HTMLColumnWidth", oTempXDCD.getDataColumnHtmlDisplayWidth());
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "HTMLHAlignment", oTempXDCD.getDataColumnHtmlHAlignment());
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "HTMLVAlignment", oTempXDCD.getDataColumnHtmlVAlignment());
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "DATASortColumn", oTempXDCD.getSortColumn());
				addSimpleAttributeNode(_oDoc, oRowElement, "styleHeader", sKey);
			}
		}

		if (_bUseAbsoluteScrolling)
		{
			if (lPageNo > 1)
			{
				_oRs.absolute(new Long((lPageNo - 1) * _nRecordsetSize).intValue());
			}
		}

		int nRecordsetLoops = 0;
		Element oRowRecordsElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "Records", null);

		while (_oRs.next() && nRecordsetLoops < _nRecordsetSize)
		{
			Element oRowElement = XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowRecordsElement, "Record", null);
			for (int i = 1; i <= n_column_count; i++)
			{
				Element oRowValue = attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, oRowElement, false);

				XSaLTDataColumnDefinition oTempXDCD = (XSaLTDataColumnDefinition) _oHeaderLinkedHashMap.get(oColumnNameHashmap.get(new Integer(i))
						.toString());
				if (oTempXDCD != null)
				{
					addSimpleAttributeNode(_oDoc, oRowValue, "HTMLColumnWidth", oTempXDCD.getDataColumnHtmlDisplayWidth());
					addSimpleAttributeNode(_oDoc, oRowValue, "HTMLDHAlignment", oTempXDCD.getDataDataHtmlHAlignment());
					addSimpleAttributeNode(_oDoc, oRowValue, "HTMLDVAlignment", oTempXDCD.getDataDataHtmlVAlignment());
				}
			}
			nRecordsetLoops = nRecordsetLoops + 1;
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowElement, "RECORD_ORDER", new Integer(nRecordsetLoops).toString());
		}

		XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRecordsElement, "LastRecordOrdinal", new Long(nRecordsetLoops).toString());

	} /**/

	/**
	 * This method grabs the first record from a ResultSet and creates a data tree
	 * 
	 * @param _oRs The ResultSet to query
	 * @param _oDoc The XML document to attatch the data to
	 * @param _oParentNode The parent node to attatch the data to
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultFirstRecordFromResultSetToXMLTree(ResultSet _oRs, Document _oDoc, Element _oParentNode) throws SQLException
	{
		if (_oRs.next())
		{
			HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
			HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_NAMES");
			HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_TYPES");
			int n_column_count = new Integer(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();
			for (int i = 1; i <= n_column_count; i++)
			{
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, _oParentNode, false);
			}
		}
	}

	/**
	 * This method grabs all records from a ResultSet and creates a data tree
	 * @param _oRs The ResultSet to query
	 * @param _oDoc The XML document to attatch the data to
	 * @param _oParentNode The parent node to attatch the data to
	 * @param _sResultRowName The name you want the row/record to be labelled in the tree
	 * @param _sPrimaryKeyColumnName The name of the primary key column in the database
	 * @param _bMakeHeaderRow Flag if this is a header row
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultAllRecordsFromResultSetToXMLTree(ResultSet _oRs, Document _oDoc, Element _oParentNode, String _sResultRowName,
			String _sPrimaryKeyColumnName, boolean _bMakeHeaderRow) throws SQLException
	{

		HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
		HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_NAMES");
		HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_TYPES");
		int n_column_count = new Integer(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();

		Element oHeaderRow = null;
		if (_bMakeHeaderRow)
		{
			oHeaderRow = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName + "_HEADER", "");
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_COUNT", new Integer(n_column_count).toString());
		}

		if (n_column_count > 0)
		{
			for (int i = 1; i <= n_column_count; i++)
			{
				String sColumnName = oColumnNameHashmap.get(new Integer(i)).toString().toUpperCase();
				if (_bMakeHeaderRow)
				{
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_HEADER_NAME", sColumnName);
				}
			}
		}

		int nRecordCount = 1;

		while (_oRs.next())
		{
			Element oRowValueOne = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName, "");
			if (_sPrimaryKeyColumnName != null)
			{
				XSaLTXMLUtils.addSimpleAttributeNode(_oDoc, oRowValueOne, _sPrimaryKeyColumnName, _oRs.getString(_sPrimaryKeyColumnName));
			}

			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, "SORT_ORDER",
					XSaLTStringUtils.padLeftWithCharacter(new Integer(nRecordCount).toString(), '0', 9));

			for (int i = 1; i <= n_column_count; i++)
			{
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, oRowValueOne, _bMakeHeaderRow);
			}

			_oParentNode.appendChild(oRowValueOne);
			nRecordCount = nRecordCount + 1;
		}
	}

	/**
	 * This method grabs all records from a ResultSet and creates a data tree
	 * @param _oRs The ResultSet to query
	 * @param _oDoc The XML document to attatch the data to
	 * @param _oParentNode The parent node to attatch the data to
	 * @param _sResultRowName The name you want the row/record to be labelled in the tree
	 * @param _sPrimaryKeyColumnName The name of the primary key column in the database
	 * @param _bMakeHeaderRow Flag if this is a header row
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static void resultAllRecordsFromResultSetToXMLTreeWithExtraNodes(ResultSet _oRs, Document _oDoc, Element _oParentNode,
			String _sResultRowName, String _sPrimaryKeyColumnName, boolean _bMakeHeaderRow, HashMap<String, String> _oExtraNodesMap)
			throws SQLException
	{

		HashMap<String, Serializable> oNamesAndCountHashMap = getColumnNamesAndCountHashMap(_oRs);
		HashMap<Integer, String> oColumnNameHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_NAMES");
		HashMap<Integer, String> oColumnTypeHashmap = (HashMap<Integer, String>) oNamesAndCountHashMap.get("COLUMN_TYPES");
		int n_column_count = new Integer(oNamesAndCountHashMap.get("COLUMN_COUNT").toString()).intValue();

		Element oHeaderRow = null;
		if (_bMakeHeaderRow)
		{
			oHeaderRow = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName + "_HEADER", "");
			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_COUNT", new Integer(n_column_count).toString());
		}

		if (n_column_count > 0)
		{
			for (int i = 1; i <= n_column_count; i++)
			{
				String sColumnName = oColumnNameHashmap.get(new Integer(i)).toString().toUpperCase();
				if (_bMakeHeaderRow)
				{
					XSaLTXMLUtils.addSimpleTextNode(_oDoc, oHeaderRow, "COLUMN_HEADER_NAME", sColumnName);
				}
			}
		}

		int nRecordCount = 1;

		while (_oRs.next())
		{
			Element oRowValueOne = XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oParentNode, _sResultRowName, "");
			if (_sPrimaryKeyColumnName != null)
			{
				XSaLTXMLUtils.addSimpleAttributeNode(_oDoc, oRowValueOne, _sPrimaryKeyColumnName, _oRs.getString(_sPrimaryKeyColumnName));
			}

			XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, "SORT_ORDER",
					XSaLTStringUtils.padLeftWithCharacter(new Integer(nRecordCount).toString(), '0', 9));

			for (int i = 1; i <= n_column_count; i++)
			{
				attachColumnDataToRecordNode(_oRs, oColumnNameHashmap, oColumnTypeHashmap, i, _oDoc, oRowValueOne, _bMakeHeaderRow);
			}

			for (Iterator<String> j = _oExtraNodesMap.keySet().iterator(); j.hasNext();)
			{
				String sKeyName = (String) j.next();
				String sValue = _oExtraNodesMap.get(sKeyName);
				XSaLTXMLUtils.addSimpleTextNode(_oDoc, oRowValueOne, sKeyName, sValue);
			}

			_oParentNode.appendChild(oRowValueOne);
			nRecordCount = nRecordCount + 1;
		}
	}
	
	/**
	 * This method grabs all information from a result set and puts it into a Excel-type
	 * XML tree.
	 * 
	 * @param _oRs
	 *            The result set to put into XML
	 * @param _sSheetName
	 *            The name to give to the Excel work sheet
	 * @param _bWriteHeader
	 *            Flag if column headers should be written
	 * @return An XML document for Excel
	 * @throws SQLException
	 */
	public static Document exportSQLAsExcelXMLTree(ResultSet _oRs,  String _sSheetName, boolean _bWriteHeader) throws SQLException
	{
		DOMImplementation oDomImp = new DOMImplementationImpl();		
		Document oDoc = oDomImp.createDocument("urn:schemas-microsoft-com:office:spreadsheet", "ss:Workbook", null);
		
		ArrayList<String> oColumnNames = new ArrayList<String>();
		ArrayList<String> oColumnTypes = new ArrayList<String>();
		
		ResultSetMetaData oRsMd = _oRs.getMetaData();
		// ResultSet & ResultSetMetaData column indices start at 1
		for (int i = 1; i <= oRsMd.getColumnCount(); i++)
		{
			oColumnNames.add(oRsMd.getColumnName(i));
			oColumnTypes.add(oRsMd.getColumnTypeName(i));
		}
		
		Element oRootElement = oDoc.getDocumentElement();
		oRootElement.setAttribute("xmlns:ss", "urn:schemas-microsoft-com:office:spreadsheet");
		
		Element oWorksheet = addTextNodeWithAttribute(oDoc, oRootElement, "ss:Worksheet", null, "ss:Name",
				(_sSheetName == null || _sSheetName.trim().equals("") ? "Sheet1" : _sSheetName));		
		Element oTable = XSaLTXMLUtils.addSimpleTextNode(oDoc, oWorksheet, "ss:Table", null);
		
		if (_bWriteHeader)
		{
			Element oRow = XSaLTXMLUtils.addSimpleTextNode(oDoc, oTable, "ss:Row", null);
			for (int i = 0; i < oColumnNames.size(); i++)
			{
				Element oCell = addSimpleTextNode(oDoc, oRow, "ss:Cell", null);
				
				addTextNodeWithAttribute(oDoc, oCell, "ss:Data", oColumnNames.get(i).toUpperCase(), "ss:Type", "String");
			}
		}
		
		while (_oRs.next())
		{
			Element oRow = addSimpleTextNode(oDoc, oTable, "ss:Row", null);
			for (int i = 0; i < oColumnNames.size(); i++)
			{
				Element oCell = addSimpleTextNode(oDoc, oRow, "ss:Cell", null);
				
				String sColumnType = oColumnTypes.get(i).toUpperCase();
				String sDataType = "String";
				if (sColumnType.contains("INT") || sColumnType.contains("DOUBLE"))
				{
					sDataType = "Number";
				}
				else if (sColumnType.contains("DATE"))
				{
					sDataType = "DateTime";
				}
				
				addTextNodeWithAttribute(oDoc, oCell, "ss:Data", _oRs.getString(oColumnNames.get(i)), "ss:Type", sDataType);				
			}
		}
		
		return oDoc;
	}

}
