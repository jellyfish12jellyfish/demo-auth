package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfilePage(Principal principal, Model model) {

        // get user from the service
        model.addAttribute("user", userService.findByUsername(principal.getName()));

        log.info(">>> GET profile.html");
        return "user/profile";
    }

    @PostMapping("/save")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session,
                             @RequestParam("id") Long id) {

        User userFromDB = userService.findById(id);

        if (bindingResult.hasErrors()) {
            log.warn(">>> WARN: field has errors");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        if (!user.getUsername().equals(userFromDB.getUsername())
                && userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        user.setRoles(userFromDB.getRoles());
        user.setCreatedAt(userFromDB.getCreatedAt());
        userService.save(user);

        // destroy the session
        session.invalidate();

        log.info(">>> GET:redirect login.html");
        return "redirect:/login";
    }
}

