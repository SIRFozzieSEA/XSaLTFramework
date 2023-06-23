package com.codef.xsalt.arch;

import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XSaLTSingleLineJsonProcess implements XSaLTDataProcessInterface {

	@Override
	public TreeMap<String, String> performOperation(String line) throws JsonMappingException, JsonProcessingException {

		TreeMap<String, String> finalParams = new TreeMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonLog = objectMapper.readTree(line);

		String author = jsonLog.get("author").textValue();
		String country = jsonLog.get("country").textValue();
		String imageLink = jsonLog.get("imageLink").textValue();
		String language = jsonLog.get("language").textValue();
		String link = jsonLog.get("link").textValue();
		String pages = jsonLog.get("pages").textValue();
		String title = jsonLog.get("title").textValue();
		String bookyear = jsonLog.get("bookyear").textValue();

		finalParams.put("author", author);
		finalParams.put("country", country);
		finalParams.put("imageLink", imageLink);
		finalParams.put("language", language);
		finalParams.put("link", link);
		finalParams.put("pages", pages);
		finalParams.put("title", title);
		finalParams.put("bookyear", bookyear);

		return finalParams;

	}

}
