package com.example.demo;
/*
 * Date: 12/2/20
 * Time: 9:29 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegistrationControllerTests {

    private static HtmlUnitDriver browser;

    private static final String USERNAME = "john";
    private static final String PASSWORD = "12345678";

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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

    @Before
    public void registerUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        userService.registerUser(user);
    }

    @Test
    public void testLoginAndRegister_HappyPath() throws Exception {

        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();

//        doRegistration(USERNAME, PASSWORD);
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
        doLogin(USERNAME, PASSWORD);
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void testLoginAndRegister_Invalid() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();

        doRegistration(USERNAME, "same_username_another_password");
        assertEquals(registrationPageUrl(), browser.getCurrentUrl());

        String errorMessage = browser.findElementById("usernameError").getText();
        assertEquals(errorMessage, "A user with the same name already exists");
    }

    @Test
    public void testLogin_HappyPath() throws Exception {
        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin(USERNAME, PASSWORD);
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

        doLogin(USERNAME, PASSWORD);

        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("form#logoutForm").submit();
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }


    @Test
    public void testDoAdminLogin() throws Exception {

        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());

        clickLoginLink();
        assertLandedOnLoginPage();

        assertEquals(loginPageUrl(), browser.getCurrentUrl());

        User test_user = userService.findByUsername(USERNAME);
        test_user.getRoles().clear();
        test_user.getRoles().add(roleService.findByName("ROLE_USER"));
        test_user.getRoles().add(roleService.findByName("ROLE_ADMIN"));
        userService.save(test_user);

        doLogin(USERNAME, PASSWORD);

        browser.get(adminPageUrl());
        assertLandedOnAdminPage();

        assertTrue(browser.findElementById("adminPage").getText().contains("Admin"));
    }


    private void assertLandedOnAdminPage() {
        assertEquals(adminPageUrl(), browser.getCurrentUrl());
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
}
