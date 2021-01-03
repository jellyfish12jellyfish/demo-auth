package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:03 AM
 * */

import com.example.demo.entity.Role;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTests extends HelperClass {

    private static HtmlUnitDriver browser;

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
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userService.save(user);
    }

    @Test
    public void testUpdateProfile_happyPath() {
        browser.get(homePageUrl());
        assertLandedOnHomePageUrl();
    }

    private void assertLandedOnHomePageUrl() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    private void assertLandedOnProfilePageUrl() {
        assertEquals(profilePageUrl(), browser.getCurrentUrl());
    }

    private void doLogin(String username, String password) {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);

        browser.findElementByCssSelector("form#loginForm").submit();
    }


}
