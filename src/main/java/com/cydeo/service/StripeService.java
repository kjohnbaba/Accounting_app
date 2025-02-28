package com.cydeo.service;

import com.cydeo.dto.ChargeRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface StripeService {
    Charge charge(ChargeRequest chargeRequest) throws StripeException;
}
