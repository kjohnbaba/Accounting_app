package com.cydeo.service.impl;


import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Product;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final MapperUtil mapperUtil;
    private final ProductRepository productRepository;
    private final CompanyService companyService;


    public ProductServiceImpl(MapperUtil mapperUtil, ProductRepository productRepository, CompanyService companyService) {
        this.mapperUtil = mapperUtil;
        this.productRepository = productRepository;
        this.companyService = companyService;
    }


    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return mapperUtil.convert(product, new ProductDto());
    }


    @Override
    public List<ProductDto> listAllProducts() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return productRepository.findByCompanyId(companyId)
                .stream()
                .sorted(Comparator.comparing((Product product) -> product.getCategory().getDescription())
                        .thenComparing(Product::getName))
                .map(p -> {
                    ProductDto dto = mapperUtil.convert(p, new ProductDto());
                    dto.setHasInvoiceProduct(!p.getInvoiceProducts().isEmpty());
                    return dto;
                })
                .toList();
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        productDto.getCategory().setCompany(companyService.getCompanyDtoByLoggedInUser());
        Product product = mapperUtil.convert(productDto, new Product());
        product = productRepository.save(product);
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(ProductNotFoundException::new);
        final int quantityInStock = productDto.getQuantityInStock() == null ?
                product.getQuantityInStock() : productDto.getQuantityInStock();
        productDto.setQuantityInStock(quantityInStock);
        product = productRepository.save(mapperUtil.convert(productDto, new Product()));
        return mapperUtil.convert(product, productDto);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public boolean isNameExist(String name, Long id) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        Product product = productRepository.findProductByNameAndCompanyId(name, companyId);
        if (product == null) {
            return false;
        }
        return !Objects.equals(product.getId(), id);
    }

}

