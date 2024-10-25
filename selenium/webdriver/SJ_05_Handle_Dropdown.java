package webdriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SJ_05_Handle_Dropdown {
	WebDriver driver;
	WebDriverWait explicitWait;
	JavascriptExecutor jsExecutor;
	String projectPath = System.getProperty("user.dir");

	@BeforeClass
	public void beforeClass() {
		System.setProperty("webdriver.gecko.driver", projectPath + "\\browserDrivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		explicitWait = new WebDriverWait(driver, 30);
		jsExecutor = (JavascriptExecutor) driver;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@Test
	public void TC_01_Default_Dropdown() {
		driver.get("https://rode.com/en/support/where-to-buy");

		String country = "Vietnam", city = "Da Nang";

		Select select = new Select(driver.findElement(By.xpath("//select[@id='country']")));

		Assert.assertFalse(select.isMultiple());

		select.selectByVisibleText(country);

		Assert.assertEquals(select.getFirstSelectedOption().getText(), country);

		driver.findElement(By.xpath("//input[@id='map_search_query']")).sendKeys(city);

		driver.findElement(By.xpath("//button[text()='Search']")).click();

		List<WebElement> leaderAddress = driver.findElements(By.xpath("//h3[text()='Dealers']/parent::div//div[1]/p"));

		for (WebElement address : leaderAddress) {
			Assert.assertTrue(address.getText().contains(country));
			Assert.assertTrue(address.getText().contains(city));
		}
	}

	@Test
	public void TC_02_Custom_Dropdown() {
		driver.get("https://react.semantic-ui.com/maximize/dropdown-example-selection/");

		selectOptionInCustomDropdown("//i[@class='dropdown icon']", "//div[@class='visible menu transition']/div", "Christian");

		Assert.assertEquals(driver.findElement(By.xpath("//div[@class='divider text']")).getText(), "Christian");
	}

	@Test
	public void TC_03_Editable_Dropdown() {
		driver.get("https://react.semantic-ui.com/maximize/dropdown-example-search-selection/");

		selectOptionInCustomDropdown("//input[@class='search']", "//div[@class='visible menu transition']/div", "Australia");

		Assert.assertEquals(driver.findElement(By.xpath("//div[@class='divider text']")).getText(), "Australia");

		selectOptionInEditableDropdown("//input[@class='search']", "//div[@class='visible menu transition']/div", "Argentina");

		Assert.assertEquals(driver.findElement(By.xpath("//div[@class='divider text']")).getText(), "Argentina");
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	public void sleepForSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void selectOptionInCustomDropdown(String dropdownXPath, String allOptionsXPath, String expectedOption) {
		driver.findElement(By.xpath(dropdownXPath)).click();
		sleepForSeconds(1);
		List<WebElement> allOptions = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allOptionsXPath)));
		for (WebElement option : allOptions) {
			if (option.getText().trim().equals(expectedOption)) {
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", option);
				sleepForSeconds(1);
				option.click();
				sleepForSeconds(1);
				break;
			}
		}
	}

	public void selectOptionInEditableDropdown(String dropdownXPath, String allOptionsXPath, String expectedOption) {
		WebElement editableDropdown = driver.findElement(By.xpath(dropdownXPath));
		editableDropdown.clear();
		editableDropdown.sendKeys(expectedOption);
		sleepForSeconds(1);
		List<WebElement> allOptions = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allOptionsXPath)));
		for (WebElement option : allOptions) {
			if (option.getText().trim().equals(expectedOption)) {
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", option);
				sleepForSeconds(1);
				option.click();
				sleepForSeconds(1);
				break;
			}
		}
	}

}
