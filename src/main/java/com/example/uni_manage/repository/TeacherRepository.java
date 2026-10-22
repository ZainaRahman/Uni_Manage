package com.example.uni_manage.repository;

import com.example.uni_manage.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByUsername(String username);
    boolean existsByUsername(String username);
}
