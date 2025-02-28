package com.cydeo.controller;

import com.cydeo.service.InvoiceProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {

        InvoiceProductService invoiceProductService;

    public ReportingController(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/profitLossData")
    public String showProfitLossReport(Model model) {
        model.addAttribute("monthlyProfitLossDataMap", invoiceProductService.getMonthlyProfitLoss());
        return "report/profit-loss-report";
    }

    @GetMapping("/stockData")
    public String showStockData(Model model){
        model.addAttribute("invoiceProducts",invoiceProductService.getLast3InvoiceProductAndInvoices());
        return "report/stock-report";
    }
}
