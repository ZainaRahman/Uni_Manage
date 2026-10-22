package com.example.uni_manage.security;

import com.example.uni_manage.entity.Admin;
import com.example.uni_manage.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username:admin}")
    private String username;

    @Value("${app.default-admin.password:admin123}")
    private String password;

    @Value("${app.default-admin.email:admin@uni.local}")
    private String email;

    public DefaultAdminInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            adminRepository.save(new Admin(null, username, passwordEncoder.encode(password), email));
            System.out.println("Created default admin account: " + username);
        }
    }
}
