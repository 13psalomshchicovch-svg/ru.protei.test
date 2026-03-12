package config;

import org.aeonbits.owner.Config;

public interface WebDriverConfig extends Config{

    @Key("baseUrl")
    @DefaultValue("file:///C:/Users/13ko31/Downloads/qa-test%20(AntiSpam).html")
    String getBaseUrl();

    @Key("browser")
    @DefaultValue("CHROME")
    Browser getBrowser();
}
