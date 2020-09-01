package my.appium.probe;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.By;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.testng.Assert.assertTrue;


public class GooglePlayTest extends BaseTest {
    private AndroidDriver<MobileElement> driver;
    private WebDriverWait wait;
    private String testAppName = "ccleaner";

    @BeforeClass
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Lenovo TB3-850M");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability("appPackage", "com.android.vending");
        capabilities.setCapability("appActivity", "com.google.android.finsky.activities.MainActivity");

        driver = new AndroidDriver<MobileElement>(getServiceUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 5);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testNetworkConnection() {
        // Appium provide these masks for network connection:
        // AIRPLANE_MODE(1), ALL_NETWORK_ON(6), DATA_ONLY(4), WIFI_ONLY(2), NONE(0);
        ConnectionState connectionState = new ConnectionState(2);
        driver.setConnection(connectionState);
        assertTrue(driver.getConnection().isWiFiEnabled());
        Assert.assertEquals(connectionState.getBitMask(), driver.getConnection().getBitMask());
    }

    @Test
    public void testSessionEstablished() {
        String activity = driver.currentActivity();
        String pkg = driver.getCurrentPackage();
        Assert.assertEquals(pkg, "com.android.vending");
        //Assert.assertEquals(activity, ".MainActivity");
        Assert.assertEquals(activity, "com.google.android.finsky.activities.MainActivity");
    }

    @Test
    public void testGooglePlayFeatures() throws Exception {

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //Show navigation drawer
        driver.findElement(By.xpath("//android.widget.FrameLayout[@content-desc = 'Show navigation drawer']")).click();

        List<MobileElement> elements = driver.findElements(By.xpath("//android.widget.TextView"));
        for (MobileElement element : elements) {
            if (element.getText().equals("Settings")) {
                element.click();
                break;
            }
        }

        MobileElement el = (MobileElement) driver
                .findElementByAndroidUIAutomator("new UiScrollable("
                        + "new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().textContains(\"Play Store version\"));");
        assert (el.isDisplayed());

        MobileElement elem = (MobileElement) driver
                .findElementByAndroidUIAutomator("new UiScrollable("
                        + "new UiSelector().scrollable(true)).scrollIntoView("
                        + "new UiSelector().textContains(\"Notifications\"));");
        assert (elem.isDisplayed());

        driver.findElement(By.xpath("//android.widget.Switch[@text = 'ON']")).click();

        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc = 'Navigate up']")).click();
    }


    @Test
    public void testSearchInputAndReturnBack() throws Exception {

        openSearchForm();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys(testAppName);

        List<MobileElement> recyclerViewResults = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().classNameMatches(\".*\")"));
        assertTrue(recyclerViewResults.size() > 1);

        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc = 'Navigate up']")).click();

        // Discover recommended games chapter
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc = 'More results for Discover recommended games']")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        this.wait = new WebDriverWait(driver, 5);
        List<MobileElement> items = driver.findElements(By.xpath("//android.widget.TextView"));
        if (items.size() > 0) {
            for (MobileElement element : items) {
                if (isElementPresentAndDisplayed(element)) {
                    if (element.getText().equals("Discover recommended games")) {
                        element.isEnabled();
                        break;
                    }
                }
            }
        }
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc = 'Navigate up']")).click();
    }

    @Test
    public void testSearchWrongInput() throws Exception {
        openSearchForm();
        driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys("!?,");
        driver.pressKey(new KeyEvent(AndroidKey.DEL));

        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc = 'Clear button']")).click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys("qqqqq");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc = 'Navigate up']")).click();

        driver.findElement(By.xpath("//android.widget.TextView[@text = 'Top charts']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text = 'Premium']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text = 'Categories']")).click();

        openSearchForm();
        driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys("%");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.pressKey(new KeyEvent(AndroidKey.BACKSLASH));
        driver.pressKey(new KeyEvent(AndroidKey.DEL));
        driver.pressKey(new KeyEvent(AndroidKey.DEL));
        driver.findElement(MobileBy.className("android.widget.EditText")).sendKeys("joomla");

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }

    @Test
    public void testToInstallAppFromGooglePlayStore() throws Exception {

        openSearchForm();

        driver.findElement(MobileBy.className("android.widget.EditText"))
                .sendKeys(testAppName);

        driver.findElement(By.xpath("//android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[1]")).click();

        MobileElement button = driver.findElement(MobileBy.className("android.widget.Button"));
        if (button.getText().equals("Install")) {
            System.out.println("Application has not installed yet");
            button.click();
        } else {
            System.out.println("Application to be opened");
            button.click();
        }
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.pressKey(new KeyEvent(AndroidKey.HOME));
        driver.closeApp();
    }


    private void openSearchForm() {
        this.wait = new WebDriverWait(driver, 5);
        List<MobileElement> elements = driver.findElements(By.xpath("//android.widget.TextView"));
        for (MobileElement element : elements) {
            if (element.getAttribute("text").equals("Search for apps & games")) {
                element.click();
                break;
            }
        }
    }
    private boolean isElementPresentAndDisplayed(final WebElement element) {
        try {
            return element.isDisplayed();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
