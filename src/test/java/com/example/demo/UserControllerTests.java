package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:03 AM
 * */

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTests extends TestClass {


    @Test
    public void testUpdateProfile_happyPath() {
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

        // update username
        updateProfile("Jane", PASSWORD, PASSWORD);

        assertLandedOnLoginPage();

        doLogin("Jane", PASSWORD);
        assertLandedOnHomePage();

        // get username from navbar
        String usernameFromNavbar = browser.findElementByCssSelector("a[id=profileLink]").getText();
        assertEquals(usernameFromNavbar, "Jane");
    }
}
