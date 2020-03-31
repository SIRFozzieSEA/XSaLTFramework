package com.codef.xsalt.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileFolderFlattener
{

	/**
	 * Number of files
	 */
	int inNumberOfFiles = 0;
	/**
	 * Number of files to move
	 */
	int inNumberOfFilesToMove = 0;

	/**
	 * Current file path
	 */
	private String isCurrentFilePath = "";
	/**
	 * Flag if folder has nested folders 
	 */
	private boolean ibHasNestedFolders = false;
	/**
	 * List of file names in folder
	 */
	private ArrayList<String> ioFileNameArrayList = null;
	/**
	 * List of nested folders to delete
	 */
	private ArrayList<String> ioNestedFolderToDeleteArrayList = null;
	/**
	 * 
	 */
	private HashMap<String, String> ioFilePathHashMap = null;

	/**
	 * Main constructor and entry method.
	 * 
	 * @param _sFilePathWithNoEndSlash
	 *            Path to directory/folder to flatten
	 * @throws IOException
	 */
	public XSaLTFileFolderFlattener(String _sFilePathWithNoEndSlash) throws IOException
	{

		inNumberOfFiles = 0;
		inNumberOfFilesToMove = 0;

		isCurrentFilePath = "";
		ibHasNestedFolders = false;

		ioFileNameArrayList = new ArrayList<String>();
		ioNestedFolderToDeleteArrayList = new ArrayList<String>();
		ioFilePathHashMap = new HashMap<String, String>();

		primeRootDocumentFolder(_sFilePathWithNoEndSlash);
		primeEmbeddedFoldersArrayList(_sFilePathWithNoEndSlash, _sFilePathWithNoEndSlash);
		if (ibHasNestedFolders)
		{

			for (Iterator<String> j = ioFilePathHashMap.keySet().iterator(); j.hasNext();)
			{
				String sOriginalFile = (String) j.next();
				String sCopyToLocation = ioFilePathHashMap.get(sOriginalFile);
				//					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, sOriginalFile + " --> " + sCopyToLocation);
				XSaLTFileSystemUtils.copyFile(sOriginalFile, sCopyToLocation, 2048);

			}

			for (int j = 0; j < ioNestedFolderToDeleteArrayList.size(); j++)
			{
				XSaLTFileSystemUtils.deleteDirectoryAndSubdirectories(ioNestedFolderToDeleteArrayList.get(j));
			}

		}

	}

	/**
	 * This method traverses the root directory and adds each file found to the
	 * file name list.
	 * 
	 * @param _sStartDirectory
	 *            Directory path to perform work on
	 */
	private void primeRootDocumentFolder(String _sStartDirectory)
	{
		File _oDirectory = new File(_sStartDirectory);
		String[] aoDirectoryChildren = _oDirectory.list();

		for (int i = 0; i < aoDirectoryChildren.length; i++)
		{
			isCurrentFilePath = _oDirectory.getAbsolutePath() + "\\" + aoDirectoryChildren[i];
			File oTestDirectory = new File(isCurrentFilePath);
			if (!oTestDirectory.isDirectory())
			{
				isCurrentFilePath = isCurrentFilePath.replace('\\', '/');
				String sCurrentFileName = isCurrentFilePath.substring(isCurrentFilePath.lastIndexOf('/') + 1, isCurrentFilePath
						.length());
				ioFileNameArrayList.add(sCurrentFileName);
			}
		}

	}

	/**
	 * This method traverses the embedded folders looking for files.  Each file
	 * found is put into the HashMap for copying files to the root directory.
	 * 
	 * @param _sStartDirectory
	 *            Directory to search
	 * @param _sHomeDirectory
	 *            Root directory where files will be copied
	 */
	private void primeEmbeddedFoldersArrayList(String _sStartDirectory, String _sHomeDirectory)
	{
		File _oDirectory = new File(_sStartDirectory);
		if (_oDirectory.isDirectory())
		{
			String[] asDirectoryChildren = _oDirectory.list();
			if (asDirectoryChildren != null)
			{
				for (int i = 0; i < asDirectoryChildren.length; i++)
				{
					isCurrentFilePath = _oDirectory.getAbsolutePath() + "\\" + asDirectoryChildren[i];
					File oTestDirectory = new File(isCurrentFilePath);
					if (oTestDirectory.isDirectory())
					{
						ibHasNestedFolders = true;
					}
					else
					{
						isCurrentFilePath = isCurrentFilePath.replace('\\', '/');
						String sCurrentFolderPath = isCurrentFilePath.substring(0, isCurrentFilePath.lastIndexOf('/'));
						String sCurrentFileName = isCurrentFilePath.substring(isCurrentFilePath.lastIndexOf('/') + 1,
								isCurrentFilePath.length());
						String sCurrentFileFullPath = sCurrentFolderPath + "/" + sCurrentFileName;

						String sKey = sCurrentFileFullPath;
						String sValue = "";

						if (!_sStartDirectory.equals(_sHomeDirectory))
						{
							ioNestedFolderToDeleteArrayList.add(sCurrentFolderPath);
						}

						if (ioFileNameArrayList.contains(sCurrentFileName))
						{
							if (sCurrentFileName.indexOf(".") != -1)
							{
								int nPeriodIndex = sCurrentFileName.lastIndexOf(".");
								String sExtension = sCurrentFileName.substring(nPeriodIndex, sCurrentFileName.length());
								String sFileNameWithoutExtension = sCurrentFileName.substring(0, nPeriodIndex);
								sValue = _sHomeDirectory + "/" + sFileNameWithoutExtension + "_"
										+ XSaLTStringUtils.getDatetimeStringNoUnderscoreWithMilli() + sExtension;
							}
							else
							{
								sValue = _sHomeDirectory + "/" + sCurrentFileName + "_"
										+ XSaLTStringUtils.getDatetimeStringNoUnderscoreWithMilli();
							}

						}
						else
						{
							sValue = _sHomeDirectory + "/" + sCurrentFileName;
						}

						ioFileNameArrayList.add(sCurrentFileName);
						inNumberOfFiles = inNumberOfFiles + 1;

						if (!_sStartDirectory.equals(_sHomeDirectory))
						{
							ioFilePathHashMap.put(sKey, sValue);
							inNumberOfFilesToMove = inNumberOfFilesToMove + 1;
						}

					}
					primeEmbeddedFoldersArrayList(new File(_oDirectory, asDirectoryChildren[i]).getAbsolutePath(),
							_sHomeDirectory);
				}
			}
		}

	}
}
