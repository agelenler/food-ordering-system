package com.food.ordering.system.payment.service.dataaccess.outbox.mapper;

import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxEntity orderOutboxMessageToOutboxEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .paymentStatus(orderOutboxMessage.getPaymentStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
    }

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity paymentOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .paymentStatus(paymentOutboxEntity.getPaymentStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }

}
