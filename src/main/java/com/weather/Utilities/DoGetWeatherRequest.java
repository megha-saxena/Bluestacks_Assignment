package com.weather.Utilities;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import net.bytebuddy.NamingStrategy.SuffixingRandom.BaseNameResolver.ForGivenType;

public class DoGetWeatherRequest {
	
	ReadingPropertiesFile read = new ReadingPropertiesFile();
	String  appId = read.getProperty("AppKey");
	
	public static Response doGetRequest(String endpoint) {
		
		RestAssured.baseURI = "https://api.openweathermap.org/";
		
		RestAssured.defaultParser = Parser.JSON;
		
		Response resp = RestAssured.given().headers("Content-Type", ContentType.JSON,"Accept", ContentType.JSON).
				when().get(endpoint).
				then().contentType(ContentType.JSON).extract().response();
		if(resp.statusCode() != 200) {
		
			throw new RuntimeException("HttpResponseCode"+resp.statusCode());
		}
		else {
			
			
			return resp;
		}
		
	}
	
	public static float getTemperature(String city , String appId) {
		
		
		String endpoint = "data/2.5/weather?q="+city+"&appid="+appId+"&units=metric";
		Response resp = doGetRequest(endpoint);
		String temps = resp.jsonPath().getString("main.temp");
		return Float.parseFloat(temps);		
	
	}

}
