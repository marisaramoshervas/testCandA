package testCandA;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPage {
	public WebDriver driver;

	 // Config constants
    private static final int FIND_RETRIES = 2;

    // Locators and Selectors
    private static final String COOKIE_CONSENT_LOCATOR = "onetrust-accept-btn-handler";
    private static final String CSS_SELECTOR_ALLOW_SELECTED_COOKIES_BUTTON = "cookie-consent .k-button:nth-of-type(2)";
 
    // Used constants
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPage.class);

	private static final int DEFAULT_TIMEOUT_IN_SECONDS = 500;
	private static final String WARNING_COOKIE_WINDOW_NOT_SHOWN = "WARNING: Cookie window not shown!";
	private static final String DURATION_TIMEOUT = "PT15S";
	
	private int timeoutSecondsInt;
	private Duration timeOutInSeconds;

	public WebPage() {
		this.driver = null;
		this.timeOutInSeconds= Duration.parse(DURATION_TIMEOUT);
	}
	
	protected WebPage getPage() {
		return new WebPage(driver);
	}

	public WebPage(WebDriver driver) {
		ChromeOptions options = new ChromeOptions();
		this.driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		this.timeoutSecondsInt = DEFAULT_TIMEOUT_IN_SECONDS;
		this.timeOutInSeconds = Duration.parse(DURATION_TIMEOUT);
	}

	public WebPage(WebDriver driver, int timeoutSecondsInt) {
		this.driver = driver;
		this.timeoutSecondsInt = Math.abs(timeoutSecondsInt);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public int getTimeout(int timeoutSeconds) {
		return this.timeoutSecondsInt;
	}

	public void setTimeout(int timeoutSeconds) {
		this.timeoutSecondsInt = Math.abs(timeoutSecondsInt);
	}

	public WebElement find(WebDriver driver, By locator) {
		return new WebDriverWait(driver, timeOutInSeconds)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	/**
	 * The method is necessary created since we need some elements to be
	 * clicked until the click action is performed
	 * @param locator
	 * @return the webelement
	 */
	public WebElement waitUntilClickable(By locator) {
        WebElement element = null;
        if(locator.toString().contains(COOKIE_CONSENT_LOCATOR)) {
            return new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        }
        for(int i = 0; i < FIND_RETRIES; i++) {
            try {
                element = new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
                if (element != null) {
                    break;
                }
            }
            catch(TimeoutException e){
                LOGGER.warn(e.getMessage());
            }
        }
        if(element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }
	
	/**
	 * This is the general method to find elements in the page scenarios using the locator
	 * @param locator
	 * @return the webelement
	 */
	public WebElement find(By locator) {
        WebElement element = null;
        if(locator.toString().contains(COOKIE_CONSENT_LOCATOR)) {
            return new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        }
        for(int i = 0; i < FIND_RETRIES; i++) {
            try {
                element = new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
                if(element != null) {
                    break;
                }
            } catch (TimeoutException e){
                LOGGER.warn(e.getMessage());
            }
        } if(element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }
	
	protected void allowSelectedCookies() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		try {
			WebElement buttonAllowCookies = getPage().find(By.cssSelector(CSS_SELECTOR_ALLOW_SELECTED_COOKIES_BUTTON));
			buttonAllowCookies.click();
			getPage().waitUntilGone(buttonAllowCookies);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		} catch (Exception e) {
		    LOGGER.info(WARNING_COOKIE_WINDOW_NOT_SHOWN);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		}
	}
	
	public void waitUntilGone(WebElement element) {
        new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.stalenessOf(element));
    }
	
	public void waitUntilAllElementsAreVisible(By locator) {
        new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

	/**
	 * This method is created to locate these element which precised to be located until be caught
	 * @param locator
	 * @return
	 */
	public List<WebElement> findPresentElements(By locator) {
	        return new WebDriverWait(driver, timeOutInSeconds)
	                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	    }
}
