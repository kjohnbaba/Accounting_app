package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.impl.ReportingServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceImpl_UnitTest {

    @Mock
    InvoiceService invoiceService;

    @Mock
    InvoiceProductService invoiceProductService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    ReportingServiceImpl reportingService;


    @Test
    void should_find_all_invoiceProducts_of_company() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();

        // when
        when(invoiceProductService.findAllApprovedInvoiceProductsOfCompany()).thenReturn(List.of(invoiceProductDto));

        List<InvoiceProductDto> actualResult = reportingService.findAllInvoiceProductsOfCompany();

        // then
        assertThat(actualResult).hasSize(1);
    }

    @Test
    void should_get_monthly_profitLoss_map() {
        // given
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.APPROVED, InvoiceType.SALES);
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        LocalDate now = LocalDate.now();
        invoiceProductDto.getInvoice().setDate(now);
        invoiceProductDto.setProfitLoss(BigDecimal.TEN);
        InvoiceProductDto invoiceProductDto2 = TestDocumentInitializer.getInvoiceProduct();
        invoiceProductDto2.getInvoice().setDate(now.minusMonths(1));
        invoiceProductDto2.setProfitLoss(BigDecimal.ONE);

        // when
        when(invoiceService.listInvoices(InvoiceType.SALES)).thenReturn(List.of(invoiceDto));
        when(invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(anyLong())).thenReturn(List.of(invoiceProductDto, invoiceProductDto2));

        Map<String, BigDecimal> map = reportingService.getMonthlyProfitLossMap();
        System.out.println("map = " + map);

        // then
        String key1 = now.getYear() + " " + now.getMonth();
        String key2 = now.minusMonths(1).getYear() + " "
                + now.minusMonths(1).getMonth();
        assertThat(map)
                .hasSize(2)
                .containsEntry(key1, BigDecimal.TEN)
                .containsEntry(key2, BigDecimal.ONE);
    }


}
