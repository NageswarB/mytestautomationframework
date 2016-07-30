package com.webmailhostopia.selenium.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import com.google.common.base.Function;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.webmailhostopia.common.utils.ByLocator;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.common.utils.TestCommonResource;

/**
 * 
 * @author nageswar.bodduri
 * This class contains all reusable methods and driver instantiate calls 
 *
 */
public class AbstractPageObject {

	protected static WebDriver driver;
	private static boolean _debugMode_ = false; 
	protected static boolean isLoggedIn = false;
	protected static boolean isBrowserOpen = false;
	protected String webDriverEventListenerClasses = "com.webmailhostopia.selenium.webdriver.WebDriverScreenShotEventHandler";
	private String seleniumTimeOut = "30000";
	private String pageLoadTimeout = "180000";
	protected static final long DefaultTimeOutInSeconds = 30;
	protected static Properties SUTprop;
	protected static Properties elementLocatorProp;
	protected static final long shortTimeOutInSeconds = 1;

	protected enum SimplePopUpAction{ClickOK, ClickCancel, ClickYes, ClickNO }

	//@FindBy(css="button:contains('OK')")
	@FindBy(xpath="//span[contains(./text(),'OK')]")
	private WebElement btnOK;

	@FindBy(css="button:contains('Cancel')")
	private WebElement btnCancel;

	static{
		loadEnvironmentProps();
		loadElementLocatorsRepository();
	}

	public AbstractPageObject(){
		initElements();
	}

	void initElements(){
		if(driver == null){
			openBrowser();
		}
	}

