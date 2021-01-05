package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Set;

public interface UserService {

    public List<User> findAll();

    public User findById(Long id);

    public void deleteById(Long id);

    public void save(User user);

    public void update(User user);

    public User findByUsername(String username);

    public List<UserRepository.NameAndLastLoginAt> recentUsers();

    public void setUserRoles(Set<String> roles, User user);
}
