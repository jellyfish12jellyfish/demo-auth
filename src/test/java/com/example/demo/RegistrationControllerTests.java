package com.example.demo;
/*
 * Date: 12/2/20
 * Time: 9:29 PM
 * */

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegistrationControllerTests {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

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

        String username = "newUser";
        String password = "12345678";

        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();

        doRegistration(username, password);
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
        doLogin(username, password);
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void testLoginAndRegister_Invalid() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doRegistration("admin", "12");
        assertEquals(registrationPageUrl(), browser.getCurrentUrl());

        String errorMessage = browser.findElementById("usernameError").getText();
        assertEquals(errorMessage, "A user with the same name already exists");
    }

    @Test
    public void testLogin_HappyPath() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
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

        String errorMessage = browser.findElementById("errorMsg").getText();
        assertEquals(errorMessage, "Invalid username or password");
    }

    @Test
    public void testLoginAndLogut() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("user", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("form#logoutForm").submit();
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }


    @Test
    public void testDoAdminLogin() throws Exception {
        successAdminLogin();
    }

    private void successAdminLogin() {
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("admin", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        clickAdminLink();
        assertLandedOnAdminPage();
        assertEquals("Admin page", browser.findElementByTagName("h1").getText());
    }

    private void clickAdminLink() {
        browser.findElementByCssSelector("a#adminLink").click();
        assertLandedOnAdminPage();
    }

    private void assertLandedOnAdminPage() {
        assertEquals(adminUserListPageUrl(), browser.getCurrentUrl());
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

    private String adminPageUrl() {
        return homePageUrl() + "admin";
    }

    private String adminUserListPageUrl() {
        return adminPageUrl() + "/user-list";
    }

}
