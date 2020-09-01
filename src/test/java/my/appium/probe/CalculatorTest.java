package my.appium.probe;

import java.util.concurrent.TimeUnit;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class CalculatorTest extends BaseTest {
    //source of the calculator is also very helpful
    //https://android.googlesource.com/platform/packages/apps/ExactCalculator/+/refs/heads/marshmallow-dr-dev/src/com/android/calculator2/KeyMaps.java

    private AndroidDriver<MobileElement> driver;

    @BeforeClass
    public void setUp() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Lenovo TB3-850M");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "6.0");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");

        capabilities.setCapability("appPackage", "com.android.calculator2");
        capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
        //driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver = new AndroidDriver<MobileElement>(getServiceUrl(), capabilities);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSessionEstablished() {
        String activity = driver.currentActivity();
        String pkg = driver.getCurrentPackage();
        Assert.assertEquals(pkg, "com.android.calculator2");
        Assert.assertEquals(activity, ".Calculator");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    @Test
    public void testCalculateSum() throws Exception {
        WebElement first = driver.findElementById("com.android.calculator2:id/digit_9");
        first.click();
        WebElement add = driver.findElementById("com.android.calculator2:id/op_add");
        add.click();
        WebElement second = driver.findElementById("com.android.calculator2:id/digit_2");
        second.click();
        WebElement equalTo = driver.findElementById("com.android.calculator2:id/eq");
        equalTo.click();

        WebElement results = driver.findElementById("com.android.calculator2:id/formula");
        assert (results.getText().equals("11"));
        if (results.getText().equals("11")) {
            System.out.println("Test Passed.");
        } else {
            System.out.println("Test Failed.");
        }
        assertEquals("11", results.getText());
    }

    @Test
    public void testCalculatePowNegative() throws Exception {
        try {
            WebElement clear = driver.findElementById("com.android.calculator2:id/del");
            clear.click();

            WebElement first = driver.findElementById("com.android.calculator2:id/digit_9");
            first.click();
            WebElement pow = driver.findElementById("com.android.calculator2:id/fun_10pow");
            pow.click();
            WebElement second = driver.findElementById("com.android.calculator2:id/digit_2");
            second.click();
            WebElement equalTo = driver.findElementById("com.android.calculator2:id/eq");
            equalTo.click();

            WebElement results = driver.findElementById("com.android.calculator2:id/formula");
            assertEquals("81", results.getText());

        } catch (org.openqa.selenium.NoSuchElementException ex) {
            assertTrue(ex.getMessage().contains("An element could not be located on the page using the given search parameters."));
        }
    }

    @Test
    public void testDivideByZero() throws Exception {
        try {
            WebElement first = driver.findElementById("com.android.calculator2:id/digit_1");
            first.click();
            WebElement div = driver.findElementById("com.android.calculator2:id/op_div");
            div.click();
            WebElement second = driver.findElementById("com.android.calculator2:id/digit_0");
            second.click();
            WebElement equalTo = driver.findElementById("com.android.calculator2:id/eq");
            equalTo.click();

            WebElement results = driver.findElementById("com.android.calculator2:id/formula");
            assertEquals("∞", results.getText());
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().contains("Divide by zero"));
        }
    }

    @Test
    public void testWrongInput() throws Exception {
        WebElement first = driver.findElementById("com.android.calculator2:id/const_pi");
        first.click();
        WebElement point = driver.findElementById("com.android.calculator2:id/dec_point");
        point.click();
        WebElement left = driver.findElementById("com.android.calculator2:id/lparen");
        left.click();
        WebElement equalTo = driver.findElementById("com.android.calculator2:id/eq");
        equalTo.click();

        WebElement results = driver.findElementById("com.android.calculator2:id/formula");
        assertEquals("π.(", results.getText());
    }

    @Test
    public void testCalculateWithPi() throws Exception {
        WebElement first = driver.findElementById("com.android.calculator2:id/const_pi");
        first.click();
        WebElement pow = driver.findElementById("com.android.calculator2:id/op_pow");
        pow.click();
        WebElement second = driver.findElementById("com.android.calculator2:id/digit_0");
        second.click();
        WebElement clear = driver.findElementById("com.android.calculator2:id/del");
        clear.click();
        WebElement third = driver.findElementById("com.android.calculator2:id/digit_2");
        third.click();
        WebElement equalTo = driver.findElementById("com.android.calculator2:id/eq");
        equalTo.click();

        assertEquals("9.8696044011", getFormulaResult().getText());
    }

    private WebElement getFormulaResult() {
        return driver.findElementById("com.android.calculator2:id/formula");
    }
}
