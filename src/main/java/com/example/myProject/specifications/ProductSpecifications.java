package com.example.myProject.specifications;

import com.example.myProject.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    // Фильтрация по имени товара
    public static Specification<Product> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null && !name.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            }
            return null;
        };
    }

    // Фильтрация по цене товара
    public static Specification<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            return null;
        };
    }

    // Метод для создания спецификации с фильтрами
    public static Specification<Product> getSpecification(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and(filterByName(name));
        }
        if (minPrice != null && maxPrice != null) {
            specification = specification.and(filterByPriceRange(minPrice, maxPrice));
        }

        return specification;
    }
}

