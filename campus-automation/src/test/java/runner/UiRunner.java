package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/resources/features/ui/"},
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:reports/UiReports.html"
        }
)
public class UiRunner extends AbstractTestNGCucumberTests {
}
