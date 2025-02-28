package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Category;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final ProductService productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, SecurityService securityService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.productService = productService;
    }

    @Transactional
    @Override
    public List<CategoryDto> listCategoriesByUserCompany() {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<Category> categories = categoryRepository.findAllByCompany_IdAndIsDeletedFalseOrderByDescriptionAsc(companyId);
        return categories.stream()
                .map(category -> mapperUtil.convert(category, new CategoryDto())).collect(Collectors.toList());
    }

    @Override
    public void save(CategoryDto categoryDto) {
        UserDto user = securityService.getLoggedInUser();
        categoryDto.setCompany(user.getCompany());
        categoryRepository.save(mapperUtil.convert(categoryDto,new Category()));
    }

    @Override
    public void update(CategoryDto categoryDto) {
        Category dbCategory = categoryRepository.findById(categoryDto.getId()).get();
        Category updatedCategory = mapperUtil.convert(categoryDto,new Category());
        updatedCategory.setId(dbCategory.getId());
        updatedCategory.setCompany(dbCategory.getCompany());
        categoryRepository.save(updatedCategory);

    }

    @Override
    public CategoryDto findByCategoryId(long id) {
        Category category = categoryRepository.findById(id).get();
        return mapperUtil.convert(category,new CategoryDto());
    }

    @Override
    public List<CategoryDto> listCategories() {
        List<Category> categories = categoryRepository.findAllByIsDeletedFalse();
        return categories.stream()
                .map(category -> mapperUtil.convert(category, new CategoryDto())).collect(Collectors.toList());
    }

}
