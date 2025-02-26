package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {
    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> findAllByInvoiceIdAndCalculateTotalPrice(Long invoiceId);

    InvoiceProductDto add(InvoiceProductDto invoiceProductDto, Long invoiceId);

    InvoiceProductDto delete(Long id);

    void updateQuantityInStockSale(Long invoiceId);

    void checkForLowQuantityAlert(Long invoiceId);

    void updateQuantityInStockPurchase(Long invoiceId);

    void updateRemainingQuantityUponPurchaseApproval(Long invoiceId);

    void calculateProfitOrLoss(Long invoiceId);

    List<InvoiceProductDto> findAllApprovedInvoiceProductsOfCompany();
}
