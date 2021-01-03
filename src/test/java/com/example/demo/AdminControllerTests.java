package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 9:04 AM
 * */

import com.example.demo.entity.User;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminControllerTests extends HelperClass {

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

        userService.update(test_user);

        doLogin(USERNAME, PASSWORD);

        browser.get(adminPageUrl());
        assertLandedOnAdminPage();

        assertTrue(browser.findElementById("adminPage").getText().contains("Admin"));
    }
}
