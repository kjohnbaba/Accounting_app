package com.cydeo.service.integration_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecuritySetUpUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class CategoryServiceImpl_IntTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testFindById() {
        CategoryDto foundCategory = categoryService.findById(1L);

        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(1L);
    }

    @Test
    void testListAllCategories() {
        List<CategoryDto> categories = categoryService.listAllCategories();

        assertThat(categories).isNotEmpty();
    }

    @Test
    void testSaveCategory() {
        CategoryDto categoryDto = TestDocumentInitializer.getCategory();
        categoryDto.setDescription("New Category");

        CategoryDto savedCategory = categoryService.save(categoryDto);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getDescription()).isEqualTo("New Category");
    }

    @Test
    void testUpdateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setDescription("Updated Description");

        CategoryDto updatedCategory = categoryService.update(categoryDto);

        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testDeleteById() {
        categoryService.deleteById(1L);

        Category deletedCategory = categoryRepository.findById(1L).orElse(null);

        assertThat(deletedCategory).isNotNull();
        assertThat(deletedCategory.getIsDeleted()).isTrue();
    }
}

