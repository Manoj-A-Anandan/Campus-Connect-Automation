package stepdefinitions.api;

import context.TestContext;
import dto.RegisterRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import utilities.LoggerUtil;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RegisterSteps {

    private final TestContext context;
    Logger logger;

    public RegisterSteps(TestContext context) {
        this.context = context;
        logger = LoggerUtil.get();
        logger.info("AuthenticationSteps for API initialised");
    }

    @Given("the path is set to {string}")
    public void the_path_is_set(String path) {
        RestAssured.basePath = path;
        logger.info("The path is set to " + path);
    }

    @When("the request payload is sent with following data:")
    public void configure_request_payload(DataTable dataTable) {
        Map<String, String> userDetails = dataTable.asMap(String.class, String.class);
        String email = userDetails.get("email");
        logger.info("Sending API registration request for email: {}", email);

        RegisterRequest registerPayload = RegisterRequest.builder()
                .email(email)
                .fullName(userDetails.get("fullName"))
                .password(userDetails.get("password"))
                .confirmPassword(userDetails.get("confirmPassword") != null ? userDetails.get("confirmPassword") : userDetails.get("password"))
                .role(userDetails.get("role"))
                .build();

        Response response = given()
                .contentType("application/json")
                .body(registerPayload)
                .when()
                .post();

        logger.info("API registration response status: {}", response.getStatusCode());
        context.setSessionVar("response", response);
    }

    @Then("^the status code should be (\\d+) and success should be (true|false)$")
    public void then_status_code_should_be(int statusCode, boolean isSuccess) {
        Response response = (Response) context.getSessionVar("response");
        logger.info("Verifying response: status code={}, success={}", statusCode, isSuccess);

        Assert.assertEquals(response.getStatusCode(), statusCode, "API returned status code: " + response.getStatusCode());
        Assert.assertEquals(isSuccess, response.jsonPath().getBoolean("success"),
                "Expected success as " +  isSuccess
                        + " but actual response is " + response.jsonPath().getBoolean("success")
        );
    }

    @And("the response message should contains {string}")
    public void theResponseMessageShouldContains(String message) {
        logger.info("Verifying response message contains: {}", message);
        Response response = (Response) context.getSessionVar("response");
        Assert.assertNotNull(response, "Response object was not found in TestContext session!");

        String responseMessage = response.jsonPath().getString("message");
        Assert.assertNotNull(responseMessage, "Response message field is null!");

        Assert.assertTrue(responseMessage.toLowerCase().contains(message.toLowerCase()),
                "Message did not match. Expected to contain: '" + message + "', but got: '" + responseMessage + "'"
        );
    }

    @When("sent request with {string}, {string}, {string}, {string}, {string}")
    public void user_enters(String email, String fullName, String password, String confirmPassword, String role) {
        logger.info("Sending registration request with inline parameters for email: {}", email);
        RegisterRequest registerPayload = RegisterRequest.builder()
                .email(email)
                .fullName(fullName)
                .password(password)
                .confirmPassword(confirmPassword)
                .role(role).build();

        Response response = given()
                .contentType("application/json")
                .body(registerPayload)
                .when()
                .post();

        logger.info("API registration response status: {}", response.getStatusCode());
        context.setSessionVar("response", response);
    }


}
