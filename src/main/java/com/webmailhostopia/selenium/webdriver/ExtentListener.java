package com.webmailhostopia.selenium.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.IResultListener;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.webmailhostopia.common.utils.ExtentManager;
import com.webmailhostopia.common.utils.ExtentTestManager;
import com.webmailhostopia.common.utils.Log;

public class ExtentListener extends TestListenerAdapter {

	private ExtentReports extent;
	private ExtentTest test;

	@Override
	public void onStart(ITestContext testContext) {

		String filePath = testContext.getOutputDirectory() + File.separator + "WebMailTestResultsReport.html";
		extent = ExtentManager.getReporter(filePath);
	}

	@Override
	public void onTestStart(ITestResult result) {

		test = ExtentTestManager.startTest(result.getName());
		test.log(LogStatus.INFO, "Log from threadId: " + Thread.currentThread().getId());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		String message = "Test " + LogStatus.PASS.toString().toLowerCase() + "ed";

		if (result.getThrowable() != null)
			message = result.getThrowable().getMessage();

		test.setStartedTime(getTime(result.getStartMillis()));
		test.setEndedTime(getTime(result.getEndMillis()));
		test.assignCategory(result.getMethod().getGroups());
		test.log(LogStatus.PASS, message);
		extent.endTest(test);
		extent.flush();
	}

	@Override
	public void onTestFailure(ITestResult result) {

		try {
			Log.error("Test failed");
			Log.error("Inside onTestFailure Method");
			test.setStartedTime(getTime(result.getStartMillis()));
			test.setEndedTime(getTime(result.getEndMillis()));
			test.assignCategory(result.getMethod().getGroups());
			test.log(LogStatus.FAIL, result.getThrowable());
			test.log(LogStatus.FAIL, test.addScreenCapture(ExtentManager.takescreen(result)));
			extent.endTest(test);
			extent.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTestSkipped(ITestResult tr) {

	}

	@Override
	public void onFinish(ITestContext testContext) {

		Log.info("Flushing and closing the extent report object!!!");
		extent.flush();
		extent.close();
	}


	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		// TODO Auto-generated method stub

	}
	
	private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();        
    }
}
