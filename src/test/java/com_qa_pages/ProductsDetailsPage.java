package com_qa_pages;

import com_qa.MenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class ProductsDetailsPage extends MenuPage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]") // \n
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
    private WebElement SLBTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
    private WebElement SLBText;

//    @AndroidFindBy(accessibility = "test-Price")
//    private WebElement SLBPrice;

    @AndroidFindBy (accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy (id = "test-BACK TO PRODUCTS")
    private WebElement backToProductsBtn;

    @iOSXCUITFindBy (id = "test-ADD TO CART")
    private WebElement addToCartBtn;

    public String getSLBTitle(){
        String title = getText(SLBTitle, "title is - ");
        return title;
    }
    public String getSLBText(){
        String txt = getText(SLBText, "txt is - ");
        return txt;
    }
//    public String getSLBPrice(){
//        String price = getText(SLBPrice);
//        System.out.println("price is - " + price);
//        return price;
//    }
    public String scrollToSLBPriceAndGetSLBPrice(){
        return  getText(scrollToElement(), "");
    }
    public void scrollPage(){
        iOSScrollToElement();
    }
    public Boolean isAddToCartBtnDisplayed() {
        return addToCartBtn.isDisplayed();
    }

    public ProductsPage pressBackToProductsBtn(){
        click(backToProductsBtn, "navigate back to products page");
        return new ProductsPage();
    }
}
