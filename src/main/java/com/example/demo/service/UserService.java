package com.example.demo.service;
/*
 * Date: 12/2/20
 * Time: 6:28 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
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


    RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User '" + username + "' not found");

        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }

    public boolean saveNewUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
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
        if (roles.isEmpty()) {
            return false;

        } else {
            user.getRoles().clear();

            if (roles.size() == 2) {
                user.getRoles().add(roleService.findByName("ROLE_USER"));
                user.getRoles().add(roleService.findByName("ROLE_ADMIN"));

            } else if (roles.size() == 1 && roles.contains("ROLE_USER")) {
                user.getRoles().add(roleService.findByName("ROLE_USER"));

            } else if (roles.size() == 1 && roles.contains("ROLE_ADMIN")) {
                user.getRoles().add(roleService.findByName("ROLE_ADMIN"));
            }
            return true;
        }
    }

}
