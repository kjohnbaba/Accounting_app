package com.cydeo.util;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class InvoiceMapper {


        private final ModelMapper modelMapper;

        public InvoiceMapper(ModelMapper modelMapper) {
            this.modelMapper = modelMapper;
        }

        public Invoice convertToEntity(InvoiceDto invoiceDto) {
            return modelMapper.map(invoiceDto, Invoice.class);
        }

        public InvoiceDto convertToDto(Invoice invoice) {
            return modelMapper.map(invoice, InvoiceDto.class);
        }
    }

