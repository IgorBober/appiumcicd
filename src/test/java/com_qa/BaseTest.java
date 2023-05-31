package com_qa;

import com.aventstack.extentreports.Status;
import com_qa_reports.ExtentReport;
import com_qa_utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {

    protected static AppiumDriver driver;
    protected static Properties props;
    protected static HashMap<String, String> strings = new HashMap<String, String>();

    protected static String platform;
    protected static String dateTime;
    protected static String deviceName;
    InputStream inputStream;
    InputStream stringis;
    private static AppiumDriverLocalService server;
    TestUtils utils = new TestUtils();


    @BeforeMethod
    public void beforeMethod(){
        System.out.println("super before method");
        ((CanRecordScreen) driver).startRecordingScreen();
    }
    @AfterMethod
    public void afterMethod(ITestResult result){
        System.out.println("super after method");
      String media = ((CanRecordScreen) driver).stopRecordingScreen();

        if(result.getStatus() == 2) {

            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();

            String dir = "videos" + File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_"
                    + params.get("deviceName") + File.separator + dateTime + File.separator + result.getTestClass().getRealClass()
                    .getSimpleName();

            File videoDir = new File(dir);

            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            try {
                FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                try {
                    stream.write(Base64.decodeBase64(media));
                } catch (IOException e) {
                    throw new RuntimeException(e); //automatic try/catch
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e); //automatic try/catch
            }
        }
    }
    public BaseTest(){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @BeforeSuite
    public void beforeSuite(){
//        ThreadContext.put("ROLLINGFILE", "ServerLogs");
        //uncomment for Mac
        server = getAppiumService();
        //uncomment for Windows
        // server.getAppiumServerDefault();
        server.start();
        server.clearOutPutStreams();     //If you do not want to print server logs to console - use this method
        System.out.println("Appium server started");
    }
    @AfterSuite
    public void afterSuite(){
        server.stop();
        System.out.println("Appium server stopped");
    }

    // for Windows
    public AppiumDriverLocalService getAppiumServerDefault(){
        return AppiumDriverLocalService.buildDefaultService();
    }


    // for Mac
    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "/Users/amac/Library/Android/sdk/emulator:/Users/amac/Library/Android/sdk/tools:/Users/amac/Library/Android/sdk/tools/bin:/Users/amac/Library/Android/sdk/platform-tools://Users/amac/maven/bin:/Library/Java/JavaVirtualMachines/jdk-15.0.2.jdk/Contents/Home/bin:/usr/local/bin/brew:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin}" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "/Users/amac/Library/Android/sdk");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//				.withArgument(() -> "--allow-insecure","chromedriver_autodownload")
                .withEnvironment(environment)
                .withLogFile(new File("ServerLogs/server.log")));  // Use it if you want to put logs in separate file
    }

    @Parameters({"emulator", "platformName", "platformVersion", "udid", "deviceName"})
    @BeforeTest
    public void beforeTest(String emulator, String platformName, String platformVersion, String udid, String deviceName)throws Exception{

        utils = new TestUtils();
        dateTime = utils.getDateTime();
        platform = platformName;
        URL url;
        try {
            props = new Properties();
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);

            stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);

            strings = utils.parseStringXML(stringis);

            DesiredCapabilities caps = new DesiredCapabilities();

            caps.setCapability("platformName", platformName);
            caps.setCapability("deviceName", deviceName);

            switch (platformName){
                case "Android":
                    caps.setCapability("automationName", props.getProperty("androidAutomationName"));
//            caps.setCapability(MobileCapabilityType.UDID, "emulator-5554");
                    caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
                    caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
                    if (emulator.equalsIgnoreCase("true")){
                        caps.setCapability("platformVersion", platformVersion);
                        caps.setCapability("avd", deviceName);
                    } else {
                        caps.setCapability("udid", udid);
                    }
//                    caps.setCapability("unlockType", "pin");
//                    caps.setCapability("unlockKey", "1111");
                    String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile(); //for Win need to use file separator
                    utils.log().info("appUrl is" + androidAppUrl);
                    caps.setCapability("app", androidAppUrl);

                    url = new URL(props.getProperty("appiumURL"));

                    driver = new AndroidDriver(url, caps);
                    break;
                case "iOS":
                    caps.setCapability("automationName", props.getProperty("iOSAutomationName"));
                    String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
                    utils.log().info("appUrl is" + iOSAppUrl);
                    caps.setCapability("bundleId", props.getProperty("iOSBundleId"));
//                    caps.setCapability("app", iOSAppUrl);

                    url = new URL(props.getProperty("appiumURL"));

                    driver = new IOSDriver(url, caps);
                    break;
                default:
                    throw new Exception("Invalid platform! - " + platformName);
            }

            String sessionId = driver.getSessionId().toString();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (inputStream != null){
                inputStream.close();
            }
            if (stringis != null) {
                stringis.close();
            }
        }
    }
    public  void waitForVisibility(WebElement e) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void clear(WebElement e){
        waitForVisibility(e);
        e.clear();
    }
    public void click(WebElement e){
        waitForVisibility(e);
        e.click();
    }

    //overloaded method
    public void click(WebElement e, String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);    // .log(Status.INFO, msg)
        e.click();
    }


    public void sendKeys (WebElement e, String txt){
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    //overloaded method
    public void sendKeys (WebElement e, String txt, String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.sendKeys(txt);
    }


    public String getAttribute(WebElement e, String attribute){
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    //overloaded method
    public String getText(WebElement e, String msg){
        String txt = null;

        switch (platform){
            case "Android":
                txt = getAttribute(e, "text");
                break;
            case "iOS":
                txt = getAttribute(e, "label");
                break;
        }
        utils.log().info(msg + txt);
        ExtentReport.getTest().log(Status.INFO, msg + txt);
        return txt;
    }


    public String getDateTime() {
        return dateTime;
    }
    public String getDeviceName(){
        return deviceName;
    }
    public String getPlatform(){
        return platform;
    }
    public static AppiumDriver getDriver(){
        return driver;
    }


    public Properties getProps() {
        return props;
    }
    public void closeApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    public void launchApp() {
        switch(getPlatform()){
            case "Android":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("androidAppPackage"));
                break;
            case "iOS":
                ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("iOSBundleId"));
        }
    }

    public WebElement scrollToElement(){
         return driver.findElement(AppiumBy.androidUIAutomator(
                 "new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
                 + "new UiSelector().description(\"test-Price\"));"));
    }
    // Basic scroll - it works!
