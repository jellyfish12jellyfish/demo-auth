package com.example.demo.controller;
/*
 * Date: 1/7/21
 * Time: 8:15 AM
 * */

import com.example.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private static final Logger log = LoggerFactory.getLogger(QuestionController.class.getName());

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{id}/all")
    public String allQuestions(@PathVariable("id") Long id, Model model) {
        model.addAttribute("questions", questionService.getThemeQuestions(id));

        log.info(">>> GET questions.html");
        return "question/questions";
    }
}
