package pagetest;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderPage;
import pages.ResultPage;
import pages.SearchPage;
import propertyprovider.PropertyProvider;
import webdriver.WebDriverProvider;

public class BookingTest extends BaseTest {

	private LoginPage loginPage;
	private SearchPage searchPage;
	private ResultPage resultPage;
	private OrderPage orderPage;
	private double expectedRating = 9.0;
	private int maxPriceByNightInLowerRange = 110;
	private int minPriceByNightInHigherRange = 450;

	@BeforeClass
	public void beforeClass() {
		setUpDriver();
		WebDriver driver = WebDriverProvider.getDriverInstance();
		loginPage = new LoginPage(driver);
		searchPage = new SearchPage(driver);
		resultPage = new ResultPage(driver);
		orderPage = new OrderPage(driver);
		LOG.info("Starting tests");
	}

	@BeforeMethod
	public void setInformation() {
		cleanCookies();
		currentWindow();
		goHomePage();
		searchPage.inputCity(PropertyProvider.getProperty("city"));
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		searchPage.setDate(tripDuration);
		searchPage.getInformationAboutGuests(1, 2, 0);
		searchPage.pushSearchButton();
	}

	@Test(priority = 0)
	public void availabilityHotelsOnSelectedDates() {
		Assert.assertTrue(resultPage.resultsOfSearch.size() > 0, "There are not hotels on selected dates");
	}

	@Test(priority = 1)
	public void sortByMaxRatingTest() {
		resultPage.getFilterByMaxRating();
		resultPage.chooseBestOffer();
		String ratingString = resultPage.getRating();
		double rating = Double.parseDouble(ratingString.replaceAll(",", "."));
		Assert.assertTrue(rating >= expectedRating, "The rating less than 9");
	}

	@Test(priority = 2)
	public void sortByPriceRangeAndChooseHotelWithLowerPriceTest() {
		resultPage.getFilterByMinPriceRange();
		resultPage.lowestPriceFirst();
		resultPage.chooseBestOffer();
		double price = resultPage.getPrice();
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		Assert.assertTrue(price / tripDuration <= maxPriceByNightInLowerRange, "The price does not match the range");
	}

	@Test(priority = 3)
	public void sortByPriceRangeAndChooseHotelWithHighestPriceTest() {
		resultPage.getFilterByMaxPriceRange();
		resultPage.lowestPriceFirst();
		resultPage.chooseBestOffer();
		int tripDuration = Integer.parseInt(PropertyProvider.getProperty("tripDuration"));
		double price = resultPage.getPrice();
		Assert.assertTrue(price / tripDuration >= minPriceByNightInHigherRange, "The price does not match the range");
	}

	@Test(priority = 4)
	public void sortByRatingAndIncudedBreakfast() {
		resultPage.getFilterByMaxRating();
		resultPage.getOffersWithBreakfast();
		Assert.assertFalse(resultPage.offersWithBreakfast.isEmpty(), "No offers with breakfast");
	}

	@Test(priority = 5)
	public void verifyInvalidCardAlertTest() {
		resultPage.getFilterByMaxRating();
		resultPage.chooseBestOffer();
		orderPage.bookingBestOffer();
		String appeal = PropertyProvider.getProperty("appeal");
		String lastName = PropertyProvider.getProperty("lastName");
		String login = PropertyProvider.getProperty("login");
		orderPage.fillFirstPartFormPersonalData(appeal, lastName, login);
		String country = PropertyProvider.getProperty("country");
		String phoneNumber = PropertyProvider.getProperty("phoneNumber");
		String address = PropertyProvider.getProperty("address");
		String city = PropertyProvider.getProperty("departureCity");
		String zip = PropertyProvider.getProperty("zip");
		orderPage.fillSecondPartFormPersonalData(address, city, zip, country, phoneNumber);
		String ccType = PropertyProvider.getProperty("ccType");
		String cardNumber = PropertyProvider.getProperty("cardNumber");
		String expMonth = PropertyProvider.getProperty("expMonth");
		String expYear = PropertyProvider.getProperty("expYear");
		String cvcCode = PropertyProvider.getProperty("cvcCode");
		orderPage.fillFormCardData(lastName, ccType, cardNumber, expMonth, expYear, cvcCode);
		orderPage.completeBooking();
		Assert.assertTrue(orderPage.verifyInvalidCardAlert(), "Invalid card accepted");
	}

	@Test(priority = 6)
	public void successfulBookingTest() {
		resultPage.getFilterByMaxRating();
		resultPage.getFilterWithoutCreditCard();
		resultPage.getFilterByFreeCancel();
		resultPage.chooseBestOffer();
		orderPage.bookingBestOffer();
		String appeal = PropertyProvider.getProperty("appeal");
		String lastName = PropertyProvider.getProperty("lastName");
		String login = PropertyProvider.getProperty("login");
		orderPage.fillFirstPartFormPersonalData(appeal, lastName, login);
		String country = PropertyProvider.getProperty("country");
		String phoneNumber = PropertyProvider.getProperty("phoneNumber");
		String address = PropertyProvider.getProperty("address");
		String city = PropertyProvider.getProperty("departureCity");
		String zip = PropertyProvider.getProperty("zip");
		orderPage.fillSecondPartFormPersonalData(address, city, zip, country, phoneNumber);
		orderPage.completeBooking();
		Assert.assertTrue(orderPage.confirmationPresents(), "No booking confirmation");
	}

	@AfterClass
	public void afterClass() {
		tearDownDriver();
	}
}
