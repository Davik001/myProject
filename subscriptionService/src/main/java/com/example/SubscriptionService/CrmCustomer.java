package com.example.SubscriptionService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crm-service", url = "${crm-service.url:http://crm-service}")
public interface CrmCustomer {

    @GetMapping("/api/customers/{customerId}")
    ResponseEntity<Void> getCustomer(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/products/{productId}")
    ResponseEntity<Void> getProduct(@PathVariable("productId") Long productId);

    @GetMapping("/api/products/{productId}")
    ResponseEntity<Void> checkProductExists(@PathVariable("productId") Long productId);

    @GetMapping("/api/customers/{customerId}")
    ResponseEntity<Void> checkCustomerExists(@PathVariable("customerId") Long customerId);
}