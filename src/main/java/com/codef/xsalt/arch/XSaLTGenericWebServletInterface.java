package com.codef.xsalt.arch;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;


/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public interface XSaLTGenericWebServletInterface
{

	/**
	 * This is the interface of the entry method of the task
	 * 
	
	 * 
	 * @param _oRequest The servlets request object
	 * @param _oResponse The servlets response object
	 * @param _sJavaTask The Java task to be executed
	 * @param _oDoc The document to be appended to
	 * @param _oConnection The pooled/transacted connection
	 * 
	 * @return Document The XML document to be transformed
	 * 
	 * @throws XSaLTGeneralException The standard XSaLT exception
	 * 
	 */
	public Document doTask(HttpServletRequest _oRequest, HttpServletResponse _oResponse, String _sJavaTask, Document _oDoc,
			Connection _oConnection) throws XSaLTGeneralException;

}
