package com.example.uni_manage.controller;


import com.example.uni_manage.entity.Student;
import com.example.uni_manage.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/students")
public class StudentController {
    @GetMapping
    public List<Student> getStudents() {
        return  StudentService.getStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return StudentService.getStudentById(id);
    }

    @PostMapping
    public void addNewStudent(@RequestBody Student student) {
        StudentService.insertStudent(student);
    }

}
