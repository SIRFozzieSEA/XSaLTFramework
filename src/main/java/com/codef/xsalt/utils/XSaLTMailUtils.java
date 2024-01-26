package com.codef.xsalt.utils;

import java.io.IOException;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.codef.xsalt.arch.XSaLTLoggerWrapper;
import com.codef.xsalt.arch.XSaLTMailMessageObject;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTMailUtils {

	public static boolean XB_TEST_MODE = false;
	public static boolean XB_IS_TEST_SERVER = false;

	/**
	 * Mails a com.codef.xsalt.arch.XSaLTMailMessageObject
	 * 
	 * @param _oXsaltMmo
	 * @param _bOverrideTestServerFlag
	 * @throws MessagingException
	 * @throws IOException 
	 */
	public void sendMail(XSaLTMailMessageObject _oXsaltMmo, String _sOverrideServerTestFlag)
			throws MessagingException, IOException {

		String sFromPerson = "";
		StringBuffer oToPersonBuffer = new StringBuffer();
		StringBuffer oBCCPersonBuffer = new StringBuffer();
		String sSubject = "";
		String sBodyText = "";

		java.util.Properties oMailProperties = System.getProperties();
		oMailProperties.put("mail.smtp.host", _oXsaltMmo.getMailServerHostAddress());
		oMailProperties.put("mail.smtp.port", _oXsaltMmo.getMailServerPort());
		oMailProperties.put("mail.smtp.auth", Boolean.valueOf(_oXsaltMmo.doesMailServerHostRequiresAuthentication()));
		oMailProperties.put("mail.from", _oXsaltMmo.getMailMessageOriginatesFromEmail());

		if (_oXsaltMmo.getMailServerPort().equalsIgnoreCase("587")) {
			oMailProperties.put("mail.imap.auth.plain.disable", "true");
			oMailProperties.put("mail.imap.auth.ntlm.disable", "true");
			oMailProperties.put("mail.smtp.auth", "true");
			oMailProperties.put("mail.debug", "true");
		}

		Session oMailSession = Session.getInstance(oMailProperties, _oXsaltMmo.getXsaltAuthenticator());

		MimeMessage oMimeMessage = new MimeMessage(oMailSession);
		Address oFromAddress = new InternetAddress(_oXsaltMmo.getMailMessageOriginatesFromEmail(),
				_oXsaltMmo.getMailMessageOriginatesFromName());
		oMimeMessage.setFrom(oFromAddress);

		Address[] aoSenderAddress = new Address[1];
		aoSenderAddress[0] = new InternetAddress(_oXsaltMmo.getMailMessageOriginatesFromEmail());
		oMimeMessage.setReplyTo(aoSenderAddress);

		sFromPerson = _oXsaltMmo.getMailMessageOriginatesFromName() + "("
				+ _oXsaltMmo.getMailMessageOriginatesFromEmail() + ")";

		boolean bHasToRecipients = false;
		int nRecipientToCount = 0;
		Address[] aoRecipientAddresses = new Address[_oXsaltMmo.getRecipientsToHashmap().size()];
		for (Iterator<String> i = _oXsaltMmo.getRecipientsToHashmap().keySet().iterator(); i.hasNext();) {
			String sEmailAddress = (String) i.next();
			String sDisplayName = (String) _oXsaltMmo.getRecipientsToHashmap().get(sEmailAddress);
			oToPersonBuffer.append(sDisplayName + " (" + sEmailAddress + "); ");
			aoRecipientAddresses[nRecipientToCount] = new InternetAddress(sEmailAddress, sDisplayName);
			nRecipientToCount = nRecipientToCount + 1;
			bHasToRecipients = true;
		}

		if (bHasToRecipients) {
			oMimeMessage.setRecipients(RecipientType.TO, aoRecipientAddresses);
		}

		boolean bHasBCCRecipients = false;
		int nRecipientBCCCount = 0;
		aoRecipientAddresses = new Address[_oXsaltMmo.getRecipientsBCCHashmap().size()];
		for (Iterator<String> i = _oXsaltMmo.getRecipientsBCCHashmap().keySet().iterator(); i.hasNext();) {
			String sEmailAddress = (String) i.next();
			String sDisplayName = (String) _oXsaltMmo.getRecipientsBCCHashmap().get(sEmailAddress);
			oBCCPersonBuffer.append(sDisplayName + " (" + sEmailAddress + "); ");
			aoRecipientAddresses[nRecipientBCCCount] = new InternetAddress(sEmailAddress, sDisplayName);
			nRecipientBCCCount = nRecipientBCCCount + 1;
			bHasBCCRecipients = true;
		}

		if (bHasBCCRecipients) {
			oMimeMessage.setRecipients(RecipientType.BCC, aoRecipientAddresses);
		}

		sSubject = _oXsaltMmo.getMailMessageSubject();
		oMimeMessage.setSubject(sSubject);

		sBodyText = _oXsaltMmo.getMailMessageText();
		BodyPart oMessageBodyPart = new MimeBodyPart();
		oMessageBodyPart.setContent(sBodyText, _oXsaltMmo.getMailMessageType());

		MimeMultipart oMimeMultipart = new MimeMultipart("FILEZ");
		oMimeMultipart.addBodyPart(oMessageBodyPart);

		for (int i = 0; i < _oXsaltMmo.getAttachmentsArraylist().size(); i++) {
			oMessageBodyPart = new MimeBodyPart();
			String sFilePath = _oXsaltMmo.getAttachmentsArraylist().get(i).toString();
			String sFilename = sFilePath.substring(sFilePath.lastIndexOf('/') + 1, sFilePath.length());
			DataSource oDatasource = new FileDataSource(sFilePath);
			oMessageBodyPart.setDataHandler(new DataHandler(oDatasource));
			oMessageBodyPart.setFileName(sFilename);
			oMimeMultipart.addBodyPart(oMessageBodyPart);
		}

		oMimeMessage.setContent(oMimeMultipart);
		if (bHasToRecipients || bHasBCCRecipients) {

			if (XB_TEST_MODE) {
				StringBuffer oMailBuffer = new StringBuffer();
				String sEMailUniqueKey = "MAIL_" + XSaLTStringUtils.getDatetimeStringNoUnderscoreWithMilli() + ".txt";
				oMailBuffer.append("XSaLTMailUtils Test Message ------------------------------------------");
				oMailBuffer.append("\n\n");
				oMailBuffer.append("     FROM: " + sFromPerson);
				oMailBuffer.append("\n\n");
				oMailBuffer.append("       TO: " + oToPersonBuffer.toString());
				oMailBuffer.append("\n\n");
				oMailBuffer.append("      BCC: " + oBCCPersonBuffer.toString());
				oMailBuffer.append("\n\n");
				oMailBuffer.append("  SUBJECT: " + sSubject);
				oMailBuffer.append("\n\n");
				oMailBuffer.append("     BODY: " + sBodyText);
				oMailBuffer.append("\n\n");

				XSaLTFileSystemUtils.createFileFolder("C:/_WORKING/TEST_MAILS/");
				XSaLTFileSystemUtils.writeStringBufferToFile(oMailBuffer, "C:/_WORKING/TEST_MAILS/" + sEMailUniqueKey);

				XSaLTLoggerWrapper.info(XSaLTMailUtils.class.getName(), "Mailing TEST 1: " + sSubject + " ("
						+ oToPersonBuffer.toString() + oBCCPersonBuffer.toString() + ")");

			} else {

				if (_sOverrideServerTestFlag != null && _sOverrideServerTestFlag.equalsIgnoreCase("YES")) {
					Transport.send(oMimeMessage);
					XSaLTLoggerWrapper.info(XSaLTMailUtils.class.getName(), "Mailing LIVE 1: " + sSubject + " ("
							+ oToPersonBuffer.toString() + oBCCPersonBuffer.toString() + ")");
				} else {
					if (XB_IS_TEST_SERVER) {
						StringBuffer oMailBuffer = new StringBuffer();
						String sEMailUniqueKey = "MAIL_" + XSaLTStringUtils.getDatetimeStringNoUnderscoreWithMilli()
								+ ".txt";
						oMailBuffer.append("XSaLTMailUtils Test Message ------------------------------------------");
						oMailBuffer.append("\n\n");
						oMailBuffer.append("     FROM: " + sFromPerson);
						oMailBuffer.append("\n\n");
						oMailBuffer.append("       TO: " + oToPersonBuffer.toString());
						oMailBuffer.append("\n\n");
						oMailBuffer.append("      BCC: " + oBCCPersonBuffer.toString());
						oMailBuffer.append("\n\n");
						oMailBuffer.append("  SUBJECT: " + sSubject);
						oMailBuffer.append("\n\n");
						oMailBuffer.append("     BODY: " + sBodyText);
						oMailBuffer.append("\n\n");

						XSaLTFileSystemUtils.createFileFolder("C:/_WORKING/TEST_MAILS/");
						XSaLTFileSystemUtils.writeStringBufferToFile(oMailBuffer,
								"C:/_WORKING/TEST_MAILS/" + sEMailUniqueKey);

						XSaLTLoggerWrapper.info(XSaLTMailUtils.class.getName(), "Mailing TEST 2: " + sSubject + " ("
								+ oToPersonBuffer.toString() + oBCCPersonBuffer.toString() + ")");

					} else {
						Transport.send(oMimeMessage);

						XSaLTLoggerWrapper.info(XSaLTMailUtils.class.getName(), "Mailing LIVE 2: " + sSubject + " ("
								+ oToPersonBuffer.toString() + oBCCPersonBuffer.toString() + ")");

					}
				}

			}

		}

	}

//	/**
//	 * This method sends a test e-mail.
//	 */
//	public void testEMail()
//	{
//		XSaLTMailMessageObject oXsaltMmo = new XSaLTMailMessageObject();
//
//		String sMailDisplayName = "tma_general";
//		String sMailReturnEMailAddress = "info@tmainc.org";
//		String sMailHostName = "hostname";
//		String sMailPopUserName = "tma_general";
//		String sMailPopPassword = "fhrnsk32#";
//
//		oXsaltMmo.setMailMessageOriginatesFromName(sMailDisplayName);
//		oXsaltMmo.setMailMessageOriginatesFromEmail(sMailReturnEMailAddress);
//		oXsaltMmo.setMailServerHostAddress(sMailHostName);
//		oXsaltMmo.setMailServerHostRequiresAuthentication(true);
//		oXsaltMmo.setMailMessageType("text/html");
//		oXsaltMmo.setMailServerPort("587");
//
//		XSaLTMailAuthenticator oXsaltAuth = new XSaLTMailAuthenticator(sMailPopUserName, sMailPopPassword);
//		oXsaltMmo.setXsaltAuthenticator(oXsaltAuth);
//
//		String sReceiptEMailSubject = "Test Subject";
//		oXsaltMmo.setMailMessageSubject(sReceiptEMailSubject);
//		oXsaltMmo.setMailMessageText("This is a test!");
//
//		HashMap<String, String> oRecipientHashmap = new HashMap<String, String>();
//		oRecipientHashmap.put("steve@tmainc.org", "steve@tmainc.org");
//		oXsaltMmo.setRecipientsHashmap(oRecipientHashmap, false);
//
//		try
//		{
//			XSaLTMailUtils oTestMail = new XSaLTMailUtils();
//			XSaLTMailUtils.setTestMode(true);
//			oTestMail.sendMail(oXsaltMmo, "YES");
//			XSaLTLoggerWrapper.info( "Success");
//
//		}
//		catch (Exception e)
//		{
//
//			XSaLTLoggerWrapper.info( "Fail", e);
//
//		}
//	}

	/**
	 * This method sets the "test mode" flag.
	 * @param _bFlag
	 *            Flag if class should be in test mode
	 */
	public static void setTestMode(boolean _bFlag) {
		XB_TEST_MODE = _bFlag;
	}

}
