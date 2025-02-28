package com.cydeo.converter;


import com.cydeo.dto.CompanyDto;
import com.cydeo.service.CompanyService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyDtoConvertor implements Converter<String, CompanyDto> {

    private final CompanyService companyService;

    public CompanyDtoConvertor(@Lazy CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDto convert(String source) {
        if(source.isEmpty()) return null;
        return companyService.findById(Long.parseLong(source));
    }
}
