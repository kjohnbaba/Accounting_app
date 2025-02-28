package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Company Name is required field.")
    private String clientVendorName;

    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @NotBlank(message = "Website should have a valid format.")
    private String website;

    @NotNull(message = "Please select type.")
    private ClientVendorType clientVendorType;

    @NotNull
    @Valid
    private AddressDto address;

    private CompanyDto company;

    private boolean hasInvoice;
}
