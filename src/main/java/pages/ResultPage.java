package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;

public class ResultPage extends BasePage {

	@FindBy(xpath = "//div[@id='search_results_table']//a[contains(@class, 'b-button')]")
	public List<WebElement> resultsOfSearch;

	@FindBy(xpath = "//*[@id='sort_by']/ul/li[3]/a")
	private WebElement filterByMaxRating;

	@FindBy(xpath = "(//div[@class='sr-cta-button-row'])[1]")
	private WebElement bestOffer;

	@FindBy(xpath = "(//div[@class='bui-review-score__badge'])[1]")
	private WebElement fieldRaiting;

	@FindBy(xpath = "//div[@class='filteroptions']/a[contains(@data-id, 'pri')]")
	private List<WebElement> priceCheckboxes;

	@FindBy(xpath = "(//strong/b)[1]")
	private WebElement fieldPrice;

	@FindBy(xpath = ".//*[@id='filter_mealplan']/div[@class='filteroptions']/a[@data-id='mealplan-1']")
	private WebElement filterIncludedBreakfast;

	@FindBy(xpath = ".//*[@id='filter_fc']/div[contains(@class,'filteroptions')]/a[@data-id='fc-2']")
	private WebElement filterFreeCancel;

	@FindBy(xpath = ".//*[@id='filter_fc']/div[contains(@class,'filteroptions')]/a[@data-id='fc-4']")
	private WebElement filterBookingWithoutCreditCard;

	@FindBy(xpath = ".//sup")
	public List<WebElement> offersWithBreakfast;

	@FindBy(xpath = "//a[@data-category='price']")
	private WebElement filterLowestPriceFirst;

	// Price range filters with checkboxes.

	@FindBy(xpath = "//div[@id='filter_price']/div[2]//a[1]")
	private WebElement filterByLowestPriceRangeCheckbox;

	@FindBy(xpath = "//div[@id='filter_price']/div[2]//a[last()]")
	private WebElement filterByHighestPriceRangeCheckbox;

	// Price range filters with slider bar.

	@FindBy(xpath = "(//a[@data-type='min'])[1]")
	private WebElement filterByLowestPriceRangeSlider;

	@FindBy(xpath = "(//a[@data-type='max'])[1]")
	private WebElement filterByHighestPriceRangeSlider;

	public ResultPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	private void waitForUpdate() {
		fluentWait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				List<WebElement> alerts = driver
						.findElements(By.xpath("//div[@class='sr-usp-overlay sr-usp-overlay--wide']"));
				return alerts.isEmpty();
			}
		});
	}

	public void chooseBestOffer() {
		waitForUpdate();
		bestOffer.click();
	}

	public String getRating() {
		driver.switchTo();
		return fieldRaiting.getText();
	}

	public double getPrice() {
		driver.switchTo();
		String priceString = fieldPrice.getText();
		priceString = priceString.replaceAll("[A-Za-z]", "").replaceAll("\\s", "");
		return Double.parseDouble(priceString);
	}

	public void getOffersWithBreakfast() {
		filterIncludedBreakfast.click();
		waitForUpdate();
	}

	public void getFilterByMaxRating() {
		filterByMaxRating.click();
		waitForUpdate();
	}

	public void getFilterByMinPriceRange() {
		if(priceCheckboxes.isEmpty()){
			Actions action = new Actions(driver);
			action.moveToElement(filterByHighestPriceRangeSlider).clickAndHold()
					.moveToElement(filterByLowestPriceRangeSlider).build().perform();
		}
		else {
			filterByLowestPriceRangeCheckbox.click();
		}
		waitForUpdate();
	}

	public void getFilterByMaxPriceRange() {
		if(priceCheckboxes.isEmpty()){
			Actions action = new Actions(driver);
			action.moveToElement(filterByLowestPriceRangeSlider).clickAndHold()
					.moveToElement(filterByHighestPriceRangeSlider).build().perform();
		}
		else {
			filterByHighestPriceRangeCheckbox.click();
		}
		waitForUpdate();
	}

	public void lowestPriceFirst() {
		filterLowestPriceFirst.click();
		waitForUpdate();
	}

	public void getFilterByFreeCancel() {
		filterFreeCancel.click();
	}

	public void getFilterWithoutCreditCard() {
		filterBookingWithoutCreditCard.click();
	}
}
