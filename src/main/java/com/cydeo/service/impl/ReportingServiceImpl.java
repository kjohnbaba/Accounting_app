package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public ReportingServiceImpl(InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public List<InvoiceProductDto> findAllInvoiceProductsOfCompany() {
        return invoiceProductService.findAllApprovedInvoiceProductsOfCompany();
    }

    @Override
    public Map<String, BigDecimal> getMonthlyProfitLossMap() {
        Map<String, BigDecimal> monthlyProfitLoss = new LinkedHashMap<>();
        List<InvoiceProductDto> salesInvoiceProducts = invoiceService.listInvoices(InvoiceType.SALES).stream()
                .filter(invoice -> invoice.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .sorted(Comparator.comparing(InvoiceDto::getDate).reversed())
                .flatMap(invoice -> invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(invoice.getId())
                        .stream())
                .toList();
        for (InvoiceProductDto invoiceProduct : salesInvoiceProducts) {
            String key = invoiceProduct.getInvoice().getDate().getYear() + " "
                    + invoiceProduct.getInvoice().getDate().getMonth();
            BigDecimal profitLoss = invoiceProduct.getProfitLoss();
            monthlyProfitLoss.put(key, monthlyProfitLoss.getOrDefault(key, BigDecimal.ZERO).add(profitLoss));
        }
        return monthlyProfitLoss;
    }

}
