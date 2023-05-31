package com_qa_tests;

import com_qa.BaseTest;
import com_qa_pages.LoginPage;
import com_qa_pages.ProductsDetailsPage;
import com_qa_pages.ProductsPage;
import com_qa_pages.SettingsPage;
import com_qa_reports.ExtentReport;
import com_qa_utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import ui.DeepLink;

import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    ProductsDetailsPage productDetailsPage;
    InputStream datais;
    JSONObject loginUsers;
//    TestUtils utils = new TestUtils();

    @BeforeClass
    public void beforeClass() throws Exception {
        try {
            String dataFileName = "data/loginUser.json";
            datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(datais);
            loginUsers = new JSONObject(tokener);
            System.out.println(datais);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(datais != null) {
                datais.close();
            }
        }
//        closeApp();
//        launchApp();
    }
    @AfterClass
    public void afterClass() {

    }
    @BeforeMethod
    public void beforeMethod(Method m) {
        loginPage = new LoginPage();
        System.out.println("\n" + "****** starting test:" + m.getName() + "******" + "\n");

//        productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
//                loginUsers.getJSONObject("validUser").getString("password"));



    }
    @AfterMethod
    public void afterMethod() {
//        settingsPage = productsPage.pressSettingsBtn();
//        loginPage = settingsPage.logoutBtn();
    }
    @Test
    public void validateProductOnProductsPage(){
        SoftAssert sa = new SoftAssert();

        DeepLink.OpenAppWith("swaglabs://swag-overview/0,0");
        ProductsPage productsPage = new ProductsPage();

        String SLBTitle = productsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, strings.get("products_page_slb_title"));

        String SLBPrice = productsPage.getSLBPrice();
        sa.assertEquals(SLBPrice, strings.get("products_page_slb_price"));

        sa.assertAll();
    }
    @Test
    public void validateProductOnProductsDetailsPage(){
        SoftAssert sa = new SoftAssert();

        DeepLink.OpenAppWith("swaglabs://swag-overview/2");
        ProductsPage productsPage = new ProductsPage();

        productDetailsPage = productsPage.pressSLBTitle();
        String SLBTitle = productDetailsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, strings.get("product_details_page_slb_title"));

        String SLBTxt = productDetailsPage.getSLBText();
        sa.assertEquals(SLBTxt, strings.get("product_details_page_slb_txt"));

        if(getPlatform().equalsIgnoreCase("Android")) {
            String SLBPrice = productDetailsPage.scrollToSLBPriceAndGetSLBPrice();
            sa.assertEquals(SLBPrice, strings.get("product_details_page_slb_price"));
        }
        if(getPlatform().equalsIgnoreCase("iOS")) {
            productDetailsPage.scrollPage();
            sa.assertTrue(productDetailsPage.isAddToCartBtnDisplayed());
        }
        sa.assertAll();
    }
}

