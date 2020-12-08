package com.example.demo.controller;
/*
 * Date: 12/2/20
 * Time: 8:13 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @PostMapping("/upd")
    public String setUserRoles(@RequestParam("userId") Long id,
                               @RequestParam(name = "formRoles", required = false, defaultValue = "") Set<String> formRoles,
                               Model model) {
        try {
            User user = userService.findById(id);
            boolean candidate = userService.isCandidate(formRoles, user, roleService);

            if (candidate) {
                log.info("> updating the user");
                userService.save(user);
            } else
                log.info("> deleting the user by id: " + id);
                userService.deleteById(id);

        } catch (Exception exception) {
            log.warn(">>> ERROR >>> : " + exception);
            model.addAttribute("error", "Somethint went wrong!");
            return "user-list";
        }

        return "redirect:/admin/user-list";
    }


    @PostMapping("/save")
    public String saveUser(
            @RequestParam("id") Long id) {

        try {
            // https://ru.stackoverflow.com/questions/959711/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D0%B1%D0%BE%D1%80%D0%BE%D1%82%D1%8C-unsupportedoperationexception-null-%D0%B2-spring
            User user = userService.findById(id);
            user.getRoles().clear();
            user.getRoles().add(roleService.findById(2L));
            userService.save(user);

            return "redirect:/admin/user-list/";

        } catch (Exception e) {
            log.warn("??? something went wrong");
            System.out.println("??? error = " + e);
        }
        return "user-list";
    }

}
