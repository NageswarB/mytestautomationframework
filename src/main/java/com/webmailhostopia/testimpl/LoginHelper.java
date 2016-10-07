package com.webmailhostopia.testimpl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class LoginHelper extends AbstractPageObject{
	
	public boolean clickCancelInForgotPwdScreen() throws Exception{
		boolean stepResult = false;
		clickCancel();
		WebElement forgotLink = waitUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("FORGOT_PWD_LINK")));
		if( forgotLink != null){
			forgotLink.click();
			stepResult = true;
		}
		return stepResult;
	}

}
