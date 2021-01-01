package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // instead of using the data.sql script
    @Bean
    InitializingBean addRoles() {
        return () -> {
            Role role_user = roleRepository.findByName("ROLE_USER");
            Role role_admin = roleRepository.findByName("ROLE_ADMIN");

            if (role_user == null && role_admin == null) {
                roleRepository.save(new Role(1L, "ROLE_USER"));
                roleRepository.save(new Role(2L, "ROLE_ADMIN"));
            }

        };
    }

    @Bean
    @Profile("test")
    InitializingBean addRolesAndAdmin() {
        return () -> {
            roleRepository.save(new Role(1L, "ROLE_USER"));
            roleRepository.save(new Role(2L, "ROLE_ADMIN"));
        };
    }

}
