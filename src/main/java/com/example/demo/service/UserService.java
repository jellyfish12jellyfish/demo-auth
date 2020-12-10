package com.example.demo.service;
/*
 * Date: 12/2/20
 * Time: 6:28 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    // создаю константы для ролей
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userFromDB = userRepository.findByUsername(username);

        if (userFromDB == null) // если пользователь не зарегистрирован, то выбросить исключение
            throw new UsernameNotFoundException("User '" + username + "' not found");

        return userFromDB;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }

    public boolean registerUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, USER)));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public boolean isCandidate(Set<String> roles, User user, RoleService roleService) {
        // при получении пустого мн-ва возвращаем 0
        if (roles.isEmpty()) {
            return false;

        } else {
            // очищаем роли
            user.getRoles().clear();

            // если переданы 2 роли, мы назначаем их
            if (roles.size() == 2) {
                user.getRoles().add(roleService.findByName(USER));
                user.getRoles().add(roleService.findByName(ADMIN));

                // если только 1 роль и мн-во содержит ROLE_USER, назаначаем юзеру эту роль
            } else if (roles.size() == 1 && roles.contains(USER)) {
                user.getRoles().add(roleService.findByName(USER));
                // если только 1 роль и мн-во содержит ROLE_AMDIN, назаначаем юзеру эту роль
            } else if (roles.size() == 1 && roles.contains(ADMIN)) {
                user.getRoles().add(roleService.findByName(ADMIN));
            }
            return true;
        }
    }

}
