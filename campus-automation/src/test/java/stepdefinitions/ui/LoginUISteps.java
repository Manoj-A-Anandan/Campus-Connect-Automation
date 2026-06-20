package stepdefinitions.ui;

import context.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;

import java.util.Map;

public class LoginUISteps {

    private final TestContext context;

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
        getLoginPage().fillLoginDetails(email, password);
    }

    @And("clicks on the Login button")
    public void clicks_on_the_login_button() {
        getLoginPage().clickLoginButton();
    }

    @Then("a login error message should contain {string}")
    public void a_login_error_message_should_contain(String expectedError) {
        String actualError = getLoginPage().getErrorMessage();
        Assert.assertTrue(actualError.toLowerCase().contains(expectedError.toLowerCase()),
                "Expected login error message to contain '" + expectedError + "' but actual was: '" + actualError + "'");
    }
}
