# Mobile App Automation Testing Framework - Appium + TestNg (Android & iOS)

This is a project for automation testing of Swag Labs mobile app.

## Used Tools/Technologies:
* Appium 2.0
* Java (15.02)
* TestNG
* Maven
* Intellij Idea
* Log4J2
* Extent Reports
* Json
* Xml

## Key points:
- Designed by Page Object Model
- Supported by Android & iOS platforms
- Used Explicit Wait
- Provided code readability
- Avoided code duplication
- Designed independent tests
- Provided parallel execution (Emulator / Simulator)
- Put test data and static text in a separate files
- Implemented programmatically starting of Appium Server
- Implemented Screenshots/Videos capturing
- Integrated with Logging Framework
- Integrated with Reporting framework
- Implemented DeepLinks

## Description of Project Structure

### src/main/resources
- app, apk files
- data/loginUser.Json - (test data)
- strings/strings.xml - (static text)
- config.properties - (Global parameters: Appium Server Url, App Package, BundleId, etc);
- log4j2.xml - (specific properties for logging);

### src/test/java
- com_qa
   - BaseTest class - (Initializing Driver, Config Properties [Global Variables], Explicit Wait, Driver command [click, sendKeys, scroll, etc], exit Driver);
   - MenuPage - (Defining common elements for few screens);
- com_pages - (Page Objects - UI Element definitions and Methods);
- com_qa_tests - (Test classes);
- com_qa_utils - (Common Utilities);
- com_qa_listeners - (Screenshots/Video capturing);
- com_qa_reports - (Extent Report);
- ui - (DeepLink);


### root folder
- testng.xml -  (file for test executing)
- Extent.html - (Tests Report)

![](../../Downloads/report.png)