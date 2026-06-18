package stepdefinitions.api;

import context.TestContext;
import dto.LoginRequest;
import dto.RegisterRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import utilities.LoggerUtil;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LoginSteps {

    TestContext context;
    Logger logger;

    public LoginSteps(TestContext context) {
        this.context = context;
        logger = LoggerUtil.get();
    }

    @Given("there should be a user exists with email {string} and pass {string}")
    public void there_should_be_a_user_exists_with_email_and_pass(String email, String password) {
        RegisterRequest registerPayload = RegisterRequest.builder()
                .email(email)
                .fullName(email.substring(0, email.length() - 10))
                .password(password)
                .confirmPassword(password)
                .role("admin")
                .build();
        given()
                .contentType(ContentType.JSON)
                .body(registerPayload)
                .when()
                .post("http://localhost:8080/api/v1/auth/register");
    }

    @When("sent request with {string}, {string}")
    public void user_enters(String email, String password) {
        LoginRequest requestPayload = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        Response response = given()
                .body(requestPayload)
                .contentType(ContentType.JSON)
                .when()
                .post();

        context.setSessionVar("response", response);
    }

    @Then("the response data should match the registration JSON schema")
    public void the_response_data_should_match_schema() {
        Response response = (Response) context.getSessionVar("response");
        Assert.assertNotNull(response, "Raw Response object missing from context session!");

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/register-schema.json"));

        logger.info("JSON Schema validation passed successfully!");
    }
}