//    public static void iOSScrollToElement() {
//        RemoteWebElement element = (RemoteWebElement) driver.findElement(AppiumBy.className("XCUIElementTypeScrollView"));
//        String elementID = element.getId();
//        HashMap<String, String> scrollObject = new HashMap<String, String>();
//        scrollObject.put("element", elementID);
//        scrollObject.put("direction", "down");
//        driver.executeScript("mobile: scroll", scrollObject);
//    }


        //It also works
//    public void iOSScrollToElement(){
////        WebElement parentElement = driver.findElement(AppiumBy.iOSNsPredicateString("type == \"XCUIElementTypeScrollView\""));
////        HashMap<String, Object> params = new HashMap<>();
//        WebElement childElement = driver.findElement(AppiumBy.accessibilityId("test-ADD TO CART"));
//        HashMap<String, Object> params2 = new HashMap<>();
////        params.put("direction", "down");
//        params2.put("elementId", ((RemoteWebElement) childElement).getId());
////        params.put("predicateString", "label == \"Web View\"");
////        params2.put("toVisible", true);
//        driver.executeScript("mobile: scrollToElement", params2);
//    }


    // Using accessibilityId = It Works!
//    public void iOSScrollToElement(){
//        final var element = this.driver.findElement(AppiumBy.accessibilityId ("test-ADD TO CART"));
//
//        final var args = new HashMap<String, Object> ();
//
//        args.put ("elementId", ((RemoteWebElement) element).getId ());
//
//        this.driver.executeScript ("mobile: scrollToElement", args);
//    }


    //Scroll to particular element using parent element and predicate string (or name) - Does not work
//    public void iOSScrollToElement() {
//        RemoteWebElement parent = (RemoteWebElement)driver.findElement(AppiumBy.className("test"));
//        String parentID = parent.getId();
//        HashMap<String, String> scrollObject = new HashMap<String, String>();
//        scrollObject.put("element", parentID);
////	  scrollObject.put("predicateString", "label == \"ADD TO CART\" AND name == \"test-ADD TO CART\"");
//        scrollObject.put("name", "test-ADD TO CART");
//        getDriver().executeScript("mobile:scroll", scrollObject);
//    }
    public void iOSScrollToElement(){
        final var element = this.driver.findElement(AppiumBy.iOSNsPredicateString("label == \"ADD TO CART\" AND name == \"ADD TO CART\""));
        final var args = new HashMap ();
        args.put ("elementId", ((RemoteWebElement) element).getId ());
        this.driver.executeScript ("mobile: scrollToElement", args);
    }

    //Scroll to element without parent element using "toVisible" - It works (As I see toVisible is not required...)
//        public void iOSScrollToElement() {
//	  RemoteWebElement element = (RemoteWebElement) driver.findElement(AppiumBy.name("test-ADD TO CART"));
//	  String elementID = element.getId();
//        HashMap<String, Object> scrollObject = new HashMap<String, Object>();
////	  scrollObject.put("element", elementID);  //elementId
//	  scrollObject.put("toVisible", true);
//        driver.executeScript("mobile:scrollToElement", scrollObject);
//    }


    @AfterTest
    public void afterTest(){
        driver.quit();
    }
}
