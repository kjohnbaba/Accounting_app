package com.cydeo.client;

import com.cydeo.dto.common.CountryDto;
import com.cydeo.dto.common.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(url = "${api.countryDropDown.url}", name = "CountryDropDown-Client")
public interface CountryClient {

    @GetMapping(value = "/api/getaccesstoken", consumes = MediaType.APPLICATION_JSON_VALUE)
    TokenDto auth(@RequestHeader("user-email") String email, @RequestHeader("api-token") String apiToken);

    @GetMapping(value = "/api/countries", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CountryDto>> getCountry(@RequestHeader("Authorization") String apiToken);

}
