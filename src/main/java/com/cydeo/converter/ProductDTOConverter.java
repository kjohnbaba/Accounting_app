package com.cydeo.converter;

import com.cydeo.dto.ProductDto;

import com.cydeo.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;



@Component
public class ProductDtoConverter implements Converter<String, ProductDto> {

    public final ProductService productService;

    public ProductDtoConverter(@Lazy ProductService productService) {
        this.productService = productService;
    }


    @Override
    public ProductDto convert(String source) {
        if (source.isEmpty()) return null;
        return productService.findById(Long.parseLong(source));
    }

}
