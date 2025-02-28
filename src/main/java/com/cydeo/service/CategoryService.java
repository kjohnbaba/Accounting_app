package com.cydeo.service;

import com.cydeo.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    public List<CategoryDto> listCategoriesByUserCompany();
    void save(CategoryDto categoryDto);
    void update(CategoryDto categoryDto);
    CategoryDto findByCategoryId(long id);
    public List<CategoryDto> listCategories();

}
