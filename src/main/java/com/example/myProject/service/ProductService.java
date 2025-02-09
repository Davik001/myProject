package com.example.myProject.service;

import com.example.myProject.dto.create.ProductCreateDTO;
import com.example.myProject.dto.common.ProductResponseDTO;
import com.example.myProject.dto.update.ProductUpdateDTO;
import com.example.myProject.entity.Product;
import com.example.myProject.map.ProductMapper;
import com.example.myProject.repository.ProductRepository;
import com.example.myProject.specifications.ProductSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // создание
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = productMapper.toEntity(productCreateDTO);
        return productMapper.toResponseDto(productRepository.save(product));
    }

    // обновление
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProductDTO.getName());
                    product.setDescription(updatedProductDTO.getDescription());
                    product.setPrice(updatedProductDTO.getPrice());
                    return productMapper.toResponseDto(productRepository.save(product));
                })
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    // удалить
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

   // получение
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Page<ProductResponseDTO> getProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> specification = ProductSpecifications.getSpecification(name, minPrice, maxPrice);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(productMapper::toResponseDto);
    }
}
