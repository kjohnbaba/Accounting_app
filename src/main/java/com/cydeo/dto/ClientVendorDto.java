package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientVendorDto {
    private Long id;

    @Size(min = 2, max = 50, message = "Company Name should have 2-50 characters long.")
    @NotBlank(message = "Company Name is a required field.")
    private String clientVendorName;

    //    @NotBlank(message = "Phone Number is a required field.")  // @Pattern is enough to check if it is not blank
//    imported from https://www.baeldung.com/java-regex-validate-phone-numbers :
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

//    @URL(protocol = "https", message = "Website should have a valid format.")
    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})(:[0-9]+)?(/[^/]+)*$", message = "Website should have a valid format.") //chatGPT
    private String website;

    @NotNull(message = "Please select type.")
    private ClientVendorType clientVendorType;

    @NotNull
    @Valid
    private AddressDto address;

    private CompanyDto company;

    private boolean hasInvoice;

}
