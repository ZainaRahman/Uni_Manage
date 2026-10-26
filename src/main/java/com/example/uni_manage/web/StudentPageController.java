package com.example.uni_manage.web;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/students")
public class StudentPageController {

    private final StudentService studentService;

    public StudentPageController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping
    public String list(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("students", studentService.getStudents());
        model.addAttribute("student",id == null ? new Student() : studentService.getStudentById(id));
        return "students";
    }


    @PostMapping
    public String create(@ModelAttribute Student student, RedirectAttributes redirect) {
        try {
            studentService.insertStudent(student);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/students";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Student student, RedirectAttributes redirect) {
        try {
            studentService.updateStudent(id, student);
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/ui/students";
    }
}
