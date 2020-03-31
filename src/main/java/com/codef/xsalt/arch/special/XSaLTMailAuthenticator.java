package com.codef.xsalt.arch.special;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTMailAuthenticator extends Authenticator
{

	/**
	 * The instance username
	 */
	public String isUsername = null;

	/**
	 * The instance password
	 */
	public String isPassword = null;

	/**
	 * 
	 * Primary constructor
	 * 
	 * @param _sUsername The username
	 * @param _sPassword The password
	 */
	public XSaLTMailAuthenticator(String _sUsername, String _sPassword)
	{
		isUsername = _sUsername;
		isPassword = _sPassword;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	public PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(isUsername, isPassword);
	}

}
