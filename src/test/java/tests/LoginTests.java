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
        step("Вход на страницу авторизации",()-> {
            loginPage.open();
        });
    }

    @AfterEach
    void backLogs(){
        Attach.screenshotAs(driver,"Last screenshot");
        Attach.pageSource(driver);
        Attach.browserConsoleLogs(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("Позитивный тест авторизации для входа в форму")
    public void successfulLogin() {

        step("Ввод корректные данные для авторизации",()-> {
            loginPage.login("test@protei.ru", "test");
        });
        step("Проверка корректно выведенной ошибки 'Неверный E-Mail или пароль'",()-> {
            assertTrue(loginPage.isLoginSuccess());
        });
    }

    @Test
    @DisplayName("Неверный E-Mail - Неверный формат E-Mail")
    public void unsuccessfulLoginWithWrongEmail() {

        step("Ввод данных для входа с другим E-Mail",()-> {
            loginPage.login("test", "123");
        });
        step("Проверка корректно выведенной ошибки 'Неверный формат E-Mail'",()-> {
            assertEquals("Неверный формат E-Mail",loginPage.getErrorMessageText());
        });
    }

    @Test
    @DisplayName("Не правильный пароль - Неверный E-Mail или пароль")
    public void unsuccessfulLoginWithWrongPassword() {

        step("Ввод данных для входа с верным E-Mail и не правильным паролем",()-> {
            loginPage.login("test@protei.ru", "123");;
        });
        step("Проверка корректно выведенной ошибки 'Неверный E-Mail или пароль'",()-> {
            assertEquals("Неверный E-Mail или пароль",loginPage.getErrorMessageText());
        });
    }


}
