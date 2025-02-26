package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;


public interface InvoiceService {
    InvoiceDto findById(Long id);

    InvoiceDto update(InvoiceDto invoiceDto);

    BigDecimal sumTotal(InvoiceType invoiceType);

    InvoiceDto approve(Long invoiceId);

    void deleteInvoice(Long id);

    List<InvoiceDto> findLastThreeApprovedInvoice();

    BigDecimal sumProfitLoss();

    List<InvoiceDto> listInvoices(InvoiceType invoiceType);

    InvoiceDto saveInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType);

    InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType);

    InvoiceDto printInvoice(Long id);

}
