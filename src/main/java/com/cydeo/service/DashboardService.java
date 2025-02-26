package com.cydeo.service;

import com.cydeo.dto.common.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {
    CurrencyDto getCachedCurrencyDto();

    Map<String, BigDecimal> getSummaryNumbers();
}
