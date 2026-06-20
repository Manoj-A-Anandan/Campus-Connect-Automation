package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    private By registerLink = By.xpath("//a[contains(text(), 'Register')]");
    
    private By emailField = By.id("email");
    private By passwordField = By.id("password");
    private By loginButton = By.className("submit-btn");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/login");
    }

    public RegisterPage clickRegisterLink() {
        waitForElementToBeClickable(registerLink).click();
        return new RegisterPage(driver);
    }

    public void fillLoginDetails(String email, String password) {
        driver.findElement(emailField).sendKeys(email == null ? "" : email);
        driver.findElement(passwordField).sendKeys(password == null ? "" : password);
    }

    public DashBoardPage clickLoginButton() {
        driver.findElement(loginButton).click();
        return new DashBoardPage(driver);
    }

    public String getErrorMessage() {
        // 1. Check HTML5 native validation messages
        String[] fieldIds = {"email", "password"};
        for (String id : fieldIds) {
            try {
                WebElement element = driver.findElement(By.id(id));
                String validationMessage = element.getAttribute("validationMessage");
                if (validationMessage != null && !validationMessage.trim().isEmpty()) {
                    return validationMessage;
                }
            } catch (Exception e) {
                // Ignore and check next
            }
        }
        
        // 2. Check the on-page error-message div
        try {
            return waitForElementVisibility(By.className("error-message")).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
