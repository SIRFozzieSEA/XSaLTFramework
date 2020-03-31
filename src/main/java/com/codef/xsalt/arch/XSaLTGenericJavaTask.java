package com.codef.xsalt.arch;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import com.codef.xsalt.utils.XSaLTXMLUtils;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTGenericJavaTask extends XSaLTGenericWebServlet implements XSaLTGenericWebServletInterface
{

	/**
	 * The generated serial ID of the object
	 */
	static final long serialVersionUID = 7206446750956243156L;

	/**
	 * This is the entry method of the task
	 * 
	
	 * 
	 * @param _oRequest The servlets request object
	 * @param _oResponse The servlets response object
	 * @param _oDoc The document to be appended to
	 * @param _oConnection The pooled/transacted connection
	 * 
	 * @return Document The XML document to be transformed
	 * 
	 * @throws XSaLTGeneralException The standard XSaLT exception
	 * 
	 */
	public Document doTask(HttpServletRequest _oRequest, HttpServletResponse _oResponse, Document _oDoc, Connection _oConnection)
			throws XSaLTGeneralException
	{
		XSaLTXMLUtils.addSimpleTextNode(_oDoc, _oDoc.getDocumentElement(), "FromJavaTask", getClass().getName());
		return _oDoc;
	}

}
