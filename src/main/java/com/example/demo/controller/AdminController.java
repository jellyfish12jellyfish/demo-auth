package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:13 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);


    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

        log.info(">>> GET user-list.html");
        return "admin/user-list";
    }

    // delete the user by id
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {

        log.info(">>> DELETE user by id: {}", userId);
        userService.deleteById(userId);

        log.info(">>> GET:redirect user-list.html");
        return "redirect:/admin/user-list";
    }


    @PostMapping("/update")
    public String setUserRoles(@RequestParam("userId") Long id,
                               @RequestParam(name = "formRoles", required = false, defaultValue = "") Set<String> formRoles,
                               Principal principal, HttpSession session, Model model) {

        User user = userService.findById(id);

        try {
            userService.setUserRoles(formRoles, user);
            log.info(">>> GET:redirect user-list.html");

            if (principal.getName().equals(user.getUsername())) {
                session.invalidate();
                log.info(">>> Invalidate session && GET:redirect login.html");
                return "redirect:/login";
            }

            log.info(">>> GET:redirect user-list.html");
            return "redirect:/admin/user-list";

        } catch (Exception exception) {
            log.error(">>> ERROR: " + exception);
            model.addAttribute("error", "Something went wrong!");

            log.info(">>> GET user-list.html");
            return "admin/user-list";
        }
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

}
