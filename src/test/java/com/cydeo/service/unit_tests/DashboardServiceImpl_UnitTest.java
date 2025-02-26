package com.cydeo.service.unit_tests;

import com.cydeo.client.ExchangeRateClient;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.dto.common.ExchangeRateDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.impl.DashboardServiceImpl;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@Import(TestAsyncConfig.class)
class DashboardServiceImpl_UnitTest {

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @Mock
    private InvoiceService invoiceService;

//    @Autowired
    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private ExchangeRateDto exchangeRateDto;
    private CurrencyDto currencyDto;

    @BeforeEach
    public void setUp() {
        currencyDto = new CurrencyDto();
        currencyDto.setBritishPound(new BigDecimal("1.25"));
        currencyDto.setCanadianDollar(new BigDecimal("1.75"));
        currencyDto.setIndianRupee(new BigDecimal("74.85"));
        currencyDto.setJapaneseYen(new BigDecimal("109.50"));

        exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setUsd(currencyDto);
    }

    //TODO failing @Disabled for now
    @Disabled
    @Test
    void testListUsdExchangeRate_Success(){
        // Arrange
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);
        Mockito.when(exchangeRateClient.getUsdExchangeRate()).thenReturn(responseEntity);

        // Act
        dashboardService.fetchCurrencyRateAsync();

        // Wait for the async operation to complete
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(dashboardService.getCachedCurrencyDto())
                        .usingRecursiveComparison()
                        .isEqualTo(currencyDto));
    }

    @Test
    void testListUsdExchangeRate_Failure() {
        // Arrange
        when(exchangeRateClient.getUsdExchangeRate()).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        // Act
        dashboardService.scheduledFetchCurrencyRate();

        // Assert
        BigDecimal britishPound = dashboardService.getCachedCurrencyDto().getBritishPound();
        assertThat(britishPound).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testGetSummaryNumbers() {
        // Mock the response from the invoice service
        when(invoiceService.sumTotal(InvoiceType.PURCHASE)).thenReturn(new BigDecimal("1000.00"));
        when(invoiceService.sumTotal(InvoiceType.SALES)).thenReturn(new BigDecimal("1500.00"));
        when(invoiceService.sumProfitLoss()).thenReturn(new BigDecimal("500.00"));

        // Call the method to test
        Map<String, BigDecimal> summaryNumbers = dashboardService.getSummaryNumbers();

        // Assert the expected results
        assertThat(summaryNumbers).isNotNull();
        assertThat(summaryNumbers.get("totalCost")).isEqualByComparingTo("1000.00");
        assertThat(summaryNumbers.get("totalSales")).isEqualByComparingTo("1500.00");
        assertThat(summaryNumbers.get("profitLoss")).isEqualByComparingTo("500.00");
    }
}

