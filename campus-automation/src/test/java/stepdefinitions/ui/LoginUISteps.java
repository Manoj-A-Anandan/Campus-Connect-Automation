package stepdefinitions.ui;

import context.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import config.ConfigManager;
import pages.LoginPage;
import pages.DashBoardPage;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;

import java.util.Map;

public class LoginUISteps {

    private final TestContext context;
    private final Logger logger = LoggerUtil.get();

    public LoginUISteps(TestContext context) {
        this.context = context;
    }

    private WebDriver getDriver() {
        WebDriver driver = (WebDriver) context.getSessionVar("driver");
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Ensure scenario is tagged with @UI.");
        }
        return driver;
    }

    private LoginPage getLoginPage() {
        return new LoginPage(getDriver());
    }

    @When("user logs in with:")
    public void user_logs_in_with(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String email = data.get("email");
        String password = data.get("password");
        logger.info("Filling login details for email: {}", email);
        getLoginPage().fillLoginDetails(email, password);
    }

    @And("clicks on the Login button")
    public void clicks_on_the_login_button() {
        logger.info("Clicking the Login button");
        getLoginPage().clickLoginButton();
    }

    @Then("a login error message should contain {string}")
    public void a_login_error_message_should_contain(String expectedError) {
        logger.info("Verifying login error message contains: {}", expectedError);
        String actualError = getLoginPage().getErrorMessage();
        Assert.assertTrue(actualError.toLowerCase().contains(expectedError.toLowerCase()),
                "Expected login error message to contain '" + expectedError + "' but actual was: '" + actualError + "'");
    }

    @Given("user navigates directly to {string}")
    public void user_navigates_directly_to(String relativeUrl) {
        String baseUrl = ConfigManager.get("base.url");
        logger.info("Navigating directly to URL: {}{}", baseUrl, relativeUrl);
        getDriver().get(baseUrl + relativeUrl);
    }

    @Then("the user should be redirected to the Login page")
    public void the_user_should_be_redirected_to_the_login_page() {
        logger.info("Verifying redirection to the Login page");
        LoginPage loginPage = getLoginPage();
        boolean redirected = loginPage.waitForUrlToContain("/login", 5);
        Assert.assertTrue(redirected, "Expected redirection to login page but URL is: " + loginPage.getCurrentUrl());
    }

    @When("the user clicks the logout button")
    public void the_user_clicks_the_logout_button() {
        logger.info("Clicking the logout button on Dashboard");
        DashBoardPage dashboardPage = new DashBoardPage(getDriver());
        dashboardPage.clickLogout();
    }

    @Then("the localStorage {string} should be deleted")
    public void the_localstorage_should_be_deleted(String key) {
        logger.info("Verifying that localStorage key '{}' is deleted", key);
        LoginPage loginPage = getLoginPage();
        String token = loginPage.getLocalStorageItem(key);
        Assert.assertNull(token, "Expected localStorage key '" + key + "' to be deleted but was: " + token);
    }
}
