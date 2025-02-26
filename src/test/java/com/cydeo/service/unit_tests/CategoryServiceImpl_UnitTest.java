package com.cydeo.service.unit_tests;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.entity.Product;
import com.cydeo.exception.CategoryNotFoundException;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.impl.CategoryServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImpl_UnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        companyDto = new CompanyDto();
        companyDto.setId(1L);

        category = new Category();
        category.setId(1L);
        category.setDescription("Test Category");
        category.setCompany(mapperUtil.convert(companyDto, new Company()));
        category.setProduct(List.of(new Product()));

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setDescription("Test Category");
        categoryDto.setCompany(companyDto);
        categoryDto.setHasProduct(true);
    }

    @Test
    void findById_ShouldReturnCategoryDto() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.findById(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(categoryDto);
        assertThat(result.isHasProduct()).isTrue();
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void listAllCategories_ShouldReturnCategoryList() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(categoryRepository.findByCompany_Id(anyLong())).thenReturn(Arrays.asList(category));

        List<CategoryDto> result = categoryService.listAllCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(categoryDto);
    }

    @Test
    void save_ShouldReturnSavedCategory() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.save(categoryDto);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("hasProduct")
                .isEqualTo(categoryDto);
    }

    @Test
    void isDescriptionExist_ShouldReturnTrueIfDescriptionExists() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(categoryRepository.existsByDescriptionAndCompany_Id(anyString(), anyLong())).thenReturn(true);

        boolean result = categoryService.isDescriptionExist("Test Category");

        assertThat(result).isTrue();
    }

    @Test
    void isDescriptionExist_ShouldReturnFalseIfDescriptionDoesNotExist() {
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(companyDto);
        when(categoryRepository.existsByDescriptionAndCompany_Id(anyString(), anyLong())).thenReturn(false);

        boolean result = categoryService.isDescriptionExist("Test Category");

        assertThat(result).isFalse();
    }

    @Test
    void update_ShouldReturnUpdatedCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto result = categoryService.update(categoryDto);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("hasProduct")
                .isEqualTo(categoryDto);
    }

    @Test
    void update_ShouldThrowException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(categoryDto));
    }

    @Test
    void deleteById_ShouldMarkCategoryAsDeleted() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteById(1L);

        assertThat(category.getIsDeleted()).isTrue();
        assertThat(category.getDescription()).isEqualTo("Test Category-1");
        verify(categoryRepository, times(1)).save(category);
    }
}
