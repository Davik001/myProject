package com.example.crmService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crm-service", url = "http://localhost:7092")
public interface Crm {
    @GetMapping("/customer/{id}")
    ResponseEntity<Void> checkCustomerExists(@PathVariable("id") Long customerId);

    @GetMapping("/products/{id}")
    ResponseEntity<Void> checkProductExists(@PathVariable("id") Long productId);
}
