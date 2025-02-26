package com.cydeo.service.integration_tests;

import com.cydeo.client.ExchangeRateClient;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.dto.common.ExchangeRateDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.impl.DashboardServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Log4j2
class DashboardServiceImpl_IntTest {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @Mock
    private InvoiceService invoiceService;

    private CurrencyDto currencyDto;

    @BeforeEach
    void setUp() {
        currencyDto = new CurrencyDto();
        currencyDto.setBritishPound(BigDecimal.valueOf(0.72));
        currencyDto.setCanadianDollar(BigDecimal.valueOf(1.21));
        currencyDto.setIndianRupee(BigDecimal.valueOf(72.93));
        currencyDto.setJapaneseYen(BigDecimal.valueOf(110.27));
    }

    @Test
    void testFetchCurrencyRateAsync_Successful() throws ExecutionException, InterruptedException {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setUsd(currencyDto);
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);

        when(exchangeRateClient.getUsdExchangeRate()).thenReturn(responseEntity);

        dashboardService.fetchCurrencyRateAsync();

        assertThat(dashboardService.getCachedCurrencyDto()).isEqualTo(currencyDto);
        verify(exchangeRateClient).getUsdExchangeRate();
    }

    @Test
    void testFetchCurrencyRateAsync_Failure() throws ExecutionException, InterruptedException {
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(exchangeRateClient.getUsdExchangeRate()).thenReturn(responseEntity);

        dashboardService.fetchCurrencyRateAsync();

        assertThat(dashboardService.getCachedCurrencyDto()).isNotEqualTo(currencyDto);
        verify(exchangeRateClient, times(2)).getUsdExchangeRate();
    }

    @Test
    @Disabled // because they are commented out
    void testGetCachedCurrencyDto_Fallback() {
        CurrencyDto fallbackCurrencyDto = dashboardService.getCachedCurrencyDto();

        assertThat(fallbackCurrencyDto.getBritishPound()).isEqualTo(BigDecimal.ZERO);
        assertThat(fallbackCurrencyDto.getCanadianDollar()).isEqualTo(BigDecimal.ZERO);
        assertThat(fallbackCurrencyDto.getIndianRupee()).isEqualTo(BigDecimal.ZERO);
        assertThat(fallbackCurrencyDto.getJapaneseYen()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testGetCachedCurrencyDto_WhenCacheIsNull_FetchesCurrencyRate() throws ExecutionException, InterruptedException {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setUsd(currencyDto);
        ResponseEntity<ExchangeRateDto> responseEntity = new ResponseEntity<>(exchangeRateDto, HttpStatus.OK);

        when(exchangeRateClient.getUsdExchangeRate()).thenReturn(responseEntity);

        // Initial call to getCachedCurrencyDto when cache is null
        CurrencyDto result = dashboardService.getCachedCurrencyDto();

        // Wait for async fetch to complete
        CompletableFuture.runAsync(() -> {
            while (dashboardService.getCachedCurrencyDto() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).get();

        // Verify the fetchCurrencyRateAsync was called
        verify(exchangeRateClient).getUsdExchangeRate();

        // Verify the result after async fetch
        assertThat(dashboardService.getCachedCurrencyDto()).isEqualTo(currencyDto);
    }


    @Test
    void testGetSummaryNumbers() {
        BigDecimal totalCost = BigDecimal.valueOf(5000);
        BigDecimal totalSales = BigDecimal.valueOf(10000);
        BigDecimal profitLoss = BigDecimal.valueOf(5000);

        when(invoiceService.sumTotal(InvoiceType.PURCHASE)).thenReturn(totalCost);
        when(invoiceService.sumTotal(InvoiceType.SALES)).thenReturn(totalSales);
        when(invoiceService.sumProfitLoss()).thenReturn(profitLoss);

        Map<String, BigDecimal> summaryNumbers = dashboardService.getSummaryNumbers();

        assertThat(summaryNumbers).containsEntry("totalCost", totalCost);
        assertThat(summaryNumbers).containsEntry("totalSales", totalSales);
        assertThat(summaryNumbers).containsEntry("profitLoss", profitLoss);
    }


}

