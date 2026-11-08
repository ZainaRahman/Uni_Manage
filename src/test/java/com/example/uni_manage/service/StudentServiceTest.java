package com.example.uni_manage.service;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.security.UsernameChecker;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsernameChecker usernameChecker;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest=new StudentService(studentRepository,passwordEncoder,usernameChecker);
    }



    @Test
    void getStudents() {
        //when
        underTest.getStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById() {
        //given
        Long id=1L;
        Student existingStudent=new Student();
        existingStudent.setId(id);
        existingStudent.setName("Zaina");
        existingStudent.setRoll("101");
        existingStudent.setUsername("zaina");
        existingStudent.setPassword("zaina 21");

        given(studentRepository.findById(id)).willReturn(Optional.of(existingStudent));
        //when
        Student foundStudent=underTest.getStudentById(id);
        //then
        verify(studentRepository).findById(id);
        assertThat(foundStudent).isEqualTo(existingStudent);

    }

    @Test
    void caninsertStudent() {
        // given
        Student student = new Student();
        student.setUsername("suhita");
        student.setPassword("suhita52");

        given(passwordEncoder.encode("suhita52")).willReturn("encodedPassword52");

        // when
        underTest.insertStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent.getUsername()).isEqualTo("suhita");
        assertThat(capturedStudent.getPassword()).isEqualTo("encodedPassword52");
        assertThat(capturedStudent.getId()).isNull();
    }

    @Test
    void canUpdateStudent() {
        //given
        Long id=1L;
        Student existingStudent=new Student();
        existingStudent.setId(id);
        existingStudent.setName("Zaina");
        existingStudent.setRoll("101");
        existingStudent.setUsername("zaina");
        existingStudent.setPassword("zaina 21");
        
        Student updatedStudent=new Student();
        updatedStudent.setName("Zaina Rahman");
        updatedStudent.setRoll("102");
        updatedStudent.setUsername("zaina rahman");
        updatedStudent.setPassword("zaina 22");
        
        given(studentRepository.findById(id)).willReturn(Optional.of(existingStudent));
        given(passwordEncoder.encode("zaina 22")).willReturn("encodedPassword");

        //when
        underTest.updateStudent(id,updatedStudent);

        //then
        verify(usernameChecker).requireUserName("zaina rahman");
        verify(studentRepository).save(existingStudent);

        assertThat(existingStudent.getName()).isEqualTo("Zaina Rahman");
        assertThat(existingStudent.getRoll()).isEqualTo("102");
        assertThat(existingStudent.getUsername()).isEqualTo("zaina rahman");
        assertThat(existingStudent.getPassword()).isEqualTo("encodedPassword");



    }

    @Test
    void willDeleteStudent(){

        //given
        Long id=1L;
        Student existingStudent=new Student();
        existingStudent.setId(id);
        existingStudent.setName("Zaina");
        existingStudent.setRoll("101");
        existingStudent.setUsername("zaina");
        existingStudent.setPassword("zaina 21");

        given(studentRepository.findById(id)).willReturn(Optional.of(existingStudent));

        //when
        underTest.deleteStudent(id);

        //then
        verify(studentRepository).delete(existingStudent);
    }

    @Test
    void willThrowWhenStudentNotFoundOnDelete() {
        // given
        Long id = 1L;

        given(studentRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Student with id " + id + " not found");


        verify(studentRepository, never()).delete(any());
    }


    @Test
    void failing_getStudentByIdWrongMessage() {
        // given
        given(studentRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> underTest.getStudentById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Student not found");
    }


    @Test
    void failing_insertStudentKeepsPlainPassword() {
        // given
        Student student = new Student();
        student.setUsername("rafi");
        student.setPassword("rafi123");
        given(passwordEncoder.encode("rafi123")).willReturn("encodedRafi");

        // when
        underTest.insertStudent(student);

        // then
        assertThat(student.getPassword()).isEqualTo("rafi123");
    }
}