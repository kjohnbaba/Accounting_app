package com.cydeo.controller;

import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public DashboardController(DashboardService dashboardService, InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.dashboardService = dashboardService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping(value = {})
    public String getCurrencyExchangeRate(Model model){
        List<String> exchangeRates = dashboardService.getCurrencyExchangeRate();
        model.addAttribute("exchangeRates",exchangeRates);
        model.addAttribute("invoiceProducts",invoiceProductService.getLast3InvoiceProductAndInvoices());
        model.addAttribute("summaryNumbers",invoiceProductService.findTotalCostAndTotalSalesAndTotalProfitAndLoss());

        return "dashboard";
    }
}
