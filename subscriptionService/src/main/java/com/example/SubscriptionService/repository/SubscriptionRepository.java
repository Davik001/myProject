package com.example.SubscriptionService.repository;

import com.example.SubscriptionService.EventType;
import com.example.SubscriptionService.entity.Subscription;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCustomerId(Long customerId);
    List<Subscription> findByProductId(Long productId);
    List<Subscription> findByProductIdAndEventType(Long productId, EventType eventType);
}
