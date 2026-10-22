package com.example.uni_manage.service;


import com.example.uni_manage.entity.Teacher;

import com.example.uni_manage.repository.TeacherRepository;
import com.example.uni_manage.security.UsernameChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameChecker usernameChecker;

    public TeacherService(TeacherRepository teacherRepository,
                          PasswordEncoder passwordEncoder,
                          UsernameChecker usernameChecker) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameChecker = usernameChecker;
    }

    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));
    }

    public void insertTeacher(Teacher teacher) {
        usernameChecker.requireUserName(teacher.getUsername());
        UsernameChecker.requirePassword(teacher.getPassword());

        teacher.setId(null); // let the database generate the id
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword())); // store hash, not plain text
        teacherRepository.save(teacher);
    }

    public void updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));

        existingTeacher.setName(teacher.getName());
        existingTeacher.setEmail(teacher.getEmail());

        if (teacher.getUsername() != null
                && !teacher.getUsername().equals(existingTeacher.getUsername())) {
            usernameChecker.requireUserName(teacher.getUsername());
            existingTeacher.setUsername(teacher.getUsername());
        }

        // Change password only if a new one is sent; hash it before saving
        if (teacher.getPassword() != null && !teacher.getPassword().isBlank()) {
            existingTeacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }

        teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));

        teacherRepository.delete(existingTeacher);
    }
}
