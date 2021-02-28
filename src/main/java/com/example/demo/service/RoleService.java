package com.example.demo.service;

import com.example.demo.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();

    Role getRoleByName(String name);
}
