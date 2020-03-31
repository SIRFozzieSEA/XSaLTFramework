package com.codef.xsalt.arch.special;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTNetworkAuthenticator extends Authenticator
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
	 * Primary constructor
	 * 
	 * @param _sUsername The username
	 * @param _sPassword The password
	 */
	public XSaLTNetworkAuthenticator(String _sUsername, String _sPassword)
	{
		isUsername = _sUsername;
		isPassword = _sPassword;
	}

	/* (non-Javadoc)
	 * @see java.net.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(isUsername, isPassword.toCharArray());
	}
}
