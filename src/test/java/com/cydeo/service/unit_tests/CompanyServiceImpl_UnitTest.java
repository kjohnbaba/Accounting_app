package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.exception.CompanyNotFoundException;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.CompanyServiceImpl;
import com.cydeo.util.MapperUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImpl_UnitTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    SecurityService securityService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    CompanyServiceImpl companyService;

    private Company company;
    private CompanyDto companyDto;
    private User user;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setTitle("Test Company");
        company.setCompanyStatus(CompanyStatus.ACTIVE);

        companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setTitle("Test Company");
        companyDto.setCompanyStatus(CompanyStatus.ACTIVE);

        user = new User();
        user.setRole(new Role());
        user.setCompany(company);
    }

    @Test
    void getCompanyDtoByLoggedInUser_ShouldReturnCompanyDto() {
        // given
        CompanyDto dto = TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE);
        UserDto loggedInUser = TestDocumentInitializer.getUser("Admin");

        // when
        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);

        CompanyDto actualResult = companyService.getCompanyDtoByLoggedInUser();

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .isEqualTo(dto);
        verify(securityService, times(1)).getLoggedInUser();

    }

    @Test
    void findById_ShouldReturnCompanyDto() {
        // given
        CompanyDto dto = TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE);
        Company entity = mapperUtil.convert(dto, new Company());
        // when
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        CompanyDto actualResult = companyService.findById(1L);

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .isEqualTo(dto);
        verify(companyRepository, times(1)).findById(1L);

    }

    @Test
    void findById_ShouldThrowException() {
        // when
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> companyService.findById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        Assertions.assertThat(throwable).isInstanceOf(CompanyNotFoundException.class);
        // or
        assertInstanceOf(CompanyNotFoundException.class, throwable);

        assertEquals("Company not found", throwable.getMessage());
    }

    @Test
    void listCompaniesByLoggedInUser_ShouldReturnCompanyList() {
        UserDto dto = mapperUtil.convert(user, new UserDto());
        dto.getRole().setDescription("Admin");
        company.setId(2L);
        companyDto.setId(2L);
        when(securityService.getLoggedInUser()).thenReturn(dto);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(company));

        List<CompanyDto> result = companyService.listCompaniesByLoggedInUser();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFieldsOfTypes()
                .isEqualTo(companyDto);
    }

    @Test
    void save_ShouldReturnSavedCompany() {
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.save(companyDto);

        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
    }

    @Test
    void update_ShouldReturnUpdatedCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.update(companyDto);

        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
    }

    @Test
    void deactivate_ShouldReturnDeactivatedCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);

        CompanyDto result = companyService.deactivate(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
        assertThat(result.getCompanyStatus())
                .isEqualTo(CompanyStatus.PASSIVE);
    }

    @Test
    void activate_ShouldReturnActivatedCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        CompanyDto result = companyService.activate(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(companyDto);
        assertThat(result.getCompanyStatus()).isEqualTo(CompanyStatus.ACTIVE);
    }

    @Test
    void isTitleExist_ShouldReturnTrueIfTitleExists() {
        company.setId(2L);
        when(companyRepository.findByTitle(anyString())).thenReturn(Optional.ofNullable(company));

        boolean result = companyService.isTitleExist(companyDto);

        assertThat(result).isTrue();
    }

    @Test
    void isTitleExist_ShouldReturnFalseIfTitleDoesNotExist() {
        when(companyRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        boolean result = companyService.isTitleExist(companyDto);

        assertThat(result).isFalse();
    }


}
