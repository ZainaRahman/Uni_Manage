package com.example.uni_manage.integration;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.service.StudentService;
import jakarta.persistence.EntityManager;
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

        @Autowired
        private EntityManager entityManager;

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


            assertThat(savedStudent.getPassword()).isNotEqualTo("plainPassword123");
            assertThat(savedStudent.getPassword()).startsWith("$2a$");
        }

        @Test
        void itShouldUpdateStudentInDatabase() {
            // given
            Student student = new Student();
            student.setUsername("updateuser");
            student.setPassword("oldPassword");
            student.setName("Old Name");
            student.setRoll("301");
            studentService.insertStudent(student);

            Long id = studentRepository.findByUsername("updateuser").orElseThrow().getId();
            String oldHash = studentRepository.findById(id).orElseThrow().getPassword();

            Student changes = new Student();
            changes.setName("New Name");
            changes.setRoll("302");
            changes.setUsername("updateuser2");
            changes.setPassword("newPassword");

            // when
            studentService.updateStudent(id, changes);
            entityManager.flush();
            entityManager.clear();

            // then
            Student updated = studentRepository.findById(id).orElseThrow();
            assertThat(updated.getName()).isEqualTo("New Name");
            assertThat(updated.getRoll()).isEqualTo("302");
            assertThat(updated.getUsername()).isEqualTo("updateuser2");
            assertThat(updated.getPassword()).isNotEqualTo(oldHash);
            assertThat(updated.getPassword()).startsWith("$2a$");
        }



        @Test
        void failing_itShouldInsertTwoStudentsWithSameUsername() {
            // given
            Student first = new Student();
            first.setUsername("sameuser");
            first.setPassword("pass1");
            first.setName("Student A");
            first.setRoll("201");

            Student second = new Student();
            second.setUsername("sameuser");
            second.setPassword("pass2");
            second.setName("Student B");
            second.setRoll("202");

            // when
            studentService.insertStudent(first);
            studentService.insertStudent(second);
            // then (never reached)
            assertThat(studentRepository.existsByUsername("sameuser")).isTrue();
        }
    }

