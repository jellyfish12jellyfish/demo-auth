package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:13 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // get all users
    @GetMapping("/user-list")
    public String listUsers(Model model) {

        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    // delete the user by id
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {
        log.info("> userId = " + userId);
        log.info("> deleting the user");

        userService.deleteById(userId);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/update")
    public String updateUser(@RequestParam("userId") Long userId, Model model) {
        log.info("> getting user by id");
        log.info("> getting user-upate page");

        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("roles", roleService.findAll());
        return "user-update";
    }

    @PostMapping("/save")
    public String saveUser(
            @RequestParam("id") Long id,
            @RequestParam("username") String userName) {

        try {
            User user = userService.findById(id);

            log.info(">> user roles: " + user.getRoles());
            user.getRoles().clear();

            user.addRole(new Role(2L, "ROLE_ADMIN"));

            log.info(">> user roles: " + user.getRoles());

            userService.save(user);
            return "redirect:/";

        } catch (Exception e) {
            log.warn("??? something went wrong");
            System.out.println("??? error = " + e);
        }
        return "redirect:/admin/user-list";
    }

}
