package hooks;

import config.ConfigManager;
import context.TestContext;
import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before("@API")
    public void setUpAPI() {
        RestAssured.baseURI = ConfigManager.get("api.base.url");
    }

    @Before("@UI")
    public void setUpUI() {
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        context.setSessionVar("driver", driver);
    }

    @After("@UI")
    public void tearDownUI() {
        DriverManager.quitDriver();
    }
}
