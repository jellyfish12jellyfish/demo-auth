package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:19 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
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

        if (bindingResult.hasErrors()) {
            log.warn(">>> WARN: field has errors");
            log.info(">>> GET registration.html");
            return "registration/registration";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            log.warn(">>> WARN: passwords do not match");

            log.info(">>> GET registration.html");
            return "registration/registration";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            log.warn(">>> WARN: username already exists");
            model.addAttribute("usernameError", "A user with the same name already exists");

            log.info(">>> GET registration.html");
            return "registration/registration";
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userService.save(user);

        log.info(">>> GET:redirect login.html");
        return "redirect:/login";
    }
}
