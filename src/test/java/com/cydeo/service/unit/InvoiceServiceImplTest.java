package com.cydeo.service.unit;

import com.cydeo.dto.*;
import com.cydeo.entity.*;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exceptions.ProductLowLimitAlertException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.InvoiceServiceImpl;
import com.cydeo.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private MapperUtil mapperUtil;
    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InvoiceProductService invoiceProductService;
    @Mock
    private CompanyService companyService;
    @Mock
    ClientVendorService clientVendorService;
    @Mock
    SecurityService securityService;
    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInvoiceByInvoiceTypeSales() {
        Long companyId = 1L;
        when(companyService.getCompanyIdByLoggedInUser()).thenReturn(companyId);
        when(invoiceRepository.countAllByInvoiceTypeAndCompany_Id(InvoiceType.SALES, companyId)).thenReturn(3);
        InvoiceDto result = invoiceService.createInvoiceByInvoiceType(InvoiceType.SALES);
        assertNotNull(result);
        assertEquals(InvoiceType.SALES, result.getInvoiceType());
        assertEquals("S-004", result.getInvoiceNo());
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    void createInvoiceByInvoiceTypePurchase() {
        Long companyId = 1L;
        when(companyService.getCompanyIdByLoggedInUser()).thenReturn(companyId);
        when(invoiceRepository.countAllByInvoiceTypeAndCompany_Id(InvoiceType.PURCHASE, companyId)).thenReturn(5);
        InvoiceDto result = invoiceService.createInvoiceByInvoiceType(InvoiceType.PURCHASE);
        assertNotNull(result);
        assertEquals(InvoiceType.PURCHASE, result.getInvoiceType());
        assertEquals("P-006", result.getInvoiceNo());
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    void findById() {
        Long id = 1L;
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId(id);
        when(invoiceRepository.findById(id)).thenReturn(Optional.of(expectedInvoice));
        Optional<Invoice> actualInvoiceOpt = invoiceRepository.findById(id);
        assertTrue(actualInvoiceOpt.isPresent(), "Invoice should be found");
        Invoice actualInvoice = actualInvoiceOpt.get();
        assertEquals(expectedInvoice.getId(), actualInvoice.getId(), "Invoice ID should match");
    }

    @Test
    void listInvoiceByInvoiceNo() {

        String invoiceNo = "INV123";
        Long companyId = 1L;

        UserDto userDto = mock(UserDto.class);
        CompanyDto companyDto = mock(CompanyDto.class);
        companyDto.setId(companyId);
        userDto.setCompany(companyDto);

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(invoiceNo);
        invoiceDto.setCompany(companyDto);

        Invoice invoice = new Invoice();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(userDto.getCompany()).thenReturn(companyDto);
        when(companyDto.getId()).thenReturn(companyId);

        when(invoiceRepository.findFirstByInvoiceNoAndCompany_Id(invoiceNo, companyId)).thenReturn(invoice);
        when(mapperUtil.convert(eq(invoice), any(InvoiceDto.class))).thenReturn(invoiceDto);

        InvoiceDto actualDto = invoiceService.listInvoiceByInvoiceNo(invoiceNo);

        verify(securityService).getLoggedInUser();
        verify(userDto).getCompany();
        verify(companyDto).getId();
        verify(invoiceRepository).findFirstByInvoiceNoAndCompany_Id(invoiceNo, companyId);
        verify(mapperUtil).convert(eq(invoice), any(InvoiceDto.class));

        assertNotNull(actualDto, "The result should not be null");
        assertEquals(invoiceNo, actualDto.getInvoiceNo(), "Invoice number should match");
        assertEquals(companyId, actualDto.getCompany().getId(), "Company ID should match");
    }


    @Test
    void listInvoices() {
        Long companyId = 1L;
        InvoiceType invoiceType = InvoiceType.PURCHASE;

        when(companyService.getCompanyIdByLoggedInUser()).thenReturn(companyId);

        List<Invoice> mockInvoices = new ArrayList<>();
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setInvoiceNo("INV001");
        Company company = new Company();
        company.setId(companyId);
        invoice1.setCompany(company);
        mockInvoices.add(invoice1);

        InvoiceDto invoiceDto1 = new InvoiceDto();
        invoiceDto1.setInvoiceNo("INV001");
        invoiceDto1.setId(1L);

        when(invoiceRepository.findAllByCompany_IdAndInvoiceTypeAndIsDeleted(companyId, invoiceType, false))
                .thenReturn(mockInvoices);

        when(mapperUtil.convert(eq(invoice1), any(InvoiceDto.class))).thenReturn(invoiceDto1);

        List<InvoiceProductDto> invoiceProducts = new ArrayList<>();
        InvoiceProductDto invoiceProductDto = new InvoiceProductDto();
        invoiceProductDto.setId(1L);
        invoiceProductDto.setPrice(BigDecimal.valueOf(100));
        invoiceProductDto.setQuantity(2);
        invoiceProductDto.setTotal(BigDecimal.valueOf(220));
        invoiceProducts.add(invoiceProductDto);

        when(invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(1L))
                .thenReturn(invoiceProducts);

        List<InvoiceDto> result = invoiceService.listInvoices(invoiceType);

        assertThat(result.get(0).getInvoiceNo()).isEqualTo("INV001");
        assertThat(result.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(200)); // Without tax
        assertThat(result.get(0).getTax()).isEqualTo(BigDecimal.valueOf(20)); // Tax amount
        assertThat(result.get(0).getTotal()).isEqualTo(BigDecimal.valueOf(220)); // Total price with tax

        verify(companyService).getCompanyIdByLoggedInUser();
        verify(invoiceRepository).findAllByCompany_IdAndInvoiceTypeAndIsDeleted(companyId, invoiceType, false);
        verify(mapperUtil).convert(eq(invoice1), any(InvoiceDto.class));
        verify(invoiceProductService).listAllByInvoiceIdAndCalculateTotalPrice(1L);
    }


    @Test
    void deleteInvoice() throws Exception {
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setIsDeleted(false);
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        invoiceService.deleteInvoice(invoiceId);
        assertTrue(invoice.getIsDeleted(), "Invoice should be marked as deleted.");
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void deleteInvoice_WhenInvoiceDoesNotExist_ShouldThrowException() {
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> invoiceService.deleteInvoice(invoiceId));
        assertEquals("Invoice not found with ID: " + invoiceId, exception.getMessage());
    }

    @Test
    void approvePurchaseInvoice_WhenInvoiceExistsAndIsApprovable() throws Exception {
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setIsDeleted(false);

        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setQuantity(10);
        invoiceProduct.setRemainingQuantity(5);

        Product product = new Product();
        product.setQuantityInStock(50);
        invoiceProduct.setProduct(product);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceProductRepository.findByInvoiceAndIsDeletedFalse(invoice)).thenReturn(List.of(invoiceProduct));
        when(invoiceRepository.save(invoice)).thenReturn(invoice); // Return the invoice after save
        when(productRepository.save(product)).thenReturn(product); // Return the product after save
        when(invoiceProductRepository.save(invoiceProduct)).thenReturn(invoiceProduct); // Return the invoice product after save

        invoiceService.approvePurchaseInvoice(invoiceId);

        verify(invoiceRepository).save(invoice);
        verify(invoiceProductRepository).findByInvoiceAndIsDeletedFalse(invoice);
        verify(productRepository).save(product);
        verify(invoiceProductRepository).save(invoiceProduct);

        assertEquals(InvoiceStatus.APPROVED, invoice.getInvoiceStatus());
        assertEquals(LocalDate.now(), invoice.getDate());
        assertEquals(60, product.getQuantityInStock());
        assertEquals(15, invoiceProduct.getRemainingQuantity());
    }

    @Test
    void approvePurchaseInvoice_WhenInvoiceNotFound() {
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.approvePurchaseInvoice(invoiceId);
        });

        assertEquals("Invoice not found with ID: " + invoiceId, exception.getMessage());
    }

    @Test
    void approvePurchaseInvoice_WhenInvoiceDoesNotExist() {
        Long id = 1L;
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> invoiceService.approvePurchaseInvoice(id));
        assertEquals("Invoice not found with ID: " + id, exception.getMessage());
    }

    @Test
    void savePurchaseInvoice() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());

        UserDto loggedInUser = new UserDto();
        CompanyDto companyDto = new CompanyDto();
        loggedInUser.setCompany(companyDto);

        Invoice convertedInvoice = new Invoice();

        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(mapperUtil.convert(eq(invoiceDto), any(Invoice.class))).thenReturn(convertedInvoice);

        invoiceService.savePurchaseInvoice(invoiceDto);

        verify(securityService).getLoggedInUser();
        verify(mapperUtil).convert(eq(invoiceDto), any(Invoice.class));
        verify(invoiceRepository).save(convertedInvoice);

        assertEquals(InvoiceType.PURCHASE, invoiceDto.getInvoiceType());
        assertEquals(InvoiceStatus.AWAITING_APPROVAL, invoiceDto.getInvoiceStatus());
        assertEquals(LocalDate.now(), invoiceDto.getDate());
        assertEquals(companyDto, invoiceDto.getCompany());
    }


    @Test
    void saveSaleInvoice() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);

        invoiceDto.setDate(LocalDate.now());
        UserDto loggedInUser = new UserDto();
        CompanyDto companyDto = new CompanyDto();
        loggedInUser.setCompany(companyDto);

        Invoice convertedInvoice = new Invoice();

        when(securityService.getLoggedInUser()).thenReturn(loggedInUser);
        when(mapperUtil.convert(eq(invoiceDto), any(Invoice.class))).thenReturn(convertedInvoice);

        invoiceService.saveSaleInvoice(invoiceDto);

        verify(securityService).getLoggedInUser();
        verify(mapperUtil).convert(eq(invoiceDto), any(Invoice.class));
        verify(invoiceRepository).save(convertedInvoice);

        assertEquals(InvoiceType.SALES, invoiceDto.getInvoiceType());
        assertEquals(InvoiceStatus.AWAITING_APPROVAL, invoiceDto.getInvoiceStatus());
        assertEquals(LocalDate.now(), invoiceDto.getDate());
        assertEquals(companyDto, invoiceDto.getCompany());


    }

    @Test
    void updateInvoice() {

        Long invoiceId = 1L;
        Long clientVendorId = 2L;
        Invoice invoice = new Invoice();
        ClientVendor clientVendor = new ClientVendor();
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(clientVendorId);

        when(invoiceRepository.findById(eq(invoiceId))).thenReturn(Optional.of(invoice));
        when(clientVendorService.findById(eq(clientVendorId))).thenReturn(clientVendorDto);
        when(mapperUtil.convert(eq(clientVendorDto), any(ClientVendor.class))).thenReturn(clientVendor);

        invoiceService.updateInvoice(invoiceId, clientVendorDto);

        verify(invoiceRepository).findById(eq(invoiceId));
        verify(clientVendorService).findById(eq(clientVendorId));
        verify(mapperUtil).convert(eq(clientVendorDto), any(ClientVendor.class));
        verify(invoiceRepository).save(invoice);

        assertThat(invoice.getClientVendor()).isEqualTo(clientVendor);
    }


    @Test
    void findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal() throws Exception {
        Long invoiceId = 1L;

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);

        List<InvoiceProduct> invoiceProducts = new ArrayList<>();

        // Product 1
        InvoiceProduct product1 = new InvoiceProduct();
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setQuantity(2);
        product1.setTax(10); // 10% tax
        invoiceProducts.add(product1);

        // Product 2
        InvoiceProduct product2 = new InvoiceProduct();
        product2.setPrice(BigDecimal.valueOf(50));
        product2.setQuantity(1);
        product2.setTax(20); // 20% tax
        invoiceProducts.add(product2);

        // Set the products to the invoice
        invoice.setInvoiceProducts(invoiceProducts);

        // Mock repository to return the invoice
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        // Mock the mapperUtil to convert Invoice to InvoiceDto
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoiceId);
        when(mapperUtil.convert(eq(invoice), any(InvoiceDto.class))).thenReturn(invoiceDto);

        // Act
        InvoiceDto result = invoiceService.findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(invoiceId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(250));  // Subtotal
        assertThat(result.getTax()).isEqualTo(BigDecimal.valueOf(30));     // Tax is  30
        assertThat(result.getTotal()).isEqualTo(BigDecimal.valueOf(280));  // Total: 250 + 30

        // Verify that the repository and mapper were called
        verify(invoiceRepository).findById(invoiceId);
        verify(mapperUtil).convert(eq(invoice), any(InvoiceDto.class));
    }

    @Test
    void findInvoiceProductsAndCalculateTotal() throws Exception {
        Long invoiceId = 1L;

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);

        List<InvoiceProduct> invoiceProducts = new ArrayList<>();

        InvoiceProduct product1 = new InvoiceProduct();
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setQuantity(2);
        product1.setTax(10); // 10% tax
        invoiceProducts.add(product1);

        // Product 2
        InvoiceProduct product2 = new InvoiceProduct();
        product2.setPrice(BigDecimal.valueOf(50));
        product2.setQuantity(1);
        product2.setTax(20); // 20% tax
        invoiceProducts.add(product2);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceProductRepository.findByInvoiceAndIsDeletedFalse(invoice)).thenReturn(invoiceProducts);

        // Mock the conversion of each InvoiceProduct to InvoiceProductDto
        when(mapperUtil.convert(eq(product1), any(InvoiceProductDto.class))).thenAnswer(invocation -> {
            InvoiceProductDto dto = new InvoiceProductDto();
            dto.setId(1L);
            dto.setPrice(product1.getPrice());
            dto.setQuantity(product1.getQuantity());
            BigDecimal price = product1.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(product1.getQuantity());
            BigDecimal tax = price.multiply(quantity).multiply(BigDecimal.valueOf(product1.getTax())).divide(BigDecimal.valueOf(100));
            BigDecimal total = price.multiply(quantity).add(tax);
            dto.setTotal(total); // Total should be 220 (price * quantity + tax)
            return dto;
        });

        when(mapperUtil.convert(eq(product2), any(InvoiceProductDto.class))).thenAnswer(invocation -> {
            InvoiceProductDto dto = new InvoiceProductDto();
            dto.setId(2L);
            dto.setPrice(product2.getPrice());
            dto.setQuantity(product2.getQuantity());
            BigDecimal price = product2.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(product2.getQuantity());
            BigDecimal tax = price.multiply(quantity).multiply(BigDecimal.valueOf(product2.getTax())).divide(BigDecimal.valueOf(100));
            BigDecimal total = price.multiply(quantity).add(tax);
            dto.setTotal(total); // Total should be 60 (price * quantity + tax)
            return dto;
        });

        List<InvoiceProductDto> result = invoiceService.findInvoiceProductsAndCalculateTotal(invoiceId);

        assertThat(result).isNotNull();

        assertThat(result.get(0).getTotal()).isEqualTo(BigDecimal.valueOf(220)); // (100 * 2) + (100 * 2 * 10 / 100) = 220
        assertThat(result.get(1).getTotal()).isEqualTo(BigDecimal.valueOf(60)); // (50 * 1) + (50 * 1 * 20 / 100) = 60

        verify(invoiceRepository).findById(invoiceId);
        verify(invoiceProductRepository).findByInvoiceAndIsDeletedFalse(invoice);
        verify(mapperUtil, times(1)).convert(eq(product1), any(InvoiceProductDto.class));
        verify(mapperUtil, times(1)).convert(eq(product2), any(InvoiceProductDto.class));
    }

    @Test
    void findInvoiceByClientVendorId() {
        Long clientVendorId = 1L;
        List<Invoice> mockInvoices = new ArrayList<>();

        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        invoice1.setInvoiceNo("INV001");

        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);
        invoice2.setInvoiceNo("INV002");

        mockInvoices.add(invoice1);
        mockInvoices.add(invoice2);

        when(invoiceRepository.findAllByClientVendor_Id(clientVendorId)).thenReturn(mockInvoices);

        when(mapperUtil.convert(eq(invoice1), any(InvoiceDto.class))).thenAnswer(invocation -> {
            InvoiceDto dto = new InvoiceDto();
            dto.setId(invoice1.getId());
            dto.setInvoiceNo(invoice1.getInvoiceNo());
            return dto;
        });

        when(mapperUtil.convert(eq(invoice2), any(InvoiceDto.class))).thenAnswer(invocation -> {
            InvoiceDto dto = new InvoiceDto();
            dto.setId(invoice2.getId());
            dto.setInvoiceNo(invoice2.getInvoiceNo());
            return dto;
        });

        List<InvoiceDto> result = invoiceService.findInvoiceByClientVendorId(clientVendorId);

        assertThat(result).isNotNull();

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getInvoiceNo()).isEqualTo("INV001");

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getInvoiceNo()).isEqualTo("INV002");

        verify(invoiceRepository).findAllByClientVendor_Id(clientVendorId);
        verify(mapperUtil, times(1)).convert(eq(invoice1), any(InvoiceDto.class));
        verify(mapperUtil, times(1)).convert(eq(invoice2), any(InvoiceDto.class));
    }

    @Test
    void approveSaleInvoice() throws Exception {
        Long invoiceId = 1L;

        // Create mock Invoice
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setIsDeleted(false);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);

        InvoiceProduct soldProduct = new InvoiceProduct();
        soldProduct.setQuantity(5); // 5 units to sell
        soldProduct.setPrice(BigDecimal.valueOf(100)); // Sell price per unit
        soldProduct.setTax(10); // 10% tax

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantityInStock(10); // Current stock
        product.setLowLimitAlert(2); // Low limit alert
        soldProduct.setProduct(product);

        invoice.setInvoiceProducts(Arrays.asList(soldProduct));

        InvoiceProduct purchaseProduct = new InvoiceProduct();
        purchaseProduct.setQuantity(10); // 10 units purchased
        purchaseProduct.setRemainingQuantity(10); // All units are available
        purchaseProduct.setPrice(BigDecimal.valueOf(50)); // Purchase price per unit
        purchaseProduct.setTax(5); // 5% purchase tax

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceProductRepository.findPurchaseProductsByProductOrderedByApprovalDate(product.getId()))
                .thenReturn(Arrays.asList(purchaseProduct));
        when(invoiceProductRepository.save(any(InvoiceProduct.class))).thenReturn(purchaseProduct); // Save purchase product changes
        when(productRepository.save(any(Product.class))).thenReturn(product); // Save product stock changes

        invoiceService.approveSaleInvoice(invoiceId);

        assertThat(invoice.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED); // Ensure invoice status is updated
        assertThat(invoice.getDate()).isNotNull(); // Ensure the date is set to the current date
        assertThat(product.getQuantityInStock()).isEqualTo(5); // 10 in stock - 5 sold = 5 remaining
        assertThat(soldProduct.getProfitLoss()).isNotNull(); // Ensure profit/loss is calculated

        verify(invoiceRepository).findById(invoiceId);
        verify(invoiceRepository).save(invoice);
        verify(invoiceProductRepository).findPurchaseProductsByProductOrderedByApprovalDate(product.getId());
        verify(invoiceProductRepository, times(2)).save(any(InvoiceProduct.class)); // One for purchase product, one for sold product
        verify(productRepository).save(product);
    }

    @Test
    void testApproveSaleInvoice_NoProducts_Exception() throws Exception {
        // Arrange
        Long invoiceId = 1L;

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setInvoiceProducts(Collections.emptyList());

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.approveSaleInvoice(invoiceId);
        });

        assertThat(exception.getMessage()).isEqualTo("Sales Invoice cannot be approved without products.");

        verify(invoiceRepository).findById(invoiceId);
    }

    @Test
    void testApproveSaleInvoice_InsufficientStock_Exception() throws Exception {
        Long invoiceId = 1L;

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setIsDeleted(false);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);

        InvoiceProduct soldProduct = new InvoiceProduct();
        soldProduct.setQuantity(5); // 5 units to sell

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantityInStock(3); // Only 3 in stock, but we need 5
        soldProduct.setProduct(product);

        invoice.setInvoiceProducts(Arrays.asList(soldProduct));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        ProductLowLimitAlertException exception = assertThrows(ProductLowLimitAlertException.class, () -> {
            invoiceService.approveSaleInvoice(invoiceId);
        });

        assertThat(exception.getMessage()).isEqualTo("Stock of Test Product is not enough to approve this invoice. Please update the invoice.");

        verify(invoiceRepository).findById(invoiceId);
    }

    @Test
    void testApproveSaleInvoice_InvoiceNotFound_Exception() {
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.approveSaleInvoice(invoiceId);
        });

        assertThat(exception.getMessage()).isEqualTo("Sales Invoice not found");

        verify(invoiceRepository).findById(invoiceId);
    }

}