package stepdefinitions.api;

import context.TestContext;
import dto.ResetPasswordRequest;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;

import static io.restassured.RestAssured.given;

public class ResetPasswordSteps {

    private final TestContext context;
    private final Logger logger;

    public ResetPasswordSteps(TestContext context) {
        this.context = context;
        this.logger = LoggerUtil.get();
    }

    @When("I retrieve the latest reset token for {string} from the test endpoint")
    public void retrieveLatestResetToken(String email) {
        logger.info("Retrieving the latest reset token for: {}", email);
        Response response = given()
                .basePath("")
                .queryParam("email", email)
                .when()
                .get("/auth/test/reset-token");

        if (response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("data");
            logger.info("Successfully retrieved reset token: {}", token);
            context.setSessionVar("resetToken", token);
        } else {
            logger.warn("Failed to retrieve reset token. Status: {}", response.getStatusCode());
        }

        context.setSessionVar("response", response);
    }

    @When("the validate token request is sent with the retrieved token")
    public void validateRetrievedToken() {
        String token = getResetToken();
        logger.info("Validating retrieved reset token: {}", token);

        Response response = given()
                .basePath("")
                .when()
                .post("/auth/validate-token/{token}", token);

        logger.info("Token validation response status: {}", response.getStatusCode());
        context.setSessionVar("response", response);
    }

    @When("the reset password request is sent with the retrieved token, password {string}, and confirmPassword {string}")
    public void resetPasswordWithRetrievedToken(String newPassword, String confirmPassword) {
        String token = getResetToken();
        logger.info("Sending reset password request with token: {}", token);
        ResetPasswordRequest requestPayload = ResetPasswordRequest.builder()
                .token(token)
                .newPassword(newPassword)
                .confirmPassword(confirmPassword)
                .build();

        Response response = given()
                .basePath("")
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post("/auth/reset-password");

        logger.info("Reset password response status: {}", response.getStatusCode());
        context.setSessionVar("response", response);
    }

    private String getResetToken() {
        String token = (String) context.getSessionVar("resetToken");
        Assert.assertNotNull(token, "Reset token was not found in TestContext session.");
        return token;
    }
}
