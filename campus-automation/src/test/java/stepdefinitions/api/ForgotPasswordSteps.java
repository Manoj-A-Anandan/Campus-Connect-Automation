package stepdefinitions.api;

import context.TestContext;
import dto.ForgotPasswordRequest;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;

import static io.restassured.RestAssured.given;

public class ForgotPasswordSteps {
    TestContext context;
    Logger logger;

    public ForgotPasswordSteps(TestContext context) {
        this.context = context;
        this.logger = LoggerUtil.get();
    }

    @When("the request sent with email {string}")
    public void the_request_sent_with_email(String email) {
        logger.info("Sending forgot password request for email: {}", email);
        ForgotPasswordRequest requestPayload = ForgotPasswordRequest.builder()
                .email(email)
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post();

        logger.info("Received forgot password response with status: {}", response.getStatusCode());
        context.setSessionVar("response", response);
    }
}
