package com.example.demo.config;
/*
 * Date: 1/6/21
 * Time: 9:35 AM
 * */

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class.getName());

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository) {

        return args -> {
            List<Role> roles = roleRepository.findAll();

            if (roles.isEmpty()) {
                log.info("Preloading ROLE_USER: " + (roleRepository.save(new Role(1L, "ROLE_USER"))));
                log.info("Preloading ROLE_ADMIN: " + (roleRepository.save(new Role(2L, "ROLE_ADMIN"))));
            }
        };
    }


    @Bean
    @Profile("test")
    CommandLineRunner initTestDatabase(RoleRepository roleRepository) {

        return args -> {
            log.info("Preloading ROLE_USER: " + (roleRepository.save(new Role(1L, "ROLE_USER"))));
            log.info("Preloading ROLE_ADMIN: " + (roleRepository.save(new Role(2L, "ROLE_ADMIN"))));
        };
    }
}
