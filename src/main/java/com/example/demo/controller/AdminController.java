package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:13 PM
 * */

import com.example.demo.entity.Theme;
import com.example.demo.service.QuestionService;
import com.example.demo.service.RoleService;
import com.example.demo.service.ThemeService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final ThemeService themeService;
    private final QuestionService questionService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, ThemeService themeService, QuestionService questionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.themeService = themeService;
        this.questionService = questionService;
    }

    // get admin page
    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("recentUsers", userService.recentUsers());
        log.info(">>> GET admin.html");
        return "admin/admin";
    }

    // get all users
    @GetMapping("/user-list")
    public String listUsers(Model model) {

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());

        log.info(">>> GET admin-admin-user-list.html");
        return "admin/admin-user-list";
    }

    // delete the user by id
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {

        log.info(">>> DELETE user by id: {}", userId);
        userService.deleteById(userId);

        log.info(">>> GET:redirect admin-user-list.html");
        return "redirect:/admin/user-list";
    }


    // update user roles
    @PostMapping("/update")
    public String setUserRoles(@RequestParam("userId") Long id,
                               @RequestParam(name = "formRoles", required = false, defaultValue = "") Set<String> formRoles,
                               Principal principal, HttpSession session) {

        userService.setUserRoles(formRoles, id);

        if (userService.selfUpdate(principal, id)) {
            session.invalidate();
            log.info(">>> Invalidate session && GET:redirect login.html");
            return "redirect:/login";
        }

        return "redirect:/admin/user-list";
    }

    // get users page
    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/admin-users";
    }

    // get themes page
    @GetMapping("/themes")
    public String getThemes(Model model) {
        model.addAttribute("themes", themeService.findAll());

        log.info(">>> GET admin-themes.html");
        return "admin/admin-themes";
    }

    // get questions page
    @GetMapping("/questions")
    public String getQuestions(Model model) {
        model.addAttribute("questions", questionService.findAll());

        log.info(">>> GET admin-questions.html");
        return "admin/admin-questions";
    }

    // delete the theme by id
    @GetMapping("/theme/delete")
    public String deleteTheme(@RequestParam("themeId") Long themeId) {
        themeService.deleteById(themeId);

        log.info(">>> GET:redirect admin-themes.html");
        return "redirect:/admin/themes";
    }

    // get update theme page
    @GetMapping("/theme")
    public String getUpdateThemePage(@RequestParam("themeId") Long themeId, Model model) {
        model.addAttribute("theme", themeService.getThemeById(themeId));
        return "theme/theme-form";
    }

    // get create new theme page
    @GetMapping("/theme/new")
    public String getCreateThemePage(Model model) {
        model.addAttribute("theme", new Theme());
        return "theme/theme-form";
    }

    // save a new theme or an updated theme
    @PostMapping("/theme/save")
    public String saveTheme(@Valid @ModelAttribute Theme theme, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "theme/theme-form";
        }
        themeService.save(theme);
        return "redirect:/admin/themes";
    }
}
