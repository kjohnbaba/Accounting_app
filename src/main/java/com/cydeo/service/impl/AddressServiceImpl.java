package com.cydeo.service.impl;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.client.CountryClient;
import com.cydeo.dto.common.CountryDto;
import com.cydeo.dto.common.TokenDto;
import com.cydeo.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final CountryClient countryClient;

    @Value("${api.country.token}")
    private String token;

    @Value("${api.country.mail}")
    private String email;

    private List<String> cachedCountries;

    public AddressServiceImpl(CountryClient countryClient) {
        this.countryClient = countryClient;
    }

    private String getBearerToken() {
        TokenDto tokenDto = countryClient.auth(email, token);
        return "Bearer " + tokenDto.getAuthToken();
    }

    @ExecutionTime
    @Scheduled(fixedRate = 100_000) // Execute every 100 seconds (in milliseconds)
//    @Scheduled(fixedRate = 3600000) // Execute every hour (in milliseconds)
    public void scheduledCountryList() {
        fetchCountryListAsync();
    }

    @Async  // runs this method asynchronously (with another thread)
    public List<String> fetchCountryListAsync() {
        log.info("... fetching Countries with thread : {}", Thread.currentThread().getName());

        ResponseEntity<List<CountryDto>> response = countryClient.getCountry(getBearerToken());
        if (response.getStatusCode().is2xxSuccessful()) {
            cachedCountries = Objects.requireNonNull(response.getBody()).stream()
                    .map(CountryDto::getCountryName)
                    .toList();
        } else if (cachedCountries == null){
            log.info("... could not fetched Countries, assigning back-up list");
            cachedCountries = List.of("United States", "Canada", "France", "India", "Japan", "United Kingdom");
        }
        return cachedCountries;
    }

    @Override
    public List<String> getCachedCountriesDto() {
        return cachedCountries;
    }

}
