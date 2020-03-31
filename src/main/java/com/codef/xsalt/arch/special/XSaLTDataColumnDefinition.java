package com.codef.xsalt.arch.special;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTDataColumnDefinition
{
	/**
	 * The XSaLTDataColumnDefinition column name
	 */
	private String isDataColumnName = null;

	/**
	 * The XSaLTDataColumnDefinition column type
	 */
	private String isDataColumnType = null;

	/**
	 * The XSaLTDataColumnDefinition column size
	 */
	private Long ioDataColumnSize = null;

	/**
	 * The XSaLTDataColumnDefinition column description
	 */
	private String isDataColumnDescription = null;

	/**
	 * The XSaLTDataColumnDefinition HTML display
	 */
	private String isDataColumnHtmlDisplayName = null;

	/**
	 * The XSaLTDataColumnDefinition HTML display width
	 */
	private String isDataColumnHtmlDisplayWidth = null;

	/**
	 * The XSaLTDataColumnDefinition (HEADER) HTML horizontal alignment
	 */
	private String isDataColumnHtmlHAlignment = null;

	/**
	 * The XSaLTDataColumnDefinition (HEADER) HTML vertical alignment
	 */
	private String isDataColumnHtmlVAlignment = null;

	/**
	 * The XSaLTDataColumnDefinition (DATA/ROW) HTML horizontal alignment
	 */
	private String isDataDataHtmlHAlignment = null;

	/**
	 * The XSaLTDataColumnDefinition (DATA/ROW) HTML vertical alignment
	 */
	private String isDataDataHtmlVAlignment = null;

	/**
	 * The XSaLTDataColumnDefinition autonumber column definition
	 */
	private String isAutonumberColumn = null;

	/**
	 * The XSaLTDataColumnDefinition unsigned column definition
	 */
	private String isUnsignedColumn = null;

	/**
	 * The XSaLTDataColumnDefinition sort column definition
	 */
	private String isSortColumn = null;

	/**
	 * Default constructor
	 */
	public XSaLTDataColumnDefinition()
	{

	}

	/**
	 * This method returns the value of DataDataHtmlHAlignment
	 * 
	 * @return Returns the value of DataDataHtmlHAlignment.
	 */
	public String getDataDataHtmlHAlignment()
	{
		return isDataDataHtmlHAlignment;
	}

	/**
	 * This method returns the value of DataDataHtmlVAlignment
	 * 
	 * @return Returns the value of DataDataHtmlVAlignment.
	 */
	public String getDataDataHtmlVAlignment()
	{
		return isDataDataHtmlVAlignment;
	}

	/**
	 * This method returns the value of AutonumberColumn
	 * 
	 * @return Returns the value of AutonumberColumn.
	 */
	public String getAutonumberColumn()
	{
		return isAutonumberColumn;
	}

	/**
	 * This method returns the value of DataColumnDescription
	 * 
	 * @return Returns the value of DataColumnDescription.
	 */
	public String getDataColumnDescription()
	{
		return isDataColumnDescription;
	}

	/**
	 * This method returns the value of DataColumnHtmlDisplayName
	 * 
	 * @return Returns the value of DataColumnHtmlDisplayName.
	 */
	public String getDataColumnHtmlDisplayName()
	{
		return isDataColumnHtmlDisplayName;
	}

	/**
	 * This method returns the value of DataColumnName
	 * 
	 * @return Returns the value of DataColumnName.
	 */
	public String getDataColumnName()
	{
		return isDataColumnName;
	}

	/**
	 * This method returns the value of DataColumnType
	 * 
	 * @return Returns the value of DataColumnType.
	 */
	public String getDataColumnType()
	{
		return isDataColumnType;
	}

	/**
	 * This method returns the value of UnsignedColumn
	 * 
	 * @return Returns the value of UnsignedColumn.
	 */
	public String getUnsignedColumn()
	{
		return isUnsignedColumn;
	}

	/**
	 * This method returns the value of DataColumnHtmlDisplayWidth
	 * 
	 * @return Returns the value of DataColumnHtmlDisplayWidth.
	 */
	public String getDataColumnHtmlDisplayWidth()
	{
		return isDataColumnHtmlDisplayWidth;
	}

	/**
	 * This method returns the value of DataColumnHtmlHAlignment
	 * 
	 * @return Returns the value of DataColumnHtmlHAlignment.
	 */
	public String getDataColumnHtmlHAlignment()
	{
		return isDataColumnHtmlHAlignment;
	}

	/**
	 * This method returns the value of DataColumnHtmlVAlignment
	 * 
	 * @return Returns the value of DataColumnHtmlVAlignment.
	 */
	public String getDataColumnHtmlVAlignment()
	{
		return isDataColumnHtmlVAlignment;
	}

	/**
	 * This method returns the value of SortColumn
	 * 
	 * @return Returns the value of SortColumn.
	 */
	public String getSortColumn()
	{
		return isSortColumn;
	}

	/**
	 * This method returns the value of DataColumnSize
	 * 
	 * @return Returns the value of DataColumnSize.
	 */
	public Long getDataColumnSize()
	{
		return ioDataColumnSize;
	}

	//---------------------------

	/**
	 * This method sets the value of DataDataHtmlHAlignment
	 * 
	 * @param _sDataDataHtmlHAlignment The DataDataHtmlHAlignment to set.
	 */
	public void setDataDataHtmlHAlignment(String _sDataDataHtmlHAlignment)
	{
		isDataDataHtmlHAlignment = _sDataDataHtmlHAlignment;
	}

	/**
	 * This method sets the value of DataDataHtmlVAlignment
	 * 
	 * @param _sDataDataHtmlVAlignment The DataDataHtmlVAlignment to set.
	 */
	public void setDataDataHtmlVAlignment(String _sDataDataHtmlVAlignment)
	{
		isDataDataHtmlVAlignment = _sDataDataHtmlVAlignment;
	}

	/**
	 * This method sets the value of AutonumberColumn
	 * 
	 * @param _sAutonumberColumn The AutonumberColumn to set.
	 */
	public void setAutonumberColumn(String _sAutonumberColumn)
	{
		isAutonumberColumn = _sAutonumberColumn;
	}

	/**
	 * This method sets the value of DataColumnDescription
	 * 
	 * @param _sDataColumnDescription The DataColumnDescription to set.
	 */
	public void setDataColumnDescription(String _sDataColumnDescription)
	{
		isDataColumnDescription = _sDataColumnDescription;
	}

	/**
	 * This method sets the value of DataColumnHtmlDisplayName
	 * 
	 * @param _sDataColumnHtmlDisplayName The DataColumnHtmlDisplayName to set.
	 */
	public void setDataColumnHtmlDisplayName(String _sDataColumnHtmlDisplayName)
	{
		isDataColumnHtmlDisplayName = _sDataColumnHtmlDisplayName;
	}

	/**
	 * This method sets the value of DataColumnName
	 * 
	 * @param _sDataColumnName The DataColumnName to set.
	 */
	public void setDataColumnName(String _sDataColumnName)
	{
		isDataColumnName = _sDataColumnName;
	}

	/**
	 * This method sets the value of DataColumnType
	 * 
	 * @param _sDataColumnType The DataColumnType to set.
	 */
	public void setDataColumnType(String _sDataColumnType)
	{
		isDataColumnType = _sDataColumnType;
	}

	/**
	 * This method sets the value of UnsignedColumn
	 * 
	 * @param _sUnsignedColumn The UnsignedColumn to set.
	 */
	public void setUnsignedColumn(String _sUnsignedColumn)
	{
		isUnsignedColumn = _sUnsignedColumn;
	}

	/**
	 * This method sets the value of DataColumnSize
	 * 
	 * @param _oDataColumnSize The DataColumnSize to set.
	 */
	public void setDataColumnSize(Long _oDataColumnSize)
	{
		ioDataColumnSize = _oDataColumnSize;
	}

	/**
	 * This method sets the value of DataColumnHtmlDisplayWidth
	 * 
	 * @param _sDataColumnHtmlDisplayWidth The DataColumnHtmlDisplayWidth to set.
	 */
	public void setDataColumnHtmlDisplayWidth(String _sDataColumnHtmlDisplayWidth)
	{
		isDataColumnHtmlDisplayWidth = _sDataColumnHtmlDisplayWidth;
	}

	/**
	 * This method sets the value of DataColumnHtmlHAlignment
	 * 
	 * @param _sDataColumnHtmlHAlignment The DataColumnHtmlHAlignment to set.
	 */
	public void setDataColumnHtmlHAlignment(String _sDataColumnHtmlHAlignment)
	{
		isDataColumnHtmlHAlignment = _sDataColumnHtmlHAlignment;
	}

	/**
	 * This method sets the value of DataColumnHtmlVAlignment
	 * 
	 * @param _sDataColumnHtmlVAlignment The DataColumnHtmlVAlignment to set.
	 */
	public void setDataColumnHtmlVAlignment(String _sDataColumnHtmlVAlignment)
	{
		isDataColumnHtmlVAlignment = _sDataColumnHtmlVAlignment;
	}

	/**
	 * This method sets the value of SortColumn
	 * 
	 * @param _sSortColumn The SortColumn to set.
	 */
	public void setSortColumn(String _sSortColumn)
	{
		isSortColumn = _sSortColumn;
	}

}
