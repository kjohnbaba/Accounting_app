package com.cydeo.controller;

import com.cydeo.dto.ChargeRequest;
import com.cydeo.dto.PaymentDto;
import com.cydeo.service.PaymentService;
import com.cydeo.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Value("${stripe.public-key}")
    private String stripePublicKey;

    private final PaymentService paymentService;
    private final StripeService stripeService;

    public PaymentController(PaymentService paymentService, StripeService stripeService) {
        this.paymentService = paymentService;
        this.stripeService = stripeService;
    }

    @GetMapping("/list")
    public String listPayments(@RequestParam(required = false) Integer year, Model model) {

        model.addAttribute("payments", paymentService.getAllPaymentsGroupedByYear());
        Map<Integer, List<PaymentDto>> paymentsByYear = paymentService.getAllPaymentsGroupedByYear();

        Set<Integer> existingYears = paymentsByYear.keySet();
        year = existingYears.stream().findFirst().orElse(null);

        List<PaymentDto> payments = paymentsByYear.getOrDefault(year, List.of());
        model.addAttribute("years", existingYears);
        model.addAttribute("year", year);
        model.addAttribute("payments", payments);
        return "payment/payment-list";
    }

    @GetMapping("/newpayment/{id}")
    public String preparePayment(@PathVariable Long id, Model model) {

        PaymentDto payment = paymentService.findById(id);
        BigDecimal amountInCents = payment.getAmount().setScale(0, RoundingMode.HALF_UP);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("payment", payment);
        model.addAttribute("amount", amountInCents.intValue());
        model.addAttribute("currency", "USD");
        return "payment/payment-method";
    }

    @PostMapping("/charge/{id}")
    public String charge(@PathVariable Long id, @ModelAttribute ChargeRequest chargeRequest, Model model) throws StripeException {
        try {
            Charge charge = stripeService.charge(chargeRequest);
            System.out.println("CHARGE REQUEST: " + chargeRequest);
            System.out.println(charge);
            model.addAttribute("description", charge.getDescription());
            model.addAttribute("chargeId", charge.getId());
            paymentService.changePaymentStatus(id);
            return "payment/payment-result";
        } catch (StripeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/payments/newpayment/" + id;
        }
    }
}
