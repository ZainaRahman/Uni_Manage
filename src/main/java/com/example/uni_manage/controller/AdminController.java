package com.example.uni_manage.controller;


import com.example.uni_manage.entity.Admin;
import com.example.uni_manage.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getAdmins() {
        return adminService.getAdmins();
    }

    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }

    @PostMapping
    public void addNewAdmin(@RequestBody Admin admin) {
        adminService.insertAdmin(admin);
    }

    @PutMapping("/{id}")
    public void updateAdmin(
            @PathVariable Long id,
            @RequestBody Admin admin) {
        adminService.updateAdmin(id, admin);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }
}
