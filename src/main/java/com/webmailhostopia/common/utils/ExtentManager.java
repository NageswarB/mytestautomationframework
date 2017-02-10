package com.webmailhostopia.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;
import com.webmailhostopia.selenium.webdriver.AbstractPageObject;

public class ExtentManager extends AbstractPageObject {
	
private static ExtentReports extent;
    
    public synchronized static ExtentReports getReporter(String filePath) {
    	
    	InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        String hostname = ip.getHostName();
        if (extent == null) {
            extent = new ExtentReports(filePath, true,DisplayOrder.NEWEST_FIRST, NetworkMode.ONLINE, Locale.ENGLISH);
            extent
                .addSystemInfo("Host Name", hostname)
                .addSystemInfo("Environment", "QA");
        }
        return extent;
    }
    
    public synchronized static ExtentReports getReporter(){
    	return extent;
    }
        
    public static String takescreen(ITestResult result) throws IOException {

    	Log.info("Inside screenshot capture method");
		File failureImageFile = null;
		if (!result.isSuccess()) {

			File imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String failureImageFileName = result.getMethod().getMethodName()+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime())
					+ ".png";
			failureImageFile = new File(TestCommonResource.getTestResourceDirctoryPath()+"\\screenshots\\"+failureImageFileName);
			FileUtils.copyFile(imageFile, failureImageFile);
		}
		return failureImageFile.getPath();
	}
    
    
}
