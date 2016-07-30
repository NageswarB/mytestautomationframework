package com.webmailhostopia.common.utils;



import org.apache.commons.lang3.RandomStringUtils;




public class StringUtils {
	
	public final static String SpecialChars = ".~;,!\\@#\\$%^&*()_+}{|\":?><`*/-+=[]";
	public enum RandomStringType {LETTERS_ONLY, NUMBERS_ONLY, LETTERS_AND_NUMBERS, SPECIAL_CHARS_ONLY, ALL }
	public final static String EMPTY_STRING = "";
  
	/**
	 * Returns true if string equals null or empty (length==0)
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
	
	
	/**
	 * Returns true if string equals null or empty (length==0) eating spaces.
	 * @param string
	 * @return
	 */
	public static boolean isEmptyAfterTrimming(String string) {
	    return string == null || string.trim().length() == 0;
	}
    
    /**
     * returns a random string of special characters
     * @param count the length of random string to create 
     * @return
     */
    public static String getRandomStringWithSpecialChars(int count) {
    	return RandomStringUtils.random(count, 0, SpecialChars.toCharArray().length-1, false, false, SpecialChars.toCharArray());
    }
    
    
    /**
     * Creates a random string based on a variety of options, using default source of randomness
     * @param count the length of random string to create 
     * @param start the position in set of chars to start at
     * @param end the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers
     * @param chars the set of chars to choose randoms from. If null, then it will use the set of all chars.
     * @return
     */
    public static String getRandomString(int count, int start, int end, boolean letters, boolean numbers, char[] chars ) {
    	return RandomStringUtils.random(count, start, end, letters, numbers, chars);
    }
    
    
 

}
