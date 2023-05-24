package pagescreenDevice.mobile.mobile;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.support.CacheLookup;
import pagescreenDevice.mobile.manager.ScreenManager;


@Getter
@Setter
public class HomePage extends ScreenManager{
    public HomePage(AppiumDriver<MobileElement> driver) {
        super(driver);
    }

    @AndroidFindBy(xpath="//android.widget.Button[@text='While using the app']")
    @CacheLookup
    public MobileElement locationAllowed;

    @AndroidFindBy(xpath="//android.view.View[@index=4]")
    @CacheLookup
    public MobileElement onBoardingNextButton1;

    @AndroidFindBy(xpath="//android.widget.Button[@text='NEXT']")
    @CacheLookup
    public MobileElement onBoardingNextButton2;

    @AndroidFindBy(xpath="//android.widget.ImageView[contains(@resource-id,'imageView_app_logo')]")
    @CacheLookup
    public MobileElement appLogoOnHomePage;

    @AndroidFindBy(xpath="//android.widget.ImageView[contains(@resource-id,'id/cart_custom_menu')]")
    public MobileElement cartForSM;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='Search products']/parent::XCUIElementTypeOther/parent::XCUIElementTypeOther/following-sibling::XCUIElementTypeButton" +
            "| //XCUIElementTypeStaticText[@name='Search products']/preceding-sibling::XCUIElementTypeButton")
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Search products']")
    MobileElement searchProductsText;

    @AndroidFindBy(xpath="//android.widget.ImageView[contains(@resource-id,'navigation_view')]")
    public MobileElement hamburgerMenuForSM;
}