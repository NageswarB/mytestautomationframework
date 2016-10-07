package com.webmailhostopia.testimpl;

import org.openqa.selenium.WebElement;

import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class MailInboxPage extends AbstractPageObject{
	
	InboxActionsHelper helper = new InboxActionsHelper();
	
	public boolean composeAndSendMail(String toMail) throws Exception{
		int failCounter = 0;
		
		WebElement composeButton = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("NEW_EMAIL_XPATH"), 60);
		
		if( composeButton == null){
			Log.error("Compose button is not visible on the page, hence failing this test case!!!");
		}else{
			composeButton.click();
			typeEditBox("msginto", toMail);

			if( helper.verifyMailSentMessage()){
				
			}
		}
		
		
		
		
		return (failCounter == 0) ? true : false;
		
	}

}
