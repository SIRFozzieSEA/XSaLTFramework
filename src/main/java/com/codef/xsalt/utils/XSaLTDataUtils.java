package com.codef.xsalt.utils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

//import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codef.xsalt.arch.XSaLTDataProcessInterface;
import com.codef.xsalt.arch.XSaLTTripleStringLinkedHashMap;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTDataUtils {

	public static boolean XB_SYSOUT_DB_CALLS = false;

	public static boolean XB_SHOW_INSERTS_DB_CALLS = false;
	public static boolean XB_SHOW_DELETES_DB_CALLS = false;
	public static boolean XB_SHOW_UPDATES_DB_CALLS = false;

	public static boolean XB_SHOW_STRING_INDEX_OUT_OF_BOUNDS_EXCEPTION = false;

	public static final String XS_ENCRYPT_KEY = "0steve1and2bob3are4awesome5";

	private static final Logger LOGGER = LogManager.getLogger(XSaLTDataUtils.class.getName());

	/**
	 * This method will sum each numeric column in the specified table and output
	 * the result to a log.
	 * 
	 * @param _oConn                      Database connection object
	 * @param _sTableName                 Table for action
	 * @param _bPostiveValues             Flag to only include positive values
	 * @param _bNegativeValues            Flag to only include negative values
	 * @param _bIncludeWholeNumberColumns Flag to include whole number columns
	 * @param _bIncludeDecimalColumn      Flag to include decimal number columns
	 * @throws SQLException
	 */
	public static void sumNumberColumns(Connection _oConn, String _sTableName, boolean _bPostiveValues,
			boolean _bNegativeValues, boolean _bIncludeWholeNumberColumns, boolean _bIncludeDecimalColumn)
			throws SQLException {

		ResultSet oTestRs = querySQL(_oConn, "SELECT * FROM " + _sTableName);
		if (oTestRs.next()) {
			ResultSetMetaData oRsMd = oTestRs.getMetaData();
			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
				String sColumnType = oRsMd.getColumnTypeName(i + 1).toUpperCase();

				if (_bIncludeWholeNumberColumns) {
					if (sColumnType.indexOf("INT") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						if (_bPostiveValues) {
							sSQL = sSQL + " AND " + sColumnName + " > 0";
						}
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info("\t" + sColumnName + "\t" + "(Greater Than Zero)" + "\t" + oRs.getString("MYSUM")
								+ "\t");
					}
				}

				if (_bIncludeWholeNumberColumns) {
					if (sColumnType.indexOf("INT") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info("\t" + sColumnName + "\t" + "(All)" + "\t" + oRs.getString("MYSUM") + "\t");
					}
				}

				if (_bIncludeWholeNumberColumns) {
					if (sColumnType.indexOf("INT") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						if (_bNegativeValues) {
							sSQL = sSQL + " AND " + sColumnName + " < 0";
						}
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info(
								"\t" + sColumnName + "\t" + "(Less Than Zero)" + "\t" + oRs.getString("MYSUM") + "\t");
					}
				}

				if (_bIncludeDecimalColumn) {
					if (sColumnType.indexOf("FLOAT") != -1 || sColumnType.indexOf("DEC") != -1
							|| sColumnType.indexOf("DOUBLE") != -1 || sColumnType.indexOf("REAL") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						if (_bPostiveValues) {
							sSQL = sSQL + " AND " + sColumnName + " > 0";
						}
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info("\t" + sColumnName + "\t" + "(Greater Than Zero)" + "\t" + oRs.getString("MYSUM")
								+ "\t");
					}

				}

				if (_bIncludeDecimalColumn) {
					if (sColumnType.indexOf("FLOAT") != -1 || sColumnType.indexOf("DEC") != -1
							|| sColumnType.indexOf("DOUBLE") != -1 || sColumnType.indexOf("REAL") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info("\t" + sColumnName + "\t" + "(All)" + "\t" + oRs.getString("MYSUM") + "\t");
					}

				}

				if (_bIncludeDecimalColumn) {
					if (sColumnType.indexOf("FLOAT") != -1 || sColumnType.indexOf("DEC") != -1
							|| sColumnType.indexOf("DOUBLE") != -1 || sColumnType.indexOf("REAL") != -1) {
						String sSQL = "SELECT sum(" + sColumnName + ") AS MYSUM FROM " + _sTableName + " WHERE "
								+ sColumnName + " IS NOT NULL";
						if (_bNegativeValues) {
							sSQL = sSQL + " AND " + sColumnName + " < 0";
						}
						ResultSet oRs = getFirstRecord(_oConn, sSQL);
						LOGGER.info(
								"\t" + sColumnName + "\t" + "(Less Than Zero)" + "\t" + oRs.getString("MYSUM") + "\t");
					}

				}

			}
		}

	}

	/**
	 * This method gets the table description as an arraylist with the values
	 * comma-delimited
	 * 
	 * @param _oConn1     First database connection object
	 * @param _sTableName First table name for schema
	 * @return Arraylist<String> table description as an arraylist with the values
	 *         comma-delimited
	 * 
	 */
	public static ArrayList<String> getTableDefinitionAsArrayListDelimited(Connection _oConn, String _sTableName)
			throws SQLException {
		ArrayList<String> oTempDescriptionArrayList = new ArrayList<String>();
		String sSQL = "describe " + _sTableName;
		ResultSet oRs = querySQL(_oConn, sSQL);
		while (oRs.next()) {
			oTempDescriptionArrayList.add(XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("FIELD")) + ","
					+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("TYPE")) + ","
					+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("NULL")) + ","
					+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("KEY")) + ","
					+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("DEFAULT")) + ","
					+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString("EXTRA")));

		}

		return oTempDescriptionArrayList;

	}

	/**
	 * This method converts a dbf/FoxPro file to a tab-delimited file
	 * 
	 * @param _sFilePath The path to the dbf file
	 * 
	 */
	public static void convertDbfToTab(String _sFilePath) {
		try {
			StringBuffer oRecordBuffer = new StringBuffer();
			InputStream inputStream = new FileInputStream(_sFilePath); // take dbf file as program argument
			DBFReader reader = new DBFReader(inputStream);
			int numberOfFields = reader.getFieldCount();
			for (int i = 0; i < numberOfFields; i++) {
				DBFField field = reader.getField(i);
				oRecordBuffer.append(field.getName().trim() + "\t");
				reader.getField(i).setDataType(DBFField.FIELD_TYPE_C);
			}
			oRecordBuffer.append("EOF\n");
			Object[] rowObjects;
			while ((rowObjects = reader.nextRecord()) != null) {
				for (int i = 0; i < rowObjects.length; i++) {
					oRecordBuffer.append(rowObjects[i].toString().trim() + "\t");
				}
				oRecordBuffer.append("EOF\n");
			}
			inputStream.close();
			XSaLTFileSystemUtils.writeStringBufferToFile(oRecordBuffer, _sFilePath.replaceAll("DBF", "tab"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * This method gets the table description as an arraylist with the values
	 * comma-delimited
	 * 
	 * @param _oConn      First database connection object
	 * @param _sTableName First table name for schema
	 * @return Arraylist<String> table columns as an arraylist
	 * 
	 */
	public static ArrayList<String> getTableColumnsNames(Connection _oConn, String _sTableName) throws SQLException {
		ArrayList<String> oColumnNames = new ArrayList<String>();
		ResultSet oTestRs = querySQL(_oConn, "SELECT * FROM " + _sTableName);
		ResultSetMetaData oRsMd = oTestRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();

			// if (oRsMd.getColumnTypeName(i + 1).equalsIgnoreCase("timestamp"))
			// {
			// LOGGER.info(_sTableName + "." +
			// sColumnName);
			// }

			oColumnNames.add(sColumnName);
		}
		return oColumnNames;
	}

	/**
	 * This method gets the table schemas for the specified tables and outputs each
	 * to a separate file for comparison.
	 * 
	 * @param _oConn1              First database connection object
	 * @param _SchemaTableName1    First table name for schema
	 * @param _oConn2              Second database connection object
	 * @param _SchemaTableName2    Second table name for schema
	 * @param _sDescribeOutputPath File path to output table schema descriptions.
	 */
	public static void compareSchemaTables(Connection _oConn1, String _SchemaTableName1, Connection _oConn2,
			String _SchemaTableName2, String _sDescribeOutputPath) {
		/*
		 * 
		 * try { Connection oConn1 = getMySQLConnection("localhost", "tmaxapp", "root",
		 * XSaLTFrameworkProperties.XS_DEFAULT_PASSWORD); Connection oConn2 =
		 * getMySQLConnection("landrover", "tmaxapp", "root",
		 * XSaLTFrameworkProperties.XS_DEFAULT_PASSWORD); compareSchemaTables(oConn1,
		 * "tmaxapp", oConn2, "tmaxapp", "C:/compare/"); } catch (Exception e) {
		 * 
		 * }
		 * 
		 */

		try {

			XSaLTFileSystemUtils.createFileFolder(_sDescribeOutputPath + "one/");
			XSaLTFileSystemUtils.createFileFolder(_sDescribeOutputPath + "two/");

			String sSQL = "SELECT table_schema, table_name FROM information_schema.`TABLES` where table_schema = '"
					+ _SchemaTableName1 + "'";
			ResultSet oRs = querySQL(_oConn1, sSQL);
			while (oRs.next()) {
				String sTableName = oRs.getString("table_schema") + "." + oRs.getString("table_name");

				String sSQLTwo = "describe " + sTableName;
				exportSQLAsCommaDelimitedDataFile(_oConn1, sSQLTwo,
						_sDescribeOutputPath + "one/" + sTableName.replaceAll("\\.", "_") + ".txt", true);

			}

			sSQL = "SELECT table_schema, table_name FROM information_schema.`TABLES` where table_schema = '"
					+ _SchemaTableName2 + "'";
			oRs = querySQL(_oConn2, sSQL);
			while (oRs.next()) {
				String sTableName = oRs.getString("table_schema") + "." + oRs.getString("table_name");

				String sSQLTwo = "describe " + sTableName;
				exportSQLAsCommaDelimitedDataFile(_oConn2, sSQLTwo,
						_sDescribeOutputPath + "two/" + sTableName.replaceAll("\\.", "_") + ".txt", true);

			}

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * This method imports the specified file into a table and returns a HashMap
	 * with data about the created table.
	 * 
	 * @param _oConn          Database connection object
	 * @param _sStartFilePath Path for file to import
	 * @param _sTableName     Name of table to create
	 * @param _bHasHeaders    Flag if file has headers
	 * @return HashMap<String, Object> with meta information about the created MySQL
	 *         table.
	 * @throws SQLException
	 * @throws IOException
	 */
	public static HashMap<String, Object> importDelimitedFile(Connection _oConn, String _sStartFilePath,
			String _sTableName, boolean _bHasHeaders) throws SQLException, IOException {

		return importDelimitedFile(_oConn, _sStartFilePath, _sTableName, null, null, _bHasHeaders, false, false, false,
				false, false);
	}

	/**
	 * This method imports the specified file into a table and returns a HashMap
	 * with data about the created table.
	 * 
	 * @param _oConn          Database connection object
	 * @param _sStartFilePath Path for file to import
	 * @param _sTableName     Name of table to create
	 * @param _bHasHeaders    Flag if file has headers
	 * @return HashMap<String, Object> with meta information about the created MySQL
	 *         table.
	 * @throws SQLException
	 * @throws IOException
	 */
	public static HashMap<String, Object> importDelimitedFileWithDupeRow(Connection _oConn, String _sStartFilePath,
			String _sTableName, boolean _bHasHeaders) throws SQLException, IOException {

		return importDelimitedFile(_oConn, _sStartFilePath, _sTableName, null, null, _bHasHeaders, false, false, false,
				false, true);
	}

	/**
	 * This method updates the specified fields with the specified default values in
	 * the given table.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableName   Table for action
	 * @param _oColsToEmpty Map of columns to set back to default value
	 * @param _sWhereClause Where clause to determine records to update
	 * @throws SQLException
	 */
	public static void updateFieldsWithEmpty(Connection _oConn, String _sTableName,
			HashMap<String, String> _oColsToEmpty, String _sWhereClause) throws SQLException {
		StringBuffer oUpdateBuffer = new StringBuffer("UPDATE " + _sTableName + " SET ");
		for (Iterator<String> j = _oColsToEmpty.keySet().iterator(); j.hasNext();) {
			String sColumnName = (String) j.next();
			String sDefaultValue = _oColsToEmpty.get(sColumnName);
			oUpdateBuffer.append(sColumnName + " = '" + sDefaultValue + "', ");
		}
		oUpdateBuffer = new StringBuffer(oUpdateBuffer.substring(0, oUpdateBuffer.length() - 2));
		oUpdateBuffer.append(" WHERE (" + _sWhereClause + ")");

		executeSQL(_oConn, oUpdateBuffer.toString());

	}

	/**
	 * This method adds empty columns to the specified tables with a appending
	 * number to denote that the new columns created are duplicates.
	 * 
	 * @param _oConn            Database connection object
	 * @param _sTableName       Table for action
	 * @param _oColumnsToCreate ArrayList of columns to duplicate
	 * @param _nColumnNo        Number to append to column name to denote as
	 *                          duplicate
	 * @throws SQLException
	 */
	public static void createEmptyDuplicateColumns(Connection _oConn, String _sTableName,
			ArrayList<String> _oColumnsToCreate, int _nColumnNo) throws SQLException {
		for (int i = 0; i < _oColumnsToCreate.size(); i++) {
			String sColumnName = _oColumnsToCreate.get(i) + "_" + Integer.valueOf(_nColumnNo).toString();
			makeColumnIfNotExist(_oConn, _sTableName, sColumnName, "VARCHAR(200) DEFAULT ''");
		}
	}

	/**
	 * This method will clear out columns columns in the specified table, leaving
	 * the generated ID and exception list alone.
	 * 
	 * @param _oConn               Database connection object
	 * @param _sRowGenIDColumn     Generated ID column name for specified table
	 * @param _sTableName          Table for action
	 * @param _sWhereClause        Where clause for finding the records to empty
	 * @param _oColumnsNotToModify ArrayList of column names that the method will
	 *                             not set to empty
	 * @throws SQLException
	 */
	public static void setColumnsEmptyWithExceptionColumns(Connection _oConn, String _sRowGenIDColumn,
			String _sTableName, String _sWhereClause, ArrayList<String> _oColumnsNotToModify) throws SQLException {

		ArrayList<String> oColumnsToChange = new ArrayList<String>();

		ResultSet oTestRs = querySQL(_oConn, "SELECT * FROM " + _sTableName);
		ResultSetMetaData oRsMd = oTestRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oColumnsToChange.add(sColumnName);
		}

		for (int i = 0; i < _oColumnsNotToModify.size(); i++) {
			oColumnsToChange.remove(_oColumnsNotToModify.get(i));
		}

		oColumnsToChange.remove(_sRowGenIDColumn);

		StringBuffer oUpdateBuffer = new StringBuffer("UPDATE " + _sTableName + " SET ");
		for (int i = 0; i < oColumnsToChange.size(); i++) {
			oUpdateBuffer.append(oColumnsToChange.get(i) + " = '', ");
		}

		oUpdateBuffer = new StringBuffer(oUpdateBuffer.toString().substring(0, oUpdateBuffer.toString().length() - 2));
		oUpdateBuffer.append(" WHERE " + _sWhereClause);

		executeSQL(_oConn, oUpdateBuffer.toString());

	}

	/**
	 * This method will find each TIMESTAMP column in the specified table schema and
	 * output the results to the log.
	 * 
	 * @param _oConn               Database connection object
	 * @param _sSchemaNameToLookAt Schema name to look
	 * @throws SQLException
	 */
	public static void findTimestampColumns(Connection _oConn, String _sSchemaNameToLookAt) throws SQLException {
		String sSQL = "SELECT table_schema, table_name FROM information_schema.`TABLES` where table_schema = '"
				+ _sSchemaNameToLookAt + "'";
		ResultSet oRs = querySQL(_oConn, sSQL);
		while (oRs.next()) {
			String sSchema = oRs.getString("table_schema");
			String sTableName = oRs.getString("table_name");

			ResultSet oTestRs = querySQL(_oConn, "SELECT * FROM " + sSchema + "." + sTableName);
			ResultSetMetaData oRsMd = oTestRs.getMetaData();
			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
				String sColumnType = oRsMd.getColumnTypeName(i + 1);

				if (sColumnType.equalsIgnoreCase("TIMESTAMP")) {
					LOGGER.info(sSchema + "." + sTableName + " ---> " + sColumnName + " (" + sColumnType + ")");

				}
			}

		}

		// convertTimestampColumnToDateTimeColumn(oConn, "table", "column");
		// convertTimestampColumnToDateTimeColumn(oConn, "table", "column");

	}

	/**
	 * This method logs each of the columns in the specified table.
	 * 
	 * @param _oConn      Database connection object
	 * @param _sTablename Table to view
	 * @throws SQLException
	 */
	public static void createXSaLTHashMapFieldNamesFromTable(Connection _oConn, String _sTablename)
			throws SQLException {
		ResultSet oTestRs = querySQL(_oConn, "SELECT * FROM " + _sTablename);
		ResultSetMetaData oRsMd = oTestRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			String sFinalColumnName = _sTablename + "__NEW__1__" + sColumnName + "__REG";
			LOGGER.info(sFinalColumnName.toUpperCase());
		}
	}

	/**
	 * This method imports the specified file into a table and returns a HashMap
	 * with data about the created table. HashMap includes the following keys: -
	 * COLUMNS - ROW_COUNT - COLUMN_COUNT_DATA - COLUMN_COUNT_HEADER -
	 * COLUMN_LENGTH_DATA
	 * 
	 * @param _oConn                 Database connection object
	 * @param _sStartFilePath        File path where to find file to import
	 * @param _sTableName            Table to import into
	 * @param _sDefaultColumnType    Column type for data when not specified
	 * @param _sDefaultTableType     Table type (MyISAM or InnoDB)
	 * @param _bHasHeaders           Flag for imported data has headers
	 * @param _bEmptyStringAsNull    Flag to treat empty strings as NULL values
	 * @param _bConvertToUpperCase   Flag to convert all strings to upper case
	 * @param _bShowDebug            Flag to show debugging output
	 * @param _bShrinkDataColumnSize Flag to shrink columns
	 * @param _bAddDupeRow           Flag to add information in table if item are
	 *                               duplicated
	 * @return HashMap<String, Object> with meta information about the created MySQL
	 *         table.
	 * @throws SQLException
	 * @throws IOException
	 */
	public static HashMap<String, Object> importDelimitedFile(Connection _oConn, String _sStartFilePath,
			String _sTableName, String _sDefaultColumnType, String _sDefaultTableType, boolean _bHasHeaders,
			boolean _bEmptyStringAsNull, boolean _bConvertToUpperCase, boolean _bShowDebug,
			boolean _bShrinkDataColumnSize, boolean _bAddDupeRow) throws SQLException, IOException {

		if (_sDefaultColumnType == null) {
			_sDefaultColumnType = "VARCHAR";
		}

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		int nMaxDataColumnCount = 0;
		int[] anMaxDataColumnLength = new int[5000];

		HashMap<String, Object> oResultsMap = new HashMap<String, Object>();
		ArrayList<String> oFileBuffer = XSaLTFileSystemUtils.writeFileToArrayList(_sStartFilePath, null);
		ArrayList<String[]> oCleanFileBuffer = new ArrayList<String[]>();

		for (int i = 0; i < oFileBuffer.size(); i++) {
			boolean bInQuotes = false;
			String sCurrentLine = oFileBuffer.get(i);
			StringBuffer oLineBuffer = new StringBuffer();

			if (sCurrentLine.indexOf("\t") != -1) {
				// file is tab delimited
				oLineBuffer.append(sCurrentLine);
			} else {
				// file is comma delimited
				for (int j = 0; j < sCurrentLine.length(); j++) {
					String sCurrentChar = sCurrentLine.substring(j, j + 1);
					if (sCurrentChar.equals("\"")) {
						if (bInQuotes) {
							bInQuotes = false;
						} else {
							bInQuotes = true;
						}
					}

					if (sCurrentChar.equals("\"")) {
					} else if (sCurrentChar.equals(",")) {
						if (bInQuotes) {
							oLineBuffer.append(",");
						} else {
							oLineBuffer.append("\t");
						}
					} else {
						oLineBuffer.append(sCurrentChar);
					}
				}
			}

			String sLineBuffer = oLineBuffer.toString().replaceAll(" +", " ");

			if (sLineBuffer.length() > 0) {
				if (sLineBuffer.toUpperCase().indexOf("ROWGENID") != -1) {
					sLineBuffer = sLineBuffer.toUpperCase().replaceAll("ROWGENID", "ROWGENIDOLD");
				}

				String[] asTempString = sLineBuffer.split("\t");

				for (int j = 0; j < asTempString.length; j++) {
					String sSingleQuoteChar = "'";
					String sTempString = asTempString[j].trim();

					if (_bConvertToUpperCase) {
						sTempString = sTempString.toUpperCase();
					}

					sTempString = sTempString.replaceAll("'", "`");

					if (_bHasHeaders && i == 0) {
						sSingleQuoteChar = "";
					} else {
						sSingleQuoteChar = "'";
					}

					if (_bEmptyStringAsNull) {
						if (sTempString == null) {
							sTempString = "null";
						} else if (sTempString.length() == 0) {
							sTempString = "null";
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					} else {
						if (sTempString == null) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else if (sTempString.length() == 0) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					}
					asTempString[j] = sTempString;
				}

				if (_bHasHeaders) {

					if (i != 0) {
						for (int j = 0; j < asTempString.length; j++) {
							String sTempString = asTempString[j];
							int nTempStringLength = sTempString.trim().length();

							if (anMaxDataColumnLength[j] == 0) {
								anMaxDataColumnLength[j] = nTempStringLength;
							} else {
								if (nTempStringLength > anMaxDataColumnLength[j]) {
									anMaxDataColumnLength[j] = nTempStringLength;
								}
							}
						}
					}
				} else {
					for (int j = 0; j < asTempString.length; j++) {
						String sTempString = asTempString[j];
						int nTempStringLength = sTempString.trim().length();

						if (anMaxDataColumnLength[j] == 0) {
							anMaxDataColumnLength[j] = nTempStringLength;
						} else {
							if (nTempStringLength > anMaxDataColumnLength[j]) {
								anMaxDataColumnLength[j] = nTempStringLength;
							}
						}
					}

				}

				oCleanFileBuffer.add(asTempString);

				if (nMaxDataColumnCount < asTempString.length) {
					nMaxDataColumnCount = asTempString.length;
				}

			}

		}

		if (_bHasHeaders) {
			oResultsMap.put("COLUMNS", oCleanFileBuffer.get(0));
			oResultsMap.put("ROW_COUNT", Integer.valueOf(oCleanFileBuffer.size() - 1));
		} else {
			StringBuffer oColumnHeaderBuffer = new StringBuffer();
			for (int j = 1; j <= nMaxDataColumnCount; j++) {
				Integer oTempInteger = Integer.valueOf(j);
				String sFieldName = "FIELD_" + XSaLTStringUtils.padLeftWithCharacter(oTempInteger.toString(), '0', 4);
				oColumnHeaderBuffer.append(sFieldName + "\t");

			}
			oColumnHeaderBuffer = new StringBuffer(
					oColumnHeaderBuffer.toString().substring(0, oColumnHeaderBuffer.toString().length() - 1));
			String[] asTempString = oColumnHeaderBuffer.toString().split("\t");

			oCleanFileBuffer.add(0, asTempString);

			oResultsMap.put("COLUMNS", asTempString);
			oResultsMap.put("ROW_COUNT", Integer.valueOf(oCleanFileBuffer.size()));
		}

		oResultsMap.put("COLUMN_COUNT_DATA", Integer.valueOf(nMaxDataColumnCount));
		oResultsMap.put("COLUMN_COUNT_HEADER", Integer.valueOf(oCleanFileBuffer.get(0).length));
		oResultsMap.put("COLUMN_LENGTH_DATA", anMaxDataColumnLength);

		if (nMaxDataColumnCount > oCleanFileBuffer.get(0).length) {
			LOGGER.fatal("DATA EXTENDS PAST COLUMN HEADERS !!!!!!!");
		}

		dropTableInDatabase(_oConn, _sTableName.toUpperCase());

		StringBuffer oCreateSqlStringbuffer = new StringBuffer("CREATE TABLE " + _sTableName.toUpperCase() + " (");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append("\n   ROWGENID serial PRIMARY KEY, ");
		} else {
			oCreateSqlStringbuffer.append("\n   ROWGENID bigint(20) NOT NULL auto_increment PRIMARY KEY, ");
		}

		if (_bAddDupeRow) {
			oCreateSqlStringbuffer.append("\n   ORIGINAL_OR_DUPE VARCHAR(20) DEFAULT '', ");
		}

		StringBuffer oAllColumnsForInsert = new StringBuffer(" (");

		String[] asTemp = (String[]) oResultsMap.get("COLUMNS");
		for (int i = 0; i < asTemp.length; i++) {

			if (!_bShrinkDataColumnSize) {
				anMaxDataColumnLength[i] = 200;
			}

			if (_sDefaultColumnType != null) {
				oAllColumnsForInsert
						.append(XSaLTStringUtils.regExReplaceSpacesWithUnderscores(asTemp[i].toUpperCase()));
				oCreateSqlStringbuffer.append(
						"\n   " + XSaLTStringUtils.regExReplaceSpacesWithUnderscores(asTemp[i].toUpperCase()) + " ");
				oCreateSqlStringbuffer.append(_sDefaultColumnType + "(" + anMaxDataColumnLength[i] + ")");
			} else {
				oAllColumnsForInsert
						.append(XSaLTStringUtils.regExReplaceSpacesWithUnderscores(asTemp[i].toUpperCase()));
				oCreateSqlStringbuffer.append(
						"\n   " + XSaLTStringUtils.regExReplaceSpacesWithUnderscores(asTemp[i].toUpperCase()) + " ");
				oCreateSqlStringbuffer.append(_sDefaultColumnType + "");
			}

			if (_sDefaultColumnType.toUpperCase().indexOf("CHAR") != -1) {
				oCreateSqlStringbuffer.append(" DEFAULT '' ");
			}
			oAllColumnsForInsert.append(", ");
			oCreateSqlStringbuffer.append(", ");

		}
		oCreateSqlStringbuffer = new StringBuffer(
				oCreateSqlStringbuffer.substring(0, oCreateSqlStringbuffer.length() - 2));
		oAllColumnsForInsert = new StringBuffer(oAllColumnsForInsert.substring(0, oAllColumnsForInsert.length() - 2));

		oAllColumnsForInsert.append(") ");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append(")");
		} else {
			oCreateSqlStringbuffer.append(")\nENGINE=" + _sDefaultTableType);
		}

		executeSQL(_oConn, oCreateSqlStringbuffer.toString());

		StringBuffer oFinalInsertBuffer = new StringBuffer("");
		if (_sDefaultTableType.equals("H2")) {
			oFinalInsertBuffer
					.append("INSERT INTO " + _sTableName.toUpperCase() + " " + oAllColumnsForInsert + " VALUES ");
		} else {
			oFinalInsertBuffer.append("INSERT INTO " + _sTableName.toUpperCase() + " VALUES ");
		}

		String[] asData;

		for (int i = 1; i < oCleanFileBuffer.size(); i++) {
			asData = oCleanFileBuffer.get(i);
			if (asData.length < 1) {
				continue;
			}

			if (_sDefaultTableType.equals("H2")) {

				if (_bAddDupeRow) {
					oFinalInsertBuffer.append(
							"('XX_ORIGINAL_XX', " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));
				} else {
					oFinalInsertBuffer.append("(" + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));
				}
			} else {
				if (_bAddDupeRow) {
					oFinalInsertBuffer.append("(null, 'XX_ORIGINAL_XX', "
							+ XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));
				} else {
					oFinalInsertBuffer
							.append("(null, " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));
				}
			}

			if (asData.length < oCleanFileBuffer.get(0).length) {
				int nMakeUpRows = oCleanFileBuffer.get(0).length - asData.length;
				for (int j = 0; j < nMakeUpRows; j++) {
					if (_bEmptyStringAsNull) {
						oFinalInsertBuffer.append(", null");
					} else {
						oFinalInsertBuffer.append(", ''");
					}
				}
			}
			oFinalInsertBuffer.append("), ");

			if ((i - 1) % 1001 == 0) {

				executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString()
						.replaceAll("XX_ORIGINAL_XX", "ORIGINAL"));

				if (_bAddDupeRow) {
					executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString()
							.replaceAll("XX_ORIGINAL_XX", "DUPE"));
				}

				oFinalInsertBuffer = new StringBuffer("");
				if (_sDefaultTableType.equals("H2")) {

					oFinalInsertBuffer.append(
							"INSERT INTO " + _sTableName.toUpperCase() + " " + oAllColumnsForInsert + " VALUES ");
				} else {
					oFinalInsertBuffer.append("INSERT INTO " + _sTableName.toUpperCase() + " VALUES ");
				}

				if (!_oConn.getAutoCommit()) {
					_oConn.commit();
				}
			}

		}

		if (!oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString().endsWith("VALUE")) {
			executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString()
					.replaceAll("XX_ORIGINAL_XX", "ORIGINAL"));

			if (_bAddDupeRow) {
				executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString()
						.replaceAll("XX_ORIGINAL_XX", "DUPE"));
			}

		}

		// change ROWGENIDOLD back to a BIGINT
		try {
			executeSQL(_oConn, "ALTER TABLE " + _sTableName + " CHANGE COLUMN ROWGENIDOLD ROWGENIDOLD BIGINT(20)");
		} catch (Exception e) {
		}

		if (!_oConn.getAutoCommit()) {
			_oConn.commit();
		}

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = "
				+ getRowsCountInDataTable(_oConn, _sTableName.toUpperCase()));

		if (_bShowDebug) {

			LOGGER.info("");
			LOGGER.info("TABLE IMPORT SUMMARY: ");
			LOGGER.info("");

			asTemp = (String[]) oResultsMap.get("COLUMNS");
			LOGGER.info(
					"                   Columns: " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asTemp, ", "));

			LOGGER.info("");

			Integer oRowCount = (Integer) oResultsMap.get("ROW_COUNT");
			LOGGER.info("                 Row Count: " + oRowCount.toString());

			Integer oColumnCountHeader = (Integer) oResultsMap.get("COLUMN_COUNT_HEADER");
			LOGGER.info("   Max Column Count Header: " + oColumnCountHeader.toString());

			Integer oColumnCount = (Integer) oResultsMap.get("COLUMN_COUNT_DATA");
			LOGGER.info("     Max Column Count Data: " + oColumnCount.toString());

			LOGGER.info("");

			int[] anTemp = (int[]) oResultsMap.get("COLUMN_LENGTH_DATA");
			for (int i = 0; i < anTemp.length; i++) {
				if (i < oColumnCountHeader.intValue()) {
					LOGGER.info("         Column Max Length: " + anTemp[i] + " - '" + asTemp[i] + "'");
				}
			}

			LOGGER.info("");

		}

		oFinalInsertBuffer = null;

		return oResultsMap;
	}

	/**
	 * This method determines if a given column exists and if does not, adds the
	 * column to the specified table.
	 * 
	 * @param _oConn             Database connection object
	 * @param _sTableName        Table for action
	 * @param _sColumnName       Column to add
	 * @param _sColumnDefinition Column definition
	 * @return If column was added
	 * @throws SQLException
	 */
	public static boolean makeColumnIfNotExist(Connection _oConn, String _sTableName, String _sColumnName,
			String _sColumnDefinition) throws SQLException {

		boolean bFieldFound = false;
		ResultSet oRs = querySQL(_oConn, "describe " + _sTableName);

		while (oRs.next()) {
			if (oRs.getString("field").equalsIgnoreCase(_sColumnName)) {
				bFieldFound = true;
			}
		}

		if (!bFieldFound) {
			String sSQL = "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " " + _sColumnDefinition;
			executeSQL(_oConn, sSQL);
			return true;
		}

		return false;

	}

	/**
	 * This method imports the specified file (which includes type headers) into a
	 * table and returns a HashMap with data bout the created table. HashMap
	 * includes the following keys: - COLUMNS - ROW_COUNT - COLUMN_COUNT_DATA -
	 * COLUMN_COUNT_HEADER - COLUMN_LENGTH_DATA
	 * 
	 * @param _oConn                 Database connection object
	 * @param _sStartFilePath        File path of where to find file to import
	 * @param _sTableName            Table to import into
	 * @param _sDefaultTableType     Table type (MyISAM or InnoDB)
	 * @param _asColumnNames         Array of column names
	 * @param _bEmptyStringAsNull    Flag to treat empty strings as NULL values
	 * @param _bConvertToUpperCase   Flag to convert all strings to upper case
	 * @param _bShowDebug            Flag to show debugging output
	 * @param _bShrinkDataColumnSize Flag to shrink columns
	 * @return HashMap<String, Object> with meta information about the created MySQL
	 *         table.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public static HashMap<String, Object> importDelimitedFileWithTypeHeaders(Connection _oConn, String _sStartFilePath,
			String _sTableName, String _sDefaultTableType, String[] _asColumnNames, boolean _bEmptyStringAsNull,
			boolean _bConvertToUpperCase, boolean _bShowDebug, boolean _bShrinkDataColumnSize)
			throws SQLException, IOException {

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		int nMaxDataColumnCount = 0;
		int[] anMaxDataColumnLength = new int[5000];

		HashMap<String, Object> oResultsMap = new HashMap<String, Object>();
		ArrayList<String> oFileBuffer = XSaLTFileSystemUtils.writeFileToArrayList(_sStartFilePath, null);
		ArrayList<String[]> oCleanFileBuffer = new ArrayList<String[]>();

		for (int i = 0; i < oFileBuffer.size(); i++) {
			boolean bInQuotes = false;
			String sCurrentLine = oFileBuffer.get(i);
			StringBuffer oLineBuffer = new StringBuffer();

			if (sCurrentLine.indexOf("\t") != -1) {
				// file is tab delimited
				oLineBuffer.append(sCurrentLine);
			} else {
				// file is comma delimited
				for (int j = 0; j < sCurrentLine.length(); j++) {
					String sCurrentChar = sCurrentLine.substring(j, j + 1);
					if (sCurrentChar.equals("\"")) {
						if (bInQuotes) {
							bInQuotes = false;
						} else {
							bInQuotes = true;
						}
					}

					if (sCurrentChar.equals("\"")) {
					} else if (sCurrentChar.equals(",")) {
						if (bInQuotes) {
							oLineBuffer.append(",");
						} else {
							oLineBuffer.append("\t");
						}
					} else {
						oLineBuffer.append(sCurrentChar);
					}
				}
			}

			String sLineBuffer = oLineBuffer.toString().replaceAll(" +", " ");

			if (sLineBuffer.length() > 0) {
				if (sLineBuffer.toUpperCase().indexOf("ROWGENID") != -1) {
					sLineBuffer = sLineBuffer.toUpperCase().replaceAll("ROWGENID", "ROWGENIDOLD");
				}

				String[] asTempString = sLineBuffer.split("\t");

				for (int j = 0; j < asTempString.length; j++) {
					String sSingleQuoteChar = "'";
					String sTempString = asTempString[j].trim();

					if (_bConvertToUpperCase) {
						sTempString = sTempString.toUpperCase();
					}

					sTempString = sTempString.replaceAll("'", "\\\\'");

					if (i == 0 && _asColumnNames == null) {
						sSingleQuoteChar = "";
					} else {
						sSingleQuoteChar = "'";
					}

					if (_bEmptyStringAsNull) {
						if (sTempString == null) {
							sTempString = "null";
						} else if (sTempString.length() == 0) {
							sTempString = "null";
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					} else {
						if (sTempString == null) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else if (sTempString.length() == 0) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					}
					asTempString[j] = sTempString;
				}

				// if (i != 0)
				// {
				for (int j = 0; j < asTempString.length; j++) {
					String sTempString = asTempString[j];
					int nTempStringLength = sTempString.trim().length();

					if (anMaxDataColumnLength[j] == 0) {
						anMaxDataColumnLength[j] = nTempStringLength;
					} else {
						if (nTempStringLength > anMaxDataColumnLength[j]) {
							anMaxDataColumnLength[j] = nTempStringLength;
						}
					}
				}
				// }

				oCleanFileBuffer.add(asTempString);

				if (nMaxDataColumnCount < asTempString.length) {
					nMaxDataColumnCount = asTempString.length;
				}

			}

		}

		if (_asColumnNames == null) {
			oResultsMap.put("COLUMNS", oCleanFileBuffer.get(0));
		} else {
			oResultsMap.put("COLUMNS", _asColumnNames);
		}

		oResultsMap.put("ROW_COUNT", Integer.valueOf(oCleanFileBuffer.size() - 1));

		oResultsMap.put("COLUMN_COUNT_DATA", Integer.valueOf(nMaxDataColumnCount));
		oResultsMap.put("COLUMN_COUNT_HEADER", Integer.valueOf(oCleanFileBuffer.get(0).length));
		oResultsMap.put("COLUMN_LENGTH_DATA", anMaxDataColumnLength);

		if (nMaxDataColumnCount > oCleanFileBuffer.get(0).length) {
			LOGGER.fatal("DATA EXTENDS PAST COLUMN HEADERS !!!!!!!");
		}

		dropTableInDatabase(_oConn, _sTableName.toUpperCase());

		StringBuffer oCreateSqlStringbuffer = new StringBuffer("CREATE TABLE " + _sTableName.toUpperCase() + " (");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append("\n   ROWGENID serial auto_increment PRIMARY KEY, ");
		} else {
			oCreateSqlStringbuffer.append("\n   ROWGENID bigint(20) NOT NULL auto_increment PRIMARY KEY, ");
		}

		String[] asTemp = (String[]) oResultsMap.get("COLUMNS");
		for (int i = 0; i < asTemp.length; i++) {

			if (!_bShrinkDataColumnSize) {
				anMaxDataColumnLength[i] = 200;
			}

			String sColumnRealName = "";
			String sColumnRealType = "";

			String asColumnUserName[] = XSaLTStringUtils.regExReplaceSpacesWithUnderscores(asTemp[i].toUpperCase())
					.split("__");

			sColumnRealName = asColumnUserName[0];

			if (asColumnUserName.length > 1) {
				sColumnRealType = asColumnUserName[1];
			}

			if (sColumnRealType.toLowerCase().equals("varchar")) {
				oCreateSqlStringbuffer.append("\n   " + sColumnRealName + " VARCHAR(" + anMaxDataColumnLength[i] + ")");
			} else if (sColumnRealType.toLowerCase().equals("decimal")) {
				oCreateSqlStringbuffer.append("\n   " + sColumnRealName + " DOUBLE(15,2)");
			} else if (sColumnRealType.toLowerCase().equals("integer")) {
				oCreateSqlStringbuffer.append("\n   " + sColumnRealName + " BIGINT(20)");
			} else {
				oCreateSqlStringbuffer.append("\n   " + sColumnRealName + " VARCHAR(" + anMaxDataColumnLength[i] + ")");
				sColumnRealName = "VARCHAR";
			}

			if (sColumnRealType.toUpperCase().indexOf("CHAR") != -1) {
				oCreateSqlStringbuffer.append(" DEFAULT '' ");
			}
			oCreateSqlStringbuffer.append(", ");

		}
		oCreateSqlStringbuffer = new StringBuffer(
				oCreateSqlStringbuffer.substring(0, oCreateSqlStringbuffer.length() - 2));

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append(")");
		} else {
			oCreateSqlStringbuffer.append(")\nENGINE=" + _sDefaultTableType);
		}

		executeSQL(_oConn, oCreateSqlStringbuffer.toString());

		StringBuffer oFinalInsertBuffer = new StringBuffer("INSERT INTO " + _sTableName.toUpperCase() + " VALUES ");

		// int nRowInsertCount = 0;
		for (int i = 0; i < oCleanFileBuffer.size(); i++) {
			String[] asData = oCleanFileBuffer.get(i);

			oFinalInsertBuffer.append("(null, " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));

			if (asData.length < oCleanFileBuffer.get(0).length) {
				int nMakeUpRows = oCleanFileBuffer.get(0).length - asData.length;
				for (int j = 0; j < nMakeUpRows; j++) {
					if (_bEmptyStringAsNull) {
						oFinalInsertBuffer.append(", null");
					} else {
						oFinalInsertBuffer.append(", ''");
					}
				}
			}
			oFinalInsertBuffer.append("), ");

			if ((i - 1) % 1001 == 0) {

				executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString());

				oFinalInsertBuffer = new StringBuffer("INSERT INTO " + _sTableName.toUpperCase() + " VALUES ");

				if (!_oConn.getAutoCommit()) {
					_oConn.commit();
				}
			}

			// nRowInsertCount = i;
		}

		executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString());
		oFinalInsertBuffer = new StringBuffer("INSERT INTO " + _sTableName.toUpperCase() + " VALUES ");

		if (!_oConn.getAutoCommit()) {
			_oConn.commit();
		}

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = "
				+ getRowsCountInDataTable(_oConn, _sTableName.toUpperCase()));

		if (_bShowDebug) {

			LOGGER.info("");
			LOGGER.info("TABLE IMPORT SUMMARY: ");
			LOGGER.info("");

			asTemp = (String[]) oResultsMap.get("COLUMNS");
			LOGGER.info(
					"                   Columns: " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asTemp, ", "));

			LOGGER.info("");

			Integer oRowCount = (Integer) oResultsMap.get("ROW_COUNT");
			LOGGER.info("                 Row Count: " + oRowCount.toString());

			Integer oColumnCountHeader = (Integer) oResultsMap.get("COLUMN_COUNT_HEADER");
			LOGGER.info("   Max Column Count Header: " + oColumnCountHeader.toString());

			Integer oColumnCount = (Integer) oResultsMap.get("COLUMN_COUNT_DATA");
			LOGGER.info("     Max Column Count Data: " + oColumnCount.toString());

			LOGGER.info("");

			int[] anTemp = (int[]) oResultsMap.get("COLUMN_LENGTH_DATA");
			for (int i = 0; i < anTemp.length; i++) {
				if (i < oColumnCountHeader.intValue()) {
					LOGGER.info("         Column Max Length: " + anTemp[i] + " - '" + asTemp[i] + "'");
				}
			}

			LOGGER.info("");

		}

		return oResultsMap;
	}

	/**
	 * This method converts a TIMESTAMP column into a DATETIME column.
	 * 
	 * @param _oConn       Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column to convert
	 * @throws SQLException
	 */
	public static void convertTimestampColumnToDateTimeColumn(Connection _oConn, String _sTableName,
			String _sColumnName) throws SQLException {
		executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName
				+ "_NEW DATETIME not null AFTER " + _sColumnName);
		executeSQL(_oConn, "UPDATE " + _sTableName + " SET " + _sColumnName + "_NEW = " + _sColumnName);
		executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN " + _sColumnName);
		executeSQL(_oConn, "ALTER TABLE " + _sTableName + " CHANGE COLUMN " + _sColumnName + "_NEW " + _sColumnName
				+ " DATETIME NOT NULL");
	}

	/**
	 * This method imports the specified file into a table and returns a HashMap
	 * with data about the data inserted. HashMap includes the following keys: -
	 * COLUMNS - ROW_COUNT - COLUMN_COUNT_DATA - COLUMN_COUNT_HEADER -
	 * COLUMN_LENGTH_DATA
	 * 
	 * @param _oConn
	 * @param _sStartFilePath
	 * @param _sTableName
	 * @param _sDefaultTableType
	 * @param _asColumnNames
	 * @param _bEmptyStringAsNull
	 * @param _bConvertToUpperCase
	 * @param _bShowDebug
	 * @param _bShrinkDataColumnSize
	 * @param _bSkipFirstLine
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static HashMap<String, Object> importDelimitedFileWithTypeHeadersNoCreateTable(Connection _oConn,
			String _sStartFilePath, String _sTableName, String _sDefaultTableType, String[] _asColumnNames,
			boolean _bEmptyStringAsNull, boolean _bConvertToUpperCase, boolean _bShowDebug,
			boolean _bShrinkDataColumnSize, boolean _bSkipFirstLine) throws SQLException, IOException {

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		int nMaxDataColumnCount = 0;
		int[] anMaxDataColumnLength = new int[5000];

		HashMap<String, Object> oResultsMap = new HashMap<String, Object>();
		ArrayList<String> oFileBuffer = XSaLTFileSystemUtils.writeFileToArrayList(_sStartFilePath, null);
		ArrayList<String[]> oCleanFileBuffer = new ArrayList<String[]>();

		if (_bSkipFirstLine) {
			oFileBuffer.remove(0);
		}

		for (int i = 0; i < oFileBuffer.size(); i++) {
			boolean bInQuotes = false;
			String sCurrentLine = oFileBuffer.get(i);
			StringBuffer oLineBuffer = new StringBuffer();

			if (sCurrentLine.indexOf("\t") != -1) {
				// file is tab delimited
				oLineBuffer.append(sCurrentLine);
			} else {
				// file is comma delimited
				for (int j = 0; j < sCurrentLine.length(); j++) {
					String sCurrentChar = sCurrentLine.substring(j, j + 1);
					if (sCurrentChar.equals("\"")) {
						if (bInQuotes) {
							bInQuotes = false;
						} else {
							bInQuotes = true;
						}
					}

					if (sCurrentChar.equals("\"")) {
					} else if (sCurrentChar.equals(",")) {
						if (bInQuotes) {
							oLineBuffer.append(",");
						} else {
							oLineBuffer.append("\t");
						}
					} else {
						oLineBuffer.append(sCurrentChar);
					}
				}
			}

			String sLineBuffer = oLineBuffer.toString().replaceAll(" +", " ");

			if (sLineBuffer.length() > 0) {
				if (sLineBuffer.toUpperCase().indexOf("ROWGENID") != -1) {
					sLineBuffer = sLineBuffer.toUpperCase().replaceAll("ROWGENID", "ROWGENIDOLD");
				}

				String[] asTempString = sLineBuffer.split("\t");

				for (int j = 0; j < asTempString.length; j++) {
					String sSingleQuoteChar = "'";
					String sTempString = XSaLTStringUtils.regExReplaceStringForInsert(asTempString[j].trim());

					if (_bConvertToUpperCase) {
						sTempString = sTempString.toUpperCase();
					}

					sTempString = sTempString.replaceAll("'", "\\\\'");

					if (i == 0 && _asColumnNames == null) {
						sSingleQuoteChar = "";
					} else {
						sSingleQuoteChar = "'";
					}

					if (_bEmptyStringAsNull) {
						if (sTempString == null) {
							sTempString = "null";
						} else if (sTempString.length() == 0) {
							sTempString = "null";
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					} else {
						if (sTempString == null) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else if (sTempString.length() == 0) {
							sTempString = sSingleQuoteChar + sSingleQuoteChar;
						} else {
							sTempString = sSingleQuoteChar + sTempString + sSingleQuoteChar;
						}
					}
					asTempString[j] = sTempString;
				}

				// if (i != 0)
				// {
				for (int j = 0; j < asTempString.length; j++) {
					String sTempString = asTempString[j];
					int nTempStringLength = sTempString.trim().length();

					if (anMaxDataColumnLength[j] == 0) {
						anMaxDataColumnLength[j] = nTempStringLength;
					} else {
						if (nTempStringLength > anMaxDataColumnLength[j]) {
							anMaxDataColumnLength[j] = nTempStringLength;
						}
					}
				}
				// }

				oCleanFileBuffer.add(asTempString);

				if (nMaxDataColumnCount < asTempString.length) {
					nMaxDataColumnCount = asTempString.length;
				}

			}

		}

		if (_asColumnNames == null) {
			oResultsMap.put("COLUMNS", oCleanFileBuffer.get(0));
		} else {
			oResultsMap.put("COLUMNS", _asColumnNames);
		}

		oResultsMap.put("ROW_COUNT", Integer.valueOf(oCleanFileBuffer.size() - 1));

		oResultsMap.put("COLUMN_COUNT_DATA", Integer.valueOf(nMaxDataColumnCount));
		oResultsMap.put("COLUMN_COUNT_HEADER", Integer.valueOf(oCleanFileBuffer.get(0).length));
		oResultsMap.put("COLUMN_LENGTH_DATA", anMaxDataColumnLength);

		if (nMaxDataColumnCount > oCleanFileBuffer.get(0).length) {
			LOGGER.fatal("DATA EXTENDS PAST COLUMN HEADERS !!!!!!!");
		}

		String sInsertStart = "INSERT INTO " + _sTableName.toUpperCase() + " ("
				+ XSaLTObjectUtils.getStringArrayWithDelimiter_String(_asColumnNames, ", ") + ") VALUES ";
		StringBuffer oFinalInsertBuffer = new StringBuffer(sInsertStart);

		int nRowInsertCount = 0;
		for (int i = 0; i < oCleanFileBuffer.size(); i++) {
			String[] asData = oCleanFileBuffer.get(i);

			oFinalInsertBuffer.append("(" + XSaLTObjectUtils.getStringArrayWithDelimiter_String(asData, ", "));

			if (asData.length < _asColumnNames.length) {
				int nMakeUpRows = _asColumnNames.length - asData.length;
				for (int j = 0; j < nMakeUpRows; j++) {
					if (_bEmptyStringAsNull) {
						oFinalInsertBuffer.append(", null");
					} else {
						oFinalInsertBuffer.append(", ''");
					}
				}
			}

			oFinalInsertBuffer.append("), ");

			if ((i - 1) % 1001 == 0) {
				LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nRowInsertCount);

				executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString());

				oFinalInsertBuffer = new StringBuffer(sInsertStart);

				if (!_oConn.getAutoCommit()) {
					_oConn.commit();
				}
			}

			nRowInsertCount = i;
		}

		if (oFinalInsertBuffer.length() > sInsertStart.length()) {
			executeSQL(_oConn, oFinalInsertBuffer.substring(0, oFinalInsertBuffer.length() - 2).toString());
		}

		if (!_oConn.getAutoCommit()) {
			_oConn.commit();
		}

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = "
				+ getRowsCountInDataTable(_oConn, _sTableName.toUpperCase()));

		if (_bShowDebug) {

			LOGGER.info("");
			LOGGER.info("TABLE IMPORT SUMMARY: ");
			LOGGER.info("");

			LOGGER.info("");

			Integer oRowCount = (Integer) oResultsMap.get("ROW_COUNT");
			LOGGER.info("                 Row Count: " + oRowCount.toString());

			Integer oColumnCountHeader = (Integer) oResultsMap.get("COLUMN_COUNT_HEADER");
			LOGGER.info("   Max Column Count Header: " + oColumnCountHeader.toString());

			Integer oColumnCount = (Integer) oResultsMap.get("COLUMN_COUNT_DATA");
			LOGGER.info("     Max Column Count Data: " + oColumnCount.toString());

			LOGGER.info("");

		}

		return oResultsMap;
	}

	/**
	 * This method appends data from the source column to data from the destination
	 * column.
	 * 
	 * @param _oMySQLConnection     Database connection object
	 * @param _sTableName           Table for action
	 * @param _sSourceColumn        Column to copy from
	 * @param _sDestinationColumn   Column to copy to
	 * @param _sConcatenationChar   Character(s) to concatenate into copied data
	 * @param _bDropColumnAfterFlag Flag to drop source column after copy
	 * @throws SQLException
	 */
	public static void addDataFromColumnToOtherColumn(Connection _oMySQLConnection, String _sTableName,
			String _sSourceColumn, String _sDestinationColumn, String _sConcatenationChar,
			boolean _bDropColumnAfterFlag) throws SQLException {

		executeSQL(_oMySQLConnection,
				"UPDATE " + _sTableName + " SET " + _sDestinationColumn + " = concat(" + _sDestinationColumn + ", '"
						+ _sConcatenationChar + "', " + _sSourceColumn + ") WHERE (" + _sSourceColumn + " != '' AND "
						+ _sSourceColumn + " is not null)");
		if (_bDropColumnAfterFlag) {
			dropColumnInTable(_oMySQLConnection, _sTableName, _sSourceColumn);
		}

	}

	/**
	 * This method renames the specified column in the specified table.
	 * 
	 * @param _oConnection    Database connection object
	 * @param sTableName      Table for action
	 * @param _sColumnName    Original column name
	 * @param _sNewColumnName New column name
	 * @throws SQLException
	 */
	public static void renameColumnInTable(Connection _oConnection, String sTableName, String _sColumnName,
			String _sNewColumnName) throws SQLException {
		executeSQL(_oConnection,
				"ALTER TABLE " + sTableName + " CHANGE " + _sColumnName + " " + _sNewColumnName + " VARCHAR(200)");
	}

	public static void fixNegativeValueInDataColumnAndConvertToDouble(Connection _oConnection, String sTableName,
			String _sColumnName) throws SQLException {
		LOGGER.info("Fixing column " + _sColumnName + " negative values");

		executeSQL(_oConnection,
				"UPDATE " + sTableName + " SET " + _sColumnName + " = null where " + _sColumnName + " = ''");

		String sSQL = "SELECT ROWGENID, " + _sColumnName + " FROM " + sTableName + " ORDER BY ROWGENID";
		ResultSet oRs = querySQL(_oConnection, sSQL);

		while (oRs.next()) {
			String sRowGenId = oRs.getString("ROWGENID");
			String sValue = oRs.getString(_sColumnName);

			if (sValue != null) {

				sValue = XSaLTStringUtils.regExStripNonNumbersTwoFromString(sValue);

				if (sValue.endsWith("-")) {
					String sNewValue = "-" + sValue.substring(0, sValue.length() - 1);
					String sSQL2 = "UPDATE " + sTableName + " SET " + _sColumnName + " = '" + sNewValue
							+ "' WHERE ROWGENID = " + sRowGenId;
					executeSQL(_oConnection, sSQL2);
				} else {
					String sSQL2 = "UPDATE " + sTableName + " SET " + _sColumnName + " = '" + sValue
							+ "' WHERE ROWGENID = " + sRowGenId;
					executeSQL(_oConnection, sSQL2);
				}

			}

		}

		executeSQL(_oConnection, "ALTER TABLE " + sTableName + " MODIFY COLUMN " + _sColumnName + " DOUBLE(9, 2)");
	}

	/**
	 * This method converts the specified column's type to DOUBLE.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column for action
	 * @throws SQLException
	 */
	public static void convertColumnToDouble(Connection _oConnection, String _sTableName, String _sColumnName)
			throws SQLException {

		ResultSet oRs = querySQL(_oConnection, "SELECT DISTINCT " + _sColumnName + " FROM " + _sTableName);
		while (oRs.next()) {
			String sCommaValue = oRs.getString(_sColumnName);
			String sCommaValueNew = XSaLTStringUtils.regExStripNonNumbersTwoFromString(sCommaValue);
			executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + sCommaValueNew
					+ "' WHERE " + _sColumnName + " = '" + sCommaValue + "'");
		}

		// executeSQL(_oConnection, "UPDATE " + sTableName + " SET " + _sColumnName + "
		// = null where " + _sColumnName + " = ''");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " MODIFY COLUMN " + _sColumnName + " DOUBLE(9, 2)");
	}

	/**
	 * This method converts the specified column's type to LONG.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column for action
	 * @throws SQLException
	 */
	public static void convertColumnToLong(Connection _oConnection, String _sTableName, String _sColumnName)
			throws SQLException {

		ResultSet oRs = querySQL(_oConnection,
				"SELECT " + _sColumnName + " FROM " + _sTableName + " WHERE " + _sColumnName + " like '%,%'");
		while (oRs.next()) {
			String sCommaValue = oRs.getString(_sColumnName);
			String sCommaValueNew = XSaLTStringUtils.regExStripNonNumbersTwoFromString(sCommaValue);
			executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + sCommaValueNew
					+ "' WHERE " + _sColumnName + " = '" + sCommaValue + "'");
		}

		executeSQL(_oConnection,
				"UPDATE " + _sTableName + " SET " + _sColumnName + " = null where " + _sColumnName + " = ''");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " MODIFY COLUMN " + _sColumnName + " BIGINT(9)");
	}

	/**
	 * This method converts dates in the format MM/DD/YY to standard the MySQL date
	 * format, changing the column type to DATE.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column for action
	 * @throws SQLException
	 */
	public static void convertColumnToDateFromMMSlashDDSlashYY(Connection _oConnection, String _sTableName,
			String _sColumnName) throws SQLException {
		executeSQL(_oConnection,
				"UPDATE " + _sTableName + " SET " + _sColumnName + " = null where " + _sColumnName + " = ''");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " CHANGE COLUMN " + _sColumnName + " " + _sColumnName
				+ "_OLD VARCHAR(30)");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " DATE");

		ResultSet oRs = querySQL(_oConnection, "SELECT DISTINCT " + _sColumnName + "_OLD FROM " + _sTableName);

		while (oRs.next()) {
			String sOldDateValueAsMMSlashDDSlashYY = oRs.getString(_sColumnName + "_OLD");
			String sFixedDateForMySQL = XSaLTStringUtils
					.formatDateForMySQLFromSlashedDate(sOldDateValueAsMMSlashDDSlashYY);
			executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + sFixedDateForMySQL
					+ "' WHERE " + _sColumnName + "_OLD = '" + sOldDateValueAsMMSlashDDSlashYY + "'");
		}

		dropColumnInTable(_oConnection, _sTableName, _sColumnName + "_OLD");
	}

	/**
	 * This method converts dates in a 0M0D0Y format to the standard MySQL date
	 * format. It also converts the column's type to DATE.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column for action
	 * @throws SQLException
	 */
	public static void convertColumnToDateFrom0M0D0Y(Connection _oConnection, String _sTableName, String _sColumnName)
			throws SQLException {
		executeSQL(_oConnection,
				"UPDATE " + _sTableName + " SET " + _sColumnName + " = null where " + _sColumnName + " = ''");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " CHANGE COLUMN " + _sColumnName + " " + _sColumnName
				+ "_OLD VARCHAR(30)");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " DATE");

		ResultSet oRs = querySQL(_oConnection, "SELECT DISTINCT " + _sColumnName + "_OLD FROM " + _sTableName);

		while (oRs.next()) {
			String sOldDateValue = oRs.getString(_sColumnName + "_OLD");
			String sFixedDateForMySQL = XSaLTStringUtils
					.formatDateForMySQLFromNonFormattedDateTwoDigitYear(sOldDateValue);
			if (!sFixedDateForMySQL.equals("")) {
				executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + sFixedDateForMySQL
						+ "' WHERE " + _sColumnName + "_OLD = '" + sOldDateValue + "'");
			}

		}

		dropColumnInTable(_oConnection, _sTableName, _sColumnName + "_OLD");
	}

	/**
	 * This method converts dates in a 0M0DYYYY format to the standard MySQL date
	 * format. It also converts the column's type to DATE.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column for action
	 * @throws SQLException
	 */
	public static void convertColumnToDateFrom0M0DYYYY(Connection _oConnection, String _sTableName, String _sColumnName)
			throws SQLException {
		executeSQL(_oConnection,
				"UPDATE " + _sTableName + " SET " + _sColumnName + " = null where " + _sColumnName + " = ''");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " CHANGE COLUMN " + _sColumnName + " " + _sColumnName
				+ "_OLD VARCHAR(30)");
		executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " DATE");

		ResultSet oRs = querySQL(_oConnection, "SELECT DISTINCT " + _sColumnName + "_OLD FROM " + _sTableName);

		while (oRs.next()) {
			String sOldDateValue = oRs.getString(_sColumnName + "_OLD");
			String sFixedDateForMySQL = XSaLTStringUtils
					.formatDateForMySQLFromNonFormattedDateFourDigitYear(sOldDateValue);

			if (!sFixedDateForMySQL.equals("")) {
				executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + sFixedDateForMySQL
						+ "' WHERE " + _sColumnName + "_OLD = '" + sOldDateValue + "'");
			}

		}

		dropColumnInTable(_oConnection, _sTableName, _sColumnName + "_OLD");
	}

	/**
	 * This method copies columns and data from one table to another
	 * 
	 * @param _oConn             Database connection object
	 * @param _oColumns          Columns to copy
	 * @param _sColumnType       Column data type
	 * @param _sFromTable        Source table
	 * @param sFromKeyColumnName Key column name from source table
	 * @param _sToTable          Destination table
	 * @param _sToKeyColumnName  Key column name from destination table
	 * @param _bJustAddColumns   Flag to copy just columns or columns and data
	 * @throws SQLException
	 */
	public static void putBackOriginalColumnsAndDataFromOtherTable(Connection _oConn, ArrayList<String> _oColumns,
			String _sColumnType, String _sFromTable, String sFromKeyColumnName, String _sToTable,
			String _sToKeyColumnName, boolean _bJustAddColumns) throws SQLException {

		StringBuffer oColumnsBuffer = new StringBuffer();
		for (int i = 0; i < _oColumns.size(); i++) {
			addColumnInTable(_oConn, _sToTable, _oColumns.get(i), _sColumnType);
			oColumnsBuffer.append(_oColumns.get(i) + ", ");
		}
		oColumnsBuffer.append(sFromKeyColumnName);

		if (!_bJustAddColumns) {

			int nRowCounter = 0;

			ResultSet oRs = querySQL(_oConn,
					"SELECT " + oColumnsBuffer.toString().toUpperCase() + " FROM " + _sFromTable);
			while (oRs.next()) {
				String sFromKeyColumnValue = oRs.getString(sFromKeyColumnName);
				StringBuffer oSQLBuffer = new StringBuffer("UPDATE " + _sToTable + " SET ");
				for (int i = 0; i < _oColumns.size(); i++) {
					oSQLBuffer.append(_oColumns.get(i) + " = '"
							+ XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_oColumns.get(i))) + "', ");
				}
				oSQLBuffer = new StringBuffer(oSQLBuffer.substring(0, oSQLBuffer.length() - 2));
				oSQLBuffer.append(" WHERE " + _sToKeyColumnName + " = '" + sFromKeyColumnValue + "'");

				if (nRowCounter % 500 == 0) {
					LOGGER.info("ADDING OLD DATA BACK, ROW: " + nRowCounter);
				}

				executeSQL(_oConn, oSQLBuffer.toString());
				nRowCounter = nRowCounter + 1;
			}

			LOGGER.info("ADDING OLD DATA BACK, ROW: " + nRowCounter);

		}

	}

	/**
	 * This method returns a LinkedHashMap of the first record returned from the
	 * given SQL statement.
	 * 
	 * @param _oConn Database connection object
	 * @param _sSQL  SQL query statement
	 * @return LinkedHashMap<String, String> of the columns and data for the first
	 *         record in the result set.
	 * @throws SQLException
	 */
	public static LinkedHashMap<String, String> getFirstRecordAsHashMap(Connection _oConn, String _sSQL)
			throws SQLException {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();
		ResultSet oTestRs = querySQL(_oConn, _sSQL);
		if (oTestRs.next()) {
			ResultSetMetaData oRsMd = oTestRs.getMetaData();
			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
				String sColumnValue = XSaLTStringUtils.getEmptyStringIfNull(oTestRs.getString(sColumnName));
				oTempLinkedHashMap.put(sColumnName, sColumnValue);
			}
		}
		return oTempLinkedHashMap;
	}

	/**
	 * This method returns the string value of the first column of the first row
	 * from the specified SQL query.
	 * 
	 * @param _oConn Database connection object
	 * @param _sSQL  SQL query statement
	 * @return String value for first column of the first record returned from the
	 *         SQL query
	 * @throws SQLException
	 */
	public static String getStringValueFromTable(Connection _oConn, String _sSQL) throws SQLException {
		ResultSet oTestRs = querySQL(_oConn, _sSQL);
		if (oTestRs.next()) {
			return oTestRs.getString(0);
		} else {
			return "";
		}
	}

	/**
	 * This method returns the result set from the SQL query if the statement yields
	 * at least one record. The position in the result set has been moved to the
	 * first record.
	 * 
	 * @param _oConn Database connection object
	 * @param _sSQL  SQL query statement
	 * @return ResultSet pointing to the first record
	 * @throws SQLException
	 */
	public static ResultSet getFirstRecord(Connection _oConn, String _sSQL) throws SQLException {

		ResultSet oTestRs = querySQL(_oConn, _sSQL);
		if (oTestRs.next()) {
			return oTestRs;
		} else {
			oTestRs.close();
			return null;
		}
	}

	/**
	 * This method returns a LinkedHashMap containing the column names and data for
	 * the current record in the result set.
	 * 
	 * @param _oRs MySQL result set object
	 * @return LinkedHashMap<String, String> containing column names as keys and
	 *         column data as values
	 * @throws SQLException
	 */
	public static LinkedHashMap<String, String> getCurrentRecordAsHashMap(ResultSet _oRs) throws SQLException {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();

		ResultSetMetaData oRsMd = _oRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			String sColumnValue = XSaLTStringUtils.getEmptyStringIfNull(_oRs.getString(sColumnName));
			oTempLinkedHashMap.put(sColumnName, sColumnValue);
		}
		return oTempLinkedHashMap;
	}

	/**
	 * This method returns the string value of the first record in the given column.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableName   Table to select from
	 * @param _sTableColumn Column to provide information for
	 * @param _sWhereClause Where clause for selecting data
	 * @return String value of the first record in the given column
	 * @throws SQLException
	 */
	public static String getStringValueFromTableColumn(Connection _oConn, String _sTableName, String _sTableColumn,
			String _sWhereClause) throws SQLException {
		String sReturnValue = "";
		StringBuffer oSQL = new StringBuffer("SELECT " + _sTableColumn + " AS MYVALUE FROM " + _sTableName);
		if (_sWhereClause != null) {
			oSQL.append(" WHERE " + _sWhereClause);
		}
		oSQL.append(" ORDER BY " + _sTableColumn);

		ResultSet oSourceRs = querySQL(_oConn, oSQL.toString());
		oSourceRs.next();

		try {
			sReturnValue = oSourceRs.getString("MYVALUE");
			return sReturnValue;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * This method returns the first value from the first record in the specified
	 * column.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableColumn Column to get data for
	 * @param _sSQL         SQL query statement to get column data
	 * @return
	 * @throws SQLException
	 */
	public static String getStringValueFromTableColumnWithSQL(Connection _oConn, String _sTableColumn, String _sSQL)
			throws SQLException {
		String sReturnValue = "";
		ResultSet oSourceRs = querySQL(_oConn, _sSQL);
		oSourceRs.next();

		try {
			sReturnValue = oSourceRs.getString(_sTableColumn);
			return sReturnValue;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * This method returns the first value from the first record in the specified
	 * column.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableColumn Column to get data for
	 * @param _sSQL         SQL query statement to get column data
	 * @return Double value from column specified
	 * 
	 * @throws SQLException
	 */
	public static Double getDoubleValueFromTableColumnWithSQL(Connection _oConn, String _sTableColumn, String _sSQL)
			throws SQLException {
		Double dRV = null;
		ResultSet oSourceRs = querySQL(_oConn, _sSQL);
		if (oSourceRs.first()) {
			dRV = oSourceRs.getDouble(_sTableColumn);
		}

		return dRV;
	}

	/**
	 * This method gets the earliest date in the given column and table.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableName   Table to get data from
	 * @param _sTableColumn Column to get data from
	 * @param _sWhereClause Where clause for narrowing data set
	 * @return String representation of date in MM/DD/YYYY format
	 * @throws SQLException
	 */
	public static String getEarliestDateFromTableColumn(Connection _oConn, String _sTableName, String _sTableColumn,
			String _sWhereClause) throws SQLException {
		String sReturnDate = "";
		StringBuffer oSQL = new StringBuffer(
				"SELECT DATE_FORMAT(" + _sTableColumn + ", '%m/%d/%Y') AS MYDATE FROM " + _sTableName);
		if (_sWhereClause != null) {
			oSQL.append(" WHERE " + _sWhereClause);
		}
		oSQL.append(" ORDER BY " + _sTableColumn);

		ResultSet oSourceRs = querySQL(_oConn, oSQL.toString());
		oSourceRs.next();

		try {
			sReturnDate = oSourceRs.getString("MYDATE");
			return sReturnDate;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * This method gets the latest date in the given table column.
	 * 
	 * @param _oConn        Database connection object
	 * @param _sTableName   Table to select from
	 * @param _sTableColumn Column to select from
	 * @param _sWhereClause Where clause for narrowing data
	 * @return String representation of data in MM/DD/YYYY format
	 * @throws SQLException
	 */
	public static String getLatestDateFromTableColumn(Connection _oConn, String _sTableName, String _sTableColumn,
			String _sWhereClause) throws SQLException {
		String sReturnDate = "";
		StringBuffer oSQL = new StringBuffer(
				"SELECT DATE_FORMAT(" + _sTableColumn + ", '%m/%d/%Y') AS MYDATE FROM " + _sTableName);
		if (_sWhereClause != null) {
			oSQL.append(" WHERE " + _sWhereClause);
		}
		oSQL.append(" ORDER BY " + _sTableColumn + " DESC");

		ResultSet oSourceRs = querySQL(_oConn, oSQL.toString());
		oSourceRs.next();

		try {
			sReturnDate = oSourceRs.getString("MYDATE");
			return sReturnDate;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * This method logs the column names from the given table to generate code for
	 * renaming columns in the given table.
	 * 
	 * @param _oMySQLConnectionSource Database connection object
	 * @param _sSourceTable           Table to select from
	 * @throws SQLException
	 */
	public static void getColumnRenameHashMapVehicle(Connection _oMySQLConnectionSource, String _sSourceTable)
			throws SQLException {
		ResultSet oSourceRs = querySQL(_oMySQLConnectionSource, "SELECT * FROM " + _sSourceTable);
		ResultSetMetaData oRsMd = oSourceRs.getMetaData();

		LOGGER.info("");
		LOGGER.info("");

		LOGGER.info("LinkedHashMap<String, String> oRenameColumnMap = new LinkedHashMap<String, String>();");

		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			LOGGER.info("oRenameColumnMap.put(\"" + sColumnName + "\", \"\");");
		}

		LOGGER.info("");
		LOGGER.info("//   ----- SUGGESTIONS -----");
		LOGGER.info("");

		LOGGER.info("//   ADDRESS_PK               PET_PK                   VEHICLE_PK");
		LOGGER.info("//   FIRST_NAME_ONE           PET_NAME                 VEHICLE_YEAR");
		LOGGER.info("//   FIRST_NAME_TWO           PET_GENDER               VEHICLE_MAKE");
		LOGGER.info("//   FULLNAME                 PET_BREED                VEHICLE_MODEL");
		LOGGER.info("//   LAST_NAME_ONE            PET_COLOR                VEHICLE_COLOR");
		LOGGER.info("//   LAST_NAME_TWO            OLD_RABIES_NO            VEHICLE_PLATE");
		LOGGER.info("//   FULLNAME_TWO             OLD_RABIES_UDATEZ        VEHICLE_VIN");
		LOGGER.info("//   COMPANY                  NEW_RABIES_NO            STICKER_NOS");
		LOGGER.info("//   ADDRESS_TYPE             NEW_RABIES_UDATEZ");
		LOGGER.info("//   ADDRESS_1");
		LOGGER.info("//   ADDRESS_2                PHONE_AREACODE");
		LOGGER.info("//   ADDRESS_3                PHONE_EXCHANGE");
		LOGGER.info("//   ADDRESS_4                PHONE_SUFFIX");
		LOGGER.info("//   CITY                     FULLPHONE");
		LOGGER.info("//   STATEPROV                                         COSTCODE_NAME");
		LOGGER.info("//   ZIP                                               COSTCODE_COST");
		LOGGER.info("//   ZIP_PLUS                                          STATUS");

		LOGGER.info("");
		LOGGER.info("//   ----- SUGGESTIONS -----");
		LOGGER.info("");
		LOGGER.info("");

	}

	/**
	 * This method uses the _oRenameColumnMap to rename columns in the given table.
	 * 
	 * @param _oMySQLConnectionSource Database connection object
	 * @param _sSourceTable           Table for action
	 * @param _oRenameColumnMap       Map of old and new column names (key = old
	 *                                name, value = new name)
	 * @throws SQLException
	 */
	public static void renameColumnHashMapVehicle(Connection _oMySQLConnectionSource, String _sSourceTable,
			LinkedHashMap<String, String> _oRenameColumnMap) throws SQLException {
		for (Iterator<String> i = _oRenameColumnMap.keySet().iterator(); i.hasNext();) {
			String sOriginalColumn = (String) i.next();
			String sRenamedColumn = _oRenameColumnMap.get(sOriginalColumn);
			renameColumnInTable(_oMySQLConnectionSource, _sSourceTable, sOriginalColumn, sRenamedColumn);
		}
	}

	/**
	 * This method copies all data from a source table to a destination table.
	 * 
	 * @param _oMySQLConnectionSource      Database connection object for source
	 *                                     data
	 * @param _sSourceTable                Table to copy from
	 * @param _oMySQLConnectionDestination Database connection object for
	 *                                     destination
	 * @param _sDestinationTableName       Table to copy to
	 * @param _sDefaultColumnType          Default data type for columns
	 * @param _bAllowRowGenId              Flag if generated ID will be copied
	 * @param _bCreateAndShowRows          Flag if columns should be created in the
	 *                                     destination table
	 * @param _bMakeDupeRow                Flag if records should be duplicated
	 * @param _bDeleteDataInTargetDatabase Flag if data in target table should be
	 *                                     deleted before data is copied
	 * @throws SQLException
	 */
	public static void addDataFromTableToOtherTableDefaultColumnType(Connection _oMySQLConnectionSource,
			String _sSourceTable, Connection _oMySQLConnectionDestination, String _sDestinationTableName,
			String _sDefaultColumnType, boolean _bAllowRowGenId, boolean _bCreateAndShowRows, boolean _bMakeDupeRow,
			boolean _bDeleteDataInTargetDatabase) throws SQLException {

		if (_bDeleteDataInTargetDatabase) {
			executeSQL(_oMySQLConnectionDestination, "DELETE FROM " + _sDestinationTableName);
		}

		ResultSet oSourceRs = querySQL(_oMySQLConnectionSource, "SELECT * FROM " + _sSourceTable);
		ResultSet oDestinationRs = querySQL(_oMySQLConnectionDestination, "SELECT * FROM " + _sDestinationTableName);

		LOGGER.info("copying " + _oMySQLConnectionSource + "." + _sSourceTable + " to " + _oMySQLConnectionDestination
				+ "." + _sDestinationTableName);

		ArrayList<String> oDefaultNCOAFields = getStandardNCOAFields();
		boolean bLastDestinationFieldFound = false;
		String sLastDestinationFieldBeforeNCOA = "";

		// _oDestinationRs.first();
		ResultSetMetaData oRsMd = oDestinationRs.getMetaData();
		LinkedHashMap<String, String> oDestinationFieldsMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oDestinationFieldsMap.put(sColumnName, sColumnName);
			if (!oDefaultNCOAFields.contains(sColumnName)) {
				if (!bLastDestinationFieldFound) {
					sLastDestinationFieldBeforeNCOA = sColumnName;
				}
			} else {
				bLastDestinationFieldFound = true;
			}
		}

		// _oSourceRs.first();
		oRsMd = oSourceRs.getMetaData();
		LinkedHashMap<String, String> oSourceFieldsMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oSourceFieldsMap.put(sColumnName, sColumnName);
		}

		for (Iterator<String> j = oDestinationFieldsMap.keySet().iterator(); j.hasNext();) {
			String sColumnName = (String) j.next();
			if (oSourceFieldsMap.containsKey(sColumnName)) {
				oSourceFieldsMap.remove(sColumnName);
			}
		}
		if (_bCreateAndShowRows) {
			String sColumnToAddAfter = sLastDestinationFieldBeforeNCOA;
			StringBuffer oColumnsCreated = new StringBuffer();
			for (Iterator<String> j = oSourceFieldsMap.keySet().iterator(); j.hasNext();) {
				String sColumnName = (String) j.next();
				String sSQL = "ALTER TABLE " + _sDestinationTableName + " ADD COLUMN " + sColumnName + " "
						+ _sDefaultColumnType + " AFTER " + sColumnToAddAfter;
				oColumnsCreated.append(sColumnName + ", ");
				executeSQL(_oMySQLConnectionDestination, sSQL);
				sColumnToAddAfter = sColumnName;
			}
			if (oColumnsCreated.length() > 2) {
				oColumnsCreated = new StringBuffer(oColumnsCreated.substring(0, oColumnsCreated.length() - 2));
				LOGGER.info("Columns Created: " + oColumnsCreated.toString());
			}
		}

		// _oSourceRs.first();
		while (oSourceRs.next()) {

			HashMap<String, String> oNulledColumns = new HashMap<String, String>();

			StringBuffer oPostInsertBuffer = new StringBuffer();
			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = XSaLTStringUtils.regExMakeDataColumnName(oRsMd.getColumnName(i + 1).toUpperCase());
				String sTempInsertValue = "";

				if (_bAllowRowGenId) {

					sTempInsertValue = "'" + XSaLTStringUtils.regExReplaceStringForInsert(XSaLTStringUtils
							.getEmptyStringIfNull(oSourceRs.getString(oRsMd.getColumnName(i + 1).toUpperCase())))
							+ "', ";

				} else {
					if (!sColumnName.equalsIgnoreCase("ROWGENID")) {

						sTempInsertValue = "'" + XSaLTStringUtils.regExReplaceStringForInsert(XSaLTStringUtils
								.getEmptyStringIfNull(oSourceRs.getString(oRsMd.getColumnName(i + 1).toUpperCase())))
								+ "', ";

					}
				}

				if (sTempInsertValue.equals("'', ")) {
					oNulledColumns.put(Long.valueOf(i).toString(), Long.valueOf(i).toString());
				} else {
					oPostInsertBuffer.append(sTempInsertValue);
				}

			}

			oPostInsertBuffer = new StringBuffer(oPostInsertBuffer.substring(0, oPostInsertBuffer.length() - 2));
			oPostInsertBuffer.append(")");

			StringBuffer oPreInsertBuffer = new StringBuffer();
			oPreInsertBuffer.append("INSERT INTO " + _sDestinationTableName.toUpperCase() + " (");

			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = XSaLTStringUtils.regExMakeDataColumnName(oRsMd.getColumnName(i + 1).toUpperCase());
				String sTempColumnValue = "";

				if (!oNulledColumns.containsKey(Long.valueOf(i).toString())) {

					if (_bAllowRowGenId) {
						sTempColumnValue = sColumnName + ", ";
					} else {
						if (!sColumnName.equalsIgnoreCase("ROWGENID")) {
							sTempColumnValue = sColumnName + ", ";
							// sColumnName = "ROWGENIDOLD";
						}
					}

				}

				if (sTempColumnValue.equals("")) {
				} else {
					oPreInsertBuffer.append(sTempColumnValue);
				}

			}
			oPreInsertBuffer = new StringBuffer(oPreInsertBuffer.substring(0, oPreInsertBuffer.length() - 2));
			oPreInsertBuffer.append(") VALUES (");
			oPreInsertBuffer.append(oPostInsertBuffer);

			// sysout

			executeSQL(_oMySQLConnectionDestination, oPreInsertBuffer.toString());
			if (_bMakeDupeRow) {
				executeSQL(_oMySQLConnectionDestination, oPreInsertBuffer.toString());
			}

		}

		dropTableInDatabase(_oMySQLConnectionDestination, _sSourceTable);

		oSourceRs = null;
		oDestinationRs = null;

	}

	/*
	 * 
	 * public static void convertCobolDecimalColumns(Connection _oMySQLConnection,
	 * String _sTableName, String _sRowGenIDColumns, HashMap _oColumnsMap) throws
	 * SQLException {
	 * 
	 * StringBuffer oSqlBuffer = new StringBuffer("SELECT " + _sRowGenIDColumns +
	 * ", "); for (Iterator j = _oColumnsMap.keySet().iterator(); j.hasNext();) {
	 * String sColumnName = (String) j.next(); oSqlBuffer.append(sColumnName +
	 * ", "); } oSqlBuffer = new StringBuffer(oSqlBuffer.substring(0,
	 * oSqlBuffer.length() - 2)); oSqlBuffer.append(" FROM " + _sTableName +
	 * " ORDER BY " + _sRowGenIDColumns);
	 * 
	 * ResultSet oRs = querySQL(_oMySQLConnection, oSqlBuffer.toString());
	 * 
	 * 
	 * int nRowCount = 0;
	 * 
	 * while (oRs.next()) { String sRowGenId = oRs.getString(_sRowGenIDColumns);
	 * HashMap<String, String> oTempValueMap = new HashMap<String, String>(); for
	 * (Iterator j = _oColumnsMap.keySet().iterator(); j.hasNext();) { String
	 * sColumnName = (String) j.next(); String sColumnValue =
	 * XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(sColumnName));
	 * sColumnValue =
	 * XSaLTStringUtils.regExStripNonNumbersTwoFromString(sColumnValue); if
	 * (sColumnValue.endsWith("-")) { sColumnValue = "-" + sColumnValue.substring(0,
	 * sColumnValue.length() - 1); } oTempValueMap.put(sColumnName, sColumnValue); }
	 * 
	 * StringBuffer oUpdateBuffer = new StringBuffer("UPDATE " + _sTableName +
	 * " SET "); for (Iterator j = oTempValueMap.keySet().iterator(); j.hasNext();)
	 * { String sColumnName = (String) j.next(); String sColumnValue =
	 * oTempValueMap.get(sColumnName).toString(); String sRealColumnValue = ""; if
	 * (sColumnValue.equals("")) { sRealColumnValue = "null"; } else {
	 * sRealColumnValue = "'" + sColumnValue + "'"; }
	 * oUpdateBuffer.append(sColumnName + " = " + sRealColumnValue + ", "); }
	 * oUpdateBuffer = new StringBuffer(oUpdateBuffer.substring(0,
	 * oUpdateBuffer.length() - 2)); oUpdateBuffer.append(" WHERE " +
	 * _sRowGenIDColumns + " = '" + sRowGenId + "'");
	 * 
	 * executeSQL(_oMySQLConnection, oUpdateBuffer.toString());
	 * 
	 * nRowCount++;
	 * 
	 * if (nRowCount % 500 == 0) { LOGGER.info( "Rows cobol corrected in '" +
	 * _sTableName + "' = " + nRowCount); }
	 * 
	 * }
	 * 
	 * for (Iterator j = _oColumnsMap.keySet().iterator(); j.hasNext();) { String
	 * sColumnName = (String) j.next(); executeSQL(_oMySQLConnection, "ALTER TABLE "
	 * + _sTableName + " MODIFY COLUMN " + sColumnName + " DOUBLE(9, 2)"); }
	 * 
	 * 
	 * }
	 * 
	 */

	/**
	 * This method appends the data from source table data to the destination table.
	 * 
	 * @param _oMySQLConnection      Database connection object
	 * @param _sSourceTableName      Source table
	 * @param _sDestinationTableName Destination table
	 * @param _bDeleteSourceTable    Flag if source table should be removed after
	 *                               copy
	 * @throws SQLException
	 */
	public static void simpleExactTableMerge(Connection _oMySQLConnection, String _sSourceTableName,
			String _sDestinationTableName, boolean _bDeleteSourceTable) throws SQLException {
		ResultSet oRs = getFirstRecord(_oMySQLConnection, "select max(rowgenid) from " + _sDestinationTableName);
		int nRowGenOne = oRs.getInt(1);

		oRs = getFirstRecord(_oMySQLConnection, "select max(rowgenid) from " + _sSourceTableName);
		int nRowGentwo = oRs.getInt(1);

		int nRowGen = 0;
		if (nRowGenOne > nRowGentwo) {
			nRowGen = nRowGenOne;
		} else {
			nRowGen = nRowGentwo;
		}

		// update source table with new rowgens

		executeSQL(_oMySQLConnection, "Update " + _sSourceTableName + " set rowgenid = rowgenid + " + nRowGen);

		// copy

		executeSQL(_oMySQLConnection, "insert into " + _sDestinationTableName + " select * from " + _sSourceTableName);

		if (!_oMySQLConnection.getAutoCommit()) {
			_oMySQLConnection.commit();
		}

		if (_bDeleteSourceTable) {
			dropTableInDatabase(_oMySQLConnection, _sSourceTableName);
		} else {
			// undo rowgen alter
			executeSQL(_oMySQLConnection, "Update " + _sSourceTableName + " set rowgenid = rowgenid - " + nRowGen);
		}

		if (!_oMySQLConnection.getAutoCommit()) {
			_oMySQLConnection.commit();
		}
	}

	/**
	 * This method adds columns and appends data from the _oSourceRs to the
	 * destination table.
	 * 
	 * @param _oMySQLConnection      Database connection object
	 * @param _oSourceRs             Source data result set object
	 * @param _sDestinationTableName Destination table name
	 * @param _oDestinationRs        Destination data result set
	 * @param _sDefaultColumnType    Default data type for newly created columns
	 * @param _bAllowRowGenId        Flag if generated IDs should be copied
	 * @param _bShowRowsCreated      Flag if columns created should be logged
	 * @param _bMakeDupeRow          Flag if duplicate records should be created
	 * @throws SQLException
	 */
	public static void addDataFromTableToOtherTable(Connection _oMySQLConnection, ResultSet _oSourceRs,
			String _sDestinationTableName, ResultSet _oDestinationRs, String _sDefaultColumnType,
			boolean _bAllowRowGenId, boolean _bShowRowsCreated, boolean _bMakeDupeRow) throws SQLException {

		ArrayList<String> oDefaultNCOAFields = getStandardNCOAFields();
		boolean bLastDestinationFieldFound = false;
		String sLastDestinationFieldBeforeNCOA = "";

		// _oDestinationRs.first();
		ResultSetMetaData oRsMd = _oDestinationRs.getMetaData();
		LinkedHashMap<String, String> oDestinationFieldsMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oDestinationFieldsMap.put(sColumnName, sColumnName);
			if (!oDefaultNCOAFields.contains(sColumnName)) {
				if (!bLastDestinationFieldFound) {
					sLastDestinationFieldBeforeNCOA = sColumnName;
				}
			} else {
				bLastDestinationFieldFound = true;
			}
		}

		// _oSourceRs.first();
		oRsMd = _oSourceRs.getMetaData();
		LinkedHashMap<String, String> oSourceFieldsMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oSourceFieldsMap.put(sColumnName, sColumnName);
		}

		for (Iterator<String> j = oDestinationFieldsMap.keySet().iterator(); j.hasNext();) {
			String sColumnName = (String) j.next();
			if (oSourceFieldsMap.containsKey(sColumnName)) {
				oSourceFieldsMap.remove(sColumnName);
			}
		}

		String sColumnToAddAfter = sLastDestinationFieldBeforeNCOA;
		StringBuffer oColumnsCreated = new StringBuffer();
		for (Iterator<String> j = oSourceFieldsMap.keySet().iterator(); j.hasNext();) {
			String sColumnName = (String) j.next();
			String sSQL = "ALTER TABLE " + _sDestinationTableName + " ADD COLUMN " + sColumnName + " "
					+ _sDefaultColumnType + " AFTER " + sColumnToAddAfter;
			oColumnsCreated.append(sColumnName + ", ");
			executeSQL(_oMySQLConnection, sSQL);
			sColumnToAddAfter = sColumnName;
		}
		if (oColumnsCreated.length() > 2) {
			oColumnsCreated = new StringBuffer(oColumnsCreated.substring(0, oColumnsCreated.length() - 2));
			if (_bShowRowsCreated) {
				LOGGER.info("Columns Created: " + oColumnsCreated.toString());
			}
		}

		// _oSourceRs.first();
		while (_oSourceRs.next()) {
			StringBuffer oInsertBuffer = new StringBuffer();
			oInsertBuffer.append("INSERT INTO " + _sDestinationTableName.toUpperCase() + " (");

			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = XSaLTStringUtils.regExMakeDataColumnName(oRsMd.getColumnName(i + 1).toUpperCase());

				if (_bAllowRowGenId) {

					oInsertBuffer.append(sColumnName + ", ");
				} else {

					if (!sColumnName.equalsIgnoreCase("ROWGENID")) {
						oInsertBuffer.append(sColumnName + ", ");
						// sColumnName = "ROWGENIDOLD";
					}
				}

			}
			oInsertBuffer = new StringBuffer(oInsertBuffer.substring(0, oInsertBuffer.length() - 2));
			oInsertBuffer.append(") VALUES (");

			for (int i = 0; i < oRsMd.getColumnCount(); i++) {
				String sColumnName = XSaLTStringUtils.regExMakeDataColumnName(oRsMd.getColumnName(i + 1).toUpperCase());

				if (_bAllowRowGenId) {
					oInsertBuffer
							.append("'"
									+ XSaLTStringUtils
											.regExReplaceStringForInsert(XSaLTStringUtils.getEmptyStringIfNull(
													_oSourceRs.getString(oRsMd.getColumnName(i + 1).toUpperCase())))
									+ "', ");

				} else {
					if (!sColumnName.equalsIgnoreCase("ROWGENID")) {
						oInsertBuffer
								.append("'"
										+ XSaLTStringUtils
												.regExReplaceStringForInsert(XSaLTStringUtils.getEmptyStringIfNull(
														_oSourceRs.getString(oRsMd.getColumnName(i + 1).toUpperCase())))
										+ "', ");
					}
				}

			}

			oInsertBuffer = new StringBuffer(oInsertBuffer.substring(0, oInsertBuffer.length() - 2));
			oInsertBuffer.append(")");

			executeSQL(_oMySQLConnection, oInsertBuffer.toString());

			if (_bMakeDupeRow) {
				executeSQL(_oMySQLConnection, oInsertBuffer.toString());
			}

		}

	}

	/**
	 * This method adds NCOA columns to the specified table
	 * 
	 * @param _oMySQLConnection   Database connection object
	 * @param _sTableName         Table for action
	 * @param _sDefaultColumnType Data type for column(s) to add
	 * @throws SQLException
	 */
	public static void addDefaultNCOAFillInColumnsToTable(Connection _oMySQLConnection, String _sTableName,
			String _sDefaultColumnType) throws SQLException {

		ArrayList<String> oDefaultNCOAFields = getStandardNCOAFields();
		LinkedHashMap<String, String> oExistingDataFieldsMap = new LinkedHashMap<String, String>();

		ResultSet oDestinationRs = querySQL(_oMySQLConnection, "SELECT * FROM " + _sTableName);
		ResultSetMetaData oRsMd = oDestinationRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			oExistingDataFieldsMap.put(sColumnName, sColumnName);
		}

		Iterator<String> oIterator = oDefaultNCOAFields.iterator();
		while (oIterator.hasNext()) {
			String sColumnName = (String) oIterator.next();
			if (!oExistingDataFieldsMap.containsValue(sColumnName)) {
				executeSQL(_oMySQLConnection,
						"ALTER TABLE " + _sTableName + " ADD COLUMN " + sColumnName + " " + _sDefaultColumnType);
			}
		}

	}

	/**
	 * This method returns a LinkedHashMap of the fields for the National Change of
	 * Address (NCOA) data.
	 * 
	 * @return ArrayList of NCOA fields
	 */
	public static ArrayList<String> getStandardNCOAFields() {
		ArrayList<String> oNCOAAddlFieldsMap = new ArrayList<String>();
		oNCOAAddlFieldsMap.add("X_______X");
		oNCOAAddlFieldsMap.add("FULLNAME");
		oNCOAAddlFieldsMap.add("ZIP");
		oNCOAAddlFieldsMap.add("PLUS4");
		oNCOAAddlFieldsMap.add("DPBC");
		oNCOAAddlFieldsMap.add("ADDRETCD");
		oNCOAAddlFieldsMap.add("NCOAADD1");
		oNCOAAddlFieldsMap.add("NCOAADD2");
		oNCOAAddlFieldsMap.add("NCOACITY");
		oNCOAAddlFieldsMap.add("NCOASTATE");
		oNCOAAddlFieldsMap.add("NCOAZIP");
		oNCOAAddlFieldsMap.add("NCOAPLUS4");
		oNCOAAddlFieldsMap.add("NCOADPBC");
		oNCOAAddlFieldsMap.add("RETURNCD");
		oNCOAAddlFieldsMap.add("MOVETYPE");
		oNCOAAddlFieldsMap.add("MAILSCORE");
		oNCOAAddlFieldsMap.add("MOVEDATE");
		oNCOAAddlFieldsMap.add("ADDERR");
		oNCOAAddlFieldsMap.add("PAGENO");
		oNCOAAddlFieldsMap.add("PAGECNT");
		oNCOAAddlFieldsMap.add("SEQNO");
		oNCOAAddlFieldsMap.add("CTNBRK");
		oNCOAAddlFieldsMap.add("PKGBRK");
		oNCOAAddlFieldsMap.add("ENDORSE");
		oNCOAAddlFieldsMap.add("CTNNO");
		oNCOAAddlFieldsMap.add("PKGNO");
		oNCOAAddlFieldsMap.add("MAILPCNO");
		oNCOAAddlFieldsMap.add("POSTAGE");
		oNCOAAddlFieldsMap.add("OCITY");
		oNCOAAddlFieldsMap.add("NCITY");
		oNCOAAddlFieldsMap.add("FILECODE");
		return oNCOAAddlFieldsMap;

	}

	/**
	 * This method creates a new table based on an existing meta data object.
	 * 
	 * @param _oAccessMetaData     Description of the source table in ODBC format
	 * @param _oMySQLConnection    Database connection object
	 * @param _sNewImportTableName Name for new table
	 * @param _sTableName          Table name (not used)
	 * @param _sDefaultColumnType  Default data type for newly create columns
	 * @param _sTableType          Engine type for newly create table (MyISAM or
	 *                             InnoDB)
	 * @throws SQLException
	 */
	public static void createMySqlTableFromODBCMetaData(ResultSetMetaData _oAccessMetaData,
			Connection _oMySQLConnection, String _sNewImportTableName, String _sTableName, String _sDefaultColumnType,
			String _sDefaultTableType) throws SQLException {

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		dropTableInDatabase(_oMySQLConnection, _sNewImportTableName.toUpperCase());

		StringBuffer oCreateSqlStringbuffer = new StringBuffer(
				"CREATE TABLE " + _sNewImportTableName.toUpperCase() + " (");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append("\n\tROWGENID serial PRIMARY KEY, ");
		} else {
			oCreateSqlStringbuffer.append("\n\tROWGENID bigint(20) NOT NULL auto_increment PRIMARY KEY, ");
		}

		for (int i = 0; i < _oAccessMetaData.getColumnCount(); i++) {
			oCreateSqlStringbuffer.append("\n\t"
					+ XSaLTStringUtils.regExMakeDataColumnName(_oAccessMetaData.getColumnName(i + 1).toUpperCase())
					+ "	");
			oCreateSqlStringbuffer.append(_sDefaultColumnType + "");

			if (_sDefaultColumnType.toUpperCase().indexOf("CHAR") != -1) {
				oCreateSqlStringbuffer.append(" DEFAULT '' ");
			}
			oCreateSqlStringbuffer.append(", ");
		}
		oCreateSqlStringbuffer = new StringBuffer(
				oCreateSqlStringbuffer.substring(0, oCreateSqlStringbuffer.length() - 2));

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append(")");
		} else {
			oCreateSqlStringbuffer.append(")\nENGINE=" + _sDefaultTableType);
		}

		executeSQL(_oMySQLConnection, oCreateSqlStringbuffer.toString());

	}

	/**
	 * This method removes columns from the table where no data is present.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @throws SQLException
	 */
	public static void dropUnusedColumnsInTable(Connection _oConnection, String _sTableName) throws SQLException {
		String sSQL = "SELECT * FROM " + _sTableName;
		ResultSet oRs = querySQL(_oConnection, sSQL);
		ResultSetMetaData oRsMd = oRs.getMetaData();

		ArrayList<String> oNCOAFields = getStandardNCOAFields();

		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			String sSQL2 = "SELECT max(length(" + sColumnName + ")) AS MAX_LENGTH FROM " + _sTableName;
			ResultSet oRs2 = querySQL(_oConnection, sSQL2);
			oRs2.first();
			int nMaxLength = oRs2.getInt("MAX_LENGTH");
			if (nMaxLength == 0) {
				if (!oNCOAFields.contains(sColumnName.toUpperCase())) {
					LOGGER.info("can delete " + sColumnName + " in table " + _sTableName);
					dropColumnInTable(_oConnection, _sTableName, sColumnName);
				}
			}

		}

	}

	/**
	 * This method iterates through the nested HashMap to insert/update/delete data
	 * represented in the HashMap.
	 * 
	 * @param _oConnection                Database connection object
	 * @param _oXSaLTDatabaseHashMap      Nested HashMap
	 * @param _sSpecificInputToProcess    Input type to process (INSERT, UPDATE,
	 *                                    DELETE or null (process all))
	 * @param _bConvertInputToUppercase   Flag if input should be converted to upper
	 *                                    case
	 * @param _bInactivateInsteadOfDelete Flag if record should be inactive instead
	 *                                    of deleted
	 * @param _bReactivateInsteadOfInsert Flag if record should be reactivated
	 *                                    instead of inserted
	 * @param _sOverrideWhereClause       Where clause to override standard delete
	 *                                    where clause
	 * @return Last generated key from data in nested HashMap
	 * @throws SQLException
	 */
	public static String processXSaLTDatabaseHashMap(Connection _oConnection,
			HashMap<String, HashMap<String, HashMap<String, String>>> _oXSaLTDatabaseHashMap,
			String _sSpecificInputToProcess, boolean _bConvertInputToUppercase, boolean _bInactivateInsteadOfDelete,
			boolean _bReactivateInsteadOfInsert, String _sOverrideWhereClause) throws SQLException {

		String sLastGenKey = "";
		String sPkValueToReturn = null;
		StringBuffer oValuesReactivated = new StringBuffer();

		for (Iterator<String> i = _oXSaLTDatabaseHashMap.keySet().iterator(); i.hasNext();) {
			String sKeyOne = (String) i.next();

			if (_sSpecificInputToProcess == null
					|| sKeyOne.toUpperCase().indexOf(_sSpecificInputToProcess.toUpperCase()) != -1) {

				HashMap<String, HashMap<String, String>> oTempHashMapOne = (HashMap<String, HashMap<String, String>>) _oXSaLTDatabaseHashMap
						.get(sKeyOne);

				String as[] = sKeyOne.split("__");

				String sTableName = XSaLTStringUtils.getEmptyStringIfNull(as[0]);

				if (sTableName.indexOf("XXX") == -1) {

					String sEditMode = XSaLTStringUtils.getEmptyStringIfNull(as[1]);

					StringBuffer sSQLBuffer = new StringBuffer();
					String sWhereColumn = "";

					if (sEditMode.equalsIgnoreCase("NEW")) {
						sSQLBuffer.append("INSERT INTO " + sTableName + " (");

						StringBuffer oInsertColumns = new StringBuffer();
						StringBuffer oInsertValues = new StringBuffer();

						boolean bUpdateNotInsert = false;

						for (Iterator<String> j = oTempHashMapOne.keySet().iterator(); j.hasNext()
								&& !bUpdateNotInsert;) {
							String sKeyTwo = (String) j.next();
							HashMap<String, String> oValueTwo = (HashMap<String, String>) oTempHashMapOne.get(sKeyTwo);
							String sInsertValue = XSaLTStringUtils
									.regExReplaceStringForInsert(oValueTwo.get("VALUE_OF_REQUEST_ITEM").toString());

							if (sKeyTwo.indexOf("UDATIZ") != -1) {
								sInsertValue = XSaLTStringUtils.formatTimestampForMySQL(sInsertValue);
							} else if (sKeyTwo.indexOf("UDATEZ") != -1) {
								sInsertValue = XSaLTStringUtils.formatDateForMySQLFromSlashedDate(sInsertValue);
							}

							if (oValueTwo.get("IS_UNIQUE_FLAG") != null
									&& oValueTwo.get("IS_UNIQUE_FLAG").equalsIgnoreCase("true")
									&& _bReactivateInsteadOfInsert) {
								ResultSet oRs = XSaLTDataUtils.querySQL(_oConnection, "SELECT * FROM " + sTableName
										+ " WHERE " + sKeyTwo + " = '" + sInsertValue + "'");
								if (oRs.first()) {
									bUpdateNotInsert = true;
									sSQLBuffer = new StringBuffer();
									sSQLBuffer.append("UPDATE " + sTableName + " SET FLAG_IS_ACTIVE = 1 WHERE "
											+ sKeyTwo + " = '" + sInsertValue + "'");

									oValuesReactivated.append(sInsertValue + ",");
								}
							}

							oInsertColumns.append(sKeyTwo + ", ");
							if (sInsertValue.equals("")) {
								oInsertValues.append("null, ");
							} else {
								if (oValueTwo.get("USE_QUOTES_FLAG").toString().equalsIgnoreCase("true")) {
									oInsertValues.append("'" + sInsertValue + "', ");
								} else {
									oInsertValues.append(sInsertValue + ", ");
								}
							}

						}

						oInsertColumns = new StringBuffer(oInsertColumns.substring(0, oInsertColumns.length() - 2));
						oInsertValues = new StringBuffer(oInsertValues.substring(0, oInsertValues.length() - 2));
						if (!bUpdateNotInsert) {
							sSQLBuffer.append(oInsertColumns + ") VALUES (" + oInsertValues + ")");
						}

					} else if (sEditMode.equalsIgnoreCase("DELETE")) {

						for (Iterator<String> j = oTempHashMapOne.keySet().iterator(); j.hasNext();) {
							String sKeyTwo = (String) j.next();
							HashMap<String, String> oValueTwo = (HashMap<String, String>) oTempHashMapOne.get(sKeyTwo);
							for (Iterator<String> l = oValueTwo.keySet().iterator(); l.hasNext();) {
								String sKeyThree = (String) l.next();
								String oValueThree = (String) oValueTwo.get(sKeyThree);

								if (sKeyThree.equalsIgnoreCase("IS_PK_FIELD_FLAG")
										&& oValueThree.equalsIgnoreCase("true")) {
									sWhereColumn = sKeyTwo;
								}
							}
						}

						StringBuffer oWhereBuffer = new StringBuffer();
						HashMap<String, String> oValueOne = (HashMap<String, String>) oTempHashMapOne.get(sWhereColumn);
						if (oValueOne.get("USE_QUOTES_FLAG").toString().equalsIgnoreCase("true")) {
							oWhereBuffer.append("'" + XSaLTStringUtils.regExReplaceStringForInsert(
									oValueOne.get("VALUE_OF_REQUEST_ITEM").toString()) + "'");
						} else {
							oWhereBuffer.append("" + XSaLTStringUtils.regExReplaceStringForInsert(
									oValueOne.get("VALUE_OF_REQUEST_ITEM").toString()) + "");
						}

						if (_bInactivateInsteadOfDelete) {

							if (_sOverrideWhereClause == null) {
								sSQLBuffer.append("UPDATE " + sTableName + " SET FLAG_IS_ACTIVE = '0' WHERE "
										+ sWhereColumn + " = " + oWhereBuffer);
							} else {
								sSQLBuffer.append("UPDATE " + sTableName + " SET FLAG_IS_ACTIVE = '0' WHERE "
										+ _sOverrideWhereClause);
							}

						} else {

							if (_sOverrideWhereClause == null) {
								sSQLBuffer.append(
										"DELETE FROM " + sTableName + " WHERE " + sWhereColumn + " = " + oWhereBuffer);
							} else {
								sSQLBuffer.append("DELETE FROM " + sTableName + " WHERE " + _sOverrideWhereClause);
							}

						}

					} else {
						sSQLBuffer.append("UPDATE " + sTableName + " SET ");

						StringBuffer oInsertColumnsValues = new StringBuffer();

						boolean bDirtyFlagFoundGlobal = false;
						boolean bDirtyFlagTrue = false;

						for (Iterator<String> j = oTempHashMapOne.keySet().iterator(); j.hasNext();) {
							String sKeyTwo = (String) j.next();
							HashMap<String, String> oValueTwo = (HashMap<String, String>) oTempHashMapOne.get(sKeyTwo);

							boolean bDirtyFlagFound = false;

							if (sKeyTwo.equalsIgnoreCase("FLAG_IS_DIRTY")) {
								bDirtyFlagFound = true;
								bDirtyFlagFoundGlobal = true;
								bDirtyFlagTrue = Boolean.valueOf(oValueTwo.get("VALUE_OF_REQUEST_ITEM").toString());
							}

							if (!bDirtyFlagFound) {

								oInsertColumnsValues.append(sKeyTwo + " = ");

								if (oValueTwo.get("USE_QUOTES_FLAG").toString().equalsIgnoreCase("true")) {
									String sInsertValue = XSaLTStringUtils.regExReplaceStringForInsert(
											oValueTwo.get("VALUE_OF_REQUEST_ITEM").toString());

									if (sKeyTwo.indexOf("UDATIZ") != -1) {
										sInsertValue = XSaLTStringUtils.formatTimestampForMySQL(sInsertValue);
									} else if (sKeyTwo.indexOf("UDATEZ") != -1) {
										sInsertValue = XSaLTStringUtils.formatDateForMySQLFromSlashedDate(sInsertValue);
									}

									String sInsertString = "'" + sInsertValue + "', ";

									if (sInsertString.equals("'', ")) {
										oInsertColumnsValues.append("null, ");
									} else {
										oInsertColumnsValues.append(sInsertString);
									}

								} else {
									String sInsertValue = XSaLTStringUtils.regExReplaceStringForInsert(
											oValueTwo.get("VALUE_OF_REQUEST_ITEM").toString());

									if (sKeyTwo.indexOf("UDATIZ") != -1) {
										sInsertValue = XSaLTStringUtils.formatTimestampForMySQL(sInsertValue);
									} else if (sKeyTwo.indexOf("UDATEZ") != -1) {
										sInsertValue = XSaLTStringUtils.formatDateForMySQLFromSlashedDate(sInsertValue);
									}

									String sInsertString = "" + sInsertValue + ", ";

									if (sInsertString.equals(", ")) {
										oInsertColumnsValues.append("null, ");
									} else {
										oInsertColumnsValues.append(sInsertString);
									}
								}

								if (oValueTwo.get("IS_PK_FIELD_FLAG") != null
										&& oValueTwo.get("IS_PK_FIELD_FLAG").toString().equalsIgnoreCase("true")) {
									sWhereColumn = sKeyTwo;
									sPkValueToReturn = (oValueTwo.get("VALUE_OF_REQUEST_ITEM") == null ? ""
											: oValueTwo.get("VALUE_OF_REQUEST_ITEM"));
								}

							}

						}

						oInsertColumnsValues = new StringBuffer(
								oInsertColumnsValues.substring(0, oInsertColumnsValues.length() - 2));

						StringBuffer oWhereBuffer = new StringBuffer();
						HashMap<String, String> oValueOne = (HashMap<String, String>) oTempHashMapOne.get(sWhereColumn);

						if (oValueOne.get("USE_QUOTES_FLAG") != null
								&& oValueOne.get("USE_QUOTES_FLAG").toString().equalsIgnoreCase("true")) {

							oWhereBuffer.append("'" + XSaLTStringUtils.regExReplaceStringForInsert(
									oValueOne.get("VALUE_OF_REQUEST_ITEM").toString()) + "'");
						} else {
							oWhereBuffer.append("" + XSaLTStringUtils.regExReplaceStringForInsert(
									oValueOne.get("VALUE_OF_REQUEST_ITEM").toString()) + "");
						}

						if (_sOverrideWhereClause == null) {
							sSQLBuffer.append(oInsertColumnsValues + " WHERE " + sWhereColumn + " = " + oWhereBuffer);
						} else {
							sSQLBuffer.append(oInsertColumnsValues + " WHERE " + _sOverrideWhereClause);
						}

						if (bDirtyFlagFoundGlobal) {
							// dirty flag was found in edit
							if (!bDirtyFlagTrue) {
								sSQLBuffer = null;
							}
						}

					}

					if (sSQLBuffer != null) {
						if (_bConvertInputToUppercase) {
							// LOGGER.info("upper:" +
							// sSQLBuffer.toString().toUpperCase());
							sLastGenKey = executeSQLGetKey(_oConnection, sSQLBuffer.toString().toUpperCase());
						} else {
							// LOGGER.info("lower:" +
							// sSQLBuffer.toString().toUpperCase());
							sLastGenKey = executeSQLGetKey(_oConnection, sSQLBuffer.toString());
						}
						/**/

					}

				}

			}

		}

		sLastGenKey = (sPkValueToReturn == null ? sLastGenKey : sPkValueToReturn);

		if (oValuesReactivated.length() > 0) {
			sLastGenKey = sLastGenKey + "__REACTIVATED__"
					+ oValuesReactivated.deleteCharAt(oValuesReactivated.length() - 1).toString();
		}

		return sLastGenKey;
	}

	/**
	 * This method replicates a table from an Oracle database to a MySQL database.
	 * 
	 * @param _oODBCConn           ODBC database connection object
	 * @param _oMySQLConnection    MySQL database connection object
	 * @param _sNewImportTableName Table name to use in the MySQL database
	 * @param _sTableName          Table name from the Oracle database
	 * @param _sDefaultColumnType  Data type for each column
	 * @param _sTableType          MySQL engine type for tables (MyISAM or InnoDB)
	 * @throws SQLException
	 */
	public static void insertODBCDataIntoMySQLTable(Connection _oODBCConn, Connection _oMySQLConnection,
			String _sNewImportTableName, String _sTableName, String _sDefaultColumnType, String _sTableType)
			throws SQLException {
		StringBuffer oInsertBuffer = new StringBuffer();

		ResultSet oRsMysql = querySQL(_oODBCConn, "SELECT * FROM " + _sTableName);
		ResultSetMetaData _oODBCMetaData = oRsMysql.getMetaData();
		createMySqlTableFromODBCMetaData(_oODBCMetaData, _oMySQLConnection, _sNewImportTableName, _sTableName,
				_sDefaultColumnType, _sTableType);

		while (oRsMysql.next()) {
			oInsertBuffer = new StringBuffer();
			oInsertBuffer.append("INSERT INTO " + _sNewImportTableName.toUpperCase() + " (");

			for (int i = 0; i < _oODBCMetaData.getColumnCount(); i++) {
				oInsertBuffer.append(
						XSaLTStringUtils.regExMakeDataColumnName(_oODBCMetaData.getColumnName(i + 1).toUpperCase())
								+ ", ");
			}
			oInsertBuffer = new StringBuffer(oInsertBuffer.substring(0, oInsertBuffer.length() - 2));
			oInsertBuffer.append(") VALUES (");

			for (int i = 0; i < _oODBCMetaData.getColumnCount(); i++) {

				oInsertBuffer
						.append("'"
								+ XSaLTStringUtils.regExReplaceStringForInsert(XSaLTStringUtils.getEmptyStringIfNull(
										oRsMysql.getString(_oODBCMetaData.getColumnName(i + 1).toUpperCase())))
								+ "', ");

			}

			oInsertBuffer = new StringBuffer(oInsertBuffer.substring(0, oInsertBuffer.length() - 2));
			oInsertBuffer.append(")");

			executeSQL(_oMySQLConnection, oInsertBuffer.toString());

		}

	}

	/*
	 * -----------------------------------------------------------------------------
	 * This section contains general database execution routines
	 * -----------------------------------------------------------------------------
	 */

	/**
	 * Drops a table in a database schema
	 * 
	 * @param _oConnection The connection
	 * @param _sTableName  The table name to drop
	 * @throws SQLException
	 * @throws SQLException
	 */
	public static void dropTableInDatabase(Connection _oConnection, String _sTableName) {
		try {
			executeSQL(_oConnection, "DROP TABLE IF EXISTS " + _sTableName.toUpperCase());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * Drops a column in a data table
	 * 
	 * @param _oConnection The connection
	 * @param _sTableName  The table name
	 * @param _sColumnName The column name to drop
	 */
	public static void dropColumnInTable(Connection _oConnection, String _sTableName, String _sColumnName) {

		try {
			executeSQL(_oConnection,
					"ALTER TABLE " + _sTableName.toUpperCase() + " DROP COLUMN " + _sColumnName.toUpperCase());
		} catch (Exception e) {
		}

	}

	/**
	 * This method drops a list of columns from a given table.
	 * 
	 * @param _oConnection        Database connection object
	 * @param _sTableName         Table for action
	 * @param _oColumnNamesToDrop List of columns to drop
	 */
	public static void dropColumnsInTable(Connection _oConnection, String _sTableName,
			ArrayList<String> _oColumnNamesToDrop) {
		for (int i = 0; i > _oColumnNamesToDrop.size(); i++) {
			try {
				executeSQL(_oConnection, "ALTER TABLE " + _sTableName.toUpperCase() + " DROP COLUMN "
						+ _oColumnNamesToDrop.get(i).toString().toUpperCase());
			} catch (Exception e) {
			}
		}

	}

	/**
	 * This method drops all columns from a table except the ones specified in the
	 * list.
	 * 
	 * @param _oConnection        Database connection object
	 * @param _sTableName         Table for action
	 * @param _oColumnNamesToKeep Names of columns to keep
	 */
	public static void keepColumnsInTable(Connection _oConnection, String _sTableName,
			ArrayList<String> _oColumnNamesToKeep) {

		try {
			HashMap<String, String> oKeepMap = new HashMap<String, String>();
			for (int i = 0; i > _oColumnNamesToKeep.size(); i++) {
				oKeepMap.put(_oColumnNamesToKeep.get(i).toString().toUpperCase(),
						_oColumnNamesToKeep.get(i).toString().toUpperCase());
			}

			String sCurrentColumn = "";

			ResultSet oTestRs = querySQL(_oConnection, "SELECT * FROM " + _sTableName);
			if (oTestRs.next()) {
				ResultSetMetaData oRsMd = oTestRs.getMetaData();
				for (int i = 0; i < oRsMd.getColumnCount(); i++) {
					sCurrentColumn = oRsMd.getColumnName(i + 1).toUpperCase();

					if (!oKeepMap.containsKey(sCurrentColumn)) {

						executeSQL(_oConnection,
								"ALTER TABLE " + _sTableName.toUpperCase() + " DROP COLUMN " + sCurrentColumn);
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * This method adds a column to the given table.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Column to add
	 * @param _sColumnType Column data definition
	 */
	public static void addColumnInTable(Connection _oConnection, String _sTableName, String _sColumnName,
			String _sColumnType) {

		try {
			executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " " + _sColumnType);
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * This method adds a column to a given table after the specified column.
	 * 
	 * @param _oConnection           Database connection object
	 * @param _sTableName            Table for action
	 * @param _sNewColumnName        Column to add
	 * @param _sPreceedingColumnName Column to add after
	 * @param _sColumnType           Column data definition
	 */
	public static void addColumnInTableAfterAnother(Connection _oConnection, String _sTableName, String _sNewColumnName,
			String _sPreceedingColumnName, String _sColumnType) {

		try {
			executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sNewColumnName + " "
					+ _sColumnType + " after " + _sPreceedingColumnName);
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * This method adds a column to a given table with a default value for the new
	 * column.
	 * 
	 * @param _oConnection   Database connection object
	 * @param _sTableName    Table for action
	 * @param _sColumnName   Column to add
	 * @param _sColumnType   Column data definition
	 * @param _sDefaultValue Default value for new column
	 * @throws SQLException
	 */
	public static void addColumnInTableWithDefaultValue(Connection _oConnection, String _sTableName,
			String _sColumnName, String _sColumnType, String _sDefaultValue) throws SQLException {
		try {
			executeSQL(_oConnection, "ALTER TABLE " + _sTableName + " ADD COLUMN " + _sColumnName + " " + _sColumnType
					+ " DEFAULT '" + _sDefaultValue + "'");
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * This method coalesces the specified fields into a new field in the given
	 * table.
	 * 
	 * @param _oConn                 Database connection object
	 * @param _sDataTableName        Table for action
	 * @param _sPrimaryKeyColumnName Column name for primary key
	 * @param _sFullFieldName        Column name for new field
	 * @param _oFieldsToCoalesce     List of fields to coalesce
	 * @param _sColumnType           New field data type
	 * @throws SQLException
	 */
	public static void coalesceFieldsInDbField(Connection _oConn, String _sDataTableName, String _sPrimaryKeyColumnName,
			String _sFullFieldName, ArrayList<String> _oFieldsToCoalesce, String _sColumnType) throws SQLException {

		if (_sColumnType != null) {
			dropColumnInTable(_oConn, _sDataTableName, _sFullFieldName);
			executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sFullFieldName + " " + _sColumnType
					+ " DEFAULT ''");
		}

		StringBuffer oSelectColumns = new StringBuffer(_sPrimaryKeyColumnName + ", ");
		for (int i = 0; i < _oFieldsToCoalesce.size(); i++) {
			if (_oFieldsToCoalesce.get(i).startsWith("@")) {
				oSelectColumns.append(
						_oFieldsToCoalesce.get(i).toString().substring(1, _oFieldsToCoalesce.get(i).length()) + ", ");
			}
		}
		oSelectColumns = new StringBuffer(oSelectColumns.substring(0, oSelectColumns.length() - 2));

		ResultSet oRs = querySQL(_oConn, "SELECT " + oSelectColumns + " FROM " + _sDataTableName);
		while (oRs.next()) {
			String sRowGenID = oRs.getString(_sPrimaryKeyColumnName);
			StringBuffer oFinalValue = new StringBuffer();
			for (int i = 0; i < _oFieldsToCoalesce.size(); i++) {
				if (_oFieldsToCoalesce.get(i).startsWith("@")) {
					String sTempValue = XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(
							_oFieldsToCoalesce.get(i).toString().substring(1, _oFieldsToCoalesce.get(i).length())));
					oFinalValue.append(sTempValue);
				} else {
					oFinalValue.append(_oFieldsToCoalesce.get(i));
				}
			}

			String sFinalValue = oFinalValue.toString().trim();
			sFinalValue = sFinalValue.replaceAll("\\s+", " ");

			executeSQL(_oConn,
					"UPDATE " + _sDataTableName + " SET " + _sFullFieldName + " = '"
							+ sFinalValue.replaceAll("\\s+", " ").trim().replaceAll("'", "`") + "' WHERE "
							+ _sPrimaryKeyColumnName + " = '" + sRowGenID + "'");
		}

	}

	/*
	 * -----------------------------------------------------------------------------
	 * This section contains stuff for importing data from flat files
	 * -----------------------------------------------------------------------------
	 */

	/**
	 * This method gets the count of rows returned for the given SQL statement.
	 * 
	 * @param _oConnection  Database connection object
	 * @param _sSQL         SQL query statement
	 * @param _bIterateRows Flag if rows should be iterated to determine count
	 * @return Number of rows returned from SQL query
	 * @throws SQLException
	 */
	public static long getRowsCountInDataTableWithSQLQuery(Connection _oConnection, String _sSQL, boolean _bIterateRows)
			throws SQLException {

		if (_bIterateRows) {
			ResultSet oRs = querySQL(_oConnection, _sSQL);
			int nDistinctRows = 0;
			while (oRs.next()) {
				nDistinctRows = nDistinctRows + 1;
			}
			return Long.valueOf(nDistinctRows);

		} else {
			ResultSet oRs = querySQL(_oConnection, _sSQL);
			oRs.next();
			return oRs.getLong(1);
		}

	}

	/**
	 * This method returns the number of rows returned from the given where clause.
	 * 
	 * @param _oConnection  Database connection object
	 * @param _sTableName   Table to query
	 * @param _sWhereClause Where clause for narrowing data
	 * @return Number of rows returned from the where clause
	 * @throws SQLException
	 */
	public static long getRowsCountInDataTable(Connection _oConnection, String _sTableName, String _sWhereClause)
			throws SQLException {
		return getRowsCountInDataTableWithSQLQuery(_oConnection,
				"SELECT COUNT(*) FROM " + _sTableName + " WHERE " + _sWhereClause, false);
	}

	/**
	 * This method returns the number of rows in the given table.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sTableName  Table to query
	 * @return Number of rows in the table
	 * @throws SQLException
	 */
	public static long getRowsCountInDataTable(Connection _oConnection, String _sTableName) throws SQLException {
		ResultSet oRs = querySQL(_oConnection, "SELECT COUNT(*) FROM " + _sTableName);
		oRs.next();
		return oRs.getLong(1);
	}

	/**
	 * This method returns the number of records in the table for the given
	 * selection criteria.
	 * 
	 * @param _oConnection  Database connection object
	 * @param _sTableName   Table to query
	 * @param _sColumnName  Column to count
	 * @param _sWhereClause Where clause for narrowing data
	 * @return Number of rows returned from the where clause
	 * @throws SQLException
	 */
	public static long getRowsCountInDataTable(Connection _oConnection, String _sTableName, String _sColumnName,
			String _sWhereClause) throws SQLException {

		ResultSet oRs = querySQL(_oConnection,
				"SELECT COUNT(" + _sColumnName + ") FROM " + _sTableName + " WHERE " + _sWhereClause);
		oRs.next();

		return oRs.getLong(1);
	}

	/**
	 * This method will calculate the size of the given result set and return the
	 * value as a "long".
	 * 
	 * @param _oRs Result set to determine the size of
	 * @return Number of rows in the result set
	 * @throws SQLException
	 */
	public static long getRowCountFromResultSet(ResultSet _oRs) throws SQLException {
		long lRowCount = 0;
		if (_oRs != null) {
			_oRs.beforeFirst();
			while (_oRs.next()) {
				lRowCount++;
			}
			_oRs.beforeFirst();
		}
		return lRowCount;
	}

	/**
	 * This method imports data from one table into another table.
	 * 
	 * @param _oImportConnection     Database connection object
	 * @param _sImportTableName      Source table name
	 * @param _sDestinationTableName Destination table name
	 * @param _oFieldsMap            Map of fields to import
	 * @throws SQLException
	 */
	public static void importFieldsFromOtherTable(Connection _oImportConnection, String _sImportTableName,
			String _sDestinationTableName, HashMap<String, String> _oFieldsMap) throws SQLException {

		importFieldsFromOtherTable(_oImportConnection, _sImportTableName, _oImportConnection, _sDestinationTableName,
				_oFieldsMap);

	}

	/**
	 * This method imports data from one table into another table.
	 * 
	 * @param _oImportConnection      Source database connection object
	 * @param _sImportTableName       Source table name
	 * @param _oDestinationConnection Destination database connection object
	 * @param _sDestinationTableName  Destination table name
	 * @param _oFieldsMap             Map of fields to import
	 * @throws SQLException
	 */
	public static void importFieldsFromOtherTable(Connection _oImportConnection, String _sImportTableName,
			Connection _oDestinationConnection, String _sDestinationTableName, HashMap<String, String> _oFieldsMap)
			throws SQLException {

		ResultSet oRs = querySQL(_oImportConnection, "SELECT * FROM " + _sImportTableName);
		while (oRs.next()) {
			StringBuffer sInsertSqlBuffer = new StringBuffer(
					"INSERT INTO " + _sDestinationTableName.toUpperCase() + " (");

			for (Iterator<String> i = _oFieldsMap.keySet().iterator(); i.hasNext();) {
				String sKey = (String) i.next();
				sInsertSqlBuffer.append(sKey + ", ");
			}

			sInsertSqlBuffer = new StringBuffer(
					sInsertSqlBuffer.substring(0, sInsertSqlBuffer.length() - 2) + ") VALUES (");

			for (Iterator<String> i = _oFieldsMap.keySet().iterator(); i.hasNext();) {
				String sKey = (String) i.next();

				String sValue = null;
				if (_oFieldsMap.get(sKey) != null) {
					sValue = (String) _oFieldsMap.get(sKey);
				}

				if (sValue == null) {
					sInsertSqlBuffer.append("null, ");
				} else if (sValue.equalsIgnoreCase("null")) {
					sInsertSqlBuffer.append("null, ");
				} else if (sValue.equalsIgnoreCase("")) {
					sInsertSqlBuffer.append("null, ");
				} else if (sValue.startsWith("@")) {
					String sInnerValue = XSaLTStringUtils
							.regExReplaceStringForInsert(oRs.getString(sValue.substring(1, sValue.length())));
					if (sInnerValue != null) {
						sInsertSqlBuffer.append("'" + sInnerValue + "', ");
					} else {
						sInsertSqlBuffer.append("null, ");
					}
				} else {
					sInsertSqlBuffer.append("'" + sValue + "', ");
				}

			}
			sInsertSqlBuffer = new StringBuffer("\t" + sInsertSqlBuffer.substring(0, sInsertSqlBuffer.length() - 2));
			sInsertSqlBuffer.append(")");

			executeSQL(_oDestinationConnection, sInsertSqlBuffer.toString());

		}

	}

	/**
	 * This method imports data from a file into a database table.
	 * 
	 * @param _oConnection        Database connection object
	 * @param _sFilePath          Path for data file
	 * @param _oColumnsOrderedmap Map specifying order of columns in table
	 * @param _oDecimalColumns    Columns to make a decimal data type
	 * @param _sTableName         Name of table to create
	 * @param _sDefaultColumnType Default data type for columns
	 * @param _sDefaultTableType  The default data type, can be null
	 * @param _bEmptyColumnAsNull Flag if blank data string should be inserted as
	 *                            NULL
	 * @param _sTableType         Engine type for table (MyISAM or InnoDB- default
	 *                            MyISAM)
	 * @param _nMaxSizeOfData     Maximum length of string to insert
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void importFixedDataFileToDatabase(Connection _oConnection, String _sFilePath,
			LinkedHashMap<String, Integer> _oColumnsOrderedmap, ArrayList<String> _oDecimalColumns, String _sTableName,
			String _sDefaultColumnType, String _sDefaultTableType, boolean _bEmptyColumnAsNull, int _nMaxSizeOfData)
			throws IOException, SQLException {

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		dropTableInDatabase(_oConnection, _sTableName);

		StringBuffer oCreateSqlStringbuffer = new StringBuffer("CREATE TABLE " + _sTableName.toUpperCase() + " (");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append("\n\tROWGENID serial PRIMARY KEY, ");
		} else {
			oCreateSqlStringbuffer.append("\n\tROWGENID bigint(20) NOT NULL auto_increment PRIMARY KEY, ");
		}

		int nColumnCount = 1;

		for (Iterator<String> i = _oColumnsOrderedmap.keySet().iterator(); i.hasNext();) {
			String sKey = (String) i.next();

			if (_oDecimalColumns != null) {
				if (_oDecimalColumns.contains(sKey) == true) {
					oCreateSqlStringbuffer.append("\n\t" + sKey.toUpperCase() + "	DECIMAL(10,2)");
				} else {
					oCreateSqlStringbuffer.append("\n\t" + sKey.toUpperCase() + "	" + _sDefaultColumnType + "");
				}
			} else {
				oCreateSqlStringbuffer.append("\n\t" + sKey.toUpperCase() + "	" + _sDefaultColumnType + "");
			}

			if (_sDefaultColumnType.toUpperCase().indexOf("CHAR") != -1) {
				oCreateSqlStringbuffer.append(" DEFAULT '' ");
			}
			oCreateSqlStringbuffer.append(", ");

			nColumnCount = nColumnCount + 1;
		}

		oCreateSqlStringbuffer = new StringBuffer(
				oCreateSqlStringbuffer.substring(0, oCreateSqlStringbuffer.length() - 2));

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append(")");
		} else {
			oCreateSqlStringbuffer.append(")\nENGINE=" + _sDefaultTableType);
		}

		executeSQL(_oConnection, oCreateSqlStringbuffer.toString());

		LineNumberReader oLineNumberReader = new LineNumberReader(new FileReader(_sFilePath));

		int nRowNo = 1;
		int nInsertedRows = 0;
		String sReadLine = null;
		while ((sReadLine = oLineNumberReader.readLine()) != null && sReadLine != "") {
			insertRowIntoTempWorkTableForFixedDataFileNew(_oConnection, _oColumnsOrderedmap, _oDecimalColumns,
					_sTableName, sReadLine, nRowNo, _bEmptyColumnAsNull, true, _nMaxSizeOfData);
			nInsertedRows = nInsertedRows + 1;
			if (nInsertedRows % 500 == 0) {
				LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nInsertedRows);
			}

		}

		oLineNumberReader.close();

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nInsertedRows);

	}

	/**
	 * This method inserts rows from a fixed-data file into the given table.
	 * 
	 * @param _oConnection         Database connection object
	 * @param _sFilePath           File path of file to import
	 * @param _oColumnsOrderedmap  Map specifying order of columns in table
	 * @param _sTableName          Table to import into
	 * @param _sDefaultColumnType  Data type for columns (not used)
	 * @param _bEmptyColumnAsNull  Flag if data is empty to insert NULL
	 * @param _bConvertToUppercase Flag if data should be in upper case
	 * @param _nMaxSizeOfData      Max length of strings to insert
	 * @param _bSkipFirstRow       Flag if first row in file should be skipped
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void importFixedDataFileToDatabaseNoCreateTable(Connection _oConnection, String _sFilePath,
			LinkedHashMap<String, Integer> _oColumnsOrderedmap, String _sTableName, String _sDefaultColumnType,
			boolean _bEmptyColumnAsNull, boolean _bConvertToUppercase, int _nMaxSizeOfData, boolean _bSkipFirstRow)
			throws IOException, SQLException {

		LineNumberReader oLineNumberReader = new LineNumberReader(new FileReader(_sFilePath));

		int nRowNo = 1;
		int nInsertedRows = 0;
		String sReadLine = null;

		boolean bSkip = _bSkipFirstRow;
		while ((sReadLine = oLineNumberReader.readLine()) != null && sReadLine != "" && sReadLine.length() > 3) {
			if (bSkip) {
				bSkip = false;
				continue;
			}

			insertRowIntoTempWorkTableForFixedDataFileNew(_oConnection, _oColumnsOrderedmap, null, _sTableName,
					sReadLine, nRowNo, _bEmptyColumnAsNull, _bConvertToUppercase, _nMaxSizeOfData);
			nInsertedRows = nInsertedRows + 1;
			if (nInsertedRows % 500 == 0) {
				LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nInsertedRows);
			}

		}

		oLineNumberReader.close();

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nInsertedRows);

	}

	/**
	 * This method creates a table from based on the XSaLTTripleStringLinkedHashMap
	 * then inserts data into the created table from the specified file.
	 * 
	 * @param _oConnection        Database connection object
	 * @param _sFilePath          Path to find the file to import
	 * @param _oTripleHashMap     Object holding column names, types, and sizes.
	 * @param _sTableName         Name of table to import into
	 * @param _sDefaultColumnType Default data type of columns
	 * @param _sDefaultTableType         Engine type for table (MyISAM or InnoDB- default
	 *                            MyISAM)
	 * @param _bEmptyColumnAsNull Flag if data is empty to insert NULL
	 * @param _nMaxSizeOfData     Max length of strings to insert
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void importFixedDataFileToDatabaseWithTripleHashMap(Connection _oConnection, String _sFilePath,
			XSaLTTripleStringLinkedHashMap _oTripleHashMap, String _sTableName, String _sDefaultColumnType,
			String _sDefaultTableType, boolean _bEmptyColumnAsNull, int _nMaxSizeOfData)
			throws IOException, SQLException {

		if (_sDefaultTableType == null) {
			_sDefaultTableType = "MyISAM";
		}

		dropTableInDatabase(_oConnection, _sTableName);

		StringBuffer oCreateSqlStringbuffer = new StringBuffer("CREATE TABLE " + _sTableName.toUpperCase() + " (");

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append("\n\tROWGENID serial PRIMARY KEY, ");
		} else {
			oCreateSqlStringbuffer.append("\n\tROWGENID bigint(20) NOT NULL auto_increment PRIMARY KEY, ");
		}

		int nColumnCount = 1;

		int nMinimumLineLength = 0;
		LinkedHashMap<String, Integer> oColumnsOrderedmap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, String> oLinkedHashMapOne_NameType = _oTripleHashMap.getHashMapOne();
		LinkedHashMap<String, String> oLinkedHashMapOne_NameSize = _oTripleHashMap.getHashMapTwo();

		for (Iterator<String> iterator = oLinkedHashMapOne_NameType.keySet().iterator(); iterator.hasNext();) {
			String sColumnName = (String) iterator.next();
			String sColumnType = oLinkedHashMapOne_NameType.get(sColumnName).toUpperCase();
			String sColumnWidth = oLinkedHashMapOne_NameSize.get(sColumnName);
			String[] sCommaColumnWidths = sColumnWidth.split("\\,");

			int nColumnWidth = 0;

			for (int i = 0; i < sCommaColumnWidths.length; i++) {
				int nFieldLength = Integer.parseInt(sCommaColumnWidths[i]);
				if (nFieldLength == 0) {
					nFieldLength = 1;
				}
				nColumnWidth = nColumnWidth + nFieldLength;
			}

			String sColumnTypeFinal = "";
			if (sColumnType.equals("A") || sColumnType.equals("D2") || sColumnType.equals("D4")) {
				sColumnTypeFinal = "VARCHAR(" + nColumnWidth + ")";
			} else {
				if (sColumnWidth.indexOf(",") != -1) {

					if (sCommaColumnWidths[1].equals("0")) {
						sColumnTypeFinal = "BIGINT(" + sCommaColumnWidths[0] + ")";
					} else {
						sColumnTypeFinal = "DECIMAL(" + sColumnWidth + ")";
					}

				} else {
					sColumnTypeFinal = "BIGINT(" + nColumnWidth + ")";
				}
			}

			oCreateSqlStringbuffer.append("\n\t" + sColumnName.toUpperCase() + "	" + sColumnTypeFinal + "");

			if (sColumnTypeFinal.toUpperCase().indexOf("CHAR") != -1) {
				oCreateSqlStringbuffer.append(" DEFAULT '' ");
			}
			oCreateSqlStringbuffer.append(", ");

			nMinimumLineLength = nMinimumLineLength + nColumnWidth;
			oColumnsOrderedmap.put(sColumnName, Integer.valueOf(nColumnWidth));
			nColumnCount = nColumnCount + 1;

		}

		oCreateSqlStringbuffer = new StringBuffer(
				oCreateSqlStringbuffer.substring(0, oCreateSqlStringbuffer.length() - 2));

		if (_sDefaultTableType.equals("H2")) {
			oCreateSqlStringbuffer.append(")");
		} else {
			oCreateSqlStringbuffer.append(")\nENGINE=" + _sDefaultTableType);
		}

		executeSQL(_oConnection, oCreateSqlStringbuffer.toString());

		LineNumberReader oLineNumberReader = new LineNumberReader(new FileReader(_sFilePath));

		int nRowNo = 1;
		int nInsertedRows = 0;
		int nExcludedRows = 0;
		String sReadLine = null;
		while ((sReadLine = oLineNumberReader.readLine()) != null && sReadLine != "") {

			//

			if (sReadLine.length() >= nMinimumLineLength) {
				insertRowIntoTempWorkTableForFixedDataFileNew(_oConnection, oColumnsOrderedmap, null, _sTableName,
						sReadLine, nRowNo, _bEmptyColumnAsNull, true, _nMaxSizeOfData);
			} else {
				LOGGER.info("ROWGENID (" + nInsertedRows
						+ ") excluded from import because it was smaller than the line length expected");
				nExcludedRows = nExcludedRows + 1;
			}

			nInsertedRows = nInsertedRows + 1;
			if (nInsertedRows % 500 == 0) {
				LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + nInsertedRows);
			}

		}

		for (Iterator<String> iterator = oLinkedHashMapOne_NameType.keySet().iterator(); iterator.hasNext();) {
			String sColumnName = (String) iterator.next();
			String sColumnType = oLinkedHashMapOne_NameType.get(sColumnName).toUpperCase();

			if (sColumnType.equals("D4")) {
				convertColumnToDateFrom0M0DYYYY(_oConnection, _sTableName.toUpperCase(), sColumnName);
			} else if (sColumnType.equals("D2")) {
				convertColumnToDateFrom0M0D0Y(_oConnection, _sTableName.toUpperCase(), sColumnName);
			}

		}

		oLineNumberReader.close();

		LOGGER.info("Lines inserted into '" + _sTableName.toUpperCase() + "' = " + (nInsertedRows - nExcludedRows));

	}

	/**
	 * This method inserts or updates a record based on parameters from the servlet
	 * request object.
	 * 
	 * @param _oConnection         Database connection object
	 * @param _oRequest            Serlvet request object
	 * @param _sDbTableAndPkColumn Delimited string with the table and primary key
	 *                             (e.g. TABLE,PKCOLUMN)
	 * @param _sDelimiter          Delimiter for the _sDbTableAndPkColumn
	 * @return Key generated from insert or null for update
	 * @throws SQLException
	 */
//	public static String insertOrUpdateRowFromRequest(Connection _oConnection, HttpServletRequest _oRequest,
//			String _sDbTableAndPkColumn, String _sDelimiter) throws SQLException {
//
//		String sTableName = _sDbTableAndPkColumn.substring(0, _sDbTableAndPkColumn.indexOf(_sDelimiter));
//		String sColumnPkName = _sDbTableAndPkColumn.substring(
//				_sDbTableAndPkColumn.indexOf(_sDelimiter) + _sDelimiter.length(), _sDbTableAndPkColumn.length());
//
//		LinkedHashMap<String, String> oValuesLinkedHashMap = new LinkedHashMap<String, String>();
//		oValuesLinkedHashMap.put(sColumnPkName,
//				XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(_sDbTableAndPkColumn)));
//
//		for (Enumeration<?> e = _oRequest.getParameterNames(); e.hasMoreElements();) {
//			String sRequestElement = e.nextElement().toString();
//			if (sRequestElement.startsWith(sTableName + _sDelimiter)) {
//				String sColumnName = sRequestElement.substring(
//						sRequestElement.indexOf(_sDelimiter) + _sDelimiter.length(), sRequestElement.length());
//				oValuesLinkedHashMap.put(sColumnName,
//						XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(sRequestElement)));
//			}
//		}
//
//		return insertOrUpdateRowFromLinkedHashMap(_oConnection, sTableName, oValuesLinkedHashMap);
//
//	}

	/**
	 * This method inserts or updates a record in the given table based on the
	 * values in the LinkedHashMap.
	 * 
	 * @param _oConnection          Database connection object
	 * @param _sTableToInsert       Table for action
	 * @param _oValuesLinkedHashMap Map of values to insert/update
	 * @return Key generated from insert or null for update
	 * @throws SQLException
	 */
	public static String insertOrUpdateRowFromLinkedHashMap(Connection _oConnection, String _sTableToInsert,
			LinkedHashMap<String, String> _oValuesLinkedHashMap) throws SQLException {

		Iterator<String> oIteratorFirst = _oValuesLinkedHashMap.keySet().iterator();
		String sKeyFirst = (String) oIteratorFirst.next();

		// if (_oValuesLinkedHashMap.getValue(0) == null)
		if (_oValuesLinkedHashMap.get(sKeyFirst) == null) {
			return insertRowFromLinkedHashMap(_oConnection, _sTableToInsert, _oValuesLinkedHashMap, true);
		} else {
			updateRowFromLinkedHashMap(_oConnection, _sTableToInsert, _oValuesLinkedHashMap);
			return null;
		}
	}

	/**
	 * This method inserts or updates a record in the given table based on the
	 * values in the LinkedHashMap.
	 * 
	 * @param _oConnection                      Database connection object
	 * @param _sTableToInsert                   Table for action
	 * @param _oValuesLinkedHashMap             Map of values to insert update
	 * @param _bDoesRecordWithSimilarValueExist Flag if records with similar values
	 *                                          exist
	 * @return Key generated from insert or null for update
	 * @throws SQLException
	 */
	public static String insertOrUpdateRowFromLinkedHashMap(Connection _oConnection, String _sTableToInsert,
			LinkedHashMap<String, String> _oValuesLinkedHashMap, boolean _bDoesRecordWithSimilarValueExist)
			throws SQLException {

		Iterator<?> oIteratorFirst = _oValuesLinkedHashMap.keySet().iterator();
		String sKeyFirst = (String) oIteratorFirst.next();

		if (_oValuesLinkedHashMap.get(sKeyFirst) == null) {
			if (_bDoesRecordWithSimilarValueExist) {
				return null;
			} else {
				return insertRowFromLinkedHashMap(_oConnection, _sTableToInsert, _oValuesLinkedHashMap, true);
			}
		} else {
			updateRowFromLinkedHashMap(_oConnection, _sTableToInsert, _oValuesLinkedHashMap);
			return null;
		}
	}

	/**
	 * This method inserts a record into the given table based on the key,value
	 * pairs in the LinkedHashMap.
	 * 
	 * @param _oConnection          Database connection object
	 * @param _sTableToInsert       Table to insert into
	 * @param _oValuesLinkedHashMap Column name (keys) and data (values) to insert
	 * @param _bReturnPk            Flag if primary key should be returned
	 * @return String value of primary key from row inserted
	 * @throws SQLException
	 */
	public static String insertRowFromLinkedHashMap(Connection _oConnection, String _sTableToInsert,
			LinkedHashMap<String, String> _oValuesLinkedHashMap, boolean _bReturnPk) throws SQLException {

		StringBuffer oInsertSqlStringbuffer = new StringBuffer("INSERT INTO " + _sTableToInsert + " (");
		StringBuffer oInsertSqlColumnNamesStringBuffer = new StringBuffer();
		StringBuffer oInsertSqlColumnValuesStringBuffer = new StringBuffer();

		for (Iterator<?> i = _oValuesLinkedHashMap.keySet().iterator(); i.hasNext();) {
			String sKey = (String) i.next();
			String sValue = (String) _oValuesLinkedHashMap.get(sKey);

			oInsertSqlColumnNamesStringBuffer.append(sKey + ", ");

			if (sValue != null && !sValue.equals("") && !sValue.equalsIgnoreCase("null")) {
				if (sValue.equalsIgnoreCase("now()")) {
					oInsertSqlColumnValuesStringBuffer
							.append("" + XSaLTStringUtils.regExReplaceStringForInsert(sValue) + ", ");
				} else {
					if (sKey.toUpperCase().contains("UDATEZ")) {
						if (sValue.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$")) {
							sValue = XSaLTStringUtils.formatDateForMySQL(sValue, "MM/dd/yyyy");
						}
					}
					oInsertSqlColumnValuesStringBuffer
							.append("'" + XSaLTStringUtils.regExReplaceStringForInsert(sValue) + "', ");
				}
			} else {
				oInsertSqlColumnValuesStringBuffer.append("null, ");
			}

		}

		oInsertSqlColumnNamesStringBuffer = new StringBuffer(
				oInsertSqlColumnNamesStringBuffer.substring(0, oInsertSqlColumnNamesStringBuffer.length() - 2));
		oInsertSqlColumnValuesStringBuffer = new StringBuffer(
				oInsertSqlColumnValuesStringBuffer.substring(0, oInsertSqlColumnValuesStringBuffer.length() - 2));

		oInsertSqlStringbuffer.append(oInsertSqlColumnNamesStringBuffer.toString() + ") VALUES ("
				+ oInsertSqlColumnValuesStringBuffer.toString() + ")");

		String sReturnKeyAsString = "";

		if (_bReturnPk) {
			return executeSQLGetKey(_oConnection, oInsertSqlStringbuffer.toString());
		} else {

			executeSQL(_oConnection, oInsertSqlStringbuffer.toString());
		}

		return sReturnKeyAsString;

	}

	/**
	 * This method updates a record in the given table based on the key,value pairs
	 * in the LinkedHashMap.
	 * 
	 * @param _oConnection          Database connection object
	 * @param _sTableToInsert       Table for action
	 * @param _oValuesLinkedHashMap Column name (keys) and data (values) to update
	 * @throws SQLException
	 */
	public static void updateRowFromLinkedHashMap(Connection _oConnection, String _sTableToInsert,
			LinkedHashMap<String, String> _oValuesLinkedHashMap) throws SQLException {

		StringBuffer oInsertSqlStringbuffer = new StringBuffer("UPDATE " + _sTableToInsert + " SET ");

		int nCountLinkedHashMap = 0;

		for (Iterator<?> i = _oValuesLinkedHashMap.keySet().iterator(); i.hasNext();) {

			String sKey = (String) i.next();
			String sValue = (String) _oValuesLinkedHashMap.get(sKey);

			if (nCountLinkedHashMap != 0) {

				oInsertSqlStringbuffer.append(sKey + " = ");

				if (sValue != null && !sValue.equals("") && !sValue.equalsIgnoreCase("null")) {
					if (sValue.equalsIgnoreCase("now()")) {
						oInsertSqlStringbuffer.append("" + XSaLTStringUtils.regExReplaceStringForInsert(sValue) + ", ");
					} else {
						oInsertSqlStringbuffer
								.append("'" + XSaLTStringUtils.regExReplaceStringForInsert(sValue) + "', ");
					}
				} else {
					oInsertSqlStringbuffer.append("null, ");
				}
			}

			nCountLinkedHashMap = nCountLinkedHashMap + 1;

		}

		oInsertSqlStringbuffer = new StringBuffer(
				oInsertSqlStringbuffer.substring(0, oInsertSqlStringbuffer.length() - 2));

		Iterator<?> oIteratorFirst = _oValuesLinkedHashMap.keySet().iterator();
		String sKeyFirst = (String) oIteratorFirst.next();
		String sValue = (String) _oValuesLinkedHashMap.get(sKeyFirst);
		oInsertSqlStringbuffer
				.append(" WHERE " + sKeyFirst + " = '" + XSaLTStringUtils.regExReplaceStringForInsert(sValue) + "'");
		executeSQL(_oConnection, oInsertSqlStringbuffer.toString());

	}

	/**
	 * This method parses the _sReadLine string as a fixed-length set of data and
	 * inserts the parsed data into the given table.
	 * 
	 * @param _oConnection         Database connection object
	 * @param _oColumnsOrderedmap  Map of columns and their order
	 * @param _oDecimalColumns     List of columns that are of decimal type
	 * @param _sTableName          Table for action
	 * @param _sReadLine           Line to parse to insert
	 * @param _nRowInsertNo        Row insert number (not used)
	 * @param _bEmptyColumnAsNull  Flag if empty data columns should be inserted as
	 *                             NULL
	 * @param _bConvertToUppercase Flag if data should be all upper case
	 * @param _nMaxSizeOfData      Max length of strings to insert
	 * @throws SQLException
	 */
	private static void insertRowIntoTempWorkTableForFixedDataFileNew(Connection _oConnection,
			LinkedHashMap<String, Integer> _oColumnsOrderedmap, ArrayList<String> _oDecimalColumns, String _sTableName,
			String _sReadLine, int _nRowInsertNo, boolean _bEmptyColumnAsNull, boolean _bConvertToUppercase,
			int _nMaxSizeOfData) throws SQLException {

		String sInsertStart = "INSERT INTO " + _sTableName.toUpperCase() + " ("
				+ XSaLTObjectUtils.getStringArrayWithDelimiter_String(
						_oColumnsOrderedmap.keySet().toArray(new String[_oColumnsOrderedmap.size()]), ", ")
				+ ") VALUES (";

		StringBuffer oInsertSqlStringbuffer = new StringBuffer(sInsertStart);

		int nRunningColumnCount = 1;

		int nCharsStart = 0;

		String sRawExtract;
		String sValueToInsert;

		for (Iterator<?> i = _oColumnsOrderedmap.keySet().iterator(); i.hasNext();) {
			String sKey = (String) i.next();

			Integer oValue = null;

			oValue = (Integer) _oColumnsOrderedmap.get(sKey);

			sValueToInsert = "";

			try {
				sRawExtract = _sReadLine.substring(nCharsStart, nCharsStart + oValue.intValue()).trim();

				if (_bConvertToUppercase) {
					sRawExtract = sRawExtract.toUpperCase();
				}

				sValueToInsert = XSaLTStringUtils.regExReplaceStringForInsert(sRawExtract);
			} catch (StringIndexOutOfBoundsException oob) {

				if (XB_SHOW_STRING_INDEX_OUT_OF_BOUNDS_EXCEPTION) {
					LOGGER.warn(oob.toString());
				}
				if (nCharsStart < _sReadLine.length()) {
					sRawExtract = _sReadLine.substring(nCharsStart).trim();

					if (_bConvertToUppercase) {
						sRawExtract = sRawExtract.toUpperCase();
					}

					sValueToInsert = XSaLTStringUtils.regExReplaceStringForInsert(sRawExtract);
				}
			} catch (Exception e) {
				LOGGER.error(e.toString(), e);
				LOGGER.info(_sReadLine);

			}

			if (_nMaxSizeOfData != 0) {
				int nMaxSize = _nMaxSizeOfData;
				if (nMaxSize > sValueToInsert.length()) {
					nMaxSize = sValueToInsert.length();
				}
				sValueToInsert = sValueToInsert.substring(0, nMaxSize);
			}

			if (_oDecimalColumns != null) {
				if (_oDecimalColumns.contains(sKey) == true) {
					BigDecimal oTestDecimal = new BigDecimal(sValueToInsert);
					sValueToInsert = oTestDecimal.toString();
				}
			}

			if (sValueToInsert.length() > 0) {
				oInsertSqlStringbuffer.append("'" + sValueToInsert + "', ");
			} else {
				if (_bEmptyColumnAsNull) {
					oInsertSqlStringbuffer.append("\n\tnull, ");
				} else {
					oInsertSqlStringbuffer.append("\n\t'', ");
				}
			}

			nCharsStart = nCharsStart + oValue.intValue();
			nRunningColumnCount = nRunningColumnCount + 1;
		}

		for (int i = nRunningColumnCount; i < _oColumnsOrderedmap.size(); i++) {
			if (_bEmptyColumnAsNull) {
				oInsertSqlStringbuffer.append("\n\tnull, ");
			} else {
				oInsertSqlStringbuffer.append("\n\t'', ");
			}
		}
		oInsertSqlStringbuffer = new StringBuffer(
				oInsertSqlStringbuffer.substring(0, oInsertSqlStringbuffer.length() - 2));
		oInsertSqlStringbuffer.append(")");

		executeSQL(_oConnection, oInsertSqlStringbuffer.toString());

	}

	/**
	 * This method gets all items from a table and exports it into a tab- delimited
	 * file on the specified path.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _sTableName      Table to export
	 * @param _sExportFilePath File to export to
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void exportTableAsTabDelimitedDataFile(Connection _oConnection, String _sTableName,
			String _sExportFilePath) throws SQLException, IOException {

		String sSQL = "SELECT * FROM " + _sTableName;
		exportSQLAsTabDelimitedDataFile(_oConnection, sSQL, _sExportFilePath);

	}

	/**
	 * This method exports selected records to a tab-delimited file.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _sSQL            SQL query statement
	 * @param _sExportFilePath File path for writing to
	 * @return String buffer with expected file contents if file is unavailable
	 * @throws SQLException
	 * @throws IOException
	 */
	public static StringBuffer exportSQLAsTabDelimitedDataFile(Connection _oConnection, String _sSQL,
			String _sExportFilePath) throws SQLException, IOException {

		return exportSQLAsTabDelimitedDataFile(_oConnection, _sSQL, _sExportFilePath, true);

	}

	/**
	 * This method exports results from an SQL query to tab-delimited file. If the
	 * specified file is null, then the method returns the results of the query in a
	 * StringBuffer.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _sSQL            SQL query statement
	 * @param _sExportFilePath File path for writing to
	 * @param _bWriteHeader    Flag if headers should be included in file
	 * @return Data that could not be written to file (null == success)
	 * @throws SQLException
	 * @throws IOException
	 */
	public static StringBuffer exportSQLAsTabDelimitedDataFile(Connection _oConnection, String _sSQL,
			String _sExportFilePath, boolean _bWriteHeader) throws SQLException, IOException {

		StringBuffer oExportFileStringbuffer = new StringBuffer();

		ResultSet oRsMysql = querySQL(_oConnection, _sSQL);
		ResultSetMetaData oRsmd = oRsMysql.getMetaData();

		if (_bWriteHeader) {
			for (int i = 0; i < oRsmd.getColumnCount(); i++) {
				oExportFileStringbuffer.append(oRsmd.getColumnName(i + 1) + "\t");
			}
			oExportFileStringbuffer = new StringBuffer(
					oExportFileStringbuffer.substring(0, oExportFileStringbuffer.length() - 1) + "\n");
		}

		while (oRsMysql.next()) {
			StringBuffer oRowStringbuffer = new StringBuffer();
			for (int i = 0; i < oRsmd.getColumnCount(); i++) {
				String sValue = oRsMysql.getString(i + 1);
				if (sValue == null || sValue.equals("null")) {
					sValue = "";
				}
				oRowStringbuffer.append(sValue + "\t");
			}
			oRowStringbuffer = new StringBuffer(oRowStringbuffer.substring(0, oRowStringbuffer.length() - 1) + "\n");
			oExportFileStringbuffer.append(oRowStringbuffer);

		}

		oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().trim());

		if (_sExportFilePath != null) {
			oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().replaceAll("`", "'"));
			XSaLTFileSystemUtils.writeStringToFile(oExportFileStringbuffer.toString(), _sExportFilePath);
			return null;
		} else {
			return oExportFileStringbuffer;
		}

	}

	/**
	 * This method exports results from multiple SQL queries to a tab-delimited
	 * file. If the specified file is null, then the method returns the results of
	 * the queries in a StringBuffer.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _asSQL           SQL query statements to export
	 * @param _sExportFilePath File to export to
	 * @param _bWriteHeader    Flag if headers should be exported
	 * @return Data that could not be written to file (null == success)
	 * @throws SQLException
	 * @throws IOException
	 */
	public static StringBuffer exportMultipleSQLAsTabDelimitedDataFile(Connection _oConnection, String[] _asSQL,
			String _sExportFilePath, boolean _bWriteHeader) throws SQLException, IOException {

		boolean bFirstPass = true;

		StringBuffer oExportFileStringbuffer = new StringBuffer();

		for (int j = 0; j < _asSQL.length; j++) {

			if (_asSQL[j] != null) {

				ResultSet oRsMysql = querySQL(_oConnection, _asSQL[j]);
				ResultSetMetaData oRsmd = oRsMysql.getMetaData();

				if (_bWriteHeader && bFirstPass) {
					for (int i = 0; i < oRsmd.getColumnCount(); i++) {
						String sColumnName = oRsmd.getColumnName(i + 1);
						if (sColumnName.equalsIgnoreCase("ROWGENID")) {
							sColumnName = "ORIGINAL_ROWGENID";
						}
						oExportFileStringbuffer.append(sColumnName + "\t");
					}
					oExportFileStringbuffer = new StringBuffer(
							oExportFileStringbuffer.substring(0, oExportFileStringbuffer.length() - 1) + "\n");
					bFirstPass = false;
				}

				while (oRsMysql.next()) {
					StringBuffer oRowStringbuffer = new StringBuffer();
					for (int i = 0; i < oRsmd.getColumnCount(); i++) {
						String sValue = oRsMysql.getString(i + 1);
						if (sValue == null || sValue.equals("null")) {
							sValue = "";
						}
						oRowStringbuffer.append(sValue + "\t");
					}
					oRowStringbuffer = new StringBuffer(
							oRowStringbuffer.substring(0, oRowStringbuffer.length() - 1) + "\n");
					oExportFileStringbuffer.append(oRowStringbuffer);

				}

				oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().trim() + "\n");

			}

		}

		if (_sExportFilePath != null) {
			oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().replaceAll("`", "'"));
			XSaLTFileSystemUtils.writeStringToFile(oExportFileStringbuffer.toString(), _sExportFilePath);
			return null;
		} else {
			return oExportFileStringbuffer;
		}

	}

	/**
	 * This method exports the contents of a table to a comma-delimited file.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _sTableName      Table to export
	 * @param _sExportFilePath File to export to
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void exportTableAsCommaDelimitedDataFile(Connection _oConnection, String _sTableName,
			String _sExportFilePath, boolean _bWriteHeader) throws SQLException, IOException {

		String sSQL = "SELECT * FROM " + _sTableName;
		exportSQLAsCommaDelimitedDataFile(_oConnection, sSQL, _sExportFilePath, _bWriteHeader);

	}

	/**
	 * This method exports results from an SQL query as a comma-delimited CSV. If
	 * the specified file is null, then the method returns the results of the query
	 * in a StringBuffer.
	 * 
	 * @param _oConnection     Database connection object
	 * @param _sSQL            SQL query statements to export
	 * @param _sExportFilePath File to export to
	 * @param _bWriteHeader    Flag if headers should be exported
	 * @return Data that could not be written to file (null == success)
	 * @throws SQLException
	 * @throws IOException
	 */
	public static StringBuffer exportSQLAsCommaDelimitedDataFile(Connection _oConnection, String _sSQL,
			String _sExportFilePath, boolean _bWriteHeader) throws SQLException, IOException {

		StringBuffer oExportFileStringbuffer = new StringBuffer();

		ResultSet oRsMysql = querySQL(_oConnection, _sSQL);

		ResultSetMetaData oRsmd = oRsMysql.getMetaData();

		if (_bWriteHeader) {

			for (int i = 0; i < oRsmd.getColumnCount(); i++) {
				oExportFileStringbuffer.append("\"" + oRsmd.getColumnName(i + 1) + "\",");
			}

			oExportFileStringbuffer = new StringBuffer(
					oExportFileStringbuffer.substring(0, oExportFileStringbuffer.length() - 1) + "\n");

		}

		while (oRsMysql.next()) {
			StringBuffer oRowStringbuffer = new StringBuffer();
			for (int i = 0; i < oRsmd.getColumnCount(); i++) {
				String sValue = oRsMysql.getString(i + 1);
				if (sValue == null || sValue.equals("null")) {
					sValue = "";
				}
				oRowStringbuffer.append("\"" + sValue + "\",");
			}
			oRowStringbuffer = new StringBuffer(oRowStringbuffer.substring(0, oRowStringbuffer.length() - 1) + "\n");
			oExportFileStringbuffer.append(oRowStringbuffer);

		}

		oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().trim());

		if (_sExportFilePath != null) {
			oExportFileStringbuffer = new StringBuffer(oExportFileStringbuffer.toString().replaceAll("`", "'"));
			XSaLTFileSystemUtils.writeStringToFile(oExportFileStringbuffer.toString(), _sExportFilePath);
			return null;
		} else {
			return oExportFileStringbuffer;
		}

	}

	/**
	 * This method attaches a connection to a given schema.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sSchemaName Database schema name
	 * @throws SQLException
	 */
	public static void attachToSchema(Connection _oConnection, String _sSchemaName) throws SQLException {
		// try to connect to the schema. if it does not exist, create it
		try {
			executeSQL(_oConnection, "USE " + _sSchemaName);
		} catch (SQLException sql) {
			if (sql.getErrorCode() == 1049) { // schema does not exist
				executeSQL(_oConnection, "CREATE SCHEMA " + _sSchemaName);
				executeSQL(_oConnection, "USE " + _sSchemaName);
			} else {
				throw sql;
			}
		}
	}

	/*
	 * -----------------------------------------------------------------------------
	 * This section contains stuff for creating and breaking down data objects
	 * -----------------------------------------------------------------------------
	 */

	/***
	 * This method creates a MySQL connection object.
	 * 
	 * @param _sHostName   Host where database is stored
	 * @param _sSchemaName Database schema to attach to
	 * @param _sUsername   Database user name
	 * @param _sPassword   Password for user name
	 * @return Connection to MySQL database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getMySQLConnection(String _sHostName, String _sSchemaName, String _sUsername,
			String _sPassword)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return getMySQLConnection(_sHostName, "3306", _sSchemaName, _sUsername, _sPassword);

	}

	/***
	 * This method creates a LOCAL H2 connection object.
	 * 
	 * @param _sSchemaName Database schema to attach to
	 * @param _sUsername   Database user name
	 * @param _sPassword   Password for user name
	 * @return Connection to LOCAL H2 database
	 * @throws SQLException
	 */
	public static Connection getLocalH2Connection(String _sDbPath, String _sUsername, String _sPassword)
			throws SQLException {
		return DriverManager.getConnection("jdbc:h2:" + _sDbPath + ";DB_CLOSE_DELAY=-1", _sUsername, _sPassword);

	}

	/***
	 * This method creates a getPostgres connection object.
	 * 
	 * @param _sHostName   Host where database is stored
	 * @param _sSchemaName Database schema to attach to
	 * @param _sUsername   Database user name
	 * @param _sPassword   Password for user name
	 * @return Connection to MySQL database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getPostgresConnection(String _sHostName, String _sSchemaName, String _sUsername,
			String _sPassword)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return getPostgresConnection(_sHostName, "5432", _sSchemaName, _sUsername, _sPassword);

	}

	/**
	 * This method creates a MySQL connection object.
	 * 
	 * @param _sHostName   Host where database is stored
	 * @param _sPortNumber Port database is listening on
	 * @param _sSchemaName Database schema to attach to
	 * @param _sUsername   Database user name
	 * @param _sPassword   Password for user name
	 * @return Connection to MySQL database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getMySQLConnection(String _sHostName, String _sPortNumber, String _sSchemaName,
			String _sUsername, String _sPassword)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection o_connection_mysql = null;
		String sConnectString = "";
		// LOGGER.info("sConnectString: " +
		// sConnectString);

		if (_sSchemaName == null) {

			sConnectString = "jdbc:mysql://" + _sHostName + ":" + _sPortNumber + "/" + "?user=" + _sUsername
					+ "&password=" + _sPassword;
			// LOGGER.info("sConnectString: " +
			// sConnectString);
		} else {
			sConnectString = "jdbc:mysql://" + _sHostName + ":" + _sPortNumber + "/" + _sSchemaName + "?user="
					+ _sUsername + "&password=" + _sPassword;
			// LOGGER.info("sConnectString: " +
			// sConnectString);
		}

		try {
			o_connection_mysql = DriverManager.getConnection(sConnectString);
			return o_connection_mysql;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o_connection_mysql;

	}

	/**
	 * This method creates a getPostgres connection object.
	 * 
	 * @param _sHostName   Host where database is stored
	 * @param _sPortNumber Port database is listening on
	 * @param _sSchemaName Database schema to attach to
	 * @param _sUsername   Database user name
	 * @param _sPassword   Password for user name
	 * @return Connection to MySQL database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getPostgresConnection(String _sHostName, String _sPortNumber, String _sSchemaName,
			String _sUsername, String _sPassword) {

		Connection o_connection_mysql = null;
		String sConnectString = "jdbc:postgresql://" + _sHostName + ":" + _sPortNumber + "/" + _sSchemaName;
		// LOGGER.info("sConnectString: " +
		// sConnectString);

		try {
			o_connection_mysql = DriverManager.getConnection(sConnectString, _sUsername, _sPassword);
			return o_connection_mysql;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o_connection_mysql;

	}

	/**
	 * This method gets an encrypted connection to a MySQL database.
	 * 
	 * @param _sHostName           Host where database is stored
	 * @param _sSchemaName         Database schema to attach to
	 * @param _sUsername           Database user name
	 * @param _sPassword           Password for user name
	 * @param _sKeystorePath       Path to find keystore
	 * @param _sKeystorePassword   Password for keystore
	 * @param _sTruststorePath     Path to find trusted access store
	 * @param _sTruststorePassword Password for trusted access
	 * @return Secured MySQL database connection
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getSecuredMySQLConnection(String _sHostName, String _sSchemaName, String _sUsername,
			String _sPassword, String _sKeystorePath, String _sKeystorePassword, String _sTruststorePath,
			String _sTruststorePassword)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		System.setProperty("javax.net.ssl.keyStore", _sKeystorePath);
		System.setProperty("javax.net.ssl.keyStorePassword", _sKeystorePassword);
		System.setProperty("javax.net.ssl.trustStore", _sTruststorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", _sTruststorePassword);

		Connection o_connection_mysql = null;

		if (_sSchemaName == null) {
			o_connection_mysql = DriverManager.getConnection("jdbc:mysql://" + _sHostName + "/" + "?user=" + _sUsername
					+ "&password=" + _sPassword + "&useSSL=true");
		} else {
			o_connection_mysql = DriverManager.getConnection("jdbc:mysql://" + _sHostName + "/" + _sSchemaName
					+ "?user=" + _sUsername + "&password=" + _sPassword + "&useSSL=true");
		}

		return o_connection_mysql;

	}

	/**
	 * This method gets a connection to an Oracle database.
	 * 
	 * @param msAccessDBName Path for database
	 * @return Oracle database connection object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getAccessConnection(String accessDbPath) throws ClassNotFoundException, SQLException {

		String dbURL = "jdbc:ucanaccess://" + accessDbPath;
		Connection o_connection_access = DriverManager.getConnection(dbURL);
		return o_connection_access;

	}

	/**
	 * Breaks down a ResultSet, Statement and connection (closes/nullifies)
	 * 
	 * @param _oRsMysql          The ResultSet to break down
	 * @param _oStmt             The Statement to break down
	 * @param _oPooledConnection The connection to break down
	 * @throws SQLException
	 */
	public static void breakDownRSPObjects(ResultSet _oRsMysql, Statement _oStmt, Connection _oPooledConnection)
			throws SQLException {
		breakDownResultSet(_oRsMysql);
		breakDownStatement(_oStmt);
		breakDownPooledConnection(_oPooledConnection);
	}

	/**
	 * Breaks down a ResultSet and Statement (closes/nullifies)
	 * 
	 * @param _oRsMysql The ResultSet to break down
	 * @param _oStmt    The Statement to break down
	 * @throws SQLException
	 */
	public static void breakDownRSObjects(ResultSet _oRsMysql, Statement _oStmt) throws SQLException {
		breakDownResultSet(_oRsMysql);
		breakDownStatement(_oStmt);
	}

	/**
	 * Breaks down a connection (closes/nullifies)
	 * 
	 * @param _oConnection The connection to break down
	 * @throws SQLException
	 */
	public static void breakDownPooledConnection(Connection _oConnection) {
		if (_oConnection != null) {
			try {
				_oConnection.close();
				_oConnection = null;
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Breaks down a Statement (closes/nullifies)
	 * 
	 * @param _oStmt The Statement to break down
	 * @throws SQLException
	 */
	public static void breakDownStatement(Statement _oStmt) throws SQLException {
		if (_oStmt != null) {
			_oStmt.close();
			_oStmt = null;
		}
	}

	/**
	 * Breaks down a ResultSet (closes/nullifies)
	 * 
	 * @param _oRsMysql The ResultSet to break down
	 * @throws SQLException
	 */
	public static void breakDownResultSet(ResultSet _oRsMysql) throws SQLException {
		if (_oRsMysql != null) {
			_oRsMysql.close();
			_oRsMysql = null;
		}
	}

	/**
	 * This method sets all the empty fields to null in a given database table
	 * 
	 * @param _oConn      The connection to the database
	 * @param _sTableName The table to update
	 * @throws SQLException
	 */
	public static void setEmptyFieldsInTableNull(Connection _oConn, String _sTableName) throws SQLException {
		ResultSet oRs = querySQL(_oConn, "SELECT * FROM " + _sTableName);
		ResultSetMetaData oRsMd = oRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			executeSQL(_oConn,
					"UPDATE " + _sTableName + " SET " + sColumnName + " = null WHERE " + sColumnName + " = ''");

		}
	}

	/**
	 * This method sets all the null fields to empty in a given database table
	 * 
	 * @param _oConn      The connection to the database
	 * @param _sTableName The table to update
	 * @throws SQLException
	 */
	public static void setNullFieldsInTableEmpty(Connection _oConn, String _sTableName) throws SQLException {
		ResultSet oRs = querySQL(_oConn, "SELECT * FROM " + _sTableName);
		ResultSetMetaData oRsMd = oRs.getMetaData();
		for (int i = 0; i < oRsMd.getColumnCount(); i++) {
			String sColumnName = oRsMd.getColumnName(i + 1).toUpperCase();
			executeSQL(_oConn,
					"UPDATE " + _sTableName + " SET " + sColumnName + " = '' WHERE " + sColumnName + " is null");

		}
	}

	/**
	 * This method will turn a SQL string into an ArrayList<HashMap<String, String>>
	 * 
	 * @param _oConnection      The connection to the database
	 * @param _sSqlText    The SQL query to execute
	 * @return a ArrayList<HashMap<String, String>> of the data
	 * @throws SQLException
	 */
	public static ArrayList<HashMap<String, String>> makeSQLAsArrayListHashMap(Connection _oConnection,
			String _sSqlText) throws SQLException, IOException {

		ArrayList<String> columnList = new ArrayList<String>();
		ArrayList<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();

		ResultSet resultset = _oConnection.createStatement().executeQuery(_sSqlText);
		ResultSetMetaData oRsmd = resultset.getMetaData();

		for (int i = 0; i < oRsmd.getColumnCount(); i++) {
			columnList.add(oRsmd.getColumnName(i + 1));
		}

		while (resultset.next()) {

			HashMap<String, String> rowRecord = new HashMap<String, String>();

			for (int i = 0; i < oRsmd.getColumnCount(); i++) {
				String sValue = resultset.getString(i + 1);
				if (sValue == null || sValue.equals("null")) {
					sValue = "";
				}
				rowRecord.put(columnList.get(i).toUpperCase(), sValue);
			}

			returnList.add(rowRecord);

		}

		return returnList;

	}

	// ------------------------------

	/**
	 * Executes SQL in a database schema
	 * 
	 * @param _oConnection The connection
	 * @param _sSqlText    The SQL query to execute
	 * @throws SQLException
	 */
	public static int executeSQL(Connection _oConnection, String _sSqlText) throws SQLException {
		if (XB_SYSOUT_DB_CALLS) {
			LOGGER.info("\t\t" + _sSqlText);

		}

		return _oConnection.createStatement().executeUpdate(_sSqlText);
	}

	/**
	 * 
	 * Executes SQL in a database schema and gets the generated ID
	 * 
	 * @param _oConnection The connection
	 * @param _sSqlText    The SQL query to execute
	 * @return The *first* generated key
	 * @throws SQLException
	 */
	public static String executeSQLGetKey(Connection _oConnection, String _sSqlText) throws SQLException {
		String sReturnKeyAsString = "";
		Statement oStmt = _oConnection.createStatement();

		oStmt.executeUpdate(_sSqlText, Statement.RETURN_GENERATED_KEYS);
		ResultSet oGenKeysRs = oStmt.getGeneratedKeys();
		if (oGenKeysRs.next()) {
			sReturnKeyAsString = oGenKeysRs.getString(1);

			if (XB_SYSOUT_DB_CALLS) {
				LOGGER.info(sReturnKeyAsString + "\t\t" + _sSqlText);
			}

		}
		return sReturnKeyAsString;

	}

	/**
	 * 
	 * Wrapper for prepared statement and generated ID
	 * 
	 * @param _oConnection The connection
	 * @param _sStatement  The SQL to be turned into a prepared statement
	 * @return The wrapped prepared statement
	 * @throws SQLException
	 */
	public static PreparedStatement createPreparedStatementForReturnKeys(Connection _oConnection, String _sStatement)
			throws SQLException {
		return _oConnection.prepareStatement(_sStatement, Statement.RETURN_GENERATED_KEYS);
	}

	/**
	 * 
	 * Executes prepared statement in a database schema and gets the generated ID
	 * 
	 * @param _oConnection        The connection
	 * @param _oPreparedStatement The prepared statement to execute
	 * @return The *first* generated key
	 * @throws SQLException
	 */
	public static String executePreparedStatementGetKey(Connection _oConnection, PreparedStatement _oPreparedStatement)
			throws SQLException {
		String sReturnKeyAsString = "";
		_oPreparedStatement.executeUpdate();
		ResultSet oGenKeysRs = _oPreparedStatement.getGeneratedKeys();
		if (oGenKeysRs.next()) {
			sReturnKeyAsString = oGenKeysRs.getString(1);

			if (XB_SYSOUT_DB_CALLS) {
				LOGGER.info(sReturnKeyAsString + "\t\t" + _oPreparedStatement.toString());
			}

		}
		return sReturnKeyAsString;

	}

	/**
	 * Gets results from a SQL in a database schema
	 * 
	 * @param _oConnection The connection
	 * @param _sSqlText    The SQL query to execute
	 * @throws SQLException
	 */
	public static ResultSet querySQL(Connection _oConnection, String _sSqlText) throws SQLException {
		return querySQL(_oConnection, _sSqlText, 0);
	}

	/**
	 * This method executes the provided query and returns the matching records- if
	 * the _nMaxRows parameter is > 0, that is the maximum number of rows that will
	 * be returned.
	 * 
	 * @param _oConnection Database connection object
	 * @param _sSqlText    SQL query text
	 * @param _nMaxRows    Max rows to return
	 * @return ResultSet with matching records
	 * @throws SQLException
	 */
	public static ResultSet querySQL(Connection _oConnection, String _sSqlText, int _nMaxRows) throws SQLException {
		Statement oStmt = _oConnection.createStatement();

		if (_nMaxRows > 0) {
			oStmt.setMaxRows(_nMaxRows);
		}
		if (XB_SYSOUT_DB_CALLS) {
			LOGGER.info("\t\t" + _sSqlText);
		}
		ResultSet oRsMysql = oStmt.executeQuery(_sSqlText);
		return oRsMysql;
	}

	/**
	 * Changes the name of a column
	 * 
	 * @param _oConnection
	 * @param _sTableName     Table with column
	 * @param _sOldColumnName The Name of the column to change
	 * @param _sNewColumnName The new column name
	 * @throws SQLException
	 */
	public static void changeColumnName(Connection _oConnection, String _sTableName, String _sOldColumnName,
			String _sNewColumnName) throws SQLException {
		String sType = "";
		String sColumn;
		boolean bFound = false;

		ResultSet rsSet = querySQL(_oConnection, "show columns from " + _sTableName);
		;
		while (rsSet.next()) {
			sColumn = rsSet.getString("Field");
			if (_sOldColumnName.equalsIgnoreCase(sColumn)) {
				bFound = true;
				sType = rsSet.getString("Type");
				break;
			}

		}

		if (bFound) {
			String sSQL = "ALTER TABLE " + _sTableName + " CHANGE " + _sOldColumnName + " " + _sNewColumnName + " "
					+ sType;
			executeSQL(_oConnection, sSQL);
		} else {
			LOGGER.fatal("Column not found and not changed: " + _sOldColumnName);
		}
	}

	/**
	 * Performs a Replace function on a column
	 * 
	 * @param _oConnection
	 * @param _sTableName  Table Name
	 * @param _sColumnName Column Name
	 * @param _sOldValue   Value to search for (can be null)
	 * @param _sNewValue   Value to replace with (can be null)
	 * @throws SQLException
	 */
	public static void replaceValuesInColumn(Connection _oConnection, String _sTableName, String _sColumnName,
			String _sOldValue, String _sNewValue) throws SQLException {
		if (_sNewValue == null) {
			_sNewValue = "";
		}

		if (_sOldValue == null) {
			_sOldValue = "";
		}

		executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + _sNewValue + "' WHERE "
				+ _sColumnName + " = '" + _sOldValue + "'");
	}

	/**
	 * Copy one column to another
	 * 
	 * @param _oConnection
	 * @param _sTableName
	 * @param _sSourceColumn
	 * @param _sDestinationColumn
	 * @param _sNewColumnType     If not null, will create a column with this type
	 * @param _sIfNotNullColumn   Only copy on lines where value on this line is not
	 *                            null (Can be null).
	 * @throws SQLException
	 */
	public static void copyColumn(Connection _oConnection, String _sTableName, String _sSourceColumn,
			String _sDestinationColumn, String _sNewColumnType, String _sIfNotNullColumn) throws SQLException {
		if (_sNewColumnType != null) {
			dropColumnInTable(_oConnection, _sTableName, _sDestinationColumn);
			addColumnInTable(_oConnection, _sTableName, _sDestinationColumn, _sNewColumnType);
		}

		String sQueryString = "UPDATE " + _sTableName + " SET " + _sDestinationColumn + " = " + _sSourceColumn;
		if (_sIfNotNullColumn != null) {
			sQueryString += " WHERE " + _sIfNotNullColumn + " IS NOT NULL";
		}
		executeSQL(_oConnection, sQueryString);
	}

	/**
	 * Generate A Delimited list from a SQL Query
	 * 
	 * @param _rsData     Dataset Containing the values
	 * @param _sColumn    Column of data
	 * @param _sDelimiter String in between the values
	 * @param _sStart     String at start
	 * @param _sEnd       String at end
	 * @return The string
	 * @throws SQLException
	 */
	public static String getDelimitedStringFromSQLQuery(ResultSet _rsData, String _sColumn, String _sDelimiter,
			String _sStart, String _sEnd) throws SQLException {
		StringBuilder sbList = new StringBuilder(_sStart);

		while (_rsData.next()) {
			sbList.append(_rsData.getString(_sColumn));
			sbList.append(_sDelimiter);
		}
		sbList.delete(sbList.length() - _sDelimiter.length(), sbList.length());
		sbList.append(_sEnd);

		return sbList.toString();
	}

	/**
	 * Generate a comma-delimited list of column names in a table in alphabetical order
	 * 
	 * @param _oConnection
	 * @param _sTableName
	 * @return The string
	 * @throws SQLException
	 */
	public static String getColumnNamesInAlphabeticalOrder(Connection _oConnection, String _sTableName)
			throws SQLException {

		SortedSet<String> oColumnNames = new TreeSet<>();
		ResultSet rs = _oConnection.createStatement().executeQuery("SELECT * FROM " + _sTableName + " LIMIT 1");
		ResultSetMetaData metaData = rs.getMetaData();
		int colCount = metaData.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			oColumnNames.add(metaData.getColumnName(i).toUpperCase());
		}
		return String.join(", ", oColumnNames);
	}

	/**
	 * Adds a limit and offset to a given SQL query
	 * 
	 * @param _sSql			The SQL
	 * @param _iMaxRows		The maximum number of rows	
	 * @param iQueryPasses	The number of passes (0 to whatever)
	 * @return The string SQL
	 * @throws SQLException
	 */
	public static String addPagingToQuery(String _sSql, int _iMaxRows, int iQueryPasses) {
		return _sSql + " LIMIT " + _iMaxRows + " OFFSET " + (iQueryPasses * _iMaxRows) + "";
	}

	/**
	 * Creates a blank database file with an identity column
	 * 
	 * @param _oConnection
	 * @param _sTableName
	 * @param _bDropFirst
	 * @return The string
	 * @throws SQLException
	 */
	public static void makeBlankDatabaseWithIdentityColumn(Connection _oConnection, String _sTableName,
			boolean _bDropFirst) throws SQLException {

		if (_bDropFirst) {
			executeSQL(_oConnection, "DROP TABLE IF EXISTS " + _sTableName);
		}

		DatabaseMetaData metadata = _oConnection.getMetaData();
		String databaseProductName = metadata.getDatabaseProductName();
		String tableSQL = "CREATE TABLE " + _sTableName + " ";
		if (databaseProductName.equalsIgnoreCase("PostgreSQL")) {
			tableSQL = tableSQL + "(id BIGSERIAL PRIMARY KEY)";
		} else if (databaseProductName.equalsIgnoreCase("MySQL")) {
			tableSQL = tableSQL + "(id INT AUTO_INCREMENT, PRIMARY KEY (id))";
		} else {
			tableSQL = tableSQL + "(id INT AUTO_INCREMENT PRIMARY KEY)";
		}
		executeSQL(_oConnection, tableSQL);

	}

	public static void provisionTableWithColumns(Connection _oConnection, String _sInputFileText, String _sTableName,
			XSaLTDataProcessInterface processor) throws SQLException, IOException {

		TreeMap<String, Long> sizeParams = new TreeMap<>();
		List<String> fileLines = null;
		fileLines = Files.readAllLines(Paths.get(_sInputFileText), StandardCharsets.UTF_8);

		for (String line : fileLines) {
			TreeMap<String, String> testParams = processor.performOperation(line);
			handleParamSizes(sizeParams, testParams);
		}

		makeBlankDatabaseWithIdentityColumn(_oConnection, _sTableName, true);

		for (Map.Entry<String, Long> entry : sizeParams.entrySet()) {
			String columnName = entry.getKey();
			long columnSize = (sizeParams.get(columnName) == null || sizeParams.get(columnName) == 0) ? 5
					: sizeParams.get(columnName);

			columnName = entry.getKey();

			String addColumnSql = "";
			try {
				addColumnSql = "ALTER TABLE " + _sTableName.toUpperCase() + " ADD COLUMN " + columnName + " VARCHAR("
						+ columnSize + ")";
				executeSQL(_oConnection, addColumnSql);
			} catch (SQLException e) {
				LOGGER.warn("Cannot alter table: {}, reason {}", addColumnSql, e.toString());
			}

		}

	}

	public static void writeTableWithColumns(Connection _oConnection, String _sInputFileText, String _sTableName,
			XSaLTDataProcessInterface processor) throws SQLException, IOException {

		int lineCount = 1;
		List<String> fileLines = null;
		fileLines = Files.readAllLines(Paths.get(_sInputFileText), StandardCharsets.UTF_8);

		for (String line : fileLines) {

			lineCount++;
			TreeMap<String, String> finalParams = processor.performOperation(line);

			try {
				makeAndInsertRows(_oConnection, _sTableName, finalParams);
				if (lineCount % 100 == 0) {
					LOGGER.info("row {} added!", lineCount);
				}
			} catch (SQLException ee) {
				ee.printStackTrace();
			}

		}

	}

	public static void makeAndInsertRows(Connection conn, String tableName, TreeMap<String, String> dbColumns)
			throws SQLException {

		ArrayList<String> columnNames = new ArrayList<>();
		ArrayList<String> columnValues = new ArrayList<>();

		for (Map.Entry<String, String> entry : dbColumns.entrySet()) {
			columnNames.add(entry.getKey());
			columnValues.add("'" + XSaLTStringUtils.getEmptyStringIfNull(entry.getValue()).replaceAll("\\'", "") + "'");
		}

		String insertSQL = "INSERT INTO " + tableName + " (" + String.join(", ", columnNames) + ") VALUES ("
				+ String.join(", ", columnValues) + ")";

		try {
			conn.createStatement().execute(insertSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void handleParamSizes(TreeMap<String, Long> _oSizeParams, TreeMap<String, String> _oTestParams) {
		for (Map.Entry<String, String> entry : _oTestParams.entrySet()) {
			String key = entry.getKey();
			if (entry.getValue() != null) {
				int value = entry.getValue().length();
				if (_oSizeParams.containsKey(key)) {
					Long testLongValue = _oSizeParams.get(key);
					if (value > testLongValue) {
						_oSizeParams.put(key, (long) value);
					}
				} else {
					_oSizeParams.put(key, (long) value);
				}
			} else {
				int value = 0;
				if (_oSizeParams.containsKey(key)) {
					Long testLongValue = _oSizeParams.get(key);
					if (value > testLongValue) {
						_oSizeParams.put(key, (long) value);
					}
				} else {
					_oSizeParams.put(key, (long) value);
				}
			}
		}
	}

}
