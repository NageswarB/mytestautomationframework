package com.webmailhostopia.testimpl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

/**
 * This is the actual test implementation file ,contains UI driving code and validations
 * @author nageswar.bodduri
 * 
 *
 */
public class MailLoginPage extends AbstractPageObject{

	//this will call the AbstractPageObject C'tor which will initialize driver object
	public MailLoginPage() {
		super();
	}

	public boolean loginToWebMail(String userName,String password) throws Exception{

		boolean isLoggedIn = false;

		if (!driver.getCurrentUrl().contains(SUTprop.getProperty("URL")) ) {
			driver.get(SUTprop.getProperty("URL"));
			isBrowserOpen = true;
		}

		if(isBrowserOpen && driver.getCurrentUrl().contains(SUTprop.getProperty("URL"))){
			try{
				Thread.sleep(5000);
				WebElement loginBtn = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("LOGIN_BTN"), 60);
				if( loginBtn != null){
					typeEditBox(elementLocatorProp.getProperty("USERNAME_ID"), userName);
					typeEditBox(elementLocatorProp.getProperty("PASSWORD_ID"), password);
					loginBtn.click();
					Thread.sleep(5000);
					if ( waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("USER_NAME_AREA"), 60) != null){
						isLoggedIn = true;
					}
				}
			}catch(Exception e){
				Log.error("Exception occured during login" + e.getMessage());
				e.printStackTrace();
			}
		}
		return isLoggedIn;
	}


	//close all open browser instances
	public void quit(){
		try{
			driver.quit();
			Log.info("closing all browser instances");
		}catch(Exception e){
			Log.error("exception occured while closing the browser");
		}
	}
} //End of the main class
