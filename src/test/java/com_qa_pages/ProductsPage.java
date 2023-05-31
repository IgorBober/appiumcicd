package com_qa_pages;

import com_qa.BaseTest;
import com_qa.MenuPage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class ProductsPage extends MenuPage {
    @AndroidFindBy(xpath =
                    "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.widget.TextView")
   @iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
    private WebElement productTitleTxt;
    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
    private WebElement SLBTitle;
    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
    private WebElement SLBPrice;

    public String getTitle(){
        String title = getText(productTitleTxt, "product page title is - ");
        return title;
    }

    public String getSLBTitle(){
        String txt = getText(SLBTitle, "txt is - ");
        return txt;
    }
    public String getSLBPrice(){
        String price = getText(SLBPrice, "price is ");
        return price;
    }
    public ProductsDetailsPage pressSLBTitle(){
        click(SLBTitle, "press SLB title link");
        return new ProductsDetailsPage();
    }
}
