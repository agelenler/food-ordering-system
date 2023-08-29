package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.domain.event.payload.OrderPaymentEventPayload;
import com.food.ordering.system.kafka.consumer.KafkaSingleItemConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.messaging.DebeziumOp;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import debezium.order.payment_outbox.Envelope;
import debezium.order.payment_outbox.Value;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaSingleItemConsumer<Envelope> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                       PaymentMessagingDataMapper paymentMessagingDataMapper,
                                       KafkaMessageHelper kafkaMessageHelper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload Envelope message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {

        if (message.getBefore() == null && DebeziumOp.CREATE.getValue().equals(message.getOp())) {
            log.info("Incoming Message in PaymentRequestKafkaListener: {} with key: {}, partition: {} and offset: {} ",
                    message, key, partition, offset);
            Value paymentRequestAvroModel = message.getAfter();
            OrderPaymentEventPayload orderPaymentEventPayload =
                    kafkaMessageHelper.getOrderEventPayload(paymentRequestAvroModel.getPayload(), OrderPaymentEventPayload.class);
            try {
                if (PaymentOrderStatus.PENDING.name().equals(orderPaymentEventPayload.getPaymentOrderStatus())) {
                    log.info("Processing payment for order id: {}", orderPaymentEventPayload.getOrderId());
                    paymentRequestMessageListener.completePayment(paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(orderPaymentEventPayload, paymentRequestAvroModel));
                } else if (PaymentOrderStatus.CANCELLED.name().equals(orderPaymentEventPayload.getPaymentOrderStatus())) {
                    log.info("Cancelling payment for order id: {}", orderPaymentEventPayload.getOrderId());
                    paymentRequestMessageListener.cancelPayment(paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(orderPaymentEventPayload, paymentRequestAvroModel));
                }
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    //NO-OP for unique constraint exception
                    log.error("Caught unique constraint exception with sql state: {} " +
                                    "in PaymentRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), orderPaymentEventPayload.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                //NO-OP for PaymentNotFoundException
                log.error("No payment found for order id: {}", orderPaymentEventPayload.getOrderId());
            }
        }

    }
}
