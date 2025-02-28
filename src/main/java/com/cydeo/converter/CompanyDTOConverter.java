package com.cydeo.converter;

import com.cydeo.dto.AddressDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class CompanyDTOConverter implements Converter<String, CompanyDto> {

    private final CompanyService companyService;

    public CompanyDTOConverter(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDto convert(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }
        return companyService.findById(Long.parseLong(id));
    }
}
