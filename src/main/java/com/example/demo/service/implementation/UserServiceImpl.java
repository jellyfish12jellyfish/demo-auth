package com.example.demo.service.implementation;
/*
 * Date: 12/2/20
 * Time: 6:28 PM
 * */

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // создаю константы для ролей
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";

    @Autowired
    private Environment env;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        log.info(">>> Get all users: {}", users);
        return users;
    }

    @Override
    public User getUserById(Long id) {
        log.info(">>> Get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not fine uesr id: " + id));
    }

    @Override
    public void deleteUserById(Long id) {
        log.info(">>> Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        log.info(">>> Update user: {}", user.getUsername());
        userRepository.save(user);
    }

    @Override
    public void setUserRoles(Set<String> roles, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        user.getRoles().clear();

        // если переданы 2 роли, мы назначаем их
        if (roles.size() == 2) {
            Set<Role> roleSet = new HashSet<>(roleService.getRoles());
            user.setRoles(roleSet);
        }

        // если только 1 роль и мн-во содержит ROLE_USER, назаначаем юзеру эту роль
        else if (roles.size() == 1 && roles.contains(USER))
            user.getRoles().add(roleService.getRoleByName(USER));

            // если только 1 роль и мн-во содержит ROLE_AMDIN, назаначаем юзеру эту роль
        else if (roles.size() == 1 && roles.contains(ADMIN))
            user.getRoles().add(roleService.getRoleByName(ADMIN));

        log.info(">>> Set roles and update");
        userRepository.save(user);
    }

    // если пользователь обновил свои данные, то возвращаем true
    @Override
    public boolean selfUpdate(Principal principal, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return principal.getName().equals(user.getUsername());
    }

    @Override
    public String updateProfile(@Valid User user, Long userId, BindingResult bindingResult, Model model, HttpSession session) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        // валидация вводимых данных
        if (bindingResult.hasErrors()) {
            log.warn(">>> WARN: field has errors");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // проверка нового пароля
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // проверяем, изменили ли юзернейм и не занято ли это новое значение кем-то другим
        if (!user.getUsername().equals(userFromDb.getUsername())
                && userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "A user with the same name already exists");
            log.info(">>> GET profile.html");
            return "user/profile";
        }

        // переносим старые данные
        user.setRoles(userFromDb.getRoles());
        user.setCreatedAt(userFromDb.getCreatedAt());

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        session.invalidate();

        log.info(">>> GET:redirect login.html");
        return "redirect:/login";
    }

    @Override
    public boolean checkFieldErrors(User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.warn(">>> WARN: field has errors");
            log.info(">>> GET registration.html");
            return true;
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            log.warn(">>> WARN: passwords do not match");

            log.info(">>> GET registration.html");
            return true;
        }

        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.warn(">>> WARN: username already exists");
            model.addAttribute("usernameError", "A user with the same name already exists");

            log.info(">>> GET registration.html");
            return true;
        }
        return false;
    }

    // registering user
    @Async
    @Override
    public void registerUser(User user, String siteUrl) {
        log.info(">>> Registering user: {}", user.getUsername());

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteUrl);
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String siteUrl) {
        String toAddress = user.getUsername();
        String fromAddress = env.getProperty("SPRING_MAIL_USERNAME");
        String senderName = "Demo Auth inc.";
        String subject = "Please verify your registration";

        String content = "Dear [[name]], <br>" +
                "Please click the link below to verify your registration:<br>" +
                "<h3><a href=\"[[URL]]\" target=\"_self\">Verify</a></h3><br>";

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(msg);

        try {
            assert fromAddress != null;
            msgHelper.setFrom(fromAddress, senderName);
            msgHelper.setTo(toAddress);
            msgHelper.setSubject(subject);

            content = content.replace("[[name]]", user.getUsername());
            String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyUrl);

            msgHelper.setText(content, true);
            javaMailSender.send(msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(">>> Something went wrong, check sendEmail method\n");
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyUser(String verificationCode) {
        User user = userRepository.findUserByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        log.info(">>> Get user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserRepository.NameAndLastLoginAt> recentUsers() {
        var recentUsers = userRepository.findAllByOrderByLastLoginAtDesc();
        log.info(">>> Get users by last login at field: {}", recentUsers.size());
        return recentUsers;
    }
}
