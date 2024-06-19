package com.alexgiou.ecommerce.kafka.consumer;

import com.alexgiou.ecommerce.email.EmailService;
import com.alexgiou.ecommerce.kafka.order.OrderConfirmation;
import com.alexgiou.ecommerce.kafka.payment.PaymentConfirmation;
import com.alexgiou.ecommerce.notification.Notification;
import com.alexgiou.ecommerce.notification.NotificationRepository;
import com.alexgiou.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;


    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        log.info("Consuming payment confirmation: {}", paymentConfirmation);
        repository.save(Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
                .build());

        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        try {
            emailService.sendPaymentSuccessEmail(paymentConfirmation.customerEmail(), customerName, paymentConfirmation.amount(), paymentConfirmation.orderReference());
        } catch (MessagingException e) {
            log.error("Failed to send email notification for payment confirmation: {}", paymentConfirmation, e);
        }
    }


    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) {
        log.info("Consuming order confirmation: {}", orderConfirmation);
        repository.save(Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIRMATION)
                .notificationDate(LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
                .build());

        var customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();
        try {
            emailService.sendOrderConfirmationEmail(orderConfirmation.customer().email(), customerName, orderConfirmation.totalAmount(), orderConfirmation.orderReference(), orderConfirmation.products());
        } catch (MessagingException e) {
            log.error("Failed to send email notification for payment confirmation: {}", orderConfirmation, e);
        }
    }


}
