package com.cydeo.converter;


import com.cydeo.dto.ProductDto;
import com.cydeo.service.ProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConverter implements Converter<String, ProductDto> {

    private final ProductService productService;

    public ProductDTOConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDto convert(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return productService.getProductById(Long.parseLong(id));
    }
}
