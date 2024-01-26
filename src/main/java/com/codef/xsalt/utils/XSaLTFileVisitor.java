package com.codef.xsalt.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.codef.xsalt.arch.XSaLTLoggerWrapper;

/**
 * @author Stephan P. Cossette
 * @author Copyright 2011 Codef.com
 */
public class XSaLTFileVisitor {

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

	public void startVisitFolders(String filePath) {
		visitFolders(Paths.get(filePath));
	}

	private void visitFolders(Path path) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path entry : stream) {
				if (entry.toFile().isDirectory()) {
					visitFolderCode(entry.toString());
					visitFolders(entry);
					fileCount++;
				}
			}
		} catch (IOException e) {
			XSaLTLoggerWrapper.error(XSaLTFileVisitor.class.getName(), e.toString(), e);
		}
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
			XSaLTLoggerWrapper.error(XSaLTFileVisitor.class.getName(), e.toString(), e);
		}
	}

	public void visitFileCode(String filePath) {
		XSaLTLoggerWrapper.info(XSaLTFileVisitor.class.getName(), "File " + filePath + " has been visited.");
	}

	public void visitFolderCode(String filePath) {
		XSaLTLoggerWrapper.info(XSaLTFileVisitor.class.getName(), "Folder " + filePath + " has been visited.");
	}

}
