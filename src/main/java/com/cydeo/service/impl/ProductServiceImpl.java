package com.cydeo.service.impl;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, CompanyService companyService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto findById(Long id) {
        Product item = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(item, new ProductDto());
    }

    @Override
    public List<ProductDto> listAllProductsByUserCompanyId() {
        List<Product> list = productRepository.findAllProductsByCategory_Company_Id(companyService.getCompanyIdByLoggedInUser());
        return list.stream().map(each -> mapperUtil.convert(each, new ProductDto())).toList();
    }

    @Override
    public List<ProductDto> listAllProducts() {
        List<Product> list = productRepository.findAllByIsDeletedFalse();
        return list.stream().map(each -> mapperUtil.convert(each, new ProductDto())).toList();
    }

    @Override
    public void deleteProduct(Long id) throws Exception {
        Product product = productRepository.findByIdAndAndIsDeletedFalse(id);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id,ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).get();
        product.setName(productDto.getName());
        product.setLowLimitAlert(productDto.getLowLimitAlert());
        product.setProductUnit(productDto.getProductUnit());
        Category category = categoryRepository.findById(productDto.getCategory().getId()).get();
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        productRepository.save(product);
    }
}
