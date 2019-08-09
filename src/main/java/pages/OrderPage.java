package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrderPage extends BasePage {

	@FindBy(xpath = "(//button[@id='hp_book_now_button'])[1]")
	private WebElement reserveButton;
		
	@FindBy(xpath = "(//td//select)[1]")
	private WebElement roomsSelect;

	@FindBy(xpath = ".//button[contains(@class,'reservation-button')]")
	private WebElement bookingButton;
	
	@FindBy(xpath = "//input[@id='lastname']")
	private WebElement lastNameInput;

	@FindBy(xpath = "//input[@id='email']")
	private WebElement emailInput;

	@FindBy(xpath = "//input[@id='email_confirm']")
	private WebElement confirmEmailInput;

	@FindBy(xpath = "//button[@name='book']")
	private WebElement finalDetailsButton;

	@FindBy(xpath = "//input[@id='address1']")
	private WebElement addressInput;

	@FindBy(xpath = "//input[@id='city']")
	private WebElement cityInput;

	@FindBy(xpath = "//input[@id='zip']")
	private WebElement zipCode;

	@FindBy(xpath = "//*[@id='cc1']")
	private WebElement countrySelect;
	
	@FindBy(xpath = "//*[@id='booker_title']")
	private WebElement bookerTitleSelect;
	
	@FindBy(xpath = "//input[@id='phone']")
	private WebElement phoneInput;

	@FindBy(xpath = "//*[@id='cc_name']")
	private WebElement ccName;
	
	@FindBy(xpath = "//select[@id='cc_type']")
	private WebElement ccTypeSelect;

	@FindBy(xpath = "//input[@id='cc_number']")
	private WebElement ccNumberInput;

	@FindBy(xpath = "//select[@id='cc_month']")
	private WebElement expMonthSelect;

	@FindBy(xpath = "//select[@id='ccYear']")
	private WebElement expYearSelect;

	@FindBy(xpath = "//input[@id='cc_cvc']")
	private List<WebElement> cvcCodeInputs;

	@FindBy(xpath = "//button[contains(@class, 'bp-overview-buttons-submit')]")
	private WebElement compliteBookingButton;
	
	@FindBy(xpath = "//div[@class='mltt__legs--current mltt__legs js__mltt__scroll_here']")
	private WebElement bookingConfirmation;

	@FindBy(xpath = "//label[@class='bp_form__field__label']")
	private List<WebElement> labelsForInputs;
	
	public OrderPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	private WebElement waitForAlert() {
		WebElement waitingAlert = fluentWait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//div[@class='bui-alert__description']"));
			}
		});
		return waitingAlert;
	}

	public void bookingBestOffer(){
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabs.size() - 1));
		reserveButton.click();
		new Select(roomsSelect).selectByValue("1");
		new WebDriverWait(driver,10);
		bookingButton.submit();
	}

	private void fillInput(WebElement element, String input){
		element.clear();
		element.sendKeys(input);
	}

	public void fillFirstPartFormPersonalData(String appeal, String lastName, String login) {
		new Select(bookerTitleSelect).selectByValue(appeal);
		fillInput(lastNameInput, lastName);
		fillInput(emailInput, login);
		fillInput(confirmEmailInput, login);
		finalDetailsButton.click();
	}

	public void fillSecondPartFormPersonalData(String address, String city, String zip,
						   String country, String phoneNumber) {

		if(labelsForInputs.size()>=10){
			fillInput(addressInput, address);
			fillInput(cityInput,city);
			fillInput(zipCode,zip);
			new Select(countrySelect).selectByVisibleText(country);
			fillInput(phoneInput, phoneNumber);
		}
		else {
			new Select(countrySelect).selectByVisibleText(country);
			fillInput(phoneInput, phoneNumber);
		}
	}
	
	public void fillFormCardData(String lastName, String ccType, String cardNumber,
			String expMonth, String expYear, String cvcCode) {
		if(ccTypeSelect.isDisplayed()) {
			new Select(ccTypeSelect).selectByVisibleText(ccType);
			fillInput(ccNumberInput, cardNumber);
			new Select(expMonthSelect).selectByValue(expMonth);
			new Select(expYearSelect).selectByValue(expYear);
			if(cvcCodeInputs.size()>0){
                            fillInput(cvcCodeInputs.get(0), cvcCode);
			}
		}
		else{
			fillInput(ccName, lastName);
			new Select(ccTypeSelect).selectByVisibleText(ccType);
			fillInput(ccNumberInput, cardNumber);
			new Select(expMonthSelect).selectByValue(expMonth);
			new Select(expYearSelect).selectByValue(expYear);
			if(cvcCodeInputs.size()>0) {
				fillInput(cvcCodeInputs.get(0), cvcCode);
			}
		}
	}

	public boolean verifyInvalidCardAlert() {
		return waitForAlert().isDisplayed();
	}
	
	public void completeBooking() {
		compliteBookingButton.submit();
	}

	public void confirmBooking(String country, String phoneNumber){
		new Select(countrySelect).selectByVisibleText(country);
		fillInput(phoneInput, phoneNumber);
		completeBooking();
	}

	public boolean confirmationPresents() {
		fluentWait.until(ExpectedConditions.visibilityOf(bookingConfirmation));
		return bookingConfirmation.isDisplayed();
	}
}
