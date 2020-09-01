package my.appium.probe;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GeckoWebpage {

    private static WebDriverWait wait;
    private static WebDriver driver;

    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        FirefoxOptions options = new FirefoxOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        options.setCapability("loggingPrefs", logPrefs);
        options.setCapability("marionette",true);

        // Setting system properties of Firefox driver
        System.setProperty("webdriver.gecko.driver", "d:/downloads/GeckoDriver.exe");
        driver = new FirefoxDriver(options);

        wait = new WebDriverWait(driver, 50);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //checkConnection();
        GoogleSearch();
    }

    private static void checkConnection() {
        WebElement error = driver.findElement(By.className("error-code"));
        if (error.getText().equals("DNS_PROBE_FINISHED_NO_INTERNET") || error.getText().equals("ERR_NAME_NOT_RESOLVED")) {
            System.out.println("No internet connection found.");
        } else {
            System.out.println("Internet connected found.");
        }
    }

    private static void GoogleSearch() throws InterruptedException {
        driver.get("https://www.google.com");
        wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        driver.findElement(By.name("q")).sendKeys("wikipedia vajra" + Keys.ENTER);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        By.tagName("div");
        By.className("r");
        driver.findElement(By.partialLinkText("Vajra - Wikipedia")).click();

        //Navigation methods allows to make movement
        driver.navigate().back();


        WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@role='listbox']")));
        List<WebElement> listHref = driver.findElements(By.xpath("//*[@id='rso']//h3/a"));
        for (int i = 0; i < listHref.size(); i++) {
            if (i > 0 && listHref.get(i).getText().contains("Vajra")) {
                listHref.get(i).click();
                break;
            }
        }

        //Thread.sleep(Long.parseLong("1000"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.close();
        driver.quit();
    }

}