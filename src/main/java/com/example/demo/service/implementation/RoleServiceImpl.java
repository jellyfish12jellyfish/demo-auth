package com.example.demo.service.implementation;
/*
 * Date: 12/6/20
 * Time: 6:34 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    // DI
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        log.info(">>> Get all roles: {}", roles.size());
        return roles;
    }

    @Override
    public Role findByName(String name) {
        log.info(">>> Get role by name: {}", name);
        return roleRepository.findByName(name);
    }
}
