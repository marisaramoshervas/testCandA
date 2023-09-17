package testCandA;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Test class to ensure the quality and results of a login in a certain page
 * with given requisites.
 * 
 * @author marisa.ramos.hervas
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CandAPageTest extends WebPage {

	private static final String LETTER_A_ALL = "a";
	private static final String TEST_CA_LAST_NAME = "TestCALastName";
	private static final String TEST_CA_FIRST_NAME = "TestCAFirstName";
	private static final int UNIQUE_ELEMENT_FOUND = 1;

	/** WebElements Locators Constants**/
	
	private static final String ID_ACCEPT_COOKIES = "onetrust-accept-btn-handler";
	private static final String ID_INPUT_EMAIL_ADDRESS = "emailAddress";
	private static final String ID_INPUT_FIRST_NAME = "firstName";
	private static final String ID_INPUT_LAST_NAME = "lastName";
	private static final String ID_INPUT_PASSWORD = "password";
	private static final String ID_PASSWORD_LOGIN_INPUT = "myaccount_login_password";
	private static final String ID_USERNAME_LOGIN_INPUT = "myaccount_login_email";
	
	private static final String XPATH_BUTTON_MODIFY_EMAIL = "//button[@data-qa='ReadOnlyEmailFieldChangeLink']";
	private static final String XPATH_RADIO_BUTTON_TITLE_OTHER = "//label[@data-qa='RadioButtonOTHER']//button";
	private static final String XPATH_REGISTER_BUTTON = "//a[@data-qa='TextLink']//span[contains(text(),'Reg')]";
	private static final String XPATH_SUBMIT_NEXT_BUTTON = "//button[@data-qa='LoadingButton']";
	private static final String XPATH_SUCCESFUL_MESSAGE_REGISTER = "//body/div/div/div[@data-qa='Headline']";
	
    
    private static final String CSS_BUTTON_PERSONAL_AREA_ACCESS = "button[data-qa='HeaderAccountButton']";
    private static final String CSS_BUTTON_WISH_LIST = "button[data-qa='HeaderWishlistButton']";
    private static final String CSS_ICON_PASSWORD_VISIBILITY = "svg[data-qa='IconEyeInactive']";
    private static final String CSS_INLINE_NOTIFICATION = "div[data-qa='InlineNotification']";
    private static final String CSS_INVALID_FORMAT_EMAIL = "div[style='opacity: 1; height: auto;'] span[data-qa='Copy']";
    private static final String CSS_WARNING_EMPTY_MESSAGE =" div >  form >  div > div:nth-child(2) > p > svg > title";
	
	/** WebElements Locators By**/
    
    private By allowCookies() {return By.id(ID_ACCEPT_COOKIES);}
    private By clickModifyEmailRegister() {return By.xpath(XPATH_BUTTON_MODIFY_EMAIL);}
    private By clickPersonalAreaButton() {return By.cssSelector(CSS_BUTTON_PERSONAL_AREA_ACCESS);}
    private By clickTitleOtherCheck() {return By.xpath(XPATH_RADIO_BUTTON_TITLE_OTHER);}
    private By clickWishListButton() {return By.cssSelector(CSS_BUTTON_WISH_LIST);}
    private By emailUser(){return By.id(ID_INPUT_EMAIL_ADDRESS);}
    private By getInlineNotification() {return By.cssSelector(CSS_INLINE_NOTIFICATION);}
    private By getInvalidEmailFormat() {return By.cssSelector(CSS_INVALID_FORMAT_EMAIL);}
    private By getSuccesfulMessage() {return By.xpath(XPATH_SUCCESFUL_MESSAGE_REGISTER);}
    private By iconPasswordVisible() {return By.cssSelector(CSS_ICON_PASSWORD_VISIBILITY);}
    private By inputFirstNameRegister() {return By.id(ID_INPUT_FIRST_NAME);}
    private By inputLastNameRegister() {return By.id(ID_INPUT_LAST_NAME);}
    private By inputUserEmail() {return By.id(ID_USERNAME_LOGIN_INPUT);}
    private By inputUserPassword() {return By.id(ID_PASSWORD_LOGIN_INPUT);}
    private By inputPasswordRegister() {return By.id(ID_INPUT_PASSWORD);}
    private By modifyEmailRegister() {return By.id(ID_INPUT_EMAIL_ADDRESS);}
    private By registerButton() {return By.xpath(XPATH_REGISTER_BUTTON);}
    private By submitNextButton() {return By.xpath(XPATH_SUBMIT_NEXT_BUTTON);}
    
	/** Private Constants to be used in the test */
    private static final String CANDA_URL_PAGE = "https://www.c-and-a.com/es/es/shop";
    private static final String EMPTY_STRING = "";
    private static final String PASSWORD_INVALID = "pA$$w0Rd";
    private static final String PASSWORD_VALID = "p4$$w0Rd";
    private static final String RESOURCES_FOLDER = "src/main/resources";
    private static final String USER_EMAIL_ES_TEMP_REGISTER = "testcandamrh@gmail.es";
    private static final String USER_EMAIL_VALID = "testcandam13rh@gmail.com";
    private static final String USER_WRONG_EMAIL = "wrong13";
	
    
    @Before
    public  void setUp() {
        System.setProperty("webdriver.chrome.driver", RESOURCES_FOLDER + "/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loadWeb(CANDA_URL_PAGE);
    	waitUntilClickable(By.id(ID_ACCEPT_COOKIES));
    	find(driver,allowCookies()).click();
    }

    @Test
	public void checkUserNameInvalidFormatLogin() {
		find(driver, clickPersonalAreaButton()).click();
		setFieldByAction(inputUserEmail(), USER_WRONG_EMAIL);
		setFieldByAction(inputUserPassword(),PASSWORD_VALID);
		clickNextStep();
		assertTrue(find(driver, getInvalidEmailFormat()) != null);
	}
    
    @Test
    public void checkWrongPasswordInvalidLogin() {
    	find(driver, clickPersonalAreaButton()).click();
    	setFieldByAction(inputUserEmail(), USER_EMAIL_VALID);
		setFieldByAction(inputUserPassword(), PASSWORD_INVALID);
		clickNextStep();
		assertTrue(find(driver, getInlineNotification()) != null);
    }
    
    @Test
    public void checkEmptyPasswordLogin() {
    	find(driver, clickPersonalAreaButton()).click();
    	setFieldByAction(inputUserEmail(), USER_EMAIL_VALID);
		setFieldByAction(inputUserPassword(), EMPTY_STRING);
		clickNextStep();
		List<WebElement> listTitles = findPresentElements(By.cssSelector(CSS_WARNING_EMPTY_MESSAGE));
		assertTrue(listTitles.size() == UNIQUE_ELEMENT_FOUND);
    }
    
    @Test
    public void verifyDataUserRegister() {
    	find(driver,clickPersonalAreaButton()).click();
    	clickRegisterButton();
    	setUserEmail();
    	waitUntilClickable(By.xpath(XPATH_SUBMIT_NEXT_BUTTON));
    	clickNextStep();
    	setDataRegister();
    	MatcherAssert.assertThat(find(driver,getSuccesfulMessage()).getText(), StringContains.containsString(TEST_CA_FIRST_NAME));
    }
        
	@Test
    public void verifyLoginWithUserAndPassword() throws InterruptedException {
        find(driver,clickPersonalAreaButton()).click();
        setFieldByAction(inputUserEmail(),USER_EMAIL_VALID);
        setFieldByAction(inputUserPassword(),PASSWORD_VALID);
        find(driver,submitNextButton()).click();
        Thread.sleep(1000);
        find(driver,clickWishListButton()).click();
        Thread.sleep(1000);
        find(driver,clickPersonalAreaButton()).click();
        waitUntilAllElementsAreVisible(By.xpath(XPATH_SUCCESFUL_MESSAGE_REGISTER));
        MatcherAssert.assertThat(find(driver,getSuccesfulMessage()).getText(), StringContains.containsString(TEST_CA_FIRST_NAME));
    }
    
	/**
	 * Method used to fill and submit the data in the register
	 */
    private void setDataRegister() {
    	
		find(driver,clickTitleOtherCheck()).click(); //Check the option Other in Greeting
		modifyEmailInRegister();
    	setFieldByAction(inputFirstNameRegister(),TEST_CA_FIRST_NAME);
    	setFieldByAction(inputLastNameRegister(),TEST_CA_LAST_NAME);
    	setFieldByAction(inputPasswordRegister(),PASSWORD_VALID);
    	find(driver,iconPasswordVisible()).click();
    	find(driver,submitNextButton()).click();
	}
    
    /**
     * This method is used to unify those actions which are setting values by
     * similar actions
     * @param by
     * @param string
     */
    private void setFieldByAction(By by, String string) {
    	Actions actions = new Actions(driver);
    	WebElement password = find(driver,by);
    	actions.click(password).perform();
    	password.sendKeys(string);
    }
    
    /**
     * This method is used in the moment of the registeer to verify the correct behaviour
     * of changing the email in the form after the user set at the initial point.
     */
	private void modifyEmailInRegister() {
		find(driver,clickModifyEmailRegister()).click(); //There's a temp email, prepared to be changed and test this functionality
		WebElement modifiedEmail = find(driver,modifyEmailRegister());
		modifiedEmail.sendKeys(Keys.CONTROL,LETTER_A_ALL,Keys.DELETE);
		Actions actions = new Actions(driver);
		actions.click(modifiedEmail).perform();
		modifiedEmail.sendKeys(USER_EMAIL_VALID);
		find(driver,submitNextButton()).click(); //Submit the new introduced email
	}
	
	/**
	 * Used method to click the submit button in several times where the same
	 * locator is used to submit the action
	 */
	private void clickNextStep() {
		find(driver,submitNextButton()).click();
	}

	/**
	 * Method used to set the email when is being modified in the register
	 * 
	 */
	private void setUserEmail() {
		Actions actions = new Actions(driver);
		WebElement emailNewUser = find(driver,emailUser());
		actions.click(emailNewUser).perform();
		emailNewUser.sendKeys(USER_EMAIL_ES_TEMP_REGISTER);
	}

	/**
	 * Button used to submit the registering in the page
	 */
	private void clickRegisterButton() {
		waitUntilClickable(By.xpath(XPATH_REGISTER_BUTTON));
		find(driver,registerButton()).click();
	}

	/**
     * Method used to open the webPage with the given url
     * 
     * @param url
     */
    private void loadWeb(String url) {
        driver.get(url);
    }


    @After
    public void tearDown() {
        driver.quit();
    }
}
