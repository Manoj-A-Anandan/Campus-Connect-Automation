package stepdefinitions.api;

import context.TestContext;
import dto.ForgotPasswordRequest;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ForgotPasswordSteps {
    TestContext context;

    public ForgotPasswordSteps(TestContext context) {
        this.context = context;
    }

    @When("the request sent with email {string}")
    public void the_request_sent_with_email(String email) {
        ForgotPasswordRequest requestPayload = ForgotPasswordRequest.builder()
                .email(email)
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post();

        context.setSessionVar("response", response);
    }


}
