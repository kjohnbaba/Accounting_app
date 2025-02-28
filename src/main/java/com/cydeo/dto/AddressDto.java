package com.cydeo.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AddressDto {

    private Long id;

    @NotBlank(message = "Address Line 1 is required.")
    @Size(min = 2, max = 100, message = "Address should have 2-100 characters.")
    private String addressLine1;

    @Size(max = 100, message = "Address Line 2 should have a maximum of 100 characters.")
    private String addressLine2;

    @NotBlank(message = "City is required.")
    @Size(min = 2, max = 50, message = "City should have 2-50 characters.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(min = 2, max = 50, message = "State should have 2-50 characters.")
    private String state;

    @NotBlank(message = "Country is required.")
    @Size(min = 2, max = 50, message = "Country should have 2-50 characters.")
    private String country;

    @NotBlank(message = "Zip Code is required.")
    @Pattern(regexp = "^\\d{5}(?:-\\d{4})?$", message = "Zip code should be in the format 'XXXXX' or 'XXXXX-XXXX'.")
    private String zipCode;
}
