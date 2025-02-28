package com.cydeo.service;

import com.cydeo.dto.ProductDto;

import java.util.List;


public interface ProductService {

    ProductDto findById(Long id);
    List<ProductDto> listAllProductsByUserCompanyId();
    List<ProductDto> listAllProducts();
    void deleteProduct(Long id) throws Exception;
    void updateProduct (Long id, ProductDto productDto);
    void createProduct(ProductDto productDto);
}
