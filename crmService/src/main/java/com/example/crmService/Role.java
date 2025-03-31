package com.example.crmService;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Админ"),
    MANAGER("Менеджер");

    private final String role;

    Role(String role) {
        this.role = name();
    }
}
