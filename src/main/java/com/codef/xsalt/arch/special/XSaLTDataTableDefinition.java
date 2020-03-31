package com.codef.xsalt.arch.special;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTDataTableDefinition
{
	/**
	 * The table name
	 */
	private String isTableName = null;

	/**
	 * The data columns
	 */
	private HashMap<String, XSaLTDataColumnDefinition> ioDataColumns = null;

	/**
	 * The data columns (convenience collection)
	 */
	private LinkedHashMap<String, String> ioDataColumnsNames = null;
	private LinkedHashMap<String, String> ioDataColumnsAsNames = null;

	private LinkedHashMap<String, String> ioConcatDataColumnsNames = null;

	/**
	 * The join column name
	 */
	private String isJoinColumnName = "";

	private String isPrimaryColumnName = "";

	/**
	 * Primary constructor
	 */
	public XSaLTDataTableDefinition()
	{
		ioDataColumns = new HashMap<String, XSaLTDataColumnDefinition>();
		ioDataColumnsNames = new LinkedHashMap<String, String>();
		ioDataColumnsAsNames = new LinkedHashMap<String, String>();
		ioConcatDataColumnsNames = new LinkedHashMap<String, String>();
	}

	/**
	 * Sets the table name
	 * 
	 * @param _sTableName The table name to set
	 */
	public void setTableName(String _sTableName)
	{
		isTableName = _sTableName;
	}

	/**
	 * This method sets the name of the primary key column.
	 * 
	 * @param _sPrimaryKeyName
	 *            Column name for primary key
	 */
	public void setTablePrimaryKeyName(String _sPrimaryKeyName)
	{
		isPrimaryColumnName = _sPrimaryKeyName;
	}

	/**
	 * This method returns the column name for the primary key.
	 * 
	 * @return The column name for the primary key as a String
	 */
	public String getTablePrimaryKeyName()
	{
		return getTableName() + "." + isPrimaryColumnName;
	}

	/**
	 * Gets the table name
	 * 
	 * @return - The instance table name
	 */
	public String getTableName()
	{
		return isTableName;
	}

	/**
	 * Sets the join field name
	 * 
	 * @param _sJoinColumnName The join field name to set
	 */
	public void setJoinColumn(String _sJoinColumnName)
	{
		isJoinColumnName = _sJoinColumnName;
	}

	/**
	 * Gets the name of the join field
	 * 
	 * @return - The instance table name
	 */
	public String getJoinColumnName()
	{
		return getTableName() + "." + isJoinColumnName;
	}

	/**
	 * Adds a column to the instance of XSaLTDataTableDefinition
	 * 
	 * @param _oColumnDefinition The XSaLTDataColumnDefinition you wish to add to the table
	 */
	public void addColumn(XSaLTDataColumnDefinition _oColumnDefinition)
	{
		ioDataColumns.put(_oColumnDefinition.getDataColumnName(), _oColumnDefinition);
		ioDataColumnsNames.put(_oColumnDefinition.getDataColumnName(), null);
	}

	/**
	 * This method determines if the table includes the given column name.
	 * 
	 * @param _sColumnName
	 *            Column to search for
	 * @return Flag if column exists in the table
	 */
	public boolean hasColumnName(String _sColumnName)
	{
		if (ioDataColumnsNames.containsKey(_sColumnName) || ioDataColumnsAsNames.containsKey(_sColumnName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This method determines if the table includes the given column "AS" name.
	 * 
	 * @param _sColumnName
	 *            Column label to search for
	 * @return Flag if this column is listed in the "AS" map
	 */
	public boolean hasAsColumnName(String _sColumnName)
	{
		if (ioDataColumnsAsNames.containsKey(_sColumnName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This method returns the column label for the given column name.
	 * 
	 * @param _sColumnName
	 *            Column name to search for
	 * @return Column label as a String
	 */
	public String getAsColumnValue(String _sColumnName)
	{
		return ioDataColumnsAsNames.get(_sColumnName);
	}

	/**
	 * Adds a column to the instance of XSaLTDataTableDefinition
	 * 
	 * @param _sColumnName The column name to add
	 * @param _AsColumnName The column name if we're using "AS XXXXX"
	 */
	public void addColumnName(String _sColumnName, String _AsColumnName)
	{
		ioDataColumnsNames.put(_sColumnName, _AsColumnName);
		if (_AsColumnName != null)
		{
			ioDataColumnsAsNames.put(_AsColumnName, _sColumnName);
		}
	}

	/**
	 * This method adds a column to the table concatenated from existing columns.
	 * 
	 * @param _asColumnNames
	 *            Array of column names to concatenate
	 * @param _sAsColumnName
	 *            Name of created column
	 * @param _sConcatenatedWithChar
	 *            Character(s) to put between values of fields (NOT USED)
	 */
	public void addConcatenatedColumns(String[] _asColumnNames, String _sAsColumnName, String _sConcatenatedWithChar)
	{
		StringBuffer sColumnConcats = new StringBuffer("concat(");

		for (int i = 0; i < _asColumnNames.length; i++)
		{
			sColumnConcats.append(_asColumnNames[i] + ", ");
		}

		sColumnConcats.append(")");

		ioConcatDataColumnsNames.put(sColumnConcats.toString(), _sAsColumnName);

	}

	/**
	 * Get the SQL to create the table instance
	 * 
	 * @return - The SQL to create the table instance
	 */
	public String getTableCreateSQLz(String _sTableType)
	{

		if (_sTableType == null)
		{
			_sTableType = "MyISAM";
		}

		StringBuffer _oCreateSqlStringbuffer = new StringBuffer();
		_oCreateSqlStringbuffer.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
		for (Iterator<String> i = ioDataColumns.keySet().iterator(); i.hasNext();)
		{
			String sKey = (String) i.next();
			XSaLTDataColumnDefinition oTempXdcd = (XSaLTDataColumnDefinition) ioDataColumns.get(sKey);
			_oCreateSqlStringbuffer.append("\n\t" + oTempXdcd.getDataColumnName() + " ");
			_oCreateSqlStringbuffer.append("" + oTempXdcd.getDataColumnType() + " ");
			if (oTempXdcd.getDataColumnSize() != null)
			{
				_oCreateSqlStringbuffer.append("(" + oTempXdcd.getDataColumnSize().toString() + ") ");
			}
			if (oTempXdcd.getUnsignedColumn() != null)
			{
				_oCreateSqlStringbuffer.append(" " + oTempXdcd.getUnsignedColumn() + " ");
			}
			if (oTempXdcd.getAutonumberColumn() != null)
			{
				_oCreateSqlStringbuffer.append(" " + oTempXdcd.getAutonumberColumn() + " ");
			}
		}
		_oCreateSqlStringbuffer.append("\n) ENGINE=" + _sTableType);
		return _oCreateSqlStringbuffer.toString();
	}

	/**
	 * This method concatenates the names of each column and returns them as a String.
	 * 
	 * @return String containing each column name and label (if it exists)
	 */
	public String getColumnNames()
	{
		StringBuffer oColumnNames = new StringBuffer();
		for (Iterator<String> i = ioDataColumnsNames.keySet().iterator(); i.hasNext();)
		{
			String sKey = (String) i.next();
			String sValue = (String) ioDataColumnsNames.get(sKey);
			boolean isConcatenatedField = false;

			if (sKey.toLowerCase().indexOf("concat") != -1)
			{
				isConcatenatedField = true;
			}

			if (sValue != null && sValue.equals(""))
			{
				sValue = " AS " + isTableName.toUpperCase() + "__" + sKey;
			}
			else if (sValue == null)
			{
				sValue = "";
			}
			else
			{
				sValue = " AS " + sValue;
			}

			if (isConcatenatedField)
			{
				oColumnNames.append(sKey + sValue + ", ");
			}
			else
			{
				oColumnNames.append(isTableName.toUpperCase() + "." + sKey + sValue + ", ");
			}

		}
		oColumnNames = new StringBuffer(oColumnNames.substring(0, oColumnNames.length() - 2));
		return oColumnNames.toString();
	}

	/**
	 * This method returns the SQL to join, display and order tables/columns with another table
	 * 
	 * @param _oSecondTableDef The second table definition to join this one with
	 * @param _sJoinType The type of join (right/left)
	 * @param _sWhereClause The additional "where" clause
	 * @param _oOrderByArrayList The ArrayList containing the order in which fields are to be sorted
	 * @param _sLimitClause The "limit" clause
	 * @return The String "join" clause 
	 */
	public String getSelectWithOtherTableDefinition(XSaLTDataTableDefinition _oSecondTableDef, String _sJoinType, String _sWhereClause,
			ArrayList<String> _oOrderByArrayList, String _sLimitClause)
	{
		StringBuffer oSelectBuffer = new StringBuffer();
		oSelectBuffer.append("SELECT ");
		oSelectBuffer.append(_oSecondTableDef.getColumnNames());
		oSelectBuffer.append(", ");
		oSelectBuffer.append(getColumnNames());
		oSelectBuffer.append(" FROM ");
		oSelectBuffer.append(getTableName());
		oSelectBuffer.append(getJoinClauseWithOtherTableDefinition(_oSecondTableDef, _sJoinType));
		if (_sWhereClause != null && !_sWhereClause.equals(""))
		{
			oSelectBuffer.append(_sWhereClause);
		}
		oSelectBuffer.append(getOrderByClause(_oOrderByArrayList));

		oSelectBuffer.append(" " + _sLimitClause);
		return oSelectBuffer.toString();
	}

	/**
	 * This method creates an SQL SELECT statement using INNER JOINs to join
	 * the current table to two other tables.
	 *  
	 * @param _oSecondTableDef
	 *            Table definition for 2nd table in JOIN
	 * @param _oThirdTableDef
	 *            Table definition for 3rd table in JOIN
	 * @param _sWhereClause
	 *            SQL WHERE clause to narrow results
	 * @param _oOrderByArrayList
	 *            ArrayList of column names to create ORDER BY clause
	 * @param _sLimitClause
	 *            SQL LIMIT BY clause
	 * @return String of created SQL SELECT statement
	 */
	public String getSelectWithOtherTableDefinitionTwo(XSaLTDataTableDefinition _oSecondTableDef, XSaLTDataTableDefinition _oThirdTableDef, String _sWhereClause,
			ArrayList<String> _oOrderByArrayList, String _sLimitClause)
	{
		StringBuffer oSelectBuffer = new StringBuffer();
		oSelectBuffer.append("SELECT ");
		oSelectBuffer.append(getColumnNames());
		oSelectBuffer.append(", ");
		oSelectBuffer.append(_oSecondTableDef.getColumnNames());
		oSelectBuffer.append(", ");
		oSelectBuffer.append(_oThirdTableDef.getColumnNames());
		oSelectBuffer.append(" FROM ");

		oSelectBuffer.append("  (" + getTableName() + " INNER JOIN " + _oSecondTableDef.getTableName() + " ON " + getJoinColumnName() + " = "
				+ _oSecondTableDef.getJoinColumnName() + ") ");
		oSelectBuffer.append(" INNER JOIN " + _oThirdTableDef.getTableName() + " ON " + getTablePrimaryKeyName() + " = " + _oThirdTableDef.getJoinColumnName() + " ");

		if (_sWhereClause != null && !_sWhereClause.equals(""))
		{
			oSelectBuffer.append(_sWhereClause);
		}
		oSelectBuffer.append(getOrderByClause(_oOrderByArrayList));

		oSelectBuffer.append(" " + _sLimitClause);
		

		return oSelectBuffer.toString();
	}
	
	/**
	 * This method creates an SQL SELECT statement using a "LEFT OUTER JOIN" to join
	 * the current table to one other table.
	 * 
	 * @param _oSecondTableDef
	 *            Table definition to join to
	 * @param _sWhereClause
	 *            SQL WHERE clause to narrow results
	 * @param _oOrderByArrayList
	 *            ArrayList of column names to create ORDER BY clause
	 * @param _sLimitClause
	 *            SQL LIMIT clause
	 * @return String of created SQL SELECT statement
	 */
	public String getSelectWithOtherTableDefinitionOuterJoin(XSaLTDataTableDefinition _oSecondTableDef, String _sWhereClause, ArrayList<String> _oOrderByArrayList,
			String _sLimitClause)
	{
		StringBuffer oSelectBuffer = new StringBuffer();
		oSelectBuffer.append("SELECT ");
		oSelectBuffer.append(getColumnNames());
		oSelectBuffer.append(", ");
		oSelectBuffer.append(_oSecondTableDef.getColumnNames());
		oSelectBuffer.append(" FROM ");
		
		oSelectBuffer.append(getTableName() + " LEFT OUTER JOIN " + _oSecondTableDef.getTableName() + " ON " + getJoinColumnName() + " = "
				+ _oSecondTableDef.getJoinColumnName() + " ");
		
		if (_sWhereClause != null && _sWhereClause.trim().length() > 0)
		{
			oSelectBuffer.append(_sWhereClause);
		}
		
		oSelectBuffer.append(getOrderByClause(_oOrderByArrayList));

		oSelectBuffer.append(" " + _sLimitClause);
		
		return oSelectBuffer.toString();
	}

	/**
	 * This method returns the "join" clause of the table
	 * 
	 * @param _oSecondTableDef The second table definition to join this one with
	 * @param _sJoinType The type of join (right/left)
	 * @return The String "join" clause 
	 */
	public String getJoinClauseWithOtherTableDefinition(XSaLTDataTableDefinition _oSecondTableDef, String _sJoinType)
	{
		StringBuffer oJoinBuffer = new StringBuffer();
		oJoinBuffer.append(" " + _sJoinType + " JOIN ");
		oJoinBuffer.append(_oSecondTableDef.getTableName() + " ON " + getJoinColumnName() + " = " + _oSecondTableDef.getJoinColumnName());
		return oJoinBuffer.toString();
	}

	/**
	 * This method returns the "order by" clause of the table
	 * 
	 * @param _oOrderByArrayList The ArrayList of column names
	 * @return The String "order by" clause
	 */
	public String getOrderByClause(ArrayList<String> _oOrderByArrayList)
	{
		StringBuffer oOrderBuffer = new StringBuffer();
		if (_oOrderByArrayList != null)
		{
			oOrderBuffer.append(" ORDER BY ");
			for (int i = 0; i < _oOrderByArrayList.size(); i++)
			{
				oOrderBuffer.append(_oOrderByArrayList.get(i) + ", ");
			}
			oOrderBuffer = new StringBuffer(oOrderBuffer.substring(0, oOrderBuffer.length() - 2));
		}
		else
		{
			oOrderBuffer.append(" ");
		}
		return oOrderBuffer.toString();
	}
}
