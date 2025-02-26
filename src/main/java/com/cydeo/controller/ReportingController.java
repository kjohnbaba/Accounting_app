package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportingController {


    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/stockData")
    public String showStockReports(Model model) {
        List<InvoiceProductDto> invoiceProductList = reportingService.findAllInvoiceProductsOfCompany();
        model.addAttribute("invoiceProducts", invoiceProductList);
        return "report/stock-report";
    }

    @ExecutionTime
    @GetMapping("/profitLossData")
    public String getProfitLossReport(Model model) {
        model.addAttribute("monthlyProfitLossDataMap", reportingService.getMonthlyProfitLossMap());
        return "report/profit-loss-report";
    }
}
