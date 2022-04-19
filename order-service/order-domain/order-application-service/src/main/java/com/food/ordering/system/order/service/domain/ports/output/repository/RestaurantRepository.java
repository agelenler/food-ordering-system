package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
