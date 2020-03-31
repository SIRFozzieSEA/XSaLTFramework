package com.codef.xsalt.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Priority;

import com.codef.xsalt.arch.XSaLTFrameworkProperties;
import com.codef.xsalt.arch.XSaLTGenericLogger;

public class XSaLTDatabaseCheckUtil
{	
	private String isHostName = null;
	private boolean ibShowOnlyErrors;
	
	public static void main(String[] args)
	{
		String sHostName = "localhost";
		boolean bShowOnlyErrors = true;
		if (args != null)
		{
			if (args.length > 0)
			{
				sHostName = args[0];
			}
			
			if (args.length > 1)
			{
				bShowOnlyErrors = Boolean.parseBoolean(args[1].trim());
			}
		}
		XSaLTDatabaseCheckUtil oCheckUtil = new XSaLTDatabaseCheckUtil(sHostName, bShowOnlyErrors);
		oCheckUtil.checkSchemaTables();
	}
	
	public XSaLTDatabaseCheckUtil(String _sHost, boolean _bShowOnlyErrors)
	{
		isHostName = _sHost;
		ibShowOnlyErrors = _bShowOnlyErrors;
	}
	
	public void checkSchemaTables()
	{
		try
		{
			Connection oConn = XSaLTDataUtils.getMySQLConnection(isHostName, null, "root", XSaLTFrameworkProperties.XS_DEFAULT_PASSWORD);
			ResultSet oSchemaRs = XSaLTDataUtils.querySQL(oConn, "SHOW DATABASES");
			while (oSchemaRs.next())
			{
				String sSchemaName = oSchemaRs.getString(1);
				
				if (!sSchemaName.matches("information_schema|mysql|performance_schema|test"))
				{
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "BEGIN check schema: " + sSchemaName);
					Connection oCurConn = XSaLTDataUtils.getMySQLConnection(isHostName, sSchemaName, "root", XSaLTFrameworkProperties.XS_DEFAULT_PASSWORD);

					ResultSet oTablesRs = XSaLTDataUtils.querySQL(oCurConn, "SHOW TABLES");
					while (oTablesRs.next())
					{
						String sTableName = oTablesRs.getString(1);
						ResultSet oCheckRs = XSaLTDataUtils.querySQL(oCurConn, "CHECK TABLE " + sTableName);
						while (oCheckRs.next())
						{
							String sMsg = oCheckRs.getString("msg_type");
							if (sMsg.matches("[Ee][Rr][Rr][Oo][Rr]") || !ibShowOnlyErrors)
							{
								XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sTableName + ": " + oCheckRs.getString("msg_type") + " " + oCheckRs.getString("msg_text"));
							}
						}
					}
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "END check schema: " + sSchemaName + "\n");
				}
				
			}
			
		}
		catch (Exception ex)
		{
			XSaLTGenericLogger.error("", ex);
		}
		
	}
}
