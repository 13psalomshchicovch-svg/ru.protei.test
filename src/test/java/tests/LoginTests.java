package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.WebDriverProvider;

import hellper.Attach;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import objects.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Owner("13ko31")
@Link("Here is Jira link")
public class LoginTests {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        driver = new WebDriverProvider().get();
        loginPage = new LoginPage(driver);
        step("Открытие страницы авторизации",()-> {
            loginPage.open();
        });
    }

    @AfterEach
    void backLogs(){
        Attach.screenshotAs(driver,"Финальное состояние страницы");
        Attach.pageSource(driver);
        Attach.browserConsoleLogs(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("Успешная авторизация с корректными данными")
    public void successfulLogin() {

        step("Ввод корректных учетных данных",()-> {
            loginPage.login("test@protei.ru", "test");
        });
        step("Проверка успешного входа",()-> {
            assertTrue(loginPage.isLoginSuccess());
        });
    }

    @Test
    @DisplayName("Ошибка 'Неверный формат E-Mail' при вводе некорректного email")
    public void unsuccessfulLoginWithWrongEmail() {

        step("Ввод некорректного email",()-> {
            loginPage.login("test", "123");
        });
        step("Проверка сообщения об ошибке 'Неверный формат E-Mail'",()-> {
            assertEquals("Неверный формат E-Mail",loginPage.getErrorMessageText());
        });
    }

    @Test
    @DisplayName("При вводе неверного пароля отображается ошибка 'Неверный E-Mail или пароль'")
    public void unsuccessfulLoginWithWrongPassword() {

        step("Ввод корректного email и неверного пароля",()-> {
            loginPage.login("test@protei.ru", "123");;
        });
        step("Проверка сообщения об ошибке 'Неверный E-Mail или пароль'",()-> {
            assertEquals("Неверный E-Mail или пароль",loginPage.getErrorMessageText());
        });
    }


}
