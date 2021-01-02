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

        userRepository.setLastLoginTime(username);
        return userFromDB;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean isCandidate(Set<String> roles, User user, RoleService roleService) {

        /*
         * user.getRoles.addRole(role)
         * https://ru.stackoverflow.com/questions/959711/%D0%9A%D0%B0%D0%BA-%D0%BF%D0%BE%D0%B1%D0%BE%D1%80%D0%BE%D1%82%D1%8C-unsupportedoperationexception-null-%D0%B2-spring
         * */

        // при получении пустого мн-ва возвращаем false
        if (roles.isEmpty()) {
            return false;

        } else {
            // очищаем роли
            user.getRoles().clear();

            // если переданы 2 роли, мы назначаем их
            if (roles.size() == 2 && (roles.contains(USER) && roles.contains(ADMIN))) {
                user.getRoles().add(roleService.findByName(USER));
                user.getRoles().add(roleService.findByName(ADMIN));

                // если только 1 роль и мн-во содержит ROLE_USER, назаначаем юзеру эту роль
            } else if (roles.size() == 1 && roles.contains(USER)) {
                user.getRoles().add(roleService.findByName(USER));

                // если только 1 роль и мн-во содержит ROLE_AMDIN, назаначаем юзеру эту роль
            } else if (roles.size() == 1 && roles.contains(ADMIN)) {
                user.getRoles().add(roleService.findByName(ADMIN));
            }
            // если роли назначены - вернуть true
            return true;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserRepository.NameAndLastLoginAt> recentUsers() {
        return userRepository.findAllByOrderByLastLoginAtDesc();
    }

}
