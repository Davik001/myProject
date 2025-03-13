package com.example.SubscriptionService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crm-service", url = "${crm-service.url}")
public interface CrmCustomer {

    @GetMapping("/customers/{id}")
    ResponseEntity<Void> checkCustomerExists(@PathVariable("id") Long customerId);

    @GetMapping("/products/{id}")
    ResponseEntity<Void> checkProductExists(@PathVariable("id") Long productId);

}
