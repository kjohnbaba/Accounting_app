package com.cydeo.service.integration_tests;

import com.cydeo.client.CountryClient;
import com.cydeo.dto.common.CountryDto;
import com.cydeo.dto.common.TokenDto;
import com.cydeo.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableAsync
@EnableScheduling
@ActiveProfiles("test")
@Disabled
class AddressServiceImpl_IntTest {

    @MockBean
    private CountryClient countryClient;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Value("${api.country.token}")
    private String token;

    @Value("${api.country.mail}")
    private String email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressService = new AddressServiceImpl(countryClient);
    }

    @Disabled
    @Test
    void testFetchCountryListAsync() {
        // Mock the authentication response
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAuthToken("test-token");
        when(countryClient.auth(null, null)).thenReturn(tokenDto);

        // Mock the country list response
        CountryDto country1 = new CountryDto();
        country1.setCountryName("Test Country 1");
        CountryDto country2 = new CountryDto();
        country2.setCountryName("Test Country 2");

        when(countryClient.getCountry(anyString())).thenReturn(
                new ResponseEntity<>(List.of(country1, country2), HttpStatus.OK)
        );

        // Execute the async method
        List<String> countries = addressService.fetchCountryListAsync();

        // Verify the results
        assertNotNull(countries);
        assertEquals(2, countries.size());
        assertEquals("Test Country 1", countries.get(0));
        assertEquals("Test Country 2", countries.get(1));
    }

    @Disabled
    @Test
    void testScheduledCountryList() {
        // Mock the authentication response
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAuthToken("test-token");
        when(countryClient.auth(anyString(), anyString())).thenReturn(tokenDto);

        // Mock the country list response
        CountryDto country1 = new CountryDto();
        country1.setCountryName("Test Country 1");
        CountryDto country2 = new CountryDto();
        country2.setCountryName("Test Country 2");

        when(countryClient.getCountry(anyString())).thenReturn(
                new ResponseEntity<>(List.of(country1, country2), HttpStatus.OK)
        );

        // Execute the scheduled method
        addressService.scheduledCountryList();

        // Verify the results
        List<String> countries = addressService.getCachedCountriesDto();
        assertNotNull(countries);
        assertEquals(2, countries.size());
        assertEquals("Test Country 1", countries.get(0));
        assertEquals("Test Country 2", countries.get(1));
    }

    @Disabled
    @Test
    void testGetCachedCountriesDtoWhenCacheIsNull() {
        // Mock the authentication response
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAuthToken("test-token");
        when(countryClient.auth(anyString(), anyString())).thenReturn(tokenDto);

        // Mock the country list response
        CountryDto country1 = new CountryDto();
        country1.setCountryName("Test Country 1");
        CountryDto country2 = new CountryDto();
        country2.setCountryName("Test Country 2");

        when(countryClient.getCountry(anyString())).thenReturn(
                new ResponseEntity<>(List.of(country1, country2), HttpStatus.OK)
        );

        // Initially, the cache should be null
        assertNull(addressService.getCachedCountriesDto());

        // Trigger the caching
        List<String> countries = addressService.getCachedCountriesDto();

        // Verify the results
        assertNotNull(countries);
        assertEquals(2, countries.size());
        assertEquals("Test Country 1", countries.get(0));
        assertEquals("Test Country 2", countries.get(1));
    }

    @Disabled
    @Test
    void testGetCachedCountriesDtoFallback() {
        // Mock the authentication response
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAuthToken("test-token");
        when(countryClient.auth(anyString(), anyString())).thenReturn(tokenDto);

        // Mock a failed country list response
        when(countryClient.getCountry(anyString())).thenReturn(
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
        );

        // Execute the method
        List<String> countries = addressService.getCachedCountriesDto();

        // Verify the fallback
        assertNotNull(countries);
        assertTrue(countries.contains("United States"));
        assertTrue(countries.contains("Canada"));
        assertTrue(countries.contains("France"));
        assertTrue(countries.contains("India"));
        assertTrue(countries.contains("Japan"));
        assertTrue(countries.contains("United Kingdom"));
    }
}

