package com.alexgiou.ecommerce.repository;

import com.alexgiou.ecommerce.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
