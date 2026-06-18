package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features/api"},
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "html: reports/RegistrationAPI.html"
        }
)
public class RegisterRunner extends AbstractTestNGCucumberTests {
}
