package com.webmailhostopia.testimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.common.utils.Verify;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class MailHomePage extends AbstractPageObject{

	public String verifyOptionsAvailableInMainPage(String userDropDownOptions)throws Exception{
		String stepResult = null;
		int i = 0;
		String temp = null;
		String[] expectedUserDropDownValues = userDropDownOptions.split(",");

		if( waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("USER_DROPDOWN_OPTIONS_XPATH"), 60) != null){
			List<WebElement> lsUserDropDwonElements = driver.findElements(By.xpath(elementLocatorProp.getProperty("USER_DROPDOWN_OPTIONS_XPATH")));

			for(WebElement dropDownOption : lsUserDropDwonElements){
				temp = dropDownOption.getText();
				if( temp.equalsIgnoreCase(expectedUserDropDownValues[i])){
					Log.info("String comparision is success :: actual : " + temp + " and expected :" + expectedUserDropDownValues[i]);
				}else{
					Log.error("String comparision failed!!!");
				}
				i++;
			}
		}else{
			Log.error("User drop down is not visible on the mail page!!!");
		}

		return stepResult;
	}


	public boolean sendEmail(String mailTo, String subject, String mailBody )throws Exception{
		boolean stepResult = false;

		int failCounter = 0;
		int mailInboxCountBefore = 0;
		int mailInboxCountAfter = 0;
		String subjectLine = subject + String.valueOf(generateRandomeNumber());

		WebElement newMailButton = pollDOMUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("NEW_EMAIL_XPATH")), 5);
		Log.info("New subject line : " + subjectLine);
		mailInboxCountBefore = getMailInboxCount();

		try{
			if( newMailButton != null){
				newMailButton.click();	
				if( waitUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("NEW_MAIL_AREA_XPATH"))) != null){
					Log.info("Composing mail..");
					typeEditBox(elementLocatorProp.getProperty("MSG_TO_ID"), mailTo);
					typeEditBox(elementLocatorProp.getProperty("SUBJECT_ID"), subjectLine);
					driver.switchTo().frame(driver.findElement(By.cssSelector(".cke_wysiwyg_frame.cke_reset")));
					driver.findElement(By.cssSelector(".cke_editable.cke_editable_themed.cke_contents_ltr.cke_show_borders")).sendKeys(mailBody);
					driver.switchTo().defaultContent();
					Log.info("Sending e-mail...");
					driver.findElement(By.xpath(elementLocatorProp.getProperty("SEND_BUTTON_XPATH"))).sendKeys(Keys.ENTER);

					if( !checkEmailSentMessageConfirmation())
						failCounter++;
					if( !navigateToCalendarSectionAndComeBack()){
						failCounter++;
					}else{
						mailInboxCountAfter = getMailInboxCount();
						if( !verifyMailReceipt(subjectLine))
							failCounter++;
					}
					if( mailInboxCountBefore+1 != mailInboxCountBefore)
						failCounter++;
				}
			}
		}catch(Exception e){
			Log.error("Exception occured while composing e-mail" + e.getMessage());
			e.printStackTrace();
		}
		return (failCounter == 0) ? true : false;
	}

	public int getMailInboxCount()throws Exception{
		int inboxMailCount = 0;
		WebElement we = null;

		Log.info("Inside getMailInboxCount method");

		we  = pollDOMUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("INBOX_MAIL_COUNT_XPATH")), 5);

		if( we != null){
			inboxMailCount = Integer.parseInt(we.getText());
			Log.info("Inbox unread mail count is : " + inboxMailCount);
		}
		return inboxMailCount;
	}

	public boolean checkEmailSentMessageConfirmation()throws Exception{

		int iter = 0;
		boolean isTextFound = false;
		WebElement we = null;

		Log.info("Inside checkEmailSentMessageConfirmation method");

		do{
			try{
				we = driver.findElement(By.xpath(elementLocatorProp.getProperty("MAIL_SENT_CONFIRM_MSG_XPATH")));
				if( we != null){
					isTextFound = true;
					break;
				}
			}catch(NoSuchElementException e){
				continue;
			}
		}while(isTextFound == true || iter<=5);

		return isTextFound;

	}

	public boolean navigateToCalendarSectionAndComeBack()throws Exception{

		boolean stepResult = false;

		Log.info("Inside navigateToCalendarSectionAndComeBack method");
		WebElement calendarIcon = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("CALENDAR_ICON_XPATH"), 
				80);
		if( calendarIcon != null){
			Log.info("Clicking on Calendar Icon...");
			calendarIcon.click();
			waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("SEARCH_IN_CALENDAR_XPATH"), 50);
			driver.findElement(By.xpath(elementLocatorProp.getProperty("MAIL_ICON_XPATH"))).click();
			if ( pollDOMUntilElementVisibility(By.xpath("MAIL_BODY_SORT_ID"), 5) != null){
				stepResult = true;
			}
		}

		return stepResult;
	}

	public int generateRandomeNumber() {
		Random r = new Random( System.currentTimeMillis() );
		return 10000 + r.nextInt(20000);
	}

	public boolean verifyMailReceipt(String mailSubjectLine)throws Exception{


		boolean stepResult = false;
		String subjectLineXpath = MessageFormat.format(elementLocatorProp.getProperty("SUBJECT_LINE_XPATH"), mailSubjectLine);

		Log.info("Inside verifyMailReceipt method");

		if( pollDOMUntilElementVisibility(By.xpath(subjectLineXpath), 5) != null){
			Log.info("Mail received with subject line : " + subjectLineXpath);
			stepResult = true;
		}else{
			Log.error("Mail not found in the inbox ");
		}

		return stepResult;

	}


	public boolean verifyTourOptionFunctionality()throws Exception{

		int failCounter = 0;

		WebElement newMailButton = pollDOMUntilElementVisibility(By.xpath(elementLocatorProp.getProperty("NEW_EMAIL_XPATH")), 5);

		Log.info("Inside verifyTourOptionFunctionality method");

		if( newMailButton != null) {
			driver.findElement(By.xpath(elementLocatorProp.getProperty("USER_DROP_DOWN"))).click();
			clickLink("Tour");
			Log.info("Switch to active element");
			driver.switchTo().activeElement();
			Log.info("Mouse over on Inbox");
			WebElement we = driver.findElement(By.xpath("//span[contains(text(),'Inbox')]"));
			moveToElement(we);
			WebElement startButton = waitForElementToBeClickable(ByLocator.xpath, 
					"//span[contains(text(),'Inbox')]/../following-sibling::div/button[contains(text(),'Start')]", 90);
			if( startButton != null){
				startButton.click();
				Log.info("Clicking on next icon...");
				driver.findElement(By.xpath("//a[contains(@href,'myCarousel')]"));
				//Rest of the code follows here
			}
		}
		return (failCounter ==0 ) ? true : false;
	}

	public boolean verifyLabelsLangugaeInMailHomePage()throws Exception{
		int failCounter = 0;

		Log.info("Changing the language to which is set in environment.prop file");
		LanguageTest.openPreferencesTab();
		LanguageTest.changeTheLanguage();
		if(!navigateToMailPage())
			failCounter++;
		if(!validateMailToolTipText())
			failCounter++;
		if(!verifySearchPlaceHolderText())
			failCounter++;
		if(!verifySearchBarDropDownLabels())
			failCounter++;
		if(!verifyAlertTextMessageWhenDiscardNewMail())
			failCounter++;
		return (failCounter==0)?true:false;
	}

	public boolean navigateToMailPage()throws Exception{

		WebElement mailSection = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("MAIL_SECTION_XPATH"), 60);

		if(mailSection == null){
			return false;
		}

		mailSection.click();
		if( waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("MAIL_LABEL_XPATH"), 60) == null ){
			return false;
		}else{
			return true;
		}
	}

	public boolean validateMailToolTipText()throws Exception{

		boolean stepResult = false;
		String nonEnglishTooltip = otherLangTestData.getProperty("MAIL_ICON_MOUSEOVER_TEXT");
		String englishToolTip = engTestData.getProperty("MAIL_LABEL_MOUSE_OVERTEXT");
		
		Actions builder=new Actions(driver);
		WebElement mailToolTip=driver.findElement(By.xpath(elementLocatorProp.getProperty("MAIL_LABEL_XPATH")));
		builder.moveToElement(mailToolTip).perform();
		String tooltip_msg=mailToolTip.getText();
		Log.info("Tool tip value : " + tooltip_msg);
		
		if( !tooltip_msg.equalsIgnoreCase(englishToolTip)){
			Log.info("Tool tip text is not in English, hence comparing the with other language");
			if(Verify.compareStringInNonEnglish(tooltip_msg, nonEnglishTooltip, locale)){
				Log.info("Tool tip text is in non-english");
				stepResult=true;
			}
		}
		return stepResult;	
	}
	
	public boolean verifySearchPlaceHolderText()throws Exception{
		boolean stepResult = false;
		
		Log.info("Inside verify search place golder text method");
		String nonEnglishSearchBarText = otherLangTestData.getProperty("MAIL_SEARCH_BOX_PLACEHOLDER");
		String englishSearchBarText = engTestData.getProperty("MAIL_LABEL_MOUSE_OVERTEXT");
		
		WebElement searchBarPlaceHolder = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("SEARCH_BAR_PLACE_HOLDER_TEXT"),
				60);
		if( searchBarPlaceHolder == null)
			return false;
		String searchBarPlaceHolderText = searchBarPlaceHolder.getAttribute("placeholder");
		
		if( !searchBarPlaceHolderText.equalsIgnoreCase(englishSearchBarText)){
			Log.info("Search bar placeholder text is not in English, hence comparing the with other language");
			if(Verify.compareStringInNonEnglish(searchBarPlaceHolderText, nonEnglishSearchBarText, locale)){
				Log.info("Tool tip text is in non-english");
				stepResult=true;
			}
		}
		return stepResult;
	}
	
	public boolean verifySearchBarDropDownLabels()throws Exception{
		boolean stepResult = false;
		List<String> lsLabelText = new ArrayList<String>();
		
		Log.info("Verfiying search bar dropdwon labels text in mail tab...");
		
		String[] expectedInEnglish = engTestData.getProperty("MAIL_SEARCH_BOX_DROPDOWN_VALUES").split(",");
		String[] expectedInNonEnglish = otherLangTestData.getProperty("MAIL_SEARCH_BOX_DROPDOWN_VALUES").split(",");
	
		WebElement searchBarDrpDwonBtn = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("SEARCH_BAR_DROPDWON_BUTTON_XPATH"), 60);
		
		if(searchBarDrpDwonBtn == null)
			return false;
		searchBarDrpDwonBtn.click();
		
		waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("SEARCH_BAR_DROPDOWN_ITEMS_XPATH"), 60);
		List<WebElement> lsSearchBarDropdownLabels = driver.findElements(By.xpath(elementLocatorProp.getProperty("SEARCH_BAR_DROPDOWN_ITEMS_XPATH")));
		for(WebElement label:lsSearchBarDropdownLabels){
			String temp = label.getText().trim();
			if( !temp.isEmpty()){
				lsLabelText.add(label.getText().trim());
			}
		}
		String[] actuals = new String[lsLabelText.size()];
		actuals = lsLabelText.toArray(actuals);

		if( !Verify.compareStringsInEnglish(actuals, expectedInEnglish,true) && Verify.compareStringInNonEnglish(actuals, expectedInNonEnglish, locale)){
			Log.info("Search bar dropdown values labels are not in English");
			stepResult=true;
			Log.info("Search bar dropdown values labels are in non-English");
		}
		
		return stepResult;
	}
	
	public boolean verifyAlertTextMessageWhenDiscardNewMail()throws Exception{
		
		boolean stepResult = false;
		String alertTextInEnglish = engTestData.getProperty("DISCARD_MAIL_ALERT_TEXT");
		String alertTextInNonEnglish = otherLangTestData.getProperty("DISCARD_MAIL_ALERT_TEXT"); 
		
		WebElement composeBtn = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("MAIL_COMPOSE_BUTTON"), 60);
		if( composeBtn == null)
			return false;
		composeBtn.click();
		
		WebElement discardBtn = waitForElementToBeClickable(ByLocator.xpath, elementLocatorProp.getProperty("DISCARD_BTN_XPATH"), 120);
		if( discardBtn == null)
			return false;
		discardBtn.click();
		
		Alert alert = driver.switchTo().alert();
		if(alert == null)
			return false;
		String actualAlertText = alert.getText();
		
		if( !actualAlertText.equalsIgnoreCase(alertTextInEnglish)){
			Verify.compareStringInNonEnglish(actualAlertText, alertTextInNonEnglish, locale);
		}
		return stepResult;
	}
}
