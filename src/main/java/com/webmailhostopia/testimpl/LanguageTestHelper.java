package com.webmailhostopia.testimpl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.common.utils.Verify;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class LanguageTestHelper extends AbstractPageObject{

	public boolean verifyOtherLabelOptions(String xpath, String[] expectedData)throws Exception{

		List<String> viewOptions = new ArrayList<String>();
		List<WebElement> mailBoxViewOptions = driver.findElements(By.xpath(xpath));

		for(WebElement label:mailBoxViewOptions){
			String temp = label.getText().trim();
			if( !temp.isEmpty()){
				viewOptions.add(label.getText().trim());
			}
		}

		String[] actuals = new String[viewOptions.size()];
		actuals = viewOptions.toArray(actuals);

		return Verify.compareStringInNonEnglish(actuals, expectedData, locale);

	}

	/*
	 * method to verify mail view options options in preferences tab
	 */
	public boolean verifyMailViewOptionsValues(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedMailViewOptions = null;
		Log.info("Verifying Mail View Options Label Names...");

		//xpath of mail view options dropdown
		String mailViewOptionsXpath = elementLocatorProp.getProperty("MAIL_BOX_VIEW_DROPDOWN_XPATH");
		if(language.equalsIgnoreCase("ENGLISH")){
			expectedMailViewOptions = engTestData.getProperty("PREF_TAB_MAIL_VIEW_BOX_OPTIONS").split(",");
		}else{
			expectedMailViewOptions = otherLangTestData.getProperty("PREF_TAB_MAIL_VIEW_BOX_OPTIONS").split(",");
		}

		driver.findElement(By.xpath(elementLocatorProp.getProperty("MAIL_BOX_VIEW"))).click();
		if( !verifyOtherLabelOptions(mailViewOptionsXpath, expectedMailViewOptions)){
			stepResult = false;
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("MAIL_BOX_VIEW"))).click();
		return stepResult;
	}

	/*
	 * method to verify default composition mode options options in preferences tab
	 */
	public boolean verifyDefaultCompositionModeOptions(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedDefaultCompOptions=null;

		Log.info("Verifying Default Composition Mode Options Label Names...");

		//xpath of mail view options dropdown
		String defaultCompOptionsXpath = elementLocatorProp.getProperty("DEFAULT_COMP_MODE_DROPDOWN_OPTIONS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedDefaultCompOptions = engTestData.getProperty("DEFAULT_COMP_OPTIONS").split(",");
		}else{
			expectedDefaultCompOptions = otherLangTestData.getProperty("DEFAULT_COMP_OPTIONS").split(",");
		}

		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFAULT_COMP_MODE_XPATH"))).click();
	
		if( !verifyOtherLabelOptions(defaultCompOptionsXpath, expectedDefaultCompOptions)){
			stepResult = false;
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFAULT_COMP_MODE_XPATH"))).click();
		return stepResult;
	}
	
	/*
	 * method to verify reply forward mode options in preferences tab
	 */
	
	public boolean verifyReplyForwardModeOptions(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedReplyForwardModeOptions=null;

		Log.info("Verifying Reply Forward Mode Options Label Names...");

		//xpath of default reply forward mode options dropdown
		String defaultReplyForwardModeOptionsXpath = elementLocatorProp.getProperty("REPLY_FWD_MODE_OPTIONS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedReplyForwardModeOptions = engTestData.getProperty("REPLY_FWD_MODE_OPTIONS").split(",");
		}else{
			expectedReplyForwardModeOptions = otherLangTestData.getProperty("REPLY_FWD_MODE_OPTIONS").split(",");
		}

		driver.findElement(By.xpath(elementLocatorProp.getProperty("REPLY_FWD_MODE_XPATH"))).click();
	
		if( !verifyOtherLabelOptions(defaultReplyForwardModeOptionsXpath, expectedReplyForwardModeOptions)){
			stepResult = false;
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("REPLY_FWD_MODE_XPATH"))).click();
		return stepResult;
	}
	
	/*
	 * method to verify default font type options options in preferences tab
	 */
	
	public boolean verifyDefaultFontTypeOptions(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedDefaultFontTypeOptions=null;

		Log.info("Verifying Default Font Type Options Label Names...");

		//xpath of mail view options dropdown
		String defaultFontTypeOptionsXpath = elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_OPTIONS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedDefaultFontTypeOptions = engTestData.getProperty("DEFAULT_FONT_TYPE_OPTIONS").split(",");
		}else{
			expectedDefaultFontTypeOptions = otherLangTestData.getProperty("DEFAULT_FONT_TYPE_OPTIONS").split(",");
		}

		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_XPATH"))).click();
	
		if( !verifyOtherLabelOptions(defaultFontTypeOptionsXpath, expectedDefaultFontTypeOptions)){
			stepResult = false;
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_XPATH"))).click();
		return stepResult;
	}
	
	/*
	 * method to verify header labels in preferences tab
	 */
	public boolean verifyHeader4Labels(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedH4HeaderLabels=null;

		Log.info("Verifying Header4 Label Names...");

		//xpath of mail view options dropdown
		String h4HeaderLabelsXpath = elementLocatorProp.getProperty("H4_HEADER_LABELS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedH4HeaderLabels = engTestData.getProperty("H4_HEADER_LABELS").split(",");
		}else{
			expectedH4HeaderLabels = otherLangTestData.getProperty("H4_HEADER_LABELS").split(",");
		}

		if( !verifyOtherLabelOptions(h4HeaderLabelsXpath, expectedH4HeaderLabels)){
			stepResult = false;
		}
		return stepResult;
	}
	
	/*
	 * method to verify header labels in preferences tab
	 */
	public boolean verifyHeader3Labels(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedH3HeaderLabels=null;

		Log.info("Verifying Header3 Label Names...");

		//xpath of mail view options dropdown
		String h3HeaderLabelsXpath = elementLocatorProp.getProperty("H3_HEADER_LABELS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedH3HeaderLabels = engTestData.getProperty("H3_HEADER_LABELS").split(",");
		}else{
			expectedH3HeaderLabels = otherLangTestData.getProperty("H3_HEADER_LABELS").split(",");
		}

		if( !verifyOtherLabelOptions(h3HeaderLabelsXpath, expectedH3HeaderLabels)){
			stepResult = false;
		}
		return stepResult;
	}
}