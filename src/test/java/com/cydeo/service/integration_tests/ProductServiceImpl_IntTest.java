package com.cydeo.service.integration_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Product;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecuritySetUpUtil;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceImpl_IntTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MapperUtil mapperUtil;

    @BeforeEach
    void setUp() {
        SecuritySetUpUtil.setUpSecurityContext();
    }

    @Test
    void testGetProductById() {
        Long id = 1L;
        ProductDto productDto = productService.getProductById(id);
        assertNotNull(productDto);
        assertEquals(id, productDto.getId());
    }

    @Test
    void testGetProductById_NotFound() {
        Throwable throwable = catchThrowable(() -> productService.getProductById(0L));
        assertInstanceOf(ProductNotFoundException.class, throwable);
        assertThat(throwable.getMessage()).isEqualTo("Product not found");
    }

    @Test
    void testListAllProducts() {
        List<ProductDto> result = productService.listAllProducts();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        ProductDto productDto = productService.getProductById(2L);
        assertThat(result.get(0)).usingRecursiveComparison()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(productDto);
    }

    @Test
    void testSave() {
        ProductDto productDto = TestDocumentInitializer.getProduct();

        ProductDto savedProductDto = productService.save(productDto);

        assertNotNull(savedProductDto);
        assertEquals(productDto.getName(), savedProductDto.getName());
    }

    @Test
    void testUpdate() {
        ProductDto productDto = productService.getProductById(1L);
        productDto.setName("Updated Product");

        ProductDto updatedProductDto = productService.update(productDto);

        assertNotNull(updatedProductDto);
        assertEquals("Updated Product", updatedProductDto.getName());
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        productService.deleteById(id);

        Product product = productRepository.findById(id).orElseThrow();
        assertTrue(product.getIsDeleted());
    }

    @Test
    void testIsNameExist() {
        ProductDto productDto = productService.getProductById(2L);
        String name = "Apple iPhone-13";

        boolean result = productService.isNameExist(name, productDto.getId());

        assertTrue(result);
    }

    @Test
    void testIsNameExist_NotExist() {
        String name = "Non-existing Product Name";
        Long id = 1L;

        boolean result = productService.isNameExist(name, id);

        assertFalse(result);
    }
}

