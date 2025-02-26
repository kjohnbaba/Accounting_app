package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;

    @NotBlank(message = "Address is a required field.")
    @Size( min = 2, max = 100, message = "Address should be 2-100 characters long.")
    private String addressLine1;

    @Size(max = 100, message = "Address should be maximum 100 characters long.")
    private String addressLine2;

    @NotBlank(message = "City is a required field.")
    @Size(min = 2, max = 50, message = "City should be 2-50 characters long.")
    private String city;

    @NotBlank(message = "State is a required field.")
    @Size(min = 2, max = 50, message = "State should be 2-50 characters long.")
    private String state;

    @NotBlank(message = "Country is a required field.")
    @Size( min = 2, max = 100, message = "Country should be 2-100 characters long.")
    private String country;

    @NotBlank(message = "Zip Code is a required field.")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Please put in proper format example: (*****-****)")
    private String zipCode;

}
