package com.cydeo.service.integration_tests;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.exception.ProductLowLimitAlertException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@Transactional
//@WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
class InvoiceProductServiceImpl_IntTest {

    @Autowired
    InvoiceProductRepository invoiceProductRepository;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ProductService productService;

    @Autowired
    CompanyService companyService;

    @Autowired
    InvoiceProductService invoiceProductService;

    @BeforeEach
    void setUp(){
        SecuritySetUpUtil.setUpSecurityContext();
    }


    @Test
    void findById_shouldReturnInvoiceProduct() {
        // when
        InvoiceProductDto actualResult = invoiceProductService.findById(1L);
        // then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getPrice()).isCloseTo(BigDecimal.valueOf(250.00), withinPercentage(5));
    }

    @Test
    void findById_shouldThrowException() {
        // when
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(0L));
        // then
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
        // or
        assertInstanceOf(InvoiceProductNotFoundException.class, throwable);

        assertEquals("InvoiceProduct not found", throwable.getMessage());
    }


    @Test
    void findAllByInvoiceIdAndCalculateTotalPrice_shouldCalculateTotalPrice() {
        // when
        List<InvoiceProductDto> actualList = invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(1L);

        // then
        assertThat(actualList.get(0).getTotal()).isCloseTo(BigDecimal.valueOf(1275), withinPercentage(10L));
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
        //org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a];
        // constraint [insert_user_id" of relation "invoice_products]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement
    void should_add_invoiceProducts_to_invoice() {
        // given
        InvoiceProductDto invoiceProductDto = TestDocumentInitializer.getInvoiceProduct();
        // when
        InvoiceProductDto actualResult = invoiceProductService.add(invoiceProductDto, 1L);

        // then
        assertThat(actualResult.getQuantity()).isEqualTo(5);
        assertThat(actualResult.getPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void should_delete_invoiceProduct_from_invoice() {
        // when
        invoiceProductService.delete(2L);

        // then
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(2L).orElseThrow();
        assertThat(invoiceProduct.getIsDeleted()).isTrue();
    }

    @Test
    void updateRemainingQuantityOfUponPurchaseInvoiceApproval_shouldUpdateRemainingQuantity() {
        // when
        invoiceProductService.updateRemainingQuantityUponPurchaseApproval(13L);

        // then
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoice_Id(13L);
        invoiceProducts.forEach(
                ip -> assertThat(ip.getRemainingQuantity()).isPositive()
        );
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    // java.lang.NullPointerException: Cannot invoke "org.springframework.security.core.Authentication.getName()" because the return value of "org.springframework.security.core.context.SecurityContext.getAuthentication()" is null
    void updateQuantityInStockPurchase_whenApproved() {
        // given
        ProductDto product = productService.getProductById(1L);
        int initialQty = product.getQuantityInStock();

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        InvoiceDto savedInvoice = invoiceService.saveInvoice(invoiceDto, InvoiceType.PURCHASE);

        InvoiceProductDto dto = new InvoiceProductDto();
        dto.setProduct(product);
        dto.setInvoice(savedInvoice);
        dto.setQuantity(10);
        invoiceProductService.add(dto, savedInvoice.getId());

        // when
        invoiceProductService.updateQuantityInStockPurchase(savedInvoice.getId());

        // then
        product = productService.getProductById(1L);
        assertThat(product.getQuantityInStock()).isEqualTo(initialQty + 10);
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    void updateQuantityInStockSale_whenApprovedAndStockEnough() {
        // given
        ProductDto product = productService.getProductById(1L);
        int initialQty = product.getQuantityInStock();
        // when
        invoiceProductService.updateQuantityInStockSale(4L);

        // then
        product = productService.getProductById(1L);
        assertThat(product.getQuantityInStock()).isEqualTo(initialQty - 2);
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    void updateQuantityInStockSale_shouldThrowException_StockNotEnough() {
        // given
        ProductDto product = productService.getProductById(1L);
        product.setQuantityInStock(1);
        productService.save(product);

        // when
        Throwable throwable = catchThrowable(() -> invoiceProductService.updateQuantityInStockSale(4L));

        // then
        assertInstanceOf(ProductNotFoundException.class, throwable);

        assertEquals("Stock of HP Elite 800G1 Desktop Computer Package is not enough to approve this invoice. " +
                "Please update the invoice.", throwable.getMessage());
    }

    @Test
    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    void calculateProfitOrLoss_withSuccess() {
        // when
//        SecurityTestUtil.setUpSecurityContext();
        invoiceProductService.calculateProfitOrLoss(4L);

        // then
        List<InvoiceProductDto> invoiceProductDtos = invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(4L);
        assertThat(invoiceProductDtos).isNotEmpty();
        invoiceProductDtos
                .forEach(ip -> assertThat(ip.getProfitLoss()).isNotEqualTo(BigDecimal.ZERO));
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    void should_invoke_low_quantity_alert() {
        // given
        ProductDto product = productService.getProductById(1L);
        product.setQuantityInStock(1);
        product.setLowLimitAlert(5);
        productService.save(product);

        // when
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(4L));

        // then
        assertInstanceOf(ProductLowLimitAlertException.class, throwable);
        assertEquals("Stock of [HP Elite 800G1 Desktop Computer Package] decreased below low limit", throwable.getMessage());
    }

    @Test
//    @WithMockUser(username = "manager@greentech.com", password = "Abc1", roles = "MANAGER")
    void should_not_invoke_low_quantity_alert() {
        // given
//        SecurityTestUtil.setUpSecurityContext();
        ProductDto product = productService.getProductById(1L);
        product.setQuantityInStock(6);
        product.setLowLimitAlert(5);
        productService.save(product);

        // when
        Throwable throwable = catchThrowable(() -> invoiceProductService.checkForLowQuantityAlert(4L));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void findAllApprovedInvoiceProductsOfCompany() {
        // when
        List<InvoiceProductDto> actualResult = invoiceProductService.findAllApprovedInvoiceProductsOfCompany();

        // then
        assertThat(actualResult).hasSize(3);
    }
}
