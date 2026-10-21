package com.example.uni_manage.service;

import com.example.uni_manage.entity.Student;
import com.example.uni_manage.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));
    }

    public void insertStudent(Student student) {
        studentRepository.save(student);
    }

    public void updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));

        existingStudent.setName(student.getName());
        existingStudent.setRoll(student.getRoll());

        studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with id " + id + " not found"));

        studentRepository.delete(existingStudent);
    }
}