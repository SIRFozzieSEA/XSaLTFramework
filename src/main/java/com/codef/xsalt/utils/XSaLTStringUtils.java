package com.codef.xsalt.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

import com.codef.xsalt.arch.XSaLTConstants;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTStringUtils {

	// private static final Logger LOGGER = LogManager.getLogger(XSaLTStringUtils.class.getName());

	/**
	 * This method gets the MD5 hash equivalent of the input
	 * 
	 * @param _sInputString The input string to hash
	 * @return String The hash equivalent of the input string
	 */
	public static String getMD5Hash(String _sInputString) throws NoSuchAlgorithmException {
		MessageDigest oMessageDigest = MessageDigest.getInstance("MD5");
		byte[] abInputBytes = _sInputString.getBytes();
		oMessageDigest.reset();
		oMessageDigest.update(abInputBytes);
		byte abOutputBytes[] = oMessageDigest.digest();
		StringBuffer oHexStringBuffer = new StringBuffer();
		for (int i = 0; i < abOutputBytes.length; i++) {
			String hex = Integer.toHexString(0xFF & abOutputBytes[i]);
			if (hex.length() == 1) {
				oHexStringBuffer.append('0');
			}
			oHexStringBuffer.append(hex);
		}
		return oHexStringBuffer.toString();
	}

	/**
	 * This method parses a phone number and returns the standard US formatting for
	 * a phone number (e.g. 7-digit: 867-5309 10-digit: (815) 867-5309).
	 * 
	 * @param _Phone Phone number to format
	 * @return Formatted phone number
	 */
	public static String formatPhoneNumber(String _Phone) {
		String sPhone = _Phone;
		if (_Phone.length() == 10) {
			String sPhoneAreaCode = _Phone.substring(0, 3);
			String sPhoneExchange = _Phone.substring(3, 6);
			String sPhoneSuffix = _Phone.substring(6, 10);
			sPhone = "(" + sPhoneAreaCode + ") " + sPhoneExchange + "-" + sPhoneSuffix;
		} else if (_Phone.length() == 7) {
			String sPhoneExchange = _Phone.substring(0, 3);
			String sPhoneSuffix = _Phone.substring(3, 7);
			sPhone = sPhoneExchange + "-" + sPhoneSuffix;
		} else {
			sPhone = "";
		}

		return sPhone;
	}

	/**
	 * This method creates a random password that consists of lower-case alpha
	 * characters and numeric characters.
	 * 
	 * @param _nPasswordLength Length of password to create
	 * @return Generated password
	 */
	public static String generateRandomPassword(int _nPasswordLength) {
		String sPasswordCharacters = "abcdefghijklmnopqrstuvwxyz0123456789";
		String sPassword = "";

		for (int i = 0; i < _nPasswordLength; i++) {
			sPassword = sPassword
					+ sPasswordCharacters.charAt((int) (Math.floor(Math.random() * sPasswordCharacters.length())));
		}
		return sPassword;

	}

	/**
	 * This method parses a message and forces line breaks at or before the max line
	 * length.
	 * 
	 * @param _sMessage Message to create breaks in
	 * @param _nMaxLine Maximum line length
	 * @return String, with line breaks generated from message
	 */
	public static String forceBreakMessage(String _sMessage, int _nMaxLine) {

		ArrayList<String> oWordsNBreaks = new ArrayList<String>();

		String sThisString = "";
		StringBuffer oCurrentWordBuffer = new StringBuffer();

		for (int i = 1; i <= _sMessage.length(); i++) {
			sThisString = _sMessage.substring(i - 1, i);
			if (sThisString.equals(" ")) {
				oCurrentWordBuffer.append(sThisString);
				oWordsNBreaks.add(oCurrentWordBuffer.toString());
				oCurrentWordBuffer = new StringBuffer();
			} else if (sThisString.equals("\r")) {
				oWordsNBreaks.add(oCurrentWordBuffer.toString());
				oWordsNBreaks.add(sThisString + "\n");
				oCurrentWordBuffer = new StringBuffer();
				i++;
			} else {
				oCurrentWordBuffer.append(sThisString);
			}
		}
		oWordsNBreaks.add(oCurrentWordBuffer.toString());

		int nNumberOfLines = 0;
		int nLineLength = 0;
		oCurrentWordBuffer = new StringBuffer();
		for (int i = 0; i < oWordsNBreaks.size(); i++) {
			String sCurrentWord = oWordsNBreaks.get(i).toString();
			int nPrelength = nLineLength + sCurrentWord.length();

			if (nPrelength > _nMaxLine) {
				oCurrentWordBuffer = new StringBuffer(oCurrentWordBuffer.toString().trim());
				oCurrentWordBuffer.append("\r\n");
				nNumberOfLines = nNumberOfLines + 1;
				oCurrentWordBuffer.append(sCurrentWord);
				nLineLength = sCurrentWord.length();

			} else {
				if (sCurrentWord.equals("\r\n")) {
					oCurrentWordBuffer.append(sCurrentWord);
					nLineLength = 0;
				} else {
					oCurrentWordBuffer.append(sCurrentWord);
					nLineLength = nLineLength + sCurrentWord.length();
				}
			}

		}

		return oCurrentWordBuffer.toString();

	}

	/**
	 * This method returns a formatted date string for MySQL
	 * 
	 * @param _sDate       Date string to re-format.
	 * @param _sDateFormat Format for passed in date.
	 * @return The date in yyyy-mm-dd format
	 */
	public static String formatDateForMySQL(String _sDate, String _sDateFormat) {
		return formatDate(_sDate, _sDateFormat, "yyyy-MM-dd");
	}

	/**
	 * This method returns a formatted date time string for MySQL
	 * 
	 * @param _sDttm       Date & Time to re-format
	 * @param _sDttmFormat Format for passed in date.
	 * @return The date in yyyy-MM-dd HH:mm:ss format
	 */
	public static String formatDatetimeForMySQL(String _sDttm, String _sDttmFormat) {
		return formatDate(_sDttm, _sDttmFormat, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * This method formats a java.util.Date object for a MySQL query.
	 * 
	 * @param _oDttm Date object to format.
	 * @return The date in yyyy-MM-dd HH:mm:ss format.
	 */
	public static String formatDatetimeForMySQL(Date _oDttm) {
		String sRV = "";

		if (_oDttm != null) {
			SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sRV = oDateFormat.format(_oDttm);
		}

		return sRV;
	}

	/**
	 * This method formats a java.util.Date object for a MySQL query.
	 * 
	 * @param _oDate Date to format
	 * @return The date in yyyy-MM-dd format
	 */
	public static String formatDateForMySQL(Date _oDate) {
		String sRV = "";

		if (_oDate != null) {
			SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sRV = oDateFormat.format(_oDate);
		}

		return sRV;
	}

	/**
	 * This method re-formats a date string form the current format to the specified
	 * format.
	 * 
	 * @param _sDate          Date string to re-format
	 * @param _sCurrFormat    Current format string for date
	 * @param _sDesiredFormat Desired format for date
	 * @return Re-formatted string representation of specified date
	 */
	public static String formatDate(String _sDate, String _sCurrFormat, String _sDesiredFormat) {
		String sRv = "";
		try {
			SimpleDateFormat oDateFormat = new SimpleDateFormat(_sCurrFormat);
			SimpleDateFormat oMySQLDateFormat = new SimpleDateFormat(_sDesiredFormat);
			Date oDate = oDateFormat.parse(_sDate);
			sRv = oMySQLDateFormat.format(oDate);
		} catch (Exception e) {
			// LOGGER.error(e.toString(), e);
		}
		return sRv;
	}

	/**
	 * The method returns a formatted string date for MySQL
	 * 
	 * @param _sDate The date you wish to format in xx/xx/xxxx or xx/xx/xx format
	 * @return The date in yyyy-mm-dd format
	 */
	public static String formatDateForMySQLFromSlashedDate(String _sDate) {
		if (_sDate != null && !_sDate.equals("")) {
			String sYear = "";
			String sMonth = "";
			String sDay = "";

			String sSplitCharacter = "";

			if (_sDate.indexOf("/") != -1) {
				sSplitCharacter = "/";
			} else {
				sSplitCharacter = " ";
			}

			String asDate[] = _sDate.split(sSplitCharacter);

			sMonth = asDate[0];
			if (sMonth.length() == 1) {
				sMonth = "0" + sMonth;
			}

			sDay = asDate[1];
			if (sDay.length() == 1) {
				sDay = "0" + sDay;
			}

			sYear = asDate[2];

			Integer oYear = Integer.valueOf(sYear);

			if (sYear.length() == 4) {
				sYear = oYear.toString();
			} else {
				if (oYear.intValue() < 20) {
					sYear = "20";

					if (oYear.intValue() < 10) {
						sYear = sYear + "0" + oYear.toString();
					} else {
						sYear = sYear + oYear.toString();
					}
				} else {
					sYear = "19" + oYear.toString();
				}
			}

			return sYear + "-" + sMonth + "-" + sDay;
		} else {
			return "";
		}
	}

	/**
	 * This method formats a full-human readable date into a standard MySQL
	 * formatted date.
	 * 
	 * @param _sDate Full readable formatted date
	 * @return MySQL formatted date string
	 */
	public static String formatFullReadableDateForMySQL(String _sDate) {
		if (_sDate != null && !_sDate.equals("")) {

			String sYear = "";
			String sMonth = "";
			String sDay = "";

			String sHour = "";
			String sMinute = "";
			String sSecond = "";

			sMonth = XSaLTStringUtils.padLeftWithCharacter(_sDate.substring(0, _sDate.indexOf("/")), '0', 2);
			sDay = XSaLTStringUtils
					.padLeftWithCharacter(_sDate.substring(_sDate.indexOf("/") + 1, _sDate.lastIndexOf("/")), '0', 2);
			sYear = _sDate.substring(_sDate.lastIndexOf("/") + 1, _sDate.lastIndexOf("/") + 5);

			sHour = _sDate.substring(_sDate.indexOf(" ") + 1, _sDate.indexOf(":"));
			int nHour = Integer.valueOf(sHour).intValue();

			sMinute = XSaLTStringUtils
					.padLeftWithCharacter(_sDate.substring(_sDate.indexOf(":") + 1, _sDate.lastIndexOf(":")), '0', 2);
			sSecond = XSaLTStringUtils.padLeftWithCharacter(
					_sDate.substring(_sDate.lastIndexOf(":") + 1, _sDate.lastIndexOf(" ")), '0', 2);

			if (_sDate.endsWith("PM")) {
				if (nHour != 12) {
					nHour = nHour + 12;
				}
			}

			sHour = XSaLTStringUtils.padLeftWithCharacter(Integer.valueOf(nHour).toString(), '0', 2);

			return sYear + "-" + sMonth + "-" + sDay + " " + sHour + ":" + sMinute + ":" + sSecond;

		} else {
			return "";
		}
	}

	/**
	 * The method returns a formatted string date for MySQL
	 * 
	 * @param _sDate The date you wish to format in OXOXXXXX or XXXXXX format
	 *               (4-digit year)
	 * @return The date in yyyy-mm-dd format
	 */
	public static String formatDateForMySQLFromNonFormattedDateFourDigitYear(String _sDate) {

		if (_sDate != null && !_sDate.equals("") && !_sDate.equals("0")) {
			String sYear = "";
			String sNotYear = "";

			String sMonth = "";
			String sDay = "";

			sYear = _sDate.substring(_sDate.length() - 4, _sDate.length());
			sNotYear = _sDate.substring(0, _sDate.length() - 4);

			if (sNotYear.length() == 2) {
				sMonth = sNotYear.substring(0, 1);
				sDay = sNotYear.substring(1, 2);
			} else if (sNotYear.length() == 3) {
				if (sNotYear.startsWith("0")) {
					sMonth = sNotYear.substring(0, 2);
					sDay = sNotYear.substring(2, 3);
				} else {
					sMonth = sNotYear.substring(0, 1);
					sDay = sNotYear.substring(1, 3);
				}
			} else if (sNotYear.length() == 4) {
				sMonth = sNotYear.substring(0, 2);
				sDay = sNotYear.substring(2, 4);
			}

			if (sMonth.length() == 1) {
				sMonth = "0" + sMonth;
			}
			if (sDay.length() == 1) {
				sDay = "0" + sDay;
			}

			return sYear + "-" + sMonth + "-" + sDay;
		} else {
			return "";
		}

	}

	/**
	 * The method returns a formatted string date for MySQL
	 * 
	 * @param _sDate The date you wish to format in OXOXXX or XXXXXX format (2-digit
	 *               year)
	 * @return The date in yyyy-mm-dd format
	 */
	public static String formatDateForMySQLFromNonFormattedDateTwoDigitYear(String _sDate) {

		if (_sDate != null && !_sDate.equals("")) {
			String sYear = "";
			String sNotYear = "";

			String sMonth = "";
			String sDay = "";

			sYear = _sDate.substring(_sDate.length() - 2, _sDate.length());
			sNotYear = _sDate.substring(0, _sDate.length() - 2);

			if (sNotYear.length() == 2) {
				sMonth = sNotYear.substring(0, 1);
				sDay = sNotYear.substring(1, 2);
			} else if (sNotYear.length() == 3) {
				if (sNotYear.startsWith("0")) {
					sMonth = sNotYear.substring(0, 2);
					sDay = sNotYear.substring(2, 3);
				} else {
					sMonth = sNotYear.substring(0, 1);
					sDay = sNotYear.substring(1, 3);
				}
			} else if (sNotYear.length() == 4) {
				sMonth = sNotYear.substring(0, 2);
				sDay = sNotYear.substring(2, 4);
			}

			if (sMonth.length() == 1) {
				sMonth = "0" + sMonth;
			}
			if (sDay.length() == 1) {
				sDay = "0" + sDay;
			}

			Integer oYear = Integer.valueOf(sYear);

			if (oYear.intValue() < 20) {
				sYear = "20";

				if (oYear.intValue() < 10) {
					sYear = sYear + "0" + oYear.toString();
				} else {
					sYear = sYear + oYear.toString();
				}
			} else {
				sYear = "19" + oYear.toString();
			}

			return sYear + "-" + sMonth + "-" + sDay;
		} else {
			return "";
		}

	}

	/**
	 * The method returns a formatted string date for MySQL
	 * 
	 * @param _sDate The date you wish to format in xx/xx/xxxx format -- uses
	 *               slashes to fix
	 * @return The date in yyyy-mm-dd format
	 */

	public static String formatDateForMySQLBySlashesPlusStaticTime(String _sDate, String _sStaticTime) {
		StringBuffer oRv = new StringBuffer();

		if (_sDate != null && !_sDate.equals("")) {
			oRv.append(formatDateForMySQL(_sDate, "MM/dd/yyyy"));

			if (_sStaticTime != null && _sStaticTime.trim().length() > 0) {
				oRv.append(" " + _sStaticTime);
			}
		}

		return oRv.toString();
	}

	/**
	 * The method returns a formatted string date for MySQL
	 * 
	 * @param _sDate The date you wish to format in xx/xx/xxxx format -- uses
	 *               slashes to fix
	 * @return The date in yyyy-mm-dd format
	 */

	public static String formatDateForMySQLBySlashes(String _sDate) {
		String sRv = "";
		if (_sDate != null && !_sDate.equals("")) {
			sRv = formatDateForMySQL(_sDate, "MM/dd/yyyy");
		}
		return sRv;
	}

	/**
	 * The method returns a formatted string timestamp for MySQL
	 * 
	 * @param _sDate The date you wish to format in xx/xx/xxxx xx:xx:xx format
	 * @return The date in yyyy-mm-dd hh:mm:ss format
	 */
	public static String formatTimestampForMySQL(String _sDate) {
		if (_sDate != null && !_sDate.equals("")) {
			String sYear = "";
			String sMonth = "";
			String sDay = "";
			String sHour = "";
			String sMinutes = "";
			String sSeconds = "";
			sMonth = _sDate.substring(0, 2);
			sDay = _sDate.substring(3, 5);
			sYear = _sDate.substring(6, 10);
			sHour = _sDate.substring(11, 13);
			sMinutes = _sDate.substring(14, 16);
			sSeconds = _sDate.substring(17, 19);
			return sYear + "-" + sMonth + "-" + sDay + " " + sHour + ":" + sMinutes + ":" + sSeconds;
		} else {
			return "";
		}
	}

	/**
	 * The method returns a formatted string timestamp from MySQL
	 * 
	 * @param _sDate The date you wish to format in xxxx-xx-xx xx:xx:xx format
	 * @return The date in mm/dd/yyyy hh:mm:ss format
	 */
	public static String formatTimeStampFromMySQLToReadableTimeStamp(String _sDate) {
		if (_sDate != null && !_sDate.equals("")) {
			String sYear = "";
			String sMonth = "";
			String sDay = "";
			String sHour = "";
			String sMinutes = "";
			String sSeconds = "";
			if (_sDate.indexOf("-") == -1) {
				sYear = _sDate.substring(0, 4);
				sMonth = _sDate.substring(4, 6);
				sDay = _sDate.substring(6, 8);
				sHour = _sDate.substring(8, 10);
				sMinutes = _sDate.substring(10, 12);
				sSeconds = _sDate.substring(12, 14);
			} else {
				sYear = _sDate.substring(0, 4);
				sMonth = _sDate.substring(5, 7);
				sDay = _sDate.substring(8, 10);
				sHour = _sDate.substring(11, 13);
				sMinutes = _sDate.substring(14, 16);
				sSeconds = _sDate.substring(17, 19);
			}
			return sMonth + "/" + sDay + "/" + sYear + " " + sHour + ":" + sMinutes + ":" + sSeconds;
		} else {
			return "";
		}
	}

	/**
	 * The method returns a formatted string timestamp from MySQL
	 * 
	 * @param _sDate The date you wish to format in xxxx-xx-xx format
	 * @return The date in mm/dd/yyyy format
	 */
	public static String formatDateFromMySQLToReadableDate(String _sDate) {

		if (_sDate != null && !_sDate.equals("")) {
			String sYear = "";
			String sMonth = "";
			String sDay = "";
			if (_sDate.indexOf("-") == -1) {
				sYear = _sDate.substring(0, 4);
				sMonth = _sDate.substring(4, 6);
				sDay = _sDate.substring(6, 8);
			} else {
				sYear = _sDate.substring(0, 4);
				sMonth = _sDate.substring(5, 7);
				sDay = _sDate.substring(8, 10);
			}

			return sMonth + "/" + sDay + "/" + sYear;
		} else {
			return "";
		}
	}

	/**
	 * This method returns a formatted string from a java Date object.
	 * 
	 * @param _oDate The date you wish to format.
	 * @return The date in MM/dd/yyyy format
	 */
	public static String formatDateToReadableDate(Date _oDate) {
		String sRv = "";
		if (_oDate != null) {
			SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy");
			sRv = oFormat.format(_oDate);
		}
		return sRv;
	}

	/**
	 * This method replaces the delimiters in a given string.
	 * 
	 * @param _sOrignalString String for replacing delimiter
	 * @param _sOldDelimiter  Delimiter to replace
	 * @param _sNewDelimiter  Replacement delimiter
	 * @return String with delimiters replaced
	 */
	public static String replaceDelimitersInString(String _sOrignalString, String _sOldDelimiter,
			String _sNewDelimiter) {
		String[] asParts = _sOrignalString.split(_sOldDelimiter);
		StringBuilder sbNewLine = new StringBuilder();

		for (String sPart : asParts) {
			sbNewLine.append(sPart.trim());
			sbNewLine.append(_sNewDelimiter);
		}

		return sbNewLine.toString();
	}

	public static String getCleanSpecialDelimitedString(String _sStringToClean) {
		String sDelimiter = ",";
		String sQuotes = "\"";
		String sReplace = "\t";

		String[] asFileLines = _sStringToClean.split("\n");
		boolean bValidSplit = true;
		ArrayList<ArrayList<String>> oAllPieces = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < asFileLines.length && bValidSplit; i++) {
			String[] asCurrLine = asFileLines[i].split(sDelimiter);
			ArrayList<String> oPieces = new ArrayList<String>();
			for (int x = 0; x < asCurrLine.length && bValidSplit; x++) {
				String sCurrPiece = asCurrLine[x];
				if (sCurrPiece.indexOf(sQuotes) != -1) {
					bValidSplit = sCurrPiece.startsWith(sQuotes) && sCurrPiece.endsWith(sQuotes);
					sCurrPiece = sCurrPiece.substring(1, sCurrPiece.length() - 1);
				}
				oPieces.add(sCurrPiece);
			}
			oAllPieces.add(oPieces);
		}

		StringBuffer oTestBuffer;
		if (bValidSplit) {
			oTestBuffer = new StringBuffer();

			for (int i = 0; i < oAllPieces.size(); i++) {
				ArrayList<String> oCurrPieces = oAllPieces.get(i);
				for (int x = 0; x < oCurrPieces.size(); x++) {
					if (x < (oCurrPieces.size() - 1)) {
						oTestBuffer.append(oCurrPieces.get(x) + sReplace);
					}
				}
				oTestBuffer.append("\n");
			}
		} else {
			oTestBuffer = new StringBuffer(getCleanMickeyMousedString(_sStringToClean));
		}

		return oTestBuffer.toString();
	}

	/**
	 * The method returns an un-Mickey Moused String
	 * 
	 * @param _sStringToClean The String to clean
	 * @return The cleaned up String
	 */
	public static String getCleanMickeyMousedString(String _sStringToClean) {
		String sDelimiter = ",";
		String sQuotes = "\"";
		String sReplace = "\t";
		StringBuffer oTestBuffer = new StringBuffer();

		boolean bInQuotes = false;
		for (int i = 0; i < _sStringToClean.length(); i++) {
			String sTestChar = _sStringToClean.substring(i, i + 1);

			if (sTestChar.equals("\"")) {
				if (bInQuotes) {
					bInQuotes = false;
				} else {
					bInQuotes = true;
				}
			}

			if (bInQuotes) {
				if (sTestChar.equals(sDelimiter)) {
					oTestBuffer.append(sTestChar);
				} else {
					if (!sTestChar.equals(sQuotes)) {
						oTestBuffer.append(sTestChar);
					}
				}
			} else {
				if (sTestChar.equals(sDelimiter)) {
					oTestBuffer.append(sReplace);
				} else {
					if (!sTestChar.equals(sQuotes)) {
						oTestBuffer.append(sTestChar);
					}
				}
			}

		}

		return (oTestBuffer.toString());

	}

	/**
	 * This method capitalizes the first character after a space in each word in the
	 * string.
	 * 
	 * @param _sValue String to capitalize
	 * @return String with first letter capitalized in each word
	 */
	public static String makeInitialCaps(String _sValue) {
		_sValue = _sValue.toLowerCase();
		StringBuffer oTempStringBuffer = new StringBuffer();

		char acStringAsChar[] = _sValue.toCharArray();
		char cLastChar = ' ';

		for (int i = 0; i < acStringAsChar.length; i++) {
			if (cLastChar == ' ') {
				oTempStringBuffer.append(Character.toUpperCase(acStringAsChar[i]));
			} else {
				oTempStringBuffer.append(acStringAsChar[i]);
			}
			cLastChar = acStringAsChar[i];
		}

		return oTempStringBuffer.toString();

	}

	/**
	 * The method returns an ArrayList from a string of comma-delimited values
	 * 
	 * @param _sInput              The string comma-delimited values to convert
	 * @param _bConvertToUpperCase The flag to change the values to uppercase
	 * @return ArrayList of values from a string of comma-delimited values
	 */
	public static ArrayList<String> commaDelimitedStringToArrayList(String _sInput, boolean _bConvertToUpperCase) {
		ArrayList<String> oArrayList = new ArrayList<String>();
		String[] sDelimitedString = _sInput.split(",");
		for (int i = 0; i < sDelimitedString.length; i++) {
			if (_bConvertToUpperCase) {
				oArrayList.add(sDelimitedString[i].toUpperCase());
			} else {
				oArrayList.add(sDelimitedString[i]);
			}
		}
		return oArrayList;
	}

	/**
	 * This method returns an ArrayList from a string split by the provided
	 * delimiter.
	 * 
	 * @param _sInput     The string to split
	 * @param _sDelimiter The delimiter to split on
	 * @return ArrayList of values from the passed string.
	 */
	public static ArrayList<String> splitStringToArrayList(String _sInput, String _sDelimiter) {
		return splitStringToArrayList(_sInput, _sDelimiter, false);
	}

	/**
	 * This method returns an ArrayList from a string split by the provided
	 * delimiter.
	 * 
	 * @param _sInput              The string to split
	 * @param _sDelimiter          The delimiter to split on
	 * @param _bConvertToUpperCase Flag if all values should be converted to upper
	 *                             case
	 * @return ArrayList of values from the passed string.
	 */
	public static ArrayList<String> splitStringToArrayList(String _sInput, String _sDelimiter,
			boolean _bConvertToUpperCase) {
		ArrayList<String> oArrayList = new ArrayList<String>();
		String[] asSplitString = _sInput.split(_sDelimiter);
		for (int i = 0; i < asSplitString.length; i++) {
			if (_bConvertToUpperCase) {
				oArrayList.add(asSplitString[i].toUpperCase());
			} else {
				oArrayList.add(asSplitString[i]);
			}
		}
		return oArrayList;
	}

	/**
	 * The method returns an HashMap from a string of comma-delimited values
	 * 
	 * @param _sInput              The string comma-delimited values to convert
	 * @param _bConvertToUpperCase The flag to change the values to uppercase
	 * @return HashMap of values from a string of comma-delimited values
	 */
	public static HashMap<String, String> commaDelimitedStringToHashMap(String _sInput, boolean _bConvertToUpperCase) {
		HashMap<String, String> oHashMap = new HashMap<String, String>();
		String[] sDelimitedString = _sInput.split(",");
		for (int i = 0; i < sDelimitedString.length; i++) {
			if (_bConvertToUpperCase) {
				oHashMap.put(sDelimitedString[i].toUpperCase(), sDelimitedString[i].toUpperCase());
			} else {
				oHashMap.put(sDelimitedString[i], sDelimitedString[i]);
			}
		}
		return oHashMap;
	}

	/**
	 * This converts a given exception to a String
	 * 
	 * @param e The exception to convert
	 * @return The String value of the exception
	 */
	public static String getStackMessage(Exception e) {
		java.io.StringWriter sw = null;
		java.io.PrintWriter pw = null;
		sw = new java.io.StringWriter();
		pw = new java.io.PrintWriter(sw);
		((Exception) e).printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * The method returns a datetime string in this format - MMddyyyy_HHmmss
	 * 
	 * @return The datetime string in this format - MMddyyyy_HHmmss
	 */
	public static String getDatetimeString() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATETIME_FORMATTER_NO_SLASHES);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method returns a datetime string in this format - MMddyyyyHHmmss
	 * 
	 * @return The datetime string in this format - MMddyyyyHHmmss
	 */
	public static String getDatetimeStringNoUnderscore() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATETIME_FORMATTER_NO_SLASHES_NO_UNDERSCORE);
		return oDateFormatter.format(new Date());
	}

	/**
	 * This method returns the current date & time in this format - MMddyyyyHHmmssSS
	 * 
	 * @return The current date & time in this format - MMddyyyyHHmmssSS
	 */
	public static String getDatetimeStringNoUnderscoreWithMilli() {
		DateFormat oDateFormatter = new SimpleDateFormat(
				XSaLTConstants.XS_DATETIME_FORMATTER_NO_SLASHES_NO_UNDERSCORE_WITH_MILLI);
		return oDateFormatter.format(new Date());
	}

	/**
	 * This method returns the current date & time in this format - MM/dd/yyyy
	 * HH:mm:ss
	 * 
	 * @return The current date & time in this format - MM/dd/yyyy HH:mm:ss
	 */
	public static String getDatetimeStamp() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATETIME_FORMATTER_WITH_SLASHES);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method returns a datetime string in this format - MMddyyyy
	 * 
	 * @return The datetime string in this format - MMddyyyy
	 */
	public static String getDateString() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_NO_SLASHES);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method returns a datetime string in this format - MMddyy
	 * 
	 * @return The datetime string in this format - MMddyy
	 */
	public static String getDateStringShort() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_NO_SLASHES_SHORT);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method returns a datetime string in this format - yyyyMMdd
	 * 
	 * @return The datetime string in this format - yyyyMMdd
	 */
	public static String getDateStringYMD() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_NO_SLASHES_YMD);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method returns a datetime string in this format - MM/dd/yyyy
	 * 
	 * @return The datetime string in this format - MM/dd/yyyy
	 */
	public static String getDatetimeStringWithSlashes() {
		DateFormat oDateFormatter = new SimpleDateFormat(XSaLTConstants.XS_DATE_FORMATTER_WITH_SLASHES);
		return oDateFormatter.format(new Date());
	}

	/**
	 * The method will strip out the garbage that would mess up a standard SQL
	 * insert
	 * 
	 * @param _sRegExProcessString The String to strip out garbage in a given String
	 * @return The cleaned up String
	 */
	public static String regExReplaceStringForInsert(String _sRegExProcessString) {

		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\\\", "\\/");
		oRegExHashMap.put("\\n", " ");
		oRegExHashMap.put("\\r", " ");
		oRegExHashMap.put("\'", "`");
		oRegExHashMap.put("\"", "``");
		oRegExHashMap.put("\\s+", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method cleans up a string for HTML output.
	 * 
	 * @param _sRegExProcessString String to process
	 * @return Clean HTML string
	 */
	public static String regExReplaceStringForCleanHTML(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("&nbsp;", " ");
		oRegExHashMap.put("\\s+", " ");
		oRegExHashMap.put(">\\s+<", "><");
		oRegExHashMap.put("\\s+<", "<");
		oRegExHashMap.put(">\\s+", ">");
		oRegExHashMap.put("\\sbgcolor=\"#DDDDDD\"", "");
		oRegExHashMap.put("<tr><td\\s+height=\"4\"><\\/td><td><\\/td><td><\\/td><td><\\/td><\\/tr>", "<!-- ITEM -->");

		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);

	}

	/**
	 * This method cleans up a string for the Sebis Packager.
	 * 
	 * @param _sRegExProcessString String to process
	 * @return Clean string for Sebis packager
	 */
	public static String regExReplaceStringForSebisPackager(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\r\\n", "XXXXXXXXXX");
		oRegExHashMap.put("\\s+", " ");
		oRegExHashMap.put("XXXXXXXXXX\\s+", "XXXXXXXXXX");
		oRegExHashMap.put("\\s+XXXXXXXXXX", "XXXXXXXXXX");
		oRegExHashMap.put("XXXXXXXXXX", "\\\r\\\n");
		oRegExHashMap.put(
				"[^\\r\\n\"'ABCDEFGHIJKLMNOPQRSTUVWXYZ:abcdefghijklmnopqrstuvwxyz1234567890\\+\\-\\=\\s~`!@#$%^&*()_{}<>,.?|/\\[\\]\\\\]",
				"@@@@@");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace quotes with minute marks in a given String
	 * 
	 * @param _sRegExProcessString The String to replace quotes with minute marks
	 * @return The cleaned up String
	 */
	public static String regExReplaceQuotes(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\\"", "``");
		oRegExHashMap.put("\\\'", "`");

		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace hyphens with spaces in a given String
	 * 
	 * @param _sRegExProcessString The String to replace hyphens with spaces
	 * @return The cleaned up String
	 */
	public static String regExReplaceHyphensWithSpace(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\-", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace directionals from a given String
	 * 
	 * @param _sRegExProcessString The String to replace directionals
	 * @return The cleaned up String
	 */
	public static String regExRemoveDirectional(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\sN\\s", " ");
		oRegExHashMap.put("\\sS\\s", " ");
		oRegExHashMap.put("\\sE\\s", " ");
		oRegExHashMap.put("\\sW\\s", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace extra spaces from a given String
	 * 
	 * @param _sRegExProcessString The String to replace extra spaces
	 * @return The cleaned up String
	 */
	public static String regExRemoveSpaces(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\s+", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method replaces multiple, consecutive spaces with a single space.
	 * 
	 * @param _sRegExProcessString The string to process
	 * @return The cleaned up string
	 */
	public static String regExRemoveExtraSpaces(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\s+", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method replaces multiple, consecutive spaces with a single space and
	 * removes any spaces before or after a line break.
	 * 
	 * @param _sRegExProcessString The string to process
	 * @return The cleaned up string
	 */
	public static String regExRemoveExtraSpacesAndSpaceBeforeBreak(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\n", "XXXXXXXXXX");
		oRegExHashMap.put("\\s+", " ");
		oRegExHashMap.put("\\s+XXXXXXXXXX", "XXXXXXXXXX");
		oRegExHashMap.put("XXXXXXXXXX\\s+", "XXXXXXXXXX");
		oRegExHashMap.put("XXXXXXXXXX", "\n");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString.trim());
	}

	/**
	 * The method will replace backslashes with forwardslashes from a given String
	 * 
	 * @param _sRegExProcessString The String to replace backslashes with
	 *                             forwardslashes
	 * @return The cleaned up String
	 */
	public static String regExReplaceBackslashesWithForwardSlashes(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\\\", "\\/");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method removes backslashes from a string
	 * 
	 * @param _sRegExProcessString
	 * @return
	 */
	public static String regExRemoveBackslashes(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\\\", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace forwardslashes with periods from a given String
	 * 
	 * @param _sRegExProcessString The String to replace forwardslashes with periods
	 * @return The cleaned up String
	 */
	public static String regExReplaceForwardSlashesWithPeriods(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\/", "\\.");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace spaces with underscores from a given String
	 * 
	 * @param _sRegExProcessString The String to replace spaces with underscores
	 * @return The cleaned up String
	 */
	public static String regExReplaceSpacesWithUnderscores(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\s", "\\_");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip numbers from a given String
	 * 
	 * @param _sRegExProcessString The String to strip numbers
	 * @return The cleaned up String
	 */
	public static String regExStripNumbersFromString(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\d", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip non-numbers from a given String
	 * 
	 * @param _sRegExProcessString The String to strip non-numbers
	 * @return The cleaned up String
	 */
	public static String regExStripNonNumbersFromString(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\D", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method will remove non-alphabetic characters from the given String.
	 * 
	 * @param _sRegExProcessString The string to process
	 * @return The cleaned up String
	 */
	public static String regExStripNonAlphaFromString(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\W", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method removes all non-numeric characters from a string, leaving any
	 * periods and dashes (for decimals and negative numbers).
	 * 
	 * @param _sRegExProcessString
	 * @return
	 */
	public static String regExStripNonNumbersTwoFromString(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("[^-.0123456789]", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will replace standard comma-delimited with tabs from a given
	 * String
	 * 
	 * @param _sRegExProcessString The String to replace standard comma-delimited
	 *                             with tabs
	 * @return The cleaned up String
	 */
	public static String regExReplaceStdCommaDelimWithTabs(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\"\\s*,\\s*\"", "\\\t");
		oRegExHashMap.put("\"\"", "\n");
		StringBuffer oTempTabBuffer = new StringBuffer(processRegExHashMap(oRegExHashMap, _sRegExProcessString));
		return oTempTabBuffer.toString().substring(0, (oTempTabBuffer.toString().length() - 1));
	}

	/**
	 * The method will clean up a given string to be database column name safe (less
	 * reserved words)
	 * 
	 * @param _sRegExProcessString The String to clean up a given string to be
	 *                             database column name safe (less reserved words)
	 * @return The cleaned up String
	 */
	public static String regExMakeDataColumnName(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\s", "_");
		oRegExHashMap.put("(\\W)", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * This method will replace "->" with a tab and the space after "router" with a
	 * tab.
	 * 
	 * @param _sRegExProcessString The string to process
	 * @return The cleaned up string
	 */
	public static String regExFixLogFileTabs(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("->", "\\\t");
		oRegExHashMap.put("router\\s", "router\\\t");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip garbage from a given String (PLATE)
	 * 
	 * @param _sRegExProcessString The String to strip garbage
	 * @return The cleaned up String
	 */
	public static String regExStripPlateGarbage(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("[^A-Za-z0-9]", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip garbage from a given String (ADDRESS)
	 * 
	 * @param _sRegExProcessString The String to strip garbage
	 * @return The cleaned up String
	 */
	public static String regExStripAddressGarbage(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("[^A-Za-z0-9\\s]", " ");
		oRegExHashMap.put("\\s+", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip garbage from a given String (ADDRESS)
	 * 
	 * @param _sRegExProcessString The String to strip garbage
	 * @return The cleaned up String
	 */
	public static String regExStripAddressGarbageHyphensCommasOkay(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("[^A-Za-z0-9\\s\\,\\-]", " ");
		oRegExHashMap.put("\\s+", " ");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method will strip trailing slashes from a given String
	 * 
	 * @param _sRegExProcessString The String to strip trailing slashes
	 * @return The cleaned up String
	 */
	public static String regExStripTrailingSlashes(String _sRegExProcessString) {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		oRegExHashMap.put("\\/$", "");
		return processRegExHashMap(oRegExHashMap, _sRegExProcessString);
	}

	/**
	 * The method to left pad a given string with a given character
	 * 
	 * @param _sStringToPad The String to pad
	 * @param _cPadChar     The character to pad the String with
	 * @param _nNumberOfPad The number of places to pad on the left
	 * @return The String padded with the characters
	 */
	public static String padLeftWithCharacter(String _sStringToPad, char _cPadChar, int _nNumberOfPad) {
		String sTempString = getEmptyStringIfNull(_sStringToPad);
		if (sTempString.length() > _nNumberOfPad) {
			return sTempString;
		} else {
			for (int i = _sStringToPad.length(); i < _nNumberOfPad; i++) {
				sTempString = _cPadChar + sTempString;
			}
		}
		return sTempString;
	}

	/**
	 * The method to right pad a given string with a given character
	 * 
	 * @param _sStringToPad The String to pad
	 * @param _cPadChar     The character to pad the String with
	 * @param _nNumberOfPad The number of places to pad on the right
	 * @return The String padded with the characters
	 */
	public static String padRightWithCharacter(String _sStringToPad, char _cPadChar, int _nNumberOfPad) {
		String sTempString = getEmptyStringIfNull(_sStringToPad);
		if (sTempString.length() > _nNumberOfPad) {
			return sTempString;
		} else {
			for (int i = _sStringToPad.length(); i < _nNumberOfPad; i++) {
				sTempString = sTempString + _cPadChar;
			}
		}
		return sTempString;
	}

	/**
	 * Return a string of a fixed size.
	 * 
	 * @param _sIncoming String to change
	 * @param _cPadChar  Pad character if shorter
	 * @param _sSize     Total Size of new string.
	 * @return
	 */
	public static String createFixedLengthString(String _sIncoming, char _cPadChar, int _sSize) {
		if (_sIncoming.length() > _sSize) {
			return _sIncoming.substring(0, _sSize);
		} else {
			return padRightWithCharacter(_sIncoming, _cPadChar, _sSize);
		}
	}

	/**
	 * The method is the harness for regex in this class
	 * 
	 * @param _oRegExHashMap       The regex LinkedHashMap from this class
	 * @param _sRegExProcessString The String to process against
	 * @return The final processed String
	 */
	public static String processRegExHashMap(LinkedHashMap<String, String> _oRegExHashMap,
			String _sRegExProcessString) {
		if (_sRegExProcessString != null) {

			StringBuffer oTempStringBuffer = new StringBuffer(_sRegExProcessString);
			for (Iterator<String> i = _oRegExHashMap.keySet().iterator(); i.hasNext();) {
				String sRegExFind = (String) i.next();
				String sRegExReplace = (String) _oRegExHashMap.get(sRegExFind);
				oTempStringBuffer = new StringBuffer(
						oTempStringBuffer.toString().replaceAll(sRegExFind, sRegExReplace));
			}
			return oTempStringBuffer.toString();
		} else {
			return _sRegExProcessString;
		}
	}

	/**
	 * The method to process a given String with some generic regular expressions
	 * 
	 * @param _sRegExProcessString The String to process
	 * @param _sRegExFind          The regex to find
	 * @param _sRegExReplace       The regex to replace
	 * @return The final processed String
	 */
	public static String processRegEx(String _sRegExProcessString, String _sRegExFind, String _sRegExReplace) {
		return _sRegExProcessString.replaceAll(_sRegExFind, _sRegExReplace).trim();
	}

	/**
	 * The method will return an empty string ("") if a given String is null
	 * 
	 * @param _sTempString The String to check
	 * @return The return value of the processed String
	 */
	public static String getEmptyStringIfNull(String _sTempString) {
		if (_sTempString == null) {
			return "";
		} else if (_sTempString.equalsIgnoreCase("null")) {
			return "";
		} else {
			return _sTempString;
		}
	}

	/**
	 * The method will return an default string (specified) if a given String is
	 * null
	 * 
	 * @param _sTempString   The String to check
	 * @param _sDefaultValue The default to return if the value is null
	 * @return The return value of the processed String
	 */
	public static String getDefaultStringIfNullOrEmpty(String _sTempString, String _sDefaultValue) {
		if (_sTempString == null) {
			return _sDefaultValue;
		} else if (_sTempString.equalsIgnoreCase("null")) {
			return _sDefaultValue;
		} else if (_sTempString.equals("")) {
			return _sDefaultValue;
		} else {
			return _sTempString;
		}
	}

	/**
	 * The method will return an default string (specified) if a given value in a
	 * LinkedHashMap is null
	 * 
	 * @param _oTestMap      The LinkedHashMap to test
	 * @param _sKeyString    The key to test within the LinkedHashMap
	 * @param _sDefaultValue The default value to use if not found, or if that value
	 *                       is null
	 * @return The string value of the LinkedHashMap key, or the default value
	 */
	public static String getDefaultStringIfLinkedHashMapValueIsNull(LinkedHashMap<String, String> _oTestMap,
			String _sKeyString, String _sDefaultValue) {
		if (_oTestMap.get(_sKeyString) == null) {
			return _sDefaultValue;
		} else {
			String sTestValue = _oTestMap.get(_sKeyString).toString();
			if (sTestValue == null) {
				return _sDefaultValue;
			} else {
				return sTestValue;
			}

		}
	}

	/**
	 * The method will return an default string (specified) if a given value in a
	 * HashMap is null
	 * 
	 * @param _oTestMap      The HashMap to test
	 * @param _sKeyString    The key to test within the HashMap
	 * @param _sDefaultValue The default value to use if not found, or if that value
	 *                       is null
	 * @return The string value of the HashMap key, or the default value
	 */
	public static String getDefaultStringIfHashMapValueIsNull(LinkedHashMap<String, String> _oTestMap,
			String _sKeyString, String _sDefaultValue) {
		if (_oTestMap.get(_sKeyString) == null) {
			return _sDefaultValue;
		} else {
			String sTestValue = _oTestMap.get(_sKeyString).toString();
			if (sTestValue == null) {
				return _sDefaultValue;
			} else {
				return sTestValue;
			}
		}
	}

	/**
	 * The method will return a default long (specified) if the String value is null
	 * or empty
	 * 
	 * @param _sTempString  The String to test
	 * @param _nDefaultLong The default long value to return if the String value is
	 *                      null or empty
	 * @return The long value of the String, or the default long value to return if
	 *         the String value is null or empty
	 */
	public static long getDefaultLongIfParameterIsNull(String _sTempString, long _nDefaultLong) {
		if (_sTempString == null || _sTempString.equals("NaN")) {
			return _nDefaultLong;
		} else if (_sTempString.equals("")) {
			return _nDefaultLong;
		} else {
			return Long.valueOf(_sTempString).longValue();
		}
	}

	/**
	 * This method will return a default int (specified) if the String value is null
	 * or empty.
	 * 
	 * @param _sTempString The String to test
	 * @param _nDefaultInt The default value to return if the String to test is
	 *                     invalid.
	 * @return The int value of the string, or the default int value if the String
	 *         is invalid
	 */
	public static int getDefaultIntegerIfParameterIsNull(String _sTempString, int _nDefaultInt) {
		if (_sTempString == null || _sTempString.equals("NaN")) {
			return _nDefaultInt;
		} else if (_sTempString.equals("")) {
			return _nDefaultInt;
		} else {
			return Long.valueOf(_sTempString).intValue();
		}
	}

	/**
	 * This method will return a default double (specified) if the String value is
	 * null or empty.
	 * 
	 * @param _sTempString    The String to test
	 * @param _nDefaultDouble The default value to return if the String to test is
	 *                        invalid.
	 * @return The double value of the string, or the default double value if the
	 *         String is invalid
	 */
	public static double getDefaultDoubleIfParameterIsNull(String _sTempString, double _nDefaultDouble) {
		if (_sTempString == null || _sTempString.equals("NaN")) {
			return _nDefaultDouble;
		} else if (_sTempString.equals("")) {
			return _nDefaultDouble;
		} else {
			return Long.valueOf(_sTempString).doubleValue();
		}
	}

	/**
	 * The method tests to see if a String is null or empty
	 * 
	 * @param _sTempString The String to test
	 * @return Is the String null or empty?
	 */
	public static boolean isStringNullOrEmpty(String _sTempString) {
		if (_sTempString == null) {
			return true;
		} else {
			if (_sTempString.length() == 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * The method tests for wildcard matches
	 * 
	 * @deprecated There probably is a more sophisticated way to do this
	 * @param _sStringPattern The simple String pattern to test for
	 * @param _sStringToCheck The String to test
	 * @return Does this pattern match?
	 */
	public static boolean checkWildCard(String _sStringPattern, String _sStringToCheck) {

		// boolean myRetvalue = StringUtils.checkWildCard("ste*tte", "stephan
		// cossette");
		boolean bMatch = true;

		String sBeginning = "";
		String sEnding = "";

		int nStringLength = _sStringPattern.length();
		int nAsteriskIndex = _sStringPattern.indexOf("*");

		if (nAsteriskIndex >= 0) {
			sEnding = _sStringPattern.substring((nAsteriskIndex + 1), nStringLength);
			if (sEnding.length() > 0) {
				if (_sStringToCheck.toLowerCase().endsWith(sEnding.toLowerCase()) == false) {
					bMatch = false;
				}
			}
		}

		if (nAsteriskIndex < nStringLength) {
			sBeginning = _sStringPattern.substring(0, nAsteriskIndex);
			if (sBeginning.length() > 0) {
				if (_sStringToCheck.toLowerCase().startsWith(sBeginning.toLowerCase()) == false) {
					bMatch = false;
				}
			}
		}

		return bMatch;

	}

	/**
	 * This method tests if the String is null, if it is, a blank String is
	 * returned. If the String is not null, the String returned will have the first
	 * letter of each word capitalized.
	 * 
	 * @param _sIn The string to test and format
	 * @return A blank String if the input is null, otherwise a string with the
	 *         first letter of each word capitalized
	 */
	public static String getEmptyStringIfNullReturnMixedCase(String _sIn) {
		if (_sIn == null) {
			return "";
		} else {
			String[] sSplit = _sIn.split(" ");
			StringBuilder sbLine = new StringBuilder(sSplit.length);
			for (String sWord : sSplit) {
				sbLine.append(sWord.substring(0, 1).toUpperCase());
				sbLine.append(sWord.substring(1).toLowerCase());
				sbLine.append(' ');
			}
			return sbLine.toString().trim();
		}
	}

	/**
	 * This method will format an account number with the separators missing into
	 * the specified account format with the specified separator.
	 * 
	 * @param _sFormatString  The desired format
	 * @param _sAccountNumber Account number to format
	 * @param _cSeparator     Separator in format string
	 * @return Formatted account number
	 */
	public static String formatUnformattedAccountNumber(String _sFormatString, String _sAccountNumber,
			char _cSeparator) {
		StringBuffer oRv;

		if (_sAccountNumber != null && _sAccountNumber.trim().length() > 0) {
			oRv = new StringBuffer(_sAccountNumber);

			char[] acFormat = _sFormatString.toCharArray();

			for (int i = acFormat.length - 1; i >= 0; i--) {
				if (acFormat[i] == _cSeparator) {
					int nOffset = acFormat.length - i - 1;
					oRv.insert(oRv.length() - nOffset, _cSeparator);
				}
			}

		} else {
			oRv = new StringBuffer();
		}

		return oRv.toString();
	}

	/**
	 * This method will format a 4 or 6 digit credit card expiration date into the
	 * expected format for an NMI request.
	 * 
	 * @param _sCCExpDate The date to format in MMyy or MMyyyy format
	 * @return The date formatted in a MM/yy format
	 */
	public static String formatCCExpDateForNMI(String _sCCExpDate) {
		String sRv;

		_sCCExpDate = _sCCExpDate.trim();
		if (_sCCExpDate.matches("^[0-9]{6}$")) {
			sRv = _sCCExpDate.replaceFirst("^([0-9]{2})[0-9]{2}", "$1/");
		} else if (_sCCExpDate.matches("^[0-9]{4}")) {
			sRv = _sCCExpDate.replaceFirst("^([0-9]{2})", "$1/");
		} else {
			sRv = "";
		}

		return sRv;
	}

	/**
	 * This method will parse a datetime in the standard MySQL format into a
	 * java.util.Date object.
	 * 
	 * @param _sDateString The date string to parse.
	 * @return A java.util.Date object set to the datetime in the original string. *
	 */
	public static Date getDateObjFromMySQLDatetimeString(String _sDateString) {
		Date oRv = null;
		try {
			SimpleDateFormat oDateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			oRv = oDateFmt.parse(_sDateString);
		} catch (ParseException e) {
			// LOGGER.error("Unable to parse date string: " + _sDateString, e);
		}

		return oRv;
	}

	/**
	 * This method will parse a date in the standard MySQL format into a
	 * java.util.Date object.
	 * 
	 * @param _sDateString The date string to parse.
	 * @return A java.util.Date object set to the datetime in the original string. *
	 */
	public static Date getDateObjFromMySQLDateString(String _sDateString) {
		Date oRv = null;
		try {
			SimpleDateFormat oDateFmt = new SimpleDateFormat("yyyy-MM-dd");
			oRv = oDateFmt.parse(_sDateString);
		} catch (ParseException e) {
			// LOGGER.error("Unable to parse date string: " + _sDateString, e);
		}

		return oRv;
	}



}
