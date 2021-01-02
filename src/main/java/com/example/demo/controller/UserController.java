package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class UserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/questions")
    public String getQuestionsPage() {

        log.info("> return 'quizzes' page");
        return "user/questions";
    }

    @GetMapping("/profile")
    public String getProfilePage(Principal principal, Model model) {

        // get user from the service
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        log.info("> return 'profile' page");

        return "user/profile";
    }

    @PostMapping("/save")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpSession session) {

        log.debug(">>> id = " + id);
        log.debug(">>> username = " + username);
        log.debug(">>> password = " + password);

        User candidate = userService.findById(id);

        candidate.setUsername(username);
        candidate.setPassword(password);

        userService.save(candidate);

        // destroy the user session
        session.invalidate();

        return "redirect:/login";
    }
}

