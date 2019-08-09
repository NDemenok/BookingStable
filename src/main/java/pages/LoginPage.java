package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
	
	@FindBy(xpath = ".//div[@class='sign_in_wrapper']/span[contains(text(),'Sign in')]")
	private WebElement inputLink;
	
	@FindBy(xpath = "//*[@id='username']")
	private WebElement userNameField;
	
	@FindBy(xpath = "//*[@id='root']/descendant::button[@type='submit']")
	private WebElement buttonContinue;
	
	@FindBy(xpath = "//*[@id='password']")
	private WebElement passwordField;
	
	@FindBy(xpath = "//*[@id='current_account']/a")
	private WebElement currentAccountLink;	

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}	
	
	public void enterLoginAndPass(String email, String password) {
		inputLink.click();
		userNameField.clear();
		userNameField.sendKeys(email);
		buttonContinue.click();
		fluentWait.until(ExpectedConditions.visibilityOf(passwordField));
		passwordField.clear();
		passwordField.sendKeys(password);
		buttonContinue.click();
	}
	
	public boolean currentAccountLinkPresents() {
		return currentAccountLink.isDisplayed();
	}
}
