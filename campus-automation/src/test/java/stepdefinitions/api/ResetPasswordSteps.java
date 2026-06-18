package stepdefinitions.api;

import context.TestContext;
import dto.ResetPasswordRequest;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class ResetPasswordSteps {

    private final TestContext context;

    public ResetPasswordSteps(TestContext context) {
        this.context = context;
    }

    @When("I retrieve the latest reset token for {string} from the test endpoint")
    public void retrieveLatestResetToken(String email) {
        Response response = given()
                .basePath("")
                .queryParam("email", email)
                .when()
                .get("/auth/test/reset-token");

        if (response.getStatusCode() == 200) {
            context.setSessionVar("resetToken", response.jsonPath().getString("data"));
        }

        context.setSessionVar("response", response);
    }

    @When("the validate token request is sent with the retrieved token")
    public void validateRetrievedToken() {
        String token = getResetToken();

        Response response = given()
                .basePath("")
                .when()
                .post("/auth/validate-token/{token}", token);

        context.setSessionVar("response", response);
    }

    @When("the reset password request is sent with the retrieved token, password {string}, and confirmPassword {string}")
    public void resetPasswordWithRetrievedToken(String newPassword, String confirmPassword) {
        ResetPasswordRequest requestPayload = ResetPasswordRequest.builder()
                .token(getResetToken())
                .newPassword(newPassword)
                .confirmPassword(confirmPassword)
                .build();

        Response response = given()
                .basePath("")
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post("/auth/reset-password");

        context.setSessionVar("response", response);
    }

    private String getResetToken() {
        String token = (String) context.getSessionVar("resetToken");
        Assert.assertNotNull(token, "Reset token was not found in TestContext session.");
        return token;
    }
}
