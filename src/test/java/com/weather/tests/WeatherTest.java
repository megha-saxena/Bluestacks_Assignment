package com.weather.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;
import com.weather.Utilities.DoGetWeatherRequest;
import com.weather.Utilities.ReadJSON;
import com.weather.Utilities.ReadingPropertiesFile;
import com.weather.Utilities.VarianceUtility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class WeatherTest {
	//initializing browser
	WebDriver driver = null;
	ReadingPropertiesFile read = new ReadingPropertiesFile();
	
	
	
	
	
	@BeforeMethod
	//Launching Application
	public void InitializeBrowser() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\Resources\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(read.getProperty("URL"));
			
		
	}
	
	
	@Test(dataProvider = "getCity")
	public void getWeather(String city) throws NumberFormatException, IOException {
		//Calling the API call method for temp of particular city
		String  appId = read.getProperty("AppKey");
		float tempAPI = DoGetWeatherRequest.getTemperature(city, appId);
		System.out.println("Temperature from API for "+city+": "+ tempAPI);
		
		//Explicit wait		
		WebDriverWait wait = new WebDriverWait(driver, 20);		
   
		//searching for the particular city in textbox
		WebElement search_input = wait.until(ExpectedConditions.elementToBeClickable(By.id("LocationSearch_input")));
		search_input.click();		
		search_input.clear();
		search_input.sendKeys(city);
		
		//waiting for the city list to load
		WebElement list_item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='LocationSearch_listbox-0']")));
		
		//clicking the searched city from the list and waiting for the temperature to load
		list_item.click();			
		ExpectedCondition<Boolean> elementTextContainsString = arg0 -> driver.findElement(By.tagName("h1")).getText().contains(city);
		wait.until(elementTextContainsString);		
		
		//getting the temperature value for searched city
		String temp = driver.findElement(By.cssSelector(".CurrentConditions--tempValue--3KcTQ")).getText();
		//chopping the degree from end to parse it to Float
		temp = StringUtils.chop(temp);		
		Float tempWeb= Float.parseFloat(temp);
		System.out.println("Temperature from web for "+city+": "+ tempWeb);
		
		//Reading the variance value from DATA.json
		int variance = ReadJSON.readJsonFileForVariance();
		System.out.println("Expected Temperature Variance: "+ variance);
		
		//Calling Variance calculation utility	
		boolean result = VarianceUtility.getVariance(tempAPI, tempWeb, variance);
		
		
		//Asserting the result 
		Assert.assertEquals(result, true);		
		
	}
	
	
	

	@AfterMethod	
	public void TearDown() {
		//quitting the driver
		driver.quit();
	}

	@DataProvider
	public Object[][] getCity() throws IOException {
		// Rows - Number of times your test has to be repeated.
		// Columns - Number of parameters in test data.
		Object[][] data = new Object[7][1];
		// 1st row
				data[0][0] = ReadJSON.readJsonFileForCity().get(0).toString();
				data[1][0] = ReadJSON.readJsonFileForCity().get(1).toString();
				data[2][0] = ReadJSON.readJsonFileForCity().get(2).toString();
				data[3][0] = ReadJSON.readJsonFileForCity().get(3).toString();
				data[4][0] = ReadJSON.readJsonFileForCity().get(4).toString();
				data[5][0] = ReadJSON.readJsonFileForCity().get(5).toString();
				data[6][0] = ReadJSON.readJsonFileForCity().get(6).toString();
				
				return data;		 
	}
}
