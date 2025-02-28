package com.cydeo.dto;

import com.cydeo.enums.Month;
import com.cydeo.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDto {

    private Long id;
    private Month month;
    private int year;
    private BigDecimal amount;
    private PaymentStatus status;
}
