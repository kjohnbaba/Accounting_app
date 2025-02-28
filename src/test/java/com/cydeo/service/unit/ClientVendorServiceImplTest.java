package com.cydeo.service.unit;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.ClientVendorServiceImpl;
import com.cydeo.util.ClientVendorMapper;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientVendorServiceImplTest {

    @InjectMocks
    private ClientVendorServiceImpl clientVendorService;
    @Mock
    private MapperUtil mapperUtil;
    @Mock
    private ClientVendorMapper clientVendorMapper;
    @Mock
    private CompanyService companyService;
    @Mock
    private SecurityService securityService;

    @Mock
    private ClientVendorRepository clientVendorRepository;

    @Test
    void findById_ShouldReturnClientVendorDto(){

        ClientVendorDto clientVendorDto = new ClientVendorDto();
        ClientVendor clientVendor = new ClientVendor();

        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(mapperUtil.convert(any(ClientVendor.class), any(ClientVendorDto.class)))
                .thenReturn(clientVendorDto);

        ClientVendorDto expected = clientVendorService.findById(1L);

        assertNotNull(expected);
        assertEquals(expected.getId(), clientVendorDto.getId());

    }

    @Test
    void findAll_ShouldReturnAListOfClientVendorDto(){
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        ClientVendor clientVendor = new ClientVendor();

        when(clientVendorRepository.findAllByIsDeleted(anyBoolean())).thenReturn(List.of(clientVendor));
        when(clientVendorMapper.convertToDto(any(ClientVendor.class))).thenReturn(clientVendorDto);

        List<ClientVendorDto> all = clientVendorService.findAll();

        assertNotNull(all);
    }

    @Test
    void delete_ShouldSetStatusAsDeleted(){
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setIsDeleted(false);

        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(clientVendorRepository.save(any(ClientVendor.class))).thenReturn(clientVendor);

        clientVendorService.delete(1L);

        verify(clientVendorRepository).save(clientVendor); // Verify save was called
        assertThat(clientVendor.getIsDeleted()).isTrue();
    }


    @Test
    void listAllByClientVendorType_ShouldReturnAListOfClientVendorDtoOfGivenType(){
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        clientVendorDto.setClientVendorType(ClientVendorType.CLIENT);
        ClientVendor clientVendor1 = new ClientVendor();
        clientVendor1.setClientVendorType(ClientVendorType.CLIENT);

        when(clientVendorRepository.findAllByClientVendorTypeAndCompany_IdOrderByClientVendorName(any(ClientVendorType.class), anyLong())).thenReturn(List.of(clientVendor1));
        when(companyService.getCompanyIdByLoggedInUser()).thenReturn(2L);
        when(mapperUtil.convert(any(ClientVendor.class), any(ClientVendorDto.class))).thenReturn(clientVendorDto);

        List<ClientVendorDto> allClients = clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT);

        assertThat(allClients.size()).isEqualTo(1);
        assertThat(allClients.get(0).getClientVendorType()).isEqualTo(ClientVendorType.CLIENT);
    }

    @Test
    void save_ShouldReturnSavedClientVendor() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setCompany(companyDto);

        ClientVendor clientVendor = new ClientVendor();
        ClientVendorDto clientVendorDto = new ClientVendorDto();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(clientVendorMapper.convertToEntity(any(ClientVendorDto.class))).thenReturn(clientVendor);
        when(clientVendorRepository.save(any(ClientVendor.class))).thenAnswer(invocation -> {
            ClientVendor savedClientVendor = invocation.getArgument(0);
            savedClientVendor.setId(10L); // Set an ID to simulate saving
            return savedClientVendor;
        });

        clientVendorService.save(clientVendorDto);

        assertThat(clientVendor.getId()).isNotNull();
    }

}