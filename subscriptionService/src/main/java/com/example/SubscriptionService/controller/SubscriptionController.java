package com.example.SubscriptionService.controller;


import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Создание подписки
    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionCreateDTO dto) {
        logger.info("Запрос на создание подписки: {}", dto);
        SubscriptionDTO created = subscriptionService.createSubscription(dto);
        logger.info("Подписка успешно создана: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Обновление подписки
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(@PathVariable Long id,
                                                              @RequestBody SubscriptionUpdateDTO dto) {
        logger.info("Запрос на обновление подписки с ID {}: {}", id, dto);
        SubscriptionDTO updated = subscriptionService.updateSubscription(id, dto);
        logger.info("Подписка с ID {} успешно обновлена: {}", id, updated);
        return ResponseEntity.ok(updated);
    }

    // Удаление подписки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        logger.info("Запрос на удаление подписки с ID {}", id);
        subscriptionService.deleteSubscription(id);
        logger.info("Подписка с ID {} успешно удалена", id);
        return ResponseEntity.noContent().build();
    }

    // Просмотр всех подписок
    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        logger.info("Запрос на получение всех подписок");
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();
        logger.info("Получено {} подписок", subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }

    // Просмотр подписки по ID
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionById(@PathVariable Long id) {
        logger.info("Запрос на получение подписки с ID {}", id);
        SubscriptionDTO subscription = subscriptionService.getSubscriptionById(id);
        logger.info("Получена подписка с ID {}: {}", id, subscription);
        return ResponseEntity.ok(subscription);
    }

    // Просмотр подписок клиента
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionsByCustomer(@PathVariable Long customerId) {
        logger.info("Запрос на получение подписок клиента с ID {}", customerId);
        List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByCustomer(customerId);
        logger.info("Получено {} подписок для клиента с ID {}", subscriptions.size(), customerId);
        return ResponseEntity.ok(subscriptions);
    }

    // Создание дефолтной подписки (для CRM)
    @PostMapping("/default")
    public ResponseEntity<SubscriptionDTO> createDefaultSubscription(@RequestParam Long productId,
                                                                     @RequestParam Long customerId) {
        logger.info("Запрос на создание дефолтной подписки для продукта с ID {} и клиента с ID {}", productId, customerId);
        SubscriptionDTO created = subscriptionService.createDefaultSubscription(productId, customerId);
        logger.info("Дефолтная подписка успешно создана: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
