package com.example.uni_manage.repository;

import com.example.uni_manage.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
