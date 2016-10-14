package com.webmailhostopia.common.utils;

import java.text.Collator;
import java.util.Locale;

public class Verify {

	public static boolean compareStringsInEnglish(String[] actual, String[] expected, boolean ignorecase){
		boolean isTrue=true;
		if(actual.length!=expected.length)
		{
			Log.info("Actual and Expected arrays Lengths are not equal: Actual: "+ actual.length+ "  Expected: "+expected.length);
			return false;
		}
		else
		{
			for(int i=0;i<actual.length;i++)
			{
				if(ignorecase){
					if(actual[i].equalsIgnoreCase(expected[i])){
						Log.info("Actual and Expected string are equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
						continue;
					}
					else{
						isTrue=false;
						Log.info("Actual and Expected string are not equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
					}
				}
				else
				{
					if(actual[i].equals(expected[i])){
						Log.info("Actual and Expected string are equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
						continue;
					}
					else{
						isTrue=false;
						Log.info("Actual and Expected string are not equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
					}
				}
			}
		}
		return isTrue;
	}

	public static boolean compareStringInNonEnglish(String[] actual, String[] expected,Locale locale)throws Exception{

		boolean isTrue=true;
		Collator collator = Collator.getInstance(locale);

		if(actual.length!=expected.length){
			Log.info("Actual and Expected arrays Lengths are not equal: Actual: "+ actual.length+ "  Expected: "+expected.length);
			return false;
		}else{
			for(int i=0;i<actual.length;i++)
			{
				if(collator.compare(actual[i].trim(), expected[i].trim()) == 0){
					Log.info("Actual and Expected string are equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
					continue;
				}else{
					isTrue=false;
					Log.error("Actual and Expected string are not equal. Actual: "+ actual[i]+ "  Expected: "+expected[i]);
				}
			}
			return isTrue;
		}
	}
}