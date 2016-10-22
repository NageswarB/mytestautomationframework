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
		String[] expectedDefaultCompOptions=null;

		Log.info("Verifying Reply Forward Mode Options Label Names...");

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
	 * method to verify default font type options options in preferences tab
	 */
	
	public boolean verifyDefaultFontTypeOptions(String language)throws Exception{

		boolean stepResult = true;
		String[] expectedDefaultCompOptions=null;

		Log.info("Verifying Default Font Type Options Label Names...");

		//xpath of mail view options dropdown
		String defaultCompOptionsXpath = elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_OPTIONS_XPATH");

		if(language.equalsIgnoreCase("ENGLISH")){
			expectedDefaultCompOptions = engTestData.getProperty("DEFAULT_FONT_TYPE_OPTIONS").split(",");
		}else{
			expectedDefaultCompOptions = otherLangTestData.getProperty("DEFAULT_FONT_TYPE_OPTIONS").split(",");
		}

		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_XPATH"))).click();
	
		if( !verifyOtherLabelOptions(defaultCompOptionsXpath, expectedDefaultCompOptions)){
			stepResult = false;
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("DEFUALT_FONT_TYPE_XPATH"))).click();
		return stepResult;
	}
}
