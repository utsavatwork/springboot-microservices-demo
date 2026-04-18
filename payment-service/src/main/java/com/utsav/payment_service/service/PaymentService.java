package com.utsav.payment_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.utsav.payment_service.dto.PaymentResponse;
import com.utsav.payment_service.entity.Payment;
import com.utsav.payment_service.entity.PaymentStatus;
import com.utsav.payment_service.event.InventoryReservedEvent;
import com.utsav.payment_service.event.PaymentDoneEvent;
import com.utsav.payment_service.repository.PaymentRepository;

@Service
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentEventProducer paymentEventProducer;

  public PaymentService(PaymentRepository paymentRepository, PaymentEventProducer paymentEventProducer) {
    this.paymentRepository = paymentRepository;
    this.paymentEventProducer = paymentEventProducer;
  }

  public List<PaymentResponse> getAllPayments() {
    return paymentRepository.findAll().stream().map(payment -> mapToPaymentResponse(payment)).toList();
  }

  public PaymentResponse createPaymentForOrder(InventoryReservedEvent event) {
    Payment newPayment = Payment.builder().orderId(event.orderId()).customerId(event.customerId())
        .amount(event.amount()).status(PaymentStatus.SUCCESS).build();
    Payment savedPayment = paymentRepository.save(newPayment);

    PaymentDoneEvent paymentDoneEvent = new PaymentDoneEvent(event.orderId(), event.amount());
    paymentEventProducer.publishPaymentDoneEvent(paymentDoneEvent);

    return mapToPaymentResponse(savedPayment);
  }

  private PaymentResponse mapToPaymentResponse(Payment payment) {
    return new PaymentResponse(
        payment.getId(),
        payment.getOrderId(),
        payment.getCustomerId(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getCreatedAt()
    );
  }
}
