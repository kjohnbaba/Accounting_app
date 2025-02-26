package com.cydeo.service.unit_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.exception.ProductLowLimitAlertException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.service.impl.InvoiceProductServiceImpl;
import com.cydeo.util.MapperUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImpl_UnitTest {

    @Mock
    InvoiceProductRepository invoiceProductRepository;

    @Mock
    InvoiceService invoiceService;

    @Mock
    ProductService productService;

    @Mock
    CompanyService companyService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    InvoiceProductServiceImpl invoiceProductService;


    @Test
    void should_find_by_id_return_invoiceProduct_if_exists() {
        // given
        InvoiceProductDto dto = TestDocumentInitializer.getInvoiceProduct();
        InvoiceProduct entity = mapperUtil.convert(dto, new InvoiceProduct());
        // when
        when(invoiceProductRepository.findById(dto.getId())).thenReturn(Optional.of(entity));

        InvoiceProductDto actualResult = invoiceProductService.findById(dto.getId());

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("invoice.price", "invoice.tax")
                .isEqualTo(dto);
    }

    @Test
    void find_by_id_should_throw_exception_if_invoiceProduct_does_exist() {
        // when
        when(invoiceProductRepository.findById(anyLong())).thenReturn(Optional.empty());
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        Assertions.assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
        // or
        assertInstanceOf(InvoiceProductNotFoundException.class, throwable);

        assertEquals("InvoiceProduct not found", throwable.getMessage());
    }


    @Test
    void should_find_all_invoiceProducts_by_invoiceId_and_calculate_totalPrice() {
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoList.get(0).getProduct().setName("first");
        dtoList.get(1).getProduct().setName("second");
        dtoList.get(2).getProduct().setName("third");
        // list of entities with random sort
        List<InvoiceProduct> invoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .toList();
        List<InvoiceProductDto> expectedList = dtoList.stream()
                .peek(each -> each.setTotal(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity() * (each.getTax() + 100) / 100d))))
                .toList();

        // when
        when(invoiceProductRepository.findByInvoice_Id(any())).thenReturn(invoiceProducts);

        List<InvoiceProductDto> actualList = invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(1L);

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedList);
    }

    @Test
    void should_add_invoiceProducts_to_invoice() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        InvoiceDto invoiceDto = TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE);

        // when
        when(invoiceService.findById(anyLong())).thenReturn(invoiceDto);
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        InvoiceProductDto actualResult = invoiceProductService.add(invoiceProductDto, 1L);

        // then
        Assertions.assertThat(actualResult).usingRecursiveComparison()
                .withStrictTypeChecking()
                .ignoringExpectedNullFields()
                .ignoringFields("invoice.price", "invoice.tax")
                .isEqualTo(invoiceProductDto);
    }

    @Test
    void should_delete_invoiceProduct_from_invoice() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceProduct.setIsDeleted(false);

        // when
        when(invoiceProductRepository.findById(any())).thenReturn(Optional.ofNullable(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        invoiceProductService.delete(1L);

        // then
        assertThat(invoiceProduct.getIsDeleted()).isTrue();
    }

    @Test
    void should_update_remainingQuantity_of_invoiceProduct_upon_approval() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());

        // when
        when(invoiceProductRepository.findByInvoice_Id(any())).thenReturn(List.of(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);

        invoiceProductService.updateRemainingQuantityUponPurchaseApproval(1L);

        // then
        assertThat(invoiceProduct.getRemainingQuantity()).isEqualTo(5);
    }

    @Test
    void should_update_quantityInStock_when_purchased_invoiceProduct() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        Product product = mapperUtil.convert(productDto, new Product());

        // when
        when(invoiceProductRepository.getProductListByInvoiceId(any())).thenReturn(List.of(product));
        when(invoiceProductRepository.sumQuantityOfProducts(any(), any())).thenReturn(10);

        invoiceProductService.updateQuantityInStockPurchase(1L);

        // then
        assertThat(product.getQuantityInStock()).isEqualTo(20);
    }

    @Test
    void should_update_quantityInStock_if_stock_enough() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        Product product = mapperUtil.convert(productDto, new Product());

        // when
        when(invoiceProductRepository.getProductListByInvoiceId(any())).thenReturn(List.of(product));
        when(invoiceProductRepository.sumQuantityOfProducts(any(), any())).thenReturn(10);

        invoiceProductService.updateQuantityInStockSale(1L);

        // then
        assertThat(product.getQuantityInStock()).isZero();
    }

    @Test
    void updateQuantityInStockSale_should_throw_exception_if_stock_not_enough() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        Product product = mapperUtil.convert(productDto, new Product());

        // when
        when(invoiceProductRepository.getProductListByInvoiceId(any())).thenReturn(List.of(product));
        when(invoiceProductRepository.sumQuantityOfProducts(any(), any())).thenReturn(11);

        Throwable throwable = catchThrowable(() -> invoiceProductService.updateQuantityInStockSale(1L));

        // then
        assertInstanceOf(ProductNotFoundException.class, throwable);

        assertEquals("Stock of Test_Product is not enough to approve this invoice. Please update the invoice.", throwable.getMessage());
    }

    @Test
    void should_calculate_profitOrLoss() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        invoiceProductDto.getProduct().setId(1L);
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());

        // when
        when(invoiceProductRepository.findByInvoice_Id(any())).thenReturn(List.of(invoiceProduct));
        when(invoiceProductRepository.save(any())).thenReturn(invoiceProduct);
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        invoiceProductService.calculateProfitOrLoss(1L);

        // then
        assertThat(invoiceProduct.getProfitLoss()).isEqualTo(BigDecimal.valueOf(55.0));
    }

    @Test
    void should_invoke_low_quantity_alert() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        productDto.setQuantityInStock(4);
        productDto.setLowLimitAlert(5);
        Product product = mapperUtil.convert(productDto, new Product());

        // when
        when(invoiceProductRepository.getProductListByInvoiceId(any())).thenReturn(List.of(product));

        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(1L));

        // then
        assertInstanceOf(ProductLowLimitAlertException.class, throwable);
        assertEquals("Stock of [Test_Product] decreased below low limit", throwable.getMessage());
    }

    @Test
    void should_not_invoke_low_quantity_alert() {
        // given
        ProductDto productDto = TestDocumentInitializer.getProduct();
        productDto.setQuantityInStock(6);
        productDto.setLowLimitAlert(5);
        Product product = mapperUtil.convert(productDto, new Product());

        // when
        when(invoiceProductRepository.getProductListByInvoiceId(any())).thenReturn(List.of(product));

        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(1L));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void should_find_all_invoiceProducts_of_company() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        invoiceProductDto.getProduct().setId(1L);
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());

        // when
        when(invoiceProductRepository.getInvoiceProductsByCompany(any())).thenReturn(List.of(invoiceProduct));
        when(companyService.getCompanyDtoByLoggedInUser()).thenReturn(TestDocumentInitializer.getCompany(CompanyStatus.ACTIVE));

        List<InvoiceProductDto> actualResult = invoiceProductService.findAllApprovedInvoiceProductsOfCompany();

        // then
        assertThat(actualResult).hasSize(1);
    }
}
