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
    public String list(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("students", studentService.getStudents());
        model.addAttribute("student", edit == null ? new Student() : studentService.getStudentById(edit));
        return "students";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Student student, RedirectAttributes redirect) {
        try {
            if (student.getId() == null) {
                studentService.insertStudent(student);
            } else {
                studentService.updateStudent(student.getId(), student);
            }
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("error", e.getReason());
        }
        return "redirect:/ui/students";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/ui/students";
    }
}
