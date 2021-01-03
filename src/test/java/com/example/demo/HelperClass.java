package com.example.demo;
/*
 * Date: 1/3/21
 * Time: 8:14 AM
 * */

import org.springframework.boot.web.server.LocalServerPort;

public abstract class HelperClass {

    static final String USERNAME = "john";
    static final String PASSWORD = "12345678";

    @LocalServerPort
    protected int port;

    String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    String loginPageUrl() {
        return homePageUrl() + "login";
    }

    String registrationPageUrl() {
        return homePageUrl() + "registration";
    }

    String profilePageUrl() {
        return homePageUrl() + "profile";
    }

    String adminPageUrl() {
        return homePageUrl() + "admin";
    }
}
