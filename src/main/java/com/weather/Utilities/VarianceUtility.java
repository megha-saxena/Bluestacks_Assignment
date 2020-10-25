package com.weather.Utilities;
import java.lang.Integer;

public class VarianceUtility {
	
	public static Boolean getVariance(float tempAPI, float tempweb, Integer variance ) {
		
		Float difference =Math.abs(tempAPI-tempweb);
		Float percentage = (difference/tempAPI)*100;
		System.out.println("Actual Temperature Variance : "+percentage);
		Float var= variance.floatValue();
		if(percentage >= var) {
			return false;
		}
		else
			return true;
	}

}
