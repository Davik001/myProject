package com.example.SubscriptionService.map;

import com.example.SubscriptionService.dto.alldtos.SubscriptionDTO;
import com.example.SubscriptionService.dto.create.SubscriptionCreateDTO;
import com.example.SubscriptionService.dto.update.SubscriptionUpdateDTO;
import com.example.SubscriptionService.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    // Сущность -> DTO
    SubscriptionDTO toDto(Subscription subscription);

    // DTO -> Сущность (для создания)
    @Mapping(target = "id", ignore = true) // ID генерируется автоматически
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Subscription toEntity(SubscriptionCreateDTO dto);

    // DTO -> Сущность (для обновления)
    @Mapping(target = "id", ignore = true) // Не трогаем ID при обновлении
    @Mapping(target = "createdAt", ignore = true) // Не трогаем дату создания
    Subscription toEntity(SubscriptionUpdateDTO dto);
}