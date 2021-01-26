package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:03 AM
 * */

import com.example.demo.entity.User;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTests extends AbstractTestClass {


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

        // get user from DB
        User updatedUser = userService.findById(1L);
        assertEquals(updatedUser.getUsername(), "Jane");

    }

    @Test
    public void testUpdateUsername_Invalid() throws Exception {
        getProfilePage();

        // update username
        updateProfile("us", PASSWORD, PASSWORD);

        String errorMessage = browser.findElementById("c-error_username").getText();
        assertTrue(errorMessage.contains("Username must have between 3 and 20 characters"));
    }

    @Test
    public void testUpdatePassword_HappyPath() throws Exception {
        getProfilePage();

        // update password
        updateProfile(USERNAME, "new_password", "new_password");

        assertLandedOnLoginPage();

        doLogin(USERNAME, "new_password");
        assertLandedOnHomePage();
    }


    @Test
    public void testUpdatePassword_InvalidLength() throws Exception {
        getProfilePage();

        // update password
        updateProfile(USERNAME, "1", "1");

        // get error message
        String errorMessage = browser.findElementById("c-error_password").getText();
        assertTrue(errorMessage.contains("At least 8 characters"));
    }

    @Test
    public void testUpdatePassword_InvalidConfirmation() throws Exception {
        getProfilePage();

        // update password
        updateProfile(USERNAME, "12345678", "87654321");

        // get error message
        String errorMessage = browser.findElementById("confirmPasswordError").getText();
        assertTrue(errorMessage.contains("Passwords do not match"));
    }

    @Test
    public void testUpdateProfile_HappyPath() throws Exception {
        getProfilePage();

        // update username and password
        String newPassword = "mary12345678";
        String newUsername = "Mary";
        updateProfile(newUsername, newPassword, newPassword);

        assertLandedOnLoginPage();
        doLogin(newUsername, newPassword);
        assertLandedOnHomePage();

        // get username from navbar
        String usernameFromNavbar = browser.findElementByCssSelector("a[id=profileLink]").getText();
        assertEquals(newUsername, usernameFromNavbar);
    }


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
