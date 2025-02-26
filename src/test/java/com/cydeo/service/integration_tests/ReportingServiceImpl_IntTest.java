package com.cydeo.service.integration_tests;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ReportingService;
import com.cydeo.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class ReportingServiceImpl_IntTest {

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceProductService invoiceProductService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindAllInvoiceProductsOfCompany() {
        List<InvoiceProductDto> invoiceProducts = reportingService.findAllInvoiceProductsOfCompany();

        assertNotNull(invoiceProducts);
        assertThat(invoiceProducts).isNotEmpty();

        //todo Additional assertions can be added here based on expected behavior
    }

    @Test
    void testGetMonthlyProfitLossMap() {
        Map<String, BigDecimal> profitLossMap = reportingService.getMonthlyProfitLossMap();

        assertNotNull(profitLossMap);
        assertThat(profitLossMap).isNotEmpty();

        //todo Additional assertions can be added here based on expected behavior
    }
}

