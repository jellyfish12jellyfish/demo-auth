package com.example.demo.controller;
/*
 * Date: 1/3/21
 * Time: 9:04 AM
 * */

import com.example.demo.AbstractTestClass;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminControllerTests extends AbstractTestClass {

    @Before
    public void setAdminRole() {
        User user = userService.getUserByUsername(USERNAME);
        Role role_admin = roleService.getRoleByName( "ROLE_ADMIN");
        user.getRoles().add(role_admin);
        userService.updateUser(user);
    }

    @Test
    public void testDoAdminLogin() throws Exception {

        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());

        clickLoginLink();
        assertLandedOnLoginPage();

        assertEquals(loginPageUrl(), browser.getCurrentUrl());

        doLogin(USERNAME, PASSWORD);

        browser.get(adminPageUrl());
        assertLandedOnAdminPage();

        assertTrue(browser.findElementById("adminPage").getText().contains("Admin"));
    }
}
