package com_qa_pages;

import com_qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class SettingsPage extends BaseTest {
    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy (id = "test-LOGOUT")
    private WebElement logoutBtn;

    public LoginPage logoutBtn(){
        click(logoutBtn, "press Logout button");
        return new LoginPage();
    }
}
