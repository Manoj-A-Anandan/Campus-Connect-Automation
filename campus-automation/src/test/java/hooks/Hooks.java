package hooks;

import config.ConfigManager;
import context.TestContext;
import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
    public void tearDownUI(Scenario scenario) {
        if (scenario.isFailed()) {
            if (DriverManager.hasDriver()) {
                try {
                    WebDriver driver = DriverManager.getDriver();
                    byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    DriverManager.setScreenshot(screenshotBytes);
                    scenario.attach(screenshotBytes, "image/png", "Failure Screenshot");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DriverManager.quitDriver();
    }

    @BeforeAll
    public static void resetDatabaseBeforeSuite() {
        try {
            // Automatically truncate the users and password_resets tables before the tests
            // start
            String cmd = "docker exec -i campus_connect_db psql -U postgres -d campus_connect -c \"TRUNCATE TABLE users, password_resets RESTART IDENTITY CASCADE;\"";
            Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", cmd }).waitFor();
            System.out.println(">>> Database auto-reset completed successfully! <<<");
        } catch (Exception e) {
            System.err.println(">>> Database auto-reset skipped (either container not running or local): "
                    + e.getMessage() + " <<<");
        }
    }

}
