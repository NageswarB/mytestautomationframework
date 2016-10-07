package com.webmailhostopia.testimpl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.LanguageType;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;
import com.webmailhostopia.selenium.webdriver.WebDriverType;

public class LanguageTest extends AbstractPageObject{
	
	
	public static boolean languageTest() throws Exception{
		int failCounter = 0;
		int i=0;
		
		String[] sideNavBar = otherLangTestData.getProperty("").split(",");
		
		openPreferencesTab();
		changeTheLanguage();
		//side navigation bar text validation
		List<WebElement> ls = driver.findElements(By.xpath("//*[contains(@class,'nav navbar-nav side-nav1')]/li"));
		
		
		for(WebElement we:ls){
			if(!we.getText().equalsIgnoreCase(sideNavBar[i])){
				failCounter++;
			}
		}
		return (failCounter==0)?true:false;
	}
	
	public static void openPreferencesTab()throws Exception{
		WebElement preferences = waitForElementToBeClickable(ByLocator.xpath, "//a[contains(@href,'preferences')]", 60);
		if( preferences != null){
			preferences.click();
		}
		
	}
	
	public static void changeTheLanguage()throws Exception{
		Select langDropDown = new Select(driver.findElement(By.id("language")));
		langDropDown.selectByValue(language);
	}
	
	

}
