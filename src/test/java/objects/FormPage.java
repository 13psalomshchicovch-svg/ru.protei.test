package objects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormPage {

    WebDriver driver;
    WebDriverWait wait;

    @FindBy(id = "dataEmail")
    private WebElement emailDataField;

    @FindBy(id = "dataName")
    private WebElement nameField;

    @FindBy(id = "dataGender")
    private WebElement genderDropdown;

    @FindBy(id = "dataSend")
    private WebElement sendBut;

    @FindBy(css = "input[type='checkbox']")
    private List<WebElement> checkboxList;

    @FindBy(css = ".uk-modal-close")
    private WebElement okBut;

    @FindBy(css = "table tbody tr:last-child")
    private WebElement lastResultRow;

    @FindBy(id = "loginEmail")
    private WebElement emailField;

    @FindBy(id = "loginPassword")
    private WebElement passwordField;

    @FindBy(id = "authButton")
    private WebElement logBut;

    public FormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(4));
        PageFactory.initElements(driver, this);
    }

    public FormPage open() {
        driver.get(Objects.requireNonNull(driver.getCurrentUrl()));
        return this;
    }

    public FormPage entry() {
        emailField.sendKeys("test@protei.ru");
        passwordField.sendKeys("test");
        logBut.click();
        return this;
    }

    public FormPage fillEmail(String email) {
        emailDataField.clear();
        if(email != null)
        emailDataField.sendKeys(email);
        return this;
    }

    public FormPage fillName(String name) {
        nameField.clear();
        nameField.sendKeys(name);
        return this;
    }

    public FormPage selectGender(String gender) {
        Select select = new Select(genderDropdown);
        select.selectByVisibleText(gender);
        return this;
    }

    public FormPage checkForCheckbox(int index) {
        if (index < 1 || index > checkboxList.size()) return this;
        WebElement cb = checkboxList.get(index - 1);
        if (!cb.isSelected()) {
            cb.click();
        }
        return this;
    }

    public FormPage radioSelect(int number) {
        if (number < 1 || number > 3) {
            throw new IllegalArgumentException("Вариант должен быть 1, 2 или 3");
        }

        String radioId = "dataSelect2" + number;

        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(
                By.id(radioId)
        ));

        if (!radio.isSelected()) {
            radio.click();
        }
        return this;
    }

    public FormPage addDataInRow() {
        sendBut.click();
        WebElement button = okBut;
        if (button == null) {
            button.click();
        }
        return this;
    }

    public boolean checkAddedDataInRow(String email, String name, String gender, int indexRadio) {

        String expectedRadio = "";

        wait.until(ExpectedConditions.visibilityOf(lastResultRow));
        String rowText = lastResultRow.getText();

        System.out.println(rowText);

        switch (indexRadio) {
            case 1 -> {
                expectedRadio = "2.1";
                break;
            }
            case 2 -> {
                expectedRadio = "2.2";
                break;
            }
            case 3 -> {
                expectedRadio = "2.3";
                break;
            }

        }

        return  rowText.contains(email) &&
                rowText.contains(name) &&
                rowText.contains(gender) &&
                rowText.contains(expectedRadio);
    }

    public String isErrorMessageShown() {
        try {

            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#dataAlertsHolder, [attribute='data-uk-alert']")
            ));

            String errorText = errorElement.getText().trim();

            System.out.println("Текст ошибки в модальном окне: " + errorText);
            return errorText;

        } catch (TimeoutException e) {
            System.out.println("Модальном окне с ошибкой не появилась");
            return null;
        }
    }

}



