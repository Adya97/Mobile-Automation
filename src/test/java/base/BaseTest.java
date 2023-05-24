package base;

import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HasSettings;
import io.appium.java_client.MobileElement;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import pagescreenDevice.mobile.mobile.HomePage;
import reports.ExtentTestManager;
import utilities.ReadConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseTest {

    private static final String LOG4J_PROPERTIES = "C:\\Users\\ViNUS\\IdeaProjects\\MobileAutomationFramework\\resouces\\log4j.properties";
    private final static String START_LOGGING_TEXT = "------------------------------Start logging------------------------------";
    private final static String GLOBAL_PROPERTIES = "C:\\Users\\ViNUS\\IdeaProjects\\MobileAutomationFramework\\resouces\\global.properties";
    public static Logger logger = LogManager.getLogger(BaseTest.class);
    public static String appName=null;
    public static String iosReset=null;
    protected AppiumDriver<MobileElement> mobileDriver;
    public Properties testData;
    public WebDriver webDriver;
    public static HomePage homePage;
    ReadConfig readconfig = new ReadConfig();

    @BeforeMethod(alwaysRun = true)
    public void setupMobileDriver() throws IOException {
        /*Init Mobile Driver*/
        mobileDriver = initAppiumDriver();
        initMobilePages();
    }

   /* @BeforeMethod(alwaysRun = true)
    public void setupWebDriver() {
        webDriver = initWebDriver();
    }*/

    public void initMobilePages() {
        homePage = new HomePage(mobileDriver);
    }

    /**
     * Initialises the appium Driver with its required capabilities
     * @return
     * @throws IOException
     */
    public AppiumDriver<MobileElement> initAppiumDriver() throws IOException {
        AppiumDriver<MobileElement> mobileDriver;
        logger = Logger.getLogger("AdityaNisal");
        PropertyConfigurator.configure(LOG4J_PROPERTIES);
        logger.info(START_LOGGING_TEXT);
        FileInputStream fis = new FileInputStream(GLOBAL_PROPERTIES);
        Properties prop = new Properties();
        prop.load(fis);
        String port = prop.getProperty("port");
        String appiumServer = prop.getProperty("appiumServer");
        logger.info("Port Number: " + port);
        logger.info("Appium Server: " + appiumServer);
        logger.info("App Name: " + appName);
            DesiredCapabilities capabilities = capabilities();
            if (System.getenv("platform").equalsIgnoreCase("Android"))
                mobileDriver = new AndroidDriver<>(new URL("http://" + appiumServer + ":" + port + "/wd/hub"), capabilities);
            else
                mobileDriver = new IOSDriver<>(new URL("http://" + appiumServer + ":" + port + "/wd/hub"), capabilities);
        mobileDriver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
        ((HasSettings)mobileDriver).setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 500);
        return mobileDriver;
    }

    public static DesiredCapabilities capabilities() throws IOException {
        FileInputStream fis = new FileInputStream(GLOBAL_PROPERTIES);
        Properties prop = new Properties();
        prop.load(fis);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String platformName = System.getenv("platform");
        String packageName;
        String activityName;
        String bundleName;

        if (platformName.equalsIgnoreCase("Android")) {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, (String) prop.get("deviceName"));
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, (String) prop.get("automationName"));
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, (String) prop.get("platformVersion"));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            capabilities.setCapability(MobileCapabilityType.ORIENTATION, (String) prop.get("orientation"));
            if(appName.equalsIgnoreCase("neovo")){
                packageName= prop.getProperty("neovoPackageName");
                activityName= prop.getProperty("neovoAppActivityName");
            }else {
                packageName= prop.getProperty("sandboxPackageName");
                activityName= prop.getProperty("sandboxAppActivityName");
            }
            capabilities.setCapability("appPackage", packageName);
            capabilities.setCapability("appActivity", activityName);
            capabilities.setCapability("autoGrantPermissions", "true");
            logger.info("Capabilities: "+ capabilities);

        }
        else {
            capabilities.setCapability("platformName", prop.getProperty("platformNameIOS"));
            if (iosReset != null && iosReset.equalsIgnoreCase("iOSAppReset")){
                capabilities.setCapability("fullReset", true);
            }
            capabilities.setCapability("useNewWDA", false);
            capabilities.setCapability("platformVersion",  prop.getProperty("platformVersionIOS"));
            capabilities.setCapability("deviceName", prop.getProperty("deviceNameIOS"));
            capabilities.setCapability("udid", prop.getProperty("udid")); //can use "auto" if only 1 device is connected
            capabilities.setCapability("newCommandTimeout", "100");
            if(appName.equalsIgnoreCase("neovo")){
                bundleName= prop.getProperty("neovoBundleId");
                if (iosReset != null && iosReset.equalsIgnoreCase("iOSAppReset")){
                    capabilities.setCapability("app", System.getProperty("user.dir") + "/src/main/resources/Neovo_rcJan_v2_3feb.ipa");
                }
            }else {
                bundleName= prop.getProperty("sandboxBundleId");
                if (iosReset != null && iosReset.equalsIgnoreCase("iOSAppReset")){
                    capabilities.setCapability("app", System.getProperty("user.dir") + "/src/main/resources/sandbox_rcJan_v2_3feb.ipa");
                }
            }
            capabilities.setCapability("bundleId", bundleName);
            capabilities.setCapability("xcodeOrgId", prop.getProperty("xcodeOrgId"));
            capabilities.setCapability("xcodeSigningId", prop.getProperty("xcodeSigningId"));
            capabilities.setCapability("isRealMobile", true);
            capabilities.setCapability("automationName", prop.getProperty("automationNameIOS"));
            capabilities.setCapability("shouldTerminateApp", true);
            capabilities.setCapability("autoLaunch", "true");
            capabilities.setCapability("autoAcceptAlerts", "true");
            logger.info("Capabilities: "+ capabilities);
        }
        return capabilities;
    }

    public void waitElement(MobileElement element, int timer) {
        WebDriverWait wait = new WebDriverWait(mobileDriver, timer);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * owner : Aditya Nisal
     * Location allowed
     * App name: Steve Madden
     */
    public void clickOnPermission() {
        waitElement(homePage.getLocationAllowed(), 6);
        homePage.getLocationAllowed().click();
        logger.info("Location allowed");
        ExtentTestManager.getTest().log(LogStatus.PASS, "Location allowed");
    }
    /**
     * owner : Aditya Nisal
     * OnBoarding Page
     * App name: Steve Madden
     */
    public void onBoardingNextButton() {
        waitElement(homePage.getOnBoardingNextButton1(), 6);
        homePage.getOnBoardingNextButton1().click();
        waitElement(homePage.getOnBoardingNextButton2(), 6);
        homePage.getOnBoardingNextButton2().click();
        logger.info("On-Boarding Page verified successfully");
        ExtentTestManager.getTest().log(LogStatus.PASS, "On-Boarding Page verified successfully");
    }

    /**
     * owner : Aditya Nisal
     * Verify App logo on Home Page
     * App name: Steve Madden
     */
    public void checkAppLogoOnHomePage() {
        assertThat(homePage.getAppLogoOnHomePage().isDisplayed(), equalTo(true));
        logger.info("Verified App Logo on home Page successfully");
        ExtentTestManager.getTest().log(LogStatus.PASS, "Verified App Logo on home Page successfully");
    }

    /**
     * App Name: SM
     * Assignee Name: Aditya Nisal
     * Verify homepage for SM and Platform
     */
    public void verifyHomepage() {
        waitElement(homePage.getCartForSM(), 16);
        assertThat(homePage.getAppLogoOnHomePage().isDisplayed(), equalTo(true));
        assertThat(homePage.getSearchProductsText().isDisplayed(), equalTo(true));
        assertThat(homePage.getHamburgerMenuForSM().isDisplayed(), equalTo(true));
        logger.info(appName + " Home page verify successfully");
        ExtentTestManager.getTest().log(LogStatus.PASS, appName + " Home page verify successfully");
    }
}
