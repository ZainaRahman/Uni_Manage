package com.example.uni_manage.web;

import com.example.uni_manage.entity.Teacher;
import com.example.uni_manage.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/teachers")
public class TeacherPageController {

    private final TeacherService teacherService;

    public TeacherPageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @GetMapping
    public String list(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("teachers", teacherService.getTeachers());
        model.addAttribute("teacher", id == null ? new Teacher() : teacherService.getTeacherById(id));
        return "teachers";
    }


    @PostMapping
    public String create(@ModelAttribute Teacher teacher, RedirectAttributes redirect) {
        try {
            teacherService.insertTeacher(teacher);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/teachers";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Teacher teacher, RedirectAttributes redirect) {
        try {
            teacherService.updateTeacher(id, teacher);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/ui/teachers";
    }
}
