package com.alexgiou.ecommerce.service;

import com.alexgiou.ecommerce.mapper.PaymentMapper;
import com.alexgiou.ecommerce.notification.NotificationProducer;
import com.alexgiou.ecommerce.notification.PaymentNotificationRequest;
import com.alexgiou.ecommerce.payment.PaymentRequest;
import com.alexgiou.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest paymentRequest) {
        var payment = paymentRepository.save(mapper.toPayment(paymentRequest));
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customer().firstName(),
                paymentRequest.customer().lastName(),
                paymentRequest.customer().email()
        ));
        return payment.getId();
    }
}
