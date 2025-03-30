package com.example.SubscriptionService.exception;

public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(String message) {
        super(message);
    }

    public CustomEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
