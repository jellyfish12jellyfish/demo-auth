package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/questions")
    public String showNewsPage() {

        log.info("> return 'quizzes' page");
        return "questions";
    }

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam("username") String username, Model model) {

        model.addAttribute("user", userService.findByUsername(username));
        log.info("> return 'profile' page");

        return "profile";
    }
}

