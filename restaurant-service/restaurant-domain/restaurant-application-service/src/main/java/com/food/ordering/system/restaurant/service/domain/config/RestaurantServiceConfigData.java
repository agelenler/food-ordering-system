package com.food.ordering.system.restaurant.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "restaurant-service")
public class RestaurantServiceConfigData {
    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;
}
