package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RegisterPage extends BasePage {

    private By registerButton = By.xpath("//button[contains(text(), 'Register')]");
    private By authCard = By.xpath("//div[@class='auth-card']/h2");

    private By emailField = By.id("email");
    private By fullNameField = By.id("fullName");
    private By passwordField = By.id("password");
    private By confirmPasswordField = By.id("confirmPassword");
    private By roleField = By.id("role");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillDetails(String email, String fullName, String password, String confirmPassword, String role) {
        driver.findElement(emailField).sendKeys(email == null ? "" : email);
        driver.findElement(fullNameField).sendKeys(fullName == null ? "" : fullName);
        driver.findElement(passwordField).sendKeys(password == null ? "" : password);
        driver.findElement(confirmPasswordField).sendKeys(confirmPassword == null ? "" : confirmPassword);
        selectRoleByValue(roleField, role);
    }

    public DashBoardPage clickRegisterButton() {
        driver.findElement(registerButton).click();
        return new DashBoardPage(driver);
    }

    public String getErrorMessage() {
        // Checking HTML5 native validation messages
        String[] fieldIds = {"email", "fullName", "password", "confirmPassword"};
        for (String id : fieldIds) {
            try {
                WebElement element = driver.findElement(By.id(id));
                String validationMessage = element.getAttribute("validationMessage");
                if (validationMessage != null && !validationMessage.trim().isEmpty()) {
                    return validationMessage;
                }
            } catch (Exception e) {
            }
        }
        
        // Checking the on-page error-message div
        try {
            return waitForElementVisibility(By.className("error-message")).getText();
        } catch (Exception e) {
            return "";
        }
    }

}
