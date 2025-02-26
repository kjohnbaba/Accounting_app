package com.cydeo.service.integration_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.exception.ClientVendorNotFoundException;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.ClientVendorService;
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
class ClientVendorServiceImpl_IntTest {

    @Autowired
    private ClientVendorRepository clientVendorRepository;

    @Autowired
    private ClientVendorService clientVendorService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindById() {
        Long id = 1L;
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(id);
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(id);

        ClientVendorDto result = clientVendorService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindById_NotFound() {

        Throwable throwable = catchThrowable(() -> clientVendorService.findById(0L));

        assertInstanceOf(ClientVendorNotFoundException.class, throwable);

        assertThat(throwable.getMessage()).isEqualTo("This client or vendor does not exist");
    }

    @Test
    void testListAllClientVendors() {
        List<ClientVendorDto> result = clientVendorService.listAllClientVendors();
        ClientVendorDto clientVendor = clientVendorService.findById(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFields("hasInvoice")
                .isEqualTo(clientVendor);
    }

    @Test
    void testListByClientVendorType() {

        List<ClientVendorDto> result = clientVendorService.listByClientVendorType(ClientVendorType.CLIENT);

        assertThat(result).hasSize(2);
        result.forEach(cv -> assertThat(cv.getClientVendorType()).isEqualTo(ClientVendorType.CLIENT));
    }

    @Test
    void testSave() {
        ClientVendorDto clientVendorDto = TestDocumentInitializer.getClientVendor(ClientVendorType.VENDOR);

        ClientVendorDto result = clientVendorService.save(clientVendorDto);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("id", "address.id")
                .isEqualTo(clientVendorDto);
    }

    @Test
    void testUpdate() {
        ClientVendorDto clientVendorDto = clientVendorService.findById(1L);
        clientVendorDto.setClientVendorName("updated client");
        clientVendorDto.setClientVendorType(ClientVendorType.VENDOR);

        ClientVendorDto result = clientVendorService.update(clientVendorDto);

        assertThat(result).usingRecursiveComparison()
                .isEqualTo(clientVendorDto);
    }

    @Test
    void testDeleteClientVendorById() {
        clientVendorService.deleteClientVendorById(1L);

        ClientVendor clientVendor = clientVendorRepository.findById(1L).orElseThrow();
        assertTrue(clientVendor.getIsDeleted());
        assertTrue(clientVendor.getAddress().getIsDeleted());
        assertEquals("Orange Tech-1", clientVendor.getClientVendorName());
    }

    @Test
    void testIsClientVendorExist() {
        ClientVendorDto clientVendorDto = clientVendorService.findById(1L);
        clientVendorDto.setClientVendorName("Photobug Tech");

        boolean result = clientVendorService.isClientVendorExist(clientVendorDto);

        assertTrue(result);
    }

    @Test
    void testIsClientVendorExist_NotExist() {
        ClientVendorDto clientVendorDto = clientVendorService.findById(1L);
        clientVendorDto.setClientVendorName("Blue Tech");

        boolean result = clientVendorService.isClientVendorExist(clientVendorDto);

        assertFalse(result);
    }
}

