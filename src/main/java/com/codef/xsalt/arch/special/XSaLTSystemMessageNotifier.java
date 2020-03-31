package com.codef.xsalt.arch.special;

import java.sql.Connection;
import java.sql.SQLException;

import com.codef.xsalt.utils.XSaLTDataUtils;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTSystemMessageNotifier
{

	/**
	 * The default constructor.
	 */
	public XSaLTSystemMessageNotifier()
	{

	}

	/**
	 * This method inserts a notifier message for the given client.
	 * 
	 * @param _oConn
	 *            Database connection object
	 * @param _sClientDBPrefixForLogs
	 *            Prefix for client's log DB table
	 * @param _sMessageType
	 *            Type of message (currently only "System")
	 * @param _sMessageSubject
	 *            Message subject 
	 * @param _sMessage
	 *            Body of message
	 * @param _lProcessPk
	 *            Primary key for process attached to message
	 * @param _lClientPk
	 *            Primary key for client attached to message
	 * @param _lUserPk
	 *            Primary key for user to send message to
	 * @throws SQLException
	 */
	public static void sendSystemMessage(Connection _oConn, String _sClientDBPrefixForLogs, String _sMessageType, String _sMessageSubject, String _sMessage, long _lProcessPk,
			long _lClientPk, long _lUserPk) throws SQLException
	{
		XSaLTDataUtils.executeSQL(_oConn, "INSERT INTO tmaxapp.client_" + _sClientDBPrefixForLogs + "_system_message_log VALUES (null, '" + _lProcessPk + "', '" + _lClientPk
				+ "', '" + _lUserPk + "', '" + _sMessageType + "', '" + _sMessageSubject + "', '" + _sMessage + "', now())");

	}

}
