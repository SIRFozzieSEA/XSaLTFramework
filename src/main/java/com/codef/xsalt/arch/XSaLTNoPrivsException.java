package com.codef.xsalt.arch;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTNoPrivsException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5607377470653262717L;
	
	/**
	 * The instance of Throwable
	 */
	private Throwable ioThrowableCause = null;

	/**
	 * Constructor One
	 */
	public XSaLTNoPrivsException()
	{
		super();
	}

	/**
	 * Constructor Two
	 * 
	 * @param _sMessage The exception message
	 */
	public XSaLTNoPrivsException(String _sMessage)
	{
		super(_sMessage);
	}

	/**
	 * Constructor Three
	 * 
	 * @param _sMessage The exception message
	 * @param _oThrowableCause The Throwable object
	 */
	public XSaLTNoPrivsException(String _sMessage, Throwable _oThrowableCause)
	{
		super(_sMessage);
		ioThrowableCause = _oThrowableCause;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	public Throwable getCause()
	{
		return ioThrowableCause;
	}

}
