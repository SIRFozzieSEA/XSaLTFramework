package com.codef.xsalt.arch;

import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class XSaLTSingleLineTextTabTableProcess implements XSaLTDataProcessInterface {

	public String[] headerLine = null;

	@Override
	public TreeMap<String, String> performOperation(String line) throws JsonMappingException, JsonProcessingException {

		TreeMap<String, String> finalParams = new TreeMap<>();
		if (headerLine == null) {
			headerLine = line.split("\t");
		} else {
			String[] workingLine = line.split("\t");
			for (int i = 0; i < headerLine.length; i++) {
				finalParams.put(headerLine[i], workingLine[i]);
			}
		}
		return finalParams;

	}
	
	public void resetHeader() {
		headerLine = null;
	}

}
