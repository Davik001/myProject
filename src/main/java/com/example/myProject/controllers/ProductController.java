package com.example.myProject.controllers;

import com.example.myProject.dto.create.ProductCreateDTO;
import com.example.myProject.dto.common.ProductResponseDTO;
import com.example.myProject.dto.update.ProductUpdateDTO;
import com.example.myProject.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@Tag(name = "Товары", description = "Управление товарами")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Создать товар", description = "Добавляет новый товар в систему")
    @ApiResponse(responseCode = "201", description = "Товар успешно создан")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductCreateDTO productCreateDTO) {

        logger.info("Creating product : {}", productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreateDTO));
    }

    @Operation(summary = "Обновить товар", description = "Обновляет информацию о товаре по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO productUpdateDTO) {

        logger.info("Updating product : {}", productUpdateDTO);
        try {
            return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
        } catch (Exception e) {
            logger.error("Error, products with id {} not found : {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Удалить товар", description = "Удаляет товар по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Товар успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Long id) {

        logger.info("Deleting product : {}", id);
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error, products with id {} not found : {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Получить товар по ID", description = "Возвращает информацию о товаре")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID товара", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("Getting product : {}", id);

        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (Exception e) {
            logger.error("Error, products with id {} not found : {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Получить список товаров", description = "Возвращает список товаров с фильтрацией, сортировкой и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список товаров успешно получен")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @Parameter(description = "Фильтр по названию", example = "Телефон")
            @RequestParam(required = false) String name,

            @Parameter(description = "Минимальная цена", example = "100.00")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Максимальная цена", example = "1000.00")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Параметры пагинации и сортировки")
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        logger.info("Getting products with filter : name: {}, page: {}", name, pageable);
        return ResponseEntity.ok(productService.getProducts(name, minPrice, maxPrice, pageable));
    }
}
