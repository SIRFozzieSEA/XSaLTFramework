package com.codef.xsalt.utils;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public class XSaLTFileFilter extends FileFilter
{
	private ArrayList<String> ioExtensions;
	private String isDescription;
	
	public XSaLTFileFilter() {}
	
	public XSaLTFileFilter(String _sDescription, String _sExtensionCSV)
	{
		isDescription = _sDescription;
		ioExtensions = XSaLTStringUtils.commaDelimitedStringToArrayList(_sExtensionCSV, false);
	}
	
	@Override
	public boolean accept(File _oFile)
	{
		boolean bRv = _oFile.isDirectory();
		if (!bRv)
		{
			String[] asPieces = _oFile.getName().split("\\.");
			if (asPieces.length > 1)
			{
				String sExtension = "." + asPieces[asPieces.length - 1];
				bRv = ioExtensions.contains(sExtension);
			}
		}
		return bRv;
	}

	@Override
	public String getDescription()
	{
		return isDescription;
	}
	
	public void setDescription(String _sDescription)
	{
		isDescription = _sDescription;
	}
	
	public void setFileExtensions(String _sExtensionCSV)
	{
		setFileExtensions(XSaLTStringUtils.commaDelimitedStringToArrayList(_sExtensionCSV, false));
	}
	
	public void setFileExtensions(ArrayList<String> _oExtensions)
	{
		ioExtensions = _oExtensions;
	}
	
	public ArrayList<String> getFileExtensions()
	{
		return ioExtensions;
	}

}
