package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;

    protected Select dropDown;


    protected void selectRoleByValue(By role, String value) {
        dropDown = new Select(driver.findElement(role));
        if (value != null && !value.trim().isEmpty()) {
            try {
                dropDown.selectByValue(value.toUpperCase().trim());
            } catch (Exception e) {
                try {
                    dropDown.selectByVisibleText(value);
                } catch (Exception ex) {
                    throw new RuntimeException("Could not select role option with value: " + value, ex);
                }
            }
        }
    }

    protected WebElement waitForElementToBeClickable(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForElementToBeClickable(By locator) {
        return waitForElementToBeClickable(locator, Duration.ofSeconds(10));
    }

    protected WebElement waitForElementVisibility(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementVisibility(By locator) {
        return waitForElementVisibility(locator, Duration.ofSeconds(10));
    }

    public boolean waitForUrlToContain(String fraction, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.urlContains(fraction));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }


}
