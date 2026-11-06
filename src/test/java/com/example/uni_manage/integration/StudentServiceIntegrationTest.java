package com.example.uni_manage.integration;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
class StudentServiceIntegrationTest {

        @Autowired
        private StudentService studentService;

        @Autowired
        private StudentRepository studentRepository;

        @Test
        void itShouldInsertStudentAndHashPassword() {
            // given
            Student student = new Student();
            student.setUsername("zainaservice");
            student.setPassword("plainPassword123");
            student.setName("Zaina Rahman");
            student.setRoll("102");

            // when
            studentService.insertStudent(student);

            // then
            Student savedStudent = studentRepository.findByUsername("zainaservice").orElseThrow();

            // Verify password is encrypted with BCrypt ($2a$ or $2b$ prefix)
            assertThat(savedStudent.getPassword()).isNotEqualTo("plainPassword123");
            assertThat(savedStudent.getPassword()).startsWith("$2a$");
        }
    }

