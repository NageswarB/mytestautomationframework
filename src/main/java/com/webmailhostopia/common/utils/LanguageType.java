package com.webmailhostopia.common.utils;

public enum LanguageType {
	
	
		FRENCH("French"), 
		GERMAN("German"),
		SPANISH("Spanish"),
		SPANISHFORAMERICA("SpanishForAmerica"),
		ITALIAN("Italian"),
		PORTUGUES("Portugues"),
		GREEK("Greek"),
		ROMAN("Roman"),
		HUNGARIAN("Hungarian"),
		SWEDISH("Swedish");
		
		private String value;

		private LanguageType(String value) {
			this.value = value;
		}
		public String getValue() {
	        return value;
	    }

}
