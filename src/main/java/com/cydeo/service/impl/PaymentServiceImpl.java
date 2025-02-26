package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.PaymentDto;
import com.cydeo.dto.common.ChargeRequest;
import com.cydeo.entity.Company;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.PaymentService;
import com.cydeo.util.MapperUtil;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    @Value("${stripe_secret_key}")
    private String stripeSecretKey;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public List<PaymentDto> getAllPaymentsByYear(int year) {
        var company = companyService.getCompanyDtoByLoggedInUser();
        List<Payment> payments = paymentRepository.findAllByYearAndCompany_Id(year, company.getId());
        if (payments.isEmpty()){
            createPayments(year);
        }
        payments = paymentRepository.findAllByYearAndCompany_Id(year, company.getId());
        return payments.stream()
                .map(payment -> mapperUtil.convert(payment, new PaymentDto()))
                .sorted(Comparator.comparing(PaymentDto::getMonth))
                .toList();
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Subscription with this month does not exist."));
        return mapperUtil.convert(payment, new PaymentDto());
    }

    @Override
    public PaymentDto charge(ChargeRequest chargeRequest, Long id) throws APIConnectionException, APIException,
                                                                          AuthenticationException, InvalidRequestException,
                                                                          CardException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Subscription with this month does not exist."));
        Stripe.apiKey = stripeSecretKey;
        String description = "Cydeo accounting subscription fee for : " + payment.getYear() + " " + payment.getMonth().getValue();
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", payment.getAmount().intValue());
        chargeParams.put("currency", "USD");
        chargeParams.put("description", description);
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);    // https://stripe.com/docs/api/charges?lang=java
        if (!charge.getStatus().equals("succeeded")){
           throw new IllegalArgumentException("Transaction failed. Please try again!");
        }
        payment.setPaid(true);
        payment.setCompanyStripeId(charge.getId());
        payment.setPaymentDate(LocalDate.now());
        PaymentDto dto = mapperUtil.convert(paymentRepository.save(payment), new PaymentDto());
        dto.setDescription(charge.getDescription());
        return dto;
    }

    private void createPayments(int year) {
        CompanyDto company = companyService.getCompanyDtoByLoggedInUser();
//        String[] months = DateFormatSymbols.getInstance().getMonths();
        for (Months month : Months.values()) {
            Payment payment = new Payment();
            payment.setMonth(month);
            payment.setYear(year);
            payment.setPaid(false);
            payment.setAmount(BigDecimal.valueOf(250));
            payment.setCompany(mapperUtil.convert(company, new Company()));
            paymentRepository.save(payment);
        }
    }
}