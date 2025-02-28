package com.cydeo.entity;

import com.cydeo.enums.Month;
import com.cydeo.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="payments")
public class Payment{

    @Id
    private Long id;

  @Enumerated(EnumType.STRING)
    private Month month;
    private int year;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}


