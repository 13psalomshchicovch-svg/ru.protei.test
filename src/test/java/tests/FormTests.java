package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.WebDriverProvider;
import hellper.Attach;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.selenide.AllureSelenide;
import objects.FormPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Owner("13ko31")
@Link("Here is Jira link")
public class FormTests {

    WebDriver driver;
    FormPage formPage;

    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        driver = new WebDriverProvider().get();
        formPage = new FormPage(driver);

        step("Открытие страницы формы для заполнения",()->{
            formPage.open();
            formPage.entry();
        });

    }

    @AfterEach
    void backLogs() {
        Attach.screenshotAs(driver,"Финальное состояние страницы");
        Attach.pageSource(driver);
        Attach.browserConsoleLogs(driver);
        if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("Минимально заполненная форма")
    void minimalValidFields() throws InterruptedException {

        step("Заполнение полей E-mail, имени и пола",()->{
            formPage.fillEmail("1test@example.com")
                    .fillName("Иван")
                    .selectGender("Мужской");
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка соответствия добавленных данных в таблице",()->{
            assertTrue(formPage.checkAddedDataInRow("test@example.com", "Иван", "Мужской",0));
        });
    }

    @Test
    @DisplayName("Заполнение всех полей в форме")
    void fullFormWithAllFields() {

        step("Заполнение полей E-mail, имени, пола, выбор чекбоксов и радиокнопки",()->{
            formPage.fillEmail("anna.kovalenko@gmail.com")
                    .fillName("Анна Коваленко")
                    .selectGender("Женский")
                    .checkForCheckbox(1)
                    .checkForCheckbox(2)
                    .radioSelect(1);
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка соответствия добавленных данных в таблице",()->{
            assertTrue(formPage.checkAddedDataInRow("anna.kovalenko@gmail.com", "Анна Коваленко", "Женский", 1));
        });
    }

    @CsvSource({
            "Заполнение формы без E-Mail, ",
            "Некорректный формат E-Mail, not-an-email"
    })
    @ParameterizedTest(name = "{0}")
    void emptyEmailNotSubmitted(
            String nameTest,
            String email
    ) throws InterruptedException {

        step("Заполнение полей E-mail, имени, пола и радиокнопки. Невалидное значение E-mail",()->{
            formPage.fillEmail(email)
                    .fillName("Пётр")
                    .selectGender("Мужской")
                    .radioSelect(2);
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка правильно выведенной ошибки в pop-up окне — Неверный формат E-mail",()->{
            assertEquals("Неверный формат E-Mail", formPage.isErrorMessageShown());
        });
    }


    @Test
    @DisplayName("Пустое имя не отправляется")
    void emptyNameNotSubmitted() {

        step("Заполнение полей E-mail и пола с пустым полем имени",()->{
            formPage.fillEmail("no_name@test.ru")
                    .selectGender("Женский");
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка правильно выведенной ошибки в pop-up окне — Поле имя не может быть пустым",()->{
            assertEquals("Поле имя не может быть пустым", formPage.isErrorMessageShown());
        });
    }
}
