package com.rozetka;

import com.rozetka.util.Action;
import com.rozetka.util.BrowserType;
import com.rozetka.util.*;
import com.rozetka.util.logger.RemoteLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTestCase {
    public static final String URL = "url";
    public static final String CATEGORIES = "categoriesNavigation";
    public static final String SMARTPHONE_CATEGORY = "categoriesPages/smartPhonesPage";
    public static FileWriter WRITER;
    public static File FILE;

    private static final BrowserType BROWSER = BrowserType.parse(Config.get(Config.BROWSER));

    protected WebDriver driver;
    protected ResourceReader resource = new JSONReader();
    protected Action action;
    protected PageActions pageActions;

    @BeforeSuite
    public void createFile() throws IOException {
        FILE = new File("/amadeus/src/main/java/com/rozetka/testResults.txt");
        WRITER = new FileWriter(FILE);
        WRITER.append("<table border=1>");
    }

    @AfterSuite
    public void closeFile() throws IOException {
        WRITER.append("</table>");
        WRITER.append("<br><br>More detailed info via URL: <a href='http://docker$5-$6" + "/report?suiteId=" + RemoteLogger.SUITE_ID + "&sub=GO' target='_blank'>reporting</a>");
        WRITER.close();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        switch(BROWSER) {
            case FIREFOX:
                DesiredCapabilities firefox = DesiredCapabilities.firefox();
                firefox.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                driver = new FirefoxDriver(firefox);
                break;
            default:
                throw new Exception("No browser specified.");
        }

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() throws IOException {
        driver.quit();
    }
}
