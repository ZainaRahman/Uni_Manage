package com.example.uni_manage.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void unauthenticatedUserShouldGetUnauthorized() throws Exception {
            mockMvc.perform(get("/students").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized()); // 401
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void adminUserShouldGetOk() throws Exception {
            mockMvc.perform(get("/students"))
                    .andExpect(status().isOk()); // 200
        }

        @Test
        @WithMockUser(username = "teacher", roles = {"TEACHER"})
        void teacherUserShouldGetForbidden() throws Exception {
            mockMvc.perform(get("/students"))
                    .andExpect(status().isForbidden()); // 403
        }
    }

