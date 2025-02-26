package com.cydeo.service;


import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.ChargeRequest;
import com.stripe.exception.*;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year);

    PaymentDto getPaymentById(Long id);

    PaymentDto charge(ChargeRequest chargeRequest, Long id) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException, CardException;
}
