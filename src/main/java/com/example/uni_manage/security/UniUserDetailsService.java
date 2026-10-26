package com.example.uni_manage.security;

import com.example.uni_manage.repository.AdminRepository;
import com.example.uni_manage.repository.StudentRepository;
import com.example.uni_manage.repository.TeacherRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Tells Spring Security how to find a user when someone logs in.
@Service
public class UniUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public UniUserDetailsService(AdminRepository adminRepository,
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> buildUser(admin.getUsername(), admin.getPassword(), "ADMIN"))
                .or(() -> teacherRepository.findByUsername(username)
                        .map(teacher -> buildUser(teacher.getUsername(), teacher.getPassword(), "TEACHER")))
                .or(() -> studentRepository.findByUsername(username)
                        .map(student -> buildUser(student.getUsername(), student.getPassword(), "STUDENT")))
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    private UserDetails buildUser(String username, String hashedPassword, String role) {
        return User.withUsername(username)
                .password(hashedPassword)
                .roles(role)
                .build();
    }
}
