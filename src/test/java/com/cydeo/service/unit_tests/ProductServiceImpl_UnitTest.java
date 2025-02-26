package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.impl.ProductServiceImpl;
import com.cydeo.util.MapperUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpl_UnitTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CompanyService companyService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void getProductById_shouldReturnProduct() {
        // given
        ProductDto dto = TestDocumentInitializer.getProduct();
        Product entity = mapperUtil.convert(dto, new Product());
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        ProductDto actualResult = productService.getProductById(anyLong());

        // then
        assertThat(actualResult).usingRecursiveComparison()
                .isEqualTo(dto);
        verify(productRepository, times(1)).findById(anyLong());

    }

    @Test
    void getProductById_ShouldThrowException() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> productService.getProductById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
        // or
        assertInstanceOf(ProductNotFoundException.class, throwable);

        assertEquals("Product not found", throwable.getMessage());
    }


    @Test
    void listAllProducts_ShouldReturnProductList() {
        // given
        List<ProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getProduct(),
                TestDocumentInitializer.getProduct(),
                TestDocumentInitializer.getProduct()
        );
        dtoList.get(0).getCategory().setDescription("samsung");
        dtoList.get(1).getCategory().setDescription("apple");
        dtoList.get(2).getCategory().setDescription("apple");
        dtoList.get(0).setName("Galaxy");
        dtoList.get(1).setName("Mac Pro");
        dtoList.get(2).setName("Iphone");
        // list of entities with random sort
        List<Product> products = dtoList.stream()
                .map(dto -> {
                    Product product = mapperUtil.convert(dto, new Product());
                    product.setInvoiceProducts(List.of(new InvoiceProduct()));
                    return product;
                })
                .toList();
        List<ProductDto> expectedList = dtoList.stream()
                .sorted(Comparator.comparing((ProductDto product) -> product.getCategory().getDescription())
                        .thenComparing(ProductDto::getName))
                .toList();

        // when
        when(productRepository.findByCompanyId(any())).thenReturn(products);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(dtoList.get(0).getCategory().getCompany());

        List<ProductDto> actualList = productService.listAllProducts();

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(expectedList);
    }

    @Test
    void save_ShouldReturnSavedProduct() {
        // given
        ProductDto dto = TestDocumentInitializer.getProduct();
        Product entity = mapperUtil.convert(dto, new Product());

        // when
        when(productRepository.save(any())).thenReturn(entity);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(dto.getCategory().getCompany());

        ProductDto actualResult = productService.save(dto);

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(dto);
    }

    @Test
    void update_ShouldReturnUpdatedProduct() {
        // given
        ProductDto dto = TestDocumentInitializer.getProduct();
        Product entity = mapperUtil.convert(dto, new Product());

        // when
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        when(productRepository.save(any())).thenReturn(entity);

        ProductDto actualResult = productService.update(dto);

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("hasInvoiceProduct")
                .isEqualTo(dto);
        assertThat(actualResult.getQuantityInStock()).isNotNull();
    }

    @Test
    void deleteById_ShouldSetProductAsDeleted() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        Product product = mapperUtil.convert(productDto, new Product());
        product.setIsDeleted(false);

        // when
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        productService.deleteById(1L);

        // then
        assertThat(product.getIsDeleted()).isTrue();
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteById_should_throw_exception_if_product_does_exist() {
        // when
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> productService.getProductById(1L));

        // then
        Assertions.assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
        // or
        assertInstanceOf(ProductNotFoundException.class, throwable);

        assertEquals("Product not found", throwable.getMessage());
    }

    @Test
    void isNameExist_ShouldReturnTrueIfNameExists() {
        // given
        ProductDto dto = TestDocumentInitializer.getProduct();
        Product entity = mapperUtil.convert(dto, new Product());
        entity.setName("product");
        entity.setId(1L);
        CompanyDto mockCompanyDto = new CompanyDto();
        mockCompanyDto.setId(2L);

        // when
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(mockCompanyDto);
        when(productRepository.findProductByNameAndCompanyId(anyString(), anyLong())).thenReturn(entity);

        boolean actualResult = productService.isNameExist("product", 2L);

        // then
       assertThat(actualResult).isTrue();
    }

    @Test
    void isNameExist_ShouldReturnFalseIfNameDoesNotExist() {
        // given
        ProductDto dto = TestDocumentInitializer.getProduct();
        Product entity = mapperUtil.convert(dto, new Product());
        entity.setName("product");
        entity.setId(1L);
        CompanyDto mockCompanyDto = new CompanyDto();
        mockCompanyDto.setId(2L);

        // when
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(mockCompanyDto);
        when(productRepository.findProductByNameAndCompanyId(anyString(), anyLong())).thenReturn(entity);

        boolean actualResult = productService.isNameExist("product", 1L);

        // then
        assertThat(actualResult).isFalse();
    }

}
