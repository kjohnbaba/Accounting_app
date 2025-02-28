package com.cydeo.service;

import com.cydeo.dto.PaymentDto;
import com.cydeo.entity.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    Map<Integer, List<PaymentDto>> getAllPaymentsGroupedByYear();
    PaymentDto findById(long parseLong);
    void changePaymentStatus(long id);
}
