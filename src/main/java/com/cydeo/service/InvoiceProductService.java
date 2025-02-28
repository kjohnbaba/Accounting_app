package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface InvoiceProductService {

    InvoiceProductDto findInvoiceProductById(Long id);

    Map<String,BigDecimal> findTotalCostAndTotalSalesAndTotalProfitAndLoss();

    List<InvoiceProductDto> getLast3InvoiceProductAndInvoices();

    void saveInvoiceProduct(InvoiceProductDto dto);

    List<InvoiceProductDto> listAllByInvoiceIdAndCalculateTotalPrice(Long id);

    void deleteInvoiceProduct(Long invoice, Long invoiceProduct);

    Map<String, BigDecimal> getMonthlyProfitLoss();

}
