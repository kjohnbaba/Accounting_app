package com.cydeo.service.integration_tests;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exception.CompanyNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CompanyServiceImpl_IntTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testGetCompanyDtoByLoggedInUser() {
        CompanyDto companyDto = companyService.getCompanyDtoByLoggedInUser();
        assertNotNull(companyDto);
        assertEquals("Green Tech", companyDto.getTitle());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        CompanyDto companyDto = companyService.findById(id);
        assertNotNull(companyDto);
        assertEquals(id, companyDto.getId());
    }

    @Test
    void testFindById_NotFound() {
        Throwable throwable = catchThrowable(() -> companyService.findById(0L));
        assertInstanceOf(CompanyNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Company not found");
    }

    @Test
    void testListCompaniesByLoggedInUser() {
        List<CompanyDto> result = companyService.listCompaniesByLoggedInUser();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Add more assertions as needed
    }

    @Test
    void testSave() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTitle("New Company");
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);

        CompanyDto savedCompanyDto = companyService.save(companyDto);

        assertNotNull(savedCompanyDto);
        assertEquals("New Company", savedCompanyDto.getTitle());
        assertEquals(CompanyStatus.PASSIVE, savedCompanyDto.getCompanyStatus());
    }

    @Test
    void testUpdate() {
        CompanyDto companyDto = companyService.findById(2L);
        companyDto.setTitle("Updated Title");

        CompanyDto updatedCompanyDto = companyService.update(companyDto);

        assertNotNull(updatedCompanyDto);
        assertEquals("Updated Title", updatedCompanyDto.getTitle());
    }

    @Test
    void testDeactivate() {
        Long id = 2L;
        CompanyDto deactivatedCompany = companyService.deactivate(id);

        assertNotNull(deactivatedCompany);
        assertEquals(CompanyStatus.PASSIVE, deactivatedCompany.getCompanyStatus());

        Company company = companyRepository.findById(id).orElseThrow();
        assertEquals(CompanyStatus.PASSIVE, company.getCompanyStatus());
    }

    @Test
    void testActivate() {
        Long id = 4L;
        CompanyDto activatedCompany = companyService.activate(id);

        assertNotNull(activatedCompany);
        assertEquals(CompanyStatus.ACTIVE, activatedCompany.getCompanyStatus());

        Company company = companyRepository.findById(id).orElseThrow();
        assertEquals(CompanyStatus.ACTIVE, company.getCompanyStatus());
    }

    @Test
    void testIsTitleExist() {
        CompanyDto companyDto = companyService.findById(2L);
        companyDto.setTitle("Red Tech");

        boolean result = companyService.isTitleExist(companyDto);

        assertTrue(result);
    }

    @Test
    void testIsTitleExist_NotExist() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTitle("Non-existing Title");

        boolean result = companyService.isTitleExist(companyDto);

        assertFalse(result);
    }
}

