package com.codef.xsalt.arch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTStreamGobbler extends Thread {

	// private static final Logger LOGGER = LogManager.getLogger(XSaLTStreamGobbler.class.getName());

	/**
	 * The instance input stream for the java process
	 */
	private InputStream ioInputStream;

	private ArrayList<String> ioOutputLines = new ArrayList<String>();

	/**
	 * 
	 * Primary constructor
	 * 
	 * @param _oInputStream The input stream for the java process
	 */
	public XSaLTStreamGobbler(InputStream _oInputStream) {
		ioInputStream = _oInputStream;
	}

	/**
	 * This method returns the output lines ArrayList.
	 * 
	 * @return The output lines ArrayList
	 */
	public ArrayList<String> getOutputLines() {
		return ioOutputLines;
	}

	/**
	 * The run thread
	 */
	public void run() {
		try {
			String sOutputLine = null;
			InputStreamReader oInputStreamReader = new InputStreamReader(ioInputStream);
			BufferedReader oBufferedReader = new BufferedReader(oInputStreamReader);

			while ((sOutputLine = oBufferedReader.readLine()) != null) {
				ioOutputLines.add(sOutputLine);
			}
		} catch (IOException e) {
			// LOGGER.error(e.toString(), e);
		}
	}

}
