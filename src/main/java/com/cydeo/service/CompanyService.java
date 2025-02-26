package com.cydeo.service;

import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto getCompanyDtoByLoggedInUser();

    CompanyDto findById(Long id);

    CompanyDto save(CompanyDto companyDto);

    CompanyDto update(CompanyDto companyDto);

    CompanyDto deactivate(Long id);

    CompanyDto activate(Long id);

    List<CompanyDto> listCompaniesByLoggedInUser();

    boolean isTitleExist(CompanyDto companyDto);
}
