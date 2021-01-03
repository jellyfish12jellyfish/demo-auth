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
        browser.get(homePageUrl());
        assertLandedOnHomePageUrl();
        browser.get(loginPageUrl());
    }

    private void assertLandedOnHomePageUrl() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    private void assertLandedOnProfilePageUrl() {
        assertEquals(profilePageUrl(), browser.getCurrentUrl());
    }


}
