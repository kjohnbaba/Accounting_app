package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanyDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title should be 2-100 characters long.")
    private String title;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @NotBlank(message = "Website is required")
    private String website;

    @NotNull
    @Valid
    private AddressDto address;

    private CompanyStatus companyStatus;


}
