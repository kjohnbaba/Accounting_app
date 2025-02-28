package com.cydeo.service.impl;

import com.cydeo.dto.PaymentDto;
import com.cydeo.entity.Payment;
import com.cydeo.enums.PaymentStatus;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.PaymentService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    MapperUtil mapperUtil;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Map<Integer, List<PaymentDto>> getAllPaymentsGroupedByYear() {
        List<Payment> allPayments = paymentRepository.findAll();
        Map<Integer, List<PaymentDto>> map = allPayments.stream()
                .map(each -> mapperUtil.convert(each, new PaymentDto()))
                .collect(Collectors.groupingBy(PaymentDto::getYear));
        return map;
    }

    @Override
    public PaymentDto findById(long id) {
        Payment item = paymentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(item, new PaymentDto());

    }

    @Override
    public void changePaymentStatus(long id) {
        Payment item = paymentRepository.findById(id).get();
        item.setStatus(PaymentStatus.PAID);
        paymentRepository.save(item);
    }
}
