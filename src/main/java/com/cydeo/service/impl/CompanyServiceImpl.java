package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exception.CompanyNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public CompanyDto getCompanyDtoByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        return mapperUtil.convert(company, new CompanyDto());
    }


    @Override
    public List<CompanyDto> listCompaniesByLoggedInUser() {
        boolean isRootUser = securityService.getLoggedInUser()
                .getRole().getDescription().equalsIgnoreCase("root user");
        List<CompanyDto> list;
        if (isRootUser) {
            list = companyRepository.findAll()
                    .stream()
                    .filter(company -> company.getId() != 1)
                    .sorted(Comparator.comparing(Company::getCompanyStatus).thenComparing(Company::getTitle))
                    .map(each -> mapperUtil.convert(each, new CompanyDto()))
                    .toList();
        } else {
            Company company = companyRepository.findById(getCompanyDtoByLoggedInUser().getId()).orElseThrow();
            list = List.of(mapperUtil.convert(company, new CompanyDto()));
        }
        return list;
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = companyRepository.save(mapperUtil.convert(companyDto, new Company()));
        return mapperUtil.convert(company, new CompanyDto());
    }


    @Override
    public CompanyDto update(CompanyDto companyDto) {
        Company company = companyRepository.findById(companyDto.getId()).orElseThrow(CompanyNotFoundException::new);
        Company updatedCompany = mapperUtil.convert(companyDto, new Company());
        updatedCompany.setCompanyStatus(company.getCompanyStatus());
        Company savedCompany = companyRepository.save(updatedCompany);
        return mapperUtil.convert(savedCompany, new CompanyDto());
    }

    @Override
    public CompanyDto deactivate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        Company saved = companyRepository.save(company);
        return mapperUtil.convert(saved, new CompanyDto());
    }

    @Override
    public CompanyDto activate(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        Company saved = companyRepository.save(company);
        return mapperUtil.convert(saved, new CompanyDto());
    }

    @Override
    public boolean isTitleExist(CompanyDto companyDto) {
        Company company = companyRepository.findByTitle(companyDto.getTitle()).orElse(null);
        if (company == null) {
            return false;
        }
        return !Objects.equals(companyDto.getId(), company.getId());
    }
}
