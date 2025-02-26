package com.cydeo.dto;

import com.cydeo.enums.Months;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto {
    private Long id;
    @DateTimeFormat(pattern = "MMMM dd, yyyy")
    private LocalDate paymentDate;
    private Integer year;
    private Months month;
    private BigDecimal amount;
    private boolean isPaid;
    private String companyStripeId;
    private String description;
    private CompanyDto company;
}
