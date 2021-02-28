package com.example.demo.controller;
/*
 * Date: 1/7/21
 * Time: 8:00 AM
 * */

import com.example.demo.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/questions")
public class ThemeController {

    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public String getThemes(Model model) {
        model.addAttribute("themes", themeService.getThemes());

        log.info(">>> GET themes.html");
        return "theme/themes";
    }
}
