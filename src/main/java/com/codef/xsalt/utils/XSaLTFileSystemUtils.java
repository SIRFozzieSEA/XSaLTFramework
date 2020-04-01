package com.codef.xsalt.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.log4j.Priority;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import multivalent.Behavior;
import multivalent.Context;
import multivalent.Document;
import multivalent.Node;
import multivalent.ParseException;
import multivalent.std.adaptor.pdf.PDF;

import com.codef.xsalt.arch.XSaLTGenericLogger;
import com.itextpdf.text.DocumentException;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileSystemUtils {

	/**
	 * This method uses a native operating system call to recode video files found
	 * in the path.
	 * 
	 * @param _sStartPath Path to search for videos
	 */
	public static void recodeVideo(String _sStartPath) {
		try {
			File _oStartDirectory = new File(_sStartPath);
			String[] asDirectoryChildren = _oStartDirectory.list();
			if (asDirectoryChildren != null) {
				for (int i = 0; i < asDirectoryChildren.length; i++) {
					StringBuffer oTestFilePath = new StringBuffer(
							_oStartDirectory.getAbsolutePath() + "\\" + asDirectoryChildren[i]);

					if (!oTestFilePath.toString().endsWith(".exe") && !oTestFilePath.toString().endsWith(".txt")) {
						// String sNewFileName = oTestFilePath.toString().substring(0,
						// oTestFilePath.toString().lastIndexOf("."));
						// String sCommand = "ffmpeg -i \"" + oTestFilePath + "\" -sameq -ab 128k \"" +
						// sNewFileName + "_SC.avi\"";

						// String sCommand = "ffmpeg -i \"" + oTestFilePath + "\" -pass 1 -threads 2
						// -vcodec libx264 -b 128k -aspect 16:9 " +
						// "-s 720x408 -g 240 -bf 3 -refs 6 -b_strategy 1 -keyint_min 25 -qmin 10 " +
						// "-qmax 51 -subq 7 -partitions +parti8x8+parti4x4+partp8x8+partp4x4+partb8x8
						// -flags2 " +
						// "+mixed_refs+dct8x8+bpyramid+wpred -me_method umh -me_range 16 -coder 1
						// -trellis 1 -an \"" + sNewFileName + "_SC.avi\"";

						// Runtime.getRuntime().exec(sCommand);

					}
				}
			}
		} catch (Exception e) {
			XSaLTGenericLogger.error("", e);
		}
	}

	/**
	 * This method writes a random number to a file.
	 * 
	 * @param _nRange Exclusive maximum number
	 * @param _sPath  Path for writing file
	 */
	public static void makeRandomFile(int _nRange, String _sPath) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(_sPath));
			while (true) {
				Random r = new Random();
				int mynumber = r.nextInt(_nRange);
				out.write(mynumber);
			}
		} catch (Exception e) {
			XSaLTGenericLogger.error("", e);
		}
	}

	/**
	 * This method reads a PDF file and converts the file and writes it into a JPEG
	 * format.
	 * 
	 * @param _sPDFFilePath      Path for PDF file
	 * @param _sImageFilePathJPG Path to write JPEG file
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	public static void convertPDFtoJPEG(String _sPDFFilePath, String _sImageFilePathJPG)
			throws IOException, DocumentException, ParseException {

		File oInputFile = new File(_sPDFFilePath);
		File oOutFile = new File(_sImageFilePathJPG);

		PDF oPDFInputFile = (PDF) Behavior.getInstance("AdobePDF", "AdobePDF", null, null, null);

		oPDFInputFile.setInput(oInputFile);

		Document oDocument = new Document("doc", null, null);
		oPDFInputFile.parse(oDocument);
		oDocument.clear();

		oDocument.putAttr(Document.ATTR_PAGE, Integer.toString(1));
		oPDFInputFile.parse(oDocument);

		Node oTopNode = oDocument.childAt(0);

		oDocument.formatBeforeAfter(612, 792, null);

		int w = oTopNode.bbox.width;
		int h = oTopNode.bbox.height;

		BufferedImage oBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D oGraphics2D = oBufferedImage.createGraphics();
		oGraphics2D.setClip(0, 0, w, h);

		oGraphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		oGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		oGraphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		Context oContext = oDocument.getStyleSheet().getContext(oGraphics2D, null);
		oTopNode.paintBeforeAfter(oGraphics2D.getClipBounds(), oContext);

		ImageIO.write(oBufferedImage, "jpeg", oOutFile);

		oDocument.removeAllChildren();
		oContext.reset();
		oGraphics2D.dispose();

		oPDFInputFile.getReader().close();
		oOutFile = null;
		oDocument = null;

	}

	/**
	 * "Main" method, attempts to join files with expected arguments.
	 * 
	 * @param args Command-line arguments for executing class
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args[0].equals("joinfile")) {
			joinFiles(args[1], new Integer(args[2]).intValue(), args[3]);
		}
	}

	/**
	 * This method deletes a file from a given path
	 * 
	 * @param _sPath The path to the file to be deleted
	 */
	public static void deleteFile(String _sPath, boolean _bLogEvent) {
		File oFile = new File(_sPath);
		if (oFile.exists()) {
			if (_bLogEvent) {
				XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Deleting: " + _sPath);
			}
			oFile.delete();
		}
	}

	public static void deleteFileNew(String pathToFile) throws IOException {
		Path filePath = Paths.get(pathToFile);
		Files.delete(filePath);
	}

	public static void makeDirectory(String pathToDirectory) {
		File directory = new File(pathToDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public static void cleanDirectory(String pathToDirectory) {
		File directory = new File(pathToDirectory);
		if (directory.exists()) {
			try (Stream<Path> stream = Files.walk(Paths.get(pathToDirectory))) {
				stream.filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			directory.mkdir();
		}
	}
	
	
	public static String readFile(String pathToFile) throws IOException {
		return new String(Files.readAllBytes(new File(pathToFile).toPath()));
	}

	public static String readResourceFile(String pathToFile) throws URISyntaxException, IOException {
		Path path = Paths.get(XSaLTFileSystemUtils.class.getResource(pathToFile).toURI());
		return new String(Files.readAllBytes(path));
	}

	/**
	 * This method renames a given file.
	 * 
	 * @param _sOldPathName Original file & path
	 * @param _sNewPathName New file and path
	 */
	public static void renameFile(String _sOldPathName, String _sNewPathName) {
		File oFile1 = new File(_sOldPathName);
		File oFile2 = new File(_sNewPathName);

		boolean success = oFile1.renameTo(oFile2);
		if (!success) {
			XSaLTGenericLogger.logXSaLT(Priority.INFO_INT,
					"File '" + _sOldPathName + "' was not successfully renamed to '" + _sNewPathName + "'");
		}

	}

	/**
	 * This method deletes empty folders in a given path
	 * 
	 * @param _sFolderPath The starting folder to look in
	 *
	 */
	public static void deleteEmptyFolders(String _sFolderPath) throws FileNotFoundException {

		// deleteEmptyFolders("A:/CLEAN/CODE_REVISION_ARCHIVE");

		File oStartingFileDirectory = new File(_sFolderPath);
		List<File> oEmptyFileFolders = new ArrayList<File>();
		findEmptyFoldersInDir(oStartingFileDirectory, oEmptyFileFolders);
		List<String> oFileNames = new ArrayList<String>();
		for (File oFile : oEmptyFileFolders) {
			String s = oFile.getAbsolutePath();
			oFileNames.add(s);
		}
		for (File oFile : oEmptyFileFolders) {
			boolean isDeleted = oFile.delete();
			if (isDeleted) {
				XSaLTGenericLogger.logXSaLT(Priority.ERROR_INT, oFile.getPath() + " deleted");
			}
		}
	}

	/**
	 * This method deletes empty folders in a given path This method is used with
	 * the one above -- deleteEmptyFolders(String _sFolderPath)
	 * 
	 * @param _oFilefolder       The starting folder to look in
	 * @param _oEmptyFileFolders A listing of empty folders
	 *
	 */
	public static boolean findEmptyFoldersInDir(File _oFilefolder, List<File> _oEmptyFileFolders) {
		boolean bIsEmpty = false;
		File[] aoFilesAndDirectories = _oFilefolder.listFiles();
		List<File> oFilesDirectories = Arrays.asList(aoFilesAndDirectories);
		if (oFilesDirectories.size() == 0) {
			bIsEmpty = true;
		}
		if (oFilesDirectories.size() > 0) {
			boolean bAllDirectoriesEmpty = true;
			boolean bNoFiles = true;
			for (File oFile : oFilesDirectories) {
				if (!oFile.isFile()) {
					boolean bIsEmptyChild = findEmptyFoldersInDir(oFile, _oEmptyFileFolders);
					if (!bIsEmptyChild) {
						bAllDirectoriesEmpty = false;
					}
				}
				if (oFile.isFile()) {
					bNoFiles = false;
				}
			}
			if (bNoFiles == true && bAllDirectoriesEmpty == true) {
				bIsEmpty = true;
			}
		}
		if (bIsEmpty) {
			_oEmptyFileFolders.add(_oFilefolder);
		}
		return bIsEmpty;
	}

	/**
	 * Deletes all files in the directory given the pattern match
	 * 
	 * @param _fDirectory
	 * @param sMatch
	 * @throws FileNotFoundException
	 */
	public static void deleteFilesByWildCard(File _fDirectory, String sMatch) {
		try {
			List<File> afFiles = getFiles(_fDirectory, sMatch);
			for (File f : afFiles) {
				deleteFile(f.getAbsolutePath(), false);
			}
		} catch (FileNotFoundException e) {
			// The stack trace is not necessary to print
			//
		}

	}

	/**
	 * This method reads the given file and puts each line into an ArrayList.
	 * 
	 * @param _sFilePath    File to read
	 * @param _sBreakString String to denote line breaks
	 * @return
	 * @throws IOException
	 */
	public static synchronized ArrayList<String> writeFileToArrayList(String _sFilePath, String _sBreakString)
			throws IOException {
		ArrayList<String> oLinesFromFile = new ArrayList<String>();

		BufferedReader oBufferedReader = new BufferedReader(new FileReader(_sFilePath));
		String sTempString;
		while ((sTempString = oBufferedReader.readLine()) != null) {
			if (_sBreakString == null) {
				oLinesFromFile.add(sTempString);
			} else {
				oLinesFromFile.add(sTempString + _sBreakString);
			}
		}
		oBufferedReader.close();
		oBufferedReader = null;

		return oLinesFromFile;
	}

	/**
	 * This method deletes files in a folder, extensions are designated by a HashMap
	 * of extensions to delete
	 * 
	 * @param _sPath                The folder path to look in
	 * @param _oDeleteExtensionsMap The HashMap of file extensions safe to delete in
	 *                              that folder
	 */
	public static void deleteFolderFiles(String _sPath, HashMap<String, String> _oDeleteExtensionsMap) {
		File sDirectoryToDelete = new File(_sPath);
		if (sDirectoryToDelete.exists()) {
			String[] sFolderFiles = sDirectoryToDelete.list();
			for (int j = 0; j < sFolderFiles.length; j++) {
				String sFileName = _sPath + "/" + sFolderFiles[j];
				if (sFileName.indexOf(".") != -1) {
					String sExtension = sFileName.substring(sFileName.indexOf("."), sFileName.length()).toUpperCase();
					if (sFileName.indexOf(".") != -1 && _oDeleteExtensionsMap.containsKey(sExtension)) {
						File newFileDel = new File(_sPath + "/" + sFolderFiles[j]);
						newFileDel.delete();
					}
				}
			}
		}
	}

	/**
	 * This method copies files with a given extension from one directory to
	 * another.
	 * 
	 * @param _sLookInDirectory       Directory to search
	 * @param _sExtensionToMove       File extension to be moved
	 * @param _sMoveToDirectory       Destination directory
	 * @param _bDeleteFilesAfterwards Flag if files should be deleted after copying
	 * @throws IOException
	 */
	public static void moveFiles(String _sLookInDirectory, String _sExtensionToMove, String _sMoveToDirectory,
			boolean _bDeleteFilesAfterwards) throws IOException {

		HashMap<String, String> oFileLocations = new HashMap<String, String>();

		File sLookInDirectory = new File(_sLookInDirectory);
		String[] sFolderFiles = sLookInDirectory.list();
		for (int j = 0; j < sFolderFiles.length; j++) {
			File oTestFile = new File(_sLookInDirectory + "/" + sFolderFiles[j]);
			if (!oTestFile.isDirectory()) {
				String sFileNameSource = _sLookInDirectory + "/" + sFolderFiles[j];
				String sFileNameDestination = _sMoveToDirectory + "/" + sFolderFiles[j];
				if (sFileNameSource.indexOf(_sExtensionToMove) != -1) {
					oFileLocations.put(sFileNameSource, sFileNameDestination);
				}
			}
			oTestFile = null;
		}

		sFolderFiles = null;
		sLookInDirectory = null;

		for (Iterator<String> j = oFileLocations.keySet().iterator(); j.hasNext();) {
			String sFileNameSource = (String) j.next();
			String sFileNameDestination = (String) oFileLocations.get(sFileNameSource);
			copyFile(sFileNameSource, sFileNameDestination);

			if (_bDeleteFilesAfterwards) {
				deleteFile(sFileNameSource, false);
			}

		}

	}

	/**
	 * This method traverses and deletes a directory and each nested directory.
	 * 
	 * @param _oDirectory Directory to be deleted
	 * @return Boolean - if directory no longer exists
	 */
	public static boolean deleteDirectoryAndSubdirectories(File _oDirectory) {
		if (_oDirectory.exists()) {
			if (_oDirectory.isDirectory()) {
				String[] sChildren = _oDirectory.list();
				for (int i = 0; i < sChildren.length; i++) {
					deleteDirectoryAndSubdirectories(new File(_oDirectory, sChildren[i]));
				}
			}
			return _oDirectory.delete();
		} else {
			return true;
		}
	}

	/**
	 * This method traverses and deletes a directory and each nested directory.
	 * 
	 * @param _oDirectory Directory to be deleted
	 * @return Boolean - if directory no longer exists
	 */
	public static boolean deleteDirectoryAndSubdirectories(String _sDirectory) {
		return deleteDirectoryAndSubdirectories(new File(_sDirectory));
	}

	/**
	 * This method compresses a directory and its contents.
	 * 
	 * @param _sDirectoryToZip   Directory to compress
	 * @param _sPathToNewZipFile Location of zipped file
	 * @param _bShowFileProgress Flag if process should show progress
	 * @throws IOException
	 */
	public static void zipDirectory(String _sDirectoryToZip, String _sPathToNewZipFile, boolean _bShowFileProgress)
			throws IOException {
		zipDirectory(_sDirectoryToZip, _sPathToNewZipFile, _bShowFileProgress, Deflater.BEST_COMPRESSION);

	}

	/**
	 * This method compresses a directory and its contents.
	 * 
	 * @param _sDirectoryToZip   Directory to compress
	 * @param _sPathToNewZipFile Archive file location
	 * @param _bShowFileProgress Flag if process should show progress
	 * @param _nCompressionLevel Level of deflation during compression
	 * @throws IOException
	 */
	public static void zipDirectory(String _sDirectoryToZip, String _sPathToNewZipFile, boolean _bShowFileProgress,
			int _nCompressionLevel) throws IOException {
		File _oDirectoryToZip = new File(_sDirectoryToZip);
		File _oPathToNewZipFile = new File(_sPathToNewZipFile);
		ZipOutputStream oZipOutputStream = new ZipOutputStream(new FileOutputStream(_oPathToNewZipFile));
		oZipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
		zipFile(_oDirectoryToZip, _oDirectoryToZip, oZipOutputStream, _bShowFileProgress);
		oZipOutputStream.close();

		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Zipped 1: " + _sPathToNewZipFile);
	}

	/**
	 * This method compresses a file and adds to the specified archive.
	 * 
	 * @param _oDirectoryToZip      Directory to compress
	 * @param _oMasterFileDirectory Root file path
	 * @param _oZipOutputStream     Compression output stream object
	 * @param _bShowFileProgress    Flag if progress should be displayed in log
	 * @throws IOException
	 */
	private static void zipFile(File _oDirectoryToZip, File _oMasterFileDirectory, ZipOutputStream _oZipOutputStream,
			boolean _bShowFileProgress) throws IOException {
		File[] oFilesToZip = _oDirectoryToZip.listFiles();
		int nReadLength = 0;
		byte[] bReadBuffer = new byte[10240 * 2];
		for (int i = 0, n = oFilesToZip.length; i < n; i++) {
			if (oFilesToZip[i].isDirectory()) {
				zipFile(oFilesToZip[i], _oMasterFileDirectory, _oZipOutputStream, _bShowFileProgress);
			} else {
				FileInputStream oFileInputStream = new FileInputStream(oFilesToZip[i]);
				if (_bShowFileProgress) {
					XSaLTGenericLogger.logXSaLT(Priority.INFO_INT,
							oFilesToZip[i].getPath().substring(_oMasterFileDirectory.getPath().length() + 1));
				}
				ZipEntry oZipEntry = new ZipEntry(
						oFilesToZip[i].getPath().substring(_oMasterFileDirectory.getPath().length() + 1));
				_oZipOutputStream.putNextEntry(oZipEntry);
				while (-1 != (nReadLength = oFileInputStream.read(bReadBuffer))) {
					_oZipOutputStream.write(bReadBuffer, 0, nReadLength);
				}
				oFileInputStream.close();
			}
		}
	}

	/**
	 * This method compresses the files in the given directory- any sub-directories
	 * are ignored.
	 * 
	 * @param _sDirectoryToZip   Path of directory to compress
	 * @param _sPathToNewZipFile Archive file location
	 * @param _bShowFileProgress Flag if progress should be logged
	 * @throws IOException
	 */
	public static void zipDirectoryNoEmbeddedZips(String _sDirectoryToZip, String _sPathToNewZipFile,
			boolean _bShowFileProgress) throws IOException {
		zipDirectoryNoEmbeddedZips(_sDirectoryToZip, _sPathToNewZipFile, _bShowFileProgress, Deflater.BEST_COMPRESSION);

	}

	/**
	 * This method compresses the files in the given directory- any sub-directories
	 * are ignored.
	 * 
	 * @param _sDirectoryToZip   Path of directory to compress
	 * @param _sPathToNewZipFile Archive file location
	 * @param _bShowFileProgress Flag if progress should be logged
	 * @param _nCompressionLevel Level of deflation during compression
	 * @throws IOException
	 */
	public static void zipDirectoryNoEmbeddedZips(String _sDirectoryToZip, String _sPathToNewZipFile,
			boolean _bShowFileProgress, int _nCompressionLevel) throws IOException {
		File _oDirectoryToZip = new File(_sDirectoryToZip);
		File _oPathToNewZipFile = new File(_sPathToNewZipFile);
		ZipOutputStream oZipOutputStream = new ZipOutputStream(new FileOutputStream(_oPathToNewZipFile));
		oZipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
		zipFileNoEmbeddedZips(_oDirectoryToZip, _oDirectoryToZip, oZipOutputStream, _bShowFileProgress);
		oZipOutputStream.close();

		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Zipped 2: " + _sPathToNewZipFile);
	}

	/**
	 * This method compresses the files in the given directory object- any
	 * sub-directorys are ignored.
	 * 
	 * @param _oDirectoryToZip      Directory to compress object
	 * @param _oMasterFileDirectory Root file path object
	 * @param _oZipOutputStream     Compression output stream object
	 * @param _bShowFileProgress    Flag if compression progress should be logged
	 * @throws IOException
	 */
	private static void zipFileNoEmbeddedZips(File _oDirectoryToZip, File _oMasterFileDirectory,
			ZipOutputStream _oZipOutputStream, boolean _bShowFileProgress) throws IOException {
		File[] oFilesToZip = _oDirectoryToZip.listFiles();
		int nReadLength = 0;
		byte[] bReadBuffer = new byte[10240 * 2];
		for (int i = 0, n = oFilesToZip.length; i < n; i++) {
			if (oFilesToZip[i].isDirectory()) {
				zipFileNoEmbeddedZips(oFilesToZip[i], _oMasterFileDirectory, _oZipOutputStream, _bShowFileProgress);
			} else {

				if (!oFilesToZip[i].getPath().endsWith(".zip")) {

					FileInputStream oFileInputStream = new FileInputStream(oFilesToZip[i]);
					if (_bShowFileProgress) {
						XSaLTGenericLogger.logXSaLT(Priority.INFO_INT,
								oFilesToZip[i].getPath().substring(_oMasterFileDirectory.getPath().length() + 1));
					}
					ZipEntry oZipEntry = new ZipEntry(
							oFilesToZip[i].getPath().substring(_oMasterFileDirectory.getPath().length() + 1));
					_oZipOutputStream.putNextEntry(oZipEntry);
					while (-1 != (nReadLength = oFileInputStream.read(bReadBuffer))) {
						_oZipOutputStream.write(bReadBuffer, 0, nReadLength);
					}
					oFileInputStream.close();

				}
			}
		}
	}

	/**
	 * This method de-compresses the given archive to into the extraction path.
	 * 
	 * @param _sPathToZipFile Path of file to extract
	 * @param _sExtractPath   Destination of archive contents
	 * @throws IOException
	 */
	public static void unzipFile(String _sPathToZipFile, String _sExtractPath) throws IOException {

		File zip = new File(_sPathToZipFile);
		File extractTo = new File(_sExtractPath);

		ZipFile oZipFile = new ZipFile(zip);
		Enumeration<?> e = oZipFile.entries();
		while (e.hasMoreElements()) {
			ZipEntry oZipEntry = (ZipEntry) e.nextElement();
			File oExtractedFile = new File(extractTo, oZipEntry.getName());
			if (oZipEntry.isDirectory() && !oExtractedFile.exists()) {
				oExtractedFile.mkdirs();
			} else {
				if (!oExtractedFile.getParentFile().exists()) {
					oExtractedFile.getParentFile().mkdirs();
				}

				InputStream oInputStream = oZipFile.getInputStream(oZipEntry);
				BufferedOutputStream oBufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(oExtractedFile));

				byte[] bWriteBuffer = new byte[10240 * 2];
				int nReadLength;

				while (-1 != (nReadLength = oInputStream.read(bWriteBuffer))) {
					oBufferedOutputStream.write(bWriteBuffer, 0, nReadLength);
				}

				oInputStream.close();
				oBufferedOutputStream.close();
			}
		}

		oZipFile.close();
		zip = null;

	}

	/**
	 * This method compresses a file.
	 * 
	 * @param _sFilePathToZip    File to compress
	 * @param _sPathToNewZipFile Archive file location
	 * @throws IOException
	 */
	public static void zipSingleFile(String _sFilePathToZip, String _sPathToNewZipFile) throws IOException {

		zipSingleFile(_sFilePathToZip, _sPathToNewZipFile, Deflater.BEST_COMPRESSION);

	}

	/**
	 * This method compresses a file.
	 * 
	 * @param _sFilePathToZip    File to compress
	 * @param _sPathToNewZipFile Archive file location
	 * @param _nCompressionLevel Level of deflation during compression
	 * @throws IOException
	 */
	public static void zipSingleFile(String _sFilePathToZip, String _sPathToNewZipFile, int _nCompressionLevel)
			throws IOException {

		File oFilesToZip = new File(_sFilePathToZip);
		ZipOutputStream oZipOutputStream = new ZipOutputStream(new FileOutputStream(_sPathToNewZipFile));
		oZipOutputStream.setLevel(_nCompressionLevel);
		FileInputStream oFileInputStream = new FileInputStream(_sFilePathToZip);

		oZipOutputStream.putNextEntry(new ZipEntry(oFilesToZip.getPath()
				.substring(oFilesToZip.getPath().lastIndexOf("\\") + 1, oFilesToZip.getPath().length())));

		byte[] oByteBuffer = new byte[10240 * 2];
		int nLength;
		while ((nLength = oFileInputStream.read(oByteBuffer)) > 0) {
			oZipOutputStream.write(oByteBuffer, 0, nLength);
		}
		oZipOutputStream.closeEntry();
		oFileInputStream.close();
		oZipOutputStream.close();

		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Zipped 3: " + _sPathToNewZipFile);
	}

	/**
	 * This method creates a folder
	 * 
	 * @param _sPath The path to the folder to be created
	 */
	public static void createFileFolder(String _sPath) {
		File oDirectory = new File(_sPath);
		if (!oDirectory.exists()) {
			oDirectory.mkdirs();
		}
	}

	/**
	 * This method writes a String out to a file as text
	 * 
	 * @param _sStringToWrite The String to write to the file
	 * @param _sFilePath      The path to the file you wish to write to (create)
	 * @throws IOException
	 */
	public static synchronized void writeStringToFile(String _sStringToWrite, String _sFilePath) throws IOException {
		Files.write(Paths.get(_sFilePath), _sStringToWrite.getBytes());
		XSaLTGenericLogger.logXSaLT(Priority.INFO_INT, "Writing: " + _sFilePath);
	}

	/**
	 * This method writes a List of Strings to a file, split by the given break
	 * string.
	 * 
	 * @param _lsList
	 * @param _sFilePath
	 * @param _sBreakString can be null
	 * @throws IOException
	 */
	public static synchronized void writeStringListToFile(List<String> _lsList, String _sFilePath, String _sBreakString)
			throws IOException {
		StringBuffer sbBuffer = new StringBuffer();

		if (_sBreakString == null) {
			_sBreakString = "";
		}

		for (String sLine : _lsList) {
			sbBuffer.append(sLine);
			sbBuffer.append(_sBreakString);
		}

		writeStringBufferToFile(sbBuffer, _sFilePath);
	}

	/**
	 * This method writes a StringBuffer out to a file as text
	 * 
	 * @param _oStringBufferToWrite The String to write to the file
	 * @param _sFilePath            The path to the file you wish to write to
	 *                              (create)
	 * @throws IOException
	 */
	public static synchronized void writeStringBufferToFile(StringBuffer _oStringBufferToWrite, String _sFilePath)
			throws IOException {
		writeStringToFile(_oStringBufferToWrite.toString(), _sFilePath);
	}

	/**
	 * This method reads a file into a StringBuffer
	 * 
	 * @param _sFilePath    The file path for the text file
	 * @param _sBreakString The return (end of line character), if desired. This can
	 *                      be null.
	 * @return A StringBuffer with the contents of the file
	 * @throws IOException
	 */
	public static synchronized StringBuffer writeFileToStringBuffer(String _sFilePath, String _sBreakString)
			throws IOException {
		StringBuffer oTempStringbuffer = new StringBuffer();

		BufferedReader oBufferedReader = new BufferedReader(new FileReader(_sFilePath));
		String sTempString;
		while ((sTempString = oBufferedReader.readLine()) != null) {
			if (_sBreakString == null) {
				oTempStringbuffer.append(sTempString);
			} else {
				oTempStringbuffer.append(sTempString + _sBreakString);
			}
		}
		oBufferedReader.close();
		oBufferedReader = null;

		return oTempStringbuffer;
	}

	/**
	 * This method adds each line that contains the given string into a StringBuffer
	 * (similar to 'grep' command).
	 * 
	 * @param _sFilePath
	 * @param _sStringMatch
	 * @param _sBreakString
	 * @return StringBuffer with matching lines from file.
	 * @throws IOException
	 */
	public static synchronized StringBuffer writeFileToStringBufferIfStringMatch(String _sFilePath,
			String _sStringMatch, String _sBreakString) throws IOException {
		StringBuffer oTempStringbuffer = new StringBuffer();

		BufferedReader oBufferedReader = new BufferedReader(new FileReader(_sFilePath));
		String sTempString;
		while ((sTempString = oBufferedReader.readLine()) != null) {
			if (_sBreakString == null) {
				if (sTempString.indexOf(_sStringMatch) != -1) {
					oTempStringbuffer.append(sTempString);
				}
			} else {
				if (sTempString.indexOf(_sStringMatch) != -1) {
					oTempStringbuffer.append(sTempString + _sBreakString);
				}
			}
		}
		oBufferedReader.close();
		oBufferedReader = null;

		return oTempStringbuffer;
	}

	/**
	 * This method reads a file into a string buffer breaking each line with a break
	 * string.
	 * 
	 * @param _sFilePath            File to read
	 * @param _sBreakString         Line break string
	 * @param _bFixNonQuotedHeader  Flag if quotes should be escaped
	 * @param _bChangeRowGenIdToOld Flag if ROWGENID column name should be changed
	 * @return StringBuffer with contents of file
	 * @throws IOException
	 */
	public static synchronized StringBuffer writeFileToStringBuffer(String _sFilePath, String _sBreakString,
			boolean _bFixNonQuotedHeader, boolean _bChangeRowGenIdToOld) throws IOException {
		StringBuffer oTempStringbuffer = new StringBuffer();

		BufferedReader oBufferedReader = new BufferedReader(new FileReader(_sFilePath));
		String sTempString;
		boolean bFirstLineFlag = true;
		while ((sTempString = oBufferedReader.readLine()) != null) {
			if (_bChangeRowGenIdToOld) {
				if (bFirstLineFlag) {
					if (sTempString.toLowerCase().indexOf("rowgenid") != -1) {
						sTempString = sTempString.toLowerCase().replaceAll("rowgenid", "rowgenidold");
					}
				}
			}
			if (_bFixNonQuotedHeader) {
				if (bFirstLineFlag) {
					sTempString = "" + sTempString.replaceAll(",", "\",\"") + "\"";
				}
			}
			if (_sBreakString == null) {
				oTempStringbuffer.append(sTempString);
			} else {
				oTempStringbuffer.append(sTempString + _sBreakString);
			}
			bFirstLineFlag = false;
		}
		oBufferedReader.close();

		return oTempStringbuffer;
	}

	/**
	 * This method reads a file into a StringBuffer, breaking each segment of
	 * characters with a break character
	 * 
	 * @param _sFilePath     The file path for the text file
	 * @param _nRecordLength The number of characters before a break character is to
	 *                       be inserted
	 * @param _sBreakString  The return (end of line character), if desired. This
	 *                       can be null.
	 * @return A StringBuffer with the contents of the file
	 * @throws IOException
	 */
	public static synchronized StringBuffer writeFileToStringBufferWithBreakChars(String _sFilePath, int _nRecordLength,
			String _sBreakString) throws IOException {
		StringBuffer oReadStringbuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sFilePath, null);
		StringBuffer sFinalStringbuffer = new StringBuffer();
		StringBuffer sLineStringbuffer = new StringBuffer();
		int i = 0;
		for (i = 0; i < oReadStringbuffer.length(); i++) {
			if (i > 0 && (i % _nRecordLength) == 0) {
				sFinalStringbuffer.append(sLineStringbuffer + _sBreakString);
				sLineStringbuffer = new StringBuffer();
			}
			sLineStringbuffer.append(oReadStringbuffer.charAt(i));
		}

		sFinalStringbuffer.append(sLineStringbuffer + _sBreakString);
		sFinalStringbuffer.append(oReadStringbuffer.substring(i, oReadStringbuffer.length()));

		return sFinalStringbuffer;
	}

	/**
	 * This method splits a binary file
	 * 
	 * @param _sFileToSplitPath              The path of the binary file you wish to
	 *                                       split
	 * @param _sTargetDirectoryForSplitFiles The target directory for the split
	 *                                       files
	 * @param _nMBChunkSize                  The split (chunk) size in MB. If this
	 *                                       is set to '0', it will do 4MB chunks by
	 *                                       default
	 * @param _bDeleteSource                 Flag to delete the source when done
	 * @throws IOException
	 */
	public static void splitBinaryFiles(String _sFileToSplitPath, String _sTargetDirectoryForSplitFiles,
			int _nMBChunkSize, boolean _bDeleteSource) throws IOException {
		if (_nMBChunkSize == 0) {
			_nMBChunkSize = 4;
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(_sFileToSplitPath));
		int nPart = 1;
		int nCycles = 1;
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(_sTargetDirectoryForSplitFiles + getRealFileName(_sFileToSplitPath) + "."
						+ XSaLTStringUtils.padLeftWithCharacter(new Integer(nPart).toString(), '0', 3)));
		byte[] buffer = new byte[(1024 * _nMBChunkSize)];
		int b = in.read(buffer);
		while (b >= 0) {
			out.write(buffer, 0, b);
			if (nCycles == 1000) {
				out.close();
				nPart = nPart + 1;
				out = new BufferedOutputStream(
						new FileOutputStream(_sTargetDirectoryForSplitFiles + getRealFileName(_sFileToSplitPath) + "."
								+ XSaLTStringUtils.padLeftWithCharacter(new Integer(nPart).toString(), '0', 3)));
				nCycles = 0;
			}
			b = in.read(buffer);
			nCycles = nCycles + 1;
		}
		in.close();
		out.close();
		if (_bDeleteSource) {
			File oFile = new File(_sFileToSplitPath);
			oFile.delete();
			oFile = null;
		}
	}

	/**
	 * This method joins binary files
	 * 
	 * @param _oRegExHashMap                 A HashMap containing the file names
	 * @param _sTargetDirectoryForJoinedFile The target directory for the joined
	 *                                       file
	 * @param _nMBChunkSize                  The split (chunk) size in MB. If this
	 *                                       is set to '0', it will do 4MB chunks by
	 *                                       default
	 * @param _bDeletePieces                 Flag to delete the pieces when done
	 * @throws IOException
	 */
	public static void joinFiles(LinkedHashMap<String, String> _oRegExHashMap, String _sTargetDirectoryForJoinedFile,
			int _nMBChunkSize, boolean _bDeletePieces, String sManualFileName) throws IOException {
		/*
		 * String sTargetDirectoryForJoinedFile = "C:/_WORKING/"; LinkedHashMap
		 * oRegExHashMap = new LinkedHashMap(); oRegExHashMap.put("nb1.mpg.001",
		 * "Z:/TO_CD/bbb/nb1.mpg.001"); oRegExHashMap.put("nb1.mpg.002",
		 * "Z:/TO_CD/bbb/nb1.mpg.002"); oRegExHashMap.put("nb1.mpg.003",
		 * "Z:/TO_CD/bbb/nb1.mpg.003"); oRegExHashMap.put("nb1.mpg.004",
		 * "Z:/TO_CD/bbb/nb1.mpg.004"); oRegExHashMap.put("nb1.mpg.005",
		 * "Z:/TO_CD/bbb/nb1.mpg.005"); oRegExHashMap.put("nb1.mpg.006",
		 * "Z:/TO_CD/bbb/nb1.mpg.006"); oRegExHashMap.put("nb1.mpg.007",
		 * "Z:/TO_CD/bbb/nb1.mpg.007"); oRegExHashMap.put("nb1.mpg.008",
		 * "Z:/TO_CD/bbb/nb1.mpg.008"); oRegExHashMap.put("nb1.mpg.009",
		 * "Z:/TO_CD/bbb/nb1.mpg.009"); oRegExHashMap.put("nb1.mpg.010",
		 * "Z:/TO_CD/bbb/nb1.mpg.010"); oRegExHashMap.put("nb1.mpg.011",
		 * "Z:/TO_CD/bbb/nb1.mpg.011"); oRegExHashMap.put("nb1.mpg.012",
		 * "Z:/TO_CD/bbb/nb1.mpg.012"); oRegExHashMap.put("nb1.mpg.013",
		 * "Z:/TO_CD/bbb/nb1.mpg.013"); oRegExHashMap.put("nb1.mpg.014",
		 * "Z:/TO_CD/bbb/nb1.mpg.014"); oRegExHashMap.put("nb1.mpg.015",
		 * "Z:/TO_CD/bbb/nb1.mpg.015"); oRegExHashMap.put("nb1.mpg.016",
		 * "Z:/TO_CD/bbb/nb1.mpg.016"); oRegExHashMap.put("nb1.mpg.017",
		 * "Z:/TO_CD/bbb/nb1.mpg.017"); oRegExHashMap.put("nb1.mpg.018",
		 * "Z:/TO_CD/bbb/nb1.mpg.018");
		 */

		if (_nMBChunkSize == 0) {
			_nMBChunkSize = 4;
		}

		BufferedInputStream in = null;
		Iterator<String> oIteratorFirst = _oRegExHashMap.keySet().iterator();
		String sKeyFirst = oIteratorFirst.next();

		String sFileName = "";

		if (sManualFileName != null) {
			sFileName = sManualFileName;
		} else {
			sFileName = XSaLTFileSystemUtils.getRealFileNameFromMultipart(_oRegExHashMap.get(sKeyFirst).toString());
		}

		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(_sTargetDirectoryForJoinedFile + sFileName));

		for (Iterator<String> i = _oRegExHashMap.keySet().iterator(); i.hasNext();) {
			String sKey = i.next();
			String sValue = _oRegExHashMap.get(sKey);
			in = new BufferedInputStream(new FileInputStream(sValue));
			byte[] buffer = new byte[(1024 * _nMBChunkSize)];
			int b = in.read(buffer);
			while (b >= 0) {
				out.write(buffer, 0, b);
				b = in.read(buffer);
			}
			in.close();
			if (_bDeletePieces) {
				File oFile = new File(sValue);
				oFile.delete();
				oFile = null;
			}
		}
		out.close();
	}

	/**
	 * This method joins pieces of a previously separated file.
	 * 
	 * @param _sFileName      Base file name to join
	 * @param _nPieces        Number of pieces
	 * @param _sFileDirectory Directory to find files to join
	 * @throws IOException
	 */
	public static void joinFiles(String _sFileName, int _nPieces, String _sFileDirectory) throws IOException {
		LinkedHashMap<String, String> oRegExHashMap = new LinkedHashMap<String, String>();
		for (int i = 1; i <= _nPieces; i++) {
			String sPieceNumber = XSaLTStringUtils.padLeftWithCharacter(new Integer(i).toString(), '0', 3);
			oRegExHashMap.put(_sFileName + "." + sPieceNumber, _sFileDirectory + "/" + _sFileName + "." + sPieceNumber);
		}
		XSaLTFileSystemUtils.joinFiles(oRegExHashMap, _sFileDirectory + "/", 2048, false, null);

	}

	/**
	 * This method created the piece list for a split file
	 * 
	 * @param _sFilePathToLastPart The path to the last part of the split file
	 * @param _bHasPart000         Flag if there is a .000 file (usually the first)
	 * @return LinkedHashed map of the file names and locations
	 */
	public static LinkedHashMap<String, String> createFilePiecesList(String _sFilePathToLastPart,
			boolean _bHasPart000) {

		LinkedHashMap<String, String> oFileToJoinMap = new LinkedHashMap<String, String>();
		String sRealFileName = XSaLTFileSystemUtils.getRealFileNameFromMultipart(_sFilePathToLastPart);
		String sRealFilePath = XSaLTFileSystemUtils.getRealFileNamePathOnlyFromMultipart(_sFilePathToLastPart);
		int nPieces = XSaLTFileSystemUtils.getNumberOfPartsFromFileNameMultipart(_sFilePathToLastPart);
		int nStartPart = 1;
		if (_bHasPart000) {
			nStartPart = 0;
		}
		for (int i = nStartPart; i < nPieces + 1; i++) {
			String sFileName = sRealFileName + "."
					+ XSaLTStringUtils.padLeftWithCharacter(new Integer(i).toString(), '0', 3);
			String sFilePart = sRealFilePath + sFileName;
			oFileToJoinMap.put(sFileName, sFilePart);
		}
		return oFileToJoinMap;
	}

	/**
	 * This method gets the real file name (with extension) from the multipart name
	 * (used in the splitter/joiner)
	 * 
	 * @param _sFilePath The file path to check
	 * @return The real name of the file
	 */
	public static String getRealFileName(String _sFilePath) {
		String sFileName = _sFilePath.substring(_sFilePath.lastIndexOf("/") + 1, _sFilePath.length());
		return sFileName;
	}

	/**
	 * This method gets the real file name (no extension) from the multipart name
	 * (used in the splitter/joiner)
	 * 
	 * @param _sFilePath The file path to check
	 * @return The real file name of the file
	 */
	private static String getRealFileNameFromMultipart(String _sFilePath) {
		String sFileName = _sFilePath.substring(_sFilePath.lastIndexOf("/") + 1, _sFilePath.lastIndexOf("."));
		return sFileName;
	}

	/**
	 * This method gets the real file name path from the multipart name (used in the
	 * splitter/joiner)
	 * 
	 * @param _sFilePath The file path to check
	 * @return The real file name path of the file
	 */
	private static String getRealFileNamePathOnlyFromMultipart(String _sFilePath) {
		String sFileName = _sFilePath.substring(0, _sFilePath.lastIndexOf("/") + 1);
		return sFileName;
	}

	/**
	 * This method gets the number of parts from the multipart name (used in the
	 * splitter/joiner)
	 * 
	 * @param _sFilePath The file path to check
	 * @return The number of parts for a split file
	 */
	private static int getNumberOfPartsFromFileNameMultipart(String _sFilePath) {
		Integer oTempInteger = new Integer(_sFilePath.substring(_sFilePath.lastIndexOf(".") + 1, _sFilePath.length()));
		return oTempInteger.intValue();
	}

	/**
	 * This method copies a file
	 * 
	 * @param _sTargetFile      The path of the target file
	 * @param _sDestinationFile The path for the destination file
	 * @param _nMBChunkSize     The size of the buffer in MB. If '0' is used, a 4MB
	 *                          buffer is assumed
	 * @throws IOException
	 */
	public static void copyFile(String _sTargetFile, String _sDestinationFile) throws IOException {
		Files.copy(new File(_sTargetFile).toPath(), new File(_sDestinationFile).toPath());
	}

	/**
	 * This method converts a standard comma-delimited file to a tab delimited file
	 * 
	 * @param _sCommaFilePath
	 * @param _sNewFilePath
	 * @throws IOException
	 */
	public static StringBuffer convertCommaDelimitedToTabDelimitedFile(String _sCommaFilePath, String _sNewFilePath)
			throws IOException {
		StringBuffer oCommaStringBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sCommaFilePath, null);
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceStdCommaDelimWithTabs(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString().trim()));
		XSaLTFileSystemUtils.writeStringToFile(oCommaStringBuffer.toString(), _sNewFilePath);
		return oCommaStringBuffer;
	}

	/**
	 * This method converts a standard comma-delimited file to a tab delimited file
	 * 
	 * @param _sCommaFilePath
	 * @param _sNewFilePath
	 * @throws IOException
	 */
	public static StringBuffer convertCommaDelimitedToTabDelimitedFileInsertHeader(String _sCommaFilePath,
			String _sNewFilePath, String _sHeaderInsert) throws IOException {
		StringBuffer oCommaStringBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sCommaFilePath, null);
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceStdCommaDelimWithTabs(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(
				_sHeaderInsert + "\n" + XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString().trim()));
		XSaLTFileSystemUtils.writeStringToFile(oCommaStringBuffer.toString(), _sNewFilePath);
		return oCommaStringBuffer;
	}

	/**
	 * This method converts a standard comma-delimited file to a tab delimited file
	 * 
	 * @param _sCommaFilePath
	 * @param _sNewFilePath
	 * @throws IOException
	 */
	public static StringBuffer convertCommaDelimitedToTabDelimitedFile(String _sCommaFilePath, String _sNewFilePath,
			boolean _bFixNonQuotedHeader, boolean _bChangeRowGenIdToOld) throws IOException {
		StringBuffer oCommaStringBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sCommaFilePath, null,
				_bFixNonQuotedHeader, _bChangeRowGenIdToOld);
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceStdCommaDelimWithTabs(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString().trim()));
		XSaLTFileSystemUtils.writeStringToFile(oCommaStringBuffer.toString(), _sNewFilePath);
		return oCommaStringBuffer;
	}

	public static void convertDelimitedFileToTabbedFile(String _sOldFilePath, String _sNewFilePath, String _sDelimiter)
			throws IOException {
		ArrayList<String> lsOldFile = writeFileToArrayList(_sOldFilePath, null);
		StringBuffer sbNewFile = new StringBuffer();

		for (String sLine : lsOldFile) {
			sbNewFile.append(XSaLTStringUtils.replaceDelimitersInString(sLine, _sDelimiter, "\t"));
			sbNewFile.append("\n");
		}

		writeStringBufferToFile(sbNewFile, _sNewFilePath);
	}

	/**
	 * This method converts a Mickey Moused comma-delimited file to a tab delimited
	 * file
	 * 
	 * @param _sCommaFilePath
	 * @param _sNewFilePath
	 * @throws IOException
	 */
	public static StringBuffer convertMickeyMousedCommaDelimitedToTabDelimitedFile(String _sCommaFilePath,
			String _sNewFilePath) throws IOException {
		StringBuffer oCommaStringBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sCommaFilePath, "\n");
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.getCleanMickeyMousedString(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString()));
		XSaLTFileSystemUtils.writeStringToFile(oCommaStringBuffer.toString(), _sNewFilePath);
		return oCommaStringBuffer;
	}

	/**
	 * 
	 * @param _sCommaFilePath
	 * @param _sNewFilePath
	 * @return
	 * @throws IOException
	 */
	public static StringBuffer convertSpecialCommaDelimitedFileToTabDelimitedFile(String _sCommaFilePath,
			String _sNewFilePath) throws IOException {
		StringBuffer oCommaStringBuffer = XSaLTFileSystemUtils.writeFileToStringBuffer(_sCommaFilePath, "\n");
		oCommaStringBuffer = new StringBuffer(
				XSaLTStringUtils.getCleanSpecialDelimitedString(oCommaStringBuffer.toString().trim()));
		oCommaStringBuffer = new StringBuffer(XSaLTStringUtils.regExReplaceQuotes(oCommaStringBuffer.toString()));
		XSaLTFileSystemUtils.writeStringToFile(oCommaStringBuffer.toString(), _sNewFilePath);
		return oCommaStringBuffer;
	}

	/**
	 * Returns a file object for a directory. Returns an exception if there it is
	 * not a directory
	 * 
	 * @param _sDirectory
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getDirectoryObject(String _sDirectory) throws FileNotFoundException {
		// verify that the incoming directory is valid
		File oDir = new File(_sDirectory);
		if (!oDir.isDirectory()) {
			throw new FileNotFoundException("Directory given (" + _sDirectory + ") is not a directory");
		}
		return oDir;
	}

	/**
	 * Get a file object for a given directory/file name combo. Gets the first
	 * instance during a wild card search.
	 * 
	 * @param _fDataRootDirectory
	 * @param _sFileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getFile(File _fDataRootDirectory, String _sFileName) {
		try {
			return getFiles(_fDataRootDirectory, _sFileName).get(0);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Get a list of file objects for a given directory/file name combo. Allows a
	 * wild card search.
	 * 
	 * @param _fDataRootDirectory
	 * @param _sFileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<File> getFiles(File _fDataRootDirectory, String _sFileName) throws FileNotFoundException {
		// check the file name. if it contains an ending asterisk, do a wildcard
		// match. Otherwise, take the file as is.
		ArrayList<File> oFile;
		String[] sFiles;

		oFile = new ArrayList<File>();
		_sFileName = _sFileName.trim();
		if (_sFileName.endsWith("*") || _sFileName.charAt(0) == '*') { // pattern
																		// match
			if (_sFileName.endsWith("*")) {
				_sFileName = _sFileName.substring(0, _sFileName.lastIndexOf('*'));
			} else if (_sFileName.charAt(0) == '*') {
				_sFileName = _sFileName.substring(1);
			}

			sFiles = _fDataRootDirectory.list();
			for (String s : sFiles) {
				if (s.toLowerCase().contains(_sFileName.toLowerCase())) {
					oFile.add(new File(_fDataRootDirectory, s));
					break;
				}
			}
			if (oFile.size() == 0) {
				throw new FileNotFoundException("File not found with matching string:" + _sFileName);
			}
		} else { // use direct file
			oFile.add(new File(_fDataRootDirectory, _sFileName));
			if (oFile.size() == 0) {
				throw new FileNotFoundException("File not found with direct lookup:" + _sFileName);
			}
		}

		return oFile;
	}

	/**
	 * Concatenates XML files in the same directory, removing XML document tag &
	 * specified XML tag.
	 * 
	 * @param _fDirectory     The directory with the files.
	 * @param _sFileWildCard  The Wild card match to get the file names (only
	 *                        supports front and back matches)
	 * @param _sFinalFileName The final file name (if it is null it will be
	 *                        generated).
	 * @param _nMBChunkSize   Number of megabytes to copy at once (if 0, will use
	 *                        default).
	 * @param _sXMLTag        XML tag name to create as the main tag for the
	 *                        document.
	 * @return String filename that is written to as the files are concatenated.
	 * @throws Exception
	 */
	public static synchronized String cleanAndConcatXMLFiles(File _fDirectory, String _sFileWildCard,
			String _sFinalFileName, int _nMBChunkSize, String _sXMLTag) throws Exception {
		File fFile = null;

		if (_nMBChunkSize == 0) {
			_nMBChunkSize = 4;
		}

		boolean bEndWildCard;

		if (_sFileWildCard.endsWith("*")) {
			bEndWildCard = true;
			_sFileWildCard = _sFileWildCard.substring(0, _sFileWildCard.lastIndexOf("*"));
		} else if (_sFileWildCard.charAt(0) == '*') {
			_sFileWildCard = _sFileWildCard.substring(1);
			bEndWildCard = false;
		} else {
			throw new FileNotFoundException(
					"Wild card match not implemented for given match string: " + _sFileWildCard);
		}

		if (_sFinalFileName == null) {
			_sFinalFileName = _sFileWildCard + ".cat.xml";
		}

		if (_sXMLTag != null && _sXMLTag.trim().length() > 0) {
			File fDest = new File(_fDirectory, _sFinalFileName);

			deleteFile(fDest.getAbsolutePath(), false);

			List<String> lsFiles = Arrays.asList(_fDirectory.list());
			Collections.sort(lsFiles);

			BufferedInputStream in;
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fDest));
			byte[] buffer = new byte[(1024 * _nMBChunkSize)];

			{
				// put in beginning tag
				String sTemp = "<" + _sXMLTag + ">";
				char[] acTemp = sTemp.toCharArray();

				for (int i = 0; i < acTemp.length; i++) {
					buffer[i] = (byte) acTemp[i];
				}

				out.write(buffer, 0, acTemp.length);
			}

			for (String s : lsFiles) {
				if (!bEndWildCard && s.toLowerCase().endsWith(_sFileWildCard.toLowerCase())) {
					fFile = new File(_fDirectory, s);
				} else if (bEndWildCard && s.toLowerCase().startsWith(_sFileWildCard.toLowerCase())) {
					fFile = new File(_fDirectory, s);
				}

				if (fFile != null) {
					in = new BufferedInputStream(new FileInputStream(fFile));

					int nBytesRead = 0;
					String sFileContents = "";
					while ((nBytesRead = in.read(buffer)) > 0) {
						sFileContents = new String(buffer, 0, nBytesRead);
						// cleaning...
						sFileContents = sFileContents.replaceAll("</?" + _sXMLTag + ">", "");
						sFileContents = sFileContents.replaceAll("<\\?xml .*\\?>", "");

						out.write(sFileContents.getBytes(), 0, sFileContents.getBytes().length);
					}

					in.close();
				}

				fFile = null;
			}

			{
				// put in ending tag
				String sTemp = "</" + _sXMLTag + ">";
				char[] acTemp = sTemp.toCharArray();

				for (int i = 0; i < acTemp.length; i++) {
					buffer[i] = (byte) acTemp[i];
				}

				out.write(buffer, 0, acTemp.length);
			}

			out.flush();
			out.close();
		} else {
			throw new Exception("XML Tag attribute cannot be null or blank.");
		}

		return _sFinalFileName;
	}

	/**
	 * Concatenates Files in the same directory
	 * 
	 * @param _fDirectory     The directory with the files
	 * @param _sFileWildCard  The Wild card match to get the file names (only
	 *                        supports front and back matches)
	 * @param _sFinalFileName The final file name (if null, will be generated)
	 * @param _nMBChunkSize   Number of megabytes to copy at once (if 0, will use
	 *                        default).
	 * @param _bXMLStyle      Add in XML tags around the data.
	 * @return The final file name
	 * @throws Exception
	 */
	public static String concatFiles(File _fDirectory, String _sFileWildCard, String _sFinalFileName, int _nMBChunkSize,
			String _sXMLTag) throws Exception {

		File fFile = null;

		if (_nMBChunkSize == 0) {
			_nMBChunkSize = 4;
		}

		boolean bEndWildCard;

		if (_sFileWildCard.endsWith("*")) {
			_sFileWildCard = _sFileWildCard.substring(0, _sFileWildCard.lastIndexOf('*'));
			bEndWildCard = true;

		} else if (_sFileWildCard.charAt(0) == '*') {
			_sFileWildCard = _sFileWildCard.substring(1);
			bEndWildCard = false;
		} else {
			throw new FileNotFoundException(
					"Wild card match not implemented for given match string: " + _sFileWildCard);
		}

		if (_sFinalFileName == null) {
			_sFinalFileName = _sFileWildCard + ".cat";
		}

		if (_sXMLTag != null) {
			_sFinalFileName += ".xml";
		}

		File fDest = new File(_fDirectory, _sFinalFileName);

		deleteFile(fDest.getAbsolutePath(), false);

		List<String> lsFiles = Arrays.asList(_fDirectory.list());
		Collections.sort(lsFiles);

		BufferedInputStream in;
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fDest));
		byte[] buffer = new byte[(1024 * _nMBChunkSize)];

		if (_sXMLTag != null) {
			String sTemp = "<" + _sXMLTag + ">";
			char[] acTemp = sTemp.toCharArray();

			for (int i = 0; i < acTemp.length; i++) {
				buffer[i] = (byte) acTemp[i];
			}

			out.write(buffer, 0, acTemp.length);
		}

		for (String s : lsFiles) {
			if (!bEndWildCard && s.toLowerCase().endsWith(_sFileWildCard.toLowerCase())) {
				fFile = new File(_fDirectory, s);
			} else if (bEndWildCard && s.toLowerCase().startsWith(_sFileWildCard.toLowerCase())) {
				fFile = new File(_fDirectory, s);
			}

			if (fFile != null) {
				in = new BufferedInputStream(new FileInputStream(fFile));

				int b = in.read(buffer);
				while (b >= 0) {
					out.write(buffer, 0, b);
					b = in.read(buffer);
				}
				in.close();
			}

			fFile = null;
		}

		if (_sXMLTag != null) {
			String sTemp = "</" + _sXMLTag + ">";
			char[] acTemp = sTemp.toCharArray();

			for (int i = 0; i < acTemp.length; i++) {
				buffer[i] = (byte) acTemp[i];
			}

			out.write(buffer, 0, acTemp.length);
		}

		out.flush();
		out.close();
		return _sFinalFileName;

	}

	/**
	 * Removes blank lines from the given file.
	 * 
	 * @param _sFilePath
	 * @param _sBreakString
	 * @throws IOException
	 */
	public static void removeBlankLines(String _sFilePath, String _sBreakString) throws IOException {
		ArrayList<String> lsFile = writeFileToArrayList(_sFilePath, _sBreakString);
		ArrayList<String> lsNew = new ArrayList<String>();

		for (String sOld : lsFile) {
			sOld = sOld.trim();
			if (sOld.length() > 0) {
				lsNew.add(sOld);
			}
		}

		writeStringListToFile(lsNew, _sFilePath, _sBreakString);
	}

	/**
	 * This method counts the number of lines with data in a given file.
	 * 
	 * @param _sFilePath    File to analyze
	 * @param _sBreakString String to denote line breaks
	 * @return Count of lines with data
	 * @throws IOException
	 */
	public static long countNumberOfDataLines(String _sFilePath, String _sBreakString) throws IOException {
		long lCount = 0;

		try {
			ArrayList<String> lsFile = writeFileToArrayList(_sFilePath, _sBreakString);

			for (String sLine : lsFile) {
				sLine = sLine.trim();
				if (sLine.length() > 0) {
					lCount++;
				}
			}

			return lCount;
		} catch (FileNotFoundException e) {
			return 0;
		}

	}

	/**
	 * This method copies files from one directory to another.
	 * 
	 * @param _oSrcPath Source path
	 * @param _oDstPath Destination
	 * @throws IOException
	 */
	public static void copyDirectory(File _oSrcPath, File _oDstPath) throws IOException {
		if (_oSrcPath.isDirectory()) {
			if (!_oDstPath.exists()) {
				_oDstPath.mkdir();
			}
			String files[] = _oSrcPath.list();

			for (int i = 0; i < files.length; i++) {
				copyDirectory(new File(_oSrcPath, files[i]), new File(_oDstPath, files[i]));
			}

		} else {
			if (!_oSrcPath.exists()) {
				throw new IOException("File or directory does not exist: " + _oSrcPath.getAbsolutePath());
			} else {
				FileInputStream in = new FileInputStream(_oSrcPath);
				FileOutputStream out = new FileOutputStream(_oDstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[4096];

				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}
		}

	}
}
