package com.example.uni_manage.service;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.security.UsernameChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameChecker usernameChecker;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          UsernameChecker usernameChecker) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameChecker = usernameChecker;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));
    }

    public void insertStudent(Student student) {
        usernameChecker.requireUserName(student.getUsername());
        UsernameChecker.requirePassword(student.getPassword());

        student.setId(null);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);
    }

    public void updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));

        existingStudent.setName(student.getName());
        existingStudent.setRoll(student.getRoll());

        if (student.getUsername() != null
                && !student.getUsername().equals(existingStudent.getUsername())) {
            usernameChecker.requireUserName(student.getUsername());
            existingStudent.setUsername(student.getUsername());
        }

        if (student.getPassword() != null && !student.getPassword().isBlank()) {
            existingStudent.setPassword(passwordEncoder.encode(student.getPassword()));
        }

        studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));

        studentRepository.delete(existingStudent);
    }
}
