package com.example.myProject.specifications;

import com.example.myProject.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {
    public static Specification<Employee> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                firstName == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<Employee> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                lastName == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
    }

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<Employee> hasRole(String role) {
        return (root, query, criteriaBuilder) ->
                role == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("role"), role);
    }
}
