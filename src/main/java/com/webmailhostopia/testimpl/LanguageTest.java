package com.webmailhostopia.testimpl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.common.utils.Verify;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class LanguageTest extends AbstractPageObject{
	
	public static LanguageTestHelper helper = new LanguageTestHelper();

	/**
	 * This method is used to verify preferences tab labels in English language
	 * 
	 */

	public static boolean labelsLanguageTestInPreferenceTabForEnglish() throws Exception{

		int failCounter=0;

		List<String> lsStr = new ArrayList<String>();

		String[] expectedSideNavBarLabels = engTestData.getProperty("SIDE_NAV_BAR_MENU_ITEMS").split(",");

		openPreferencesTab();
		changeTheLanguageToEnglish();
		//side navigation bar text validation
		List<WebElement> ls = driver.findElements(By.xpath(elementLocatorProp.getProperty("SIDE_NAV_BAR_ELE_XPATH")));
		
		for(WebElement we:ls){
			String temp = we.getText().trim();
			if( !temp.isEmpty()){
				lsStr.add(temp);
			}
		}
		String[] actualSideNavBarLabels = new String[lsStr.size()];
		actualSideNavBarLabels = lsStr.toArray(actualSideNavBarLabels);

		if( !Verify.compareStringsInEnglish(actualSideNavBarLabels, expectedSideNavBarLabels, true)){
			failCounter++;
		}
		String expected[] = engTestData.getProperty("LABELS_IN_PREF_TAB_PAGE").split(",");
		if( !labelsTestInPreferenceMainActivityPage(expected)){
			failCounter++;
		}

		if( !helper.verifyMailViewOptionsValues("english")){
			failCounter++;
		}
		
		if( !helper.verifyDefaultCompositionModeOptions("english")){
			failCounter++;
		}
		
		if( !helper.verifyReplyForwardModeOptions("english")){
			failCounter++;
		}
		
		if( !helper.verifyDefaultFontTypeOptions("english")){
			failCounter++;
		}
		
		if( !helper.verifyHeader4Labels("english")){
			failCounter++;
		}
		
		
		return (failCounter==0)?true:false;

	}

	public static boolean labelsLanguageTestInPreferenceTabForNonEnglish() throws Exception{

		int failCounter=0;
		List<String> lsStr = new ArrayList<String>();

		String[] expectedSideNavBarLabels = otherLangTestData.getProperty("SIDE_NAV_BAR_MENU_ITEMS").split(",");

		openPreferencesTab();
		changeTheLanguage();
		//side navigation bar text validation
		List<WebElement> ls = driver.findElements(By.xpath(elementLocatorProp.getProperty("SIDE_NAV_BAR_ELE_XPATH")));

		Log.info("Side navigation bar labels:");
		for(WebElement we:ls){
			String temp = we.getText().trim();
			if( !temp.isEmpty()){
				lsStr.add(temp);
			}
		}
		String[] actualSideNavBarLabels = new String[lsStr.size()];
		actualSideNavBarLabels = lsStr.toArray(actualSideNavBarLabels);

		if( !Verify.compareStringInNonEnglish(actualSideNavBarLabels, expectedSideNavBarLabels, locale)){
			failCounter++;
		}
		String expected[] = otherLangTestData.getProperty("LABELS_IN_PREF_TAB_PAGE").split(",");
		if( !labelsTestInPreferenceMainActivityPage(expected)){
			failCounter++;
		}
		
		if( !helper.verifyMailViewOptionsValues("nonenglish")){
			failCounter++;
		}
		
		if( !helper.verifyDefaultCompositionModeOptions("nonenglish")){
			failCounter++;
		}
		
		if( !helper.verifyReplyForwardModeOptions("nonenglish")){
			failCounter++;
		}
		
		if( !helper.verifyDefaultFontTypeOptions("nonenglish")){
			failCounter++;
		}
		
		if( !helper.verifyHeader4Labels("nonenglish")){
			failCounter++;
		}

		return (failCounter==0)?true:false;
	}


	public static void openPreferencesTab()throws Exception{
		Log.info("Opening preferences tab...");
		WebElement preferences = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("PREFERENCE_BUTTON_XPATH"), 60);
		if( preferences != null){
			preferences.click();
			waitForElementToBeClickable(ByLocator.css, elementLocatorProp.getProperty("PREF_HEADER_BAR"), 30);
			Log.info("Successfully opened preferences tab!!");
		}

	}

	public static void changeTheLanguage()throws Exception{
		Log.info("Selecting language to test");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//label[@translate='LANG_PREFERENCES_GENERAL_FORM_LANGUAGE']/../div")).click();

		WebElement lan = waitForElementToBeClickable(ByLocator.xpath, "//*[@translate='LANG_PREFERENCES_GENERAL_FORM_LANGUAGE']/../div/a/div[2]/div[2]/div["+languageValue+"]", 30);
		if( lan != null){
			Log.info("Language selected successfully!!!");
			lan.click();
		}else{
			Log.error("drop down not visible");
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("SAVE_BTN_XPATH"))).click();
		Thread.sleep(10000);
		refreshWebPage();
		Thread.sleep(10000);
	}


	public static void changeTheLanguageToEnglish()throws Exception{
		Log.info("Selecting language to test");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//label[@translate='LANG_PREFERENCES_GENERAL_FORM_LANGUAGE']/../div")).click();

		WebElement lan = waitForElementToBeClickable(ByLocator.xpath, "//*[@translate='LANG_PREFERENCES_GENERAL_FORM_LANGUAGE']/../div/a/div[2]/div[2]/div[1]", 30);
		if( lan != null){
			Log.info("English language selected successfully!!!");
			lan.click();
		}else{
			Log.error("Language drop down not visible");
		}
		driver.findElement(By.xpath(elementLocatorProp.getProperty("SAVE_BTN_XPATH"))).click();
		Thread.sleep(10000);
		refreshWebPage();
		Thread.sleep(10000);
	}

	public static boolean labelsTestInPreferenceMainActivityPage(String[] expected)throws Exception{

		Log.info("Verfiying labels in preferences tab...");

		List<String> lsLabelText = new ArrayList<String>();

		List<WebElement> lsLabels = driver.findElements(By.tagName("label"));
		for(WebElement label:lsLabels){
			String temp = label.getText().trim();
			if( !temp.isEmpty()){
				lsLabelText.add(label.getText().trim());
			}
		}
		String[] actuals = new String[lsLabelText.size()];
		actuals = lsLabelText.toArray(actuals);

		boolean stepResult = Verify.compareStringInNonEnglish(actuals, expected,locale);
		return stepResult;
	}
	
}