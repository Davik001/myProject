package com.example.SubscriptionService.repository;

import com.example.SubscriptionService.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCustomerId(Long customerId);
    List<Subscription> findByProductId(Long productId);
}
