package stepdefinitions;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.en.*;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.UUID;

public class RegistrationSteps {


    WebDriver driver;

    WebDriverWait wait;

    String uniqueEmail;


    private String generateUniqueEmail() {
        String uniquePart = UUID.randomUUID().toString().substring(0, 8);
        return "Lionel_" + uniquePart + "@mailnesia.com";
    }


    private WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    private WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }


    private void fillFieldById(String id, String value) {
        WebElement field = waitUntilVisible(By.id(id));
        field.clear();
        field.sendKeys(value);
    }


    @Before
    public void setUp() {

        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @After
    public void tearDown() {
        if (driver != null) driver.quit();
    }


    @Given("the user is on the registration page")
    public void the_user_is_on_the_registration_page() {

        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");


        WebElement firstNameField = waitUntilVisible(By.id("member_firstname"));


        assertTrue(driver.getCurrentUrl().contains("NewSupporterAccount"), "Expected to be on the registration page, but was on: " + driver.getCurrentUrl());


        assertNotNull(firstNameField, "The registration form did not load correctly.");
    }


    @When("the user enters {string} as first name")
    public void the_user_enters_as_first_name(String firstName) {
        fillFieldById("member_firstname", firstName);
    }


    @When("the user enters {string} as last name")
    public void the_user_enters_as_last_name(String lastName) {
        fillFieldById("member_lastname", lastName);
    }


    @When("the user leaves last name field empty")
    public void the_user_leaves_last_name_field_empty() {
        fillFieldById("member_lastname", "");
    }


    @When("the user enters a unique Mailnesia email")
    public void the_user_enters_a_unique_mailnesia_email() {
        uniqueEmail = generateUniqueEmail();
        fillFieldById("member_emailaddress", uniqueEmail);
    }


    @When("the user confirms the email")
    public void the_user_confirms_the_email() {
        fillFieldById("member_confirmemailaddress", uniqueEmail);
    }


    @When("the user enters {string} as date of birth")
    public void the_user_enters_as_date_of_birth(String dob) {
        WebElement dobField = waitUntilVisible(By.id("dp"));
        dobField.sendKeys(dob);
        dobField.sendKeys(Keys.RETURN);
    }


    @When("the user enters {string} as password")
    public void the_user_enters_as_password(String password) {
        fillFieldById("signupunlicenced_password", password);
    }


    @When("the user confirms {string} as password")
    public void the_user_confirms_as_password(String password) {
        fillFieldById("signupunlicenced_confirmpassword", password);
    }


    @When("the user selects {string} as basketball role")
    public void the_user_selects_as_basketball_role(String role) {
        waitUntilClickable(By.cssSelector("label[for='signup_basketballrole_18']")).click();
    }


    @When("the user agrees to the Code of Ethics and Conduct")
    public void the_user_agrees_to_the_code_of_ethics_and_conduct() {
        WebElement ethicsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='fanmembersignup_agreetocodeofethicsandconduct']")));
        ethicsCheckboxLabel.click();
    }


    @When("the user confirms being over {int} or a guardian")
    public void the_user_confirms_being_over_or_a_guardian(Integer age) {
        WebElement ageCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("sign_up_26")));
        if (!ageCheckbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ageCheckbox);
        }
    }


    @When("the user submits the form")
    public void the_user_submits_the_form() {
        WebElement confirmAndJoinButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("join")));
        try {
            WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(confirmAndJoinButton));
            clickableButton.click();
        } catch (TimeoutException e) {
            System.out.println("Knappen är inte klickbar eftersom formuläret inte är ifyllt korrekt.");
        }
    }


    @Then("the account should be created successfully")
    public void the_account_should_be_created_successfully() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("SignUpConfirmation"), "Expected to be on sign-up confirmation page, but was on: " + currentUrl);
    }


    @Then("the registration should not be completed")
    public void the_registration_should_not_be_completed() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentUrl = driver.getCurrentUrl();

        assertFalse(currentUrl.contains("SignUpConfirmation"),
                "Test misslyckades: användaren registrerades trots att formuläret inte var korrekt ifyllt.");
    }


    @Then("an error message should be displayed for missing last name")
    public void an_error_message_should_be_displayed_for_missing_last_name() {
        WebElement error = waitUntilVisible(By.id("member_lastname-error"));
        assertTrue(error.getText().contains("Last Name is required"), "Expected error message for missing last name, but got: " + error.getText());
    }


    @Then("an error message should be displayed for password mismatch")
    public void an_error_message_should_be_displayed_for_password_mismatch() {
        WebElement error = waitUntilVisible(By.id("signupunlicenced_confirmpassword-error"));
        assertTrue(error.getText().toLowerCase().contains("did not match"), "Password mismatch error was not displayed correctly");
    }


    @Then("an error message should be displayed for terms not accepted")
    public void an_error_message_should_be_displayed_for_terms_not_accepted() {
        WebElement error = waitUntilVisible(By.cssSelector("span[for='TermsAccept']"));
        assertTrue(error.getText().contains("You must confirm that you have read and accepted our Terms and Conditions"),
                "Expected error message for terms acceptance, but got: " + error.getText());
    }


    @Then("an error message should be displayed for age not confirmed")
    public void an_error_message_should_be_displayed_for_age_not_confirmed() {
        WebElement error = waitUntilVisible(By.cssSelector("span[for='AgeAccept']"));
        assertTrue(error.getText().contains("You must confirm that you are over 18 or a person with parental responsibility"),
                "Expected error message for age confirmation, but got: " + error.getText());
    }


    @And("the user agrees to the Terms and Conditions")
    public void theUserAgreesToTheTermsAndConditions() {
        WebElement termsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_25']")));
        termsCheckboxLabel.click();
    }


    @And("the user agrees to being over {int} years old")
    public void theUserAgreesToBeingOverYearsOld(int age) {
        WebElement ageResponsibilityCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_26']")));
        ageResponsibilityCheckboxLabel.click();
    }


    @And("the user {string} to the Terms and Conditions")
    public void theUserToTheTermsAndConditions(String action) {
        WebElement termsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_25']")));
        if (action.trim().equalsIgnoreCase("accepts")) {
            termsCheckboxLabel.click();
        }
    }


    @Then("{string} should be displayed")
    public void shouldBeDisplayed(String expectedMessage) {
        String expected = expectedMessage.trim().toLowerCase();
        StringBuilder combinedErrors = new StringBuilder();

        String[] errorIds = {
                "member_firstname-error",
                "member_lastname-error",
                "member_emailaddress-error",
                "member_confirmemailaddress-error",
                "signupunlicenced_password-error",
                "signupunlicenced_confirmpassword-error",
                "dp-error"
        };

        for (String id : errorIds) {
            try {
                WebElement errorElement = driver.findElement(By.id(id));
                if (errorElement.isDisplayed()) {
                    combinedErrors.append(errorElement.getText().trim()).append(" ");
                }
            } catch (NoSuchElementException ignored) {}
        }

        try {
            WebElement summaryError = driver.findElement(By.className("validation-summary-errors"));
            if (summaryError.isDisplayed()) {
                combinedErrors.append(summaryError.getText().trim()).append(" ");
            }
        } catch (NoSuchElementException ignored) {}

        String allErrors = combinedErrors.toString().toLowerCase();
        if (expected.contains("account created")) {
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("SignUpConfirmation"),
                    "Expected to be on the sign-up confirmation page, but was on: " + currentUrl);
        } else {
            assertTrue(allErrors.contains(expected),
                    "\nExpected message to contain: \"" + expected + "\"\nBut got: \"" + allErrors + "\"");
        }
    }
}
//