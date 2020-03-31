package com.codef.xsalt.arch.special;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTMailMessageObject
{

	/**
	 * The mail authenticator
	 */
	private XSaLTMailAuthenticator ioXsaltAuthenticator = null;

	/**
	 * The mail server host address/ip
	 */
	private String isMailServerHostAddress = null;

	/**
	 * The mail server host address/ip
	 */
	private String isMailServerPort = null;

	/**
	 * The name that shows up on the outbound e-mail
	 */
	private String isMailMessageOriginatesFromName = null;

	/**
	 * The e-mail that shows up on the outbound e-mail
	 */
	private String isMailMessageOriginatesFromEmail = null;

	/**
	 * The message subject of the e-mail
	 */
	private String isMailMessageSubject = null;

	/**
	 * The message or body text of the e-mail
	 */
	private String isMailMessageText = null;

	/**
	 * The static parameter for text only
	 */
	public static String XS_MIME_TYPE_TEXT_ONLY = "text/text";

	/**
	 * The static parameter for html
	 */
	public static String XS_MIME_TYPE_HTML = "text/html";

	/**
	 * The message type (using the static parameters above)
	 */
	private String isMailMessageType = null;

	/**
	 * The flag that determines if the SMTP server requires authentication
	 */
	private boolean ibMailServerHostRequiresAuthentication = false;

	/**
	 * The hashmap of recipients (email, displayname)
	 */
	private HashMap<String, String> ioRecipientsToHashmap = new HashMap<String, String>();
	private HashMap<String, String> ioRecipientsBCCHashmap = new HashMap<String, String>();

	/**
	 * The arraylist of attatchments (file paths)
	 */
	private ArrayList<String> ioAttachmentsArraylist = new ArrayList<String>();

	/**
	 *  Default constructor
	 */
	public XSaLTMailMessageObject()
	{
		isMailMessageType = XS_MIME_TYPE_HTML;
		isMailMessageSubject = "No Subject";
		isMailMessageText = "No text in message.";
		isMailServerPort = "25";
	}

	/**
	 * This method returns whether or not the mail server requires authentication
	 * 
	 * @return Whether or not the mail server requires authentication
	 */
	public boolean doesMailServerHostRequiresAuthentication()
	{
		return ibMailServerHostRequiresAuthentication;
	}

	/**
	 * This method returns a HashMap of the recipients (directly addressed)
	 * 
	 * @return HashMap of the recipients
	 */
	public HashMap<String, String> getRecipientsToHashmap()
	{
		return ioRecipientsToHashmap;
	}

	/**
	 * This method returns a HashMap of the recipients
	 * 
	 * @return HashMap of the recipients
	 */
	public HashMap<String, String> getRecipientsBCCHashmap()
	{
		return ioRecipientsBCCHashmap;
	}

	/**
	 * This method returns the instance of the XSaLTMailAuthenticator
	 * 
	 * @return Instance of the XSaLTMailAuthenticator
	 */
	public XSaLTMailAuthenticator getXsaltAuthenticator()
	{
		return ioXsaltAuthenticator;
	}

	/**
	 * This method returns the e-mail the mail originates from (steve@codef.com)
	 * 
	 * @return The e-mail the mail originates from
	 */
	public String getMailMessageOriginatesFromEmail()
	{
		return isMailMessageOriginatesFromEmail;
	}

	/**
	 * This method returns the e-mail *name* the mail originates from (Stephan Cossette)
	 * 
	 * @return The e-mail the mail originates from
	 */
	public String getMailMessageOriginatesFromName()
	{
		return isMailMessageOriginatesFromName;
	}

	/**
	 * This method returns the e-mail subject line
	 * 
	 * @return The e-mail subject line
	 */
	public String getMailMessageSubject()
	{
		return isMailMessageSubject;
	}

	/**
	 * This method returns the e-mail body text
	 * 
	 * @return The e-mail body text
	 */
	public String getMailMessageText()
	{
		return isMailMessageText;
	}

	/**
	 * This method returns the e-mail message type
	 * 
	 * @return The e-mail message type
	 */
	public String getMailMessageType()
	{
		return isMailMessageType;
	}

	/**
	 * This method returns the mail server host address
	 * 
	 * @return The mail server host address
	 */
	public String getMailServerHostAddress()
	{
		return isMailServerHostAddress;
	}

	/**
	 * This method returns the mail server port number.
	 * 
	 * @return The mail server port number
	 */
	public String getMailServerPort()
	{
		return isMailServerPort;
	}

	/**
	 * This method returns an ArrayList of e-mail attatchments (location on disc)
	 * 
	 * @return An ArrayList of e-mail attatchments
	 */
	public ArrayList<String> getAttachmentsArraylist()
	{
		return ioAttachmentsArraylist;
	}

	/**
	 * This method returns the total size of all attachments in the message
	 * 
	 * @return The total K of attatchments to this message
	 */
	public long getTotalAttachmentSize()
	{
		long lTotalFilesSize = 0;
		for (int i = 0; i < getAttachmentsArraylist().size(); i++)
		{
			String sFilePath = getAttachmentsArraylist().get(i).toString();
			File oFile = new File(sFilePath);
			lTotalFilesSize = lTotalFilesSize + oFile.length();
		}
		return lTotalFilesSize;
	}

	/**
	 * This sets whether the mail host server requires authentication
	 * 
	 * @param _bMailServerHostRequiresAuthentication Whether the mail host server requires authentication
	 */
	public void setMailServerHostRequiresAuthentication(boolean _bMailServerHostRequiresAuthentication)
	{
		ibMailServerHostRequiresAuthentication = _bMailServerHostRequiresAuthentication;
	}

	/**
	 * This method sets the recipients of the mail message
	 * 
	 * @param _oRecipientsHashmap A HashMap of the recipients
	 * @param _bFromTMAGatewayFlag 
	 */
	public void setRecipientsHashmap(HashMap<String, String> _oRecipientsHashmap, boolean _bFromTMAGatewayFlag)
	{

		HashMap<String, String> oTempRecipientsToHashmap = new HashMap<String, String>();
		HashMap<String, String> oTempRecipientsBCCHashmap = new HashMap<String, String>();

		if (_bFromTMAGatewayFlag)
		{

			for (Iterator<String> i = _oRecipientsHashmap.keySet().iterator(); i.hasNext();)
			{
				String sEmailAddress = (String) i.next();
				String sDisplayName = (String) _oRecipientsHashmap.get(sEmailAddress);

				if (sEmailAddress.endsWith("tmainc.org") || sEmailAddress.endsWith("dmspostal.com") || sEmailAddress.endsWith("datserv.com"))
				{
					oTempRecipientsBCCHashmap.put(sEmailAddress, sDisplayName);
				}
				else
				{
					oTempRecipientsToHashmap.put(sEmailAddress, sDisplayName);
				}

			}

			ioRecipientsToHashmap = oTempRecipientsToHashmap;
			ioRecipientsBCCHashmap = oTempRecipientsBCCHashmap;

		}
		else
		{
			for (Iterator<String> i = _oRecipientsHashmap.keySet().iterator(); i.hasNext();)
			{
				String sEmailAddress = (String) i.next();
				String sDisplayName = (String) _oRecipientsHashmap.get(sEmailAddress);
				oTempRecipientsToHashmap.put(sEmailAddress, sDisplayName);

			}

			ioRecipientsToHashmap = oTempRecipientsToHashmap;
			ioRecipientsBCCHashmap = oTempRecipientsBCCHashmap;
		}

	}

	/**
	 * This method sets the XSaLTMailAuthenticator if the mail server requires authentication
	 * 
	 * @param _oXsaltAuthenticator The XSaLTMailAuthenticator to use with this message
	 */
	public void setXsaltAuthenticator(XSaLTMailAuthenticator _oXsaltAuthenticator)
	{
		ioXsaltAuthenticator = _oXsaltAuthenticator;
	}

	/**
	 * This method sets the return e-mail address (steve@codef.com)
	 * 
	 * @param _sMailMessageOriginatesFromEmail The e-mail address the e-mail orginates from
	 */
	public void setMailMessageOriginatesFromEmail(String _sMailMessageOriginatesFromEmail)
	{
		isMailMessageOriginatesFromEmail = _sMailMessageOriginatesFromEmail;
	}

	/**
	 * This method sets the return e-mail *name* (Stephan Cossette)
	 * 
	 * @param _sMailMessageOriginatesFromName The e-mail name the e-mail orginates from
	 */
	public void setMailMessageOriginatesFromName(String _sMailMessageOriginatesFromName)
	{
		isMailMessageOriginatesFromName = _sMailMessageOriginatesFromName;
	}

	/**
	 * This method sets the subject line of the message
	 * 
	 * @param _sMailMessageSubject The subject message you wish to use for the e-mail
	 */
	public void setMailMessageSubject(String _sMailMessageSubject)
	{
		isMailMessageSubject = _sMailMessageSubject;
	}

	/**
	 * This method sets the body text of the message
	 * 
	 * @param _sMailMessageText The body text of the e-mail
	 */
	public void setMailMessageText(String _sMailMessageText)
	{
		isMailMessageText = _sMailMessageText;
	}

	/**
	 * This method sets the mail message type
	 * 
	 * @param _sMailMessageType The mail message type you wish to use
	 */
	public void setMailMessageType(String _sMailMessageType)
	{
		isMailMessageType = _sMailMessageType;
	}

	/**
	 * This method sets the mail server host address
	 * 
	 * @param _sMailServerHostAddress The mail server host address or name
	 */
	public void setMailServerHostAddress(String _sMailServerHostAddress)
	{
		isMailServerHostAddress = _sMailServerHostAddress;
	}

	/**
	 * This method sets the mail server port number.
	 * 
	 * @param _sMailServerPort
	 *            The mail server port
	 */
	public void setMailServerPort(String _sMailServerPort)
	{
		isMailServerPort = _sMailServerPort;
	}

	/**
	 * This method sets the e-mail attachments
	 * 
	 * @param _oAttachmentsArraylist ArrayList of Strings telling where the location of the files are on disc
	 */
	public void setAttachmentsArraylist(ArrayList<String> _oAttachmentsArraylist)
	{
		ioAttachmentsArraylist = _oAttachmentsArraylist;
	}

}
