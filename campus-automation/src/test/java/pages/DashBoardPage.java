package pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class DashBoardPage extends BasePage {

    @FindBy(xpath = "//div[@class='user-details']/p")
    List<WebElement> userdetails;

    @FindBy(xpath = "//strong[contains(text(), 'Email')]/parent::p")
    WebElement email;

    @FindBy(xpath = "//strong[contains(text(), 'Role')]/parent::p")
    WebElement role;

    @FindBy(xpath = "//strong[contains(text(), 'User')]/parent::p")
    WebElement userId;

    public DashBoardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<String> getUserDetails() {
        List<String> details = new ArrayList<>();

        userdetails.forEach((user) -> {
            details.add(user.getText());
        });
        return details;
    }

    public String getEmail() {
        return email.getText();
    }
    public String getRole() {
        return role.getText();
    }
    public String getUserId() {
        return userId.getText();
    }
}
