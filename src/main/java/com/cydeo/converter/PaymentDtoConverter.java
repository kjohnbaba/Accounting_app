package com.cydeo.converter;

import com.cydeo.dto.PaymentDto;
import com.cydeo.service.PaymentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

public class PaymentDtoConverter implements Converter<String, PaymentDto> {

    public final PaymentService paymentService;

    public PaymentDtoConverter(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public PaymentDto convert(String source) {
        if (source.isEmpty()) return null;
        return paymentService.findById(Long.parseLong(source));
    }
}
