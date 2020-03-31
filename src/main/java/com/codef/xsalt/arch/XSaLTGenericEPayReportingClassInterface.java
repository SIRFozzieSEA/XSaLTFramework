package com.codef.xsalt.arch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

public interface XSaLTGenericEPayReportingClassInterface
{
	public HashMap<String, String> buildACHActivityReportInfo(Document _oDoc, String _sStartDate, String _sEndDate) throws SQLException;
	
	public Document createApplicationReport(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc,
			String _sStartDate, String _sEndDate) throws SQLException;
	
	public Document createTransactionReportInfo(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc,
			String _sStartDate, String _sEndDate) throws SQLException;
	
	public void setClientPk(String _sClientPk);
	
	public void setConnection(Connection _oConn);
}
