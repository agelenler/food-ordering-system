package com.food.ordering.system.domain.valueobject;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderPreferences(
        List<String> removeIngredients,
        List<String> addIngredients,
        SpiceLevel spiceLevel,
        String specialInstructions,
        String deliveryInstructions
) {
}
