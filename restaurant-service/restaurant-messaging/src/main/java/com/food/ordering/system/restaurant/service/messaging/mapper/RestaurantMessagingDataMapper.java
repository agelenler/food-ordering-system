package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.event.payload.OrderApprovalEventPayload;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.outbox.model.OrderEventPayload;
import debezium.order.restaurant_approval_outbox.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApproval(OrderApprovalEventPayload orderApprovalEventPayload,
                                                           Value restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(orderApprovalEventPayload.getRestaurantId())
                .orderId(orderApprovalEventPayload.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(orderApprovalEventPayload
                        .getRestaurantOrderStatus()))
                .products(orderApprovalEventPayload.getProducts()
                        .stream().map(avroModel ->
                                Product.builder()
                                        .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(orderApprovalEventPayload.getPrice())
                .createdAt(Instant.parse(restaurantApprovalRequestAvroModel.getCreatedAt()))
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload orderEventPayload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderEventPayload.getOrderId())
                .setRestaurantId(orderEventPayload.getRestaurantId())
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
