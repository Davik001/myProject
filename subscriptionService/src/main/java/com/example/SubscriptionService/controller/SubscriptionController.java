package com.example.SubscriptionService.controller;


import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Создание подписки
    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionCreateDTO dto) {
        SubscriptionDTO created = subscriptionService.createSubscription(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Обновление подписки
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable Long id,
                                                              @RequestBody SubscriptionUpdateDTO dto) {
        SubscriptionDTO updated = subscriptionService.updateSubscription(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Удаление подписки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    // Просмотр всех подписок
    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    // Просмотр подписки по ID
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable Long id) {
        SubscriptionDTO subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    // Просмотр подписок клиента
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionsByCustomer(@PathVariable Long customerId) {
        List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByCustomer(customerId);
        return ResponseEntity.ok(subscriptions);
    }

    // Создание дефолтной подписки (для CRM)
    @PostMapping("/default")
    public ResponseEntity<SubscriptionDTO> createDefaultSubscription(@RequestParam Long productId,
                                                                     @RequestParam Long customerId) {
        SubscriptionDTO created = subscriptionService.createDefaultSubscription(productId, customerId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}