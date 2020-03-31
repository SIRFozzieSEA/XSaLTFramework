package com.codef.xsalt.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileVisitor
{

	private String ioTestFilePath;
	private StringBuffer ioTextBuffer = new StringBuffer();

	/*

	try
	{
		XSaLTFileVisitor oFileVisitor = new XSaLTFileVisitor();
		File oFile = new File("C:/SVN/TMAClientJava");
		oFileVisitor.visitAllFiles(oFile);
		oFileVisitor.writeBufferToFile("C:/_WORKING/TMAClientJava.txt");
		
		oFileVisitor = new XSaLTFileVisitor();
		oFile = new File("C:/SVN/TMAIPBroadcaster");
		oFileVisitor.visitAllFiles(oFile);
		oFileVisitor.writeBufferToFile("C:/_WORKING/TMAIPBroadcaster.txt");
		
		oFileVisitor = new XSaLTFileVisitor();
		oFile = new File("C:/SVN/XSaLTWebs");
		oFileVisitor.visitAllFiles(oFile);
		oFileVisitor.writeBufferToFile("C:/_WORKING/XSaLTWebs.txt");
		
		oFileVisitor = new XSaLTFileVisitor();
		oFile = new File("C:/SVN/TMABackup");
		oFileVisitor.visitAllFiles(oFile);
		oFileVisitor.writeBufferToFile("C:/_WORKING/TMABackup.txt");
		
		oFileVisitor = new XSaLTFileVisitor();
		oFile = new File("C:/SVN/ZZZ_OLD_CODE");
		oFileVisitor.visitAllFiles(oFile);
		oFileVisitor.writeBufferToFile("C:/_WORKING/OldCode.txt");

		
	}
	catch (IOException e)
	{
		
	}
	
	*/

	/**
	 * This method traverses a directory and puts the contents of each file
	 * into a StringBuffer. 
	 * 
	 * @param _oDirectory
	 *            Directory to traverse as a File object
	 * @throws IOException
	 */
	public void visitAllFiles(File _oDirectory) throws IOException
	{
		if (_oDirectory.isDirectory())
		{
			String[] aoDirectoryChildren = _oDirectory.list();
			if (aoDirectoryChildren != null)
			{
				for (int i = 0; i < aoDirectoryChildren.length; i++)
				{
					ioTestFilePath = _oDirectory.getAbsolutePath() + "\\" + aoDirectoryChildren[i];
					File oTestDirectory = new File(ioTestFilePath);
					if (!oTestDirectory.isDirectory())
					{
						ioTestFilePath = ioTestFilePath.replace('\\', '/');
						//						String sTestOne = ioTestFilePath.substring(0,
						//								ioTestFilePath.lastIndexOf('/') + 1);
						String sTestTwo = ioTestFilePath.substring(ioTestFilePath.lastIndexOf('/') + 1, ioTestFilePath.length());

						if (sTestTwo.endsWith(".xsl") || sTestTwo.endsWith(".java") || sTestTwo.endsWith(".xml"))
						{

							StringBuffer oTempBuffer = new StringBuffer();
							oTempBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(ioTestFilePath, "\n");

							ioTextBuffer.append(oTempBuffer);
							ioTextBuffer.append("\n\n BEGIN NEW --------------------------------------------------------------\n\n");

						}

					}
					visitAllFiles(new File(_oDirectory, aoDirectoryChildren[i]));
				}
			}
		}

	}

	/**
	 * This method writes the buffer created in the visitAllFiles method to a
	 * file.
	 * 
	 * @param _sFilePath
	 *            Destination file for buffer
	 * @throws IOException
	 */
	public void writeBufferToFile(String _sFilePath) throws IOException
	{
		XSaLTFileSystemUtils.writeStringBufferToFile(ioTextBuffer, _sFilePath);
	}

}
