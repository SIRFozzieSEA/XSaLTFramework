package com.codef.xsalt.arch;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTGeneralException extends Exception
{
	/**
	 * The generated serial ID of the object
	 */
	static final long serialVersionUID = -435282924173309062L;

	/**
	 * The instance of Throwable
	 */
	private Throwable ioThrowableCause = null;

	/**
	 * Constructor One
	 */
	public XSaLTGeneralException()
	{
		super();
	}

	/**
	 * Constructor Two
	 * 
	 * @param _sMessage The exception message
	 */
	public XSaLTGeneralException(String _sMessage)
	{
		super(_sMessage);
	}

	/**
	 * Constructor Three
	 * 
	 * @param _sMessage The exception message
	 * @param _oThrowableCause The Throwable object
	 */
	public XSaLTGeneralException(String _sMessage, Throwable _oThrowableCause)
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
