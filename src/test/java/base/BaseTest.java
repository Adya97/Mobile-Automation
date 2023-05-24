package Base;

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
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private static final String LOG4J_PROPERTIES = "./src/java/Resources/log4j.properties";
    private final static String START_LOGGING_TEXT = "------------------------------Start logging------------------------------";
    private final static String GLOBAL_PROPERTIES = "/src/main/resources/global.properties";
    public static Logger logger = LogManager.getLogger(BaseTest.class);
    public static String appName=null;
    public static String iosReset=null;
    protected AppiumDriver<MobileElement> mobileDriver;
    public Properties testData;

    public WebDriver webDriver;
    

    @BeforeMethod(alwaysRun = true)
    public void setupMobileDriver() throws IOException {
        /*Init Mobile Driver*/
        mobileDriver = initAppiumDriver();
    }
    @BeforeMethod(alwaysRun = true)
    public void setupWebDriver() {
        webDriver = initWebDriver();
    }

    /**
     * Initialises the appium Driver with its required capabilities
     * @return
     * @throws IOException
     */
    public AppiumDriver<MobileElement> initAppiumDriver() throws IOException {
        AppiumDriver<MobileElement> mobileDriver;
        logger = Logger.getLogger("PlobalApps");
        PropertyConfigurator.configure(LOG4J_PROPERTIES);
        logger.info(START_LOGGING_TEXT);
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + GLOBAL_PROPERTIES);
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

    /**
     * Initialises the Web Driver with its required capabilities
     * @return
     */
    public WebDriver initWebDriver() {
        WebDriver webDriver;
        logger=Logger.getLogger("AdityaNisal");
        PropertyConfigurator.configure(LOG4J_PROPERTIES);
        logger.info(START_LOGGING_TEXT);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("window-size= 1400,800");
//        options.addArguments("---headless");
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return webDriver;
    }
    public static DesiredCapabilities capabilities() throws IOException {
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/global.properties");
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
}
