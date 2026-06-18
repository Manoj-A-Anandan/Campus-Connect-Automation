package hooks;

import driver.ConfigManager;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

    @Before
    public void setUp() {
        RestAssured.baseURI = ConfigManager.get("api.base.url");
    }
}
