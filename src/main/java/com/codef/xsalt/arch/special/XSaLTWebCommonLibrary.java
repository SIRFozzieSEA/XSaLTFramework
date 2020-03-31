package com.codef.xsalt.arch.special;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.codef.xsalt.arch.XSaLTConstants;
import com.codef.xsalt.utils.XSaLTDataUtils;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTWebCommonLibrary
{
	
	/**
	 * This method activates a given client
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sClientPk The client_pk to inactivate
	 * @throws SQLException
	 */
	public static void activateClient(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sClientPk) throws SQLException
	{
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + XSaLTConstants.XS_APP_TABLE_CLIENT + " SET FLAG_IS_ACTIVE = '1' WHERE CLIENT_PK = '"
				+ _sClientPk + "'");
	}

	/**
	 * This method inactivates a given client
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sClientPk The client_pk to inactivate
	 * @throws SQLException
	 */
	public static void inactivateClient(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sClientPk) throws SQLException
	{
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE " + XSaLTConstants.XS_APP_TABLE_CLIENT + " SET FLAG_IS_ACTIVE = '0' WHERE CLIENT_PK = '"
				+ _sClientPk + "'");
	}

	/**
	 * This method activates a given user
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sUserPk The user_pk to inactivate
	 * @throws SQLException
	 */
	public static void activateUser(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sUserPk) throws SQLException
	{
		
		String sClientPrefix = _oXAppObject.getClientPrefix();
		
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE client_" + sClientPrefix + "_users SET FLAG_IS_ACTIVE = '1' WHERE USER_PK = '"
				+ _sUserPk + "'");
	}

	/**
	 * This method inactivates a given user
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sUserPk The user_pk to inactivate
	 * @throws SQLException
	 */
	public static void inactivateUser(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sUserPk) throws SQLException
	{
		
		String sClientPrefix = _oXAppObject.getClientPrefix();
		
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE client_" + sClientPrefix + "_users SET FLAG_IS_ACTIVE = '0' WHERE USER_PK = '"
				+ _sUserPk + "'");
	}

	/**
	 * This method activates a given role
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sRolePk The user_pk to inactivate
	 * @throws SQLException
	 */
	public static void activateUserRole(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sRolePk) throws SQLException
	{
		
		String sClientPrefix = _oXAppObject.getClientPrefix();
		
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE client_" + sClientPrefix + "_roles SET FLAG_IS_ACTIVE = '1' WHERE ROLE_PK = '" + _sRolePk + "'");
	}

	/**
	 * This method inactivates a given role
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sRolePk The user_pk to inactivate
	 * @throws SQLException
	 */
	public static void inactivateUserRole(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sRolePk) throws SQLException
	{
		
		String sClientPrefix = _oXAppObject.getClientPrefix();
		
		XSaLTDataUtils.executeSQL(_oConn, "UPDATE client_" + sClientPrefix + "_roles SET FLAG_IS_ACTIVE = '0' WHERE ROLE_PK = '" + _sRolePk + "'");
	}

	/**
	 * This method creates or edits a given client
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sClientPk The client_pk (if any)
	 * @param _sClientPrefix The clients database prefix
	 * @param _sClientName The clients full name or community name
	 * @return The newly created client_pk
	 * @throws SQLException
	 */
	public static String createEditClient(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sClientPk,
			String _sClientPrefix, String _sClientName) throws SQLException
	{

		LinkedHashMap<String, String> oFieldsMap = new LinkedHashMap<String, String>();

		if (_sClientPk != null)
		{
			oFieldsMap.put("CLIENT_PK", _sClientPk);
		}
		else
		{
			oFieldsMap.put("CLIENT_PK", null);
		}

		oFieldsMap.put("DB_PREFIX", _sClientPrefix.toLowerCase());
		oFieldsMap.put("MUNI_NAME", _sClientName);
		oFieldsMap.put("FLAG_IS_ACTIVE", "1");

		String sClientPk = XSaLTDataUtils.insertOrUpdateRowFromLinkedHashMap(_oConn, XSaLTConstants.XS_APP_TABLE_CLIENT, oFieldsMap,
				doesClientPrefixExist(_oConn, XSaLTConstants.XS_APP_TABLE_CLIENT, _sClientPrefix.toLowerCase()));
		if (sClientPk != null)
		{
			return sClientPk;
		}
		else
		{
			return null;
		}

	}

	/**
	 * This method creates a given user role
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sRolePk The role_pk (if any)
	 * @param _sClientPk The client_pk
	 * @param _sRoleName The role name to add
	 * @return The newly created role_pk
	 * @throws SQLException
	 */
	public static String createEditUserRole(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sRolePk, String _sClientPk,
			String _sRoleName) throws SQLException
	{

		LinkedHashMap<String, String> oFieldsMap = new LinkedHashMap<String, String>();

		if (_sRolePk != null)
		{
			oFieldsMap.put("ROLE_PK", _sRolePk);
		}
		else
		{
			oFieldsMap.put("ROLE_PK", null);
		}

		oFieldsMap.put("CLIENT_PK", _sClientPk);
		oFieldsMap.put("ROLE_NAME", _sRoleName);
		oFieldsMap.put("FLAG_IS_ACTIVE", "1");
		
		String sClientPrefix = _oXAppObject.getClientPrefix();

		String sRolePk = XSaLTDataUtils.insertOrUpdateRowFromLinkedHashMap(_oConn, "client_" + sClientPrefix + "_roles", oFieldsMap,
				doesUserRoleExist(_oConn, "client_" + sClientPrefix + "_roles", _sClientPk, _sRoleName));
		if (sRolePk != null)
		{
			return sRolePk;
		}
		else
		{
			return null;
		}

	}

	/**
	 * This method creates a new user for a given client and given role
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _oXAppObject The sessions XAuthObject
	 * @param _sUserPk The user_pk (if any)
	 * @param _sClientPk The client_pk
	 * @param _sLoginName The login name
	 * @param _sLoginPassword The login password
	 * @param _sFirstName The first name of the user
	 * @param _sLastName The last name of the user
	 * @param _sRolePk The role_pk of the user
	 * @return The newly created user_pk
	 * @throws SQLException
	 */
	public static String createEditUser(Connection _oConn, XSaLTAuthenticationObject _oXAppObject, String _sUserPk, String _sClientPk,
			String _sLoginName, String _sLoginPassword, String _sFirstName, String _sLastName, String _sRolePk) throws SQLException
	{

		LinkedHashMap<String, String> oFieldsMap = new LinkedHashMap<String, String>();

		if (_sUserPk != null)
		{
			oFieldsMap.put("USER_PK", _sUserPk);
		}
		else
		{
			oFieldsMap.put("USER_PK", null);
		}

		oFieldsMap.put("CLIENT_PK", _sClientPk);
		oFieldsMap.put("ID_NAME", _sLoginName.toLowerCase());
		oFieldsMap.put("PASSWORD", _sLoginPassword.toLowerCase());
		oFieldsMap.put("FIRST_NAME", _sFirstName);
		oFieldsMap.put("LAST_NAME", _sLastName);

		oFieldsMap.put("ROLE_PK", _sRolePk);
		oFieldsMap.put("FLAG_IS_ACTIVE", "1");
		
		String sClientPrefix = _oXAppObject.getClientPrefix();

		String sUserPk = XSaLTDataUtils.insertOrUpdateRowFromLinkedHashMap(_oConn, "client_" + sClientPrefix + "_users", oFieldsMap,
				doesUserNameExist(_oConn, "client_" + sClientPrefix + "_users", _sClientPk, _sLoginName.toLowerCase()));
		if (sUserPk != null)
		{
			return sUserPk;
		}
		else
		{
			return null;
		}

	}

	/**
	 * This method that tell whether or not the client prefix exists
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _sDataTable The data table to look up
	 * @param _sClientPrefix The clients database prefix
	 * @return Does the client prefix exist?
	 * @throws SQLException
	 */
	public static boolean doesClientPrefixExist(Connection _oConn, String _sDataTable, String _sClientPrefix) throws SQLException
	{

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT COUNT(*) AS RECCOUNT FROM " + _sDataTable + " WHERE DB_PREFIX like '"
				+ _sClientPrefix + "'");

		if (oRs.next())
		{
			if (oRs.getInt("RECCOUNT") > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

	/**
	 * This method to tell whether or not the username is in use
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _sDataTable The data table to look up
	 * @param _sClientPk The clients database prefix
	 * @param _sUserName The username to look up
	 * @return Does the user name exist?
	 * @throws SQLException
	 */
	public static boolean doesUserNameExist(Connection _oConn, String _sDataTable, String _sClientPk, String _sUserName)
			throws SQLException
	{

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT COUNT(*) AS RECCOUNT FROM " + _sDataTable + " WHERE CLIENT_PK = '"
				+ _sClientPk + "' AND ID_NAME like '" + _sUserName + "'");
		if (oRs.next())
		{
			if (oRs.getInt("RECCOUNT") > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

	/**
	 * This method to tell whether or not the user role exists
	 * 
	 * @param _oConn The pooled/transacted connection
	 * @param _sDataTable The data table to look up
	 * @param _sClientPk The clients database prefix
	 * @param _sUserRole The user role to look up
	 * @return Does the user role exist?
	 * @throws SQLException
	 */
	public static boolean doesUserRoleExist(Connection _oConn, String _sDataTable, String _sClientPk, String _sUserRole)
			throws SQLException
	{

		ResultSet oRs = XSaLTDataUtils.querySQL(_oConn, "SELECT COUNT(*) AS RECCOUNT FROM " + _sDataTable + " WHERE CLIENT_PK = '"
				+ _sClientPk + "' AND ROLE_NAME like '" + _sUserRole + "'");
		if (oRs.next())
		{
			if (oRs.getInt("RECCOUNT") > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

}
