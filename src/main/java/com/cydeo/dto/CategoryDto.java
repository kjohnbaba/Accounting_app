package com.cydeo.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Description is a required field.")
    @Size(min = 2, max = 200, message = "Description should have 2-100 characters long.")
    private String description;

    private CompanyDto company;

    private boolean hasProduct;

}
