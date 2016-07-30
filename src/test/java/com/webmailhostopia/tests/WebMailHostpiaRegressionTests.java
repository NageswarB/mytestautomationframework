package com.webmailhostopia.tests;

import java.io.File;
import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.webmailhostopia.common.utils.ExtentManager;
import com.webmailhostopia.common.utils.ExtentTestManager;
import com.webmailhostopia.common.utils.Log;
import com.webmailhostopia.testimpl.MailLoginPage;

public class WebMailHostpiaRegressionTests {

	public static MailLoginPage login;
	private ExtentReports extent;
	private ExtentTest test;

	@BeforeClass
	public void setup(){
		extent = ExtentManager.getReporter();
		login = new MailLoginPage();
	}

	@Test(priority=0,description="This is webmail login test with valid username and password",
			groups="Regression",testName = "WebMail Valid Login Test")
	@Parameters({ "sUserName", "sPassword" })
	public void webMailLoginTest(String sUserName,String sPassword) throws Exception {
		
		Log.info("Inside webmail login test method");
		boolean expected = true;
		Assert.assertEquals(login.loginToWebMail(sUserName,sPassword), expected);

	}

/*	@Test(priority=1,description="This is webmail login test with invalid username and password",
			groups="Regression",testName = "WebMail Invalid Login Test")
	@Parameters({ "sInvalidUserName", "sInvalidPassword" })
	public void webMailLoginTestInvalid(String sUserName,String sPassword) throws Exception {

		Log.info("Inside webmail login test method");
		boolean expected = true;
		Assert.assertEquals(login.loginToWebMail(sUserName,sPassword), expected);

	}*/

	@Test(priority=2,description="This is webmail login test with no username and password",
			groups="Regression",testName = "WebMail Null Login Test")
	@Parameters({ "sUserName", "sPassword" })
	public void webMailLoginNullTest(String sUserName,String sPassword) throws Exception {
		
		boolean expected = true;
		Assert.assertEquals(false, expected);

	}

	@AfterClass
	public void tearDown(){
		login.quit();
	}
}
