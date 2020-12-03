package com.example.demo;
/*
 * Date: 12/2/20
 * Time: 9:29 PM
 * */

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestRestTemplate rest;

    @BeforeClass
    public static void setup() {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void testLoginAndRegister_HappyPath() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doRegistration("admin", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void testLoginAndRegister_Invalid() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doRegistration("admin", "12");
        assertEquals(registrationPageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void testLogin_HappyPath() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        doLogin("admin", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void testLoginPage_Invalid() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("this user does not exist", "and this password too");
        assertEquals(loginPageUrl() + "?error", browser.getCurrentUrl());
    }

    @Test
    public void testAdminPageAccess() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("user","12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    private void doLogin(String username, String password) {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);

        browser.findElementByCssSelector("form#loginForm").submit();
    }


    private void doRegistration(String username, String password) {
        browser.findElementByLinkText("Sign Up").click();

        assertEquals(registrationPageUrl(), browser.getCurrentUrl());

        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);
        browser.findElementByName("confirmPassword").sendKeys(password);

        browser.findElementByCssSelector("form#registerForm").submit();
    }


    private void assertLandedOnLoginPage() {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
    }

    private void clickLoginLink() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("a[id='login']").click();
    }


    /* URL helper methods */
    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private String loginPageUrl() {
        return homePageUrl() + "login";
    }

    private String registrationPageUrl() {
        return homePageUrl() + "registration";
    }

    // admin
    private String adminPageUrl() {
        return homePageUrl() + "admin";
    }

    // admin
    private String adminUserListPageUrl() {
        return homePageUrl() + "user-list";
    }

    // users
    private String newsPageUrl() {
        return homePageUrl() + "news";
    }


}
