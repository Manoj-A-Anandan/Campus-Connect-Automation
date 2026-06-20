package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private By registerLink = By.xpath("//a[contains(text(), 'Register')]");

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
}
