package webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;

public class WebDriverProvider {

	public static WebDriver driverInstance;
	
//	public static WebDriver getDriverInstance()
//	 {
	  if (driverInstance == null)
	  {
		  ChromeOptions chromeOptions = new ChromeOptions();
		  chromeOptions.addArguments(Arrays.asList("--disable-web-security", "--allow-running-insecure-content", "enable-automation", "--no-sandbox", "--disable-extensions", "--dns-prefetch-disable", "--disable-gpu"));

		  driverInstance = new ChromeDriver(chromeOptions);
	  }
	  return driverInstance
	 }
}

