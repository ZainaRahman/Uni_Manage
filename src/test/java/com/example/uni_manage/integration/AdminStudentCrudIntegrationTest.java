package com.example.uni_manage.integration;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"})
class AdminStudentCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void adminCanCreateReadUpdateStudent() throws Exception {


        String newStudentJson = """
                {
                  "name": "Crud Student",
                  "roll": "501",
                  "username": "cruduser",
                  "password": "crudPassword"
                }
                """;

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStudentJson))
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        Student created = studentRepository.findByUsername("cruduser").orElseThrow();
        Long id = created.getId();
        assertThat(created.getName()).isEqualTo("Crud Student");
        assertThat(created.getPassword()).startsWith("$2a$");


        mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Crud Student"))
                .andExpect(jsonPath("$.roll").value("501"))
                .andExpect(jsonPath("$.username").value("cruduser"))
                .andExpect(jsonPath("$.password").doesNotExist());


        String updateJson = """
                {
                  "name": "Updated Student",
                  "roll": "502",
                  "username": "cruduser"
                }
                """;

        mockMvc.perform(put("/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        Student updated = studentRepository.findById(id).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Student");
        assertThat(updated.getRoll()).isEqualTo("502");


    }
}
