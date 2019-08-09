package pagetest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import propertyprovider.PropertyProvider;
import webdriver.WebDriverProvider;

import java.io.File;
import java.util.ArrayList;

public class BaseTest {

	protected static final Logger LOG = Logger.getLogger(BookingTest.class);

	protected void setUpDriver() {
		WebDriverProvider.getDriverInstance().manage().window().maximize();
		LOG.debug("Windows maximize");
	}

	protected void goHomePage() {
		String url = PropertyProvider.getProperty("url");
		WebDriverProvider.getDriverInstance().navigate().to(url);
		LOG.debug("Navigated to " + url);
	}

	protected void currentWindow(){
		ArrayList<String> tabs = new ArrayList<>(WebDriverProvider.getDriverInstance().getWindowHandles());
		String currentlyPage = tabs.get(0);
		WebDriverProvider.getDriverInstance().switchTo().window(currentlyPage);
	}

	protected void cleanCookies() {
		WebDriverProvider.getDriverInstance().manage().deleteAllCookies();
		LOG.debug("Cleaned cookies");
	}

	protected void tearDownDriver() {
		WebDriverProvider.getDriverInstance().quit();
		LOG.debug("Killed the driver");
	}

	@AfterMethod
	public void takeScreenshotOnFailure(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			try {
				TakesScreenshot screenshot = (TakesScreenshot) WebDriverProvider.getDriverInstance();
				File source = screenshot.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(source, new File("./Screenshots/" + "failed_test.png"));
				LOG.info("Screenshot has been taken");
			} catch (Exception ex) {
				LOG.error("Throwing exception while taking screenshot" + ex.getMessage());
			}
		}
	}
}