	public static void loadEnvironmentProps(){
		SUTprop = new Properties();
		String workDir = System.getProperty("user.dir");

		File f=new File(workDir + "/environment.properties");

		if( f.exists()){
			Log.info(workDir + " is exists!!");
			try{
				SUTprop.load(new FileInputStream(f));
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			Log.error(f.getName() + " not found !!!");
		}
	}

	public static void loadElementLocatorsRepository(){
		elementLocatorProp = new Properties();
		String workDir = System.getProperty("user.dir");

		File f=new File(workDir + "/elementlocators.properties");

		if( f.exists()){
			Log.info(workDir + " is exists!!");
			try{
				elementLocatorProp.load(new FileInputStream(f));
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			Log.error(f.getName() + " not found !!!");
		}
	}

	/**
	 * This method is used to create browser object based on the browser type set in env properties
	 */
	protected void openBrowser(){

		if (!isBrowserOpen){
			try{
				WebDriverWrapper driverWrapper = getBrowserInstance();
				isBrowserOpen = true;
				initWebDriverEventListener(driverWrapper);

				if( seleniumTimeOut != null ){
					setImplicitWait(driverWrapper, seleniumTimeOut);
					setPageLoadTimeOut(driverWrapper, pageLoadTimeout);
				}

				driver = driverWrapper;
				driver.manage().window().maximize();

			}catch(Exception ex){
				Log.error("Exception while opening a browser " + ex.getMessage());
			}
		}

	}

	private WebDriverWrapper getBrowserInstance()throws Exception{

		WebDriverType wdType = null;
		String sWebDriverType = SUTprop.getProperty("BROWSER");
		WebDriverWrapper webDriverInstance = null;
		wdType = setWebDriverType(sWebDriverType);
		webDriverInstance = WebDriverFactory.getWebDriver(wdType);
		return webDriverInstance;

	}

	private WebDriverType setWebDriverType(String browsertype) throws Exception{

		WebDriverType wdType = null;

		if( browsertype != null){
			if(browsertype.equalsIgnoreCase("chrome")){
				wdType = WebDriverType.CHROME_DRIVER;
			}else if(browsertype.equalsIgnoreCase("iexplorer")){
				wdType = WebDriverType.INTERNET_EXPLORER_DRIVER;
			}else{
				wdType = WebDriverType.FIREFOX_DRIVER;
			}
		}
		return wdType;
	}


	private void initWebDriverEventListener(WebDriverWrapper driverWrapper){
		if(webDriverEventListenerClasses != null && !webDriverEventListenerClasses.isEmpty()){
			//if you have more than one event listner
			String[] listeners = webDriverEventListenerClasses.split(";");

			ClassLoader loader;
			loader = this.getClass().getClassLoader();

			for(String eventListenerClass : listeners){
				try {
					if( eventListenerClass != null && !eventListenerClass.isEmpty()){
						eventListenerClass = eventListenerClass.trim();
						Class<?> loadClass = loader.loadClass(eventListenerClass);
						WebDriverEventListener webDriverEventListener = (WebDriverEventListener) loadClass.newInstance();
						driverWrapper.register(webDriverEventListener);
					}
				}catch(Throwable e){
					Log.error(eventListenerClass + " can not be loaded " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private void setImplicitWait(WebDriverWrapper webDriverWrapper, String seleniumTimeOut){
		try{
			long miliSecs = Long.valueOf(seleniumTimeOut);
			Log.info("Set webdriver implicitlyWait seleniumTimeOut to " + seleniumTimeOut + "(MilliSeconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.SECONDS);
		}catch(Exception e){
			Log.error("Unable to set implicitly wait!!");
		}
	}

	private void setPageLoadTimeOut(WebDriverWrapper webDriverWrapper, String pageLodTimeout){
		try{
			long miliSecs = Long.valueOf(pageLodTimeout);
			Log.info("Set webdriver pageLoadTimeOut to " + pageLodTimeout + "(MilliSeconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.SECONDS);
		}catch(Exception e){
			Log.error("Unable to set implicitly wait!!");
		}
	}

	/**###
	 * #commonly used web application actions
	 * ###
	 */

	public void typeEditBox(String editBoxName, String textToType) throws Exception{

		String xpath = "//input[@id='" + editBoxName + "' or " +
				"@name='" + editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));
		WebElement editBox = driver.findElement(By.xpath(xpath));
		editBox.sendKeys(textToType);
	}


	public WebElement waitForElementVisibility(By by){
		return waitForElementVisibility(by, DefaultTimeOutInSeconds);
	}

	public WebElement waitForElementVisibility(By by, long timeOutInSeconds){

		WebElement webElement = null;
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		try{
			webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}catch(TimeoutException ex){
			//to do things
		}

		if(webElement == null)
			Log.warn("Waited for " + timeOutInSeconds + " seconds for the webelement visibility...but not found");		
		return webElement;
	}

	/**
	 * This method is used to wait for an element until it is visible in the web page
	 * Used fluent wait to return the control right after the element visibility 
	 * @param by
	 * @return web element
	 */
	public WebElement waitUntilElementVisibility(final By by){

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement we = wait.until(new Function<WebDriver, WebElement>() {

			public WebElement apply(WebDriver driver){
				return driver.findElement(by);
			}
		});
		return we;
	}

	public static WebElement waitForElementToBeClickable(ByLocator by, String elementLocator,long timeoutInSeconds)throws Exception{
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		switch (by) {
		case xpath:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		case id:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.id(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		case linktext:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		case  css:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		case  classname:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.className(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		case  name:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.name(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;

		default:
			break;
		}		
		if (element == null) {
			throw new Exception ("Web element not found: " + elementLocator);
		}
		return element;
	}

	/**
	 * Checks check box. 
	 * @param chkBoxName - check box unique identifier
	 */
	public void checkChkBox(String chkBoxName) {
		checkCheckbox(chkBoxName, true);
	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch - text to be search in a specific tag
	 * @param positionToCheckForCheckBox - check a check box at the position passed here...
	 * @param check - pass true to check and false to uncheck
	 */
	private boolean checkObjectWithText(String textToSearch, String positionToCheckForCheckBox, boolean check){
		String tagWithText = "//.[text()='"+textToSearch+"']";
		String checkBoxXPath = tagWithText + positionToCheckForCheckBox; 

		if (check && !driver.findElement(By.xpath(checkBoxXPath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXPath)).click();
		}
		if (!check && driver.findElement(By.xpath(checkBoxXPath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXPath)).click();
		}

		return driver.findElement(By.xpath(checkBoxXPath)).isSelected();

	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch
	 * @param positionToCheckForCheckBox
	 */
	public boolean checkObjectWithText(String textToSearch, String positionToCheckForCheckBox){
		return checkObjectWithText(textToSearch, positionToCheckForCheckBox, true);
	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch
	 * @param positionToCheckForCheckBox
	 */
	public boolean UncheckObjectWithText(String textToSearch, String positionToCheckForCheckBox){
		return checkObjectWithText(textToSearch, positionToCheckForCheckBox, false);
	}


	/**
	 * Un-checks check box. 
	 * @param chkBoxName - check box unique identifier
	 */
	public void uncheckChkBox(String chkBoxName) {
		checkCheckbox(chkBoxName, false);
	}

	/**
	 * A private method to do check/uncheck 
	 * @return
	 */
	protected void checkCheckbox(String checkboxName, boolean enable) {
		String checkBoxXpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"' or @title='Select " + checkboxName + "')]" +
				" | //span[.='" + checkboxName + "']/preceding-sibling::input"+
				" | //td[text()='" + checkboxName + "']/preceding-sibling::td/input";

		try {
			WebDriverWait wait = new WebDriverWait(driver, 8); 
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkBoxXpath)));
		} catch (org.openqa.selenium.TimeoutException ex) {
		}
		if ( enable && !driver.findElement(By.xpath(checkBoxXpath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
		if ( !enable && driver.findElement(By.xpath(checkBoxXpath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
	}


	public  void checkCheckboxes(List<String> checkboxName) {
		for (String item : checkboxName) {
			checkChkBox(item);
		}
	}

	/**
	 * Check box is checked?. 
	 * @param chkBoxName - check box unique identifier
	 */
	public boolean isChecked(String checkboxName) {
		String checkBoxCpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"')]";
		return driver.findElement(By.xpath(checkBoxCpath)).isSelected();

	}

	/**
	 * Use this method to click on the OK button
	 * @return
	 * @throws Exception 
	 */
	public void clickOK() throws Exception {
		clickButton("OK");	
		if (SUTprop.getProperty("BROWSER") == WebDriverType.INTERNET_EXPLORER_DRIVER.toString()) {
			sendEnterOnWebElement(btnOK);
		}
	}


	/**
	 * Use this method to click on the OK button
	 * @return
	 * @throws Exception 
	 */
	public void clickCancel() throws Exception {
		clickButton("Cancel");	
		if (SUTprop.getProperty("BROWSER") == WebDriverType.INTERNET_EXPLORER_DRIVER.toString()) {
			sendEnterOnWebElement(btnCancel);
		}
	}

	private static void sendEnterOnWebElement(WebElement element){
		try {
			int count = 0 ;
			while (element.isEnabled() && count < 5){//Special case for IE9 
				Log.info("Sending Enter to WebElement");
				element.sendKeys(Keys.ENTER);
				count ++ ;
				Thread.sleep(2000);
			}
		} catch (Exception e) {
		}	
	}

	/**
	 * Use this method to know whether the current page is the last page or not	
	 * @return true if it's a last page; false otherwise
	 */
	public boolean isLastPage(){
		return waitForElementVisibility(By.xpath("//input[@value='last page']"),20)==null?true:false;
	}

	/**
	 * Use this method to know whether the next page does exist or not.	
	 * @return true if next page does exist;false otherwise
	 */
	public boolean isNextPageExists(){
		return waitForElementVisibility(By.xpath("//input[@value='next page']"),20)!=null?true:false;
	}

	/**
	 * Use this method to know whether the current page is the first page or not	
	 * @return true if it's a first page; false otherwise
	 */
	public boolean isFirstPage(){
		return waitForElementVisibility(By.xpath("//input[@value='first page']"),20)==null?true:false;
	}

	/**
	 * Use this method to navigate to the next page	
	 */
	public void navigateToNextPageInTheTable(){
		if(isNextPageExists())
			clickImage("next page");
		else
			Log.error("Next Page doesn't exist in this table");
	}

	/**
	 * Use this method to navigate to the first page	
	 */
	public void navigateToFirstPageInTheTable(){
		if(isFirstPage()){
			Log.info("already in the first page of the table");
			return;
		}
		else{
			try {
				if(isElementPresent(By.xpath("//input[@value='first page']")))
					clickImage("first page");
			} catch (Exception e) {
				Log.error("Failed to navigate to first Page in the table");
			}
		}
	}

	/**
	 * Use this method to navigate to the last page	
	 */
	public void navigateToLastPageInTheTable(){
		if(isFirstPage()){
			Log.info("already in the last page of the table");
			return;
		}
		else{
			try {
				if(isElementPresent(By.xpath("//input[@value='last page']")))
					clickImage("last page");
			} catch (Exception e) {
				Log.error("Failed to navigate to last Page in the table");
			}
		}
	}

	/**
	 * Clicks a button after verified that it is visible and that you can click it.
	 * @param btnName -unique representer of the button. E.g, unique name or id. 
	 * @throws Exception 
	 */	
	public static void clickButton(String btnName) throws Exception {
		clickButton(btnName, 8);
	}

	public void clickButtonInGrid(String itemGridName) throws Exception {
		String btnGridXpath = "//a[.='" + itemGridName + "']/../preceding-sibling::td/span//button";
		clickOnElement(ByLocator.xpath, btnGridXpath, 5);
	}

	/**
	 * Clicks a span element with a given text on page
	 * @param text
	 * @throws Exception 
	 */
	public void clickSpanElement(String text) throws Exception {
		String spanXpath = "//span[.='" + text + "']";
		clickOnElement(ByLocator.xpath, spanXpath, 5);
	}

	/**
	 * Clicks a button after verified that it is visible and that you can click it.
	 * @param btnName - unique representer of the button. E.g, unique name or id. 
	 * @param timeoutInSeconds - max time in seconds to wait for the button to be visible and clickable.
	 * @throws Exception
	 */
	public static void clickButton(String btnName, long timeoutInSeconds) throws Exception {
		String btnXpath = "//button[@value='" + btnName+ "' or text()='" + btnName + "'] | " +
				"//button[contains(text(),'" + btnName + "')] | "+
				"//input[@value='" + btnName + "'] | //input[contains(@value,'" + btnName+ "')] | //input[(@title='" + btnName + "')] | //input[(@name='" + btnName + "')] | " +
				"//input[@id='" + btnName + "'] | //span[@id='" + btnName + "'] | //span[contains(text(),'" + btnName + "')]/following-sibling::span[@role='img'] | "+ 				
				"//span[contains(text(),'" + btnName + "')][@class='x-btn-inner x-btn-inner-center']";

		clickOnElement(ByLocator.xpath, btnXpath, timeoutInSeconds);
	}

	/**
	 * Clicks a button which may have different names/id/title on different browsers. i.e, IE, FF.
	 * @param possibleButtnNames - array of possible buttons names
	 * @param timeoutInSeconds - time out in seconds to wait for the button to show  before giving up.
	 * @throws Exception
	 */
	public static void clickButton(String[] possibleButtnNames, long timeoutInSeconds) throws Exception {
		boolean buttonFound = false;
		int loop = possibleButtnNames.length - 1;
		while (loop >= 0 && buttonFound == false){
			try {
				clickButton(possibleButtnNames[loop--],timeoutInSeconds );
				buttonFound = true;
			} catch (Exception ex) {
				// ignore
			}
		}
		if (buttonFound == false) {
			throw new Exception ("Button not found: " + possibleButtnNames.toString()); 
		}
	}

	/**
	 * Clicks a button which may have different names/id/title on different browsers. i.e, IE, FF.
	 * @param possibleButtnNames - array of possible buttons names
	 * @param timeoutInSeconds - time out in seconds to wait for the button to show  before giving up.
	 * @throws Exception
	 */
	public static void clickButton(String[] possibleButtnNames) throws Exception {
		clickButton(possibleButtnNames, DefaultTimeOutInSeconds);
	}

	/**
	 * Wait for button to be visible and clickable
	 * @param btnName - unique representer of the button. E.g, unique name or id. 
	 * @param timeoutInSeconds - max time in seconds to wait for the button to be visible and clickable.
	 * @throws Exception
	 */
	public static void waitForButtonToBeClickable(String btnName, long timeoutInSeconds) throws Exception {
		String btnXpath = "//button[@value='" + btnName+ "' or text()='" + btnName + "'] | " +
				"//input[@value='" + btnName + "'] | //input[(@name='" + btnName + "')] | " +
				"//input[@id='" + btnName + "'] | //span[@id='" + btnName + "'] | //span[contains(text(),'" + btnName + "')]/following-sibling::span[@role='img'] | "+ 				
				"//span[contains(text(),'" + btnName + "')][@class='x-btn-inner x-btn-inner-center']";

		waitForElementToBeClickable(ByLocator.xpath, btnXpath, timeoutInSeconds);
	}

	public static void clickOnElement(ByLocator by, String elementLocator,long timeoutInSeconds) throws Exception {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		switch (by) {
		case xpath:			
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		default:
			break;
		}	
	}


	/**
	 * Use this method to click on an image by title or alt attribute
	 * @param imageIdentifier - make sure it represents unique identifier of the image on the page. 
	 */	
	public void clickImage(String imageIdentifier) {
		String imgXpath = "//input[@type='image'][@alt='" + imageIdentifier + "' or @title='" + imageIdentifier + "'] | " +
				" //input[@type='image'][contains(@alt,'" + imageIdentifier + "')]";
		WebDriverWait wait = new WebDriverWait(driver, 3); // for links that become enable by client-side js
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(imgXpath)));
		WebElement button = driver.findElement(By.xpath(imgXpath));

		button.click();
		if(SUTprop.getProperty("BROWSER") == WebDriverType.INTERNET_EXPLORER_DRIVER.toString()){
			sendEnterOnWebElement(button);	
		}
	}


	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void clickLink(String linkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null)?lnkText:linkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link;
		if (lnkText == null) {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
		} else {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.="+linkText+"]")));
		}

		if (link == null ) {
			Log.debug("debug:: link wasn't found. try to locate with CSS locator ....");
			link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains(^"+linkText+"$)")));
		}

		if (link!=null){
			highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + linkText);
		}
	}

	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void SearchAllPagesAndclickLink(String linkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null)?lnkText:linkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link=null;
		boolean nextpage=true;
		do{		
			if (lnkText == null) {
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			else {
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.=concat(" + linkText + ")]")));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (link == null ) {
				Log.debug("debug:: link wasn't found. try to locate with CSS locator ....");
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains(^"+linkText+"$)")));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(link!=null)
				break;
			else if(isElementPresent(By.xpath("//input[@title='next page']"))){
				driver.findElement(By.xpath("//input[@title='next page']")).click();
			}
			else{
				nextpage=false;
				break;
			}

		}while(nextpage);

		if (link!=null){
			highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + linkText);
		}
	}

	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void SearchAllPagesAndSelectRoleCheckBox(String adminRole) throws Exception {
		//String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(adminRole);
		//lnkText = (lnkText!=null)?lnkText:adminRole;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link=null;
		boolean nextpage=true;
		do{		
			try{
				link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='checkbox'][@title='Select \""+adminRole+"\"']")));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			/*	  else {
			  try{
				  link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.=concat(" + adminRole + ")]")));
			  }
			  catch (Exception e) {
				// TODO: handle exception
			}
		  }
		 /*  if (link == null ) {
			report.report("debug:: link wasn't found. try to locate with CSS locator ....");
			try{
			 link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains(^"+adminRole+"$)")));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		  }*/
			if(link!=null)
				break;
			else if(isElementPresent(By.xpath("//input[@title='next page']"))){
				driver.findElement(By.xpath("//input[@title='next page']")).click();
			}
			else{
				nextpage=false;
				break;
			}

		}while(nextpage);

		if (link!=null){
			highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + adminRole);
		}
	}

	/**
	 * Clicks link by link text, looking for the text to be contained.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void clickLinkPartialText(String partialLinkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(partialLinkText);
		partialLinkText = (lnkText!=null)?lnkText:partialLinkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link;
		if (lnkText == null) {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(partialLinkText)));
		} else {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.,concat(" + partialLinkText + ")]")));
		}
		if (link == null ) {
			Log.debug("debug:: partially link  text wasn't found. try to locate with CSS locator ....");
			link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains("+partialLinkText+")")));
		}

		if (link!=null){
			highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + partialLinkText);
		}
	}


	/**
	 * Clicks link by link text ONLY if it is exist ,otherwise do nothing
	 * @param linkText
	 */
	public boolean clickLinkOnlyIfExists(String linkText) {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null && !linkText.startsWith("/"))?lnkText:linkText;

		boolean found = true ;
		WebElement link = null;
		WebDriverWait wait = new WebDriverWait(driver, shortTimeOutInSeconds);
		try {
			if (lnkText == null) {
				link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
			} else {
				if (linkText.startsWith("/")) // link text is actualy an XPATH
				{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(linkText)));
				} else {
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.=concat(" + linkText + ")]")));
				}				
			}
		} catch (org.openqa.selenium.TimeoutException ex) {
			found = false;
		} catch (org.openqa.selenium.NoSuchElementException ex2) {
			//ignore
		}
		if (link!=null) {
			highlightElement(driver, link);
			link.click();
		}
		return found;

	}

	/**
	 * private method to be used when text contains signle quote signs.
	 * The methods returns the string in a different form to be used with the Xpath concat function 
	 * @param str - the string
	 * @return string in a form to be used with the Xpath concat function 
	 */
	private String reconstructStringWithSingleQuoteForXpathConcatFunction(String str) {
		StringBuilder sb = null;
		if (str.contains("'")) {
			String[] arr = str.split("'");
			sb = new StringBuilder();
			for (int i =0; i < arr.length-1; i++) {
				sb.append("'" + arr[i] + "'");
				sb.append(",");
				sb.append("\"'\"");
				sb.append(",");
			}
			sb.append("'" + arr[arr.length-1] + "'");
		}

		return (sb == null)?null:sb.toString();
	}

	/**
	 * Use this method to type a text in edit box
	 * @param WebElement
	 * @param textToType
	 */
	public void typeEditBoxByWebElement(WebElement editBox, String textToType) throws Exception{
		int count = 0 ;
		editBox.clear();
		editBox.sendKeys(textToType);
		if (!editBox.getAttribute("value").contains(textToType) & count < 3){
			editBox.clear();
			editBox.sendKeys(textToType); 
		}
	}

	/**
	 * Used to type something inside a text edit box 
	 * @param xpath
	 * @param textToType
	 */
	private void enterTextToField (String xpath , String textToType) {
		WebElement editBox = driver.findElement(By.xpath(xpath));
		highlightElement(driver, editBox);
		editBox.clear();
		editBox.sendKeys(textToType);
	}

	protected void clearInputByClickingDelete(WebElement webElement) {	    
		while (!StringUtils.isEmpty(webElement.getAttribute("value"))) {
			// "\u0008" - is backspace char
			webElement.sendKeys("\u0008");
		}
	}
	/**
	 * Use this method to select from drop-down list
	 * @param selectNameOrID - set the name or id of the desired select tag
	 * @param valueToSelect - the value to select from the drop-down list
	 */
	public void selectByNameOrID(String selectNameOrID, String valueToSelect) {
		/* WebElement we = waitForElementVisibility(By.xpath("//select[contains(@name,'" + selectNameOrID + "') or " +
				 										  "contains(@id,'"+ selectNameOrID + "') or " + "contains(@title,'" + selectNameOrID + "')]"));
		 */ 
		//added by bodna02 to handle a select element if it is inside a span tag when a name / id couldn't able to identify
		WebElement we = waitForElementVisibility(By.xpath("//select[contains(@name,'" + selectNameOrID + "') or " + 
				"contains(@id,'"+ selectNameOrID + "') or " + "contains(@title,'" + selectNameOrID + "')] | " + 
				"//span[@id='"+ selectNameOrID +"']/select | " + "//td[span[contains(@title,"+"'"+ selectNameOrID +"'"+")]]/following-sibling::td/select | " + "//td[contains(text(),"+"'"+ selectNameOrID +"'"+")]/select"));

		highlightElement(driver, we);
		setSelectedField(we, valueToSelect);
		// sendEnterOnWebElement(we);

		try {

			if (we.getAttribute("onchange")!= null){
				Log.info("firing 'onchange' event");
				((JavascriptExecutor) driver).executeScript(we.getAttribute("onchange")); 
				// Thread.sleep(2000);
			}

			String handler = we.getAttribute("handler");
			if (handler!=null) {
				Log.info("firing 'onchange' event");
				handler = handler.substring(handler.indexOf('{')+1, handler.lastIndexOf('}')-1);
				((JavascriptExecutor) driver).executeScript(handler); 

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to select from drop-down list using WebElement parameter
	 * @param WebElement of the select tag
	 * @param optionToSelect
	 */
	public void selectByWebElement(WebElement selectTag, String optionToSelect) {
		highlightElement(driver, selectTag);
		setSelectedField(selectTag, optionToSelect);

		try {
			if (selectTag.getAttribute("onchange")!= null){
				((JavascriptExecutor) driver).executeScript(selectTag.getAttribute("onchange")); 
			}
			String handler = selectTag.getAttribute("handler");
			if (handler!=null) {
				Log.info("firing 'onchange' event");
				handler = handler.substring(handler.indexOf('{')+1, handler.lastIndexOf('}')-1);
				((JavascriptExecutor) driver).executeScript(handler); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Use this method to select from radio buttons options
	 * @param radioNameOrID - set the name or id of the desired select tag
	 * @param valueToSelect - If value is dynamic set to NA
	 */
	public void selectRadioButton(String radioNameOrID, String valueToSelect) {
		String xpath = "";
		if (valueToSelect.contains("NA")){
			xpath = "//input[@type='radio'][(@name='" + radioNameOrID + "' or " + "@id='"+ radioNameOrID + "' or "+ "@value='" + radioNameOrID + "' or " +
					"@title[contains(.,'"+ radioNameOrID + "')]" + " or "+ "@title='" + "Select " + "\"" + radioNameOrID + "\"" + "')]";
		}else {
			xpath = "//input[@type='radio'][(@name='" + radioNameOrID + "' or " + "@id='"+ radioNameOrID + "' or "+ 
					"@title[contains(.,'"+ radioNameOrID + "')]" + " or " + "@title='" + "Select " + "\"" + radioNameOrID + "\"" + "')  and @value='" + valueToSelect + "']";
		}

		waitForElementVisibility(By.xpath(xpath));
		driver.findElement(By.xpath(xpath)).click();
	}
	/**
	 * Use this method to wait explicitly for element
	 * @param locator
	 * @param timeout in seconds
	 *//*
    public void waitFor(By locator, int timeout){
    	WebDriverWait wait = new WebDriverWait(driver, timeout); 
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
	  */
	/**
	 * Use this method move to generic IM tab
	 * @param tabName
	 */
	public void switchToTab(String tabName)throws Exception {
		waitForElementVisibility(By.xpath("//input[@value='" + tabName + "']"));
		WebElement input = driver.findElement(By.xpath("//input[@value='" + tabName + "']"));
		highlightElement(driver, input);
		input.click();
	}
	private void setSelectedField(WebElement selectWebElement, String value) {
		Select dropDown = new Select(selectWebElement);
		try {
			dropDown.selectByVisibleText(value);
		} catch (Exception ex) {

			dropDown.selectByValue(value);
		} 

		/*List<WebElement> options = element.findElements(By.tagName("option"));
	    for (WebElement option : options) {
	        if (value.equals(option.getAttribute("value")) || option.getText().equals(value)) {
	            if(!option.isSelected()) {
	                (Select)option.click();
	                break;
	            }
	        }
	    }*/
	}


	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isTextExistInTable(String text) throws Exception{
		return isTextExistInTable(text, DefaultTimeOutInSeconds);
	}


	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isTextExistInTable(String text, long waitTimeInSeconds) throws Exception {
		String xpath = "//td[text() ='"+text+"'] | //td/span[text() ='"+text+"']" ;
		//String xpath ="//span[text() ='"+text+"']";
		WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds); // for links that become enable by client-side js
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		}
		catch(Exception e){
			return false;
		}
		return (driver.findElements(By.xpath(xpath)).size() > 0);
	}

	/**
	 * Deletes rows in the webtable by clicking - (minus) icon after searching a matching text
	 * Author INJSU01
	 * @param text - the text to look for in each row of the table
	 * @param tableName - the id/name of the table
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean clickRemoveIconBasedOnTextInTable(String text, String tableName) throws Exception {
		return clickRemoveIconBasedOnTextInTable(text,tableName,DefaultTimeOutInSeconds);
	}

	/**
	 * Deletes rows in the webtable by clicking - (minus) icon after searching a matching text   
	 * Author INJSU01
	 * @param text - the text to look for in each row of the table
	 * @param tableName - the id/name of the table
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean clickRemoveIconBasedOnTextInTable(String text, String tableName, long waitTimeInSeconds) throws Exception {

		//Construct xpath for the table in webpage
		String tableXPath = "//table[@id='" + tableName + "' or @name='" + tableName + "']";

		WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tableXPath)));
		}catch(Exception e){
			throw (new Exception("Webtable not found")); 
		}

		//Get the number of rows in the webtable
		int numOfRows =  driver.findElements(By.xpath(tableXPath + "//tr")).size();

		//Trimming all spaces in the text passed by user
		String trimmedText = text.replaceAll("\\s+", "");
		String cellText;
		int rowNum, colNum, numOfCols;

		//Starting the search in rows from 2nd row as 1st row is header row
		for(rowNum=2;rowNum<=numOfRows;rowNum++){
			numOfCols = driver.findElements(By.xpath(tableXPath + "//tr[" + rowNum + "]//td")).size();
			for(colNum=1;colNum<numOfCols;colNum++){
				//Get the text from each cell ignoring the last cell as it contains minus icon
				//Case cell contains input box
				if (isElementPresent(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + colNum + "]/input")))
					cellText = (driver.findElement(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + colNum + "]/input")).getAttribute("value")).replace("\n","");
				else
					//Case cell contains normal text
					//Removing new line characters as the cell text is spanned across multiple lines in some cases
					cellText = (driver.findElement(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + colNum + "]")).getText()).replace("\n","");
				//Trimming all spaces in the cell text
				cellText = cellText.replaceAll("\\s+", "");
				//Case drop-down/select box exists in the cell
				if (isElementPresent(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + colNum + "]/select"))){
					if (cellText.contains(trimmedText)){
						driver.findElement(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + numOfCols + "]/input")).click();
						return true;
					}
				}
				else {
					//case cell contains normal text
					if (cellText.equals(trimmedText)){
						//Click - icon which removes the matching row
						driver.findElement(By.xpath(tableXPath + "//tr[" + rowNum + "]//td[" + numOfCols + "]/input")).click();
						return true;
					}
				}
			}
		}

		//Case owner rule passed by user does not exist
		throw (new Exception("\"" + text + "\"" + " does not exist in webtable"));
	}

	/**
	 * Takes the column name as input and returns all the values in that column as string array.
	 * @param ColName
	 * @return
	 * @throws Exception
	 */
	public List<String> getColumnValuesFromTable(String ColName) throws Exception {
		String XpathCol="//th[(text())] | //th/input";

		//get the column number with the help of column name
		List<WebElement> colnamelist=driver.findElements(By.xpath(XpathCol));
		int colnum=1;
		for (WebElement el:colnamelist)
		{
			if((el.getText()!=null) && (el.getText().trim().equalsIgnoreCase(ColName)))
			{
				Log.info("The column numver is "+ colnum);
				break;
			}
			else if ((el.getAttribute("value")!=null) && (el.getAttribute("value").trim().equalsIgnoreCase(ColName)))
			{
				Log.info("The column number is "+ colnum);
				break;
			}
			else
			{
				colnum++;
				Log.info("The column number is " + colnum);
			}
		}
		//after getting the column number get the list of element and return the strin array
		String xpath="//td["+colnum+"]";
		List<WebElement> colvaluelist=driver.findElements(By.xpath(xpath));
		List<String> colvalues=new ArrayList<String>();
		for (WebElement el:colvaluelist)
		{
			colvalues.add(el.getText().trim());
		}
		return colvalues;
	}

	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isChckboxNameExistInTable(String text, long waitTimeInSeconds) throws Exception {
		String xpath = 	"//input[@title='Select \""+ text +"\"']";
		WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds); // for links that become enable by client-side js
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		}
		catch(Exception e){
			return false;
		}
		return (driver.findElements(By.xpath(xpath)).size() > 0);
	}

	/**
	 * Verifies if any option is available in Select Box
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isOptionExistInSelect(String selectID) throws Exception {
		String xpath = "//select[@id='"+selectID+"']/option";
		//int num = driver.findElements(By.xpath(xpath)).size();
		if(driver.findElements(By.xpath(xpath)).size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Verifies if any option is available in Select Box
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public void selectFirstOptionInSelect(String selectID) throws Exception {
		String xpath = "//select[@id='"+selectID+"']/option";
		try{
			driver.findElement(By.xpath(xpath)).click();
		}
		catch(Exception e){
			e.printStackTrace();
		}



	}

	/**
	 * Use this method to verify if a text is being displayed somewhere on the active page.
	 * @param text
	 * @return
	 * @throws Exception 
	 */
	public static boolean isTextPresent(String text) throws Exception{
		return driver.getPageSource().contains(text);
		//return (driver.findElements(By.xpath("//.[.='"+ text +"']")).size() > 0);
	}

	/**
	 * Wait for text to present within a given element
	 * @param text
	 * @return true if text was found and false otherwise.
	 * @throws Exception 
	 */
	public boolean waitForTextVisibility(ByLocator by, String elementLocator, String text) throws Exception {
		return waitForTextVisibility(by, elementLocator, text, DefaultTimeOutInSeconds);
	}

	public boolean waitForTextVisibility(ByLocator by, String elementLocator, String text, long timeoutInSeconds) throws Exception {
		WebElement webElement = null;
		switch (by) {
		case xpath:
			webElement = waitForElementVisibility(By.xpath(elementLocator + "[contains(text(),\""+ text +"\")]"), timeoutInSeconds);
			break;
		case classname:
		case css:
		case linktext:
		case id:
		case name:
		case title:
			Log.error("Currently waitForTextVisibility method doesn't support this locator type: " + by.name());
		}
		return (webElement !=null)?true:false;
	}

	/**
	 * Use this method to handle simple popup windows.
	 * Simple popup usually come with 2 buttons: Yes,No or OK,Cancel
	 * @param popupTitle
	 * @param action - the action to do (e.g, click yes). Assuming the action will close the popup.
	 * @throws Exception 
	 */
	public void handlePopUp(String popupTitle, SimplePopUpAction action) throws Exception {
		String parentWindowHandle = driver.getWindowHandle(); // save the current window handle.
		WebDriver popup = null;
		Set<String> winHndlSet = driver.getWindowHandles();
		boolean isPopupFound = false;
		for(String windHndl : winHndlSet) {	
			popup = driver.switchTo().window(windHndl);
			if (popup.getTitle().equals(popupTitle)) {
				isPopupFound = true;
				break;
			}
		}
		if (isPopupFound && action!=null) {
			clickButton("Yes");		// should close the popup window	
			driver.switchTo().window(parentWindowHandle); // Switch back to parent window.
		} else {
			Log.info("No popup windows was found => nothing to do here");
		}

		//return (isPopupFound)?true:false;
	}

	public void switchUrl(String url) {
		initElements();
		if (!driver.getCurrentUrl().toLowerCase().contains(url.toLowerCase())){
			driver.get(url);
		}
	}

	/**
	 * Looks for an element on the active web page
	 * @param elementIdentifier - provide name, id ,XPath or CSS
	 * @param args - provide arguments for xpath or css if needed
	 * @return true if the element was found, or false otherwise.
	 * @throws Exception
	 */
	public static boolean isElementPresent(By by)throws Exception{
		return driver.findElements(by).size()>0?true:false;
	}


	/**
	 * Use this method to type a text in edit box
	 * @param editBoxName
	 * @param textToType
	 */
	public String getEditBoxValue(String editBoxName) throws Exception{
		String xpath = "//input[@name='" + editBoxName + "' or " +
				"@title='" + editBoxName + "' or " +
				"@id='"+ editBoxName + "'] | //textarea[@name='" + editBoxName + "' or @id='"+ editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));

		return driver.findElement(By.xpath(xpath)).getAttribute("value"); 
	}

	/**
	 * Use this method to get the text in an edit box
	 * @param editBoxName
	 * @param textToType
	 */
	public String getEditBoxText(String editBoxName) throws Exception{
		String xpath = "//input[@name='" + editBoxName + "' or " +
				"@title='" + editBoxName + "' or " +
				"@id='"+ editBoxName + "'] | //textarea[@name='" + editBoxName + "' or @id='"+ editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));

		return driver.findElement(By.xpath(xpath)).getText().trim(); 
	}
	/**
	 * Use this method to get the current state of a check box
	 * @param checkboxName
	 * @return
	 */
	public boolean getCheckBoxState(String checkboxName){

		String checkBoxXpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"')]" +
				" | //span[.='" + checkboxName + "']/preceding-sibling::input";

		try  {
			WebDriverWait wait = new WebDriverWait(driver, 8); 
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkBoxXpath)));
		} 
		catch (org.openqa.selenium.TimeoutException ex) {
		}
		return driver.findElement(By.xpath(checkBoxXpath)).isSelected();
	}

	/**
	 * Use this method to get the current state of a radio button
	 * @param radioNameOrID
	 * @return
	 */
	public boolean getRadioButtonState(String radioNameOrID){
		String xpath = "//input[@type='radio'][(@name='" + radioNameOrID + "' or " + "@id='"+ radioNameOrID + "' or "+ 
				"@title[contains(.,'"+ radioNameOrID + "')]" + " or "+ "@title='" + "Select " + "\"" + radioNameOrID + "\"" + "')]";
		waitForElementVisibility(By.xpath(xpath));
		return driver.findElement(By.xpath(xpath)).isSelected();
	}

	public boolean verifyEditBoxText(String editBoxName, String expectedValue) throws Exception{
		String actualValue = getEditBoxText(editBoxName);
		Log.info("Compaing Actual: '"+actualValue+"' with Expected: '"+expectedValue+"'");
		return actualValue.equals(expectedValue);
	}


	public boolean verifyCheckBox(String checkBoxName, String expectedValue) throws Exception{
		boolean actualValue = getCheckBoxState(checkBoxName);
		Log.info("Compaing Actual: '"+actualValue+"' with Expected: '"+expectedValue+"'");
		return actualValue==Boolean.parseBoolean(expectedValue);
	}

	public boolean verifyRadioButton(String radiobuttonName, String expectedValue) throws Exception{
		boolean actualValue = getRadioButtonState(radiobuttonName);
		Log.info("Compaing Actual: '"+actualValue+"' with Expected: '"+expectedValue+"'");
		return actualValue==Boolean.parseBoolean(expectedValue);
	}

	/**
	 * Use this method to highlight selected elements during run time for debug
	 * @param driver
	 * @param element
	 */
	public static void highlightElement(WebDriver driver, WebElement element) {
		if (_debugMode_) {
			if (driver instanceof JavascriptExecutor) {
				((JavascriptExecutor)driver).executeScript("arguments[0].style.backgroundColor = '#FF0000'", element);
			}
		}
	}
	
	
}
