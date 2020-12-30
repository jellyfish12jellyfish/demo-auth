package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:19 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class RegistrationController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    // get login page
    @GetMapping("/login")
    public String getLoginPage(Principal principal) {
        return principal == null ? "registration/login" : "home";
    }

    // get registration page
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user, Principal principal) {

        log.info("> return 'registration' page");
        return principal == null ? "registration/registration" : "home";
    }

    // do registration
    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors() || !user.getPassword().equals(user.getConfirmPassword()) || !userService.registerUser(user)) {
            log.warn("> field has errors");
            model.addAttribute("passwordError", "Passwords do not match");
            model.addAttribute("usernameError", "A user with the same name already exists");
            log.info("> return 'registration' page");

            return "registration/registration";
        }

        log.info("> saving the user to the DB");
        userService.registerUser(user);
        log.info("> redirect to 'home' page");

        return "redirect:/";
    }
}
