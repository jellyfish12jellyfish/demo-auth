package com.example.demo.service.implementation;
/*
 * Date: 12/2/20
 * Time: 6:28 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    // создаю константы для ролей
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.info(">>> Get all users: {}", users);
        return users;
    }

    @Override
    public User findById(Long id) {
        log.info(">>> Get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        log.info(">>> Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void save(User user) {
        log.info(">>> Save user: {}", user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        log.info(">>> Update user: {}", user.getUsername());
        userRepository.save(user);
    }

    @Override
    public void setUserRoles(Set<String> roles, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        user.getRoles().clear();

        // если переданы 2 роли, мы назначаем их
        if (roles.size() == 2) {
            Set<Role> roleSet = new HashSet<>(roleService.findAll());
            user.setRoles(roleSet);
        }

        // если только 1 роль и мн-во содержит ROLE_USER, назаначаем юзеру эту роль
        else if (roles.size() == 1 && roles.contains(USER))
            user.getRoles().add(roleService.findByName(USER));

            // если только 1 роль и мн-во содержит ROLE_AMDIN, назаначаем юзеру эту роль
        else if (roles.size() == 1 && roles.contains(ADMIN))
            user.getRoles().add(roleService.findByName(ADMIN));

        log.info(">>> Set roles and update");
        userRepository.save(user);
    }

    // если пользователь обновил свои данные, то возвращаем true
    @Override
    public boolean checkUser(Principal principal, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return principal.getName().equals(user.getUsername());
    }

    @Override
    public String updateProfile(@Valid User user, Long userId, BindingResult bindingResult, Model model, HttpSession session) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // валидация вводимых данных
        if (bindingResult.hasErrors()) {
            log.warn(">>> WARN: field has errors");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // проверка нового пароля
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // проверяем, изменили ли юзернейм и не занято ли это новое значение кем-то другим
        if (!user.getUsername().equals(userFromDb.getUsername())
                && userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // переносим старые данные
        user.setRoles(userFromDb.getRoles());
        user.setCreatedAt(userFromDb.getCreatedAt());

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        session.invalidate();

        log.info(">>> GET:redirect login.html");
        return "redirect:/login";
    }

    @Override
    public User findByUsername(String username) {
        log.info(">>> Get user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserRepository.NameAndLastLoginAt> recentUsers() {
        var recentUsers = userRepository.findAllByOrderByLastLoginAtDesc();
        log.info(">>> Get users by last login at field: {}", recentUsers.size());
        return recentUsers;
    }
}
