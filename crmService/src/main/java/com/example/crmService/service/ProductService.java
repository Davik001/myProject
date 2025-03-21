package com.example.crmService.service;


import com.example.crmService.dto.common.ProductResponseDTO;
import com.example.crmService.dto.create.ProductCreateDTO;
import com.example.crmService.dto.update.ProductUpdateDTO;
import com.example.crmService.entity.Product;
import com.example.crmService.kafka.CrmKafkaProducer;
import com.example.crmService.kafka.ProductEvent;
import com.example.crmService.map.ProductMapper;
import com.example.crmService.repository.ProductRepository;
import com.example.crmService.specifications.ProductSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CrmKafkaProducer kafkaProducer;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          CrmKafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = productMapper.toEntity(productCreateDTO);
        Product savedProduct = productRepository.save(product);

        // Отправляем событие в Kafka
        ProductEvent productEvent = new ProductEvent(savedProduct.getId(), "Продукт создан");
        kafkaProducer.sendProductEvent(productEvent);

        return productMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updatedProductDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(updatedProductDTO.getName());
        product.setDescription(updatedProductDTO.getDescription());
        product.setPrice(updatedProductDTO.getPrice());
        Product updatedProduct = productRepository.save(product);

        // Отправляем событие в Kafka
        ProductEvent productEvent = new ProductEvent(updatedProduct.getId(), "Цена продукта изменена на " + updatedProduct.getPrice());
        kafkaProducer.sendProductEvent(productEvent);

        return productMapper.toResponseDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);

        // Отправляем событие в Kafka
        ProductEvent productEvent = new ProductEvent(id, "Продукт удалён");
        kafkaProducer.sendProductEvent(productEvent);
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
