package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:19 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // get login page
    @GetMapping("/login")
    public String getLoginPage(Principal principal) {
        log.info(">>> GET login.html");
        return principal == null ? "registration/login" : "home";
    }

    // get registration page
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user, Principal principal) {

        log.info(">>> GET registration.html");
        return principal == null ? "registration/registration" : "home";
    }

    // do registration
    @PostMapping("/registration")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          Model model) {

        boolean fieldHasErrors = userService.checkFieldErrors(user, bindingResult, model);

        if (fieldHasErrors) {
            return "registration/registration";
        }

        userService.registerUser(user);

        log.info(">>> GET:redirect login.html");
        return "redirect:/login";
    }

}
