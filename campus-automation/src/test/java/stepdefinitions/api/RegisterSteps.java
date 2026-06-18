package stepdefinitions.api;

import context.TestContext;
import dto.AuthResponse;
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

        RegisterRequest registerPayload = RegisterRequest.builder()
                .email(userDetails.get("email"))
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

        context.setSessionVar("response", response);
    }

    @Then("^the status code should be (\\d+) and success should be (true|false)$")
    public void then_status_code_should_be(int statusCode, boolean isSuccess) {

        Response response = (Response) context.getSessionVar("response");

        Assert.assertEquals(response.getStatusCode(), statusCode, "API returned status code: " + response.getStatusCode());
        Assert.assertEquals(isSuccess, response.jsonPath().getBoolean("success"),
                "Expected success as " +  isSuccess
                        + " but actual response is " + response.jsonPath().getBoolean("success")
        );

        try {
            AuthResponse authResponse = response.as(AuthResponse.class);
            context.setSessionVar("authResponse", authResponse);
        } catch (Exception e) {
            logger.warn("Could not deserialize response to AuthResponse: " + e.getMessage());
        }
    }

    @And("the response message should contains {string}")
    public void theResponseMessageShouldContains(String message) {
        AuthResponse response = (AuthResponse) context.getSessionVar("authResponse");
        Assert.assertNotNull(response, "RegisterResponse object was not found in TestContext session!");
        Assert.assertTrue(response.getMessage().toLowerCase().contains(message.toLowerCase()    ),
                "Success message is not matched, Actual response : " + response.getMessage()
        );
    }

    @When("sent request with {string}, {string}, {string}, {string}, {string}")
    public void user_enters(String email, String fullName, String password, String confirmPassword, String role) {
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

        context.setSessionVar("response", response);
    }


}
