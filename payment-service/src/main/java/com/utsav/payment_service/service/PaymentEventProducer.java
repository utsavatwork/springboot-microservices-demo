package com.utsav.payment_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.utsav.payment_service.event.PaymentDoneEvent;

@Service
public class PaymentEventProducer {
  private final KafkaTemplate<String, PaymentDoneEvent> kafkaTemplate;

  public PaymentEventProducer(KafkaTemplate<String, PaymentDoneEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishPaymentDoneEvent(PaymentDoneEvent event) {
    kafkaTemplate.send("payment-done", event);
  }
}
