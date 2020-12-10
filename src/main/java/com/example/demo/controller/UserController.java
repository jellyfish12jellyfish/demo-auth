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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/quizzes")
    public String showNewsPage() {

        log.info("> return 'quizzes' page");
        return "quizzes";
    }

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));

        log.info("> return 'profile' page");
        return "profile";
    }
}
