package com.cydeo.service.unit_tests;

import com.cydeo.client.CountryClient;
import com.cydeo.dto.common.CountryDto;
import com.cydeo.dto.common.TokenDto;
import com.cydeo.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImpl_UnitTest {

    @Mock
    private CountryClient countryClient;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Value("${api.country.email}")
    private String email;
    @Value("${api.country.token}")
    private String token;

    @Test
    void testGetCountryList_Success() {
        // Mock the response from the client
        List<CountryDto> mockCountryList = List.of(
                new CountryDto("United States"),
                new CountryDto("Canada")
        );
        ResponseEntity<List<CountryDto>> mockResponse = new ResponseEntity<>(mockCountryList, HttpStatus.OK);

        // Mock the TokenDto response
        TokenDto mockTokenDto = new TokenDto();
        mockTokenDto.setAuthToken("mockAuthToken");

        // Mock the behavior of the countryClient
        when(countryClient.auth(email, token)).thenReturn(mockTokenDto);
        when(countryClient.getCountry("Bearer mockAuthToken")).thenReturn(mockResponse);

        // Call the method to fetch the country list
        List<String> fetchedCountries = addressService.fetchCountryListAsync();

        // Assert that the fetched list is correct
        assertThat(fetchedCountries).containsExactly("United States", "Canada");

        // Call the method to test
        List<String> result = addressService.getCachedCountriesDto();

        // Assert the expected result
        assertThat(result).containsExactly("United States", "Canada");
    }

    @Test
    void testGetCountryList_Failure() {
        // Mock the TokenDto response
        TokenDto mockTokenDto = new TokenDto();
        mockTokenDto.setAuthToken("mockAuthToken");
        when(countryClient.auth(email, token)).thenReturn(mockTokenDto);

        // Mock the response from the client
        ResponseEntity<List<CountryDto>> mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(countryClient.getCountry("Bearer mockAuthToken")).thenReturn(mockResponse);

        // Call the method to fetch the country list
        List<String> fetchedCountries = addressService.fetchCountryListAsync();

        // Assert that the default list is assigned
        assertThat(fetchedCountries).containsExactly("United States", "Canada", "France", "India", "Japan", "United Kingdom");

        // Call the method to test
        List<String> result = addressService.getCachedCountriesDto();

        // Assert the expected result
        assertThat(result).containsExactly("United States", "Canada", "France", "India", "Japan", "United Kingdom");
    }
}

