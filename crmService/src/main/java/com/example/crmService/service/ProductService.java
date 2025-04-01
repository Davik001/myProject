package com.example.crmService.service;


import com.example.crmService.dto.common.ProductResponseDTO;
import com.example.crmService.dto.create.ProductCreateDTO;
import com.example.crmService.dto.update.ProductUpdateDTO;
import com.example.crmService.entity.Product;
import com.example.crmService.kafka.CrmKafkaProducer;
import com.example.shared.EventType;
import com.example.shared.ProductEvent;
import com.example.crmService.map.ProductMapper;
import com.example.crmService.repository.ProductRepository;
import com.example.crmService.specifications.ProductSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CrmKafkaProducer kafkaProducer;

    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = productMapper.toEntity(productCreateDTO);
        Product savedProduct = productRepository.save(product);

        Map<String, String> details = new HashMap<>();
        details.put("message", "Продукт создан");

        kafkaProducer.sendProductEvent(savedProduct.getId(), EventType.CREATED, details);
        return productMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updatedProductDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Map<String, String> details = new HashMap<>();
        if (!product.getPrice().equals(updatedProductDTO.getPrice())) {
            details.put("Старая цена", product.getPrice().toString());
            details.put("Новая цена", updatedProductDTO.getPrice().toString());
        }

        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setPrice(updatedProductDTO.getPrice());
        Product updatedProduct = productRepository.save(product);

        kafkaProducer.sendProductEvent(updatedProduct.getId(), EventType.UPDATE, details);
        return productMapper.toResponseDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);

        Map<String, String> details = new HashMap<>();
        details.put("message", "Продукт удалён");

        kafkaProducer.sendProductEvent(id, EventType.DELETE, details);
    }

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
