package utilities;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

public class AllureUtils {

    public static void attachResponse(Response response){

        Allure.addAttachment(
                "Response",
                "application/json",
                response.asPrettyString(),
                ".json"
        );
    }

    public static void attachRequest(String requestBody){

        Allure.addAttachment(
                "Request",
                "application/json",
                requestBody,
                ".json"
        );
    }

    public static void attachScreenshot(byte[] screenshot) {
        Allure.addAttachment(
                "Failure Screenshot",
                "image/png",
                new java.io.ByteArrayInputStream(screenshot),
                ".png"
        );
    }
}