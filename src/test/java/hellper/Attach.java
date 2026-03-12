package hellper;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Attach {

    @Attachment(value = "{attachName}", type = "image/png")
    public static byte[] screenshotAs(WebDriver driver, String attachName) {
        if (driver == null) {
            return null;
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource(WebDriver driver) {
        if (driver == null) {
            return "WebDriver is null → page source unavailable".getBytes(StandardCharsets.UTF_8);
        }
        return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "{attachName}", type = "text/plain")
    public static String text(String attachName, String content) {
        return content;
    }

    public static void browserConsoleLogs(WebDriver driver) {
        if (driver == null) {
            text("Browser console logs", "WebDriver is null → logs unavailable");
            return;
        }

        try {
            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
            String logs = logEntries.getAll().stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));

            String content = logs.isEmpty() ? "No browser console logs available" : logs;
            text("Browser console logs", content);
        } catch (Exception e) {
            text("Browser console logs", "Error retrieving browser logs: " + e.getMessage());
        }
    }

    public static void screenshot(WebDriver driver) {
        screenshotAs(driver, "Screenshot");
    }

    public static void screenshot(WebDriver driver, String name) {
        screenshotAs(driver, name);
    }
}

