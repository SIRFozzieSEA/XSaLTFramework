package com.codef.xsalt.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.codef.xsalt.arch.XSaLTConstants;
import com.codef.xsalt.arch.XSaLTStreamGobbler;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTCipherUtils
{

	/**
	 * The instace of javax.crypto.Cipher
	 */
	private Cipher ioDesCipher = null;

	/**
	 * The instance of javax.crypto.SecretKey
	 */
	private SecretKey ioDesSecretKey = null;

	/**
	 * The instance default encryption scheme
	 */
	public String isDefaultEncryptionScheme = "DESede";

	/**
	 * Primary constructor
	 */
	public XSaLTCipherUtils()
	{
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	/**
	 * Alternate constructor specifying encryption scheme
	 * 
	 * @param _sDefaultEncryptionScheme The encryption scheme to use with this instance
	 */
	public XSaLTCipherUtils(String _sDefaultEncryptionScheme)
	{
		//		/Security.addProvider(new com.sun.crypto.provider.SunJCE());
		isDefaultEncryptionScheme = _sDefaultEncryptionScheme;
	}

	/**
	 * Initializes the cipher for the instance
	 * 
	 * @param _oDesCipher The cipher
	 * @param _nMode The mode of the cypher (Cipher.ENCRYPT_MODE/Cipher.DECRYPT_MODE)
	 * @param _oSecretKey The secret key
	 * @throws InvalidKeyException
	 */
	public void setAndInitCipher(Cipher _oDesCipher, int _nMode, SecretKey _oSecretKey) throws InvalidKeyException
	{
		ioDesCipher = _oDesCipher;
		ioDesCipher.init(_nMode, _oSecretKey);
	}

	/**
	 * Returns the cipher instance
	 * 
	 * @return javax.crypto.Cipher - The cipher instance
	 */
	public Cipher getCipher()
	{
		return ioDesCipher;
	}

	/**
	 * Writes the key to a file
	 * 
	 * @param _sKeyFilePath The path for the key file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeKeytoFile(String _sKeyFilePath) throws FileNotFoundException, IOException
	{
		_sKeyFilePath = _sKeyFilePath + "." + XSaLTConstants.XS_EXTENSION_FOR_KEY_FILES;
		FileOutputStream oFileOutputStream = new FileOutputStream(_sKeyFilePath);
		ObjectOutputStream oObjectOutputStream = new ObjectOutputStream(oFileOutputStream);
		oObjectOutputStream.writeObject(ioDesSecretKey);
		oObjectOutputStream.flush();
		oFileOutputStream.close();

	}

	/**
	 * Generates the key for the instance
	 * 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public void generateKey() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		KeyGenerator oKeyGenerator = KeyGenerator.getInstance(isDefaultEncryptionScheme);
		SecretKey oTempSecretKey = oKeyGenerator.generateKey();
		setSecretKey(oTempSecretKey, "genr");
		setAndInitCipher(Cipher.getInstance(isDefaultEncryptionScheme), Cipher.ENCRYPT_MODE, getSecretKey());
	}

	/**
	 * Reads in the key file for the instance
	 * 
	 * @param _sKeyFilePath The path to the key file
	 * @throws ClassNotFoundException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public void readKeyFile(String _sKeyFilePath) throws ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException
	{
		_sKeyFilePath = _sKeyFilePath + "." + XSaLTConstants.XS_EXTENSION_FOR_KEY_FILES;
		FileInputStream oFileInputStream = new FileInputStream(_sKeyFilePath);
		ObjectInputStream oObjectInputStream = new ObjectInputStream(oFileInputStream);
		SecretKey oTempSecretKey = (SecretKey) oObjectInputStream.readObject();
		setSecretKey(oTempSecretKey, "file");
		oObjectInputStream.close();
		oFileInputStream.close();
		setAndInitCipher(Cipher.getInstance(isDefaultEncryptionScheme), Cipher.DECRYPT_MODE, getSecretKey());
	}

	/**
	 * Encrypts a file
	 * 
	 * @param _sSourceFilePath The source file (normal)
	 * @param _sTargetFilePath The target file (encrypted)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void encryptFile(String _sSourceFilePath, String _sTargetFilePath) throws FileNotFoundException, IOException
	{

		CipherOutputStream oCipherOutstream = new CipherOutputStream(new FileOutputStream(_sTargetFilePath + "." + XSaLTConstants.XS_EXTENSION_FOR_ENCRYPTED_FILES), ioDesCipher);
		FileInputStream oFileToEncrypt = new FileInputStream(_sSourceFilePath);
		byte[] bFilebuffer = new byte[1024];
		int nLength = 0;
		while ((nLength = oFileToEncrypt.read(bFilebuffer)) != -1)
		{
			oCipherOutstream.write(bFilebuffer, 0, nLength);
		}
		oCipherOutstream.close();
		oFileToEncrypt.close();

	}



	/**
	 * Decrypts a file
	 * 
	 * @param _sSourceFilePath The source file (encrypted)
	 * @param _sTargetFilePath The target file (normal)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void decryptFile(String _sSourceFilePath, String _sTargetFilePath) throws FileNotFoundException, IOException
	{
		CipherOutputStream oCipherOutstream = null;
		_sSourceFilePath = _sSourceFilePath + "." + XSaLTConstants.XS_EXTENSION_FOR_ENCRYPTED_FILES;
		oCipherOutstream = new CipherOutputStream(new FileOutputStream(_sTargetFilePath), ioDesCipher);
		FileInputStream oFileToDecrypt = new FileInputStream(_sSourceFilePath);
		byte[] bFilebuffer = new byte[1024];
		int nLength = 0;
		while ((nLength = oFileToDecrypt.read(bFilebuffer)) != -1)
		{
			oCipherOutstream.write(bFilebuffer, 0, nLength);
		}
		oCipherOutstream.close();
		oFileToDecrypt.close();

	}

	/**
	 * Zips up directories and encrypts them
	 * 
	 * @param _sPathToCompress The path to compress
	 * @param _sDestinationFilepath The destination file path (encrypted)
	 * @throws IOException
	 * @throws InterruptedException
	 * @deprecated
	 */
	public void zipFilesAndEncrypt(String _sPathToCompress, String _sDestinationFilepath) throws IOException, InterruptedException
	{
		Runtime oRuntime = Runtime.getRuntime();
		String sZipFiles = "jar cvfM \"" + _sDestinationFilepath + "\" -C \"" + _sPathToCompress + "\" .";
		Process oProcess = oRuntime.exec(sZipFiles);

		XSaLTStreamGobbler oErrorGobbler = new XSaLTStreamGobbler(oProcess.getErrorStream());
		XSaLTStreamGobbler oInputGobbler = new XSaLTStreamGobbler(oProcess.getInputStream());
		oErrorGobbler.start();
		oInputGobbler.start();
		oProcess.waitFor();
		encryptFile(_sDestinationFilepath, _sDestinationFilepath);
		File oFile = new File(_sDestinationFilepath);
		if (oFile.exists())
		{
			oFile.delete();
		}
		oFile = null;
	}

	/**
	 * Zips up directories and encrypts them
	 * 
	 * @param _sFilePathToCompress The path to compress
	 * @param _sDestinationFilepath The destination file path (encrypted)
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void zipFileAndEncrypt(String _sFilePathToCompress, String _sDestinationFilepath) throws IOException, InterruptedException
	{

		Runtime oRuntime = Runtime.getRuntime();

		String sFilepath = _sFilePathToCompress.substring(0, _sFilePathToCompress.lastIndexOf('\\') + 1);
		String sFilename = _sFilePathToCompress.substring(_sFilePathToCompress.lastIndexOf('\\') + 1, _sFilePathToCompress.length());

		String commandArray[] = new String[] { "cmd.exe", "/c", "jar cvfM \"" + _sDestinationFilepath + "\" \"" + sFilepath + "" + sFilename + "\""};
		Process o_process = oRuntime.exec(commandArray);

		XSaLTStreamGobbler oErrorGobbler = new XSaLTStreamGobbler(o_process.getErrorStream());
		XSaLTStreamGobbler oInputGobbler = new XSaLTStreamGobbler(o_process.getInputStream());

		oErrorGobbler.start();
		oInputGobbler.start();
		o_process.waitFor();
		encryptFile(_sDestinationFilepath, _sDestinationFilepath);
		File oFile = new File(_sDestinationFilepath);
		if (oFile.exists())
		{
			oFile.delete();
		}
		oFile = null;
	}

	/**
	 * Zips up directories and encrypts them
	 * 
	 * @param _sFilePathToCompress The path to compress
	 * @param _sDestinationFilepath The destination file path (encrypted)
	 * @param _bUseNew Use a newer method (not actually used, but allows for overload).
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void zipFileAndEncrypt(String _sFilePathToCompress, String _sDestinationFilepath, boolean _bUseNew) throws IOException, InterruptedException
	{
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(_sDestinationFilepath);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[2048];

		String sFilePath = _sFilePathToCompress;
		String sShortPath = _sFilePathToCompress;

		FileInputStream fi = new FileInputStream(sFilePath);
		origin = new BufferedInputStream(fi, 2048);
		ZipEntry entry = new ZipEntry(sShortPath);
		out.putNextEntry(entry);
		int count;
		while ((count = origin.read(data, 0, 2048)) != -1)
		{
			out.write(data, 0, count);
		}
		origin.close();

		out.close();

		encryptFile(_sDestinationFilepath, _sDestinationFilepath);

	}

	/**
	 * Decrypts and unzips file
	 * 
	 * @param _sPathForFile The path to the file to be decrypted and unzipped (encrypted)
	 * @param _sPathForTargetFile The path to put the target file (zip/unencypted)
	 * @param _bDeleteFileWhenDone Are we deleting the encrypted file when done?
	 * @param _sFileExtension The file extension
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void decryptZipFiles(String _sPathForFile, String _sPathForTargetFile, boolean _bDeleteFileWhenDone, String _sFileExtension) throws FileNotFoundException, IOException
	{

		String sBackupFilePath = null;
		String sTestDecryptPath = null;
		if (_sFileExtension == null)
		{
			sBackupFilePath = _sPathForFile;
			sTestDecryptPath = _sPathForTargetFile;
		}
		else
		{
			sBackupFilePath = _sPathForFile + "." + _sFileExtension;
			sTestDecryptPath = _sPathForTargetFile + "." + _sFileExtension;
		}

		decryptFile(sBackupFilePath, sTestDecryptPath);

		if (_bDeleteFileWhenDone)
		{

			File oFile = new File(sBackupFilePath + "." + XSaLTConstants.XS_EXTENSION_FOR_ENCRYPTED_FILES);
			if (oFile.exists())
			{
				oFile.delete();
			}
		}

	}

	/**
	 * Set the secret key
	 * 
	 * @param _oDesSecretKey The secret key
	 * @param _sGeneratedFrom Where is the key generated from (not relevant other than debugging)
	 */
	public void setSecretKey(SecretKey _oDesSecretKey, String _sGeneratedFrom)
	{
		ioDesSecretKey = _oDesSecretKey;
	}

	/**
	 * Gets the instance secret key
	 * 
	 * @return javax.crypto.SecretKey - The secret key
	 */
	public SecretKey getSecretKey()
	{
		return ioDesSecretKey;
	}
}
