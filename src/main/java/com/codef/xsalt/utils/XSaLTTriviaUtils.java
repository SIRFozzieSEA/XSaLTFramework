package com.codef.xsalt.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.codef.xsalt.arch.XSaLTConstants;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTTriviaUtils {

	private static final Logger LOGGER = Logger.getLogger(XSaLTTriviaUtils.class.getName());

	/**
	 * This method returns the formed URL to manually check bad addresses on USPS'
	 * website
	 * 
	 * @param _sAddress1 Address 1 field
	 * @param _sAddress2 Address 2 field
	 * @param _sCity     City field
	 * @param _sState    State field
	 * @param _sZip      Zip field
	 * 
	 * @return The formed URL
	 */
	public static String getUSPSLookupURL(String _sAddress1, String _sAddress2, String _sCity, String _sState,
			String _sZip) {

		_sAddress1 = _sAddress1.replaceAll(" ", "+");
		_sAddress2 = _sAddress2.replaceAll(" ", "+");
		_sCity = _sCity.replaceAll(" ", "+");
		_sState = _sState.replaceAll(" ", "+");
		_sZip = _sZip.replaceAll(" ", "+");

		String sURL = "https://tools.usps.com/go/ZipLookupResultsAction!input.action?resultMode=0&companyName=&address1="
				+ _sAddress1 + "&address2=" + _sAddress2 + "&city=" + _sCity + "&state=" + _sState
				+ "&urbanCode=&postalCode=&zip=" + _sZip + "";

		return sURL;

	}

	/**
	 * This method saves a truncated lookup of the bad address
	 * 
	 * @param _sURL      The URL to test
	 * @param _sFileName The file name to save (without extension)
	 * @param _sSavePath The save path for the lookup
	 */
	public static void saveUSPSLookupPage(String _sURL, String _sFileName, String _sSavePath) throws IOException {

		URL o_item_url = new URL(_sURL);
		InputStream tempInputStream = o_item_url.openStream();
		StringBuffer oTempBuffer = new StringBuffer();

		int Content;
		while ((Content = tempInputStream.read()) != -1) {
			oTempBuffer.append((char) Content);
		}

		String sStartTag = "You entered:";
		String sEndTag = "<!-- Begin Footer -->";
		oTempBuffer = new StringBuffer(
				oTempBuffer.substring(oTempBuffer.indexOf(sStartTag) - 4, oTempBuffer.indexOf(sEndTag)));

		String sReturnReason = "[Unknown]";
		if (oTempBuffer.indexOf("is not recognized by the US Postal") != -1) {
			sReturnReason = "[MBR]";
		}

		if (oTempBuffer.indexOf("address wasn't found") != -1) {
			sReturnReason = "[ANK]";
		}

		XSaLTFileSystemUtils.writeStringBufferToFile(oTempBuffer,
				_sSavePath + "/" + sReturnReason + " " + _sFileName + ".html");

	}

	/**
	 * This method tests launching the AccuZip software.
	 */
	public static void testWindowsLauncher() {
		LOGGER.info("Starting process...");

		try {
			Process oProcess = Runtime.getRuntime().exec("D:/AccuZIP/AccuZIPLauncher.bat");
			oProcess.waitFor();
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}

		LOGGER.info("Process is completed...");
	}

	/**
	 * This method returns either the Sebis value for the key or the Sebis key for
	 * the value.
	 * 
	 * @param _sKey          Key or value to search for
	 * @param _bGetCharacter Flag if standard character or Sebis value is desired
	 * @return
	 */
	public static String getSebis3of9Value(String _sKey, boolean _bGetCharacter) {

		HashMap<String, String> oSebisMap = new HashMap<String, String>();

		oSebisMap.put("0", "0");
		oSebisMap.put("1", "1");
		oSebisMap.put("2", "2");
		oSebisMap.put("3", "3");
		oSebisMap.put("4", "4");
		oSebisMap.put("5", "5");
		oSebisMap.put("6", "6");
		oSebisMap.put("7", "7");
		oSebisMap.put("8", "8");
		oSebisMap.put("9", "9");
		oSebisMap.put("10", "A");
		oSebisMap.put("11", "B");
		oSebisMap.put("12", "C");
		oSebisMap.put("13", "D");
		oSebisMap.put("14", "E");
		oSebisMap.put("15", "F");
		oSebisMap.put("16", "G");
		oSebisMap.put("17", "H");
		oSebisMap.put("18", "I");
		oSebisMap.put("19", "J");
		oSebisMap.put("20", "K");
		oSebisMap.put("21", "L");
		oSebisMap.put("22", "M");
		oSebisMap.put("23", "N");
		oSebisMap.put("24", "O");
		oSebisMap.put("25", "P");
		oSebisMap.put("26", "Q");
		oSebisMap.put("27", "R");
		oSebisMap.put("28", "S");
		oSebisMap.put("29", "T");
		oSebisMap.put("30", "U");
		oSebisMap.put("31", "V");
		oSebisMap.put("32", "W");
		oSebisMap.put("33", "X");
		oSebisMap.put("34", "Y");
		oSebisMap.put("35", "Z");
		oSebisMap.put("36", "-");
		oSebisMap.put("37", ".");
		oSebisMap.put("38", " ");
		oSebisMap.put("39", "$");
		oSebisMap.put("40", "/");
		oSebisMap.put("41", "+");
		oSebisMap.put("42", "%");

		if (_bGetCharacter) {
			return oSebisMap.get(_sKey);
		} else {
			String sReturnKey = "";
			for (Iterator<String> j = oSebisMap.keySet().iterator(); j.hasNext();) {
				String sKey = j.next();
				String sValue = oSebisMap.get(sKey);

				if (sValue.equalsIgnoreCase(_sKey)) {
					sReturnKey = sKey;
				}
			}
			return sReturnKey;
		}

	}

	/**
	 * This method creates the IMB encoded value and returns the USPS bar code.
	 * 
	 * @param _sMailerId Mailing ID for the current community
	 * @param _sZip      Zip for mailing address
	 * @param _sPlusFour Zip Plus4 for mailing address
	 * @param _sDPBC     The DBPC (delivery point bar code)
	 * @return Bar code from IMB encoding from USPS
	 */
	public static String doIMBEncode(String _sMailerId, String _sZip, String _sPlusFour, String _sDPBC) {

		String sEncodeValue = "00700" + _sMailerId + "000000" + _sZip + _sPlusFour + _sDPBC;

		try {
//			String sTrack = sEncodeValue.substring(0, 20);
//			String sRoute = sEncodeValue.substring(20, 31);
//			USPS4CB oUSPSEncoder = gov.usps.USPS4CB.getInstance();
//			oUSPSEncoder.setTrack(sTrack);
//			oUSPSEncoder.setRoute(sRoute);
//			String sBarcode = oUSPSEncoder.getBarCode();
//			return sBarcode.substring(3, sBarcode.length());
			return sEncodeValue;

		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return "";
		}

	}

	/**
	 * This method updates records in the specified table, counting vehicles by
	 * keyified address.
	 * 
	 * @param _oConn             Database connection object
	 * @param _sTableName        Table for action
	 * @param _sRowgenColumnName Column name for generated primary key
	 * @param _asFieldNames      Array of field names to select
	 * @param _sNumberColumnName Column to put vehicle number into
	 * @throws SQLException
	 */
	public static void numberVehicles(Connection _oConn, String _sTableName, String _sRowgenColumnName,
			String[] _asFieldNames, String _sNumberColumnName) throws SQLException {
		String sLastAddressKey = "";

		String sSQL = "SELECT " + _sRowgenColumnName + ","
				+ XSaLTObjectUtils.getStringArrayWithDelimiter_String(_asFieldNames, ",") + " FROM " + _sTableName
				+ " ORDER BY " + XSaLTObjectUtils.getStringArrayWithDelimiter_String(_asFieldNames, ",");

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);

		int nCycleCount = 0;
		int nVehicleCount = 1;
		while (oRs.next()) {

			String sRowgenID = oRs.getString(_sRowgenColumnName);

			StringBuffer oKeyBuffer = new StringBuffer();
			for (int j = 0; j < _asFieldNames.length; j++) {
				oKeyBuffer.append(XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_asFieldNames[j]).toUpperCase()));
			}

			String sCurrentAddressKey = XSaLTStringUtils.regExRemoveSpaces(oKeyBuffer.toString());

			String sSQL2 = "";

			if (sCurrentAddressKey.equals(sLastAddressKey)) {
				nVehicleCount = nVehicleCount + 1;
				sSQL2 = "UPDATE " + _sTableName + " SET " + _sNumberColumnName + " = '" + nVehicleCount + "' WHERE "
						+ _sRowgenColumnName + " = '" + sRowgenID + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL2);

			} else {
				nVehicleCount = 1;
				sSQL2 = "UPDATE " + _sTableName + " SET " + _sNumberColumnName + " = '" + nVehicleCount + "' WHERE "
						+ _sRowgenColumnName + " = '" + sRowgenID + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL2);
			}

			sLastAddressKey = sCurrentAddressKey;
			nCycleCount = nCycleCount + 1;

		}

	}

	/**
	 * This method creates the NCOA file from a table that has had the CASS
	 * processes previously run on it.
	 * 
	 * @param _oConn            Database connection object
	 * @param _sNCOAFilePath    Path to write the NCOA file
	 * @param _sCASSedTable     Table to get data fromUSPS4CB
	 * @param _sAccountFieldSQL SQL for getting account information
	 * @throws Exception
	 */
	public static void generateNCOAFile(Connection _oConn, String _sNCOAFilePath, String _sCASSedTable,
			String _sAccountFieldSQL) throws Exception {
		// check to see that CASS is complete

		ResultSet rsCASSCount = XSaLTDataUtils.getFirstRecord(_oConn,
				"SELECT count(*) as cnt from " + _sCASSedTable + " where JQ_FULL_NAME is null");
		if (rsCASSCount.getInt("cnt") > 0) {
			LOGGER.fatal("Can not generate NCOA file, CASS process incomplete for table " + _sCASSedTable);
			return;
		} else {
			final long lMaxFileRecords = 10000000;
			long lRecordCount = 0;
			long lFileCount = 1;

			BufferedWriter bwWriter = new BufferedWriter(new FileWriter(_sNCOAFilePath, false));

			ResultSet rsCASS = XSaLTDataUtils.querySQL(_oConn,
					"SELECT JQ_FULL_NAME, JQ_ADDRESS_1, JQ_ADDRESS_2, JQ_ADDRESS_3, JQ_ADDRESS_4, JQ_CITY, JQ_STATE, JQ_ZIP5, JQ_PLUS4, "
							+ _sAccountFieldSQL + " AS JQ_ACCOUNT from " + _sCASSedTable
							+ " order by JQ_ZIP5, JQ_PLUS4, JQ_DPBC, JQ_SORT_ORDER");

			String sName = "", sCity = "", sState = "", sZip5 = "", sPlus4 = "", sAccount = "";
			StringBuilder sbName, sbAddress, sbCSZ, sbWriteLine;
			ArrayList<String> lsRawNamesAddresses = new ArrayList<String>();

			while (rsCASS.next()) {
				lRecordCount++;
				lsRawNamesAddresses = new ArrayList<String>();

				sName = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_FULL_NAME")).trim();

				if (sName.length() == 0) {
					continue;
				}

				lsRawNamesAddresses.add(XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ADDRESS_1")).trim());
				lsRawNamesAddresses.add(XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ADDRESS_2")).trim());
				lsRawNamesAddresses.add(XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ADDRESS_3")).trim());
				lsRawNamesAddresses.add(XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ADDRESS_4")).trim());

				sCity = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_CITY")).trim();
				sState = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_STATE")).trim();
				sZip5 = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ZIP5")).trim();
				sPlus4 = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_PLUS4")).trim();
				sAccount = XSaLTStringUtils.getEmptyStringIfNull(rsCASS.getString("JQ_ACCOUNT")).trim();

				sbName = new StringBuilder(sName + " ");
				sbAddress = new StringBuilder();

				boolean bPastAddress = false;
				for (String sStr : lsRawNamesAddresses) {
					if (bPastAddress) {
						sbAddress.append(sStr);
						sbAddress.append(' ');
					} else {
						if (XSaLTTriviaUtils.isAddressString(sStr)) { // at address
							sbAddress.append(sStr);
							bPastAddress = true;
							sbAddress.append(' ');
						} else {
							sbName.append(sStr);
							sbName.append(' ');
						}
					}
				}

				sbName.deleteCharAt(sbName.length() - 1);
				sbAddress.deleteCharAt(sbAddress.length() - 1);

				sbCSZ = new StringBuilder();

				sbCSZ.append(sCity);
				sbCSZ.append(", ");
				sbCSZ.append(sState);
				sbCSZ.append(", ");
				sbCSZ.append(sZip5);

				sbWriteLine = new StringBuilder();

				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sZip5, ' ', 5)); // five digit zip
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sbName.toString(), ' ', 40)); // Name
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sbAddress.toString(), ' ', 50)); // Address
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sbCSZ.toString(), ' ', 40)); // City, State,
																											// Zip (5)
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sAccount, ' ', 20)); // Customer/Account
																									// Number (optional)
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString("", ' ', 10)); // Parsed Primary Number
																							// (Street address #, po box
																							// #, etc.) (optional)
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString("", ' ', 8)); // Parsed Secondary Number (?)
																							// (optional)
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sZip5, ' ', 5)); // five digit zip
				sbWriteLine.append(XSaLTStringUtils.createFixedLengthString(sPlus4, ' ', 4)); // plus four code
				sbWriteLine.append(System.getProperty("line.separator"));

				bwWriter.write(sbWriteLine.toString());

				LOGGER.info(sbWriteLine.toString());

				// really, if we get to 10 million records (USPS max size)
				if (lRecordCount % lMaxFileRecords == 0) {
					bwWriter.flush();
					bwWriter.close();

					LOGGER.info("Hit record limit, creating new file");
					lFileCount++;
					bwWriter = new BufferedWriter(new FileWriter(_sNCOAFilePath + "_" + lFileCount, false));
				}

			}

			bwWriter.flush();
			bwWriter.close();

			LOGGER.info("Record Count: " + lRecordCount);
			LOGGER.info("File Count: " + lFileCount);
		}
	}

	/**
	 * This method adds the specified number of days to the given date.
	 * 
	 * @param _sDate      Beginning date string (this will always be in XX/XX/XXXX
	 *                    format)
	 * @param _nDaysToAdd Number of days to add
	 * @return String representation of modified date
	 */
	public static String addDays(String _sDate, int _nDaysToAdd) {
		/*
		 * Suggestion by Keith for mulitple format
		 * 
		 * SimpleDateFormat oSdf = new
		 * SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_WITH_SLASHES); Calendar
		 * oCalendar = Calendar.getInstance(); oCalendar.setTime(oSdf.parse(_sDate));
		 * oCalendar.add(Calendar.DATE, _nDaysToAdd);
		 * 
		 * return oSdf.format(oCalendar.getTime());
		 */

		String sYear = "";
		String sMonth = "";
		String sDay = "";
		sMonth = _sDate.substring(0, 2);
		sDay = _sDate.substring(3, 5);
		sYear = _sDate.substring(6, 10);

		SimpleDateFormat oSdf = new SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_WITH_SLASHES);
		GregorianCalendar oGregorianCalendar = new GregorianCalendar(Integer.valueOf(sYear).intValue(),
				Integer.valueOf(sMonth).intValue() - 1, Integer.valueOf(sDay).intValue());
		oGregorianCalendar.add(Calendar.DATE, _nDaysToAdd);

		return oSdf.format(oGregorianCalendar.getTime());

	}

	/**
	 * This method compares the two passed in dates and returns a String
	 * representation of the results.
	 * 
	 * @param _sDate    Date for left side of comparison (this will always be in
	 *                  XX/XX/XXXX format)
	 * @param _sDateTwo Date for right side of comparison (this will always be in
	 *                  XX/XX/XXXX format)
	 * @return String with results of comparison (EQUALS, BEFORE, or AFTER)
	 */
	public static String isDateOnOrAfter(String _sDate, String _sDateTwo) {

		String sYear = "";
		String sMonth = "";
		String sDay = "";
		sMonth = _sDate.substring(0, 2);
		sDay = _sDate.substring(3, 5);
		sYear = _sDate.substring(6, 10);

		String sYear2 = "";
		String sMonth2 = "";
		String sDay2 = "";
		sMonth2 = _sDateTwo.substring(0, 2);
		sDay2 = _sDateTwo.substring(3, 5);
		sYear2 = _sDateTwo.substring(6, 10);

		GregorianCalendar oGregorianCalendar = new GregorianCalendar(Integer.valueOf(sYear).intValue(),
				Integer.valueOf(sMonth).intValue() - 1, Integer.valueOf(sDay).intValue());
		GregorianCalendar oGregorianCalendarTwo = new GregorianCalendar(Integer.valueOf(sYear2).intValue(),
				Integer.valueOf(sMonth2).intValue() - 1, Integer.valueOf(sDay2).intValue());

		if (oGregorianCalendar.compareTo(oGregorianCalendarTwo) == 0) {
			return "EQUALS";
		} else if (oGregorianCalendar.compareTo(oGregorianCalendarTwo) > 0) {
			return "BEFORE";
		} else if (oGregorianCalendar.compareTo(oGregorianCalendarTwo) < 0) {
			return "AFTER";
		}

		return "EQUALS";

	}

	/**
	 * This method calculates the number of whole days between the start and end
	 * dates.
	 * 
	 * @param _sStartDate String representation of start date
	 * @param _sEndDate   String representation of end date
	 * @return Number of whole days between given dates
	 */
	public static int dateDifference(String _sStartDate, String _sEndDate) {

		try {
			String sYear = "";
			String sMonth = "";
			String sDay = "";
			sMonth = _sEndDate.substring(0, 2);
			sDay = _sEndDate.substring(3, 5);
			sYear = _sEndDate.substring(6, 10);

			String sYear2 = "";
			String sMonth2 = "";
			String sDay2 = "";
			sMonth2 = _sStartDate.substring(0, 2);
			sDay2 = _sStartDate.substring(3, 5);
			sYear2 = _sStartDate.substring(6, 10);

			GregorianCalendar oGregorianCalendar = new GregorianCalendar(Integer.valueOf(sYear).intValue(),
					Integer.valueOf(sMonth).intValue() - 1, Integer.valueOf(sDay).intValue());
			GregorianCalendar oGregorianCalendarTwo = new GregorianCalendar(Integer.valueOf(sYear2).intValue(),
					Integer.valueOf(sMonth2).intValue() - 1, Integer.valueOf(sDay2).intValue());

			long lMilliDiff = oGregorianCalendar.getTimeInMillis() - oGregorianCalendarTwo.getTimeInMillis();
			return (int) (lMilliDiff / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * This method calculates the number of whole days between the start and end
	 * dates.
	 * 
	 * @param start Calendar representation of start date
	 * @param end   Calendar representation of end date
	 * @return Formatted string of the two dates and the difference
	 */

	public static String getFormatedDateDifference(Calendar start, Calendar end) {

//		Calendar start = Calendar.getInstance();
//		start.getTime();
//		Calendar end = Calendar.getInstance();
//		end.set(2019, 9, 20);
//		System.out.println(getFormatedDateDifference(start, end));

		Date startDate = start.getTime();
		Date endDate = end.getTime();
		DateFormat dateFormat = DateFormat.getDateInstance();
		return "The difference between " + dateFormat.format(startDate) + " and " + dateFormat.format(endDate) + " is "
				+ getDateDifferenceInDays(start, end) + " days.";

	}

	/**
	 * This method calculates the number of whole days between the start and end
	 * dates.
	 * 
	 * @param start Calendar representation of start date
	 * @param end   Calendar representation of end date
	 * @return Number of whole days between given dates
	 */

	public static long getDateDifferenceInDays(Calendar start, Calendar end) {

//		Calendar start = Calendar.getInstance();
//		start.getTime();
//		Calendar end = Calendar.getInstance();
//		end.set(2019, 9, 20);

		Date startDate = start.getTime();
		Date endDate = end.getTime();
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return diffDays;

	}

	/**
	 * This method looks for records that are marked as a transfer. For each
	 * transfer record, it looks for the original record and marks it for deletion.
	 * 
	 * @param _oConnection       Database connection object
	 * @param _sTableName        Table for action
	 * @param _sFindTransferCode Cost code that denotes a record is a transfer
	 * @throws SQLException
	 */
	public static void processOutTransfers(Connection _oConnection, String _sTableName, String _sFindTransferCode)
			throws SQLException {

		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\-DD", "");
		oRegExHashMap.put("\\-dd", "");
		oRegExHashMap.put("\\-dD", "");
		oRegExHashMap.put("\\-Dd", "");

		XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName
				+ " SET FROMWHERE = 'TRANSFERXXX' WHERE COSTCODE = '" + _sFindTransferCode + "'");

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConnection,
				"SELECT ROWGENID, PLATE FROM " + _sTableName + " where COSTCODE = '" + _sFindTransferCode + "'");
		while (oRs.next()) {
			String sRowGenID = oRs.getString("ROWGENID");
			String sTransferPlate = oRs.getString("PLATE");
			String sLookForPlate = "";

			if (sTransferPlate.endsWith("-dd") || sTransferPlate.endsWith("-DD")) {
				sLookForPlate = XSaLTStringUtils.processRegExHashMap(oRegExHashMap, sTransferPlate);
			} else {
				sLookForPlate = sTransferPlate + "-dd";
			}

			ResultSet oRs2 = XSaLTDataUtils.querySQL(_oConnection,
					"SELECT ROWGENID, COSTCODE FROM " + _sTableName + " where PLATE like '" + sLookForPlate + "'");
			while (oRs2.next()) {
				String sNonTransferCostCode = oRs2.getString("COSTCODE");
				String sPreTransferVehicle = oRs2.getString("ROWGENID");
				int nRecordsAffected = XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName
						+ " SET COSTCODE = '" + sNonTransferCostCode + "' WHERE ROWGENID = '" + sRowGenID + "'");

				if (nRecordsAffected > 0) {
					XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName
							+ " SET FROMWHERE = 'DELETETRANSFER' WHERE ROWGENID = '" + sPreTransferVehicle + "'");
				}

			}

		}
	}

	/**
	 * This method imports the given Munis file into the given database table.
	 * 
	 * @param _oConnection         Database connection object
	 * @param _sImportFileName     File to import
	 * @param _sImportTableName    Table to import file into
	 * @param _nMaxDataLength      Maximum length of string to insert
	 * @param _bUseExtendedColumns Flag if extended columns should be used
	 * @param _bDropUnused         Flag if unused columns in table should be dropped
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void importMunisFile(Connection _oConnection, String _sImportFileName, String _sImportTableName,
			int _nMaxDataLength, boolean _bUseExtendedColumns, boolean _bDropUnused) throws IOException, SQLException {

		LinkedHashMap<String, Integer> oDbColumnsHashmap = new LinkedHashMap<String, Integer>();

		oDbColumnsHashmap.put("FIELD_NO", Integer.valueOf(3));
		oDbColumnsHashmap.put("TYPE", Integer.valueOf(8));
		oDbColumnsHashmap.put("FIELDNAME", Integer.valueOf(71));
		oDbColumnsHashmap.put("SIZE", Integer.valueOf(4));
		oDbColumnsHashmap.put("LN1", Integer.valueOf(4));
		oDbColumnsHashmap.put("COL1", Integer.valueOf(6));
		oDbColumnsHashmap.put("LN2", Integer.valueOf(4));
		oDbColumnsHashmap.put("COL2", Integer.valueOf(6));
		oDbColumnsHashmap.put("LN3", Integer.valueOf(4));
		oDbColumnsHashmap.put("COL3", Integer.valueOf(6));
		oDbColumnsHashmap.put("LN4", Integer.valueOf(4));
		oDbColumnsHashmap.put("COL4", Integer.valueOf(6));

		ArrayList<String> oNumericArrayList = new ArrayList<String>();
		oNumericArrayList.add("FIELD_NO");
		oNumericArrayList.add("SIZE");
		oNumericArrayList.add("LN1");
		oNumericArrayList.add("COL1");
		oNumericArrayList.add("LN2");
		oNumericArrayList.add("COL2");
		oNumericArrayList.add("LN3");
		oNumericArrayList.add("COL3");
		oNumericArrayList.add("LN4");
		oNumericArrayList.add("COL4");

		XSaLTDataUtils.importFixedDataFileToDatabase(_oConnection,
				"S:/Clients/TMAClientData/H-P/Munis/DataLayout/MunisLayout.txt", oDbColumnsHashmap, oNumericArrayList,
				"MUNIS_LAYOUT", "VARCHAR(200)", true, "MyISAM", 0);

		XSaLTDataUtils.dropColumnInTable(_oConnection, "MUNIS_LAYOUT", "FIELD_NO");

		// XSaLTDataUtils.executeSQL(_oConnection, "delete from munis_layout where
		// rowgenid > 364");

		oDbColumnsHashmap = new LinkedHashMap<String, Integer>();

		long lFieldNumber = 1;
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConnection,
				"SELECT FIELDNAME, SIZE FROM MUNIS_LAYOUT WHERE COL1 > 0 ORDER BY ROWGENID");
		while (oRs.next()) {
			String sFieldName = XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(), '0', 5)
					+ "_" + XSaLTStringUtils.regExMakeDataColumnName(oRs.getString("FIELDNAME"));
			// String sFieldName = "FIELD_" +
			// XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(),
			// '0', 5) + "";
			// String sFieldName = "FIELD_" + nFieldNumber;

			int nFieldSize = oRs.getInt("SIZE");
			oDbColumnsHashmap.put(sFieldName, Integer.valueOf(nFieldSize));
			lFieldNumber = lFieldNumber + 1;
		}

		if (_bUseExtendedColumns) {

			oRs = XSaLTDataUtils.querySQL(_oConnection,
					"SELECT FIELDNAME, SIZE FROM MUNIS_LAYOUT WHERE COL2 > 0 ORDER BY ROWGENID");
			while (oRs.next()) {
				String sFieldName = XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(), '0', 5)
						+ "_" + XSaLTStringUtils.regExMakeDataColumnName(oRs.getString("FIELDNAME"));
				// String sFieldName = "FIELD_" +
				// XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(),
				// '0', 5) + "";
				// String sFieldName = "FIELD_" + nFieldNumber;

				int nFieldSize = oRs.getInt("SIZE");
				oDbColumnsHashmap.put(sFieldName, Integer.valueOf(nFieldSize));
				lFieldNumber = lFieldNumber + 1;
			}

			oRs = XSaLTDataUtils.querySQL(_oConnection,
					"SELECT FIELDNAME, SIZE FROM MUNIS_LAYOUT WHERE COL3 > 0 ORDER BY ROWGENID");
			while (oRs.next()) {
				String sFieldName = XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(), '0', 5)
						+ "_" + XSaLTStringUtils.regExMakeDataColumnName(oRs.getString("FIELDNAME"));
				// String sFieldName = "FIELD_" +
				// XSaLTStringUtils.padLeftWithCharacter(Long.valueOf(lFieldNumber).toString(),
				// '0', 5) + "";
				// String sFieldName = "FIELD_" + nFieldNumber;

				int nFieldSize = oRs.getInt("SIZE");
				oDbColumnsHashmap.put(sFieldName, Integer.valueOf(nFieldSize));
				lFieldNumber = lFieldNumber + 1;
			}

		}

		XSaLTDataUtils.importFixedDataFileToDatabase(_oConnection, _sImportFileName, oDbColumnsHashmap,
				oNumericArrayList, _sImportTableName, "VARCHAR(50)", true, "MyISAM", _nMaxDataLength);

		StringBuffer oColumnsBuffer = new StringBuffer();

		for (Iterator<String> j = oDbColumnsHashmap.keySet().iterator(); j.hasNext();) {
			String sColumnName = (String) j.next();

			if (sColumnName.toLowerCase().indexOf("customer_number") != -1
					|| sColumnName.toLowerCase().indexOf("meter_number") != -1
					|| sColumnName.toLowerCase().indexOf("service_total") != -1
					|| sColumnName.toLowerCase().indexOf("ce_description__short") != -1
					|| sColumnName.toLowerCase().indexOf("00021") != -1
					|| sColumnName.toLowerCase().indexOf("00077") != -1) {

				oColumnsBuffer.append(sColumnName + ", ");

			}

		}

		oColumnsBuffer = new StringBuffer(oColumnsBuffer.substring(0, oColumnsBuffer.length() - 2));

		if (_bDropUnused) {
			XSaLTDataUtils.dropUnusedColumnsInTable(_oConnection, _sImportTableName);
		}

	}

	/**
	 * This method creates the postal scan line with a checksum digit based on the
	 * specified value and weights.
	 * 
	 * @param _sValueToEvaluate          Scan line string to use
	 * @param _sWeights                  String of weights to use for debugging
	 *                                   purposes
	 * @param _bAddSpaceBeforeCheckDigit Flag if a space should be added before the
	 *                                   checksum digit
	 * @return Formatted scan line string for postal sorting
	 */
	public static String createOCRAScanLineWithCheckDigit(String _sValueToEvaluate, String _sWeights,
			boolean _bAddSpaceBeforeCheckDigit) {

		// LOGGER.info( _sValueToEvaluate);
		// LOGGER.info( _sWeights);
		// LOGGER.info( "_sValueToEvaluate = " + _sValueToEvaluate.length() + ",
		// _sWeights " + _sWeights.length());

		StringBuffer sReturnScanLine = new StringBuffer();
		StringBuffer oValueBufferForArrayList = new StringBuffer();
		StringBuffer oWeightBufferForArrayList = new StringBuffer();
		StringBuffer oProductBufferForArrayList = new StringBuffer();
		StringBuffer oResultBufferForArrayList = new StringBuffer();

		int[] anValue = new int[_sValueToEvaluate.length()];
		for (int i = 0; i < _sValueToEvaluate.length(); i++) {
			sReturnScanLine.append(_sValueToEvaluate.substring(i, i + 1));
			if (_sValueToEvaluate.substring(i, i + 1).equals(" ")) {
				anValue[i] = 0;
			} else {
				anValue[i] = Integer.valueOf(_sValueToEvaluate.substring(i, i + 1)).intValue();
			}
			oValueBufferForArrayList.append(anValue[i] + "\t");

		}

		int[] anWeights = new int[_sValueToEvaluate.length()];
		for (int i = 0; i < _sValueToEvaluate.length(); i++) {
			anWeights[i] = Integer.valueOf(_sWeights.substring(i, i + 1)).intValue();
			oWeightBufferForArrayList.append(anWeights[i] + "\t");
		}

		int[] nResults = new int[_sValueToEvaluate.length()];

		int nTotal = 0;
		for (int i = 0; i < anValue.length; i++) {
			int nCurrentDigit = anValue[i];
			int nCurrentWeight = anWeights[i];

			nResults[i] = nCurrentDigit * nCurrentWeight;

			Integer oResults = Integer.valueOf(nResults[i]);

			if (oResults.toString().length() > 1) {
				Integer oNewIntegerOne = Integer.valueOf(oResults.toString().substring(0, 1));
				Integer oNewIntegerTwo = Integer.valueOf(oResults.toString().substring(1));
				nResults[i] = oNewIntegerOne.intValue() + oNewIntegerTwo.intValue();
				oProductBufferForArrayList.append(oNewIntegerOne + "+" + oNewIntegerTwo + "\t");

			} else {
				oProductBufferForArrayList.append(oResults + "\t");
			}

			oResultBufferForArrayList.append(nResults[i] + "\t");
			nTotal = nTotal + nResults[i];

		}

		int nModOut = (nTotal % 10);
		if (nModOut > 0) {
			nModOut = 10 - nModOut;
		}

		if (_bAddSpaceBeforeCheckDigit) {
			sReturnScanLine.append(" ");
		}

		sReturnScanLine.append(nModOut);

		// LOGGER.info( "Line:\t" + oValueBufferForArrayList.toString());
		// LOGGER.info( "Weight:\t" + oWeightBufferForArrayList.toString());
		// LOGGER.info( "Product:\t" + oProductBufferForArrayList.toString());
		// LOGGER.info( "Add:\t" + oResultBufferForArrayList.toString() + "\t = " +
		// nTotal + " mod10 is '" + nModOut + "'");
		// LOGGER.info(
		// "-------------------------------------------------------------------------------------------------");

		return sReturnScanLine.toString();
	}

	/**
	 * This method will create an OCR-B scan line with a calculated check digit
	 * 
	 * @param _sValueToEvaluate          Scan line string to use
	 * @param _sWeights                  String of weights to use
	 * @param _bAddSpaceBeforeCheckDigit Flag if a space should be added before the
	 *                                   checksum digit
	 * @return Formatted OCR-B type scan line for fulfillment scanning.
	 */
	public static String createOCRBScanLineWithCheckDigit(String _sValueToEvaluate, String _sWeights,
			boolean _bAddSpaceBeforeCheckDigit) {
		StringBuffer oReturnValue = new StringBuffer();
		oReturnValue.append(_sValueToEvaluate);

		_sValueToEvaluate = _sValueToEvaluate.toUpperCase();

		int[] anValues = new int[_sValueToEvaluate.length()];
		for (int i = 0; i < _sValueToEvaluate.length(); i++) {
			anValues[i] = XO_OCRB_SCANLINE_VALUES_MAP.get(_sValueToEvaluate.charAt(i));
		}

		// make sure the size of our weights string matches the value to evaluate
		while (_sWeights.length() < _sValueToEvaluate.length()) {
			_sWeights = _sWeights + _sWeights;
		}
		_sWeights = _sWeights.substring(0, _sValueToEvaluate.length());

		int[] anWeights = new int[_sValueToEvaluate.length()];
		for (int i = 0; i < _sValueToEvaluate.length(); i++) {
			anWeights[i] = Integer.parseInt(_sWeights.substring(i, i + 1));
		}

		int[] anProducts = new int[_sValueToEvaluate.length()];
		int nTotal = 0;
		for (int i = 0; i < anValues.length; i++) {
			anProducts[i] = anValues[i] * anWeights[i];
			nTotal += anProducts[i];
		}

		int nModulo = nTotal % 10;
		int nCheckDigit = 0;
		if (nModulo != 0) {
			nCheckDigit = 10 - nModulo;
		}

		if (_bAddSpaceBeforeCheckDigit) {
			oReturnValue.append(" ");
		}

		oReturnValue.append(nCheckDigit);

		return oReturnValue.toString();
	}

	public static final LinkedHashMap<Character, Integer> XO_OCRB_SCANLINE_VALUES_MAP;
	static {
		XO_OCRB_SCANLINE_VALUES_MAP = new LinkedHashMap<Character, Integer>();
		XO_OCRB_SCANLINE_VALUES_MAP.put('0', 0);
		XO_OCRB_SCANLINE_VALUES_MAP.put('1', 1);
		XO_OCRB_SCANLINE_VALUES_MAP.put('2', 2);
		XO_OCRB_SCANLINE_VALUES_MAP.put('3', 3);
		XO_OCRB_SCANLINE_VALUES_MAP.put('4', 4);
		XO_OCRB_SCANLINE_VALUES_MAP.put('5', 5);
		XO_OCRB_SCANLINE_VALUES_MAP.put('6', 6);
		XO_OCRB_SCANLINE_VALUES_MAP.put('7', 7);
		XO_OCRB_SCANLINE_VALUES_MAP.put('8', 8);
		XO_OCRB_SCANLINE_VALUES_MAP.put('9', 9);
		XO_OCRB_SCANLINE_VALUES_MAP.put('A', 10);
		XO_OCRB_SCANLINE_VALUES_MAP.put('B', 11);
		XO_OCRB_SCANLINE_VALUES_MAP.put('C', 12);
		XO_OCRB_SCANLINE_VALUES_MAP.put('D', 13);
		XO_OCRB_SCANLINE_VALUES_MAP.put('E', 14);
		XO_OCRB_SCANLINE_VALUES_MAP.put('F', 15);
		XO_OCRB_SCANLINE_VALUES_MAP.put('G', 16);
		XO_OCRB_SCANLINE_VALUES_MAP.put('H', 17);
		XO_OCRB_SCANLINE_VALUES_MAP.put('I', 18);
		XO_OCRB_SCANLINE_VALUES_MAP.put('J', 19);
		XO_OCRB_SCANLINE_VALUES_MAP.put('K', 20);
		XO_OCRB_SCANLINE_VALUES_MAP.put('L', 21);
		XO_OCRB_SCANLINE_VALUES_MAP.put('M', 22);
		XO_OCRB_SCANLINE_VALUES_MAP.put('N', 23);
		XO_OCRB_SCANLINE_VALUES_MAP.put('O', 24);
		XO_OCRB_SCANLINE_VALUES_MAP.put('P', 25);
		XO_OCRB_SCANLINE_VALUES_MAP.put('Q', 26);
		XO_OCRB_SCANLINE_VALUES_MAP.put('R', 27);
		XO_OCRB_SCANLINE_VALUES_MAP.put('S', 28);
		XO_OCRB_SCANLINE_VALUES_MAP.put('T', 29);
		XO_OCRB_SCANLINE_VALUES_MAP.put('U', 30);
		XO_OCRB_SCANLINE_VALUES_MAP.put('V', 31);
		XO_OCRB_SCANLINE_VALUES_MAP.put('W', 32);
		XO_OCRB_SCANLINE_VALUES_MAP.put('X', 33);
		XO_OCRB_SCANLINE_VALUES_MAP.put('Y', 34);
		XO_OCRB_SCANLINE_VALUES_MAP.put('Z', 35);
		XO_OCRB_SCANLINE_VALUES_MAP.put('/', 36);
		XO_OCRB_SCANLINE_VALUES_MAP.put('-', 37);
	}

	// String[] asScanLines = { "02 0002708 000001500 2",
	// "02 0002721 000003000 6", "02 0002722 000003000 5",
	// "02 0002723 000003000 4", "02 0003606 000000500 5",
	// "02 0003607 000000500 4", "02 0003802 000007500 2",
	// "02 0003803 000007500 1", "02 0003804 000003000 4",
	// "02 0004053 000003000 0", "02 0004419 000003000 9",
	// "02 0004420 000003000 6", "02 0006249 000000500 1",
	// "02 0006448 000003000 9", "02 0006535 000004500 6",
	// "02 0006536 000001500 1", "02 0007283 000000500 6",
	// "02 0007284 000003000 4", "02 0007285 000001500 2",
	// "02 0007286 000000500 3", "02 0008410 000000500 0",
	// "02 0008882 000000500 9", "02 0008883 000000500 8",
	// "02 0008884 000000500 7", "02 0009200 000003500 6",
	// "02 0009201 000007500 6", "02 0010133 000003000 1",
	// "02 0010134 000003000 0", "02 0010135 000003000 9",
	// "02 0010539 000000500 2", "02 0011145 000003000 5",
	// "02 0011145 000003000 5", "02 0011146 000003000 4",
	// "02 0011147 000003000 3", "02 0011148 000003000 2",
	// "02 0011149 000003000 1", "02 0011373 000004500 1",
	// "02 0011605 000003000 8", "02 0012180 000003000 9",
	// "02 0012926 000003000 8", "02 0013205 000003500 3",
	// "02 0013532 000003000 2", "02 0013560 000003000 7",
	// "02 0013605 000007500 0", "02 0013616 000000500 2",
	// "02 0013999 000003000 8", "02 0014000 000007500 9",
	// "02 0014001 000004500 5", "02 0014233 000003000 2",
	// "02 0014234 000003000 1", "02 0014313 000003000 5",
	// "02 0015141 000003000 0", "02 0015142 000003000 9",
	// "02 0015143 000003000 8", "02 0015144 000001500 6" };
	//
	// for (int i = 0; i < asScanLines.length; i++)
	// {
	// String sFullScanLine = asScanLines[i];
	// String sScanLine = asScanLines[i].substring(0, 20);
	// String sCheckDigit = asScanLines[i].substring(21, 22);
	// LOGGER.info( "'" + sScanLine + "', '" + sCheckDigit + "'");
	//
	// String sScanLineChecker =
	// XSaLTTriviaUtils.createScanLineWithCheckDigit(sScanLine,
	// "21212121212121212121212121212", true);
	//
	// if (sScanLineChecker.equalsIgnoreCase(sFullScanLine))
	// {
	// LOGGER.info( "Good");
	// }
	// else
	// {
	// LOGGER.info( "Bad");
	// }
	//
	// }
	//
	// LOGGER.info( "");

	/**
	 * This method checks a generated scan line for accuracy.
	 * 
	 * @param _sCheckLineWithCheckDigit Scan line to check
	 * @param _anWeights                Weights to check against
	 * @param _bPrintSummary            Flag if summary should be returned or scan
	 *                                  line string with results should be returned
	 * @return String with test results or scan line string with results appending
	 *         depending on the value in _bPrintSummary
	 */
	public static String Mod10Test(String _sCheckLineWithCheckDigit, int[] _anWeights, boolean _bPrintSummary) {

		String oReturnMod10Output = _sCheckLineWithCheckDigit + "\n";

		/*
		 * 
		 * int[] anWeights = { 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
		 * 1, 2, 1 };
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000148000000030008", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000983000000030006",
		 * anWeights, true));
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000818000000030007", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200001161000000050006",
		 * anWeights, true));
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000034000000050003", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200001588000000050001",
		 * anWeights, true));
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000982000000040006", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200001541000000050007",
		 * anWeights, true));
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200000876000000030006", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200002611000000030002",
		 * anWeights, true));
		 * 
		 * LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200001581000000020001", anWeights,
		 * true)); LOGGER.info( XSaLTTriviaUtils.Mod10Test("0200002102000000050006",
		 * anWeights, true));
		 * 
		 */

		// 000620714
		int[] anTestCheckDigitFinal = new int[_sCheckLineWithCheckDigit.length()];

		for (int i = 0; i < (anTestCheckDigitFinal.length - 1); i++) {
			///

			if (i == 0) {

				LOGGER.info(_sCheckLineWithCheckDigit.substring(i, 1));

				if (_sCheckLineWithCheckDigit.substring(i, 1).equals(" ")) {
					anTestCheckDigitFinal[i] = 0;
				} else {
					anTestCheckDigitFinal[i] = Integer.valueOf(_sCheckLineWithCheckDigit.substring(i, 1)).intValue();
				}
			} else {
				if (_sCheckLineWithCheckDigit.substring(i, i + 1).equals(" ")) {
					anTestCheckDigitFinal[i] = 0;
				} else {
					anTestCheckDigitFinal[i] = Integer.valueOf(_sCheckLineWithCheckDigit.substring(i, i + 1))
							.intValue();
				}
			}

		}

		int nCheckDigit = Integer.valueOf(_sCheckLineWithCheckDigit.substring(_sCheckLineWithCheckDigit.length() - 1,
				_sCheckLineWithCheckDigit.length())).intValue();

		int[] nResults = new int[_sCheckLineWithCheckDigit.length()];
		int nTotal = 0;

		for (int i = 0; i < anTestCheckDigitFinal.length - 1; i++) {
			nResults[i] = anTestCheckDigitFinal[i] * _anWeights[i];
			Integer oResults = Integer.valueOf(nResults[i]);
			if (oResults.toString().length() > 1) {
				Integer oNewIntegerOne = Integer.valueOf(oResults.toString().substring(0, 1));
				Integer oNewIntegerTwo = Integer.valueOf(oResults.toString().substring(1));
				nResults[i] = oNewIntegerOne.intValue() + oNewIntegerTwo.intValue();

				oReturnMod10Output = oReturnMod10Output + anTestCheckDigitFinal[i] + " * " + _anWeights[i] + " = "
						+ oResults.toString() + "(" + oNewIntegerOne.intValue() + " + " + oNewIntegerTwo.intValue()
						+ ") = " + nResults[i] + "\n";
			} else {
				oReturnMod10Output = oReturnMod10Output + anTestCheckDigitFinal[i] + " * " + _anWeights[i]
						+ " =           = " + nResults[i] + "\n";
			}

			nTotal = nTotal + nResults[i];

		}

		oReturnMod10Output = oReturnMod10Output + "Total             = " + nTotal + "\n";
		int nModOut = (nTotal % 10);
		oReturnMod10Output = oReturnMod10Output + "Mod 10 Remainder  = " + nModOut + "\n";

		if (nModOut > 0) {
			nModOut = 10 - nModOut;
		}

		String sIsGood = "";

		if (nCheckDigit == nModOut) {
			oReturnMod10Output = oReturnMod10Output + "Good!" + "\n";
			sIsGood = "Good!";
		} else {
			oReturnMod10Output = oReturnMod10Output + "No good!!!!!!!!!!!!!!!!!!" + "\n";
			sIsGood = "No Good!";
		}

		oReturnMod10Output = oReturnMod10Output
				+ "-----------------------------------------------------------------------------" + "\n";

		if (_bPrintSummary) {
			return oReturnMod10Output;
		} else {
			return _sCheckLineWithCheckDigit + " --> " + sIsGood;
		}

	}

	/**
	 * The method returns a LinkedHashMap of US States and Canadian Provinces
	 * 
	 * @return LinkedHashMap of US States and Canadian Provinces
	 */
	public static LinkedHashMap<String, String> getStatesAbbrvMap() {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();
		oTempLinkedHashMap.put("AL", "USA - ALABAMA");
		oTempLinkedHashMap.put("AK", "USO - ALASKA");
		oTempLinkedHashMap.put("AS", "USO - AMERICAN SAMOA");
		oTempLinkedHashMap.put("AZ", "USA - ARIZONA ");
		oTempLinkedHashMap.put("AR", "USA - ARKANSAS");
		oTempLinkedHashMap.put("CA", "USA - CALIFORNIA ");
		oTempLinkedHashMap.put("CO", "USA - COLORADO ");
		oTempLinkedHashMap.put("CT", "USA - CONNECTICUT");
		oTempLinkedHashMap.put("DE", "USA - DELAWARE");
		oTempLinkedHashMap.put("DC", "USA - DISTRICT OF COLUMBIA");
		oTempLinkedHashMap.put("FM", "USO - FEDERATED STATES OF MICRONESIA");
		oTempLinkedHashMap.put("FL", "USA - FLORIDA");
		oTempLinkedHashMap.put("GA", "USA - GEORGIA");
		oTempLinkedHashMap.put("GU", "USO - GUAM ");
		oTempLinkedHashMap.put("HI", "USO - HAWAII");
		oTempLinkedHashMap.put("ID", "USA - IDAHO");
		oTempLinkedHashMap.put("IL", "USA - ILLINOIS");
		oTempLinkedHashMap.put("IN", "USA - INDIANA");
		oTempLinkedHashMap.put("IA", "USA - IOWA");
		oTempLinkedHashMap.put("KS", "USA - KANSAS");
		oTempLinkedHashMap.put("KY", "USA - KENTUCKY");
		oTempLinkedHashMap.put("LA", "USA - LOUISIANA");
		oTempLinkedHashMap.put("ME", "USA - MAINE");
		oTempLinkedHashMap.put("MH", "USO - MARSHALL ISLANDS");
		oTempLinkedHashMap.put("MD", "USA - MARYLAND");
		oTempLinkedHashMap.put("MA", "USA - MASSACHUSETTS");
		oTempLinkedHashMap.put("MI", "USA - MICHIGAN");
		oTempLinkedHashMap.put("MN", "USA - MINNESOTA");
		oTempLinkedHashMap.put("MS", "USA - MISSISSIPPI");
		oTempLinkedHashMap.put("MO", "USA - MISSOURI");
		oTempLinkedHashMap.put("MT", "USA - MONTANA");
		oTempLinkedHashMap.put("NE", "USA - NEBRASKA");
		oTempLinkedHashMap.put("NV", "USA - NEVADA");
		oTempLinkedHashMap.put("NH", "USA - NEW HAMPSHIRE");
		oTempLinkedHashMap.put("NJ", "USA - NEW JERSEY");
		oTempLinkedHashMap.put("NM", "USA - NEW MEXICO");
		oTempLinkedHashMap.put("NY", "USA - NEW YORK");
		oTempLinkedHashMap.put("NC", "USA - NORTH CAROLINA");
		oTempLinkedHashMap.put("ND", "USA - NORTH DAKOTA");
		oTempLinkedHashMap.put("MP", "USO - NORTHERN MARIANA ISLANDS");
		oTempLinkedHashMap.put("OH", "USA - OHIO");
		oTempLinkedHashMap.put("OK", "USA - OKLAHOMA");
		oTempLinkedHashMap.put("OR", "USA - OREGON");
		oTempLinkedHashMap.put("PW", "USO - PALAU");
		oTempLinkedHashMap.put("PA", "USA - PENNSYLVANIA");
		oTempLinkedHashMap.put("PR", "USA - PUERTO RICO");
		oTempLinkedHashMap.put("RI", "USA - RHODE ISLAND");
		oTempLinkedHashMap.put("SC", "USA - SOUTH CAROLINA");
		oTempLinkedHashMap.put("SD", "USA - SOUTH DAKOTA");
		oTempLinkedHashMap.put("TN", "USA - TENNESSEE");
		oTempLinkedHashMap.put("TX", "USA - TEXAS");
		oTempLinkedHashMap.put("UT", "USA - UTAH");
		oTempLinkedHashMap.put("VT", "USA - VERMONT");
		oTempLinkedHashMap.put("VI", "USO - VIRGIN ISLANDS");
		oTempLinkedHashMap.put("VA", "USA - VIRGINIA ");
		oTempLinkedHashMap.put("WA", "USA - WASHINGTON");
		oTempLinkedHashMap.put("WV", "USA - WEST VIRGINIA");
		oTempLinkedHashMap.put("WI", "USA - WISCONSIN");
		oTempLinkedHashMap.put("WY", "USA - WYOMING");
		oTempLinkedHashMap.put("AE", "USM - Armed Forces Africa ");
		oTempLinkedHashMap.put("AA", "USM - Armed Forces Americas (except CAN - )");
		oTempLinkedHashMap.put("AE", "USM - Armed Forces CAN - ");
		oTempLinkedHashMap.put("AE", "USM - Armed Forces Europe");
		oTempLinkedHashMap.put("AE", "USM - Armed Forces Middle East");
		oTempLinkedHashMap.put("AP", "USM - Armed Forces Pacific");
		oTempLinkedHashMap.put("AB", "CAN - Alberta");
		oTempLinkedHashMap.put("BC", "CAN - British Columbia");
		oTempLinkedHashMap.put("MB", "CAN - Manitoba");
		oTempLinkedHashMap.put("NB", "CAN - New Brunswick");
		oTempLinkedHashMap.put("NL", "CAN - Newfoundland and Labrador");
		oTempLinkedHashMap.put("NT", "CAN - Northwest Territories");
		oTempLinkedHashMap.put("NS", "CAN - Nova Scotia");
		oTempLinkedHashMap.put("NU", "CAN - Nunavut");
		oTempLinkedHashMap.put("ON", "CAN - Ontario");
		oTempLinkedHashMap.put("PE", "CAN - Prince Edward Island");
		oTempLinkedHashMap.put("QC", "CAN - Quebec");
		oTempLinkedHashMap.put("SK", "CAN - Saskatchewan");
		oTempLinkedHashMap.put("YT", "CAN - Yukon");
		return oTempLinkedHashMap;
	}

	/**
	 * The method returns a LinkedHashMap of mail unit abbreviations
	 * 
	 * @return LinkedHashMap of mail unit abbreviations
	 */
	public static LinkedHashMap<String, String> getUnitAbbrvMap() {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();
		oTempLinkedHashMap.put("APT", "Apartment");
		oTempLinkedHashMap.put("BSMT", "Basement");
		oTempLinkedHashMap.put("BLDG", "Building");
		oTempLinkedHashMap.put("DEPT", "Department");
		oTempLinkedHashMap.put("FL", "Floor");
		oTempLinkedHashMap.put("FRNT", "Front");
		oTempLinkedHashMap.put("HNGR", "Hangar");
		oTempLinkedHashMap.put("LBBY", "Lobby");
		oTempLinkedHashMap.put("LOT", "Lot");
		oTempLinkedHashMap.put("LOWR", "Lower");
		oTempLinkedHashMap.put("OFC", "Office");
		oTempLinkedHashMap.put("PH", "Penthouse");
		oTempLinkedHashMap.put("PIER", "Pier");
		oTempLinkedHashMap.put("REAR", "Rear");
		oTempLinkedHashMap.put("RM", "Room");
		oTempLinkedHashMap.put("SIDE", "Side");
		oTempLinkedHashMap.put("SLIP", "Slip");
		oTempLinkedHashMap.put("SPC", "Space");
		oTempLinkedHashMap.put("STOP", "Stop");
		oTempLinkedHashMap.put("STE", "Suite");
		oTempLinkedHashMap.put("SUITE", "Suite");
		oTempLinkedHashMap.put("TRLR", "Trailer");
		oTempLinkedHashMap.put("UNIT", "Unit");
		oTempLinkedHashMap.put("UPPR", "Upper");
		return oTempLinkedHashMap;

	}

	/**
	 * The method returns a LinkedHashMap of mail unit abbreviations
	 * 
	 * @return LinkedHashMap of abbreviated mail unit abbreviations
	 */
	public static LinkedHashMap<String, String> getUnitDeliveryAbbrvMap() {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();
		oTempLinkedHashMap.put("APT", "Apartment");
		oTempLinkedHashMap.put("BLDG", "Building");
		oTempLinkedHashMap.put("DEPT", "Department");
		oTempLinkedHashMap.put("FL", "Floor");
		oTempLinkedHashMap.put("LOT", "Lot");
		oTempLinkedHashMap.put("OFC", "Office");
		oTempLinkedHashMap.put("RM", "Room");
		oTempLinkedHashMap.put("STOP", "Stop");
		oTempLinkedHashMap.put("STE", "Suite");
		oTempLinkedHashMap.put("TRLR", "Trailer");
		oTempLinkedHashMap.put("UNIT", "Unit");
		return oTempLinkedHashMap;
	}

	/**
	 * This method returns a LinkedHashMap of common street suffixes. The common
	 * incorrect String is the key and the corrected value is the value for each
	 * key-value pair.
	 * 
	 * @return HashMap with common street suffixes and corrections
	 */
	public static LinkedHashMap<String, String> getStreetSuffixesMap() {
		LinkedHashMap<String, String> oTempLinkedHashMap = new LinkedHashMap<String, String>();
		oTempLinkedHashMap.put("ALLEE", "ALY");
		oTempLinkedHashMap.put("ALLEY", "ALY");
		oTempLinkedHashMap.put("ALLY", "ALY");
		oTempLinkedHashMap.put("ALY", "ALY");
		oTempLinkedHashMap.put("ANEX", "ANX");
		oTempLinkedHashMap.put("ANNEX", "ANX");
		oTempLinkedHashMap.put("ANNX", "ANX");
		oTempLinkedHashMap.put("ANX", "ANX");
		oTempLinkedHashMap.put("ARC", "ARC");
		oTempLinkedHashMap.put("ARCADE", "ARC");
		oTempLinkedHashMap.put("AV", "AVE");
		oTempLinkedHashMap.put("AVE", "AVE");
		oTempLinkedHashMap.put("AVEN", "AVE");
		oTempLinkedHashMap.put("AVENU", "AVE");
		oTempLinkedHashMap.put("AVENUE", "AVE");
		oTempLinkedHashMap.put("AVN", "AVE");
		oTempLinkedHashMap.put("AVNUE", "AVE");
		oTempLinkedHashMap.put("BAYOO", "BYU");
		oTempLinkedHashMap.put("BAYOU", "BYU");
		oTempLinkedHashMap.put("BCH", "BCH");
		oTempLinkedHashMap.put("BEACH", "BCH");
		oTempLinkedHashMap.put("BEND", "BND");
		oTempLinkedHashMap.put("BLF", "BLF");
		oTempLinkedHashMap.put("BLUF", "BLF");
		oTempLinkedHashMap.put("BLUFF", "BLF");
		oTempLinkedHashMap.put("BLUFFS", "BLFS");
		oTempLinkedHashMap.put("BLVD", "BLVD");
		oTempLinkedHashMap.put("BND", "BND");
		oTempLinkedHashMap.put("BOT", "BTM");
		oTempLinkedHashMap.put("BOTTM", "BTM");
		oTempLinkedHashMap.put("BOTTOM", "BTM");
		oTempLinkedHashMap.put("BOUL", "BLVD");
		oTempLinkedHashMap.put("BOULEVARD", "BLVD");
		oTempLinkedHashMap.put("BOULV", "BLVD");
		oTempLinkedHashMap.put("BR", "BR");
		oTempLinkedHashMap.put("BRANCH", "BR");
		oTempLinkedHashMap.put("BRDGE", "BRG");
		oTempLinkedHashMap.put("BRG", "BRG");
		oTempLinkedHashMap.put("BRIDGE", "BRG");
		oTempLinkedHashMap.put("BRK", "BRK");
		oTempLinkedHashMap.put("BRNCH", "BR");
		oTempLinkedHashMap.put("BROOK", "BRK");
		oTempLinkedHashMap.put("BROOKS", "BRKS");
		oTempLinkedHashMap.put("BTM", "BTM");
		oTempLinkedHashMap.put("BURG", "BG");
		oTempLinkedHashMap.put("BURGS", "BGS");
		oTempLinkedHashMap.put("BYP", "BYP");
		oTempLinkedHashMap.put("BYPA", "BYP");
		oTempLinkedHashMap.put("BYPAS", "BYP");
		oTempLinkedHashMap.put("BYPASS", "BYP");
		oTempLinkedHashMap.put("BYPS", "BYP");
		oTempLinkedHashMap.put("CAMP", "CP");
		oTempLinkedHashMap.put("CANYN", "CYN");
		oTempLinkedHashMap.put("CANYON", "CYN");
		oTempLinkedHashMap.put("CAPE", "CPE");
		oTempLinkedHashMap.put("CAUSEWAY", "CSWY");
		oTempLinkedHashMap.put("CAUSWAY", "CSWY");
		oTempLinkedHashMap.put("CEN", "CTR");
		oTempLinkedHashMap.put("CENT", "CTR");
		oTempLinkedHashMap.put("CENTER", "CTR");
		oTempLinkedHashMap.put("CENTERS", "CTRS");
		oTempLinkedHashMap.put("CENTR", "CTR");
		oTempLinkedHashMap.put("CENTRE", "CTR");
		oTempLinkedHashMap.put("CIR", "CIR");
		oTempLinkedHashMap.put("CIRC", "CIR");
		oTempLinkedHashMap.put("CIRCL", "CIR");
		oTempLinkedHashMap.put("CIRCLE", "CIR");
		oTempLinkedHashMap.put("CIRCLES", "CIRS");
		oTempLinkedHashMap.put("CK", "CRK");
		oTempLinkedHashMap.put("CLB", "CLB");
		oTempLinkedHashMap.put("CLF", "CLF");
		oTempLinkedHashMap.put("CLFS", "CLFS");
		oTempLinkedHashMap.put("CLIFF", "CLF");
		oTempLinkedHashMap.put("CLIFFS", "CLFS");
		oTempLinkedHashMap.put("CLUB", "CLB");
		oTempLinkedHashMap.put("CMP", "CP");
		oTempLinkedHashMap.put("CNTER", "CTR");
		oTempLinkedHashMap.put("CNTR", "CTR");
		oTempLinkedHashMap.put("CNYN", "CYN");
		oTempLinkedHashMap.put("COMMON", "CMN");
		oTempLinkedHashMap.put("COR", "COR");
		oTempLinkedHashMap.put("CORNER", "COR");
		oTempLinkedHashMap.put("CORNERS", "CORS");
		oTempLinkedHashMap.put("CORS", "CORS");
		oTempLinkedHashMap.put("COURSE", "CRSE");
		oTempLinkedHashMap.put("COURT", "CT");
		oTempLinkedHashMap.put("COURTS", "CTS");
		oTempLinkedHashMap.put("COVE", "CV");
		oTempLinkedHashMap.put("COVES", "CVS");
		oTempLinkedHashMap.put("CP", "CP");
		oTempLinkedHashMap.put("CPE", "CPE");
		oTempLinkedHashMap.put("CR", "CRK");
		oTempLinkedHashMap.put("CRCL", "CIR");
		oTempLinkedHashMap.put("CRCLE", "CIR");
		oTempLinkedHashMap.put("CRECENT", "CRES");
		oTempLinkedHashMap.put("CREEK", "CRK");
		oTempLinkedHashMap.put("CRES", "CRES");
		oTempLinkedHashMap.put("CRESCENT", "CRES");
		oTempLinkedHashMap.put("CRESENT", "CRES");
		oTempLinkedHashMap.put("CREST", "CRST");
		oTempLinkedHashMap.put("CRK", "CRK");
		oTempLinkedHashMap.put("CROSSING", "XING");
		oTempLinkedHashMap.put("CROSSROAD", "XRD");
		oTempLinkedHashMap.put("CRSCNT", "CRES");
		oTempLinkedHashMap.put("CRSE", "CRSE");
		oTempLinkedHashMap.put("CRSENT", "CRES");
		oTempLinkedHashMap.put("CRSNT", "CRES");
		oTempLinkedHashMap.put("CRSSING", "XING");
		oTempLinkedHashMap.put("CRSSNG", "XING");
		oTempLinkedHashMap.put("CRT", "CT");
		oTempLinkedHashMap.put("CSWY", "CSWY");
		oTempLinkedHashMap.put("CT", "CT");
		oTempLinkedHashMap.put("CT", "CTS");
		oTempLinkedHashMap.put("CTR", "CTR");
		oTempLinkedHashMap.put("CURVE", "CURV");
		oTempLinkedHashMap.put("CV", "CV");
		oTempLinkedHashMap.put("CYN", "CYN");
		oTempLinkedHashMap.put("DALE", "DL");
		oTempLinkedHashMap.put("DAM", "DM");
		oTempLinkedHashMap.put("DIV", "DV");
		oTempLinkedHashMap.put("DIVIDE", "DV");
		oTempLinkedHashMap.put("DL", "DL");
		oTempLinkedHashMap.put("DM", "DM");
		oTempLinkedHashMap.put("DR", "DR");
		oTempLinkedHashMap.put("DRIV", "DR");
		oTempLinkedHashMap.put("DRIVE", "DR");
		oTempLinkedHashMap.put("DRIVES", "DRS");
		oTempLinkedHashMap.put("DRV", "DR");
		oTempLinkedHashMap.put("DV", "DV");
		oTempLinkedHashMap.put("DVD", "DV");
		oTempLinkedHashMap.put("EST", "EST");
		oTempLinkedHashMap.put("ESTATE", "EST");
		oTempLinkedHashMap.put("ESTATES", "ESTS");
		oTempLinkedHashMap.put("ESTS", "ESTS");
		oTempLinkedHashMap.put("EXP", "EXPY");
		oTempLinkedHashMap.put("EXPR", "EXPY");
		oTempLinkedHashMap.put("EXPRESS", "EXPY");
		oTempLinkedHashMap.put("EXPRESSWAY", "EXPY");
		oTempLinkedHashMap.put("EXPW", "EXPY");
		oTempLinkedHashMap.put("EXPY", "EXPY");
		oTempLinkedHashMap.put("EXT", "EXT");
		oTempLinkedHashMap.put("EXTENSION", "EXT");
		oTempLinkedHashMap.put("EXTENSIONS", "EXTS");
		oTempLinkedHashMap.put("EXTN", "EXT");
		oTempLinkedHashMap.put("EXTNSN", "EXT");
		oTempLinkedHashMap.put("EXTS", "EXTS");
		oTempLinkedHashMap.put("FALL", "FALL");
		oTempLinkedHashMap.put("FALLS", "FLS");
		oTempLinkedHashMap.put("FERRY", "FRY");
		oTempLinkedHashMap.put("FIELD", "FLD");
		oTempLinkedHashMap.put("FIELDS", "FLDS");
		oTempLinkedHashMap.put("FLAT", "FLT");
		oTempLinkedHashMap.put("FLATS", "FLTS");
		oTempLinkedHashMap.put("FLD", "FLD");
		oTempLinkedHashMap.put("FLDS", "FLDS");
		oTempLinkedHashMap.put("FLS", "FLS");
		oTempLinkedHashMap.put("FLT", "FLT");
		oTempLinkedHashMap.put("FLTS", "FLTS");
		oTempLinkedHashMap.put("FORD", "FRD");
		oTempLinkedHashMap.put("FORDS", "FRDS");
		oTempLinkedHashMap.put("FOREST", "FRST");
		oTempLinkedHashMap.put("FORESTS", "FRST");
		oTempLinkedHashMap.put("FORG", "FRG");
		oTempLinkedHashMap.put("FORGE", "FRG");
		oTempLinkedHashMap.put("FORGES", "FRGS");
		oTempLinkedHashMap.put("FORK", "FRK");
		oTempLinkedHashMap.put("FORKS", "FRKS");
		oTempLinkedHashMap.put("FORT", "FT");
		oTempLinkedHashMap.put("FRD", "FRD");
		oTempLinkedHashMap.put("FREEWAY", "FWY");
		oTempLinkedHashMap.put("FREEWY", "FWY");
		oTempLinkedHashMap.put("FRG", "FRG");
		oTempLinkedHashMap.put("FRK", "FRK");
		oTempLinkedHashMap.put("FRKS", "FRKS");
		oTempLinkedHashMap.put("FRRY", "FRY");
		oTempLinkedHashMap.put("FRST", "FRST");
		oTempLinkedHashMap.put("FRT", "FT");
		oTempLinkedHashMap.put("FRWAY", "FWY");
		oTempLinkedHashMap.put("FRWY", "FWY");
		oTempLinkedHashMap.put("FRY", "FRY");
		oTempLinkedHashMap.put("FT", "FT");
		oTempLinkedHashMap.put("FWY", "FWY");
		oTempLinkedHashMap.put("GARDEN", "GDN");
		oTempLinkedHashMap.put("GARDENS", "GDNS");
		oTempLinkedHashMap.put("GARDN", "GDN");
		oTempLinkedHashMap.put("GATEWAY", "GTWY");
		oTempLinkedHashMap.put("GATEWY", "GTWY");
		oTempLinkedHashMap.put("GATWAY", "GTWY");
		oTempLinkedHashMap.put("GDN", "GDN");
		oTempLinkedHashMap.put("GDNS", "GDNS");
		oTempLinkedHashMap.put("GLEN", "GLN");
		oTempLinkedHashMap.put("GLENS", "GLNS");
		oTempLinkedHashMap.put("GLN", "GLN");
		oTempLinkedHashMap.put("GRDEN", "GDN");
		oTempLinkedHashMap.put("GRDN", "GDN");
		oTempLinkedHashMap.put("GRDNS", "GDNS");
		oTempLinkedHashMap.put("GREEN", "GRN");
		oTempLinkedHashMap.put("GREENS", "GRNS");
		oTempLinkedHashMap.put("GRN", "GRN");
		oTempLinkedHashMap.put("GROV", "GRV");
		oTempLinkedHashMap.put("GROVE", "GRV");
		oTempLinkedHashMap.put("GROVES", "GRVS");
		oTempLinkedHashMap.put("GRV", "GRV");
		oTempLinkedHashMap.put("GTWAY", "GTWY");
		oTempLinkedHashMap.put("GTWY", "GTWY");
		oTempLinkedHashMap.put("HARB", "HBR");
		oTempLinkedHashMap.put("HARBOR", "HBR");
		oTempLinkedHashMap.put("HARBORS", "HBRS");
		oTempLinkedHashMap.put("HARBR", "HBR");
		oTempLinkedHashMap.put("HAVEN", "HVN");
		oTempLinkedHashMap.put("HAVN", "HVN");
		oTempLinkedHashMap.put("HBR", "HBR");
		oTempLinkedHashMap.put("HEIGHT", "HTS");
		oTempLinkedHashMap.put("HEIGHTS", "HTS");
		oTempLinkedHashMap.put("HGTS", "HTS");
		oTempLinkedHashMap.put("HIGHWAY", "HWY");
		oTempLinkedHashMap.put("HIGHWY", "HWY");
		oTempLinkedHashMap.put("HILL", "HL");
		oTempLinkedHashMap.put("HILLS", "HLS");
		oTempLinkedHashMap.put("HIWAY", "HWY");
		oTempLinkedHashMap.put("HIWY", "HWY");
		oTempLinkedHashMap.put("HL", "HL");
		oTempLinkedHashMap.put("HLLW", "HOLW");
		oTempLinkedHashMap.put("HLS", "HLS");
		oTempLinkedHashMap.put("HOLLOW", "HOLW");
		oTempLinkedHashMap.put("HOLLOWS", "HOLW");
		oTempLinkedHashMap.put("HOLW", "HOLW");
		oTempLinkedHashMap.put("HOLWS", "HOLW");
		oTempLinkedHashMap.put("HRBOR", "HBR");
		oTempLinkedHashMap.put("HT", "HTS");
		oTempLinkedHashMap.put("HTS", "HTS");
		oTempLinkedHashMap.put("HVN", "HVN");
		oTempLinkedHashMap.put("HWAY", "HWY");
		oTempLinkedHashMap.put("HWY", "HWY");
		oTempLinkedHashMap.put("INLET", "INLT");
		oTempLinkedHashMap.put("INLT", "INLT");
		oTempLinkedHashMap.put("IS", "IS");
		oTempLinkedHashMap.put("ISLAND", "IS");
		oTempLinkedHashMap.put("ISLANDS", "ISS");
		oTempLinkedHashMap.put("ISLE", "ISLE");
		oTempLinkedHashMap.put("ISLES", "ISLE");
		oTempLinkedHashMap.put("ISLND", "IS");
		oTempLinkedHashMap.put("ISLNDS", "ISS");
		oTempLinkedHashMap.put("ISS", "ISS");
		oTempLinkedHashMap.put("JCT", "JCT");
		oTempLinkedHashMap.put("JCTION", "JCT");
		oTempLinkedHashMap.put("JCTN", "JCT");
		oTempLinkedHashMap.put("JCTNS", "JCTS");
		oTempLinkedHashMap.put("JCTS", "JCTS");
		oTempLinkedHashMap.put("JUNCTION", "JCT");
		oTempLinkedHashMap.put("JUNCTIONS", "JCTS");
		oTempLinkedHashMap.put("JUNCTN", "JCT");
		oTempLinkedHashMap.put("JUNCTON", "JCT");
		oTempLinkedHashMap.put("KEY", "KY");
		oTempLinkedHashMap.put("KEYS", "KYS");
		oTempLinkedHashMap.put("KNL", "KNL");
		oTempLinkedHashMap.put("KNLS", "KNLS");
		oTempLinkedHashMap.put("KNOL", "KNL");
		oTempLinkedHashMap.put("KNOLL", "KNL");
		oTempLinkedHashMap.put("KNOLLS", "KNLS");
		oTempLinkedHashMap.put("KY", "KY");
		oTempLinkedHashMap.put("KYS", "KYS");
		oTempLinkedHashMap.put("LA", "LN");
		oTempLinkedHashMap.put("LAKE", "LK");
		oTempLinkedHashMap.put("LAKES", "LKS");
		oTempLinkedHashMap.put("LAND", "LAND");
		oTempLinkedHashMap.put("LANDING", "LNDG");
		oTempLinkedHashMap.put("LANE", "LN");
		oTempLinkedHashMap.put("LANES", "LN");
		oTempLinkedHashMap.put("LCK", "LCK");
		oTempLinkedHashMap.put("LCKS", "LCKS");
		oTempLinkedHashMap.put("LDG", "LDG");
		oTempLinkedHashMap.put("LDGE", "LDG");
		oTempLinkedHashMap.put("LF", "LF");
		oTempLinkedHashMap.put("LGT", "LGT");
		oTempLinkedHashMap.put("LIGHT", "LGT");
		oTempLinkedHashMap.put("LIGHTS", "LGTS");
		oTempLinkedHashMap.put("LK", "LK");
		oTempLinkedHashMap.put("LKS", "LKS");
		oTempLinkedHashMap.put("LN", "LN");
		oTempLinkedHashMap.put("LNDG", "LNDG");
		oTempLinkedHashMap.put("LNDNG", "LNDG");
		oTempLinkedHashMap.put("LOAF", "LF");
		oTempLinkedHashMap.put("LOCK", "LCK");
		oTempLinkedHashMap.put("LOCKS", "LCKS");
		oTempLinkedHashMap.put("LODG", "LDG");
		oTempLinkedHashMap.put("LODGE", "LDG");
		oTempLinkedHashMap.put("LOOP", "LOOP");
		oTempLinkedHashMap.put("LOOPS", "LOOP");
		oTempLinkedHashMap.put("MALL", "MALL");
		oTempLinkedHashMap.put("MANOR", "MNR");
		oTempLinkedHashMap.put("MANORS", "MNRS");
		oTempLinkedHashMap.put("MDW", "MDW");
		oTempLinkedHashMap.put("MDWS", "MDWS");
		oTempLinkedHashMap.put("MEADOW", "MDW");
		oTempLinkedHashMap.put("MEADOWS", "MDWS");
		oTempLinkedHashMap.put("MEDOWS", "MDWS");
		oTempLinkedHashMap.put("MEWS", "MEWS");
		oTempLinkedHashMap.put("MILL", "ML");
		oTempLinkedHashMap.put("MILLS", "MLS");
		oTempLinkedHashMap.put("MISSION", "MSN");
		oTempLinkedHashMap.put("MISSN", "MSN");
		oTempLinkedHashMap.put("ML", "ML");
		oTempLinkedHashMap.put("MLS", "MLS");
		oTempLinkedHashMap.put("MNR", "MNR");
		oTempLinkedHashMap.put("MNRS", "MNRS");
		oTempLinkedHashMap.put("MNT", "MT");
		oTempLinkedHashMap.put("MNTAIN", "MTN");
		oTempLinkedHashMap.put("MNTN", "MTN");
		oTempLinkedHashMap.put("MNTNS", "MTNS");
		oTempLinkedHashMap.put("MOTORWAY", "MTWY");
		oTempLinkedHashMap.put("MOUNT", "MT");
		oTempLinkedHashMap.put("MOUNTAIN", "MTN");
		oTempLinkedHashMap.put("MOUNTAINS", "MTNS");
		oTempLinkedHashMap.put("MOUNTIN", "MTN");
		oTempLinkedHashMap.put("MSN", "MSN");
		oTempLinkedHashMap.put("MSSN", "MSN");
		oTempLinkedHashMap.put("MT", "MT");
		oTempLinkedHashMap.put("MTIN", "MTN");
		oTempLinkedHashMap.put("MTN", "MTN");
		oTempLinkedHashMap.put("NCK", "NCK");
		oTempLinkedHashMap.put("NECK", "NCK");
		oTempLinkedHashMap.put("ORCH", "ORCH");
		oTempLinkedHashMap.put("ORCHARD", "ORCH");
		oTempLinkedHashMap.put("ORCHRD", "ORCH");
		oTempLinkedHashMap.put("OVAL", "OVAL");
		oTempLinkedHashMap.put("OVERPASS", "OPAS");
		oTempLinkedHashMap.put("OVL", "OVAL");
		oTempLinkedHashMap.put("PARK", "PARK");
		oTempLinkedHashMap.put("PARKS", "PARK");
		oTempLinkedHashMap.put("PARKWAY", "PKWY");
		oTempLinkedHashMap.put("PARKWAYS", "PKWY");
		oTempLinkedHashMap.put("PARKWY", "PKWY");
		oTempLinkedHashMap.put("PASS", "PASS");
		oTempLinkedHashMap.put("PASSAGE", "PSGE");
		oTempLinkedHashMap.put("PATH", "PATH");
		oTempLinkedHashMap.put("PATHS", "PATH");
		oTempLinkedHashMap.put("PIKE", "PIKE");
		oTempLinkedHashMap.put("PIKES", "PIKE");
		oTempLinkedHashMap.put("PINE", "PNE");
		oTempLinkedHashMap.put("PINES", "PNES");
		oTempLinkedHashMap.put("PK", "PARK");
		oTempLinkedHashMap.put("PKWAY", "PKWY");
		oTempLinkedHashMap.put("PKWY", "PKWY");
		oTempLinkedHashMap.put("PKWYS", "PKWY");
		oTempLinkedHashMap.put("PKY", "PKWY");
		oTempLinkedHashMap.put("PL", "PL");
		oTempLinkedHashMap.put("PLACE", "PL");
		oTempLinkedHashMap.put("PLAIN", "PLN");
		oTempLinkedHashMap.put("PLAINES", "PLNS");
		oTempLinkedHashMap.put("PLAINS", "PLNS");
		oTempLinkedHashMap.put("PLAZA", "PLZ");
		oTempLinkedHashMap.put("PLN", "PLN");
		oTempLinkedHashMap.put("PLNS", "PLNS");
		oTempLinkedHashMap.put("PLZ", "PLZ");
		oTempLinkedHashMap.put("PLZA", "PLZ");
		oTempLinkedHashMap.put("PNES", "PNES");
		oTempLinkedHashMap.put("POINT", "PT");
		oTempLinkedHashMap.put("POINTS", "PTS");
		oTempLinkedHashMap.put("PORT", "PRT");
		oTempLinkedHashMap.put("PORTS", "PRTS");
		oTempLinkedHashMap.put("PR", "PR");
		oTempLinkedHashMap.put("PRAIRIE", "PR");
		oTempLinkedHashMap.put("PRARIE", "PR");
		oTempLinkedHashMap.put("PRK", "PARK");
		oTempLinkedHashMap.put("PRR", "PR");
		oTempLinkedHashMap.put("PRT", "PRT");
		oTempLinkedHashMap.put("PRTS", "PRTS");
		oTempLinkedHashMap.put("PT", "PT");
		oTempLinkedHashMap.put("PTS", "PTS");
		oTempLinkedHashMap.put("RAD", "RADL");
		oTempLinkedHashMap.put("RADIAL", "RADL");
		oTempLinkedHashMap.put("RADIEL", "RADL");
		oTempLinkedHashMap.put("RADL", "RADL");
		oTempLinkedHashMap.put("RAMP", "RAMP");
		oTempLinkedHashMap.put("RANCH", "RNCH");
		oTempLinkedHashMap.put("RANCHES", "RNCH");
		oTempLinkedHashMap.put("RAPID", "RPD");
		oTempLinkedHashMap.put("RAPIDS", "RPDS");
		oTempLinkedHashMap.put("RD", "RD");
		oTempLinkedHashMap.put("RDG", "RDG");
		oTempLinkedHashMap.put("RDGE", "RDG");
		oTempLinkedHashMap.put("RDGS", "RDGS");
		oTempLinkedHashMap.put("RDS", "RDS");
		oTempLinkedHashMap.put("REST", "RST");
		oTempLinkedHashMap.put("RIDGE", "RDG");
		oTempLinkedHashMap.put("RIDGES", "RDGS");
		oTempLinkedHashMap.put("RIV", "RIV");
		oTempLinkedHashMap.put("RIVER", "RIV");
		oTempLinkedHashMap.put("RIVR", "RIV");
		oTempLinkedHashMap.put("RNCH", "RNCH");
		oTempLinkedHashMap.put("RNCHS", "RNCH");
		oTempLinkedHashMap.put("ROAD", "RD");
		oTempLinkedHashMap.put("ROADS", "RDS");
		oTempLinkedHashMap.put("ROUTE", "RTE");
		oTempLinkedHashMap.put("ROW", "ROW");
		oTempLinkedHashMap.put("RPD", "RPD");
		oTempLinkedHashMap.put("RPDS", "RPDS");
		oTempLinkedHashMap.put("RST", "RST");
		oTempLinkedHashMap.put("RUE", "RUE");
		oTempLinkedHashMap.put("RUN", "RUN");
		oTempLinkedHashMap.put("RVR", "RIV");
		oTempLinkedHashMap.put("SHL", "SHL");
		oTempLinkedHashMap.put("SHLS", "SHLS");
		oTempLinkedHashMap.put("SHOAL", "SHL");
		oTempLinkedHashMap.put("SHOALS", "SHLS");
		oTempLinkedHashMap.put("SHOAR", "SHR");
		oTempLinkedHashMap.put("SHOARS", "SHRS");
		oTempLinkedHashMap.put("SHORE", "SHR");
		oTempLinkedHashMap.put("SHORES", "SHRS");
		oTempLinkedHashMap.put("SHR", "SHR");
		oTempLinkedHashMap.put("SHRS", "SHRS");
		oTempLinkedHashMap.put("SKYWAY", "SKWY");
		oTempLinkedHashMap.put("SMT", "SMT");
		oTempLinkedHashMap.put("SPG", "SPG");
		oTempLinkedHashMap.put("SPGS", "SPGS");
		oTempLinkedHashMap.put("SPNG", "SPG");
		oTempLinkedHashMap.put("SPNGS", "SPGS");
		oTempLinkedHashMap.put("SPRING", "SPG");
		oTempLinkedHashMap.put("SPRINGS", "SPGS");
		oTempLinkedHashMap.put("SPRNG", "SPG");
		oTempLinkedHashMap.put("SPRNGS", "SPGS");
		oTempLinkedHashMap.put("SPUR", "SPUR");
		oTempLinkedHashMap.put("SPURS", "SPUR");
		oTempLinkedHashMap.put("SQ", "SQ");
		oTempLinkedHashMap.put("SQR", "SQ");
		oTempLinkedHashMap.put("SQRE", "SQ");
		oTempLinkedHashMap.put("SQRS", "SQS");
		oTempLinkedHashMap.put("SQU", "SQ");
		oTempLinkedHashMap.put("SQUARE", "SQ");
		oTempLinkedHashMap.put("SQUARES", "SQS");
		oTempLinkedHashMap.put("ST", "ST");
		oTempLinkedHashMap.put("STA", "STA");
		oTempLinkedHashMap.put("STATION", "STA");
		oTempLinkedHashMap.put("STATN", "STA");
		oTempLinkedHashMap.put("STN", "STA");
		oTempLinkedHashMap.put("STR", "ST");
		oTempLinkedHashMap.put("STRA", "STRA");
		oTempLinkedHashMap.put("STRAV", "STRA");
		oTempLinkedHashMap.put("STRAVE", "STRA");
		oTempLinkedHashMap.put("STRAVEN", "STRA");
		oTempLinkedHashMap.put("STRAVENUE", "STRA");
		oTempLinkedHashMap.put("STRAVN", "STRA");
		oTempLinkedHashMap.put("STREAM", "STRM");
		oTempLinkedHashMap.put("STREET", "ST");
		oTempLinkedHashMap.put("STREETS", "STS");
		oTempLinkedHashMap.put("STREME", "STRM");
		oTempLinkedHashMap.put("STRM", "STRM");
		oTempLinkedHashMap.put("STRT", "ST");
		oTempLinkedHashMap.put("STRVN", "STRA");
		oTempLinkedHashMap.put("STRVNUE", "STRA");
		oTempLinkedHashMap.put("SUMIT", "SMT");
		oTempLinkedHashMap.put("SUMITT", "SMT");
		oTempLinkedHashMap.put("SUMMIT", "SMT");
		oTempLinkedHashMap.put("TER", "TER");
		oTempLinkedHashMap.put("TERR", "TER");
		oTempLinkedHashMap.put("TERRACE", "TER");
		oTempLinkedHashMap.put("THROUGHWAY", "TRWY");
		oTempLinkedHashMap.put("TPK", "TPKE");
		oTempLinkedHashMap.put("TPKE", "TPKE");
		oTempLinkedHashMap.put("TR", "TRL");
		oTempLinkedHashMap.put("TRACE", "TRCE");
		oTempLinkedHashMap.put("TRACES", "TRCE");
		oTempLinkedHashMap.put("TRACK", "TRAK");
		oTempLinkedHashMap.put("TRACKS", "TRAK");
		oTempLinkedHashMap.put("TRAFFICWAY", "TRFY");
		oTempLinkedHashMap.put("TRAIL", "TRL");
		oTempLinkedHashMap.put("TRAILS", "TRL");
		oTempLinkedHashMap.put("TRAK", "TRAK");
		oTempLinkedHashMap.put("TRCE", "TRCE");
		oTempLinkedHashMap.put("TRFY", "TRFY");
		oTempLinkedHashMap.put("TRK", "TRAK");
		oTempLinkedHashMap.put("TRKS", "TRAK");
		oTempLinkedHashMap.put("TRL", "TRL");
		oTempLinkedHashMap.put("TRLS", "TRL");
		oTempLinkedHashMap.put("TRNPK", "TPKE");
		oTempLinkedHashMap.put("TRPK", "TPKE");
		oTempLinkedHashMap.put("TUNEL", "TUNL");
		oTempLinkedHashMap.put("TUNL", "TUNL");
		oTempLinkedHashMap.put("TUNLS", "TUNL");
		oTempLinkedHashMap.put("TUNNEL", "TUNL");
		oTempLinkedHashMap.put("TUNNELS", "TUNL");
		oTempLinkedHashMap.put("TUNNL", "TUNL");
		oTempLinkedHashMap.put("TURNPIKE", "TPKE");
		oTempLinkedHashMap.put("TURNPK", "TPKE");
		oTempLinkedHashMap.put("UN", "UN");
		oTempLinkedHashMap.put("UNDERPASS", "UPAS");
		oTempLinkedHashMap.put("UNION", "UN");
		oTempLinkedHashMap.put("UNIONS", "UNS");
		oTempLinkedHashMap.put("VALLEY", "VLY");
		oTempLinkedHashMap.put("VALLEYS", "VLYS");
		oTempLinkedHashMap.put("VALLY", "VLY");
		oTempLinkedHashMap.put("VDCT", "VIA");
		oTempLinkedHashMap.put("VIA", "VIA");
		oTempLinkedHashMap.put("VIADCT", "VIA");
		oTempLinkedHashMap.put("VIADUCT", "VIA");
		oTempLinkedHashMap.put("VIEW", "VW");
		oTempLinkedHashMap.put("VIEWS", "VWS");
		oTempLinkedHashMap.put("VILL", "VLG");
		oTempLinkedHashMap.put("VILLAG", "VLG");
		oTempLinkedHashMap.put("VILLAGE", "VLG");
		oTempLinkedHashMap.put("VILLAGES", "VLGS");
		oTempLinkedHashMap.put("VILLE", "VL");
		oTempLinkedHashMap.put("VILLG", "VLG");
		oTempLinkedHashMap.put("VILLIAGE", "VLG");
		oTempLinkedHashMap.put("VIS", "VIS");
		oTempLinkedHashMap.put("VIST", "VIS");
		oTempLinkedHashMap.put("VISTA", "VIS");
		oTempLinkedHashMap.put("VL", "VL");
		oTempLinkedHashMap.put("VLG", "VLG");
		oTempLinkedHashMap.put("VLGS", "VLGS");
		oTempLinkedHashMap.put("VLLY", "VLY");
		oTempLinkedHashMap.put("VLY", "VLY");
		oTempLinkedHashMap.put("VLYS", "VLYS");
		oTempLinkedHashMap.put("VST", "VIS");
		oTempLinkedHashMap.put("VSTA", "VIS");
		oTempLinkedHashMap.put("VW", "VW");
		oTempLinkedHashMap.put("VWS", "VWS");
		oTempLinkedHashMap.put("WALK", "WALK");
		oTempLinkedHashMap.put("WALKS", "WALK");
		oTempLinkedHashMap.put("WALL", "WALL");
		oTempLinkedHashMap.put("WAY", "WAY");
		oTempLinkedHashMap.put("WAYS", "WAYS");
		oTempLinkedHashMap.put("WELL", "WL");
		oTempLinkedHashMap.put("WELLS", "WLS");
		oTempLinkedHashMap.put("WLS", "WLS");
		oTempLinkedHashMap.put("WY", "WAY");
		oTempLinkedHashMap.put("XING", "XING");
		return oTempLinkedHashMap;
	}

	/**
	 * This method searches the given string for one of the standard directional
	 * characters (N S E W).
	 * 
	 * @param _sStreetAddress Street address to search
	 * @return Directional string if it is found
	 */
	public static String getDirectionalFromStreetString(String _sStreetAddress) {
		String sReturnDirectional = "";

		if (_sStreetAddress.indexOf(" N ") != -1) {
			sReturnDirectional = "N";
		} else if (_sStreetAddress.indexOf(" S ") != -1) {
			sReturnDirectional = "S";
		} else if (_sStreetAddress.indexOf(" W ") != -1) {
			sReturnDirectional = "W";
		} else if (_sStreetAddress.indexOf(" E ") != -1) {
			sReturnDirectional = "E";
		}

		return sReturnDirectional;
	}

	/**
	 * This method checks if a String is in a standard address format.
	 * 
	 * @param _sStringToCheck Address String to check
	 * @return Flag if String is in a standard address format
	 */
	public static boolean isAddressString(String _sStringToCheck) {
		if (_sStringToCheck.length() < 2) {
			return false;
		}

		if (_sStringToCheck.toUpperCase().startsWith("POBOX")) {
			return true;
		}

		if (_sStringToCheck.toUpperCase().startsWith("PO BOX")) {
			return true;
		}

		if (_sStringToCheck.toUpperCase().startsWith("P.O. BOX")) {
			return true;
		}

		char[] acChars = _sStringToCheck.split(" ")[0].toCharArray();

		for (char c : acChars) {
			if (c >= '0' && c <= '9') {
				return true;
			}
		}

		return false;
	}

	/**
	 * This method traverses a directory looking for files with the given
	 * extensions. If the found file has a desired extension, the method will rename
	 * the file based on the regular expression strings.
	 * 
	 * @param _sFilePath                File path to search
	 * @param _sWhatToLookForRegExp     Regular expression to search for
	 * @param _sWhatToReplaceWithRegExp Replacement string when regex is found
	 * @param _oIncludeExtensions       HashMap of extensions to modify
	 */
	public static void truncerRename(String _sFilePath, String _sWhatToLookForRegExp, String _sWhatToReplaceWithRegExp,
			HashMap<String, String> _oIncludeExtensions) {
		File f = new File(_sFilePath);
		File myFiles[] = f.listFiles();
		if (myFiles.length != 0) {
			for (int i = 0; i < myFiles.length; i++) {

				if (myFiles[i].isDirectory() == false) {
					boolean bProcessFileName = false;
					for (Iterator<String> j = _oIncludeExtensions.keySet().iterator(); j.hasNext();) {
						String sExtension = (String) j.next();
						if (myFiles[i].getAbsolutePath().endsWith(sExtension) == true) {
							bProcessFileName = true;
						}
					}

					if (bProcessFileName) {

						String sOriginalFileName = myFiles[i].getAbsolutePath();
						String sModifiedFileName = XSaLTStringUtils.processRegEx(sOriginalFileName,
								_sWhatToLookForRegExp, _sWhatToReplaceWithRegExp);
						if (!sOriginalFileName.equals(sModifiedFileName)) {
							LOGGER.info(sOriginalFileName + ", " + sModifiedFileName);
							File oFile = new File(sModifiedFileName);
							myFiles[i].renameTo(oFile);
						}

					}

					// int strIndex = myFiles[i].getName().indexOf(whatToLoookFor);
					// if (strIndex != -1)
					// {
					//
					// String s_file_name = myFiles[i].getName();
					// String s_file_number = ".0" + s_file_name.substring(16, 19);
					//
					// strIndex = strIndex + whatToLoookFor.length();
					// String newName = myFiles[i].getName().substring(strIndex,
					// myFiles[i].getName().length());
					//
					// File myTemp = new File(s_filepath, whatToLoookFor + s_file_number);
					//
					// myFiles[i].renameTo(myTemp);
					// }

				}
			}
		}
	}

	/**
	 * This method takes an address string and parses it into the following pieces
	 * in a HashMap: STREET_NUMBER STREET_DIRECTIONAL STREET_NAME STREET_SUFFIX
	 * UNIT_DESIGNATION UNIT_NUMBER
	 * 
	 * If any of the above is absent in the address, the value associated in the
	 * HashMap with that key will be an empty String. If the address is invalid for
	 * some reason, the value associated with the key ERROR will reflect the reason
	 * for the error.
	 * 
	 * @param _sAddress The address to parse.
	 * @return HashMap<String, String> containing the address parts.
	 */
	public static HashMap<String, String> splitAddressIntoParts(String _sAddress) {
		HashMap<String, String> oAddressParts = new HashMap<String, String>();
		oAddressParts.put("STREET_NUMBER", "");
		oAddressParts.put("STREET_DIRECTIONAL", "");
		oAddressParts.put("STREET_NAME", "");
		oAddressParts.put("STREET_SUFFIX", "");
		oAddressParts.put("UNIT_DESIGNATION", "");
		oAddressParts.put("UNIT_NUMBER", "");
		oAddressParts.put("COMPOSED_ADDRESS", "");

		_sAddress = _sAddress.trim().toUpperCase().replaceAll("[.,]", "").replaceAll("[ ]{2,}", " ");

		if (_sAddress.matches("^P[ ]?O[ ]?BOX[ ]?[0-9A-Z ]*$")) {
			oAddressParts.put("STREET_NAME", _sAddress.replaceFirst("^P ", "P").replaceFirst("^PO[ ]?", "PO "));
		} else {
			if (_sAddress.matches("^(ONE|TWO|THREE|FOUR|FIVE|SIX|SEVEN|EIGHT|NINE|TEN) .*")) {
				String sNumerical = _sAddress.split(" ")[0];
				if (sNumerical.equalsIgnoreCase("ONE")) {
					_sAddress = _sAddress.replaceFirst("^ONE ", "1 ");
				} else if (sNumerical.equalsIgnoreCase("TWO")) {
					_sAddress = _sAddress.replaceFirst("^TWO ", "2 ");
				} else if (sNumerical.equalsIgnoreCase("THREE")) {
					_sAddress = _sAddress.replaceFirst("^THREE ", "3 ");
				} else if (sNumerical.equalsIgnoreCase("FOUR")) {
					_sAddress = _sAddress.replaceFirst("^FOUR ", "4 ");
				} else if (sNumerical.equalsIgnoreCase("FIVE")) {
					_sAddress = _sAddress.replaceFirst("^FIVE ", "5 ");
				} else if (sNumerical.equalsIgnoreCase("SIX")) {
					_sAddress = _sAddress.replaceFirst("^SIX ", "6 ");
				} else if (sNumerical.equalsIgnoreCase("SEVEN")) {
					_sAddress = _sAddress.replaceFirst("^SEVEN ", "7 ");
				} else if (sNumerical.equalsIgnoreCase("EIGHT")) {
					_sAddress = _sAddress.replaceFirst("^EIGHT ", "8 ");
				} else if (sNumerical.equalsIgnoreCase("NINE")) {
					_sAddress = _sAddress.replaceFirst("^NINE ", "9 ");
				} else if (sNumerical.equalsIgnoreCase("TEN")) {
					_sAddress = _sAddress.replaceFirst("^TEN ", "10 ");
				}
			}

			if (_sAddress.matches("^[0-9][0-9A-Z\\-]* .*$")) {
				int nIdx = _sAddress.indexOf(" ");
				String sStreetNumber = _sAddress.substring(0, nIdx).trim();
				oAddressParts.put("STREET_NUMBER", sStreetNumber);
				_sAddress = _sAddress.substring(nIdx).trim();

				if (_sAddress.matches("^[NESW] .*$")) {
					nIdx = _sAddress.indexOf(" ");
					String sDirectional = _sAddress.substring(0, nIdx).trim();
					oAddressParts.put("STREET_DIRECTIONAL", sDirectional);
					_sAddress = _sAddress.substring(nIdx).trim();
				}

				ArrayList<String> oParts = XSaLTStringUtils.splitStringToArrayList(_sAddress, " ");
				if (oParts.size() == 1) {
					oAddressParts.put("STREET_NAME", oParts.get(0).trim());
				} else {
					HashMap<String, String> oUnitMap = new HashMap<String, String>();
					oUnitMap.putAll(getUnitAbbrvMap());
					for (String sUnit : oUnitMap.keySet()) {
						if (oParts.contains(sUnit)) {
							int nUnitIdx = oParts.indexOf(sUnit);
							if (nUnitIdx == (oParts.size() - 2)) {
								oAddressParts.put("UNIT_DESIGNATION", oParts.get(nUnitIdx));
								oAddressParts.put("UNIT_NUMBER", oParts.get(nUnitIdx + 1));
								oParts.remove(nUnitIdx + 1);
								oParts.remove(nUnitIdx);
							}
						} else if (oParts.get(oParts.size() - 1).matches("#.*")) {
							oAddressParts.put("UNIT_DESIGNATION", "#");
							oAddressParts.put("UNIT_NUMBER", oParts.get(oParts.size() - 1).replaceFirst("#", ""));
							int nRmv = oParts.size() - 1;
							oParts.remove(nRmv);
						}
					}

					if (oParts.size() == 1) {
						oAddressParts.put("STREET_NAME", oParts.get(0));
					} else {
						HashMap<String, String> oSuffixMap = new HashMap<String, String>();
						oSuffixMap.putAll(getStreetSuffixesMap());

						if (oSuffixMap.containsKey(oParts.get(oParts.size() - 1))
								|| oSuffixMap.containsValue(oParts.get(oParts.size() - 1))) {
							oAddressParts.put("STREET_SUFFIX", oParts.get(oParts.size() - 1));
							oParts.remove(oParts.size() - 1);
						}

						if (oParts.size() > 0) {
							StringBuffer oStreetName = new StringBuffer();
							for (int i = 0; i < oParts.size(); i++) {
								oStreetName.append(oParts.get(i) + " ");
							}

							oAddressParts.put("STREET_NAME", oStreetName.toString().replaceAll("_", " ").trim());
						} else {
							oAddressParts.put("ERROR", "Invalid street name");
						}
					}

				}
			} else {
				oAddressParts.put("ERROR", "No street number or PO BOX");
			}
		}

		if (oAddressParts.get("ERROR") == null) {
			String sFullComposedsAddress = (oAddressParts.get("STREET_NUMBER") + " "
					+ oAddressParts.get("STREET_DIRECTIONAL") + " " + oAddressParts.get("STREET_NAME") + " "
					+ oAddressParts.get("STREET_SUFFIX") + " " + oAddressParts.get("UNIT_DESIGNATION") + " "
					+ oAddressParts.get("UNIT_NUMBER")).replaceAll(",", " ").replaceAll("\\.", " ")
							.replaceAll(" +", " ").trim();

			oAddressParts.put("COMPOSED_ADDRESS", sFullComposedsAddress);
		}

		return oAddressParts;
	}

	public static double roundUpToNearestUSCent(double _dVal) {
		double dTmp = Math.ceil(_dVal * 100);
		return dTmp / 100;
	}

}
