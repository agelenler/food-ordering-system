package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    private static final int MAX_EXECUTION = 100;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper) {
        this.paymentRequestHelper = paymentRequestHelper;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        processPayment(paymentRequestHelper::persistPayment, paymentRequest, "completePayment");
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
       processPayment(paymentRequestHelper::persistCancelPayment, paymentRequest, "cancelPayment");
    }

    private void processPayment(Function<PaymentRequest, Boolean> func, PaymentRequest paymentRequest, String methodName) {
        int execution = 1;
        boolean result;
        do {
            log.info("Executing {} operation for {} time for order id {}", methodName, execution, paymentRequest.getOrderId());
            try {
                result = func.apply(paymentRequest);
                execution++;
            } catch (OptimisticLockingFailureException e) {
                log.warn("Caught OptimisticLockingFailureException exception in {} with message {}!. Retrying for order id {}!",
                        methodName, e.getMessage(), paymentRequest.getOrderId());
                result = false;
            }
        } while(!result && execution < MAX_EXECUTION);

        if (!result) {
            throw new PaymentApplicationServiceException("Could not complete " + methodName + " operation. Throwing exception!");
        }
    }

}
