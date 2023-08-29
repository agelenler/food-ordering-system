package com.food.ordering.system.order.service.dataaccess.outbox.payment.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentOutboxDataAccessMapper {

    public PaymentOutboxEntity orderPaymentOutboxMessageToOutboxEntity(OrderPaymentOutboxMessage
                                                                               orderPaymentOutboxMessage) {
        return PaymentOutboxEntity.builder()
                .id(orderPaymentOutboxMessage.getId())
                .sagaId(orderPaymentOutboxMessage.getSagaId())
                .createdAt(orderPaymentOutboxMessage.getCreatedAt())
                .type(orderPaymentOutboxMessage.getType())
                .payload(orderPaymentOutboxMessage.getPayload())
                .orderStatus(orderPaymentOutboxMessage.getOrderStatus())
                .sagaStatus(orderPaymentOutboxMessage.getSagaStatus())
                .outboxStatus(orderPaymentOutboxMessage.getOutboxStatus())
                .version(orderPaymentOutboxMessage.getVersion())
                .build();
    }

    public OrderPaymentOutboxMessage paymentOutboxEntityToOrderPaymentOutboxMessage(PaymentOutboxEntity
                                                                               paymentOutboxEntity) {
        return OrderPaymentOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .orderStatus(paymentOutboxEntity.getOrderStatus())
                .sagaStatus(paymentOutboxEntity.getSagaStatus())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }

}
