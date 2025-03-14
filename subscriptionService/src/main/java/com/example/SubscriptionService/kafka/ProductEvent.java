package com.example.SubscriptionService.kafka;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductEvent {
    Long productId;
    String details;

    public void setDetails(String details) {
        this.details = details;
    }
    public String getDetails() {
        return details;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getProductId() {
        return productId;
    }
}
