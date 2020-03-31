package com.codef.xsalt.utils;

import java.io.BufferedReader;
import org.apache.log4j.Priority;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;

import com.codef.xsalt.arch.XSaLTGenericLogger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTNMIGatewayUtils
{

	protected String isServerTransactionHost;
	protected String isServerTransactionPort;
	protected String ioServerTransactionPath;
	protected String ioServerTransactionUserName;
	protected String ioServerTransactionPassword;

	/**
	 * Main constructor.
	 * 
	 * @param _sNMIHostAddress
	 *            Host address
	 * @param _sNMIAccountName
	 *            User name
	 * @param _sNMIPassword
	 *            Password
	 */
	public XSaLTNMIGatewayUtils(String _sNMIHostAddress, String _sNMIAccountName, String _sNMIPassword)
	{

		isServerTransactionHost = _sNMIHostAddress;
		isServerTransactionPort = "443";
		ioServerTransactionPath = "/api/transact.php";
		ioServerTransactionUserName = _sNMIAccountName;
		ioServerTransactionPassword = _sNMIPassword;

	}

	/**
	 * This method processes a credit card payment through NMI for Schiller Park Utilities.
	 *   
	 * @param _sCreditCardNo
	 *            Credit card number to process
	 * @param _sCreditCardExp
	 *            Credity card expiration date
	 * @param _sCCV
	 *            Security code for card
	 * @param _sAddress1
	 *            Billing address (line 1)
	 * @param _sAddress2
	 *            Billing address (line 2)
	 * @param _sZip
	 *            Billing zip code
	 * @param _dChargeAmount
	 *            Amount to bill
	 * @param _dTaxAmount
	 *            Amount of sales tax to bill
	 * @param _oRequest
	 *            HttpServletRequest object
	 * @return HashMap with billing results information
	 *         Successful transactions will have: "transactionid" and "success" as keys.
	 *         Failed transactions will have: "failure error" and "success" as keys.
	 * @throws Exception
	 */
	public HashMap<String, String> doEPayUtilityNMISale(String _sCreditCardNo, String _sCreditCardExp, String _sCCV, String _sAddress1, String _sAddress2, String _sZip,
			double _dChargeAmount, double _dTaxAmount, HttpServletRequest _oRequest) throws Exception
	{
		HashMap<String, String> oResultHashMap = new HashMap<String, String>();
		HashMap<String, String> oRequestHashMap = new HashMap<String, String>();

		DecimalFormat form = new DecimalFormat("#.00");

		oRequestHashMap.put("type", "sale");
		oRequestHashMap.put("ccexp", _sCreditCardExp);
		oRequestHashMap.put("ccnumber", _sCreditCardNo);
		oRequestHashMap.put("amount", form.format(_dChargeAmount));
		oRequestHashMap.put("tax", form.format(_dTaxAmount));
		oRequestHashMap.put("address1", _sAddress1);
		oRequestHashMap.put("address2", _sAddress2);
		oRequestHashMap.put("zip", _sZip);

		if (_sCCV != null)
		{
			oRequestHashMap.put("cvv", _sCCV);
		}

		Enumeration<?> oRequestEnum = _oRequest.getParameterNames();
		while (oRequestEnum.hasMoreElements())
		{
			String sKey = (String) oRequestEnum.nextElement();
			if (sKey.startsWith("ADDL_NMI_PARAMETER"))
			{
				String sParameterName = sKey.split("__")[1].toLowerCase();
				oRequestHashMap.put(sParameterName, XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(sKey)));
			}
		}

		String sDataOut = prepareRequest(oRequestHashMap);
		String sErrorText = "";

		boolean success = true;
		try
		{
			HashMap<String, String> oReturnValueHashMap = postTransaction(sDataOut);
			oResultHashMap.put("transactionid", oReturnValueHashMap.get("transactionid").toString());
			oResultHashMap.put("success", "true");
		}
		catch (IOException e)
		{
			success = false;
			sErrorText = "Connect error, " + e.getMessage();

		}
		catch (Exception e)
		{
			success = false;
			sErrorText = e.getMessage();
		}

		if (!success)
		{
			oResultHashMap.put("success", "false");
			oResultHashMap.put("failure error", sErrorText);
			//throw new Exception(sErrorText);
		}

		XSaLTObjectUtils.printHashMap(oResultHashMap, "\t");

		return oResultHashMap;

	}

	/**
	 * This method processes a credit card payment through NMI.
	 * 
	 * @param _sCreditCardNo
	 *            Credit card to bill
	 * @param _sCreditCardExp
	 *            Card expiration date
	 * @param _sCCV
	 *            Card security code
	 * @param _dChargeAmount
	 *            Amount to bill before sales tax
	 * @param _dTaxAmount
	 *            Sales tax amount
	 * @param _oRequest
	 *            HttpServletRequest object
	 * @return HashMap with billing results information
	 *         Successful transactions will have: "transactionid" and "success" as keys.
	 *         Failed transactions will have: "failure error" and "success" as keys.
	 * @throws Exception
	 */
	public HashMap<String, String> doGenericNMISale(String _sCreditCardNo, String _sCreditCardExp, String _sCCV, double _dChargeAmount, double _dTaxAmount,
			HttpServletRequest _oRequest) throws Exception
	{
		HashMap<String, String> oResultHashMap = new HashMap<String, String>();
		HashMap<String, String> oRequestHashMap = new HashMap<String, String>();

		DecimalFormat form = new DecimalFormat("#.00");

		oRequestHashMap.put("type", "sale");
		oRequestHashMap.put("ccexp", _sCreditCardExp);
		oRequestHashMap.put("ccnumber", _sCreditCardNo);
		oRequestHashMap.put("amount", form.format(_dChargeAmount));
		oRequestHashMap.put("tax", form.format(_dTaxAmount));

		if (_sCCV != null)
		{
			oRequestHashMap.put("cvv", _sCCV);
		}

		Enumeration<?> oRequestEnum = _oRequest.getParameterNames();
		while (oRequestEnum.hasMoreElements())
		{
			String sKey = (String) oRequestEnum.nextElement();
			if (sKey.startsWith("ADDL_NMI_PARAMETER"))
			{
				String sParameterName = sKey.split("__")[1].toLowerCase();
				oRequestHashMap.put(sParameterName, XSaLTStringUtils.getEmptyStringIfNull(_oRequest.getParameter(sKey)));
			}
		}

		String sDataOut = prepareRequest(oRequestHashMap);
		String sErrorText = "";

		boolean success = true;
		try
		{
			HashMap<String, String> oReturnValueHashMap = postTransaction(sDataOut);
			oResultHashMap.put("transactionid", oReturnValueHashMap.get("transactionid").toString());
			oResultHashMap.put("success", "true");
		}
		catch (IOException e)
		{
			success = false;
			sErrorText = "Connect error, " + e.getMessage();

		}
		catch (Exception e)
		{
			success = false;
			sErrorText = e.getMessage();
		}

		if (!success)
		{
			oResultHashMap.put("success", "false");
			oResultHashMap.put("failure error", sErrorText);
			//throw new Exception(sErrorText);
		}

		XSaLTObjectUtils.printHashMap(oResultHashMap, "\t");

		return oResultHashMap;

	}

	/**
	 * This method process a credit card payment through NMI for Western Springs.
	 * 
	 * @param _sCreditCardNo
	 *            Card number to bill
	 * @param _sCreditCardExp
	 *            Card expiration date
	 * @param _sCCV
	 *            Card security code
	 * @param _dChargeAmount
	 *            Amount to bill before sales tax
	 * @param _dTaxAmount
	 *            Sales tax amount to bill
	 * @param _sCity
	 *            Credit card billing city
	 * @param _sZip
	 *            Credit card billing zip code
	 * @param _sDefined1
	 *            Merchant defined field #1
	 * @param _sDefined2
	 *            Merchant defined field #2
	 * @return HashMap with billing results information
	 *         Successful transactions will have: "transactionid" and "success" as keys.
	 *         Failed transactions will have: "failure error" and "success" as keys.
	 * @throws Exception
	 */
	public HashMap<String, String> doWesternSpringsSale(String _sCreditCardNo, String _sCreditCardExp, String _sCCV, double _dChargeAmount, double _dTaxAmount, String _sCity,
			String _sZip, String _sDefined1, String _sDefined2) throws Exception
	{
		String sErrorText = "";
		boolean success = true;

		HashMap<String, String> oResultHashMap = new HashMap<String, String>();
		HashMap<String, String> oRequestHashMap = new HashMap<String, String>();

		if (_dChargeAmount > 0)
		{

			DecimalFormat form = new DecimalFormat("#.00");

			oRequestHashMap.put("amount", form.format(_dChargeAmount));
			oRequestHashMap.put("type", "sale");
			oRequestHashMap.put("ccnumber", _sCreditCardNo);
			oRequestHashMap.put("ccexp", _sCreditCardExp);
			oRequestHashMap.put("ponumber", _sDefined1);
			oRequestHashMap.put("tax", form.format(_dTaxAmount));
			oRequestHashMap.put("city", _sCity);
			oRequestHashMap.put("zip", _sZip);
			oRequestHashMap.put("merchant_defined_field_1", _sDefined1);
			oRequestHashMap.put("merchant_defined_field_2", _sDefined2);

			if (_sCCV != null)
			{
				oRequestHashMap.put("cvv", _sCCV);
			}

			String sDataOut = prepareRequest(oRequestHashMap);

			try
			{
				HashMap<String, String> oReturnValueHashMap = postTransaction(sDataOut);
				oResultHashMap.put("transactionid", oReturnValueHashMap.get("transactionid").toString());
				oResultHashMap.put("success", "true");
			}
			catch (IOException e)
			{
				success = false;
				sErrorText = "Connect error, " + e.getMessage();

			}
			catch (Exception e)
			{
				success = false;
				sErrorText = e.getMessage();
			}

		}
		else
		{
			// zero dollar payment

			oResultHashMap.put("transactionid", "0-" + XSaLTStringUtils.getDatetimeString());
			oResultHashMap.put("success", "true");
			success = true;

		}

		if (!success)
		{
			oResultHashMap.put("success", "false");
			oResultHashMap.put("failure error", sErrorText);
			//throw new Exception(sErrorText);
		}

		return oResultHashMap;
	}

	/**
	 * This method processes a credit card payment through NMI for non-Western Springs
	 * merchants.
	 * 
	 * @param _sCreditCardNo
	 *            Credit card to bill
	 * @param _sCreditCardExp
	 *            Credit card expriation date
	 * @param _sCCV
	 *            Credit card security code
	 * @param _dChargeAmount
	 *            Amount to bill to credit card
	 * @param _sAddress
	 *            Credit card billing address
	 * @param _sZip
	 *            Credit card billing zip
	 * @param _dTaxAmount
	 *            Sales tax amount to bill
	 * @param _sDefined1
	 *            Merchant defined field #1
	 * @param _sDefined2
	 *            Merchant defined field #2
	 * @return HashMap with billing results information
	 *         Successful transactions will have: "transactionid" and "success" as keys.
	 *         Failed transactions will have: "failure error" and "success" as keys.
	 * @throws Exception
	 */
	public HashMap<String, String> doNonWesternSpringsTransaction(String _sCreditCardNo, String _sCreditCardExp, String _sCCV, double _dChargeAmount, String _sAddress,
			String _sZip, double _dTaxAmount, String _sDefined1, String _sDefined2) throws Exception
	{

		String sErrorText = "";
		boolean bSuccess = true;

		HashMap<String, String> oResultHashMap = new HashMap<String, String>();
		HashMap<String, String> oRequestHashMap = new HashMap<String, String>();

		if (_dChargeAmount > 0)
		{

			DecimalFormat form = new DecimalFormat("#.00");

			oRequestHashMap.put("type", "sale");

			oRequestHashMap.put("ccnumber", _sCreditCardNo);
			oRequestHashMap.put("ccexp", _sCreditCardExp);
			oRequestHashMap.put("amount", form.format(_dChargeAmount));
			oRequestHashMap.put("address1", _sAddress);
			oRequestHashMap.put("zip", _sZip);
			oRequestHashMap.put("ponumber", _sDefined1);
			oRequestHashMap.put("orderid", _sDefined1);
			oRequestHashMap.put("tax", form.format(_dTaxAmount));
			oRequestHashMap.put("merchant_defined_field_1", _sDefined1);
			oRequestHashMap.put("merchant_defined_field_2", _sDefined2);

			if (_sCCV != null)
			{
				oRequestHashMap.put("cvv", _sCCV);
			}

			String sDataOut = prepareRequest(oRequestHashMap);

			try
			{
				HashMap<String, String> oReturnValueHashMap = postTransaction(sDataOut);
				oResultHashMap.put("transactionid", oReturnValueHashMap.get("transactionid").toString());
				oResultHashMap.put("success", "true");
			}
			catch (IOException e)
			{
				bSuccess = false;
				sErrorText = "Connect error, " + e.getMessage();

			}
			catch (Exception e)
			{
				bSuccess = false;
				sErrorText = e.getMessage();
			}

		}
		else
		{
			// zero dollar payment

			oResultHashMap.put("transactionid", "0-" + XSaLTStringUtils.getDatetimeString());
			oResultHashMap.put("success", "true");

		}

		if (!bSuccess)
		{
			oResultHashMap.put("success", "false");
			oResultHashMap.put("failure error", sErrorText);
			//throw new Exception(sErrorText);
		}

		return oResultHashMap;
	}

	/**
	 * This method creates a query string from the map of key-value pairs
	 * in the request map.
	 * 
	 * @param _oRequest
	 *            Map of request key and value pairs
	 * @return Query string from request values
	 * @throws UnsupportedEncodingException
	 */
	public String prepareRequest(HashMap<String, String> _oRequest) throws UnsupportedEncodingException
	{

		if (_oRequest.size() == 0)
		{
			return "";
		}

		_oRequest.put("username", ioServerTransactionUserName);
		_oRequest.put("password", ioServerTransactionPassword);

		Set<String> oSet = _oRequest.keySet();
		Iterator<String> oIterator = oSet.iterator();
		Object oKey = oIterator.next();
		StringBuffer oEncodeStringBuffer = new StringBuffer();

		oEncodeStringBuffer.append(oKey).append("=").append(URLEncoder.encode((String) _oRequest.get(oKey), "UTF-8"));

		while (oIterator.hasNext())
		{
			oKey = oIterator.next();
			if (_oRequest.get(oKey) != null)
			{
				oEncodeStringBuffer.append("&").append(oKey).append("=").append(URLEncoder.encode((String) _oRequest.get(oKey), "UTF-8"));
			}
			
		}

		return oEncodeStringBuffer.toString();

	}

	/**
	 * This method contacts the NMI host and posts the requested transaction.
	 * 
	 * @param _sPostRequest
	 *            String representation of post request.
	 * @return HashMap with return key-value pairs.
	 *         
	 * @throws Exception
	 */
	protected HashMap<String, String> postTransaction(String _sPostRequest) throws Exception
	{

		HashMap<String, String> oTransactionResultHashMap = new HashMap<String, String>();
		HttpURLConnection oPostConnection;

		HostnameVerifier hv = new HostnameVerifier()
		{
			public boolean verify(String urlHostName, SSLSession session)
			{
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		URL oPostUrl = new URL("https", isServerTransactionHost, Integer.parseInt(isServerTransactionPort), ioServerTransactionPath);
		oPostConnection = (HttpURLConnection) oPostUrl.openConnection();
		oPostConnection.setRequestMethod("POST");
		oPostConnection.setDoOutput(true);

		PrintWriter oPrintWriter = new PrintWriter(oPostConnection.getOutputStream());
		oPrintWriter.print(_sPostRequest);
		oPrintWriter.close();

		BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oPostConnection.getInputStream()));

		String inputLine;
		StringBuffer buffer = new StringBuffer();
		while ((inputLine = oBufferedReader.readLine()) != null)
		{
			buffer.append(inputLine);
		}
		oBufferedReader.close();

		String oResponse = buffer.toString();
		oTransactionResultHashMap.put("response", oResponse);

		// Parse Result
		StringTokenizer oStringTokenizer = new StringTokenizer(oResponse, "&");
		while (oStringTokenizer.hasMoreTokens())
		{
			String oTempString = oStringTokenizer.nextToken();
			StringTokenizer oStringTokenizerString = new StringTokenizer(oTempString, "=");
			if (oStringTokenizerString.countTokens() > 2 || oStringTokenizerString.countTokens() < 1)
			{
				throw new Exception("Bad variable from processor center: " + oTempString);
			}
			if (oStringTokenizerString.countTokens() == 1)
			{
				oTransactionResultHashMap.put(oStringTokenizerString.nextToken(), "");
			}
			else
			{
				oTransactionResultHashMap.put(oStringTokenizerString.nextToken(), oStringTokenizerString.nextToken());
			}
		}

		if (oTransactionResultHashMap.get("response") == "")
		{
			throw new Exception("Bad response from processor center" + oResponse);
		}

		if (!oTransactionResultHashMap.get("response").toString().equals("1"))
		{
			throw new Exception(oTransactionResultHashMap.get("responsetext").toString());
		}

		return oTransactionResultHashMap;
	}

	/**
	 * This method runs a test transaction to NMI with invalid credit card info.
	 */
	public void tryTransaction()
	{
		try
		{

			HashMap<String, String> oPaymentHashMap = null;
			// XSaLTNMIGatewayUtils oPaymentGateway = new
			// XSaLTNMIGatewayUtils("secure.networkmerchants.com", "third001",
			// "6497MkMaK");
			XSaLTNMIGatewayUtils oPaymentGateway = new XSaLTNMIGatewayUtils("secure.networkmerchants.com", "volg123", "58fhs04hs");

			String sCreditCardNo = "4111111111111111";
			String sExpDate = "09/2010";
			String sCCV = "111";
			double dTotalToCharge = 205.00;
			String sAddress1 = "sAddress1";
			String sAddress2 = "sAddress2";
			String sZip = "60126";

			oPaymentHashMap = oPaymentGateway.doNonWesternSpringsTransaction(sCreditCardNo, sExpDate, sCCV, dTotalToCharge, sAddress1 + ", " + sAddress2, sZip, 0.0,
					XSaLTStringUtils.getDatetimeStringNoUnderscore(), "000");

			oPaymentGateway = null;

			if (oPaymentHashMap.get("success").equals("true"))
			{
				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Success");
			}
			else
			{
				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "failure error");
			}

		}
		catch (Exception e)
		{
			XSaLTGenericLogger.error("", e);

		}
	}

}
