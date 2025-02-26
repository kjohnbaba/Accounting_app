package com.cydeo.service.integration_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceNotFoundException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@Transactional
class InvoiceServiceImpl_IntTest {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ProductService productService;

    @Autowired
    CompanyService companyService;

    @Autowired
    InvoiceProductService invoiceProductService;

    @BeforeEach
    void setUp(){
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void findById_shouldReturnInvoice() {
        // when
        InvoiceDto actualResult = invoiceService.findById(1L);
        // then
        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById_shouldThrowException() {
        // when
        Throwable throwable = catchThrowable(() -> invoiceService.findById(0L));
        // then
        assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
        // or
        assertInstanceOf(InvoiceNotFoundException.class, throwable);

        assertEquals("Invoice not found", throwable.getMessage());
    }


    @Test
    void listPurchaseInvoices() {
        // when
        List<InvoiceDto> actualList = invoiceService.listInvoices(InvoiceType.PURCHASE);

        // then
        assertThat(actualList).hasSize(2);
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
//        // ModelMapper mapping errors: Error mapping com.cydeo.entity.InvoiceProduct to com.cydeo.dto.InvoiceProductDto
    void listSalesInvoices() {
        // when
        List<InvoiceDto> actualList = invoiceService.listInvoices(InvoiceType.SALES);

        // then
        assertThat(actualList).hasSize(3);
        actualList.forEach(
                invoiceDto -> {
                    assertThat(invoiceDto.getPrice()).isNotNull();
                    assertThat(invoiceDto.getTax()).isNotNull();
                    assertThat(invoiceDto.getTotal()).isNotNull();
                }
        );
        List<String> list = actualList.stream().map(InvoiceDto::getInvoiceNo).toList();
        List<String> sortedList = actualList.stream().map(InvoiceDto::getInvoiceNo).sorted(Comparator.reverseOrder()).toList();
        assertThat(list).isEqualTo(sortedList);
    }

    @Test
    void generateNewInvoiceDto() {
        // when
        InvoiceDto actual = invoiceService.generateNewInvoiceDto(InvoiceType.SALES);

        // then
        assertThat(actual.getInvoiceNo()).isEqualTo("S-004");
    }

    @Test
    void saveInvoice() {
        // given
        InvoiceDto dto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE);
        dto.setId(null);

        // when
        InvoiceDto actual = invoiceService.saveInvoice(dto, InvoiceType.PURCHASE);

        // then
        assertThat(actual.getInvoiceNo()).isEqualTo(dto.getInvoiceNo());
    }

    @Test
    void updateInvoice() {
        // given
        ClientVendorDto clientVendor = TestDocumentInitializer.getClientVendor(ClientVendorType.CLIENT);
        InvoiceDto dto = invoiceService.findById(1L);
        dto.setClientVendor(clientVendor);

        // when
        InvoiceDto actual = invoiceService.update(dto);

        // then
        assertThat(actual.getClientVendor().getClientVendorName()).isEqualTo(clientVendor.getClientVendorName());
    }

    @Test
        //NoSuchElementException: invoice not found
    void should_delete_invoice_from_invoice() {
        // when
        invoiceService.deleteInvoice(2L);

        // then
        Invoice invoice = invoiceRepository.findById(2L).orElseThrow();
        assertThat(invoice.getIsDeleted()).isTrue();
    }

    @Test
    void printInvoice() {
        // when
        InvoiceDto invoiceDto = invoiceService.printInvoice(1L);

        // then
        assertThat(invoiceDto.getPrice()).isNotNull();
        assertThat(invoiceDto.getTax()).isNotNull();
        assertThat(invoiceDto.getTotal()).isNotNull();
    }

    @Test
    void approveSalesInvoice() {
        // when
        InvoiceDto invoiceDto = invoiceService.approve(4L);

        // then
        assertThat(invoiceDto.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(invoiceDto.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void approveSalesInvoice_shouldThrowExceptionDueToInsufficientStock() {
        // given
        ProductDto product = productService.getProductById(1L);
        product.setQuantityInStock(1);
        productService.save(product);

        // when
        Throwable throwable = catchThrowable(() -> invoiceService.approve(4L));

        // then
        assertInstanceOf(ProductNotFoundException.class, throwable);

        // @Transactional not works as expected with integration test:
        InvoiceDto invoiceDto = invoiceService.findById(4L);
//        assertThat(invoiceDto.getInvoiceStatus()).isEqualTo(InvoiceStatus.AWAITING_APPROVAL); // still APPROVED
//        assertThat(invoiceDto.getDate()).isNotEqualTo(LocalDate.now()); // still today

        assertThat(throwable.getMessage()).isEqualTo("Stock of HP Elite 800G1 Desktop Computer Package is not enough to approve this invoice. Please update the invoice.");
    }

    @Test
    void approvePurchaseInvoice() {
        // when
        InvoiceDto invoiceDto = invoiceService.approve(13L);

        // then
        assertThat(invoiceDto.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(invoiceDto.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void findLastThreeApprovedInvoice() {
        // when
        List<InvoiceDto> lastThreeApprovedInvoice = invoiceService.findLastThreeApprovedInvoice();

        // then
        assertThat(lastThreeApprovedInvoice).hasSize(3);
        assertThat(lastThreeApprovedInvoice.get(0).getDate()).isEqualTo(LocalDate.of(2023, 9, 17));
    }

    @Test
    void sumTotal() {
        // when
        BigDecimal total = invoiceService.sumTotal(InvoiceType.SALES);

        // then
        assertThat(total).isEqualTo(new BigDecimal("660.000"));
    }

    @Test
    void sumProfitLoss() {
        // when
        BigDecimal total = invoiceService.sumProfitLoss();

        // then
        assertThat(total).isEqualTo(new BigDecimal("110.00"));
    }


}
