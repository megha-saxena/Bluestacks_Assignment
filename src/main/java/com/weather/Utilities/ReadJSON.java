package com.weather.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReadJSON {
	
	public static List<Object> readJsonFileForCity() throws IOException {
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\Data.json");
		if(file.exists()) {
			 String content = FileUtils.readFileToString(file, "utf-8");

			    // Convert JSON string to JSONObject
			    JSONObject testData = new JSONObject(content);  
			    
			    JSONArray Cities = testData.getJSONArray("City");

			    for (int i = 0; i < Cities.length(); i++) {
			        String city = (String) Cities.get(i);
			        System.out.println(city);
			    }

			    // Or convert the JSONArray to Java List
			    List<Object> City_List = Cities.toList();			   
			    return City_List;
		}
		else {
			throw new FileNotFoundException("Test Data File not Found");
		}
	   
	    
	}
	
	public static int readJsonFileForVariance() throws IOException {
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\Data.json");
		if(file.exists()) {
			  String content = FileUtils.readFileToString(file, "utf-8");
			    
			    JSONObject testData = new JSONObject(content);  
			    int variance = testData.getInt("Variance");
				return variance;
		}
		else {
			throw new FileNotFoundException("Test Data File not found");
		}
	}

}
