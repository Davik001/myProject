package com.example.myProject.repository;

import com.example.myProject.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email); // проверка на существование имейла
    Employee findByEmail(String email); // найти по имейлу
    Employee getById(long id); // получить айди

    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
}
