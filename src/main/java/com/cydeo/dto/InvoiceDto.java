package com.cydeo.dto;


import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    @DateTimeFormat(pattern = "MMMM-dd-yyyy")
    private LocalDate date;
    private CompanyDto company;

    @NotNull(message = "Client/Vendor name is required")
    private ClientVendorDto clientVendor;

    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
}
