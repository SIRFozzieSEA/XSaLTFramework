package com.codef.xsalt.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTDataScrubUtils {

	private static final Logger LOGGER = LogManager.getLogger(XSaLTDataScrubUtils.class.getName());

	/**
	 * Cleans phone number data, removing non-numerical data from phone numbers.
	 * 
	 * @param _oConn       Connection object
	 * @param _sTableName  Table for action
	 * @param _sColumnName Generated ID column in specified table
	 * @throws SQLException
	 */
	public static void convertYearColumnToFourDigitYear(Connection _oConn, String _sTableName, String _sColumnName)
			throws SQLException {
		Calendar oCalendar = Calendar.getInstance();
		int sYearThreshold = oCalendar.get(Calendar.YEAR) - 2000 + 1;

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT DISTINCT " + _sColumnName + " FROM " + _sTableName + " ORDER BY " + _sColumnName);
		while (oRs.next()) {
			int nFinalYear = 0;
			String sOriginalYearAsString = oRs.getString(_sColumnName);
			if (sOriginalYearAsString != null && !sOriginalYearAsString.equalsIgnoreCase("")) {
				int nOriginalYearAsInt = oRs.getInt(_sColumnName);
				if (nOriginalYearAsInt < 100) {
					if (nOriginalYearAsInt <= sYearThreshold) {
						nFinalYear = nOriginalYearAsInt + 2000;
					} else {
						nFinalYear = nOriginalYearAsInt + 1900;
					}
					String sSQL = "UPDATE " + _sTableName + " SET " + _sColumnName + " = '" + nFinalYear + "' WHERE "
							+ _sColumnName + " = '" + sOriginalYearAsString + "'";
					XSaLTDataUtils.executeSQL(_oConn, sSQL);
				}
			}
		}
	}

	/**
	 * Cleans phone number data, removing non-numerical data from phone numbers.
	 * 
	 * @param _oConn           Connection object
	 * @param _sDataTableName  Table for action
	 * @param _sRowGenIdColumn Generated ID column in specified table
	 * @param _sPhoneColumn    Phone number column name in specified table
	 * @throws SQLException
	 */
	public static void processPhoneColumn(Connection _oConn, String _sDataTableName, String _sRowGenIdColumn,
			String _sPhoneColumn) throws SQLException {
		String sSQL = "SELECT " + _sRowGenIdColumn + ", " + _sPhoneColumn + " FROM " + _sDataTableName
				+ " WHERE LENGTH(" + _sPhoneColumn + ") > 0";

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);

		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sRowGenIdColumn);
			String sPhone = oRs.getString(_sPhoneColumn);
			sPhone = XSaLTStringUtils.regExStripNonNumbersFromString(sPhone);
			String sSQL2 = "UPDATE " + _sDataTableName + " SET " + _sPhoneColumn + " = '" + sPhone + "' WHERE "
					+ _sRowGenIdColumn + " = '" + sRowGenId + "'";
			XSaLTDataUtils.executeSQL(_oConn, sSQL2);

		}

	}

	/**
	 * Cleans non-numerical data from phone numbers and adds dashes where
	 * appropriate.
	 * 
	 * @param _oConn           Connection object
	 * @param _sDataTableName  Table for action
	 * @param _sRowGenIdColumn Generated ID column in specified table.
	 * @param _sPhoneColumn    Phone number column name in specified table.
	 * @throws SQLException
	 */
	public static void processPhoneColumnMakeDashes(Connection _oConn, String _sDataTableName, String _sRowGenIdColumn,
			String _sPhoneColumn) throws SQLException {
		String sSQL = "SELECT " + _sRowGenIdColumn + ", " + _sPhoneColumn + " FROM " + _sDataTableName
				+ " WHERE LENGTH(" + _sPhoneColumn + ") > 0";

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);

		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sRowGenIdColumn);
			String sPhone = oRs.getString(_sPhoneColumn);
			sPhone = XSaLTStringUtils.regExStripNonNumbersFromString(sPhone);

			String sAreaCode = "";
			String sExchange = "";
			String sNumber = "";

			if (sPhone.length() >= 10) {

				sAreaCode = sPhone.substring(0, 3);
				sExchange = sPhone.substring(3, 6);
				sNumber = sPhone.substring(6, 10);

				String sSQL2 = "UPDATE " + _sDataTableName + " SET " + _sPhoneColumn + " = '" + sAreaCode + "-"
						+ sExchange + "-" + sNumber + "' WHERE " + _sRowGenIdColumn + " = '" + sRowGenId + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL2);
			}

		}

	}

	/**
	 * Fix date formats in the specified table & column. Change each to mm/dd/yyyy
	 * format to put into database.
	 * 
	 * @param _oConn          Connection object
	 * @param _sDataTableName Table for action
	 * @param _sDateColumn    Column holding the date to be processed.
	 * @throws SQLException
	 */
	public static void processDateColumns(Connection _oConn, String _sDataTableName, String _sDateColumn)
			throws SQLException {

		String sSQL = "SELECT " + _sDateColumn + " FROM " + _sDataTableName + " WHERE " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{2,4}$' OR " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{2,4}$' GROUP BY " + _sDateColumn;

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);

		while (oRs.next()) {
			String sDateValue = oRs.getString(_sDateColumn).trim();

			if (sDateValue.indexOf("-") != -1) {
				String sDateParts[] = sDateValue.split("-");
				String sFinalDate = sDateParts[0] + "/01/" + sDateParts[1];
				sSQL = "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '" + sFinalDate + "' WHERE "
						+ _sDateColumn + " = '" + sDateValue + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			} else {
				String sDateParts[] = sDateValue.split("/");
				String sFinalDate = sDateParts[0] + "/01/" + sDateParts[1];
				sSQL = "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '" + sFinalDate + "' WHERE "
						+ _sDateColumn + " = '" + sDateValue + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			}

		}

		oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT " + _sDateColumn + " FROM " + _sDataTableName + " WHERE "
				+ _sDateColumn
				+ " NOT REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{2,4}$' AND "
				+ _sDateColumn
				+ " NOT REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{2,4}$' GROUP BY "
				+ _sDateColumn);
		while (oRs.next()) {
			String sDateValue = oRs.getString(_sDateColumn).trim();
			String sDateDigitsOnly = XSaLTStringUtils.regExStripNonNumbersFromString(sDateValue);

			if (sDateDigitsOnly.length() == 8) {
				// format is most likely mm/dd/yyyy
				String sMonth = sDateDigitsOnly.substring(0, 2);
				String sDate = sDateDigitsOnly.substring(2, 4);
				String sYear = sDateDigitsOnly.substring(4, 8);
				String sFinalDate = sMonth + "/" + sDate + "/" + sYear;
				sSQL = "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '" + sFinalDate + "' WHERE "
						+ _sDateColumn + " = '" + sDateValue + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			} else if (sDateDigitsOnly.length() == 6) {
				// format is most likely mm/dd/yy or mmddyy

				if (sDateValue.equals(sDateDigitsOnly)) {
					// format is mmddyy
					String sMonth = sDateDigitsOnly.substring(0, 2);
					String sDate = sDateDigitsOnly.substring(2, 4);
					String sYear = sDateDigitsOnly.substring(4, 6);
					String sFinalDate = sMonth + "/" + sDate + "/" + sYear;
					sSQL = "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '" + sFinalDate + "' WHERE "
							+ _sDateColumn + " = '" + sDateValue + "'";
					XSaLTDataUtils.executeSQL(_oConn, sSQL);

				}

			}

		}

		oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT " + _sDateColumn + " FROM " + _sDataTableName + " WHERE "
				+ _sDateColumn
				+ " NOT REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{2,4}$' AND "
				+ _sDateColumn
				+ " NOT REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{2,4}$' GROUP BY "
				+ _sDateColumn);

		while (oRs.next()) {
			String sDateValue = oRs.getString(_sDateColumn);
			XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '' where "
					+ _sDateColumn + " = '" + sDateValue + "'");

		}

	}

	/**
	 * Fix SQL records with a trailing negative sign (eg 12-)
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixTrailingSigns(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn,
				"UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT('-',SUBSTR("
						+ _sColumnWithValues + ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-1)) WHERE "
						+ _sColumnWithValues + " LIKE '%-'");

		XSaLTDataUtils.executeSQL(_oConn,
				"UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = SUBSTR(" + _sColumnWithValues
						+ ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-1) WHERE " + _sColumnWithValues + " LIKE '%+'");

	}

	/**
	 * Replace 'cr' data as negative
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixCredits(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = TRIM("
				+ _sColumnWithValues + ") where " + _sColumnWithValues + " LIKE '%cr%'");

		XSaLTDataUtils.executeSQL(_oConn,
				"UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT('-',SUBSTR("
						+ _sColumnWithValues + ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-2)) WHERE "
						+ _sColumnWithValues + " LIKE '%cr'");

	}

	/**
	 * Change blank values to NULL values in database in given table and column.
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixVirtualNulls(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = null where "
				+ _sColumnWithValues + " like ''");

		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = null where "
				+ _sColumnWithValues + " like '%*%'");

	}

	/**
	 * replace parenthetical data as negative
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action.
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixParenthesis(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = TRIM("
				+ _sColumnWithValues + ") where " + _sColumnWithValues + " LIKE '%(%'");

		XSaLTDataUtils.executeSQL(_oConn,
				"UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT('-',SUBSTR("
						+ _sColumnWithValues + ",2,CHAR_LENGTH(" + _sColumnWithValues + ")-2)) WHERE "
						+ _sColumnWithValues + " LIKE '(%'");

	}

	/**
	 * remove dollar signs from data
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixDollars(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		String sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = substr(" + _sColumnWithValues
				+ ",locate('$'," + _sColumnWithValues + ")+1) WHERE " + _sColumnWithValues + " LIKE '%$%'";
		XSaLTDataUtils.executeSQL(_oConn, sQuery);
	}

	/**
	 * remove commas from data
	 * 
	 * @param _oConn             Database connection, ready for queries.
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixCommas(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {
		String sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = replace(" + _sColumnWithValues
				+ ",',','')";
		XSaLTDataUtils.executeSQL(_oConn, sQuery);
	}

	/**
	 * Convert old IBM trailing alphas into proper numbers
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixIBMTrailingAlphas(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {

		Set<Character> scChars;
		String sQuery;

		// convert these characters in to positive numbers
		Map<Character, String> mcPositive = new HashMap<Character, String>();

		mcPositive.put('{', "0");
		mcPositive.put('A', "1");
		mcPositive.put('B', "2");
		mcPositive.put('C', "3");
		mcPositive.put('D', "4");
		mcPositive.put('E', "5");
		mcPositive.put('F', "6");
		mcPositive.put('G', "7");
		mcPositive.put('H', "8");
		mcPositive.put('I', "9");

		// convert these characters into negative numbers
		Map<Character, String> mcNegtive = new HashMap<Character, String>();

		mcNegtive.put('}', "0");
		mcNegtive.put('J', "1");
		mcNegtive.put('K', "2");
		mcNegtive.put('L', "3");
		mcNegtive.put('M', "4");
		mcNegtive.put('N', "5");
		mcNegtive.put('O', "6");
		mcNegtive.put('P', "7");
		mcNegtive.put('Q', "8");
		mcNegtive.put('R', "9");

		scChars = mcPositive.keySet();
		for (char cChar : scChars) {
			sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT(SUBSTR("
					+ _sColumnWithValues + ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-1),'" + mcPositive.get(cChar)
					+ "') WHERE " + _sColumnWithValues + " LIKE '%" + cChar + "'";

			XSaLTDataUtils.executeSQL(_oConn, sQuery);

		}

		scChars = mcNegtive.keySet();
		for (char cChar : scChars) {
			sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT('-',SUBSTR("
					+ _sColumnWithValues + ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-1),'" + mcNegtive.get(cChar)
					+ "') WHERE " + _sColumnWithValues + " LIKE '%" + cChar + "'";

			XSaLTDataUtils.executeSQL(_oConn, sQuery);

		}

	}

	/**
	 * convert old IBM trailing alphas into proper numbers
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action
	 * @param _sColumnWithValues Column for action
	 * @throws SQLException
	 */
	public static void fixIBMTrailingAlphasIDC(Connection _oConn, String _sDataTableName, String _sColumnWithValues)
			throws SQLException {

		Set<Character> scChars;
		String sQuery;

		// convert these characters into negative numbers
		Map<Character, String> mcNegtive = new HashMap<Character, String>();

		// IDC ONLY!!!!!!
		mcNegtive.put('P', "0");
		mcNegtive.put('Q', "1");
		mcNegtive.put('R', "2");
		mcNegtive.put('S', "3");
		mcNegtive.put('T', "4");
		mcNegtive.put('U', "5");
		mcNegtive.put('V', "6");
		mcNegtive.put('W', "7");
		mcNegtive.put('X', "8");
		mcNegtive.put('Y', "9");

		scChars = mcNegtive.keySet();
		for (char cChar : scChars) {
			sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = CONCAT('-',SUBSTR("
					+ _sColumnWithValues + ",1,CHAR_LENGTH(" + _sColumnWithValues + ")-1),'" + mcNegtive.get(cChar)
					+ "') WHERE " + _sColumnWithValues + " LIKE '%" + cChar + "'";

			XSaLTDataUtils.executeSQL(_oConn, sQuery);

		}

	}

	/**
	 * Convert non-standard MySQL date format to standard MySQL date format.
	 * 
	 * @param _oConn             Connection object
	 * @param _sDataTableName    Table for action.
	 * @param _sColumnWithValues Column for action.
	 * @param bFourDigitYear     Is date in a four-digit year format?
	 * @param bYearFirst         Is the year field first in the date string?
	 * @param nCentury           The century to use with a two-digit year format
	 * @throws SQLException
	 */
	public static void convertNonDelimitedSQLDateStringColumnToStandard(Connection _oConn, String _sDataTableName,
			String _sColumnWithValues, boolean bFourDigitYear, boolean bYearFirst, int nCentury) throws SQLException {

		String sYear, sMonth, sDay;
		String sPadding = null;

		if (bYearFirst) {
			if (bFourDigitYear) {
				sYear = "substring(" + _sColumnWithValues + ",1,4)";
				sMonth = "substring(" + _sColumnWithValues + ",5,2)";
				sDay = "substring(" + _sColumnWithValues + ",7,2)";
			} else {
				sYear = "concat('" + nCentury + "',substring(" + _sColumnWithValues + ",1,2))";
				sMonth = "substring(" + _sColumnWithValues + ",3,2)";
				sDay = "substring(" + _sColumnWithValues + ",5,2)";
			}
		} else {
			sPadding = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = concat('0',"
					+ _sColumnWithValues + ") where length(" + _sColumnWithValues + ") = ";

			if (bFourDigitYear) {
				sPadding += "7";
				sYear = "substring(" + _sColumnWithValues + ",5,4)";
				sMonth = "substring(" + _sColumnWithValues + ",1,2)";
				sDay = "substring(" + _sColumnWithValues + ",3,2)";
			} else {
				sPadding += "5";
				sYear = "concat('" + nCentury + "',substring(" + _sColumnWithValues + ",5,4))";
				sMonth = "substring(" + _sColumnWithValues + ",1,2)";
				sDay = "substring(" + _sColumnWithValues + ",3,2)";
			}
		}

		if (sPadding != null) {
			XSaLTDataUtils.executeSQL(_oConn, sPadding);
		}

		String sQuery = "UPDATE " + _sDataTableName + " SET " + _sColumnWithValues + " = concat(" + sYear + ",'-',"
				+ sMonth + ",'-'," + sDay + ")";
		XSaLTDataUtils.executeSQL(_oConn, sQuery);
	}

	/**
	 * Convert a timestamp string in MM/DD/(YY)YY HH:MM:SS (...) format into sql
	 * compatable insert format
	 * 
	 * @param _oConn          Connection object
	 * @param _sDataTableName Table for action
	 * @param _sDateColumn    Date column for action
	 * @throws SQLException
	 */
	public static void fixDateSlashedWithTimeToSQLDateTime(Connection _oConn, String _sDataTableName,
			String _sDateColumn) throws SQLException {

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "Select distinct " + _sDateColumn + " from " + _sDataTableName
				+ " where " + _sDateColumn + " is not null");

		String sDateValue;
		String[] asDate, asTime;

		while (oRs.next()) {
			sDateValue = oRs.getString(1);

			if (sDateValue == null) {
				continue;
			}

			asDate = sDateValue.split(" ");

			if (asDate.length > 2) {
				Character cEx = asDate[1].charAt(2);

				asTime = asDate[1].split((String) cEx.toString());

				Integer nHour = Integer.parseInt(asTime[0]);

				if (asDate[2].equalsIgnoreCase("PM")) {
					if (nHour < 12) {
						nHour += 12;
					}
					asTime[0] = nHour.toString();
					asDate[1] = nHour + ":" + asTime[1] + ":" + asTime[2];
				}

				if (asDate[2].equalsIgnoreCase("AM")) {
					if (nHour == 12) {
						nHour = 0;
					}
					asTime[0] = nHour.toString();
					asDate[1] = nHour + ":" + asTime[1] + ":" + asTime[2];
				}
			}

			String sNewDate = XSaLTStringUtils.formatDateForMySQLBySlashesPlusStaticTime(asDate[0], asDate[1]);

			XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = '" + sNewDate
					+ "' WHERE " + _sDateColumn + " = '" + sDateValue + "'");
		}

	}

	/**
	 * Fix dates in mm/dd/yy, mm/dd/yyyy, mm-dd-yy, or mm-dd-yyyy formats. All
	 * strings not matching the particular date patterns are set to NULL. Format
	 * these dates into standard MySQL date format (yyyy-mm-dd).
	 * 
	 * @param _oConn          Connection object
	 * @param _sDataTableName Table for action.
	 * @param _sDateColumn    Date column for action.
	 * @throws SQLException
	 */
	public static void fixDatesToSQLDates(Connection _oConn, String _sDataTableName, String _sDateColumn)
			throws SQLException {

		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sDataTableName + " SET " + _sDateColumn + " = NULL WHERE "
				+ _sDateColumn
				+ " not REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{1,2}[[.solidus.]]{1}([[:digit:]]{2})|([[:digit:]]{4})$' AND "
				+ _sDateColumn
				+ " not REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{1,2}[[.hyphen-minus.]]{1}([[:digit:]]{2})|([[:digit:]]{4})$'");

		String sSQL = "" + "UPDATE " + _sDataTableName + "\n" + "   SET " + _sDateColumn + " = SUBSTRING(STR_TO_DATE("
				+ _sDateColumn + ", '%m/%d/%y'),1,10)\n" + "   WHERE " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{2}$'";
		XSaLTDataUtils.executeSQL(_oConn, sSQL);

		sSQL = "" + "UPDATE " + _sDataTableName + "\n" + "   SET " + _sDateColumn + " = SUBSTRING(STR_TO_DATE("
				+ _sDateColumn + ", '%m/%d/%Y'),1,10)\n" + "   WHERE " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{1,2}[[.solidus.]]{1}[[:digit:]]{4}$'";
		XSaLTDataUtils.executeSQL(_oConn, sSQL);

		sSQL = "" + "UPDATE " + _sDataTableName + "\n" + "   SET " + _sDateColumn + " = SUBSTRING(STR_TO_DATE("
				+ _sDateColumn + ", '%m-%d-%y'),1,10)\n" + "   WHERE " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{2}$'";
		XSaLTDataUtils.executeSQL(_oConn, sSQL);

		sSQL = "" + "UPDATE " + _sDataTableName + "\n" + "   SET " + _sDateColumn + " = SUBSTRING(STR_TO_DATE("
				+ _sDateColumn + ", '%m-%d-%Y'),1,10)\n" + "   WHERE " + _sDateColumn
				+ " REGEXP '^[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{1,2}[[.hyphen-minus.]]{1}[[:digit:]]{4}$'";
		XSaLTDataUtils.executeSQL(_oConn, sSQL);
	}

	/**
	 * This method will take a full address and break it down into "street number"
	 * and "street address" (useful for sorting)
	 * 
	 * @param _oConn                     The database connection
	 * @param _sDataTableName            The database table name to use
	 * @param _sPrimaryKeyColumnName     The primary key of the row in the above
	 *                                   table
	 * @param _sFullStreetColumnName     The column containing the address value you
	 *                                   wish to break down
	 * @param _sStreetNoColumnField      The column you wish to put the "street
	 *                                   number" into
	 * @param _sStreetAddressColumnField The column you wish to put the "street
	 *                                   address" into
	 * @param _sColumnType               The default column type, if this is null,
	 *                                   it will not create any columns, if this is
	 *                                   specified, it will create the columns
	 * @throws SQLException
	 */
	public static void breakFullStreetSpaceIntoDbFields(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sFullStreetColumnName, String _sStreetNoColumnField,
			String _sStreetAddressColumnField, String _sColumnType) throws SQLException {
		if (_sColumnType != null) {
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sStreetNoColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sStreetAddressColumnField);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sStreetNoColumnField
					+ " " + " BIGINT(40) after " + _sPrimaryKeyColumnName);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column "
					+ _sStreetAddressColumnField + " " + _sColumnType + " after " + _sStreetNoColumnField);

		}

		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX " + _sStreetAddressColumnField + "_INDEXX ON " + _sDataTableName
				+ " (" + _sStreetAddressColumnField + "(200))");

		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX " + _sStreetNoColumnField + "_INDEXX ON " + _sDataTableName
				+ " (" + _sStreetNoColumnField + ")");

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sFullStreetColumnName + " FROM " + _sDataTableName);
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			StringBuffer oFullStreet = new StringBuffer(
					XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_sFullStreetColumnName)));
			String sStreet[] = oFullStreet.toString().split(" ");
			StringBuffer oStreetNo = new StringBuffer(XSaLTStringUtils.regExStripNonNumbersFromString(sStreet[0]));
			StringBuffer oStreetName = new StringBuffer();
			for (int i = 1; i < sStreet.length; i++) {
				oStreetName.append(sStreet[i] + " ");
			}

			String sStreetNo = "";
			if (oStreetNo.toString().equals("")) {
				sStreetNo = "null";
			} else {
				sStreetNo = "'" + oStreetNo.toString() + "'";
			}

			String sSQL = "UPDATE " + _sDataTableName + " SET " + _sStreetNoColumnField + " = " + sStreetNo + ", "
					+ _sStreetAddressColumnField + " = '" + oStreetName.toString().trim() + "' WHERE "
					+ _sPrimaryKeyColumnName + " = '" + sRowGenId + "'";

			XSaLTDataUtils.executeSQL(_oConn, sSQL);

		}
	}

	/**
	 * Breaks down an incoming address line column into City, state, Zip and Plus4
	 * Columns
	 * 
	 * @param _oConn                  Connection Object
	 * @param _sDataTableName         The Table this column is in
	 * @param _sPrimaryKeyColumnName  The primary key of this table
	 * @param _sAddressLineColumnName The column containing the address line
	 * @param _sCityColumnField       The column to store the city info
	 * @param _sStateColumnField      The column to store the state info
	 * @param _sZipColumnField        The column to store the zip info
	 * @param _sPlus4ColumnField      The column to store the plus4 info
	 * @param _sColumnType            The default column type (if null, assumes
	 *                                columns already exist)
	 * @param _bOnlyIfDestinationNull If there is data already in the city column,
	 *                                skip that line
	 * @throws SQLException
	 */
	public static void breakAddressLineIntoCityStateZip(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sAddressLineColumnName, String _sCityColumnField,
			String _sStateColumnField, String _sZipColumnField, String _sPlus4ColumnField, String _sColumnType,
			boolean _bOnlyIfDestinationNull) throws SQLException {

		breakAddressLineIntoCityStateZipWithWhereClause(_oConn, _sDataTableName, _sPrimaryKeyColumnName,
				_sAddressLineColumnName, _sCityColumnField, _sStateColumnField, _sZipColumnField, _sPlus4ColumnField,
				_sColumnType, null, _bOnlyIfDestinationNull);
	}

	/**
	 * 
	 * @param _oConn                  Connection object
	 * @param _sDataTableName         Table for action
	 * @param _sPrimaryKeyColumnName  Primary key in table for action
	 * @param _sAddressLineColumnName Address column name
	 * @param _sCityColumnField       City column name
	 * @param _sStateColumnField      State column name
	 * @param _sZipColumnField        Zip column name
	 * @param _sPlus4ColumnField      Zip+4 column name
	 * @param _sColumnType            Column type for City, State, Zip and Plus-4
	 *                                columns
	 * @param _sWhereClause           Where clause to determine which addresses to
	 *                                act upon
	 * @param _bOnlyIfDestinationNull If there is already data in the City column,
	 *                                skip that line
	 * @throws SQLException
	 */
	public static void breakAddressLineIntoCityStateZipWithWhereClause(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sAddressLineColumnName, String _sCityColumnField,
			String _sStateColumnField, String _sZipColumnField, String _sPlus4ColumnField, String _sColumnType,
			String _sWhereClause, boolean _bOnlyIfDestinationNull) throws SQLException {

		if (_sColumnType != null) {
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sCityColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sStateColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sZipColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sPlus4ColumnField);

			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sCityColumnField,
					_sPrimaryKeyColumnName, _sColumnType);
			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sStateColumnField, _sCityColumnField,
					_sColumnType);
			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sZipColumnField, _sStateColumnField,
					_sColumnType);
			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sPlus4ColumnField, _sZipColumnField,
					_sColumnType);

		}

		String sQuery = "SELECT " + _sPrimaryKeyColumnName + ", " + _sAddressLineColumnName + " FROM " + _sDataTableName
				+ " WHERE " + _sAddressLineColumnName + " is not null";
		if (_bOnlyIfDestinationNull) {
			sQuery += " and " + _sCityColumnField + " is null";
		}

		if (_sWhereClause != null) {
			sQuery += " and " + _sWhereClause;
		}

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sQuery);

		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);

			HashMap<String, String> oMyMap = XSaLTDataScrubUtils.getSplitCityStateZip(
					XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_sAddressLineColumnName)));

			sQuery = "UPDATE " + _sDataTableName + " SET " + _sAddressLineColumnName + " = '', " + _sCityColumnField
					+ " = '" + oMyMap.get("CITY").toString().trim() + "', " + _sStateColumnField + " = '"
					+ oMyMap.get("STATE").toString().trim() + "', " + _sZipColumnField + " = '"
					+ oMyMap.get("ZIP").toString().trim() + "', " + _sPlus4ColumnField + " = '"
					+ oMyMap.get("PLUS4").toString().trim() + "' WHERE " + _sPrimaryKeyColumnName + " = '" + sRowGenId
					+ "'";
			XSaLTDataUtils.executeSQL(_oConn, sQuery);
		}
	}

	/**
	 * Breaks down an incoming address line column into City and State Columns
	 * 
	 * @param _oConn                  Connection Object
	 * @param _sDataTableName         The Table this column is in
	 * @param _sPrimaryKeyColumnName  The primary key of this table
	 * @param _sAddressLineColumnName The column containing the address line
	 * @param _sCityColumnField       The column to store the city info
	 * @param _sStateColumnField      The column to store the state info
	 * @param _sColumnType            The default column type (if null, assumes
	 *                                columns already exist)
	 * @param _bOnlyIfDestinationNull If there is data already in the city column,
	 *                                skip that line
	 * @throws SQLException
	 */
	public static void breakAddressLineIntoCityState(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sAddressLineColumnName, String _sCityColumnField,
			String _sStateColumnField, String _sColumnType, boolean _bOnlyIfDestinationNull) throws SQLException {

		if (_sColumnType != null) {
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sCityColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sStateColumnField);

			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sCityColumnField,
					_sPrimaryKeyColumnName, _sColumnType);
			XSaLTDataUtils.addColumnInTableAfterAnother(_oConn, _sDataTableName, _sStateColumnField, _sCityColumnField,
					_sColumnType);

		}

		String sQuery = "SELECT " + _sPrimaryKeyColumnName + ", " + _sAddressLineColumnName + " FROM " + _sDataTableName
				+ " WHERE " + _sAddressLineColumnName + " is not null";
		if (_bOnlyIfDestinationNull) {
			sQuery += " and " + _sCityColumnField + " is null";
		}

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sQuery);

		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);

			HashMap<String, String> oMyMap = XSaLTDataScrubUtils
					.getSplitCityState(XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_sAddressLineColumnName)));

			sQuery = "UPDATE " + _sDataTableName + " SET " + _sCityColumnField + " = '"
					+ oMyMap.get("CITY").toString().trim() + "', " + _sStateColumnField + " = '"
					+ oMyMap.get("STATE").toString().trim() + "' WHERE " + _sPrimaryKeyColumnName + " = '" + sRowGenId
					+ "'";
			XSaLTDataUtils.executeSQL(_oConn, sQuery);
		}
	}

	/**
	 * This method will take a full name (with just spaces - e.g. "Stephan
	 * Cossette") and break it down into "first name" and "last name" (useful for
	 * sorting)
	 * 
	 * @param _oConn                 The database connection
	 * @param _sDataTableName        The database table name to use
	 * @param _sPrimaryKeyColumnName The primary key of the row in the above table
	 * @param _sFullNameColumnName   The column containing the name value you wish
	 *                               to break down
	 * @param _sFirstNameColumnField The column you wish to put the "first name"
	 *                               into
	 * @param _sLastNameColumnField  The column you wish to put the "last name" into
	 * @param _sColumnType           The default column type, if this is null, it
	 *                               will not create any columns, if this is
	 *                               specified, it will create the columns
	 * @throws SQLException
	 */
	public static void breakFullnameFirstNameLastNameIntoDbFields(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sFullNameColumnName, String _sFirstNameColumnField,
			String _sLastNameColumnField, String _sColumnType, boolean _bUseFirstSpace) throws SQLException {

		if (_sColumnType != null) {
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sFirstNameColumnField);
			XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName, _sLastNameColumnField);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sFirstNameColumnField
					+ " " + _sColumnType + " after " + _sPrimaryKeyColumnName);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sLastNameColumnField
					+ " " + _sColumnType + " after " + _sFirstNameColumnField);

		}
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sFullNameColumnName + " FROM " + _sDataTableName);

		String sQuery;
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);

			HashMap<String, String> oMyMap = XSaLTDataScrubUtils.getSplitFirstNameLastNameSpace(
					XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_sFullNameColumnName)), _bUseFirstSpace);

			sQuery = "UPDATE " + _sDataTableName + " SET " + _sFirstNameColumnField + " = '"
					+ oMyMap.get("FIRSTNAME").toString().trim() + "', " + _sLastNameColumnField + " = '"
					+ oMyMap.get("LASTNAME").toString().trim() + "' WHERE " + _sPrimaryKeyColumnName + " = '"
					+ sRowGenId + "'";
			XSaLTDataUtils.executeSQL(_oConn, sQuery);
		}
	}

	/**
	 * This method will take a full name (with commas - e.g. "Cossette, Stephan")
	 * and break it down into "first name" and "last name" (useful for sorting)
	 * 
	 * @param _oConn                 The database connection
	 * @param _sDataTableName        The database table name to use
	 * @param _sPrimaryKeyColumnName The primary key of the row in the above table
	 * @param _sFullNameColumnName   The column containing the name value you wish
	 *                               to break down
	 * @param _sFirstNameColumnField The column you wish to put the "first name"
	 *                               into
	 * @param _sLastNameColumnField  The column you wish to put the "last name" into
	 * @param _sColumnType           The default column type, if this is null, it
	 *                               will not create any columns, if this is
	 *                               specified, it will create the columns
	 * @throws SQLException
	 */
	public static void breakFullnameLastNameFirstNameIntoDbFieldsWithComma(Connection _oConn, String _sDataTableName,
			String _sPrimaryKeyColumnName, String _sFullNameColumnName, String _sFirstNameColumnField,
			String _sLastNameColumnField, String _sColumnType) throws SQLException {
		if (_sColumnType != null) {
			// XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName,
			// _sFirstNameColumnField);
			// XSaLTDataUtils.dropColumnInTable(_oConn, _sDataTableName,
			// _sLastNameColumnField);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sFirstNameColumnField
					+ " " + _sColumnType + " after " + _sPrimaryKeyColumnName);

			XSaLTDataUtils.executeSQL(_oConn, "alter table " + _sDataTableName + " add column " + _sLastNameColumnField
					+ " " + _sColumnType + " after " + _sFirstNameColumnField);

		}
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sFullNameColumnName + " FROM " + _sDataTableName);
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			String sFullName = XSaLTStringUtils.getEmptyStringIfNull(oRs.getString(_sFullNameColumnName));
			HashMap<String, String> oMyMap = null;
			if (sFullName.indexOf(",") != -1) {
				oMyMap = XSaLTDataScrubUtils.getSplitFirstNameLastNameComma(sFullName);
			} else {
				oMyMap = XSaLTDataScrubUtils.getSplitFirstNameLastNameSpaceReverse(sFullName);
			}
			XSaLTDataUtils.executeSQL(_oConn,
					"UPDATE " + _sDataTableName + " SET " + _sFirstNameColumnField + " = '"
							+ XSaLTStringUtils.regExReplaceStringForInsert(oMyMap.get("FIRSTNAME").toString().trim())
							+ "', " + _sLastNameColumnField + " = '"
							+ XSaLTStringUtils.regExReplaceStringForInsert(oMyMap.get("LASTNAME").toString().trim())
							+ "' WHERE " + _sPrimaryKeyColumnName + " = '" + sRowGenId + "'");
		}
	}

	/**
	 * This method breaks a label and date column into separate columns for the
	 * label and the date
	 * 
	 * @param _oConn                The database connection object
	 * @param _sDataTableName       The table to act on
	 * @param _sFullLabelColumnName The column to break
	 * @param _sLabelColumnField    The label column to populate
	 * @param _sDateColumnField     The date column to populate
	 * @param _sLabelColumnType     The column type for the label column (if not
	 *                              null, the column will be created)
	 * @param _sDateColumnType      The column type for the date column (if not
	 *                              null, the column will be created)
	 * @param _sDateRegexp          The regular expression used to find the date in
	 *                              the original column
	 * @param _sDateFormat          The java simple date format that the date will
	 *                              be in
	 * @throws SQLException
	 */
	public static void breakLabelAndDateFieldIntoDbFields(Connection _oConn, String _sDataTableName,
			String _sFullLabelColumnName, String _sLabelColumnField, String _sDateColumnField, String _sLabelColumnType,
			String _sDateColumnType, String _sDateRegexp, String _sDateFormat) throws SQLException {
		if (_sLabelColumnType != null) {
			XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sDataTableName + " ADD COLUMN " + _sLabelColumnField
					+ " " + _sLabelColumnType + " AFTER " + _sFullLabelColumnName);
		}

		if (_sDateColumnType != null) {
			XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sDataTableName + " ADD COLUMN " + _sDateColumnField
					+ " " + _sDateColumnType + " AFTER " + _sLabelColumnField);
		}

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT DISTINCT " + _sFullLabelColumnName + " AS my_col FROM "
				+ _sDataTableName + " WHERE " + _sFullLabelColumnName + " REGEXP '" + _sDateRegexp + "'");

		Pattern oPattern = Pattern.compile("^.*(" + _sDateRegexp + ").*$");

		while (oRs.next()) {

			String sLabelAndDate = oRs.getString("my_col");
			java.util.regex.Matcher oMatcher = oPattern.matcher(sLabelAndDate);
			String sDate = null;
			if (oMatcher.find()) {
				sDate = XSaLTStringUtils.formatDateForMySQL(oMatcher.group(1), _sDateFormat);
			}

			XSaLTStringUtils.formatDateForMySQL(oMatcher.group(1), _sDateFormat);
			if (sDate == null || sDate.trim().length() == 0) {
				LOGGER.info("====> " + sLabelAndDate);
				sDate = null;
			} else {
				sDate = "'" + sDate + "'";
			}
			String sLabel = sLabelAndDate.replaceAll(sDate, "");

			XSaLTDataUtils.executeSQL(_oConn,
					"UPDATE " + _sDataTableName + " SET " + _sLabelColumnField + " = '" + sLabel + "', "
							+ _sDateColumnField + " = " + sDate + " " + "WHERE " + _sFullLabelColumnName + " = '"
							+ sLabelAndDate + "'");
		}
	}

	/**
	 * This method returns a HashMap of FIRSTNAME and LASTNAME from a given comma
	 * split String (e.g. "Cossette, Stephan")
	 * 
	 * @param _sFullName The full name to split
	 * @return A HashMap of FIRSTNAME and LASTNAME
	 */
	private static HashMap<String, String> getSplitFirstNameLastNameComma(String _sFullName) {
		HashMap<String, String> oNameHashMap = new HashMap<String, String>();
		if (_sFullName == null) {
			oNameHashMap.put("FIRSTNAME", "");
			oNameHashMap.put("LASTNAME", "");
		} else if (_sFullName.lastIndexOf(",") == -1) {
			oNameHashMap.put("FIRSTNAME", "");
			oNameHashMap.put("LASTNAME", _sFullName);
		} else {
			String[] asNameString = _sFullName.split(",");
			oNameHashMap.put("LASTNAME", (asNameString.length >= 1 ? asNameString[0].trim() : ""));
			oNameHashMap.put("FIRSTNAME", (asNameString.length > 1 ? asNameString[1].trim() : ""));
		}
		return oNameHashMap;
	}

	/**
	 * Split a ZIP code field into Zip5 and Plus4 fields
	 * 
	 * @param _oConnection    Connection object
	 * @param _sTableName     Table for action
	 * @param _sZipColumn     Full zip code column
	 * @param _s5Column       Zip5 column name
	 * @param _s4Column       Plus4 column name
	 * @param _sNewColumnType New column data type
	 * @throws SQLException
	 */
	public static void splitZipField(Connection _oConnection, String _sTableName, String _sZipColumn, String _s5Column,
			String _s4Column, String _sNewColumnType) throws SQLException {
		if (_sNewColumnType != null) {
			XSaLTDataUtils.dropColumnInTable(_oConnection, _sTableName, _s5Column);
			XSaLTDataUtils.dropColumnInTable(_oConnection, _sTableName, _s4Column);

			XSaLTDataUtils.addColumnInTable(_oConnection, _sTableName, _s5Column, _sNewColumnType);
			XSaLTDataUtils.addColumnInTable(_oConnection, _sTableName, _s4Column, _sNewColumnType);
		}

		XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _s5Column + " = left(" + _sZipColumn
				+ ", 5) WHERE " + _sZipColumn + " NOT REGEXP '[a-zA-Z]'");
		XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _s5Column + " = " + _sZipColumn
				+ " WHERE " + _sZipColumn + " REGEXP '[a-zA-Z]'");
		XSaLTDataUtils.executeSQL(_oConnection, "UPDATE " + _sTableName + " SET " + _s4Column + " = right("
				+ _sZipColumn + ", 4) WHERE length(" + _sZipColumn + ") >= 9");
	}

	/**
	 * Parse an address to get City, State and Zip based on standard format. Format
	 * example: Warrenville, IL 60555-1234
	 * 
	 * @param _sAddressLine String to parse
	 * @return HashMap<String, String> HashMap contains 4 entries: CITY, STATE, ZIP,
	 *         PLUS4
	 */
	private static HashMap<String, String> getSplitCityStateZip(String _sAddressLine) {

		HashMap<String, String> msParts = new HashMap<String, String>();

		msParts.put("CITY", "");
		msParts.put("STATE", "");
		msParts.put("ZIP", "");
		msParts.put("PLUS4", "");

		if (_sAddressLine == null || _sAddressLine.length() == 0) {
			return msParts;
		}

		String[] asParts = _sAddressLine.split(" ");
		int nNumParts = asParts.length;

		if (nNumParts <= 0) {
			return msParts;
		}

		nNumParts--;
		String sZN4 = asParts[nNumParts];
		String sState = "";

		if (sZN4.charAt(0) >= 'A' && sZN4.charAt(1) >= 'A') {
			sState = sZN4.substring(0, 2);
			sZN4 = sZN4.substring(2);
		} else if (sZN4.charAt(0) >= 'A' && sZN4.charAt(1) <= '9' && sZN4.charAt(2) <= '9') {
			sState = sZN4.substring(0, 1);
			sZN4 = sZN4.substring(1);
		}

		if (sZN4.length() < 5 || sZN4.length() > 10) {
			LOGGER.warn("Address Line split encountered non-standard data: " + _sAddressLine);
		}

		String[] asZN4 = sZN4.split("-");
		msParts.put("ZIP", asZN4[0].replaceAll("_", " "));
		if (asZN4.length > 1) {
			msParts.put("PLUS4", asZN4[1].replaceAll("_", " "));
		} else {
			msParts.put("PLUS4", "");
		}

		if (sState.length() == 0) {
			nNumParts--;
			if (nNumParts == -1) {
				msParts.put("CITY", "");
				msParts.put("STATE", "");
				return msParts;
			}

			sState = asParts[nNumParts];
		}

		String sLast = "";
		if (sState.lastIndexOf(',') == -1) {
			msParts.put("STATE", sState.replaceAll("_", " "));
		} else {
			String[] asNew = sState.split(",");
			msParts.put("STATE", asNew[1].replaceAll("_", " "));
			sLast = asNew[0];
		}

		StringBuilder sbCity = new StringBuilder();
		for (int i = 0; i < nNumParts; i++) {
			sbCity.append(asParts[i]);
			if (i < nNumParts - 1) {
				sbCity.append(" ");
			}
		}
		if (sLast != "") {
			sbCity.append(" " + sLast);
		}

		String sCity = sbCity.toString().trim();
		if (sCity.length() < 1) {
			sCity = sState;
		}
		if (sCity.lastIndexOf(',') == sCity.length() - 1) {
			sCity = sCity.substring(0, sCity.length() - 1);
		}
		msParts.put("CITY", sCity.replaceAll("_", " "));

		return msParts;
	}

	/**
	 * Parse city & state in a string into a HashMap. Attempts based on multiple
	 * possible formats (e.g. "Warrenville IL" or "Warrenville, IL").
	 * 
	 * @param _sAddressLine String to parse
	 * @return HashMap<String, String> HashMap contains 2 entries: CITY & STATE
	 */
	private static HashMap<String, String> getSplitCityState(String _sAddressLine) {
		HashMap<String, String> msParts = new HashMap<String, String>();

		msParts.put("CITY", "");
		msParts.put("STATE", "");

		if (_sAddressLine == null) {
			return msParts;
		}

		String[] asParts = _sAddressLine.split(" ");
		int nNumParts = asParts.length;

		if (nNumParts <= 0) {
			return msParts;
		}

		nNumParts--;
		String sZN4 = asParts[nNumParts];
		String sState = "";

		if (sZN4.charAt(0) >= 'A' && sZN4.charAt(1) >= 'A') {
			sState = sZN4.substring(0, 2);
			sZN4 = sZN4.substring(2);
		} else if (sZN4.charAt(0) >= 'A' && sZN4.charAt(1) <= '9' && sZN4.charAt(2) <= '9') {
			sState = sZN4.substring(0, 1);
			sZN4 = sZN4.substring(1);
		}

		if (sState.length() == 0) {
			nNumParts--;
			if (nNumParts == -1) {
				msParts.put("CITY", "");
				msParts.put("STATE", "");
				return msParts;
			}

			sState = asParts[nNumParts];
		}

		String sLast = "";
		if (sState.lastIndexOf(',') == -1) {
			msParts.put("STATE", sState.replaceAll("_", " "));
		} else {
			String[] asNew = sState.split(",");
			msParts.put("STATE", asNew[1].replaceAll("_", " "));
			sLast = asNew[0];
		}

		StringBuilder sbCity = new StringBuilder();
		for (int i = 0; i < nNumParts; i++) {
			sbCity.append(asParts[i]);
			if (i < nNumParts - 1) {
				sbCity.append(" ");
			}
		}
		if (sLast != "") {
			sbCity.append(" " + sLast);
		}

		String sCity = sbCity.toString().trim();
		if (sCity.length() < 1) {
			sCity = sState;
		}
		if (sCity.lastIndexOf(',') == sCity.length() - 1) {
			sCity = sCity.substring(0, sCity.length() - 1);
		}
		msParts.put("CITY", sCity.replaceAll("_", " "));

		return msParts;
	}

	/**
	 * This method returns a HashMap of FIRSTNAME and LASTNAME from a given space
	 * split String (e.g. "Stephan Cossette")
	 * 
	 * @param _sFullName The full name to split
	 * @return A HashMap of FIRSTNAME and LASTNAME
	 */
	private static HashMap<String, String> getSplitFirstNameLastNameSpace(String _sFullName, boolean _bUseFirstSpace) {

		String[] asShortNameSubs = { "II", "III", "IV", "J", "JR", "JR.", "SR", "SR." };

		HashMap<String, String> oNameHashMap = new HashMap<String, String>();
		if (_bUseFirstSpace) {
			if (_sFullName == null) {
				oNameHashMap.put("LASTNAME", "");
				oNameHashMap.put("FIRSTNAME", "");
			} else if (_sFullName.lastIndexOf(" ") == -1) {
				oNameHashMap.put("LASTNAME", "");
				oNameHashMap.put("FIRSTNAME", _sFullName);
			} else {
				oNameHashMap.put("FIRSTNAME", _sFullName.substring(0, _sFullName.indexOf(" ")));
				oNameHashMap.put("LASTNAME", _sFullName.substring(_sFullName.indexOf(" ") + 1, _sFullName.length()));

			}
		} else {
			if (_sFullName == null) {
				oNameHashMap.put("FIRSTNAME", "");
				oNameHashMap.put("LASTNAME", "");
			} else if (_sFullName.lastIndexOf(" ") == -1) {
				oNameHashMap.put("FIRSTNAME", "");
				oNameHashMap.put("LASTNAME", _sFullName);
			} else {
				boolean bOverRideLastSpace = false;

				for (int i = 0; i < asShortNameSubs.length; i++) {
					if (_sFullName.toLowerCase().endsWith(asShortNameSubs[i].toLowerCase())) {
						bOverRideLastSpace = true;
					}
				}

				if (bOverRideLastSpace) {
					oNameHashMap.put("FIRSTNAME", _sFullName.substring(0, _sFullName.lastIndexOf(" ")));
					oNameHashMap.put("LASTNAME",
							_sFullName.substring(_sFullName.indexOf(" ") + 1, _sFullName.length()));
				} else {
					oNameHashMap.put("FIRSTNAME", _sFullName.substring(0, _sFullName.lastIndexOf(" ")));
					oNameHashMap.put("LASTNAME",
							_sFullName.substring(_sFullName.lastIndexOf(" ") + 1, _sFullName.length()));
				}

			}
		}

		oNameHashMap.put("FIRSTNAME", XSaLTStringUtils.regExReplaceStringForInsert(oNameHashMap.get("FIRSTNAME")));
		oNameHashMap.put("LASTNAME", XSaLTStringUtils.regExReplaceStringForInsert(oNameHashMap.get("LASTNAME")));

		return oNameHashMap;
	}

	/**
	 * This method returns a HashMap of FIRSTNAME and LASTNAME from a given space
	 * split String (e.g. "Cossette Stephan")
	 * 
	 * @param _sFullName The full name to split
	 * @return A HashMap of FIRSTNAME and LASTNAME
	 */
	private static HashMap<String, String> getSplitFirstNameLastNameSpaceReverse(String _sFullName) {
		HashMap<String, String> oNameHashMap = new HashMap<String, String>();
		if (_sFullName == null) {
			oNameHashMap.put("FIRSTNAME", "");
			oNameHashMap.put("LASTNAME", "");
		} else if (_sFullName.lastIndexOf(" ") == -1) {
			oNameHashMap.put("FIRSTNAME", "");
			oNameHashMap.put("LASTNAME", _sFullName);
		} else {
			oNameHashMap.put("LASTNAME", _sFullName.substring(0, _sFullName.lastIndexOf(" ")));
			oNameHashMap.put("FIRSTNAME", _sFullName.substring(_sFullName.lastIndexOf(" ") + 1, _sFullName.length()));
		}

		oNameHashMap.put("FIRSTNAME", XSaLTStringUtils.regExReplaceStringForInsert(oNameHashMap.get("FIRSTNAME")));
		oNameHashMap.put("LASTNAME", XSaLTStringUtils.regExReplaceStringForInsert(oNameHashMap.get("LASTNAME")));

		return oNameHashMap;
	}

	/**
	 * This method returns a HashMap of STREETNO and STREETNAME from a given address
	 * 
	 * @param _sInputAddress The address to create an address key from
	 * @return A HashMap of STREETNO and STREETNAME
	 */
	public static HashMap<String, String> getAddressKeyMap(String _sInputAddress) {
		HashMap<String, String> oAddressMap = new HashMap<String, String>();
		if (_sInputAddress.indexOf(" ") != -1) {
			String sStreetNo = XSaLTStringUtils
					.regExStripNonNumbersFromString(_sInputAddress.substring(0, _sInputAddress.indexOf(" ")));
			oAddressMap.put("STREETNO", sStreetNo);
			if (sStreetNo.length() > 0) {
				oAddressMap.put("STREETNAME",
						XSaLTStringUtils
								.regExStripAddressGarbage(
										_sInputAddress.substring(_sInputAddress.indexOf(" "), _sInputAddress.length()))
								.trim());
			} else {
				oAddressMap.put("STREETNAME", XSaLTStringUtils.regExStripAddressGarbage(_sInputAddress).trim());
			}
		} else {
			oAddressMap.put("STREETNO", "");
			oAddressMap.put("STREETNAME", XSaLTStringUtils.regExStripAddressGarbage(_sInputAddress).trim());
		}
		return oAddressMap;
	}

	/**
	 * This method "keyifies" the address provided to it. This removes the unit
	 * designation and directions. Useful when deduping a list of addresses
	 * 
	 * @param _sInputAddress      The address to keyify
	 * @param _bCleanDirectionals Flag to eliminate the directionals (N, S, E, W)
	 * @return The keyified address
	 */
	public static String keyifyAddressString(String _sInputAddress, boolean _bCleanDirectionals) {
		if (_sInputAddress == null) {
			return null;
		} else {
			LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
			oRegExHashMap.put("\\n", " ");
			oRegExHashMap.put("\\r", " ");
			oRegExHashMap.put("[^A-Za-z0-9\\s]", "");
			if (_bCleanDirectionals) {
				oRegExHashMap.put("\\sN\\s", " ");
				oRegExHashMap.put("\\sW\\s", " ");
				oRegExHashMap.put("\\sE\\s", " ");
				oRegExHashMap.put("\\sS\\s", " ");
			}
			LinkedHashMap<String, String> oUnitAbbrev = XSaLTTriviaUtils.getUnitAbbrvMap();
			for (Iterator<String> i = oUnitAbbrev.keySet().iterator(); i.hasNext();) {
				String sAbbrev = (String) i.next();
				oRegExHashMap.put("\\s" + sAbbrev + "\\s", " ");
			}
			oRegExHashMap.put("\\s", "");

			return XSaLTStringUtils.processRegExHashMap(oRegExHashMap, _sInputAddress.toUpperCase());
		}
	}

	/**
	 * This method removes all non-numeric data from the specified column in the
	 * specified table.
	 * 
	 * @param _oConn                 Connection object
	 * @param _sPrimaryKeyColumnName Primary key column name from table
	 * @param _sTableName            Table for action
	 * @param _sDataColumnName       Column for action
	 * @throws SQLException
	 */
	public static void removeNonNumbersFromColumnData(Connection _oConn, String _sPrimaryKeyColumnName,
			String _sTableName, String _sDataColumnName) throws SQLException {
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sDataColumnName + " FROM " + _sTableName);
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			String sOldData = oRs.getString(_sDataColumnName);
			if (sOldData != null && !sOldData.equals("")) {
				String sNewData = XSaLTStringUtils.regExStripNonNumbersFromString(sOldData);
				String sSQL = "UPDATE " + _sTableName + " SET " + _sDataColumnName + " = '" + sNewData.toUpperCase()
						+ "' WHERE " + _sPrimaryKeyColumnName + " = '" + sRowGenId + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			}
		}
	}

	/**
	 * This method removes all non-numeric data from the specified column in the
	 * specified table, leaving decimal points in place.
	 * 
	 * @param _oConn                 Connection object
	 * @param _sPrimaryKeyColumnName Primary key column name from table
	 * @param _sTableName            Table for action
	 * @param _sDataColumnName       Column for action
	 * @throws SQLException
	 */
	public static void removeNonNumbersFromColumnDataKeepDecimal(Connection _oConn, String _sPrimaryKeyColumnName,
			String _sTableName, String _sDataColumnName) throws SQLException {
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sDataColumnName + " FROM " + _sTableName);
		String sRowGenId, sOldData, sNewData;
		while (oRs.next()) {
			sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			sOldData = oRs.getString(_sDataColumnName);
			if (sOldData != null && !sOldData.equals("")) {
				sNewData = XSaLTStringUtils.regExStripNonNumbersTwoFromString(sOldData);
				if (sNewData.trim().length() == 0) {
					sNewData = "NULL";
				} else {
					sNewData = "'" + sNewData.toUpperCase() + "'";
				}

				String sSQL = "UPDATE " + _sTableName + " SET " + _sDataColumnName + " = " + sNewData + " WHERE "
						+ _sPrimaryKeyColumnName + " = '" + sRowGenId + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			}
		}
	}

	/**
	 * This method removes extra spaces from the specified column in the specified
	 * table.
	 * 
	 * @param _oConn                 Connection object
	 * @param _sPrimaryKeyColumnName Primary key column name from table
	 * @param _sTableName            Table for action
	 * @param _sDataColumnName       Column for action
	 * @throws SQLException
	 */
	public static void removeExtraSpacesFromColumnData(Connection _oConn, String _sPrimaryKeyColumnName,
			String _sTableName, String _sDataColumnName) throws SQLException {
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sDataColumnName + " FROM " + _sTableName);
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			String sOldData = oRs.getString(_sDataColumnName);
			if (sOldData != null && !sOldData.equals("")) {
				String sNewData = XSaLTStringUtils.regExRemoveExtraSpaces(sOldData).trim();
				String sSQL = "UPDATE " + _sTableName + " SET " + _sDataColumnName + " = '" + sNewData.toUpperCase()
						+ "' WHERE " + _sPrimaryKeyColumnName + " = '" + sRowGenId + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			}
		}
	}

	/**
	 * This method "keyifies" the plate field in a database. This removes any
	 * non-alphanumberic data, including spaces. Useful when deduping a list of
	 * plates
	 * 
	 * @param _oConn                 The database connection
	 * @param _sPrimaryKeyColumnName The primary key column for the table
	 * @param _sTableName            The table name you wish to keyify the plate in
	 * @param _sPlateColumnName      The name of the column containing plates
	 * @throws SQLException
	 */
	public static void keyifyPlateColumns(Connection _oConn, String _sPrimaryKeyColumnName, String _sTableName,
			String _sPlateColumnName) throws SQLException {
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn,
				"SELECT " + _sPrimaryKeyColumnName + ", " + _sPlateColumnName + " FROM " + _sTableName);
		while (oRs.next()) {
			String sRowGenId = oRs.getString(_sPrimaryKeyColumnName);
			String sOldPlate = oRs.getString(_sPlateColumnName);
			if (sOldPlate != null && !sOldPlate.equals("")) {
				String sNewPlate = XSaLTStringUtils.regExStripPlateGarbage(sOldPlate);
				String sSQL = "UPDATE " + _sTableName + " SET PLATE_KEY = '" + sNewPlate.toUpperCase() + "' WHERE "
						+ _sPrimaryKeyColumnName + " = '" + sRowGenId + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL);
			}
		}
	}

	/**
	 * This method "keyifies" the addresses in the specified table. This removes the
	 * unit designation and directions. Useful when deduping a list of addresses.
	 * 
	 * @param _oConn                  Connection object
	 * @param _sTableName             Table for action
	 * @param _sRowGenIdColumnName    Generated ID column name for specified table
	 * @param _sMake                  Vehicle make
	 * @param _sModel                 Vehicle model
	 * @param _sAddress1FieldName     First address column name
	 * @param _sAddress2FieldName     Second address column name
	 * @param _sNCOAAddress1FieldName First NCOA address column
	 * @param _sNCOAAddress2FieldName Second NCOA address column
	 * @param _bRemoveDirectionals    Flag to eliminate the directionals (N, E, S,
	 *                                W)
	 */
	public static void keyifyAddressColumns(Connection _oConn, String _sTableName, String _sRowGenIdColumnName,
			String _sMake, String _sModel, String _sAddress1FieldName, String _sAddress2FieldName,
			String _sNCOAAddress1FieldName, String _sNCOAAddress2FieldName, boolean _bRemoveDirectionals)
			throws SQLException {
		String sAddress2FieldNameSub = "";
		String sNCOAAddress1FieldNameSub = "";
		String sNCOAAddress2FieldNameSub = "";

		if (_sAddress2FieldName == null) {
			_sAddress2FieldName = "ADD2";
			sAddress2FieldNameSub = "'' AS ADD2";
		} else {
			sAddress2FieldNameSub = _sAddress2FieldName;
		}

		if (_sNCOAAddress1FieldName == null) {
			_sNCOAAddress1FieldName = "NCOAADD1";
			sNCOAAddress1FieldNameSub = "'' AS NCOAADD1";
		} else {
			sNCOAAddress1FieldNameSub = _sNCOAAddress1FieldName;
		}

		if (_sNCOAAddress2FieldName == null) {
			_sNCOAAddress2FieldName = "NCOAADD2";
			sNCOAAddress2FieldNameSub = "'' AS NCOAADD2";
		} else {
			sNCOAAddress2FieldNameSub = _sNCOAAddress2FieldName;
		}

		String sSQL = "";

		if (_sMake == null) {
			sSQL = "SELECT " + _sRowGenIdColumnName + ", " + _sAddress1FieldName + ", " + sAddress2FieldNameSub + ", "
					+ sNCOAAddress1FieldNameSub + ", " + sNCOAAddress2FieldNameSub + " FROM " + _sTableName
					+ " ORDER BY " + _sRowGenIdColumnName;
		} else {
			sSQL = "SELECT " + _sRowGenIdColumnName + ", " + _sMake + ", " + _sModel + ", " + _sAddress1FieldName + ", "
					+ sAddress2FieldNameSub + ", " + sNCOAAddress1FieldNameSub + ", " + sNCOAAddress2FieldNameSub
					+ " FROM " + _sTableName + " ORDER BY " + _sRowGenIdColumnName;
		}

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
		while (oRs.next()) {
			String sCurrentRowGenID = oRs.getString(_sRowGenIdColumnName);
			String sAddress1 = oRs.getString(_sAddress1FieldName);
			String sAddress2 = oRs.getString(_sAddress2FieldName);
			String sNCOAAddress1 = oRs.getString(_sNCOAAddress1FieldName);
			String sNCOAAddress2 = oRs.getString(_sNCOAAddress2FieldName);
			String sKeyifiedAddress = keyifyAddressString(sAddress1 + " " + sAddress2, _bRemoveDirectionals);
			String sKeyifiedNCOAAddress = keyifyAddressString(sNCOAAddress1 + " " + sNCOAAddress2,
					_bRemoveDirectionals);

			String sMake = "";
			String sModel = "";

			if (_sMake != null) {

				sMake = XSaLTStringUtils.regExStripNonAlphaFromString(oRs.getString(_sMake));
				sModel = XSaLTStringUtils.regExStripNonAlphaFromString(oRs.getString(_sModel));

			}

			// XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName +
			// " ADD COLUMN CASS_MAKE_KEY VARCHAR(200)");
			// XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName +
			// " ADD COLUMN NCOA_MAKE_KEY VARCHAR(200)");
			// XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName +
			// " ADD COLUMN CASS_MODEL_KEY VARCHAR(200)");
			// XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName +
			// " ADD COLUMN NCOA_MODEL_KEY VARCHAR(200)");

			if (sKeyifiedAddress.equals("NULL")) {
				sKeyifiedAddress = "";
			}
			if (sKeyifiedNCOAAddress.equals("NULL")) {
				sKeyifiedNCOAAddress = "";
			}

			String sAddMakeKeyCASS = "";
			String sAddModelKeyCASS = "";
			String sAddMakeKeyNCOA = "";
			String sAddModelKeyNCOA = "";

			if (!sKeyifiedAddress.equals("")) {
				if (!sMake.equals("")) {
					sAddMakeKeyCASS = ", CASS_MAKE_KEY = '" + sKeyifiedAddress + "_" + sMake + "'";
				}
			}

			if (!sKeyifiedAddress.equals("")) {
				if (!sModel.equals("")) {
					sAddModelKeyCASS = ", CASS_MODEL_KEY = '" + sKeyifiedAddress + "_" + sMake + "'";
				}
			}

			if (!sKeyifiedNCOAAddress.equals("")) {
				if (!sMake.equals("")) {
					sAddMakeKeyNCOA = ", NCOA_MAKE_KEY = '" + sKeyifiedNCOAAddress + "_" + sMake + "'";
				}
			}

			if (!sKeyifiedNCOAAddress.equals("")) {
				if (!sModel.equals("")) {
					sAddModelKeyNCOA = ", NCOA_MODEL_KEY = '" + sKeyifiedNCOAAddress + "_" + sMake + "'";
				}
			}

			if (sKeyifiedAddress.equals(sKeyifiedNCOAAddress)) {
				String sSQL2 = "UPDATE " + _sTableName + " SET CASS_KEY = '" + sKeyifiedAddress + "' " + sAddMakeKeyCASS
						+ " " + sAddModelKeyCASS + " " + sAddMakeKeyNCOA + " " + sAddModelKeyNCOA + " WHERE "
						+ _sRowGenIdColumnName + " = '" + sCurrentRowGenID + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL2);
			} else {
				String sSQL2 = "UPDATE " + _sTableName + " SET CASS_KEY = '" + sKeyifiedAddress + "', NCOA_KEY = '"
						+ sKeyifiedNCOAAddress + "' " + sAddMakeKeyCASS + " " + sAddModelKeyCASS + " " + sAddMakeKeyNCOA
						+ " " + sAddModelKeyNCOA + " WHERE " + _sRowGenIdColumnName + " = '" + sCurrentRowGenID + "'";
				XSaLTDataUtils.executeSQL(_oConn, sSQL2);
			}

		}
	}

	/**
	 * This method "keyifies" the addresses in the specified tables. This is useful
	 * for de-duping addresses.
	 * 
	 * @param _oConn               Database connection object
	 * @param _sTableName          Table for action
	 * @param _sRowGenIdColumnName Generated ID column name for table
	 * @param _sMake               Vehicle make
	 * @param _sModel              Vehicle model
	 * @param _sAddress1FieldName  First address column name
	 * @param _sAddress2FieldName  Second address column name
	 * @param _bRemoveDirectionals Flag to remove directionals (N, E, S, W)
	 * @throws SQLException
	 */
	public static void keyifySingleAddressColumns(Connection _oConn, String _sTableName, String _sRowGenIdColumnName,
			String _sMake, String _sModel, String _sAddress1FieldName, String _sAddress2FieldName,
			boolean _bRemoveDirectionals) throws SQLException {
		String sAddress2FieldNameSub = "";

		if (_sAddress2FieldName == null) {
			_sAddress2FieldName = "ADD2";
			sAddress2FieldNameSub = "'' AS ADD2";
		} else {
			sAddress2FieldNameSub = _sAddress2FieldName;
		}

		String sSQL = "";

		if (_sMake == null) {
			sSQL = "SELECT " + _sRowGenIdColumnName + ", " + _sAddress1FieldName + ", " + sAddress2FieldNameSub
					+ " FROM " + _sTableName + " ORDER BY " + _sRowGenIdColumnName;
		} else {
			sSQL = "SELECT " + _sRowGenIdColumnName + ", " + _sMake + ", " + _sModel + ", " + _sAddress1FieldName + ", "
					+ sAddress2FieldNameSub + " FROM " + _sTableName + " ORDER BY " + _sRowGenIdColumnName;
		}

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
		while (oRs.next()) {
			String sCurrentRowGenID = oRs.getString(_sRowGenIdColumnName);
			String sAddress1 = oRs.getString(_sAddress1FieldName);
			String sAddress2 = oRs.getString(_sAddress2FieldName);
			String sKeyifiedAddress = keyifyAddressString(sAddress1 + " " + sAddress2, _bRemoveDirectionals);

			String sMake = "";
			String sModel = "";

			if (_sMake != null) {

				sMake = XSaLTStringUtils.regExStripNonAlphaFromString(oRs.getString(_sMake));
				sModel = XSaLTStringUtils.regExStripNonAlphaFromString(oRs.getString(_sModel));

			}

			if (sKeyifiedAddress.equals("NULL")) {
				sKeyifiedAddress = "";
			}

			String sAddMakeKeyCASS = "";
			String sAddModelKeyCASS = "";

			if (!sKeyifiedAddress.equals("")) {
				if (sMake != null && !sMake.equals("")) {
					sAddMakeKeyCASS = ", MAIL_MAKE_KEY = '" + sKeyifiedAddress + "_" + sMake + "'";
				}
			}

			if (!sKeyifiedAddress.equals("")) {
				if (sModel != null && !sModel.equals("")) {
					sAddModelKeyCASS = ", MAIL_MODEL_KEY = '" + sKeyifiedAddress + "_" + sMake + "'";
				}
			}

			String sSQL2 = "UPDATE " + _sTableName + " SET MAIL_KEY = '" + sKeyifiedAddress + "' " + sAddMakeKeyCASS
					+ " " + sAddModelKeyCASS + " WHERE " + _sRowGenIdColumnName + " = '" + sCurrentRowGenID + "'";
			XSaLTDataUtils.executeSQL(_oConn, sSQL2);

		}
	}

	/**
	 * Create indices for address and plate keys in given table.
	 * 
	 * @param _oConn      Database connection object
	 * @param _sTableName Table for action
	 * @throws SQLException
	 */
	public static void createPlateAndAddressKeyColumnsAndIndex(Connection _oConn, String _sTableName)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN PLATE_KEY VARCHAR(100)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN CASS_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN NCOA_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN CASS_MAKE_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN NCOA_MAKE_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN CASS_MODEL_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN NCOA_MODEL_KEY VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX PLATE_KEY_INDEX ON " + _sTableName + " (PLATE_KEY(100))");
		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX CASS_ADDRESS_INDEX ON " + _sTableName + " (CASS_KEY(200))");
		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX NCOA_ADDRESS_INDEX ON " + _sTableName + " (NCOA_KEY(200))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX CASS_MAKE_ADDRESS_INDEX ON " + _sTableName + " (CASS_MAKE_KEY(200))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX NCOA_MAKE_ADDRESS_INDEX ON " + _sTableName + " (NCOA_MAKE_KEY(200))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX CASS_MODEL_ADDRESS_INDEX ON " + _sTableName + " (CASS_MODEL_KEY(200))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX NCOA_MODEL_ADDRESS_INDEX ON " + _sTableName + " (NCOA_MODEL_KEY(200))");

		try {
			XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX FROMWHERE_INDEX ON " + _sTableName + " (FROMWHERE(50))");
		} catch (Exception e) {
		}

		try {
			XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX PURCHASE_INDEX ON " + _sTableName + " (STATUS(50))");
		} catch (Exception e) {
		}

		try {
			XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX IN_OR_OUT_INDEX ON " + _sTableName + " (IN_OR_OUT(50))");
		} catch (Exception e) {
		}

	}

	/**
	 * This method adds columns and creates indices for plates and mailing
	 * information (address, make, and model).
	 * 
	 * @param _oConn      Database connection object
	 * @param _sTableName Table for action
	 * @throws SQLException
	 */
	public static void createPlateAndMailingAddressKeyColumnsAndIndex(Connection _oConn, String _sTableName)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN PLATE_KEY VARCHAR(100)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN MAIL_KEY VARCHAR(400)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN MAIL_MAKE_KEY VARCHAR(400)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN MAIL_MODEL_KEY VARCHAR(400)");
		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX PLATE_KEY_INDEX ON " + _sTableName + " (PLATE_KEY(100))");
		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX MAIL_ADDRESS_INDEX ON " + _sTableName + " (MAIL_KEY(400))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX MAIL_MAKE_ADDRESS_INDEX ON " + _sTableName + " (MAIL_MAKE_KEY(400))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX MAIL_MODEL_ADDRESS_INDEX ON " + _sTableName + " (MAIL_MODEL_KEY(400))");

		try {
			XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX FROMWHERE_INDEX ON " + _sTableName + " (FROMWHERE(50))");
		} catch (Exception e) {
		}

		try {
			XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX PURCHASE_INDEX ON " + _sTableName + " (STATUS(50))");
		} catch (Exception e) {
		}

	}

	/**
	 * This method removes indices and columns for plates and address keys.
	 * 
	 * @param _oConn      Database connection object
	 * @param _sTableName Table for action
	 * @throws SQLException
	 */
	public static void dropPlateAndAddressKeyColumnsAndIndex(Connection _oConn, String _sTableName)
			throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP INDEX PLATE_KEY_INDEX ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP INDEX CASS_ADDRESS_INDEX ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP INDEX NCOA_ADDRESS_INDEX ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN PLATE_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN CASS_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN NCOA_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN CASS_MAKE_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN NCOA_MAKE_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN CASS_MODEL_KEY ");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " DROP COLUMN NCOA_MODEL_KEY ");
	}

	/**
	 * This method creates columns and indices for no-key addresses.
	 * 
	 * @param _oConn      Database connection object
	 * @param _sTableName Table for action
	 * @throws SQLException
	 */
	public static void createAddressNoKeyColumnsAndIndex(Connection _oConn, String _sTableName) throws SQLException {
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN ADD1_NO_KEY BIGINT(9)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN ADD1_STREET VARCHAR(200)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN NCOAADD1_NO_KEY BIGINT(9)");
		XSaLTDataUtils.executeSQL(_oConn, "ALTER TABLE " + _sTableName + " ADD COLUMN NCOAADD1_STREET VARCHAR(200)");

		XSaLTDataUtils.executeSQL(_oConn, "CREATE INDEX ADD1_STREET_INDEX ON " + _sTableName + " (ADD1_STREET(200))");
		XSaLTDataUtils.executeSQL(_oConn,
				"CREATE INDEX NCOAADD1_STREET_INDEX ON " + _sTableName + " (NCOAADD1_STREET(200))");
	}

	/**
	 * This method gets row IDs to delete (or that were deleted) and returns an
	 * ArrayList<String> containing the IDs.
	 * 
	 * @param _oConn                  Database connection object
	 * @param _sTableName             Table for action
	 * @param _sRowgenID              Column name for generated ID
	 * @param _sFromWhereClauseDelete "WHERE" clause for addresses to delete
	 * @param _sFromWhereClauseKeep   "WHERE" clause for addresses to keep
	 * @param _bProcessNCOAKey        Flag to process NCOA key
	 * @param _bDeleteToo             Flag to delete processed addresses
	 * @return ArrayList<String> containing IDs of rows to delete or that were
	 *         deleted
	 * @throws SQLException
	 */
	public static ArrayList<String> processOutVehicleAddressFromWhereSameTableSQL(Connection _oConn, String _sTableName,
			String _sRowgenID, String _sFromWhereClauseDelete, String _sFromWhereClauseKeep, boolean _bProcessNCOAKey,
			boolean _bDeleteToo) throws SQLException {

		ArrayList<String> oDeleteRowgenIDs = new ArrayList<String>();

		String sSQL = getProcessOutVehicleAddressFromWhereSameTableSQL(_sTableName, _sRowgenID, _sFromWhereClauseDelete,
				"CASS_KEY", _sFromWhereClauseKeep, "CASS_KEY");
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
		while (oRs.next()) {
			oDeleteRowgenIDs.add(oRs.getString(_sRowgenID));
		}

		if (_bProcessNCOAKey) {
			sSQL = getProcessOutVehicleAddressFromWhereSameTableSQL(_sTableName, _sRowgenID, _sFromWhereClauseDelete,
					"CASS_KEY", _sFromWhereClauseKeep, "NCOA_KEY");
			oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
			while (oRs.next()) {
				oDeleteRowgenIDs.add(oRs.getString(_sRowgenID));
			}
		}

		if (_bDeleteToo) {
			for (int i = 0; i < oDeleteRowgenIDs.size(); i++) {
				XSaLTDataUtils.executeSQL(_oConn,
						"DELETE FROM " + _sTableName + " WHERE " + _sRowgenID + " = '" + oDeleteRowgenIDs.get(i) + "'");
			}
		} else {
			for (int i = 0; i < oDeleteRowgenIDs.size(); i++) {
				XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sTableName + " SET RECTYPE = 'DELETE' WHERE "
						+ _sRowgenID + " = '" + oDeleteRowgenIDs.get(i) + "'");
			}
		}

		return oDeleteRowgenIDs;

	}

	/**
	 * This method returns an ArrayList<String> containing the IDs of the rows to
	 * delete or that were deleted.
	 * 
	 * @param _oConn                  Database connection object
	 * @param _sTableName             Table for action
	 * @param _sRowgenID              Column name for generated ID
	 * @param _sFromWhereClauseDelete Where clause for records to delete
	 * @param _sFromWhereClauseKeep   Where clause for records to keep
	 * @param _bDeleteToo             Flag for performing deletion
	 * @return ArrayList<String> containing IDs for addresses that were/should be
	 *         deleted
	 * @throws SQLException
	 */
	public static ArrayList<String> processOutVehicleAddressFromWhereSameTableSQLSingleAddressCompare(Connection _oConn,
			String _sTableName, String _sRowgenID, String _sFromWhereClauseDelete, String _sFromWhereClauseKeep,
			boolean _bDeleteToo) throws SQLException {

		ArrayList<String> oDeleteRowgenIDs = new ArrayList<String>();

		String sSQL = getProcessOutVehicleAddressFromWhereSameTableSQL(_sTableName, _sRowgenID, _sFromWhereClauseDelete,
				"MAIL_KEY", _sFromWhereClauseKeep, "MAIL_KEY");

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
		while (oRs.next()) {
			oDeleteRowgenIDs.add(oRs.getString(_sRowgenID));
		}

		if (_bDeleteToo) {
			for (int i = 0; i < oDeleteRowgenIDs.size(); i++) {
				XSaLTDataUtils.executeSQL(_oConn,
						"DELETE FROM " + _sTableName + " WHERE " + _sRowgenID + " = '" + oDeleteRowgenIDs.get(i) + "'");
			}
		} else {
			for (int i = 0; i < oDeleteRowgenIDs.size(); i++) {
				XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + _sTableName + " SET RECTYPE = 'FLAG' WHERE " + _sRowgenID
						+ " = '" + oDeleteRowgenIDs.get(i) + "'");
			}
		}

		return oDeleteRowgenIDs;

	}

	/**
	 * This method removes duplicate make records from the given table.
	 * 
	 * @param _oConn                     Database connection object
	 * @param _sRowGenIdColumnName       Column name for generated ID in specified
	 * @param _sLookTableName            Table for action
	 * @param _sLookInWhereMakeClause    Where clause for determining make for
	 *                                   de-duplication
	 * @param _sPurgeFromWhereMakeClause Where clause for determining which items to
	 *                                   purge
	 * @throws SQLException
	 */
	public static void processOutDupeMakeRecords(Connection _oConn, String _sRowGenIdColumnName, String _sLookTableName,
			String _sLookInWhereMakeClause, String _sPurgeFromWhereMakeClause) throws SQLException {

		HashMap<String, String> oPlateMap = new HashMap<String, String>();
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT " + _sRowGenIdColumnName + ", CASS_MAKE_KEY FROM "
				+ _sLookTableName + " WHERE " + _sLookInWhereMakeClause + " AND CASS_MAKE_KEY IS NOT NULL");

		while (oRs.next()) {
			String sCurrentPlateKey = oRs.getString("CASS_MAKE_KEY");
			if (!oPlateMap.containsKey(sCurrentPlateKey)) {
				oPlateMap.put(sCurrentPlateKey, sCurrentPlateKey);
			}
		}

		int nPlateKeyCount = 0;
		StringBuffer oSqlBuffer = new StringBuffer("DELETE FROM " + _sLookTableName + " WHERE (");

		for (String sPlateKey : oPlateMap.keySet()) {
			if (sPlateKey.length() > 0) {

				String sSQL = "(CASS_MAKE_KEY = '" + sPlateKey + "') or ";
				oSqlBuffer.append(sSQL);

				nPlateKeyCount = nPlateKeyCount + 1;
				if (nPlateKeyCount % 500 == 0) {
					oSqlBuffer = new StringBuffer(oSqlBuffer.substring(0, oSqlBuffer.length() - 4));
					oSqlBuffer.append(") AND " + _sPurgeFromWhereMakeClause);
					XSaLTDataUtils.executeSQL(_oConn, oSqlBuffer.toString());
					oSqlBuffer = new StringBuffer("DELETE FROM " + _sLookTableName + " WHERE (");
				}
			}

		}

		if (!oSqlBuffer.toString().endsWith(" WHERE (")) {
			oSqlBuffer = new StringBuffer(oSqlBuffer.substring(0, oSqlBuffer.length() - 4));
			oSqlBuffer.append(") AND " + _sPurgeFromWhereMakeClause);
			XSaLTDataUtils.executeSQL(_oConn, oSqlBuffer.toString());
		}

	}

	/**
	 * This method removes duplicate address records from a given table.
	 * 
	 * @param _oConn         Database connection object
	 * @param _sTableName    Table for action
	 * @param _sRowgenID     Generated ID for table specified
	 * @param sCompareColumn Column for comparison
	 * @param _sWhereClause  Where clause to determine duplicate addresses
	 * @throws SQLException
	 */
	public static void deleteOutDuplicateAddressesFromSameTable(Connection _oConn, String _sTableName,
			String _sRowgenID, String sCompareColumn, String _sWhereClause) throws SQLException {
		String sSQL = "SELECT " + sCompareColumn + ", count(*) AS MYCOUNT FROM " + _sTableName + " WHERE ("
				+ _sWhereClause + ") group by " + sCompareColumn + " having count(*) > 1";
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, sSQL);
		while (oRs.next()) {
			String sKey = oRs.getString(sCompareColumn);
			int nKeyCount = oRs.getInt("MYCOUNT");
			String sSQL2 = "DELETE FROM " + _sTableName + " WHERE CASS_KEY = '" + sKey + "' AND (" + _sWhereClause
					+ ") limit " + (nKeyCount - 1);
			XSaLTDataUtils.executeSQL(_oConn, sSQL2);
		}

	}

	/**
	 * This method generates an SQL statement to get vehicle address to process out.
	 * 
	 * @param _sTableName             Table for action
	 * @param _sRowgenID              Generated ID in specified table
	 * @param _sFromWhereClauseDelete Where clause for records to delete
	 * @param _sCompareKeyDelete      Key for delete comparison
	 * @param _sFromWhereClauseKeep   Where clause for records to keep
	 * @param _sCompareKeyKeep        Key for records to keep comparison
	 * @return String containing SQL statement for processing vehicle addresses
	 */
	private static String getProcessOutVehicleAddressFromWhereSameTableSQL(String _sTableName, String _sRowgenID,
			String _sFromWhereClauseDelete, String _sCompareKeyDelete, String _sFromWhereClauseKeep,
			String _sCompareKeyKeep) {

		StringBuffer oSQLBuffer = new StringBuffer("SELECT " + _sRowgenID + " FROM " + _sTableName + " ");
		if (_sFromWhereClauseDelete != null) {
			oSQLBuffer.append("WHERE ((" + _sFromWhereClauseDelete + ") ");
		}
		oSQLBuffer
				.append("AND " + _sCompareKeyDelete + " IN (SELECT " + _sCompareKeyKeep + " FROM " + _sTableName + " ");
		if (_sFromWhereClauseKeep != null) {
			oSQLBuffer.append("WHERE ((" + _sFromWhereClauseKeep + ")) ");
		}
		oSQLBuffer.append("))");
		return oSQLBuffer.toString();

	}

	/**
	 * This method removes duplicate license plate records.
	 * 
	 * @param _oConn                      Database connection object
	 * @param _sLookTableName             Table for action
	 * @param _sRowGenIdColumnName        Column name for generated ID for specified
	 *                                    table
	 * @param _sPurgeFromWherePlateClause Where clause to purge desired records
	 * @param _sLookInWherePlateClause    Where clause for finding records to
	 *                                    de-duplicate
	 * @param _nPlateThreshhold           The threshhold number of characters in the
	 *                                    plate for fuzzy matching
	 * @param _nMaxCharDifference         The fuzzy difference in length of plates
	 * @throws SQLException
	 */
	public static void processOutDupePlateRecords(Connection _oConn, String _sLookTableName,
			String _sRowGenIdColumnName, String _sPurgeFromWherePlateClause, String _sLookInWherePlateClause,
			int _nPlateThreshhold, int _nMaxCharDifference) throws SQLException {

		HashMap<String, String> oPlateMap = new HashMap<String, String>();
		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT " + _sRowGenIdColumnName + ", PLATE_KEY FROM "
				+ _sLookTableName + " WHERE " + _sLookInWherePlateClause
				+ " AND PLATE_KEY IS NOT NULL AND PLATE_KEY != '' AND PLATE_KEY not like 'APPL%' order by PLATE_KEY");

		while (oRs.next()) {
			String sCurrentPlateKey = oRs.getString("PLATE_KEY");
			if (!oPlateMap.containsKey(sCurrentPlateKey)) {
				oPlateMap.put(sCurrentPlateKey, sCurrentPlateKey);
			}
		}

		int nPlateKeyCount = 0;
		StringBuffer oSqlBuffer = new StringBuffer("DELETE FROM " + _sLookTableName + " WHERE (");

		for (Iterator<String> j = oPlateMap.keySet().iterator(); j.hasNext();) {
			String sPlateKey = (String) j.next();

			if (sPlateKey != null && sPlateKey.length() > 0) {

				if (sPlateKey.length() >= _nPlateThreshhold) {
					int nMaxPlateLength = sPlateKey.length() + _nMaxCharDifference;
					String sSQL = "((PLATE_KEY like '" + sPlateKey + "%' or PLATE_KEY like '%" + sPlateKey
							+ "') AND (LENGTH(PLATE_KEY) <= " + nMaxPlateLength + ")) or ";
					oSqlBuffer.append(sSQL);

				} else {
					String sSQL = "(PLATE_KEY like '" + sPlateKey + "') or ";
					oSqlBuffer.append(sSQL);
				}

				nPlateKeyCount = nPlateKeyCount + 1;
				if (nPlateKeyCount % 500 == 0) {
					oSqlBuffer = new StringBuffer(oSqlBuffer.substring(0, oSqlBuffer.length() - 4));
					oSqlBuffer.append(") AND " + _sPurgeFromWherePlateClause);
					XSaLTDataUtils.executeSQL(_oConn, oSqlBuffer.toString());
					oSqlBuffer = new StringBuffer("DELETE FROM " + _sLookTableName + " WHERE (");
				}
			}

		}

		if (!oSqlBuffer.toString().endsWith(" WHERE (")) {
			oSqlBuffer = new StringBuffer(oSqlBuffer.substring(0, oSqlBuffer.length() - 4));
			oSqlBuffer.append(") AND " + _sPurgeFromWherePlateClause);
			XSaLTDataUtils.executeSQL(_oConn, oSqlBuffer.toString());
		}

	}

}
