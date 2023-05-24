package testCase.mobile;

import base.BaseTest;
import io.appium.java_client.HasSettings;
import io.appium.java_client.Setting;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import reports.ExtentTestManager;

public class HomePageTestCases extends BaseTest {
        private static final Logger logger = Logger.getLogger(HomePageTestCases.class);

    @Test
    public void verifyAppLogoOnHomePage() {
        logger.info("This test verifies that App Logo On Home Page.");
        ExtentTestManager.getTest().setDescription("This test verifies that App Logo On Home Page.");
        ((HasSettings) mobileDriver).setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 500);
        clickOnPermission();
        onBoardingNextButton();
        checkAppLogoOnHomePage();
        verifyHomepage();
    }
}
