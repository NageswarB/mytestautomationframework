package com.webmailhostopia.common.utils;

public enum LanguageType {
	
	
		FRENCH("French"), 
		SPANISH("Spanish");
		
		private String value;

		private LanguageType(String value) {
			this.value = value;
		}
		public String getValue() {
	        return value;
	    }

}
