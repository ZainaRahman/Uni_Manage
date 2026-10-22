package com.example.uni_manage.service;

import com.example.uni_manage.entity.Admin;
import com.example.uni_manage.repository.AdminRepository;
import com.example.uni_manage.security.UsernameChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameChecker usernameChecker;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        UsernameChecker usernameChecker) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameChecker = usernameChecker;
    }

    public List<Admin>getAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Admin with id " + id + " not found"));
    }

    public void insertAdmin(Admin admin) {
        usernameChecker.requireUserName(admin.getUsername());
        UsernameChecker.requirePassword(admin.getPassword());

        admin.setId(null);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    public void updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Admin with id " + id + " not found"));


        if (admin.getUsername() != null
                && !admin.getUsername().equals(existingAdmin.getUsername())) {
            usernameChecker.requireUserName(admin.getUsername());
            existingAdmin.setUsername(admin.getUsername());
        }

        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        existingAdmin.setEmail(admin.getEmail());

        adminRepository.save(existingAdmin);
    }

    public void deleteAdmin(Long id) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Admin with id " + id + " not found"));

        adminRepository.delete(existingAdmin);
    }

}
