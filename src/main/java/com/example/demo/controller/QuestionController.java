package com.example.demo.controller;
/*
 * Date: 1/7/21
 * Time: 8:15 AM
 * */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @GetMapping("/{theme}/all")
    public String allQuestions() {
        return "question/questions";
    }
}
