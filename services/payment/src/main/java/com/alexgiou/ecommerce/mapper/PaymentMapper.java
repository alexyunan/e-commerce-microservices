package com.alexgiou.ecommerce.mapper;

import com.alexgiou.ecommerce.payment.Payment;
import com.alexgiou.ecommerce.payment.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {


    public Payment toPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .id(paymentRequest.id())
                .amount(paymentRequest.amount())
                .paymentMethod(paymentRequest.paymentMethod())
                .orderId(paymentRequest.orderId())
                .build();

    }
}
