package com.cydeo.service.unit_tests;


import com.cydeo.dto.AddressDto;
import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Address;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.exception.ClientVendorNotFoundException;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.impl.ClientVendorServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientVendorServiceImpl_UnitTest {

    @Mock
    private ClientVendorRepository clientVendorRepository;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private ClientVendorServiceImpl clientVendorService;

    private ClientVendor clientVendor;
    private ClientVendorDto clientVendorDto;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        companyDto = new CompanyDto();
        companyDto.setId(1L);

        clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setClientVendorName("Test Vendor");
        clientVendor.setCompany(mapperUtil.convert(companyDto, new Company()));
        clientVendor.setInvoices(List.of(new Invoice()));

        clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(1L);
        clientVendorDto.setClientVendorName("Test Vendor");
        clientVendorDto.setCompany(companyDto);
    }

    @Test
    void findById_ShouldReturnClientVendorDto() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));

        ClientVendorDto result = clientVendorService.findById(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
        verify(clientVendorRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClientVendorNotFoundException.class, () -> clientVendorService.findById(1L));
    }

    @Test
    void listAllClientVendors_ShouldReturnClientVendorList() {
        clientVendorDto.setHasInvoice(true);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.findClientVendorsByCompany_Id(anyLong())).thenReturn(List.of(clientVendor));

        List<ClientVendorDto> result = clientVendorService.listAllClientVendors();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void listByClientVendorType_ShouldReturnClientVendorList() {
        clientVendorDto.setClientVendorType(ClientVendorType.CLIENT);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        clientVendor.setClientVendorType(ClientVendorType.CLIENT);
        when(clientVendorRepository.findAll()).thenReturn(List.of(clientVendor));

        List<ClientVendorDto> result = clientVendorService.listByClientVendorType(ClientVendorType.CLIENT);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void save_ShouldReturnSavedClientVendor() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        ClientVendorDto result = clientVendorService.save(clientVendorDto);

        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void update_ShouldReturnUpdatedClientVendor() {
        Address address = new Address();
        address.setId(1L);
        clientVendor.setAddress(address);
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        clientVendorDto.setAddress(addressDto);
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        ClientVendorDto result = clientVendorService.update(clientVendorDto);

        assertThat(result).usingRecursiveComparison().isEqualTo(clientVendorDto);
    }

    @Test
    void update_ShouldThrowException() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClientVendorNotFoundException.class, () -> clientVendorService.update(clientVendorDto));
    }

    @Test
    void deleteClientVendorById_ShouldMarkClientVendorAsDeleted() {
        Address address = new Address();
        address.setId(1L);
        clientVendor.setAddress(address);
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        clientVendorDto.setAddress(addressDto);
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));

        clientVendorService.deleteClientVendorById(1L);

        assertThat(clientVendor.getIsDeleted()).isTrue();
        assertThat(clientVendor.getAddress().getIsDeleted()).isTrue();
        assertThat(clientVendor.getClientVendorName()).isEqualTo("Test Vendor-1");
        verify(clientVendorRepository, times(1)).save(clientVendor);
    }

    @Test
    void isClientVendorExist_ShouldReturnTrueIfClientVendorExists() {
        clientVendorDto.setId(1L);
        clientVendor.setId(2L);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.findByClientVendorName_AndCompany_Id(anyString(), anyLong()))
                .thenReturn(Optional.of(clientVendor));

        boolean result = clientVendorService.isClientVendorExist(clientVendorDto);

        assertThat(result).isTrue();
    }

    @Test
    void isClientVendorExist_ShouldReturnFalseIfClientVendorDoesNotExist() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(clientVendorRepository.findByClientVendorName_AndCompany_Id(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        boolean result = clientVendorService.isClientVendorExist(clientVendorDto);

        assertThat(result).isFalse();
    }
}

