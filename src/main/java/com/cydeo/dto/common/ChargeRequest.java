package com.cydeo.dto.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeRequest {
    private String stripeEmail;
    private String stripeToken;
    private BigDecimal amount;
}