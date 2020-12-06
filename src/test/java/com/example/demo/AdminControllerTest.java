package com.example.demo;
/*
 * Date: 12/6/20
 * Time: 6:47 PM
 * */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminControllerTest {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestRestTemplate rest;

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


    @Test
    public void testDoAdminRegistration() {
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("admin", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
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

    // admin
    private String adminPageUrl() {
        return homePageUrl() + "admin";
    }

    // admin
    private String adminUserListPageUrl() {
        return homePageUrl() + "user-list";
    }


    private void doLogin(String username, String password) {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
        browser.findElementByName("username").sendKeys(username);
        browser.findElementByName("password").sendKeys(password);

        browser.findElementByCssSelector("form#loginForm").submit();
    }

    private void clickLoginLink() {
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        browser.findElementByCssSelector("a[id='login']").click();
    }

    private void assertLandedOnLoginPage() {
        assertEquals(loginPageUrl(), browser.getCurrentUrl());
    }
}
