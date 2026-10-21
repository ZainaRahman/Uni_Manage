package com.example.uni_manage.service;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;

import java.util.List;

public class StudentService {
    private static StudentRepository studentRepository = null;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public static Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Student with id " + id + " not found"));
    }

    public static void insertStudent(Student student) {
        studentRepository.save(student);
    }

}
