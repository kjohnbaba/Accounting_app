package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.entity.Category;
import com.cydeo.exception.CategoryNotFoundException;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        CategoryDto dto = mapperUtil.convert(category, new CategoryDto());
        dto.setHasProduct(!category.getProduct().isEmpty());
        return dto;
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        return categoryRepository.findByCompany_Id(companyService.getCompanyDtoByLoggedInUser().getId())
                .stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(c -> {
                    CategoryDto dto = mapperUtil.convert(c, new CategoryDto());
                    dto.setHasProduct(!c.getProduct().isEmpty());
                    return dto;
                })
                .toList();
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        categoryDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        Category category = mapperUtil.convert(categoryDto, new Category());
        Category saved = categoryRepository.save(category);
        return mapperUtil.convert(saved, new CategoryDto());
    }

    @Override
    public boolean isDescriptionExist(String description) {
        return categoryRepository.existsByDescriptionAndCompany_Id(description,
                companyService.getCompanyDtoByLoggedInUser().getId());
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(CategoryNotFoundException::new);
        category.setDescription(categoryDto.getDescription());
        Category saved = categoryRepository.save(category);
        return mapperUtil.convert(saved, new CategoryDto());
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "-" + category.getId());
        categoryRepository.save(category);
    }
}
