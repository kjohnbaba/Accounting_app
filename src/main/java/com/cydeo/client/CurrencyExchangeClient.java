package com.cydeo.client;

import com.cydeo.annotations.ExecutionTime;
import com.cydeo.dto.CurrencyRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://cdn.jsdelivr.net/npm" , name = "CURRENCY-CLIENT")
public interface CurrencyExchangeClient {
    @ExecutionTime
    @GetMapping("/@fawazahmed0/currency-api@latest/v1/currencies/usd.json")
    CurrencyRateDto getCurrencyExchange();
}
