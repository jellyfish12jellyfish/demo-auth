package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:03 AM
 * */

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTests extends TestClass {


    @Test
    public void testUpdateUsername_HappyPath() throws Exception {
        getProfilePage();

        // update username
        updateProfile("Jane", PASSWORD, PASSWORD);

        assertLandedOnLoginPage();

        doLogin("Jane", PASSWORD);
        assertLandedOnHomePage();

        // get username from navbar
        String usernameFromNavbar = browser.findElementByCssSelector("a[id=profileLink]").getText();
        assertEquals(usernameFromNavbar, "Jane");
    }

    @Test
    public void testUpdateUsername_Invalid() throws Exception {
        getProfilePage();

        // update username
        updateProfile("us", PASSWORD, PASSWORD);

        String errorMessage = browser.findElementById("c-error_username").getText();
        assertTrue(errorMessage.contains("Username must have between 3 and 20 characters"));
    }

    // todo testUpdatePassword_HappyPath
    // todo testUpdatePassword_Invalid
    // todo testUpdateProfile_HappyPath
    // todo testUpdateProfile_Invalid


    public void getProfilePage() {
        // get home page
        browser.get(homePageUrl());
        assertLandedOnHomePage();

        // get login page
        browser.get(loginPageUrl());
        assertLandedOnLoginPage();

        doLogin(USERNAME, PASSWORD);
        assertLandedOnHomePage();

        // get profile page
        clickProfileLink();
        assertLandedOnProfilePage();
    }
}
