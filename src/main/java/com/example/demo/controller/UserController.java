package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quizzes")
public class UserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public String showNewsPage() {
        log.info("> show quizzes page controller");
        return "quizzes";
    }
}
