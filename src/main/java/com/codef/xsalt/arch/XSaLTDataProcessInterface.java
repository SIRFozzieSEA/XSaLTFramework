package com.codef.xsalt.arch;

import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@FunctionalInterface
public interface XSaLTDataProcessInterface {
	TreeMap<String, String> performOperation(String line) throws JsonMappingException, JsonProcessingException;
}
