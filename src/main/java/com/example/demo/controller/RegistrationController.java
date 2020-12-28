package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:19 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class RegistrationController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    // get login page
    @GetMapping("/login")
    public String login(Principal principal) {
        return principal == null ? "login" : "home";
    }

    // get registration page
    @GetMapping("/registration")
    public String showRegistrationPage(@ModelAttribute("user") User user, Principal principal) {

        log.info("> return 'registration' page");
        return principal == null ? "registration" : "home";
    }

    // do registration
    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          Model model) {

        // если поля содержат ошибки, вернуть ту же страницу и отобразить ошибки
        if (bindingResult.hasErrors()) {
            log.warn("> field has errors");

            log.info("> return 'registration' page");
            return "registration";
        }

        // если пароли не совпадают, вернуть ту же страницу и отображить 'passwordError' на странице
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            log.warn("> password error");
            model.addAttribute("passwordError", "Passwords do not match");

            log.info("> return 'registration' page");
            return "registration";
        }

        // registerUser вернул False, значит, такой пользователь уже зарегистрирован;
        // возращаем страницу и отображаем 'usernameError'
        if (!userService.registerUser(user)) {
            log.warn("> user already exists error");
            model.addAttribute("usernameError", "A user with the same name already exists");

            log.info("> return 'registration' page");
            return "registration";
        }

        log.info("> saving the user to the DB");
        // если все проверки пройдены, регистрируем нового юзера
        userService.registerUser(user);

        log.info("> redirect to 'home' page");
        return "redirect:/";
    }
}
