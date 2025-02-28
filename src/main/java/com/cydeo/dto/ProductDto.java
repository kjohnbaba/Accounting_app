package com.cydeo.dto;

import com.cydeo.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {

    private Long id;
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product Name must be between 2 and 100 characters long.")
    @NotNull(message = "Name is required")
    private String name;

    private Integer quantityInStock;
    @Min(value = 1, message = "Low Limit Alert must be a non-negative number")
    private Integer lowLimitAlert;
    @NotNull(message = "Product Unit is required")
    private ProductUnit productUnit;
    @NotNull(message = "Please select a category")
    private CategoryDto category;
    private boolean hasInvoiceProduct;
}
