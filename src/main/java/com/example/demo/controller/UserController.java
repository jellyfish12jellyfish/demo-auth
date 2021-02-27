package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserQuestionService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    private final UserQuestionService userQuestionService;

    @Autowired
    public UserController(UserService userService, UserQuestionService userQuestionService) {
        this.userService = userService;
        this.userQuestionService = userQuestionService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model) {

        if (principal != null) {
            try {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                model.addAttribute("userQuestions", userQuestionService.findAllByUserId(user.getUser().getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "home";
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

        return userService.updateProfile(user, id, bindingResult, model, session);
    }
}

