package com.webmailhostopia.tests;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.webmailhostopia.common.utils.ExtentManager;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.testimpl.LanguageTest;
import com.webmailhostopia.testimpl.MailHomePage;
import com.webmailhostopia.testimpl.MailInboxPage;
import com.webmailhostopia.testimpl.MailLoginPage;

public class WebMailHostpiaRegressionTests {

	public static MailLoginPage login;
	public static MailHomePage homepage;
	public static MailInboxPage inboxPage;
	private ExtentReports extent;
	private ExtentTest test;
	private LanguageTest ltest;

	@BeforeClass
	public void setup(){
		extent = ExtentManager.getReporter();
		login = new MailLoginPage();
		/*homepage = new MailHomePage();
		inboxPage = new MailInboxPage();*/
		ltest=new LanguageTest();
	}

	/*@Test(priority=0,description="This method is used to verify the forgot password feature",
			groups="Regression",testName = "Verify Forgot Password Link")
	public void verifyForgotPasswordLink() throws Exception {
		boolean expected = true;
		Log.info("Inside verifyForgotPasswordLink method");
		Assert.assertEquals(login.verifyForgotPasswordLink(), expected);
	}*/

	@Test(priority=1,description="This is webmail login test with valid username and password",
			groups="Regression",testName = "WebMail Valid Login Test")
	@Parameters({ "sUserName", "sPassword" })
	public void webMailLoginTest(String sUserName,String sPassword) throws Exception {

		Log.info("Inside webmail login test method");
		boolean expected = true;
		int failCounter = 0;
		/*
		String expectedWarnMessage1 = "Please provide your username.";
		String expectedWarnMessage2 = "Please provide your password.";

		Log.info("Inside webmail login test method");

		String actualWarnMessage1 = login.noUserNameNoPassword("", "");

		if( actualWarnMessage1 != null){
			Assert.assertEquals(actualWarnMessage1, expectedWarnMessage1);
		}else{
			failCounter++;
			Log.warn("");
		}

		String actualWarnMessage2 = login.validUserNameWithNoPassword(sUserName, "");
		if( actualWarnMessage2 != null){
			Assert.assertEquals(actualWarnMessage2, expectedWarnMessage2);
		}else{
			failCounter++;
			Log.warn("");
		}

		String actualWarnMessage3 = login.noUserNameWithValidPassword("", sPassword);
		if( actualWarnMessage3 != null){
			Assert.assertEquals(actualWarnMessage3, expectedWarnMessage1);
		}else{
			failCounter++;
			Log.warn("");
		}*/

		//Assert.assertEquals(failCounter, 0);
		Assert.assertEquals(login.loginToWebMail(sUserName,sPassword), expected);

	}

	/*@Test(priority=0,description="This method is used to verify main activity page",
			groups="Regression",testName = "Verify Main Activity Page")
	@Parameters({ "sUserName", "sPassword" })
	public void verifyEmailLogin(String sUserName, String sPassword) throws Exception {
		boolean expected = true;
		Log.info("Inside verifyEmailLogin method");
		//Assert.assertEquals(homepage.verifyMainActivityPage(), expected);
		Assert.assertEquals(login.loginToWebMail(sUserName,sPassword), expected);
	}

	@Test(priority=1,description="This method is used to send e-mail",
			groups="Regression",testName = "Verify main send functionality")
	@Parameters({ "mailTo", "subject", "mailBody" })
	public void verifyEmailSendFunctionality(String mailTo, String subject, String mailBody) throws Exception {
		boolean expected = true;
		Log.info("Inside verifyEmailSendFunctionality method");
		Assert.assertEquals(homepage.sendEmail(mailTo, subject, mailBody), expected);
	}

	@Test(priority=2,description="This method is used to verify the GUI options in Main Home page",
			groups="Regression",testName = "Verify Main Page Functionality functionality")
	@Parameters({ "userDropDownOptions", " " })
	public void verifyOptionsAvailableInMainPage(String userDropDownOptions) throws Exception {
		boolean expected = true;
		Log.info("Inside verifyEmailSendFunctionality method");
		Assert.assertEquals(homepage.verifyOptionsAvailableInMainPage(userDropDownOptions), expected);
	}

	@Test(priority=2,description="This method is used to verify tour option functionality",
			groups="Regression",testName = "Verify main send functionality")
	public void verifyTourOptionFunctionality() throws Exception {
		boolean expected = true;
		Log.info("Inside verifyTourOptionFunctionality method");
		Assert.assertEquals(homepage.verifyTourOptionFunctionality(), expected);
	}*/

	/*@Test(priority=2,description="This method is used to verify compose and send email functionality",
			groups="Regression",testName = "Verify mail send functionality")
	@Parameters({ "toMail"})
	public void composeAndSendMail(String toMail) throws Exception {
		boolean expected = true;
		Log.info("Inside composeAndSendMail method");
		Assert.assertEquals(inboxPage.composeAndSendMail(toMail), expected);
	}
	 */

	@Test(priority=2)
	public void labelsLanguageTestInPreferenceTabForEnglish() throws Exception {
		boolean expected = true;
		Log.info("Inside labelsLanguageTestInPreferenceTab method");
		Assert.assertEquals(LanguageTest.labelsLanguageTestInPreferenceTabForEnglish(), expected);
	}
	
	@Test(priority=3)
	public void labelsLanguageTestInPreferenceTabForNonEnglish() throws Exception {
		boolean expected = true;
		Log.info("Inside labelsLanguageTestInPreferenceTab method");
		Assert.assertEquals(LanguageTest.labelsLanguageTestInPreferenceTabForNonEnglish(), expected);
	}
	
	
	@AfterClass
	public void tearDown(){
		login.quit();

	}
}