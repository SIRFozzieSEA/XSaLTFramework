package com.codef.xsalt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import org.apache.log4j.Logger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileDeDupeUtils {

	private static final Logger LOGGER = Logger.getLogger(XSaLTFileDeDupeUtils.class.getName());

	/**
	 * The current file being examined
	 */
	private String isExistingFileInDirectory;

	/**
	 * Flag to delete the dedupes
	 */
	private boolean ibDeleteDupes = false;

	/**
	 * The total files XSaLTFileDeDupeUtils examined
	 */
	private long ilTotalCountFiles = 0;

	/**
	 * The total count of dupes XSaLTFileDeDupeUtils suspects
	 */
	private long ilTotalCountOfDupes = 0;

	/**
	 * The total bytes XSaLTFileDeDupeUtils examined
	 */
	private long ilTotalBytesFiles = 0;

	/**
	 * The total duplicate bytes XSaLTFileDeDupeUtils suspects
	 */
	private long ilTotalBytesOfDupes = 0;

	/**
	 * The HashMap which contains the file name, file size and file date
	 */
	private HashMap<String, StringBuffer> ioFileNamesSizeDatesHashMap = new HashMap<String, StringBuffer>();

	/**
	 * The ArrayList of distinct extensions found
	 */
	private ArrayList<String> ioFileExtensionsArrayList = new ArrayList<String>();

	/**
	 * The operations log buffer
	 */
	public StringBuffer ioLogBuffer = null;

	/**
	 * The deleted files log buffer
	 */
	public StringBuffer ioDeletedFilesBuffer = null;

	public StringBuffer ioReportFilesBuffer = null;

	/**
	 * Main constructor (testing only)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new XSaLTFileDeDupeUtils("", "", true, true);
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}

	}

	/**
	 * The main constructor and entry method
	 * 
	 * @param _sDirectoryToStartIn The directory to start the searching in
	 * @param _sLogPath            The path for the logs
	 * @param _bDeleteDupes        Flag to delete the files
	 * @param _bDoAlderDupe        Flag to do standard dedupe, or Alder checksum
	 * @throws IOException
	 */
	public XSaLTFileDeDupeUtils(String _sDirectoryToStartIn, String _sLogPath, boolean _bDeleteDupes,
			boolean _bDoAlderDupe) throws IOException {
		ilTotalCountFiles = 0;
		ilTotalCountOfDupes = 0;
		ilTotalBytesFiles = 0;
		ilTotalBytesOfDupes = 0;
		ioLogBuffer = new StringBuffer();
		ioDeletedFilesBuffer = new StringBuffer();
		ioReportFilesBuffer = new StringBuffer();
		File o_directory = new File(_sDirectoryToStartIn);
		ibDeleteDupes = _bDeleteDupes;
		traverseDirectoriesForDedupe(o_directory, true, true, _bDoAlderDupe);
		writeDeDupeFileSummary();
		XSaLTFileSystemUtils.writeStringToFile(ioLogBuffer.toString(), _sLogPath + "/TMADupeReport_FileDateSize.txt");
		XSaLTFileSystemUtils.writeStringToFile(ioDeletedFilesBuffer.toString(),
				_sLogPath + "/TMADupeDeleted_FileDateSize.txt");
		XSaLTFileSystemUtils.writeStringToFile(ioReportFilesBuffer.toString(),
				_sLogPath + "/TMAReport_FileDateSize.txt");
	}

	/**
	 * The method that prints out the dedupe summary
	 */
	public void writeDeDupeFileSummary() {
		ioLogBuffer.append("\n\n              Total Files: " + ilTotalCountFiles);
		ioLogBuffer.append("\n      Total Files (bytes): " + ilTotalBytesFiles);
		ioLogBuffer.append("\n         Total Files (KB): " + Double.valueOf(ilTotalBytesFiles / 1024));
		ioLogBuffer.append("\n         Total Files (MB): " + Double.valueOf(ilTotalBytesFiles / 1048576));

		ioLogBuffer.append("\n\n         Total Dupe Files: " + ilTotalCountOfDupes);
		ioLogBuffer.append("\n Total Dupe Files (bytes): " + ilTotalBytesOfDupes);
		ioLogBuffer.append("\n    Total Dupe Files (KB): " + Double.valueOf(ilTotalBytesOfDupes / 1024));
		ioLogBuffer.append("\n    Total Dupe Files (MB): " + Double.valueOf(ilTotalBytesOfDupes / 1048576));

		ioDeletedFilesBuffer.append("\n\n              Total Files: " + ilTotalCountFiles);
		ioDeletedFilesBuffer.append("\n      Total Files (bytes): " + ilTotalBytesFiles);
		ioDeletedFilesBuffer.append("\n         Total Files (KB): " + Double.valueOf(ilTotalBytesFiles / 1024));
		ioDeletedFilesBuffer.append("\n         Total Files (MB): " + Double.valueOf(ilTotalBytesFiles / 1048576));

		ioDeletedFilesBuffer.append("\n\n         Total Dupe Files: " + ilTotalCountOfDupes);
		ioDeletedFilesBuffer.append("\n Total Dupe Files (bytes): " + ilTotalBytesOfDupes);
		ioDeletedFilesBuffer.append("\n    Total Dupe Files (KB): " + Double.valueOf(ilTotalBytesOfDupes / 1024));
		ioDeletedFilesBuffer.append("\n    Total Dupe Files (MB): " + Double.valueOf(ilTotalBytesOfDupes / 1048576));

	}

	/**
	 * The method that calculates the Alder checksum
	 * 
	 * @param _sFilePath The path to the file
	 * @return The long Alder checksum
	 * @throws IOException
	 */
	public long getFileChecksum(String _sFilePath) throws IOException {
		// Compute Adler-32 checksum
		CheckedInputStream cis = new CheckedInputStream(new FileInputStream(_sFilePath), new Adler32());
		byte[] tempBuf = new byte[10240];
		while (cis.read(tempBuf) >= 0) {
		}
		long checksum = cis.getChecksum().getValue();
		cis.close();
		return checksum;

	}

	/**
	 * The static method that calculates the Alder checksum
	 * 
	 * @param _sFilePath The path to the file
	 * @return The long Alder checksum
	 * @throws IOException
	 */
	public static long getFileChecksumStatic(String _sFilePath) throws IOException {
		// Compute Adler-32 checksum
		CheckedInputStream cis = new CheckedInputStream(new FileInputStream(_sFilePath), new Adler32());
		byte[] tempBuf = new byte[10240];
		while (cis.read(tempBuf) >= 0) {
		}
		long checksum = cis.getChecksum().getValue();
		cis.close();
		return checksum;

	}

	/**
	 * This method actually does all the deduping and traversing through directories
	 * 
	 * @param _oStartDirectory        The directory to start the searching in
	 * @param _bShowDirectoryProgress Flag to show directory progress
	 * @param _bShowFileDupes         Flag to show the file dupes or not
	 * @param _bDoAlderDupe           Flag to do standard dedupe, or Alder checksum
	 * @throws IOException
	 */
	public void traverseDirectoriesForDedupe(File _oStartDirectory, boolean _bShowDirectoryProgress,
			boolean _bShowFileDupes, boolean _bDoAlderDupe) throws IOException {
		if (_oStartDirectory.isDirectory()) {
			String sDirectoryMessage = "In directory: " + _oStartDirectory.getAbsolutePath();
			if (_bShowDirectoryProgress) {
				LOGGER.info(sDirectoryMessage);
			}
			ioLogBuffer.append(sDirectoryMessage);

			String[] asDirectoryChildren = _oStartDirectory.list();
			if (asDirectoryChildren != null) {
				for (int i = 0; i < asDirectoryChildren.length; i++) {
					StringBuffer oTestFilePath = new StringBuffer(
							_oStartDirectory.getAbsolutePath() + "\\" + asDirectoryChildren[i]);
					File oTestFile = new File(oTestFilePath.toString());
					if (!oTestFile.isDirectory()) {
						StringBuffer oFullFilePath = new StringBuffer(oTestFilePath.toString().replace('\\', '/'));
						String sFileAndExtension = oFullFilePath
								.substring(oFullFilePath.toString().lastIndexOf('/') + 1, oFullFilePath.length());

						Long oLastModified = Long.valueOf(oTestFile.lastModified());
						Long oFileSize = Long.valueOf(oTestFile.length());

						ilTotalCountFiles = ilTotalCountFiles + 1;
						ilTotalBytesFiles = ilTotalBytesFiles + oTestFile.length();

						String sHashKey = "";

						if (_bDoAlderDupe) {
							sHashKey = Long.valueOf(getFileChecksum(oFullFilePath.toString())).toString();
						} else {
							sHashKey = sFileAndExtension + "_" + oFileSize + "_" + oLastModified;
						}

						if (sFileAndExtension.lastIndexOf(".") != -1) {
							String sFileExtension = sFileAndExtension
									.substring(sFileAndExtension.lastIndexOf(".") + 1, sFileAndExtension.length())
									.toLowerCase();
							if (!ioFileExtensionsArrayList.contains(sFileExtension)) {
								ioFileExtensionsArrayList.add(sFileExtension);
							}
						}

						if (ioFileNamesSizeDatesHashMap.containsKey(sHashKey)) {
							isExistingFileInDirectory = ioFileNamesSizeDatesHashMap.get(sHashKey).toString();
							String sMessageOne = "\tSuspect Duplicate File: '" + oFullFilePath + " ("
									+ sHashKey.toString() + ")'\n\t               copy of: '"
									+ isExistingFileInDirectory + "'";

							if (_bShowFileDupes) {
								LOGGER.info(sMessageOne);
								if (sMessageOne != null) {
									ioReportFilesBuffer.append(sMessageOne);
								}
							}

							ioLogBuffer.append(sMessageOne);

							if (ibDeleteDupes) {
								ioDeletedFilesBuffer.append("\n" + oFullFilePath);
								ilTotalBytesOfDupes = ilTotalBytesOfDupes + oTestFile.length();
								oTestFile.delete();
							}

							ilTotalCountOfDupes = ilTotalCountOfDupes + 1;

						} else {
							ioFileNamesSizeDatesHashMap.put(sHashKey, oFullFilePath);
						}
					}
					traverseDirectoriesForDedupe(new File(_oStartDirectory, asDirectoryChildren[i]),
							_bShowDirectoryProgress, _bShowFileDupes, _bDoAlderDupe);
				}
			}
		}

	}

}
