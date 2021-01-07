package com.example.demo.controller;
/*
 * Date: 1/7/21
 * Time: 8:00 AM
 * */

import com.example.demo.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class ThemeController {

    private final static Logger log = LoggerFactory.getLogger(ThemeController.class.getName());

    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public String getThemes(Model model) {
        model.addAttribute("themes", themeService.getAllThemes());

        log.info(">>> GET themes.html");
        return "theme/themes";
    }
}
