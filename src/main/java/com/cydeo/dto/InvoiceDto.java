package com.cydeo.dto;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDto {
    Long id;

    private String invoiceNo;

    private InvoiceStatus invoiceStatus;

    private InvoiceType invoiceType;

//    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate date;

    @NotNull(message = "Please select a type")
    private ClientVendorDto clientVendor;

    private CompanyDto company;

    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
}
