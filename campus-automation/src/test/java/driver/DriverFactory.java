package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {

    private DriverFactory() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver createInstance(String browser) {
        WebDriver driver;
        String browserName = (browser != null) ? browser.toLowerCase().trim() : "chrome";
        
        // Read headless mode safely (default to false if not set)
        String headlessProp = ConfigManager.get("headless");
        boolean headless = Boolean.parseBoolean(headlessProp);

        switch (browserName) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserName);
        }
        return driver;
    }
}
