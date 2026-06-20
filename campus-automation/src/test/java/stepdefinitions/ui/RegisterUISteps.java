package stepdefinitions.ui;

import config.ConfigManager;
import context.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;
import pages.RegisterPage;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;

import java.util.Map;

public class RegisterUISteps {

    private final TestContext context;
    private final Logger logger = LoggerUtil.get();

    public RegisterUISteps(TestContext context) {
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

    private RegisterPage getRegisterPage() {
        return new RegisterPage(getDriver());
    }

    @Given("user is on the Login page")
    public void user_is_on_the_login_page() {
        logger.info("Navigating to the Login page");
        getLoginPage().navigate(ConfigManager.get("base.url"));
    }

    @And("user navigates to the Registration page")
    public void user_navigates_to_the_registration_page() {
        logger.info("Navigating to the Registration page");
        getLoginPage().clickRegisterLink();
    }

    @When("user registers with:")
    public void user_registers_with(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String email = data.getOrDefault("email", "");
        String fullName = data.getOrDefault("fullName", "");
        String password = data.getOrDefault("password", "");
        String confirmPassword = data.getOrDefault("confirmPassword", "");
        String role = data.getOrDefault("role", "");
        
        logger.info("Filling registration details for email: {}", email);
        getRegisterPage().fillDetails(email, fullName, password, confirmPassword, role);
    }

    @And("clicks on the Register button")
    public void clicks_on_the_register_button() {
        logger.info("Clicking the Register button");
        getRegisterPage().clickRegisterButton();
    }

    @Then("the user should be redirected to the Dashboard page")
    public void the_user_should_be_redirected_to_the_dashboard_page() {
        logger.info("Verifying redirection to the Dashboard page");
        boolean redirected = getRegisterPage().waitForUrlToContain("/dashboard", 5);
        Assert.assertTrue(redirected, "Expected redirection to dashboard but URL is: " + getRegisterPage().getCurrentUrl());
    }

    @Then("an error message should contain {string}")
    public void an_error_message_should_contain(String string) {
        logger.info("Verifying registration error message contains: {}", string);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("register"));
        String actualError = getRegisterPage().getErrorMessage();
        Assert.assertTrue(actualError.toLowerCase().contains(string.toLowerCase()),
                "Expected error message to contain '" + string + "' but actual error was: '" + actualError + "'");
    }

}
