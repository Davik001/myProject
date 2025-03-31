package com.example.SubscriptionService;

import com.example.SubscriptionService.dto.alldtos.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crm-service", url = "${crm-service.url}")
public interface CrmFeignClient {

    @GetMapping("/customer/{id}")
    ResponseEntity<Void> checkCustomerExists(@PathVariable("id") Long customerId);

    @GetMapping("/products/{id}")
    ResponseEntity<Void> checkProductExists(@PathVariable("id") Long productId);

    @GetMapping("/products/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Long id);
}
