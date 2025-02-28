package com.cydeo.service;

import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    Long getCompanyIdByLoggedInUser();

    List<CompanyDto> getAllCompanies();

    void updateCompany(Long id, CompanyDto companyDto);

    CompanyDto findById(long l);

    public void createCompany(CompanyDto companyDto);

    void activateCompany(Long id);

    void deactivateCompany(Long id);
}
