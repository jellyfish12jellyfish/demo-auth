package com.example.demo;
/*
 * Date: 12/2/20
 * Time: 9:29 PM
 * */

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationControllerTests extends TestClass {


    @Test
    public void testLoginAndRegister_HappyPath() throws Exception {

        browser.get(homePageUrl());
        clickLoginLink();
        assertLandedOnLoginPage();

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

}
