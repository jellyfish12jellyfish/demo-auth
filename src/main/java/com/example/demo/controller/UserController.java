package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 9:39 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class UserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

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
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             @RequestParam("id") Long id,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {

        User userFromDB = userService.findById(id);

        if (bindingResult.hasErrors()) {
            return "user/profile";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "user/profile";
        }

        if (!user.getUsername().equals(userFromDB.getUsername())
                && userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            return "user/profile";
        }

        user.setRoles(userFromDB.getRoles());
        user.setCreatedAt(userFromDB.getCreatedAt());
        userService.save(user);

        // destroy the session
        session.invalidate();

        return "redirect:/login";
    }
}

