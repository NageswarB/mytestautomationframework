package com.webmailhostopia.testimpl;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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

	LoginHelper helper = new LoginHelper();
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
					driver.findElement(By.id(elementLocatorProp.getProperty("PASSWORD_ID"))).sendKeys(Keys.TAB);
					
					if( SUTprop.getProperty("BROWSER").equalsIgnoreCase("iexplorer")){
						loginBtn.sendKeys(Keys.ENTER);
					}else{
						loginBtn.click();
					}
										
					if( pollDOMUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("NEW_EMAIL_XPATH")), 5) != null){
						Log.info("New email pencil button is present");
						isLoggedIn = true;
					}else{
						Log.error("New email pencil button not present");
					}
				}
			}catch(Exception e){
				Log.error("Exception occured during login" + e.getMessage());
				e.printStackTrace();
			}
		}
		return isLoggedIn;
	}

	public String noUserNameWithValidPassword(String sUserName, String sPassword) throws Exception{
		Log.info("Inside no username with valid password method");
		return testLogin(sUserName, sPassword);
	}

	public String validUserNameWithNoPassword(String sUserName, String sPassword) throws Exception{
		Log.info("Inside valid username with no password method");
		return testLogin(sUserName, sPassword);
	}

	public String noUserNameNoPassword(String sUserName, String sPassword) throws Exception{
		Log.info("Inside no username no password method");
		return testLogin(sUserName, sPassword);
	}

	public String testLogin(String sUserName,String sPassword)throws Exception{
		Log.info("Inside no username with valid password method");
		String actualWarnMessage = null;

		boolean isLoggedIn = false;

		if (!driver.getCurrentUrl().contains(SUTprop.getProperty("URL")) ) {
			driver.get(SUTprop.getProperty("URL"));
			isBrowserOpen = true;
		}

		if(isBrowserOpen && driver.getCurrentUrl().contains(SUTprop.getProperty("URL"))){

			WebElement loginBtn = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("LOGIN_BTN"), 60);
			if( loginBtn != null){
				typeEditBox(elementLocatorProp.getProperty("USERNAME_ID"), sUserName);
				typeEditBox(elementLocatorProp.getProperty("PASSWORD_ID"), sPassword);
				driver.findElement(By.id(elementLocatorProp.getProperty("PASSWORD_ID"))).sendKeys(Keys.TAB);
				loginBtn.click();

				WebElement alertDangerArea = waitUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("LOGIN_ALERT_AREA")));
				if( alertDangerArea != null){
					actualWarnMessage = alertDangerArea.getText();
					Log.info("Warning message received : " + actualWarnMessage);
					clickClose(driver.findElement(By.xpath(elementLocatorProp.getProperty("CLOSE_BUTTON_XPATH"))));
					refreshWebPage();
				}else{
					Log.error("No warn messsage has been found with no username and no password entered");
				}
			}
		}
		return actualWarnMessage;
	}

	public boolean verifyForgotPasswordLink() throws Exception{
		int failCounter = 0;
		String expectedAlertMsg = "Please provide the characters from the picture.";
		
		if (!driver.getCurrentUrl().contains(SUTprop.getProperty("URL")) ) {
			driver.get(SUTprop.getProperty("URL"));
			isBrowserOpen = true;
		}
		
		WebElement forgotPwdLink = waitUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("FORGOT_PWD_LINK")));
		
		if( forgotPwdLink != null){
			forgotPwdLink.click();
			WebElement alertDanger = waitUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("FORGOT_PASSWORD_AREA")));
			if( !alertDanger.getText().equalsIgnoreCase(expectedAlertMsg)){
				failCounter++;
			}
			
			typeEditBox(elementLocatorProp.getProperty("USERNAME_ID"), "testuser");
			typeEditBox(elementLocatorProp.getProperty("CAPTCHA_TEXT_ID"), "captchatext");
			
			if( !helper.clickCancelInForgotPwdScreen()){
				failCounter++;
			}
			
		}
		
		
		
		return (failCounter == 0) ? true : false;
		
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
