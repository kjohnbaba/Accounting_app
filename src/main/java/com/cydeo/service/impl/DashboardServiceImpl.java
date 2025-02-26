package com.cydeo.service.impl;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.client.ExchangeRateClient;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.dto.common.ExchangeRateDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class DashboardServiceImpl implements DashboardService {

    private final ExchangeRateClient exchangeRateClient;
    private final InvoiceService invoiceService;

    private CurrencyDto cachedCurrencyDto;

    public DashboardServiceImpl(ExchangeRateClient exchangeRateClient, InvoiceService invoiceService) {
        this.exchangeRateClient = exchangeRateClient;
        this.invoiceService = invoiceService;
    }

    @ExecutionTime
    @Scheduled(fixedRate = 10_000) // Execute every 100 seconds (in milliseconds)
//    @Scheduled(fixedRate = 3600000) // Execute every hour (in milliseconds)
    public void scheduledFetchCurrencyRate() {
        fetchCurrencyRateAsync();
    }

    @Async  // runs this method asynchronously (with another thread)
    public void fetchCurrencyRateAsync() {
        log.info("... fetching currencies with thread : " + Thread.currentThread().getName());
        try {
            ResponseEntity<ExchangeRateDto> response = exchangeRateClient.getUsdExchangeRate();
            if (response.getStatusCode().is2xxSuccessful()) {
                cachedCurrencyDto = Objects.requireNonNull(response.getBody()).getUsd();
            } else {
                // Handle non-2xx status codes
                // Log error or throw exception
                log.warn("failed to fetch and cache currency... : " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("...failed to fetch and cache currency..." + e.getMessage());
            // Handle exception
        }
    }

    @Override
    public CurrencyDto getCachedCurrencyDto() {
        if (cachedCurrencyDto == null) {
            log.error("while requesting rates, cached currency was null, trying to fetch again ...");
            fetchCurrencyRateAsync();
            if (cachedCurrencyDto == null) {
                CurrencyDto currencyDto = new CurrencyDto();
                currencyDto.setBritishPound(BigDecimal.ZERO);
                currencyDto.setCanadianDollar(BigDecimal.ZERO);
                currencyDto.setIndianRupee(BigDecimal.ZERO);
                currencyDto.setJapaneseYen(BigDecimal.ZERO);
                cachedCurrencyDto = currencyDto;
            }
        }
        return cachedCurrencyDto;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
        BigDecimal totalCost = invoiceService.sumTotal(InvoiceType.PURCHASE);
        BigDecimal totalSales = invoiceService.sumTotal(InvoiceType.SALES);
        BigDecimal profitLoss = invoiceService.sumProfitLoss();

        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }
}
