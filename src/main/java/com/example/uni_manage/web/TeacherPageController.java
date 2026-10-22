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
    public String list(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("teachers", teacherService.getTeachers());
        model.addAttribute("teacher", edit == null ? new Teacher() : teacherService.getTeacherById(edit));
        return "teachers";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Teacher teacher, RedirectAttributes redirect) {
        try {
            if (teacher.getId() == null) {
                teacherService.insertTeacher(teacher);
            } else {
                teacherService.updateTeacher(teacher.getId(), teacher);
            }
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/teachers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/ui/teachers";
    }
}
