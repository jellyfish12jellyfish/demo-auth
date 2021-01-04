package com.example.demo.service;

import com.example.demo.entity.Role;

import java.util.List;

public interface RoleService {

    public List<Role> findAll();

    public Role findByName(String name);
}
