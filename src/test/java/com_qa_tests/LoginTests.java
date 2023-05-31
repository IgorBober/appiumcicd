package com_qa_tests;

import com_qa.BaseTest;
import com_qa_pages.LoginPage;
import com_qa_pages.ProductsPage;
import com_qa_utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    InputStream datais;
    JSONObject loginUsers;
    TestUtils utils = new TestUtils();

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

    }
    @AfterClass
    public void afterClass() {

    }
    @BeforeMethod
    public void beforeMethod(Method m) {
        System.out.println("login test before method");
        loginPage = new LoginPage();
        utils.log().info("\n" + "****** starting test:" + m.getName() + "******" + "\n");

    }
    @AfterMethod
    public void afterMethod() {
        System.out.println("login test after method");
    }
    @Test
    public void invalidUserName(){
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
        loginPage.pressLoginBtn();


        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = strings.get("err-invalid_username_or_password");
        System.out.println("actual error txt - " + actualErrTxt + "\n" + "expected error txt - " + expectedErrTxt);

        Assert.assertEquals(actualErrTxt, expectedErrTxt);
    }
    @Test
    public void invalidPassword(){
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginBtn();


        String actualErrTxt = loginPage.getErrTxt();
        String expectedErrTxt = strings.get("err-invalid_username_or_password");
        System.out.println("actual error txt - " + actualErrTxt + "\n" + "expected error txt - " + expectedErrTxt);

        Assert.assertEquals(actualErrTxt, expectedErrTxt);

    }
    @Test
    public void successfulLogin(){
        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginBtn();

        String actualProductTitle = productsPage.getTitle();
        String expectedProductTitle = strings.get("product_title");
        System.out.println("product title - " + actualProductTitle + "\n" + "expected error txt - " + expectedProductTitle);

        Assert.assertEquals(actualProductTitle, expectedProductTitle);

    }
}

