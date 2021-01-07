package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    void deleteById(Long id);

    void save(User user);

    void update(User user);

    User findByUsername(String username);

    List<UserRepository.NameAndLastLoginAt> recentUsers();

    void setUserRoles(Set<String> roles, User user);
}
