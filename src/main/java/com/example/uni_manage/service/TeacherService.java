package com.example.uni_manage.service;


import com.example.uni_manage.entity.Teacher;

import com.example.uni_manage.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));
    }

    public void insertTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));

        existingTeacher.setName(teacher.getName());
        existingTeacher.setEmail(teacher.getEmail());

        teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Teacher with id " + id + " not found"));

        teacherRepository.delete(existingTeacher);
    }
}
