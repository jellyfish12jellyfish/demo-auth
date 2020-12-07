package com.example.demo;
/*
 * Date: 12/6/20
 * Time: 6:47 PM
 * */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminControllerTest {

    private static HtmlUnitDriver browser;

    Wait<WebDriver> wait = new FluentWait<WebDriver>(browser)
            .withTimeout(Duration.ofSeconds(30))
            .pollingEvery(Duration.ofSeconds(5))
            .ignoring(NoSuchElementException.class);

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
    public void testDoAdminLogin() {
        successAdminLogin();
    }

    @Test
    public void testDeleteUserById() {
        successAdminLogin();
        browser.findElement(By.linkText("delete")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = browser.switchTo().alert();
        alert.accept();
    }

    private void successAdminLogin() {
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        clickLoginLink();
        assertLandedOnLoginPage();
        doLogin("admin", "12");
        assertEquals(homePageUrl(), browser.getCurrentUrl());
        clickAdminLink();
        assertLandedOnAdminPage();
        assertEquals("Admin page", browser.findElementByTagName("h1").getText());
    }

    private void clickAdminLink() {
        browser.findElementByCssSelector("a#adminLink").click();
        assertLandedOnAdminPage();
    }

    private void assertLandedOnAdminPage() {
        assertEquals(adminUserListPageUrl(), browser.getCurrentUrl());
    }

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private String loginPageUrl() {
        return homePageUrl() + "login";
    }

    // admin
    private String adminPageUrl() {
        return homePageUrl() + "admin";
    }

    // admin
    private String adminUserListPageUrl() {
        return adminPageUrl() + "/user-list";
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
