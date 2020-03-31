package com.codef.xsalt.arch.special;

import java.sql.Connection;
import org.apache.log4j.Priority;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.codef.xsalt.arch.XSaLTGenericLogger;
import com.codef.xsalt.utils.XSaLTDataUtils;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTSystemTables
{
	/**
	 * Main Constructor
	 */
	public XSaLTSystemTables()
	{
	}

	/**
	 * The method that creates the `client_" + _sClientPrefix + "_address_types`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            The prefix for a given client
	 * @throws SQLException
	 */
	public void createValidAddressTypesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();

		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_address_types`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_address_types` (");
		sCreateTableSQL.append("  `ADDRESS_TYPE_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `ADDRESS_TYPE` varchar(50) NOT NULL,");
		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");
		sCreateTableSQL.append("  PRIMARY KEY  (`ADDRESS_TYPE_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB ");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * This method creates the 'client_" + _sClientPrefix + "_html_colors'
	 * table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for client
	 * @throws SQLException
	 */
	public void createValidHtmlColorsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_html_colors`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_html_colors` (");
		sCreateTableSQL.append("  `COLOR_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `COLOR` varchar(50) NOT NULL,");
		sCreateTableSQL.append("  PRIMARY KEY  (`COLOR_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB ");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `xapp_valid_clients` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createClientsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{

		String sClientTableName = "xapp_valid_clients";
		if (_sClientPrefix.equalsIgnoreCase("TMAG"))
		{
			sClientTableName = "client_" + _sClientPrefix + "_clients";
		}

		if (_sClientPrefix.equalsIgnoreCase("TEST"))
		{
			sClientTableName = "xapp_" + _sClientPrefix + "_clients";
		}

		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `" + sClientTableName + "`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();

		sCreateTableSQL.append("CREATE TABLE `" + sClientTableName + "` (");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `DB_PREFIX` varchar(20) NOT NULL default '',");
		sCreateTableSQL.append("  `MUNI_NAME` varchar(200) NOT NULL default '',");

		if (_sClientPrefix.equalsIgnoreCase("TMAG"))
		{
			sCreateTableSQL.append("  `FLAG_CAN_DO_PDF_WO_BACKGROUND` TINYINT unsigned  NOT NULL default '0',");
			sCreateTableSQL.append("  `FLAG_CAN_DOWNLOAD_PRODUCTION_PDF` TINYINT unsigned  NOT NULL default '0',");
		}

		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");
		sCreateTableSQL.append("  PRIMARY KEY  (`CLIENT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_field_types`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidDynamicFieldTypesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_field_types`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_field_types` (");
		sCreateTableSQL.append("  `TYPE_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `FIELD_TYPE` varchar(100) NOT NULL,");
		sCreateTableSQL.append("  `FIELD_SEARCH_TYPE` varchar(100) NOT NULL,");
		sCreateTableSQL.append("  `FIELD_SQL` varchar(100) NOT NULL,");
		sCreateTableSQL.append("  `FIELD_DESCRIPTION` varchar(100) NOT NULL,");
		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");
		sCreateTableSQL.append("  PRIMARY KEY  (`TYPE_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_processes`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidProcessesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes` (");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `AVAILABLE_PROCESS_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `CLIENT_NAME` varchar(100) default '0',");
		sCreateTableSQL.append("  `RENDER_NAME` varchar(100) default '0',");
		sCreateTableSQL.append("  `RENDER_JAVA_PATH` varchar(750) ,");
		sCreateTableSQL.append("  `RENDER_XML_DEFINITION` varchar(100) ,");
		sCreateTableSQL.append("  `ACCOUNT_NO_FORMAT` varchar(100) default '',");
		sCreateTableSQL.append("  `DATA_FOLDER_PATH` varchar(500)  default '',");
		sCreateTableSQL.append("  `PDF_FOLDER_PATH` varchar(500)  default '',");
		sCreateTableSQL.append("  `SCHEMA_NAME` varchar(200) ,");
		sCreateTableSQL.append("  `TABLE_NAME` varchar(200) ,");

		sCreateTableSQL.append("  `PROCESS_FINISHED_FLAG` varchar(1)  default 'N',");
		sCreateTableSQL.append("  `PROCESS_TEST_FLAG` varchar(1)  default 'N',");

		sCreateTableSQL.append("  `PROCESS_MANIFEST_FLAG` varchar(1)  default 'N',");
		sCreateTableSQL.append("  `PROCESS_NCOA_SUBSTITUTE_FLAG` varchar(1)  default 'N',");
		sCreateTableSQL.append("  `PROCESS_EPAY_FLAG` varchar(1)  default 'N',");

		sCreateTableSQL.append("  `PROCESS_STARTED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `POSTAL_STARTED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `PROCESS_ENDED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `PROCESS_APPROVED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `JOB_STARTED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `JOB_COMPLETE_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `JOB_ENTERED_FOR_MAIL_DATE` DATE,");
		sCreateTableSQL.append("  `JOB_BILLED_DATE` DATE DEFAULT NULL,");
		sCreateTableSQL.append("  `JOB_BILLED_INVOICE_NO` varchar(20) DEFAULT NULL,");

		sCreateTableSQL.append("  `PROCESS_STAGE` varchar(100),");
		sCreateTableSQL.append("  `PROCESS_LAST_ACTIVITY_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `PRODUCTION_STATUS` varchar(20),");

		sCreateTableSQL.append("  `PRODUCTION_LOCATION_SHORT` varchar(10) default '' ,");
		sCreateTableSQL.append("  `PRODUCTION_LOCATION_LONG` varchar(50) default '' ,");

		sCreateTableSQL.append("  `SEGMENTATION_NOTES` varchar(100) default 'No',");
		sCreateTableSQL.append("  `SEGMENTATION_NOTES_NO_CRE` varchar(100) default '',");

		sCreateTableSQL.append("  `PERMIT_NUMBER` varchar(10) default '',");
		sCreateTableSQL.append("  `CAPS_NUMBER` varchar(20) default '',");
		sCreateTableSQL.append("  `HELD_AT_USPS_LOCATION` varchar(50) default '',");

		sCreateTableSQL.append("  `DECLINE_NOTES` varchar(210) default '',");
		sCreateTableSQL.append("  `MAIL_NOTES` varchar(500) default '',");

		sCreateTableSQL.append("  PRIMARY KEY  (`PROCESS_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_clients_pafs`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidPAFTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_clients_pafs`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_clients_pafs` (");
		sCreateTableSQL.append("  `PAF_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `DB_PREFIX` varchar(20) NOT NULL default '',");
		sCreateTableSQL.append("  `MUNI_NAME` varchar(200) NOT NULL default '',");

		sCreateTableSQL.append("  `CLIENT_NAME` varchar(100) default '',");
		sCreateTableSQL.append("  `ADDRESS_1` varchar(100) default '',");
		sCreateTableSQL.append("  `ADDRESS_2` varchar(100) default '',");
		sCreateTableSQL.append("  `CITY` varchar(40) default '',");
		sCreateTableSQL.append("  `STATEPROV` varchar(20) default '',");
		sCreateTableSQL.append("  `ZIP` varchar(20) default '',");
		sCreateTableSQL.append("  `PHONE` varchar(40) default '',");

		sCreateTableSQL.append("  `PAF_SIGNER_NAME` varchar(100) default '',");
		sCreateTableSQL.append("  `PAF_SIGNER_TITLE` varchar(100) default '',");
		sCreateTableSQL.append("  `PAF_SIGNER_EMAIL` varchar(100) default '',");

		sCreateTableSQL.append("  `PAF_SIGNED_DATE` DATE DEFAULT NULL,");
		sCreateTableSQL.append("  `PAF_VALID_UNTIL_DATE` DATE DEFAULT NULL,");

		sCreateTableSQL.append("  PRIMARY KEY  (`PAF_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * This method creates the 'client_" + _sClientPrefix + "_processes_options'
	 * table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for given client
	 * @throws SQLException
	 */
	public void createValidProcessOptionsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes_options`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes_options` (");
		sCreateTableSQL.append("  `PROCESS_OPTION_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned,");

		sCreateTableSQL.append("  `OPTION_NAME` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE` varchar(100) default '',");

		sCreateTableSQL.append("  PRIMARY KEY  (`PROCESS_OPTION_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + sClientPrefix + "_available_processes` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidAvailableProcessesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_available_processes`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_available_processes` (");
		sCreateTableSQL.append("  `AVAILABLE_PROCESS_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `CLIENT_NAME` varchar(100) default '0',");
		sCreateTableSQL.append("  `RENDER_NAME` varchar(100) default '0',");
		sCreateTableSQL.append("  `RENDER_JAVA_PATH` varchar(750) ,");
		sCreateTableSQL.append("  `RENDER_XML_DEFINITION` varchar(100) ,");
		sCreateTableSQL.append("  `ACCOUNT_NO_FORMAT` varchar(100) default '',");
		sCreateTableSQL.append("  `DEFAULT_PRODUCTION_LOCATION_SHORT` varchar(10) default '' ,");
		sCreateTableSQL.append("  `DEFAULT_PRODUCTION_LOCATION_LONG` varchar(50) default '' ,");

		sCreateTableSQL.append("  `SEGMENTATION_NOTES` varchar(100) default 'No',");
		sCreateTableSQL.append("  `SEGMENTATION_NOTES_NO_CRE` varchar(100) default '',");

		sCreateTableSQL.append("  `PROCESS_NOTES` varchar(1000) default 'No notes entered.',");

		sCreateTableSQL.append("  `PROCESS_MANIFEST_FLAG` varchar(1)  default 'N',");
		sCreateTableSQL.append("  `PROCESS_NCOA_SUBSTITUTE_FLAG` varchar(1)  default 'N',");
		sCreateTableSQL.append("  `PROCESS_EPAY_FLAG` varchar(1)  default 'N',");

		sCreateTableSQL.append("  `FLAG_CAN_SUBMIT_FILES` varchar(1)  default 'Y',");
		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");

		sCreateTableSQL.append("  PRIMARY KEY  (`AVAILABLE_PROCESS_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * This method creates the
	 * `client_" + _sClientPrefix + "_processes_reminders` table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createProcessRemindersTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes_reminders`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();

		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes_reminders` (");
		sCreateTableSQL.append("  `PROCESS_REMINDER_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `CLIENT_NAME` varchar(50) default '',");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `ITEM_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `RENDER_NAME` varchar(75) default '',");
		sCreateTableSQL.append("  `REMINDER_TYPE` varchar(100) default '',");

		sCreateTableSQL.append("  `PROCESS_CLASS` varchar(1000) default '',");
		sCreateTableSQL.append("  `PROCESS_ARG_1` varchar(20) default '',");
		sCreateTableSQL.append("  `PROCESS_ARG_2` varchar(20) default '',");
		sCreateTableSQL.append("  `PROCESS_ARG_3` varchar(20) default '',");
		sCreateTableSQL.append("  `PROCESS_ARG_4` varchar(20) default '',");
		sCreateTableSQL.append("  `PROCESS_ARG_5` varchar(20) default '',");

		sCreateTableSQL.append("  `NEXT_REMINDER_SEND` DATETIME,");

		sCreateTableSQL.append("  PRIMARY KEY  (`PROCESS_REMINDER_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + sClientPrefix + "_available_processes` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidAvailableAssemblyTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_available_processes_assembly`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_available_processes_assembly` (");
		sCreateTableSQL.append("  `AVAILABLE_ASSEMBLY_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `AVAILABLE_PROCESS_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `ITEM_PK` bigint(20) unsigned default '0',");

		sCreateTableSQL.append("  PRIMARY KEY  (`AVAILABLE_ASSEMBLY_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + sClientPrefix + "_available_processes` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidAssemblyTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes_assembly`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes_assembly` (");
		sCreateTableSQL.append("  `ASSEMBLY_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned default '0',");

		sCreateTableSQL.append("  `SEGMENT_NAME` varchar(20) default 'Main',");

		sCreateTableSQL.append("  `ITEM_PK` bigint(20) unsigned default '0',");

		sCreateTableSQL.append("  `QUANTITY_NEEDED` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `QUANTITY_USED` bigint(20) unsigned default '0',");
		sCreateTableSQL.append("  `QUANTITY_SPOILED` bigint(20) unsigned default '0',");

		sCreateTableSQL.append("  PRIMARY KEY  (`ASSEMBLY_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_client_permits`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @throws SQLException
	 */
	public void createValidClientPermitsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_clients_permits`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_clients_permits` (");
		sCreateTableSQL.append("  `PERMIT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned default '0',");

		sCreateTableSQL.append("  `CLIENT_NAME` varchar(100) default '0',");

		sCreateTableSQL.append("  `PERMIT_TYPE` varchar(50) default 'Utility',");
		sCreateTableSQL.append("  `PERMIT_NUMBER` varchar(10) default '',");
		sCreateTableSQL.append("  `CAPS_NUMBER` varchar(20) default '',");
		sCreateTableSQL.append("  `MAILER_ID` varchar(20) default '',");
		sCreateTableSQL.append("  `HELD_AT_USPS_LOCATION` varchar(50) default '',");

		sCreateTableSQL.append("  `CLIENT_MAILING_ADDRESS` varchar(1000) default '',");
		sCreateTableSQL.append("  `CLIENT_MAIN_PHONE` varchar(15) default '',");

		sCreateTableSQL.append("  PRIMARY KEY  (`PERMIT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * This method creates the
	 * `client_" + _sClientPrefix + "_available_processes_options` table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidAvailableProcessOptionsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_available_processes_options`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_available_processes_options` (");
		sCreateTableSQL.append("  `AVAILABLE_PROCESS_OPTION_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `AVAILABLE_PROCESS_PK` bigint(20) unsigned,");

		sCreateTableSQL.append("  `OPTION_DESCRIPTION` varchar(200) default '',");
		sCreateTableSQL.append("  `OPTION_MAP_NAME` varchar(200) default '',");
		sCreateTableSQL.append("  `OPTION_DEFAULT_VALUE` varchar(100) default '',");

		sCreateTableSQL.append("  `OPTION_NAME_ONE` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE_ONE` varchar(100) default '',");

		sCreateTableSQL.append("  `OPTION_NAME_TWO` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE_TWO` varchar(100) default '',");

		sCreateTableSQL.append("  `OPTION_NAME_THREE` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE_THREE` varchar(100) default '',");

		sCreateTableSQL.append("  `OPTION_NAME_FOUR` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE_FOUR` varchar(100) default '',");

		sCreateTableSQL.append("  `OPTION_NAME_FIVE` varchar(100) default '',");
		sCreateTableSQL.append("  `OPTION_VALUE_FIVE` varchar(100) default '',");

		sCreateTableSQL.append("  `ADMIN_ONLY_FLAG` TINYINT unsigned default '0',");

		sCreateTableSQL.append("  PRIMARY KEY  (`AVAILABLE_PROCESS_OPTION_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_vendors` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidVendorsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_vendors`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_vendors` (");
		sCreateTableSQL.append("  `VENDOR_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `VENDOR_NAME` varchar(100) default '0',");
		sCreateTableSQL.append("  PRIMARY KEY  (`VENDOR_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" + _sClientPrefix + "_vendors` values (null, 'Sebis')");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" + _sClientPrefix + "_vendors` values (null, 'NDS')");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" + _sClientPrefix + "_vendors` values (null, 'DMS')");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" + _sClientPrefix + "_vendors` values (null, 'OB')");

	}

	/**
	 * The method that creates the
	 * `client_" + sClientPrefix + "_processes_totals` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidProcessesTotalsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes_totals`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes_totals` (");
		sCreateTableSQL.append("  `TOTALS_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned NOT NULL,");
		sCreateTableSQL.append("  `TOTAL_TITLE` varchar(200),");
		sCreateTableSQL.append("  `TOTAL_VALUE` varchar(200),");
		sCreateTableSQL.append("  PRIMARY KEY  (`TOTALS_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + sClientPrefix + "_processes_totals` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidProcessesTotalsSystemTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_processes_totals_system`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_processes_totals_system` (");
		sCreateTableSQL.append("  `TOTALS_SYSTEM_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned NOT NULL,");
		sCreateTableSQL.append("  `TOTAL_TITLE` varchar(200),");
		sCreateTableSQL.append("  `TOTAL_VALUE` varchar(200),");
		sCreateTableSQL.append("  PRIMARY KEY  (`TOTALS_SYSTEM_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the `client_" + _sClientPrefix + "_preferences`
	 * table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidPreferencesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_preferences`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_preferences` (");
		sCreateTableSQL.append("  `PREF_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `PREF_NAME` varchar(100) NOT NULL,");
		sCreateTableSQL.append("  `PREF_CATEGORY` varchar(50) NOT NULL,");
		sCreateTableSQL.append("  `PREF_ORDER` TINYINT unsigned NOT NULL,");
		sCreateTableSQL.append("  `PREF_TYPE` varchar(50) NOT NULL,");
		sCreateTableSQL.append("  `PREF_VALUE_STRING` varchar(250),");
		sCreateTableSQL.append("  `PREF_VALUE_NUMBER` bigint(20),");
		sCreateTableSQL.append("  `PREF_VALUE_VALID` bigint(20) unsigned,");
		sCreateTableSQL.append("  `PREF_VALID_TABLE_NAME` varchar(250),");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  PRIMARY KEY  (`PREF_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `client_" + sClientPrefix + "_privs` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidPrivsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_privs`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_privs` (");
		sCreateTableSQL.append("  `PRIV_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `ROLE_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) NOT NULL default '0',");
		sCreateTableSQL.append("  `RESOURCE_NAME` text NOT NULL,");
		sCreateTableSQL.append("  PRIMARY KEY  (`PRIV_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `client_" + _sClientPrefix + "_roles` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createValidRolesTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_roles`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_roles` (");
		sCreateTableSQL.append("  `ROLE_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `ROLE_NAME` varchar(100) NOT NULL default '',");
		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");
		sCreateTableSQL.append("  PRIMARY KEY  (`ROLE_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB ");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());
	}

	/**
	 * The method that creates the `client_" + _sClientPrefix + "_users` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createUserTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());
		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users` (");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `ID_NAME` varchar(30) NOT NULL default '',");

		if (_sClientPrefix.equalsIgnoreCase("TMAG"))
		{
			sCreateTableSQL.append("  `PASSWORD` BLOB,");
			sCreateTableSQL.append("  `PASSWORD_SEBIS` varchar(50) default '',");
		}
		else
		{
			sCreateTableSQL.append("  `PASSWORD` varchar(30) NOT NULL default '',");
		}

		sCreateTableSQL.append("  `FIRST_NAME` varchar(50) NOT NULL default '',");
		sCreateTableSQL.append("  `LAST_NAME` varchar(50) NOT NULL default '',");

		if (_sClientPrefix.equalsIgnoreCase("TMAG"))
		{

			sCreateTableSQL.append("  `E_MAIL_ADDRESS` varchar(100) default '',");
			sCreateTableSQL.append("  `E_MAIL_VERIFY_CODE` varchar(30) default '',");

			sCreateTableSQL.append("  `SIGN_UP_DATETIME` DATETIME,");

			sCreateTableSQL.append("  `PHONE_AREACODE` varchar(3) default '',");
			sCreateTableSQL.append("  `PHONE_EXCHANGE` varchar(3) default '',");
			sCreateTableSQL.append("  `PHONE_SUFFIX` varchar(4) default '',");

		}

		sCreateTableSQL.append("  `LAST_ONLINE_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `ROLE_PK` bigint(20) unsigned NOT NULL default '0',");

		if (_sClientPrefix.equalsIgnoreCase("TMAG"))
		{

			sCreateTableSQL.append("  `FAILED_LOGINS` TINYINT unsigned default '0',");

			sCreateTableSQL.append("  `FLAG_STOCK_NOTIFY` TINYINT unsigned default '1',");
			sCreateTableSQL.append("  `FLAG_PROCESS_NOTIFY` TINYINT unsigned default '1',");
			sCreateTableSQL.append("  `FLAG_APPROVE_NOTIFY` TINYINT unsigned default '1',");
			sCreateTableSQL.append("  `FLAG_EPAY_NOTIFY` TINYINT unsigned default '0',");

			sCreateTableSQL.append("  `ELECTRONIC_DELIVERY` varchar(50) default 'E-Mail Notification Only',");
			sCreateTableSQL.append("  `FLAG_EMAIL_BILLS_NOTICES_ONLY` TINYINT unsigned default '1',");
			sCreateTableSQL.append("  `FLAG_EMAIL_MUNI` TINYINT unsigned default '0',");
			sCreateTableSQL.append("  `FLAG_EMAIL_THIRD_PARTY` TINYINT unsigned default '0',");

			sCreateTableSQL.append("  `ADDED_MODIFIED_DATETIME` DATETIME,");

		}

		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned NOT NULL default '1',");
		sCreateTableSQL.append("  PRIMARY KEY  (`USER_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + _sClientPrefix + "_users_accounts` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createUserAccountsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_accounts`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_accounts` (");
		sCreateTableSQL.append("  `ACCOUNT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0', ");
		sCreateTableSQL.append("  `SERVICE_ADDRESS` varchar(200) default '',");
		sCreateTableSQL.append("  `ACCOUNT_NUMBER_CONSOLIDATED` varchar(50) default '',");
		sCreateTableSQL.append("  `ADDED_MODIFIED_DATETIME` DATETIME,");
		sCreateTableSQL.append("  `FLAG_IS_ACTIVE` TINYINT unsigned  NOT NULL default '1',");

		sCreateTableSQL.append("  PRIMARY KEY  (`ACCOUNT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + _sClientPrefix + "_users_accounts` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createCommonDocumentsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_repository_docs`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_repository_docs` (");
		sCreateTableSQL.append("  `DOCUMENT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `FILE_NAME` varchar(50) default '',");
		sCreateTableSQL.append("  `FILE_DESCRIPTION` varchar(400) default '',");
		sCreateTableSQL.append("  `FILE_SIZE` varchar(20) default '',");
		sCreateTableSQL.append("  `FILE_TYPE` varchar(40) default '',");
		sCreateTableSQL.append("  `FILE_VISIBILITY` varchar(20) default 'All',");
		sCreateTableSQL.append("  `FILE_UPLOADED_DATETIME` DATETIME default null,");
		sCreateTableSQL.append("  PRIMARY KEY  (`DOCUMENT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		// XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" +
		// _sClientPrefix
		// +
		// "_repository_docs` values (null, 2, 2, 'Test_PDF', 'Test PDF', '10 K', 'text/pdf', now() )");

	}

	/**
	 * This method creates the `client_" + _sClientPrefix + "_export_docs`
	 * table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createExportDocumentsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_export_docs`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_export_docs` (");
		sCreateTableSQL.append("  `EXPORT_DOCUMENT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `FILE_NAME` varchar(50) default '',");
		sCreateTableSQL.append("  `FILE_DESCRIPTION` varchar(400) default '',");
		sCreateTableSQL.append("  `FILE_SIZE` varchar(20) default '',");
		sCreateTableSQL.append("  `FILE_TYPE` varchar(40) default '',");
		sCreateTableSQL.append("  `START_DATETIME` DATETIME default null,");
		sCreateTableSQL.append("  `END_DATETIME` DATETIME default null,");
		sCreateTableSQL.append("  `FILE_UPLOADED_DATETIME` DATETIME default null,");
		sCreateTableSQL.append("  PRIMARY KEY  (`EXPORT_DOCUMENT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		// XSaLTDataUtils.executeSQL(_oConnectionXApp, "INSERT INTO `client_" +
		// _sClientPrefix
		// +
		// "_repository_docs` values (null, 2, 2, 'Test_PDF', 'Test PDF', '10 K', 'text/pdf', now() )");

	}

	/**
	 * This method creates the `client_" + _sClientPrefix + "_users_log` table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createEPayLogTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_log`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_log` (");
		sCreateTableSQL.append("  `USER_LOG_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_ADMIN_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  `CLASS_NAME` varchar(200) default '',");
		sCreateTableSQL.append("  `EVENT_DESCRIPTION` varchar(400) default '',");
		sCreateTableSQL.append("  `EVENT_DATE` DATETIME, ");
		sCreateTableSQL.append("  PRIMARY KEY  (`USER_LOG_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + _sClientPrefix + "_users_accounts` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createUserAvailableUtilityBillsTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_bills`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_bills` (");
		sCreateTableSQL.append("  `USER_BILL_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned,");

		sCreateTableSQL.append("  `BILL_TYPE` varchar(100) default '',");

		sCreateTableSQL.append("  `SERVICE_ADDRESS` varchar(200) default '',");
		sCreateTableSQL.append("  `ACCOUNT_NUMBER_CONSOLIDATED` varchar(50) default '',");

		sCreateTableSQL.append("  `BILL_DATE` DATE,");
		sCreateTableSQL.append("  `BILL_DUE_DATE` DATE,");
		sCreateTableSQL.append("  `AMOUNT_PAST_DUE`  DOUBLE (15,2) ,");
		sCreateTableSQL.append("  `AMOUNT_CURRENT_CHARGES`  DOUBLE (15,2) ,");
		sCreateTableSQL.append("  `AMOUNT_TOTAL_DUE`  DOUBLE (15,2) ,");
		sCreateTableSQL.append("  `AMOUNT_TOTAL_DUE_AFTER`  DOUBLE (15,2) ,");

		sCreateTableSQL.append("  `FLAG_BILL_IS_VISIBLE` TINYINT unsigned default '1',");
		sCreateTableSQL.append("  `FLAG_BILL_IS_ACH` TINYINT unsigned default '0',");
		sCreateTableSQL.append("  `FLAG_BILL_IS_PAID` TINYINT unsigned default '0',");

		sCreateTableSQL.append("  `EPAY_TRANSACTION_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  `USER_PK_PAID` bigint(20) unsigned,");
		sCreateTableSQL.append("  `BILL_PAID_AMOUNT`  DOUBLE (15,2) ,");
		sCreateTableSQL.append("  `BILL_PAID_DATE` DATETIME, ");
		sCreateTableSQL.append("  `BILL_PAID_LAST_FOUR` varchar(4) default '',");
		sCreateTableSQL.append("  `BILL_PAID_CONF_CODE` varchar(50) default '',");

		sCreateTableSQL.append("  PRIMARY KEY  (`USER_BILL_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		XSaLTDataUtils
				.executeSQL(_oConnectionXApp, "CREATE INDEX ACCOUNT_NUMBER_CONSOLIDATED_INDEX ON client_" + _sClientPrefix + "_users_bills (ACCOUNT_NUMBER_CONSOLIDATED(50))");

		sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_bills_detail`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_bills_detail` (");
		sCreateTableSQL.append("  `USER_BILL_PK` bigint(20) unsigned NOT NULL,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned,");
		sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned,");

		sCreateTableSQL.append("  `DIRECT_PAYMENT_TO` varchar(200) default '',");
		sCreateTableSQL.append("  `PAYMENT_TYPE` varchar(200) default '',");
		sCreateTableSQL.append("  `PAYMENT_GENERAL_NOTES` varchar(3000) default '',");

		sCreateTableSQL.append("  PRIMARY KEY  (`USER_BILL_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + _sClientPrefix + "_users_accounts` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @param _bAddTestData
	 *            Flag if test data should be added
	 * @throws SQLException
	 */
	public void createUserCardsTable(Connection _oConnectionXApp, String _sClientPrefix, boolean _bAddTestData) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_payment_cards`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_payment_cards` (");
		sCreateTableSQL.append("  `USER_CARD_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");

		sCreateTableSQL.append("  `NAME_ON_CARD` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_1` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_2` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_CITY` varchar(100) default '',");
		sCreateTableSQL.append("  `BILLING_STATE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_ZIP` varchar(5) default '',");
		sCreateTableSQL.append("  `BILLING_PLUS_4` varchar(4) default '',");

		sCreateTableSQL.append("  `BILLING_PHONE_AREACODE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_PHONE_EXCHANGE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_PHONE_SUFFIX` varchar(4) default '',");

		sCreateTableSQL.append("  `CARD_NUMBER` BLOB,");
		sCreateTableSQL.append("  `CARD_CCV` BLOB,");
		sCreateTableSQL.append("  `CARD_EXP_MONTH` varchar(2) default '',");
		sCreateTableSQL.append("  `CARD_EXP_YEAR` varchar(4) default '',");

		sCreateTableSQL.append("  `ADDED_MODIFIED_DATETIME` DATETIME,");

		sCreateTableSQL.append("  PRIMARY KEY  (`USER_CARD_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		if (_bAddTestData)
		{

			try
			{

				String sSQL = "INSERT INTO `client_" + _sClientPrefix + "_users_payment_cards` VALUES (null, '2', '2', "
						+ "'STEPHAN P COSSETTE', '2 Elm Creek Drive', 'Apt 105', 'Elmhurst', 'IL', '60126', '0000', '312', '636', '8213', "
						+ "'Visa', AES_ENCRYPT('1111-2222-3333-4444', '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "'), AES_ENCRYPT('123', '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "'), "
						+ "'09', '2010')";

				XSaLTDataUtils.executeSQL(_oConnectionXApp, sSQL);

				sSQL = "SELECT AES_DECRYPT(CARD_NUMBER, '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "') AS UNENCRYPTED_CARD_NUMBER FROM client_" + _sClientPrefix + "_users_payment_cards";
				ResultSet oRs = XSaLTDataUtils.querySQL(_oConnectionXApp, sSQL);
				while (oRs.next())
				{
					String sCardNo = oRs.getString("UNENCRYPTED_CARD_NUMBER");
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sCardNo);
				}

				sSQL = "SELECT AES_DECRYPT(CARD_CCV, '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "') AS UNENCRYPTED_CARD_CCV FROM client_" + _sClientPrefix + "_users_payment_cards";
				oRs = XSaLTDataUtils.querySQL(_oConnectionXApp, sSQL);
				while (oRs.next())
				{
					String sCardCCV = oRs.getString("UNENCRYPTED_CARD_CCV");
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sCardCCV);
				}
			}
			catch (Exception e)
			{
				XSaLTGenericLogger.error("", e);
			}

		}

	}

	/**
	 * This method creates the
	 * `client_" + _sClientPrefix + "_users_transactions` table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @throws SQLException
	 */
	public void createUserTransactionTable(Connection _oConnectionXApp, String _sClientPrefix) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_transactions`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_transactions` (");
		sCreateTableSQL.append("  `EPAY_TRANSACTION_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");

		sCreateTableSQL.append("  `NAME_ON_CARD` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_1` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_2` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_CITY` varchar(100) default '',");
		sCreateTableSQL.append("  `BILLING_STATE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_ZIP` varchar(5) default '',");
		sCreateTableSQL.append("  `BILLING_PLUS_4` varchar(4) default '',");

		sCreateTableSQL.append("  `BILLING_PHONE` varchar(20) default '',");
		sCreateTableSQL.append("  `BILLING_EMAIL` varchar(200) default '',");

		sCreateTableSQL.append("  `LAST_FOUR_CC_NO` varchar(4),");
		sCreateTableSQL.append("  `CARD_CHARGED_AMOUNT` DOUBLE (15,2) NOT NULL default '0',");
		sCreateTableSQL.append("  `CARD_CHARGED_DATE` DATETIME,");
		sCreateTableSQL.append("  `CARD_TRANSACTION_NO` varchar(100),");
		sCreateTableSQL.append("  `FLAG_TRANSACTION_BATCHED` TINYINT unsigned default '0',");
		sCreateTableSQL.append("  `BATCH_PK` bigint(20) unsigned,");

		sCreateTableSQL.append("  PRIMARY KEY  (`EPAY_TRANSACTION_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

	}

	/**
	 * The method that creates the
	 * `client_" + _sClientPrefix + "_users_accounts` table
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sClientPrefix
	 *            Prefix for the given client
	 * @param _bAddTestData
	 *            Flag if test data should be added
	 * @throws SQLException
	 */
	public void createACHAccountTable(Connection _oConnectionXApp, String _sClientPrefix, boolean _bAddTestData) throws SQLException
	{
		StringBuffer sDropTableSQL = new StringBuffer();
		sDropTableSQL.append("DROP TABLE IF EXISTS `client_" + _sClientPrefix + "_users_payment_ach`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sDropTableSQL.toString());

		StringBuffer sCreateTableSQL = new StringBuffer();
		sCreateTableSQL.append("CREATE TABLE `client_" + _sClientPrefix + "_users_payment_ach` (");
		sCreateTableSQL.append("  `USER_ACCT_PK` bigint(20) unsigned NOT NULL auto_increment,");
		sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned NOT NULL default '0',");
		sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned NOT NULL default '0',");

		sCreateTableSQL.append("  `NAME_ON_ACCOUNT` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_1` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_ADDRESS_2` varchar(200) default '',");
		sCreateTableSQL.append("  `BILLING_CITY` varchar(100) default '',");
		sCreateTableSQL.append("  `BILLING_STATE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_ZIP` varchar(5) default '',");
		sCreateTableSQL.append("  `BILLING_PLUS_4` varchar(4) default '',");

		sCreateTableSQL.append("  `BILLING_PHONE_AREACODE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_PHONE_EXCHANGE` varchar(3) default '',");
		sCreateTableSQL.append("  `BILLING_PHONE_SUFFIX` varchar(4) default '',");

		sCreateTableSQL.append("  `ACCOUNT_OPERATION` varchar(20) default '',");
		sCreateTableSQL.append("  `ACCOUNT_CHECKING_OR_SAVINGS` varchar(20) default '',");
		sCreateTableSQL.append("  `BANK_NAME` varchar(150) default '',");
		sCreateTableSQL.append("  `BANK_CHECKING_ROUTING_NUMBER` BLOB,");
		sCreateTableSQL.append("  `BANK_CHECKING_ACCOUNT_NUMBER` BLOB,");
		sCreateTableSQL.append("  `BANK_SAVINGS_ACCOUNT_NUMBER` BLOB,");

		sCreateTableSQL.append("  `ACCOUNT_NUMBERS_TO_ASSOCIATE` varchar(1000),");
		sCreateTableSQL.append("  `ADDED_MODIFIED_DATETIME` DATETIME,");

		sCreateTableSQL.append("  PRIMARY KEY  (`USER_ACCT_PK`)");
		sCreateTableSQL.append(") ENGINE=InnoDB");

		XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		if (_bAddTestData)
		{

			try
			{

				String sSQL = "INSERT INTO `client_" + _sClientPrefix + "_users_payment_ach` VALUES (null, '2', '2', "
						+ "'STEPHAN P COSSETTE', '2 Elm Creek Drive', 'Apt 105', 'Elmhurst', 'IL', '60126', '0000', '312', '636', '8213', "
						+ "'Test', 'Checking', 'Chase', AES_ENCRYPT('123456789', '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "'), AES_ENCRYPT('987654321', '"
						+ XSaLTDataUtils.XS_ENCRYPT_KEY + "'), " + "AES_ENCRYPT('1122334455', '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "'), now())";

				XSaLTDataUtils.executeSQL(_oConnectionXApp, sSQL);

				sSQL = "SELECT AES_DECRYPT(BANK_CHECKING_ROUTING_NUMBER, '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "') AS UNENCRYPTED_BANK_CHECKING_ROUTING_NUMBER FROM client_"
						+ _sClientPrefix + "_users_payment_ach";
				ResultSet oRs = XSaLTDataUtils.querySQL(_oConnectionXApp, sSQL);
				while (oRs.next())
				{
					String sCardNo = oRs.getString("UNENCRYPTED_BANK_CHECKING_ROUTING_NUMBER");
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sCardNo);
				}

				sSQL = "SELECT AES_DECRYPT(BANK_CHECKING_ACCOUNT_NUMBER, '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "') AS UNENCRYPTED_BANK_CHECKING_ACCOUNT_NUMBER FROM client_"
						+ _sClientPrefix + "_users_payment_ach";
				oRs = XSaLTDataUtils.querySQL(_oConnectionXApp, sSQL);
				while (oRs.next())
				{
					String sCardNo = oRs.getString("UNENCRYPTED_BANK_CHECKING_ACCOUNT_NUMBER");
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sCardNo);
				}

				sSQL = "SELECT AES_DECRYPT(BANK_SAVINGS_ACCOUNT_NUMBER, '" + XSaLTDataUtils.XS_ENCRYPT_KEY + "') AS UNENCRYPTED_BANK_SAVINGS_ACCOUNT_NUMBER FROM client_"
						+ _sClientPrefix + "_users_payment_ach";
				oRs = XSaLTDataUtils.querySQL(_oConnectionXApp, sSQL);
				while (oRs.next())
				{
					String sCardNo = oRs.getString("UNENCRYPTED_BANK_SAVINGS_ACCOUNT_NUMBER");
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sCardNo);
				}

			}
			catch (Exception e)
			{
				XSaLTGenericLogger.error("", e);
			}

		}

	}

	/**
	 * This method returns the standard XApp privileges.
	 * 
	 * @param _bIsTMAG
	 *            Flag if privileges are for the TMAGateway
	 * @param _bOverrideInstanceQualifier
	 *            Flag if instance flag should be overridden
	 * @return ArrayList of tasks user has access to
	 */
	public ArrayList<String> getStandardXAppPrivs(boolean _bIsTMAG, boolean _bOverrideInstanceQualifier)
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("ALL_USERS");

		oPrivsArrayList.add("com.tma.xsalttasks.DefaultTask");

		if (_bOverrideInstanceQualifier || _bIsTMAG)
		{
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.test*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.DefaultTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.ErrorTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.epay*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.shared.LoginTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.shared.LogoutTask*");

		}

		if (_bOverrideInstanceQualifier || !_bIsTMAG)
		{
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.DefaultTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.ErrorTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.help.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.online*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.common.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.pet.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.vehicle.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.LoginTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.shared.LogoutTask*");
		}

		return oPrivsArrayList;
	}

	/**
	 * This method returns a List of privileges for an administrator.
	 * 
	 * @param _bIsTMAG
	 *            Flag if this is the TMAGateway application
	 * @return List of tasks user has access to
	 */
	public ArrayList<String> getAdminPrivsArrayList(boolean _bIsTMAG)
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("ADMIN");

		if (_bIsTMAG)
		{
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.admin.SearchViewAccountTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.admin.ViewMasterAccountTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.approval.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.inventory.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.process.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.reports.CreateACHReportTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.reports.CreateApplicationReportTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.reports.CreateEPayReportTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.reports.CreateEPayBatchFileTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.reports.SetExportReportCriteriaTask*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.shared.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.users.*");
		}
		else
		{
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.pet.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.reports.*");
			oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.vehicle.*");
		}
		return oPrivsArrayList;
	}

	/**
	 * This method returns a List of privileges for a READ_WRITE user.
	 * 
	 * @return A List of privileges for a READ_WRITE user
	 */
	public ArrayList<String> getRWPrivsArrayList()
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("READ_WRITE");

		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.pet.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.reports.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.vehicle.*");

		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.DefaultTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.DefaultFillinsTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.ManualDateSetTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.PrintFillinsTask*");

		return oPrivsArrayList;
	}

	/**
	 * This method returns a List of privileges for a READ_WRITE_BANK user.
	 * 
	 * @return A List of privileges for a READ_WRITE_BANK user
	 */
	public ArrayList<String> getBankRWPrivsArrayList()
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("READ_WRITE_BANK");

		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.pet.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.reports.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.vehicle.*");

		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.DefaultTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.DefaultFillinsTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.ManualDateSetTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.ImportBankFilesTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.PrintFillinsTask*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.admin.ProcessBatchesTask*");

		return oPrivsArrayList;
	}

	/**
	 * This method returns a List of privileges for a READ_ONLY user.
	 * 
	 * @return A List of privileges for a READ_ONLY user
	 */
	public ArrayList<String> getReadOnlyPrivsArrayList()
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("READ_ONLY");

		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.pet.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.reports.*");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaXapp.vehicle.*");

		return oPrivsArrayList;
	}

	/**
	 * This method returns a List of privileges for an ONLINE user.
	 * 
	 * @return A List of privileges for an ONLINE user
	 */
	public ArrayList<String> getOnlinePrivsArrayList()
	{
		ArrayList<String> oPrivsArrayList = new ArrayList<String>();
		oPrivsArrayList.add("ONLINE");
		oPrivsArrayList.add("com.tma.xsalttasks.tmaGateway.epay*");

		return oPrivsArrayList;
	}

	/**
	 * The method that gets the standard address type for TMAXApp
	 * 
	 * @return The ArrayList of address types
	 */
	public ArrayList<String> getStandardAddressTypesArrayList()
	{
		ArrayList<String> oStandardAddressTypesArrayList = new ArrayList<String>();
		oStandardAddressTypesArrayList.add("- Unknown");
		oStandardAddressTypesArrayList.add("Individual");
		oStandardAddressTypesArrayList.add("Couple");
		oStandardAddressTypesArrayList.add("Business");
		oStandardAddressTypesArrayList.add("Resident");
		oStandardAddressTypesArrayList.add("Elderly");
		return oStandardAddressTypesArrayList;
	}

	/**
	 * This method gets a List of standard HTML colors.
	 * 
	 * @return A List of standard HTML colors
	 */
	public ArrayList<String> getStandardHtmlColorsArrayList()
	{
		ArrayList<String> oStandardHtmlColorsArrayList = new ArrayList<String>();
		oStandardHtmlColorsArrayList.add("Aqua");
		oStandardHtmlColorsArrayList.add("White");
		oStandardHtmlColorsArrayList.add("Yellow");
		oStandardHtmlColorsArrayList.add("Lime");
		oStandardHtmlColorsArrayList.add("Fuchsia");
		oStandardHtmlColorsArrayList.add("Teal");
		oStandardHtmlColorsArrayList.add("Olive");
		oStandardHtmlColorsArrayList.add("Gray");
		oStandardHtmlColorsArrayList.add("Silver");
		return oStandardHtmlColorsArrayList;
	}

	/**
	 * This method grants privs for Tomcats connection
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection (SYSTEM)
	 * @throws SQLException
	 */
	public void grantPrivsForTomcat(Connection _oConnectionXApp) throws SQLException
	{
		StringBuffer sGrantSQL = new StringBuffer();
		sGrantSQL.append("GRANT ALL PRIVILEGES ON *.* TO tomcat@localhost IDENTIFIED BY 'tomcat' WITH GRANT OPTION");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sGrantSQL.toString());
	}

	/**
	 * This method grants privileges for the given client and role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addXAppStandardPrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{

		boolean bIsTMAG = false;
		if (_sClientPrefix.equalsIgnoreCase("tmag"))
		{
			bIsTMAG = true;
		}

		ArrayList<String> oStandardPrivsArrayList = getStandardXAppPrivs(bIsTMAG, false);
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method grants administrator privileges for the given client and
	 * role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addAdminPrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{

		boolean bIsTMAG = false;
		if (_sClientPrefix.equalsIgnoreCase("tmag"))
		{
			bIsTMAG = true;
		}

		ArrayList<String> oStandardPrivsArrayList = getAdminPrivsArrayList(bIsTMAG);
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method grants READ_WRITE privileges for the given client and role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addRWPrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardPrivsArrayList = getRWPrivsArrayList();
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method grants READ_WRITE_BANK privileges for the given client and
	 * role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addBankRWPrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardPrivsArrayList = getBankRWPrivsArrayList();
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method grants READ_ONLY privileges for the given client and role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addReadOnlyPrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardPrivsArrayList = getReadOnlyPrivsArrayList();
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method grants ONLINE privileges for the given client and role.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sRolePk
	 *            Role ID to add privileges for
	 * @param _sClientPk
	 *            Client ID to add privileges for
	 * @param _sClientPrefix
	 *            Client prefix to add privileges for
	 * @throws SQLException
	 */
	public void addOnlinePrivsForClient(Connection _oConnectionXApp, String _sRolePk, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardPrivsArrayList = getOnlinePrivsArrayList();
		for (int i = 0; i < oStandardPrivsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_privs` values (null, '" + _sRolePk + "', '" + _sClientPk + "', '"
					+ oStandardPrivsArrayList.get(i).toString() + "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method add the address types for a given client and role
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sRolePk
	 *            The role_pk to assign address types to
	 * @param _sClientPk
	 *            The client_pk to assign address types to
	 * @throws SQLException
	 */
	public void addStandardAddressTypesForClient(Connection _oConnectionXApp, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardAddressTypesArrayList = getStandardAddressTypesArrayList();
		for (int i = 0; i < oStandardAddressTypesArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_address_types` values (null, '" + _sClientPk + "', '"
					+ oStandardAddressTypesArrayList.get(i).toString() + "', '1')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method adds the standard HTML colors for a given client.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _sClientPk
	 *            The client_pk to assign colors to
	 * @param _sClientPrefix
	 *            The client_prefix to assign colors to
	 * @throws SQLException
	 */
	public void addStandardHtmlColorsForClient(Connection _oConnectionXApp, String _sClientPk, String _sClientPrefix) throws SQLException
	{
		ArrayList<String> oStandardHtmlColorsArrayList = getStandardHtmlColorsArrayList();
		for (int i = 0; i < oStandardHtmlColorsArrayList.size(); i++)
		{
			StringBuffer sInsertTableSQL = new StringBuffer();
			sInsertTableSQL.append("insert into `client_" + _sClientPrefix + "_html_colors` values (null, '" + _sClientPk + "', '" + oStandardHtmlColorsArrayList.get(i).toString()
					+ "')");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}
	}

	/**
	 * This method add the standard dynamic fields types for a given client and
	 * role
	 * 
	 * @param _oConnectionXApp
	 *            The pooled/transacted connection
	 * @param _sRolePk
	 *            The role_pk to assign dynamic fields type to
	 * @param _sClientPk
	 *            The client_pk to assign dynamic fields type to
	 * @throws SQLException
	 */
	public int addStandardDynfieldsForClient(Connection _oConnectionXApp, String _sClientPk, String _sClientPrefix, boolean _bCountOnly) throws SQLException
	{

		int nCountOfFieldTypes = 0;

		StringBuffer sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'TINYINT', 'SMALLINT', 'TINYINT', 'Number (-128 to 127)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'SMALLINT', 'SMALLINT', 'SMALLINT', 'Number (-32768 to 32767)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'MEDIUMINT', 'BIGINT', 'MEDIUMINT', 'Number (-8388608 to 8388607)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'INT', 'BIGINT', 'INT', 'Number (-2147483648 to 2147483647)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'BIGINT', 'BIGINT', 'BIGINT', 'A large integer. The range is -9223372036854775808 to 9223372036854775807.', '0')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'DOUBLE', 'DOUBLE', 'DOUBLE (15,2)', 'Number (-99999.00 to 99999.00)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'TINYTEXT', 'STRING', 'TINYTEXT', 'Text (less than 255 characters)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'TEXT', 'STRING', 'TEXT', 'Text (less than 65,535 characters)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk + "', 'DATE', 'DATE', 'DATE', 'Date (XX/XX/XXXX)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'DATETIME', 'DATETIME', 'DATETIME', 'Datetime (XX/XX/XXXX XX:XX:XX)', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		sInsertTableSQL = new StringBuffer();
		sInsertTableSQL.append("insert into client_" + _sClientPrefix + "_field_types values (null, '" + _sClientPk
				+ "', 'REQUIREDTEXT', 'STRING', 'TINYTEXT', 'Text (less than 255 characters) - SPECIAL', '1')");
		nCountOfFieldTypes = nCountOfFieldTypes + 1;
		if (!_bCountOnly)
		{
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sInsertTableSQL.toString());
		}

		return nCountOfFieldTypes;

	}

	/**
	 * This method creates the TMAGateway inventory item table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _bDropOnly
	 *            Flag if table should be dropped only
	 * @throws SQLException
	 */
	public void createTMAGatewayInventoryTables(Connection _oConnectionXApp, boolean _bDropOnly) throws SQLException
	{
		StringBuffer sSqlBuffer = new StringBuffer();
		sSqlBuffer.append("DROP TABLE IF EXISTS `client_tmag_inventory`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sSqlBuffer.toString());

		if (!_bDropOnly)
		{

			StringBuffer sCreateTableSQL = new StringBuffer();
			sCreateTableSQL.append("CREATE TABLE `client_tmag_inventory` (");

			sCreateTableSQL.append("  `ITEM_PK` bigint(20) unsigned NOT NULL auto_increment,");
			sCreateTableSQL.append("  `ITEM_QB_NO` varchar(25),");
			sCreateTableSQL.append("  `ITEM_NAME` varchar(50),");
			sCreateTableSQL.append("  `ITEM_TYPE` varchar(50),");
			sCreateTableSQL.append("  `ITEM_DESCRIPTION` varchar(200),");

			sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `QUANTITY_ON_HAND` bigint(20),");
			sCreateTableSQL.append("  `QUANTITY_ALLOCATED` bigint(20),");
			sCreateTableSQL.append("  `QUANTITY_TRIGGER` bigint(20) unsigned,");
			sCreateTableSQL.append("  `QUANTITY_TO_ORDER` bigint(20) unsigned,");

			sCreateTableSQL.append("  `VENDOR_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `PIECE_WEIGHT`  DOUBLE (15,5) ,");
			sCreateTableSQL.append("  `PIECE_THICKNESS`  DOUBLE (15,5) ,");
			sCreateTableSQL.append("  `PIECE_HEIGHT`  DOUBLE (15,5) ,");
			sCreateTableSQL.append("  `PIECE_WIDTH`  DOUBLE (15,5) ,");

			sCreateTableSQL.append("  `ITEM_NOTES` varchar(1000),");
			sCreateTableSQL.append("  `ITEM_ACTIVE_FLAG` TINYINT unsigned default '1',");

			sCreateTableSQL.append("  `PIECE_HANDLING_ACTION` varchar(50) default 'TAKE NO ACTION',");
			sCreateTableSQL.append("  `PIECE_HANDLING_ACTION_DETAIL` varchar(2000),");
			sCreateTableSQL.append("  `PIECE_HANDLING_UDATEZ` DATE,");

			sCreateTableSQL.append("  PRIMARY KEY  (`ITEM_PK`)");

			sCreateTableSQL.append(") ENGINE=InnoDB");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		}

	}

	/**
	 * This method creates the TMAGateway inventory count table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _bDropOnly
	 *            Flag if table should be dropped only
	 * @throws SQLException
	 */
	public void createTMAGatewayInventoryCountTables(Connection _oConnectionXApp, boolean _bDropOnly) throws SQLException
	{
		StringBuffer sSqlBuffer = new StringBuffer();
		sSqlBuffer.append("DROP TABLE IF EXISTS `client_tmag_inventory_counts`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sSqlBuffer.toString());

		if (!_bDropOnly)
		{

			StringBuffer sCreateTableSQL = new StringBuffer();
			sCreateTableSQL.append("CREATE TABLE `client_tmag_inventory_counts` (");

			sCreateTableSQL.append("  `ITEM_PK` bigint(20) unsigned NOT NULL,");
			sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned NOT NULL,");
			sCreateTableSQL.append("  `EVENT_DESCRIPTION` varchar(50) NOT NULL,");
			sCreateTableSQL.append("  `ENTRY_DESCRIPTION` varchar(50) NOT NULL,");
			sCreateTableSQL.append("  `ENTRY_REMARKS` varchar(200) NOT NULL,");
			sCreateTableSQL.append("  `ENTRY_ADJUSTMENT_AMOUNT` bigint(20) NOT NULL,");
			sCreateTableSQL.append("  `ENTRY_DEBITED_FROM_INVENTORY` TINYINT unsigned default '0',");
			sCreateTableSQL.append("  `ENTRY_DATE` DATETIME NOT NULL");
			sCreateTableSQL.append(") ENGINE=InnoDB");

			XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		}

	}

	/**
	 * This method creates the TMAGateway system email log table
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _bDropOnly
	 *            Flag if table should be dropped only
	 * @throws SQLException
	 */
	public void createTMAGatewayEMailTable(Connection _oConnectionXApp, boolean _bDropOnly) throws SQLException
	{
		StringBuffer sSqlBuffer = new StringBuffer();
		sSqlBuffer.append("DROP TABLE IF EXISTS `client_tmag_system_email_log`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sSqlBuffer.toString());

		if (!_bDropOnly)
		{

			StringBuffer sCreateTableSQL = new StringBuffer();
			sCreateTableSQL.append("CREATE TABLE `client_tmag_system_email_log` (");
			sCreateTableSQL.append("  `EMAIL_PK` bigint(20) unsigned NOT NULL auto_increment,");
			sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `ADDITIONAL_RECIP` varchar(500),");
			sCreateTableSQL.append("  `EMAIL_SUBJECT` varchar(100),");
			sCreateTableSQL.append("  `EMAIL_BODY` varchar(5000),");
			sCreateTableSQL.append("  `EMAIL_DATETIME` DATETIME NOT NULL,");
			sCreateTableSQL.append("  PRIMARY KEY  (`EMAIL_PK`)");
			sCreateTableSQL.append(") ENGINE=InnoDB");

			XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		}

	}

	/**
	 * This method creates the TMAGateway system message table.
	 * 
	 * @param _oConnectionXApp
	 *            Database connection object
	 * @param _bDropOnly
	 *            Flag if table should be dropped only
	 * @throws SQLException
	 */
	public void createTMAGatewaySystemMessageTable(Connection _oConnectionXApp, boolean _bDropOnly) throws SQLException
	{
		StringBuffer sSqlBuffer = new StringBuffer();
		sSqlBuffer.append("DROP TABLE IF EXISTS `client_tmag_system_message_log`");
		XSaLTDataUtils.executeSQL(_oConnectionXApp, sSqlBuffer.toString());

		if (!_bDropOnly)
		{

			StringBuffer sCreateTableSQL = new StringBuffer();
			sCreateTableSQL.append("CREATE TABLE `client_tmag_system_message_log` (");
			sCreateTableSQL.append("  `MESSAGE_PK` bigint(20) unsigned NOT NULL auto_increment,");
			sCreateTableSQL.append("  `PROCESS_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `CLIENT_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `USER_PK` bigint(20) unsigned,");
			sCreateTableSQL.append("  `MESSAGE_TYPE` varchar(100),");
			sCreateTableSQL.append("  `MESSAGE_SUBJECT` varchar(100),");
			sCreateTableSQL.append("  `MESSAGE_BODY` varchar(1000),");
			sCreateTableSQL.append("  `MESSAGE_DATETIME` DATETIME NOT NULL,");
			sCreateTableSQL.append("  PRIMARY KEY  (`MESSAGE_PK`)");
			sCreateTableSQL.append(") ENGINE=InnoDB");
			XSaLTDataUtils.executeSQL(_oConnectionXApp, sCreateTableSQL.toString());

		}

	}

}
