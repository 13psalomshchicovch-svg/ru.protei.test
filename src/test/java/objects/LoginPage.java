package objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(id = "loginEmail")
    private WebElement emailField;

    @FindBy(id = "loginPassword")
    private WebElement passwordField;

    @FindBy(id = "authButton")
    private WebElement logBut;

    @FindBy(id = "dataGender")
    private WebElement gender;

    @FindBy(id = "authAlertsHolder")
    private WebElement error;



    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        PageFactory.initElements(driver, this);
    }

    public LoginPage open(){
        driver.get(Objects.requireNonNull(driver.getCurrentUrl()));
        return this;
    }

    public LoginPage login(String email,String password){
        emailField.sendKeys(email);
        passwordField.sendKeys(password);

        logBut.click();
        return this;
    }

    public boolean isLoginSuccess(){
        try {
            wait.until(ExpectedConditions.visibilityOf(gender));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(error)).getText();
        } catch (Exception e) {
            return null;
        }
    }
}
