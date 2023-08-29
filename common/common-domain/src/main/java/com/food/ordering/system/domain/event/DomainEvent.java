package com.food.ordering.system.domain.event;

public interface DomainEvent<T> {
    void fire();
}
