package com.codef.xsalt.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.codef.xsalt.arch.XSaLTLoggerWrapper;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFTPClient {

	private FTPClient ioFTPClient = new FTPClient();

	private boolean ibFTPError = false;

	private boolean ibPassiveMode = true;

	private String isFTPHostName = "";

	private String isFTPPassword = "";

	private String isFTPUserName = "";

	/**
	 * Constructor that provides access to this class.
	 * 
	 * @param _sFTPHostName Host to connect to
	 * @param _sFTPUserName User name
	 * @param _sFTPPassword Password for user
	 */
	public XSaLTFTPClient(String _sFTPHostName, String _sFTPUserName, String _sFTPPassword) {
		isFTPHostName = _sFTPHostName;
		isFTPUserName = _sFTPUserName;
		isFTPPassword = _sFTPPassword;
		ioFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**
	 * This method creates the FTP connection.
	 * 
	 * @throws SocketException
	 * @throws IOException
	 */
	public void connectFTP() throws SocketException, IOException {
		ioFTPClient.connect(isFTPHostName);
		int reply = ioFTPClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ioFTPClient.disconnect();
			ibFTPError = true;
		}
	}

	/**
	 * This method logs the user specified in the constructor into the remote FTP
	 * server.
	 * 
	 * @throws IOException
	 */
	public void login() throws IOException {
		if (!ioFTPClient.login(isFTPUserName, isFTPPassword)) {
			ioFTPClient.logout();
			ibFTPError = true;
		}
	}

	/**
	 * @return Flag if an FTP error occurred
	 */
	public boolean isFTPError() {
		return ibFTPError;
	}

	/**
	 * This method logs the user out and disconnects the FTP connection.
	 */
	public void disconnectFTP() {
		try {
			ioFTPClient.logout();

			if (ioFTPClient.isConnected()) {
				try {
					ioFTPClient.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}
		} catch (IOException e) {
			XSaLTLoggerWrapper.error(XSaLTFTPClient.class.getName(), e.toString(), e);
		}
	}

	/**
	 * This method sets the transfer mode for the connection (passive or active).
	 * 
	 * @param _bPassiveTransfer Flag if transfer should be passive
	 */
	public void setPassiveTranferMode(boolean _bPassiveTransfer) {
		if (_bPassiveTransfer) {
			ioFTPClient.enterLocalPassiveMode();
		} else {
			ioFTPClient.enterLocalActiveMode();
		}
	}

	/**
	 * This method sets the transfer mode for the connection (binary or ASCII).
	 * 
	 * @param _bBinaryTransfer Flag if transfer should be binary
	 * @throws IOException
	 */
	public void setBinaryTransfer(boolean _bBinaryTransfer) throws IOException {
		if (_bBinaryTransfer) {
			ioFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
		} else {
			ioFTPClient.setFileType(FTP.ASCII_FILE_TYPE);
		}
	}

	/**
	 * This method retrieves a file from the remote host ("get" command).
	 * 
	 * @param _sLocalFileName  Local path for file to copy to
	 * @param _sRemoteFileName Source file on remote host
	 * @throws IOException
	 */
	public void getFile(String _sLocalFileName, String _sRemoteFileName) throws IOException {
		OutputStream output = new FileOutputStream(_sLocalFileName);
		ioFTPClient.retrieveFile(_sRemoteFileName, output);
		output.close();
	}

	/**
	 * This method saves a file to the remote host ("put" command).
	 * 
	 * @param _sLocalFileName  Local source file location
	 * @param _sRemoteFileName Remote destination file location
	 * @throws IOException
	 */
	public void storeFile(String _sLocalFileName, String _sRemoteFileName) throws IOException {
		InputStream oInputStream = new FileInputStream(_sLocalFileName);
		ioFTPClient.storeFile(_sRemoteFileName, oInputStream);
		oInputStream.close();
	}

	public class PrintCommandListener implements ProtocolCommandListener {
		private PrintWriter __writer;

		/**
		 * Main constructor for access to this class.
		 * 
		 * @param writer PrintWriter object used for output
		 */
		public PrintCommandListener(PrintWriter writer) {
			__writer = writer;
		}

		/**
		 * This method prints the event message for commands sent to the PrintWriter.
		 * 
		 * @param event Event to get information for
		 */
		public void protocolCommandSent(ProtocolCommandEvent event) {
			__writer.print(event.getMessage());
			__writer.flush();
		}

		/**
		 * This method prints the event message for replies received to the PrintWriter.
		 * 
		 * @param event
		 */
		public void protocolReplyReceived(ProtocolCommandEvent event) {
			__writer.print(event.getMessage());
			__writer.flush();
		}
	}

	/**
	 * @return Flag if transfer mode is passive
	 */
	public boolean isPassiveMode() {
		return ibPassiveMode;
	}

	/**
	 * @return Remote host name
	 */
	public String getFTPHostName() {
		return isFTPHostName;
	}

	/**
	 * @return Password for remote host
	 */
	public String getFTPPassword() {
		return isFTPPassword;
	}

	/**
	 * @return User name for remote host
	 */
	public String getFTPUserName() {
		return isFTPUserName;
	}

}
