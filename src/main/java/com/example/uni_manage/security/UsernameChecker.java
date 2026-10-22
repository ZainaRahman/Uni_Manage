package com.example.uni_manage.security;

import com.example.uni_manage.repository.AdminRepository;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UsernameChecker {

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public UsernameChecker(AdminRepository adminRepository,
                           TeacherRepository teacherRepository,
                           StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public boolean isTaken(String username) {
        return adminRepository.existsByUsername(username)
                || teacherRepository.existsByUsername(username)
                || studentRepository.existsByUsername(username);
    }

    public void requireUserName(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
        }
        if (isTaken(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "username '" + username + "' is already taken");
        }
    }

    public static void requirePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is required");
        }
    }
}
