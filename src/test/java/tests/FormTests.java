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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
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

        step("Вход на страницу формы для заполнения",()->{
            formPage.open();
            formPage.entry();
        });

    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @AfterEach
    void backLogs() {
        Attach.screenshotAs(driver,"Last screenshot");
        Attach.pageSource(driver);
        Attach.browserConsoleLogs(driver);
    }

    @Test
    @DisplayName("Минимально заполненная форма")
    void minimalValidFields() throws InterruptedException {

        step("Заполнение полей E-Mail, имя и пол",()->{
            formPage.fillEmail("1test@example.com")
                    .fillName("Иван")
                    .selectGender("Мужской");
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка соответсвия добавленых данных",()->{
            assertTrue(formPage.checkAddedDataInRow("test@example.com", "Иван", "Мужской",0));
        });
    }

    @Test
    @DisplayName("Полное заполнение всех полей")
    void fullFormWithAllFields() {

        step("Заполнение полей E-Mail, имя, пол, checkbox и radioButton",()->{
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

        step("Проверка соответсвия добавленых данных",()->{
            assertTrue(formPage.checkAddedDataInRow("anna.kovalenko@gmail.com", "Анна Коваленко", "Женский", 1));
        });
    }

    @Test
    @DisplayName("Заполнение формы без E-Mail")
    void emptyEmailNotSubmitted() throws InterruptedException {

        step("Заполнение полей имя, пол и radioButton. Без E-Mail",()->{
            formPage.fillName("Пётр")
                    .selectGender("Мужской")
                    .radioSelect(2);
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка правильно выведеной ошибке в pop-up окне - Неверный формат E-Mail",()->{
            assertEquals("Неверный формат E-Mail", formPage.isErrorMessageShown());
        });
    }

    @Test
    @DisplayName("Некорректный формат E-Mail")
    void invalidEmailFormat() {

        step("Заполнение полей E-Mail, имя, пол и radioButton. Не валидное значение E-Mail",()->{
            formPage.fillEmail("not-an-email")
                    .fillName("Сергей")
                    .selectGender("Мужской")
                    .radioSelect(1);
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка правильно выведеной ошибке в pop-up окне - Неверный формат E-Mail",()->{
            assertEquals("Неверный формат E-Mail", formPage.isErrorMessageShown());
        });
    }

    @Test
    @DisplayName("Пустое поле Имя")
    void emptyNameNotSubmitted() {

        step("Заполнение полей E-Mail и пол. Без имени",()->{
            formPage.fillEmail("no_name@test.ru")
                    .selectGender("Женский");
        });

        step("Добавление данных в таблицу",()->{
            formPage.addDataInRow();
        });

        step("Проверка правильно выведеной ошибке в pop-up окне - Поле имя не может быть пустым",()->{
            assertEquals("Поле имя не может быть пустым", formPage.isErrorMessageShown());
        });
    }
}
