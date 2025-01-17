package com.example.myProject.repository;

import com.example.myProject.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email); // проверка на существование имейла
    Employee findByEmail(String email); // найти по имейлу

    @Query("SELECT e FROM Employee e WHERE " +
            "(:firstName IS NULL OR e.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR e.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR e.email LIKE %:email%) AND " +
            "(:role IS NULL OR e.role = :role)")
    Page<Employee> findAllWithFilters(String firstName, String lastName, String email, String role, Pageable pageable);
}
