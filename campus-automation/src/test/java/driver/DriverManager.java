package driver;

import config.ConfigManager;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            String browser = ConfigManager.get("browser");
            WebDriver driver = DriverFactory.createInstance(browser);
            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    public static boolean hasDriver() {
        return driverThreadLocal.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    private static final ThreadLocal<byte[]> screenshotThreadLocal = new ThreadLocal<>();

    public static void setScreenshot(byte[] screenshot) {
        screenshotThreadLocal.set(screenshot);
    }

    public static byte[] getScreenshot() {
        return screenshotThreadLocal.get();
    }

    public static void clearScreenshot() {
        screenshotThreadLocal.remove();
    }
}
