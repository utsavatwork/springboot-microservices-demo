package com.utsav.payment_service.controller;


import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utsav.payment_service.dto.PaymentResponse;
import com.utsav.payment_service.service.PaymentService;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GetMapping
  public List<PaymentResponse> getAllPayments() {
    return paymentService.getAllPayments();
  }
}
