package com.example.demo.service;
/*
 * Date: 12/2/20
 * Time: 6:28 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

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
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void setUserRoles(Set<String> roles, User user) {

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

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserRepository.NameAndLastLoginAt> recentUsers() {
        return userRepository.findAllByOrderByLastLoginAtDesc();
    }
}
