package com.example.demo.security;
/*
 * Date: 1/4/21
 * Time: 1:28 PM
 * */

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userFromDB = userRepository.findByUsername(username);

        if (userFromDB == null) // если пользователь не зарегистрирован, то выбросить исключение
            throw new UsernameNotFoundException("User '" + username + "' not found");

        userRepository.setLastLoginTime(username);
        log.info(">>> update login at timestamp");
        return new CustomUserDetails(userFromDB);
    }
}
