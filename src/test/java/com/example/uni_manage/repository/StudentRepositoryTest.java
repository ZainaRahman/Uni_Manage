package com.example.uni_manage.repository;

import com.example.uni_manage.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {
    @Autowired
    private StudentRepository underTest;


    @Test
    void itShouldCheckIfStudentexistsByUsername() {
        // given
        String username = "suhita islam";
        Student student = new Student();
        student.setUsername(username);
        student.setPassword("suhita 52");
        underTest.save(student);

        // when
        boolean exists = underTest.existsByUsername(username);

        // then
        assertThat(exists).isTrue();
    }
}