package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:14 AM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class TestClass {

    static HtmlUnitDriver browser;

    static final String USERNAME = "john";
    static final String PASSWORD = "12345678";

    @LocalServerPort
    protected int port;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

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

    // creates the specific user for each test case
    @Before
    public void registerUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userService.save(user);
    }

    void assertLandedOnAdminPage() {
        assertEquals(adminPageUrl(), browser.getCurrentUrl());
    }


    void doLogin(String username, String password) {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());

        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);

        browser.findElementByCssSelector("form#loginForm").submit();
    }


    void doRegistration(String username, String password) {
        browser.findElementByLinkText("Sign Up").click();

        assertEquals(registrationPageUrl(), browser.getCurrentUrl());

        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);
        browser.findElementByName("confirmPassword").sendKeys(password);

        browser.findElementByCssSelector("form#registerForm").submit();
    }

    void updateProfile(String username, String password, String confirmPassword) {
        assertLandedOnProfilePage();

        // clear fields
        browser.findElementByName("username").clear();
        browser.findElementByName("password").clear();
        browser.findElementByName("confirmPassword").clear();

        // set new values
        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);
        browser.findElementByName("confirmPassword").sendKeys(confirmPassword);

        browser.findElementByCssSelector("form#updateForm").submit();
    }

    void assertLandedOnLoginPage() {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
    }

    void assertLandedOnHomePage() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    void assertLandedOnProfilePage() {
        assertEquals(profilePageUrl(), browser.getCurrentUrl());
    }

    void clickLoginLink() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("a[id='login']").click();
    }

    void clickProfileLink() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("a[id=profileLink]").click();
    }

    // url helper methods
    String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    String loginPageUrl() {
        return homePageUrl() + "login";
    }

    String registrationPageUrl() {
        return homePageUrl() + "registration";
    }

    String profilePageUrl() {
        return homePageUrl() + "profile";
    }

    String adminPageUrl() {
        return homePageUrl() + "admin";
    }
}
