package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceNotFoundException;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.impl.InvoiceServiceImpl;
import com.cydeo.util.MapperUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImpl_UnitTest {

    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    InvoiceProductService invoiceProductService;

    @Mock
    CompanyService companyService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    InvoiceServiceImpl invoiceService;


    @Test
    void should_find_by_id_return_invoice_if_exists() {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        // when
        when(invoiceRepository.findById(invoiceDto.getId())).thenReturn(Optional.of(invoice));

        InvoiceDto actualResult = invoiceService.findById(invoiceDto.getId());

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(invoiceDto);
    }

    @Test
    void find_by_id_should_throw_exception_if_invoiceProduct_does_exist() {
        // when
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> invoiceService.findById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        Assertions.assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
        // or
        assertInstanceOf(InvoiceNotFoundException.class, throwable);

        assertEquals("Invoice not found", throwable.getMessage());
    }


    @Test
    void should_list_and_sort_invoices () {
        // given
        List<InvoiceDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES),
                TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES),
                TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES)
        );

        List<Invoice> invoices = dtoList.stream()
                .map(invoiceDto -> mapperUtil.convert(invoiceDto, new Invoice()))
                .toList();
        List<InvoiceDto> expectedList = dtoList.stream()
                .sorted(Comparator.comparing(InvoiceDto::getDate).reversed())
                .toList();
        List<InvoiceProductDto> invoiceProducts = List.of(TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(), TestDocumentInitializer.getInvoiceProduct());
        invoiceProducts.get(0).setTotal(BigDecimal.valueOf(55));
        invoiceProducts.get(1).setTotal(BigDecimal.valueOf(55));
        invoiceProducts.get(2).setTotal(BigDecimal.valueOf(55));

        // when
        when(invoiceRepository.findAllByCompany_IdAndInvoiceType(1L, InvoiceType.SALES)).thenReturn(invoices);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));
        when(invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(1L)).thenReturn(invoiceProducts);

        List<InvoiceDto> actualList = invoiceService.listInvoices(InvoiceType.SALES);

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(expectedList);
    }

    @Test
    void should_generate_new_invoice () {
        // when
        when(invoiceRepository.retrieveLastInvoiceNo(anyLong(), anyString())).thenReturn("S-003");
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        InvoiceDto actualDto = invoiceService.generateNewInvoiceDto(InvoiceType.SALES);

        // then
        assertThat(actualDto.getInvoiceNo()).isEqualTo("S-004");
    }


    @Test
    void should_save_new_invoice () {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());

        // when
        when(invoiceRepository.save(any())).thenReturn(invoice);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        InvoiceDto actualDto = invoiceService.saveInvoice(invoiceDto, InvoiceType.SALES);

        // then
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(invoiceDto);
    }

    @Test
    void should_update_invoice () {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());

        // when
        when(invoiceRepository.findById(any())).thenReturn(Optional.ofNullable(invoice));
        when(invoiceRepository.save(any())).thenReturn(invoice);

        InvoiceDto actualDto = invoiceService.update(invoiceDto);

        // then
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(invoiceDto);
    }


    @Test
    void should_delete_invoice() {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoice.setIsDeleted(false);

        // when
        when(invoiceRepository.findById(any())).thenReturn(Optional.ofNullable(invoice));
        when(invoiceRepository.save(any())).thenReturn(invoice);

        invoiceService.deleteInvoice(invoice.getId());

        // then
        assertThat(invoice.getIsDeleted()).isTrue();
    }

    @Test
    void should_print_invoice () {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());

        // when
        when(invoiceRepository.findById(any())).thenReturn(Optional.ofNullable(invoice));
        InvoiceDto actualDto = invoiceService.printInvoice(invoiceDto.getId());

        // then
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total")
                .isEqualTo(invoiceDto);
    }


    @Test
    void should_approve_SALES_invoice () {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.SALES);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);

        // when
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any())).thenReturn(invoice);

        InvoiceDto actualDto = invoiceService.approve(invoiceDto.getId());

        // then
        verify(invoiceProductService, times(1)).updateQuantityInStockSale(invoice.getId());
        verify(invoiceProductService, times(1)).calculateProfitOrLoss(invoice.getId());
        assertThat(actualDto.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(actualDto.getDate()).isEqualTo(LocalDate.now());
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total", "invoiceStatus", "date")
                .isEqualTo(invoiceDto);
    }

    @Test
    void should_approve_PURCHASE_invoice () {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);

        // when
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any())).thenReturn(invoice);

        InvoiceDto actualDto = invoiceService.approve(invoiceDto.getId());

        // then
        verify(invoiceProductService, times(1)).updateRemainingQuantityUponPurchaseApproval(invoice.getId());
        verify(invoiceProductService, times(1)).updateQuantityInStockPurchase(invoice.getId());
        assertThat(actualDto.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
        assertThat(actualDto.getDate()).isEqualTo(LocalDate.now());
        assertThat(actualDto).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price", "tax", "total", "invoiceStatus", "date")
                .isEqualTo(invoiceDto);
    }

    @Test
    void should_find_last_three_approved_invoice () {
        // given
        List<Invoice> invoices = new ArrayList<>();

        // when
        when(invoiceRepository.findLastApproved3Invoices(1L)).thenReturn(invoices);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        List<InvoiceDto> actualList = invoiceService.findLastThreeApprovedInvoice();

        // then
        assertThat(actualList).isNotNull();
        verify(invoiceRepository).findLastApproved3Invoices(1L);
        verify(companyService).getCompanyDtoByLoggedInUser();
    }

    @Test
    void should_sum_total_approved_invoice () {
        // given
        List<InvoiceDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoice(InvoiceStatus.APPROVED, InvoiceType.PURCHASE),
                TestDocumentInitializer.getInvoice(InvoiceStatus.APPROVED, InvoiceType.PURCHASE),
                TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE)
        );

        List<Invoice> invoices = dtoList.stream()
                .map(invoiceDto -> mapperUtil.convert(invoiceDto, new Invoice()))
                .toList();

        List<InvoiceProductDto> invoiceProductDtos = List.of(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        invoiceProductDtos.get(0).setTotal(BigDecimal.valueOf(55));
        invoiceProductDtos.get(1).setTotal(BigDecimal.valueOf(55));
        invoiceProductDtos.get(2).setTotal(BigDecimal.valueOf(55));

        // when
        when(invoiceRepository.findAllByCompany_IdAndInvoiceType(1L, InvoiceType.PURCHASE)).thenReturn(invoices);
        when(invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(1L)).thenReturn(invoiceProductDtos);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        BigDecimal actualResult = invoiceService.sumTotal(InvoiceType.PURCHASE);

        // then
        assertThat(actualResult).isEqualTo(BigDecimal.valueOf(330));
        verify(companyService).getCompanyDtoByLoggedInUser();
    }

    @Test
    void should_sum_profit_loss_approved_invoice () {
        // given
        List<InvoiceDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoice(InvoiceStatus.APPROVED, InvoiceType.PURCHASE),
                TestDocumentInitializer.getInvoice(InvoiceStatus.APPROVED, InvoiceType.PURCHASE),
                TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE)
        );

        List<Invoice> invoices = dtoList.stream()
                .map(invoiceDto -> mapperUtil.convert(invoiceDto, new Invoice()))
                .toList();

        List<InvoiceProductDto> invoiceProductDtos = List.of(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        invoiceProductDtos.get(0).setProfitLoss(BigDecimal.TEN);
        invoiceProductDtos.get(1).setProfitLoss(BigDecimal.TEN);
        invoiceProductDtos.get(2).setProfitLoss(BigDecimal.TEN);

        // when
        when(invoiceRepository.findApprovedSalesInvoices(1L)).thenReturn(invoices);
        when(invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(1L)).thenReturn(invoiceProductDtos);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        BigDecimal actualResult = invoiceService.sumProfitLoss();

        // then
        assertThat(actualResult).isEqualTo(BigDecimal.valueOf(90));
    }
}
