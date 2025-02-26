package com.cydeo.controller;

import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.ChargeRequest;
import com.cydeo.service.CompanyService;
import com.cydeo.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CompanyService companyService;

    @Value("${stripe_public_key}")
    private String stripePublicKey;

    public PaymentController(PaymentService paymentService, CompanyService companyService) {
        this.paymentService = paymentService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "year", required = false) Integer year, Model model) {
        int selectedYear = (year == null) ? LocalDate.now().getYear() : year;
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(selectedYear));
        model.addAttribute("year", selectedYear);
        return "payment/payment-list";
    }

    @GetMapping("/newpayment/{id}")
    public String checkout(@PathVariable("id") Long id, Model model) {
        PaymentDto dto = paymentService.getPaymentById(id);
        model.addAttribute("payment", dto);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", "USD");
        model.addAttribute("monthId", id);
        return "payment/payment-method";
    }

    @PostMapping("/charge/{id}")
    public String charge(ChargeRequest chargeRequest, @PathVariable("id") Long id, Model model) throws StripeException {
        PaymentDto dto = paymentService.charge(chargeRequest, id);
        System.out.println(chargeRequest);
        model.addAttribute("chargeId", dto.getCompanyStripeId());
        model.addAttribute("description", dto.getDescription());
        return "payment/payment-result";
    }

    @GetMapping("/toInvoice/{id}")
    public String toInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("payment", paymentService.getPaymentById(id));
        model.addAttribute("company", companyService.getCompanyDtoByLoggedInUser());
        return "payment/payment-invoice-print";
    }
}