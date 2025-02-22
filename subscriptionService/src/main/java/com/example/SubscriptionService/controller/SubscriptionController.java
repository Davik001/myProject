package com.example.SubscriptionService.controller;


import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> create(@RequestBody SubscriptionCreateDTO dto) {
        return ResponseEntity.ok(service.createSubscription(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> update(@PathVariable Long id, @RequestBody SubscriptionUpdateDTO dto) {
        return ResponseEntity.ok(service.updateSubscription(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSubscriptionById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SubscriptionDTO>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getSubscriptionsByCustomerId(customerId));
    }
}