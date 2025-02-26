package com.cydeo.controller;

import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;

    public DashboardController(DashboardService dashboardService, InvoiceService invoiceService) {
        this.dashboardService = dashboardService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", invoiceService.findLastThreeApprovedInvoice());
        model.addAttribute("exchangeRates", dashboardService.getCachedCurrencyDto());
        model.addAttribute("title", "Cydeo Accounting-Dashboard");
        return "dashboard";
    }

}
