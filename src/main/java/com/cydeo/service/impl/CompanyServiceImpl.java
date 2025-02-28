package com.cydeo.service.impl;

import com.cydeo.converter.CompanyDTOConverter;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exceptions.CompanyNotFoundException;
import com.cydeo.exceptions.ResourceNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public Long getCompanyIdByLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return loggedInUser.getCompany().getId();
    }



    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .filter(company -> !company.getId().equals(1L))
                .sorted(Comparator.comparing(Company::getCompanyStatus)
                        .thenComparing(Company::getTitle))
                .map(company -> mapperUtil.convert(company, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCompany(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setTitle(companyDto.getTitle());
        company.setPhone(companyDto.getPhone());
        company.setWebsite(companyDto.getWebsite());
        company.setAddress(mapperUtil.convert(companyDto.getAddress(), company.getAddress()));
        companyRepository.save(company);
    }


    @Override
    public CompanyDto findById(long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public void createCompany(CompanyDto companyDto) {
        Company company = mapperUtil.convert(companyDto, new Company());
        companyRepository.save(company);
    }

    @Transactional
    @Override
    public void activateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
    }
    @Transactional
    @Override
    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));

        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
    }


}
