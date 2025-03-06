package com.example.SubscriptionService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crm-service", url = "${crm-service.url}")
public interface CrmCustomer {

    @GetMapping("/api/subscriptions/{customerId}")
    ResponseEntity<Void> getCustomer(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/subscriptions/customers/exists/{customerId}")
    ResponseEntity<Void> checkCustomerExists(@PathVariable("customerId") Long customerId);

    @GetMapping("/api/subscriptions/products/exists/{productId}")
    ResponseEntity<Void> checkProductExists(@PathVariable("productId") Long productId);
}
