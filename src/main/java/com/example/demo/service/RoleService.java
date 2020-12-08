package com.example.demo.service;
/*
 * Date: 12/6/20
 * Time: 6:34 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }


    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException(">> role not found: " + id));
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
