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

    // WebDriver instans som används för att interagera med webbläsaren
    WebDriver driver;
    // WebDriverWait för att hantera väntetider och synlighet för element
    WebDriverWait wait;
    // Variabel för att lagra en unik e-postadress
    String uniqueEmail;

    // Hjälpmetod som genererar en unik e-postadress med Mailnesia
    private String generateUniqueEmail() {
        String uniquePart = UUID.randomUUID().toString().substring(0, 8); // Skapar en unik del genom att ta en del av en UUID
        return "Lionel_" + uniquePart + "@mailnesia.com"; // Returnerar e-post med unikt suffix
    }

    // Väntar på att ett element ska bli synligt på sidan
    private WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Väntar på att ett element ska bli klickbart
    private WebElement waitUntilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Fyller ett fält baserat på ID med ett givet värde
    private void fillFieldById(String id, String value) {
        WebElement field = waitUntilVisible(By.id(id)); // Väntar på att fältet ska bli synligt
        field.clear(); // Rensar eventuell tidigare text i fältet
        field.sendKeys(value); // Fyller fältet med det angivna värdet
    }

    // Sätt upp WebDriver innan testet körs
    @Before
    public void setUp() {
        // Hämtar vilken webbläsare som ska användas, standard är Chrome
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

        driver.manage().deleteAllCookies(); // Rensar alla cookies innan varje test
        driver.manage().window().maximize(); // Maximerar webbläsarfönstret
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Sätter en timeout på 10 sekunder för väntningar
    }

    // Stänger WebDriver efter att testet har körts
    @After
    public void tearDown() {
        if (driver != null) driver.quit(); // Stänger WebDriver om den är öppen
    }

    // Given att användaren är på registreringssidan
    @Given("the user is on the registration page")
    public void the_user_is_on_the_registration_page() {
        // Navigera till registreringssidan
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");

        // Vänta tills ett element på sidan är synligt för att bekräfta att sidan har laddats
        WebElement firstNameField = waitUntilVisible(By.id("member_firstname"));

        // Validera att användaren verkligen är på registreringssidan
        assertTrue(driver.getCurrentUrl().contains("NewSupporterAccount"), "Expected to be on the registration page, but was on: " + driver.getCurrentUrl());

        // Kontrollera att registreringsformuläret har laddats korrekt genom att se till att förnamnsfältet finns
        assertNotNull(firstNameField, "The registration form did not load correctly.");
    }

    // När användaren anger ett förnamn
    @When("the user enters {string} as first name")
    public void the_user_enters_as_first_name(String firstName) {
        fillFieldById("member_firstname", firstName); // Anropa hjälpfunktionen för att fylla i förnamn
    }

    // När användaren anger ett efternamn
    @When("the user enters {string} as last name")
    public void the_user_enters_as_last_name(String lastName) {
        fillFieldById("member_lastname", lastName); // Anropa hjälpfunktionen för att fylla i efternamn
    }

    // När användaren lämnar efternamnsfältet tomt
    @When("the user leaves last name field empty")
    public void the_user_leaves_last_name_field_empty() {
        fillFieldById("member_lastname", ""); // Lämnar efternamnsfältet tomt
    }

    // När användaren anger en unik e-postadress
    @When("the user enters a unique Mailnesia email")
    public void the_user_enters_a_unique_mailnesia_email() {
        uniqueEmail = generateUniqueEmail(); // Skapa en unik e-postadress
        fillFieldById("member_emailaddress", uniqueEmail); // Fyll i e-postadressen i fältet
    }

    // När användaren bekräftar e-postadressen
    @When("the user confirms the email")
    public void the_user_confirms_the_email() {
        fillFieldById("member_confirmemailaddress", uniqueEmail); // Fyll i bekräftelse-fältet med samma e-postadress
    }

    // När användaren anger sitt födelsedatum
    @When("the user enters {string} as date of birth")
    public void the_user_enters_as_date_of_birth(String dob) {
        WebElement dobField = waitUntilVisible(By.id("dp")); // Vänta på att födelsedatumfältet ska bli synligt
        dobField.sendKeys(dob); // Fyll i födelsedatumet
        dobField.sendKeys(Keys.RETURN); // Tryck Enter för att skicka datumet
    }

    // När användaren anger ett lösenord
    @When("the user enters {string} as password")
    public void the_user_enters_as_password(String password) {
        fillFieldById("signupunlicenced_password", password); // Fyll i lösenordet i fältet
    }

    // När användaren bekräftar lösenordet
    @When("the user confirms {string} as password")
    public void the_user_confirms_as_password(String password) {
        fillFieldById("signupunlicenced_confirmpassword", password); // Fyll i bekräftelse-fältet för lösenordet
    }

    // När användaren väljer en roll för sin basketaktivitet
    @When("the user selects {string} as basketball role")
    public void the_user_selects_as_basketball_role(String role) {
        waitUntilClickable(By.cssSelector("label[for='signup_basketballrole_18']")).click(); // Klicka på lämplig checkbox för basketroll
    }

    // När användaren godkänner Code of Ethics and Conduct
    @When("the user agrees to the Code of Ethics and Conduct")
    public void the_user_agrees_to_the_code_of_ethics_and_conduct() {
        WebElement ethicsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='fanmembersignup_agreetocodeofethicsandconduct']")));
        ethicsCheckboxLabel.click(); // Klicka för att godkänna etikreglerna
    }

    // När användaren bekräftar att de är över 18 år eller har en förmyndare
    @When("the user confirms being over {int} or a guardian")
    public void the_user_confirms_being_over_or_a_guardian(Integer age) {
        WebElement ageCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("sign_up_26"))); // Vänta på åldersbekräftelse
        if (!ageCheckbox.isSelected()) { // Om checkboxen inte redan är markerad
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ageCheckbox); // Klicka för att markera den
        }
    }

    // När användaren skickar formuläret
    @When("the user submits the form")
    public void the_user_submits_the_form() {
        WebElement confirmAndJoinButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("join"))); // Vänta på att knappen blir synlig
        try {
            WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(confirmAndJoinButton)); // Vänta på att knappen blir klickbar
            clickableButton.click(); // Klicka på knappen för att skicka formuläret
        } catch (TimeoutException e) {
            System.out.println("Knappen är inte klickbar eftersom formuläret inte är ifyllt korrekt."); // Om knappen inte går att klicka på, skriv ut ett felmeddelande
        }
    }

    // Då kontot ska skapas framgångsrikt, kontrollera att användaren omdirigeras till bekräftelsesidan
    @Then("the account should be created successfully")
    public void the_account_should_be_created_successfully() {
        String currentUrl = driver.getCurrentUrl(); // Hämta den aktuella URL:en
        assertTrue(currentUrl.contains("SignUpConfirmation"), "Expected to be on sign-up confirmation page, but was on: " + currentUrl); // Verifiera att användaren är på bekräftelsesidan
    }

    // När registreringen inte ska vara framgångsrik
    @Then("the registration should not be completed")
    public void the_registration_should_not_be_completed() {
        try {
            Thread.sleep(3000); // Vänta på eventuell omdirigering
        } catch (InterruptedException e) {
            e.printStackTrace(); // Hantera eventuella undantag
        }

        String currentUrl = driver.getCurrentUrl(); // Hämta aktuell URL
        // Om användaren inte hamnar på bekräftelsesidan ska testet passera
        assertFalse(currentUrl.contains("SignUpConfirmation"),
                "Test misslyckades: användaren registrerades trots att formuläret inte var korrekt ifyllt.");
    }

    // När ett felmeddelande ska visas för saknat efternamn
    @Then("an error message should be displayed for missing last name")
    public void an_error_message_should_be_displayed_for_missing_last_name() {
        WebElement error = waitUntilVisible(By.id("member_lastname-error")); // Vänta på felmeddelande om saknat efternamn
        assertTrue(error.getText().contains("Last Name is required"), "Expected error message for missing last name, but got: " + error.getText());
    }

    // När ett felmeddelande ska visas för mismatch mellan lösenord
    @Then("an error message should be displayed for password mismatch")
    public void an_error_message_should_be_displayed_for_password_mismatch() {
        WebElement error = waitUntilVisible(By.id("signupunlicenced_confirmpassword-error")); // Vänta på felmeddelande om mismatch mellan lösenord
        assertTrue(error.getText().toLowerCase().contains("did not match"), "Password mismatch error was not displayed correctly");
    }

    // När ett felmeddelande ska visas för icke accepterade villkor
    @Then("an error message should be displayed for terms not accepted")
    public void an_error_message_should_be_displayed_for_terms_not_accepted() {
        WebElement error = waitUntilVisible(By.cssSelector("span[for='TermsAccept']"));
        assertTrue(error.getText().contains("You must confirm that you have read and accepted our Terms and Conditions"),
                "Expected error message for terms acceptance, but got: " + error.getText());
    }

    // När ett felmeddelande ska visas för icke bekräftad ålder
    @Then("an error message should be displayed for age not confirmed")
    public void an_error_message_should_be_displayed_for_age_not_confirmed() {
        WebElement error = waitUntilVisible(By.cssSelector("span[for='AgeAccept']"));
        assertTrue(error.getText().contains("You must confirm that you are over 18 or a person with parental responsibility"),
                "Expected error message for age confirmation, but got: " + error.getText());
    }

    // När användaren godkänner villkoren för användning
    @And("the user agrees to the Terms and Conditions")
    public void theUserAgreesToTheTermsAndConditions() {
        WebElement termsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_25']")));
        termsCheckboxLabel.click(); // Klicka för att godkänna villkoren
    }

    // När användaren bekräftar att de är över 18 år
    @And("the user agrees to being over {int} years old")
    public void theUserAgreesToBeingOverYearsOld(int age) {
        WebElement ageResponsibilityCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_26']")));
        ageResponsibilityCheckboxLabel.click(); // Klicka för att bekräfta ålder
    }

    // När användaren vidtar en åtgärd på villkoren (t.ex. accepterar eller inte accepterar)
    @And("the user {string} to the Terms and Conditions")
    public void theUserToTheTermsAndConditions(String action) {
        WebElement termsCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='sign_up_25']")));
        if (action.trim().equalsIgnoreCase("accepts")) {
            termsCheckboxLabel.click(); // Om handlingen är att acceptera, klicka på villkorsrutan
        }
    }

    // När ett specifikt meddelande ska vara synligt
    @Then("{string} should be displayed")
    public void shouldBeDisplayed(String expectedMessage) {
        String expected = expectedMessage.trim().toLowerCase(); // Ta bort eventuella extra mellanslag och konvertera till lowercase
        StringBuilder combinedErrors = new StringBuilder(); // Samlar alla felmeddelanden

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
                WebElement errorElement = driver.findElement(By.id(id)); // Försök att hitta felmeddelandet för varje fält
                if (errorElement.isDisplayed()) {
                    combinedErrors.append(errorElement.getText().trim()).append(" "); // Lägg till felet i den samlade strängen
                }
            } catch (NoSuchElementException ignored) {}
        }

        try {
            WebElement summaryError = driver.findElement(By.className("validation-summary-errors"));
            if (summaryError.isDisplayed()) {
                combinedErrors.append(summaryError.getText().trim()).append(" "); // Lägg till sammanfattningsfelet
            }
        } catch (NoSuchElementException ignored) {}

        String allErrors = combinedErrors.toString().toLowerCase(); // Samla alla felmeddelanden och konvertera till lowercase
        if (expected.contains("account created")) {
            String currentUrl = driver.getCurrentUrl(); // Hämta aktuell URL
            assertTrue(currentUrl.contains("SignUpConfirmation"),
                    "Expected to be on the sign-up confirmation page, but was on: " + currentUrl); // Verifiera att användaren omdirigeras till bekräftelsesidan
        } else {
            assertTrue(allErrors.contains(expected),
                    "\nExpected message to contain: \"" + expected + "\"\nBut got: \"" + allErrors + "\""); // Kontrollera att felmeddelandena matchar det förväntade
        }
    }
}