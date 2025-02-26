package com.cydeo.converter;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.service.InvoiceProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceProductDTOConverter implements Converter<String, InvoiceProductDto> {
    private final InvoiceProductService invoiceProductService;

    public InvoiceProductDTOConverter(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }


    @Override
    public InvoiceProductDto convert(String source) {
        if (source == null || source == "") return null;
        return invoiceProductService.findById(Long.parseLong(source));
    }
}
