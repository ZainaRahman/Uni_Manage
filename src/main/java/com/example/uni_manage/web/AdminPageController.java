package com.example.uni_manage.web;

import com.example.uni_manage.entity.Admin;
import com.example.uni_manage.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/admins")
public class AdminPageController {

    private final AdminService adminService;

    public AdminPageController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping
    public String list(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("admins", adminService.getAdmins());
        model.addAttribute("admin", id == null ? new Admin() : adminService.getAdminById(id));
        return "admins";
    }

    @PostMapping
    public String create(@ModelAttribute Admin admin, RedirectAttributes redirect) {
        try {
            adminService.insertAdmin(admin);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/admins";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Admin admin, RedirectAttributes redirect) {
        try {
            adminService.updateAdmin(id, admin);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/admins";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return "redirect:/ui/admins";
    }
}
