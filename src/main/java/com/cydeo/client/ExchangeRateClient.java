package com.cydeo.client;

import com.cydeo.dto.common.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${api.currency.url}", name = "ExchangeRate-Client")
public interface ExchangeRateClient {

    @GetMapping("/usd.json")
    ResponseEntity<ExchangeRateDto> getUsdExchangeRate();
}
