package com.example.demo;
/*
 * Date: 2/11/21
 * Time: 6:23 PM
 * */

import com.example.demo.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class MockTests {

    @Mock
    SpringTemplateEngine springTemplateEngine;

    @Mock
    SpringResourceTemplateResolver springResourceTemplateResolver;

    private final static String LOGIN_URL = "http://localhost/login";

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test /login with unauthenticated user")
    public void loginUnauthenticated() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test /questions with unauthenticated user returns login page")
    public void themesUnauthenticated() throws Exception {
        mvc.perform(get("/questions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_URL));
    }

    @Test
    @DisplayName("Test /question with authenticated user returns ok")
    @WithMockUser
    public void themesAuthenticated() throws Exception {
        mvc.perform(get("/questions"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test /profile with unauthenticated user returns login page")
    void profileUnatuhenticated() throws Exception {
        mvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_URL));
    }

    @Test
    @DisplayName("Test /admin with authenticated user returns 403")
    @WithMockUser
    public void userNotAdmin() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test /admin with authenticated admin returns ok")
    @WithMockUser(roles = "ADMIN")
    void userIsAdmin() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }
}
