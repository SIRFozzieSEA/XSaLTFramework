package com.codef.xsalt.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileVisitor {

	private static final Logger LOGGER = LogManager.getLogger(XSaLTFileVisitor.class.getName());

	public static int fileCount = 0;

	public HashMap<String, String> argumentsMap = new HashMap<String, String>();

	public XSaLTFileVisitor(HashMap<String, String> argumentsMap) {
		if (argumentsMap != null) {
			this.argumentsMap = argumentsMap;
		}
	}

	public HashMap<String, String> getArgumentsMap() {
		return this.argumentsMap;
	}

	public void startVisit(String filePath) {
		visitFiles(Paths.get(filePath));
	}

	private void visitFiles(Path path) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (entry.toFile().isDirectory()) {
					visitFiles(entry);
				} else {
					visitFileCode(entry.toString());
					fileCount++;
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	public void visitFileCode(String filePath) {
		LOGGER.info("File " + filePath + " has been visited.");
	}

}
