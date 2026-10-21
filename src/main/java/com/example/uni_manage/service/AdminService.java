package com.example.uni_manage.service;

import com.example.uni_manage.entity.Admin;
import com.example.uni_manage.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
        adminRepository.save(admin);
    }

    public void updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Admin with id " + id + " not found"));

        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setPassword(admin.getPassword());
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
