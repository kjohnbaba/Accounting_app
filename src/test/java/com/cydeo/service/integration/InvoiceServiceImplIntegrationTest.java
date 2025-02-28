package com.cydeo.service.integration;

import com.cydeo.dto.*;
import com.cydeo.entity.*;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.enums.*;
import com.cydeo.repository.*;
import com.cydeo.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class InvoiceServiceImplIntegrationTest {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ClientVendorRepository clientVendorRepository;
    @Autowired
    private InvoiceProductRepository invoiceProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    Role role = new Role();
    User user = new User();
    Address address = new Address();
    Company company = new Company();
    ClientVendor clientVendor = new ClientVendor();
    Invoice invoice = new Invoice();
    Category category = new Category();
    Product product = new Product();
    InvoiceProduct invoiceProduct = new InvoiceProduct();
    UserPrincipal userPrincipal;

    CompanyDto companyDto = new CompanyDto();
    ClientVendorDto clientVendorDto = new ClientVendorDto();
    InvoiceDto invoiceDto = new InvoiceDto();


    @BeforeEach
    void setUp() {

        role.setInsertDateTime(LocalDateTime.now());
        role.setInsertUserId(1L);
        role.setIsDeleted(false);
        role.setLastUpdateDateTime(LocalDateTime.now());
        role.setLastUpdateUserId(1L);
        role.setDescription("Manager");
        roleRepository.save(role);

        address.setInsertDateTime(LocalDateTime.now());
        address.setInsertUserId(1L);
        address.setIsDeleted(false);
        address.setLastUpdateDateTime(LocalDateTime.now());
        address.setLastUpdateUserId(1L);
        address.setAddressLine1("7925 Jones Branch Dr, #3300");
        address.setAddressLine2("Gilbert");
        address.setCity("Seattle");
        address.setState("WA");
        address.setCountry("United State");
        address.setZipCode("22102-5678");
        addressRepository.save(address);

        company.setInsertDateTime(LocalDateTime.now());
        company.setInsertUserId(1L);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        company.setIsDeleted(false);
        company.setLastUpdateDateTime(LocalDateTime.now());
        company.setLastUpdateUserId(1L);
        company.setTitle("CYDEO-" + UUID.randomUUID());
        company.setPhone("+1 (652) 852-8888");
        company.setWebsite("https://www.cydeo.com'");
        company.setAddress(address);
        companyRepository.save(company);

        user.setInsertDateTime(LocalDateTime.now());
        user.setInsertUserId(1L);
        user.setIsDeleted(false);
        user.setLastUpdateDateTime(LocalDateTime.now());
        user.setLastUpdateUserId(1L);
        user.setFirstname("Robert");
        user.setLastname("Noah");
        user.setUsername("manager@greentech.com" + UUID.randomUUID());
        user.setPhone("+1 (234) 564-5874");
        user.setPassword("$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK");
        user.setEnabled(true);
        user.setRole(role);
        user.setCompany(company);
        userRepository.save(user);

        userPrincipal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        clientVendor.setInsertDateTime(LocalDateTime.now());
        clientVendor.setInsertUserId(1L);
        clientVendor.setIsDeleted(false);
        clientVendor.setLastUpdateDateTime(LocalDateTime.now());
        clientVendor.setLastUpdateUserId(1L);
        clientVendor.setClientVendorType(ClientVendorType.CLIENT);
        clientVendor.setClientVendorName("Orange Tech" + UUID.randomUUID());
        clientVendor.setPhone("+1 (251) 321-4155");
        clientVendor.setWebsite("https://www.orange.com");
        clientVendor.setAddress(address);
        clientVendor.setCompany(company);
        clientVendorRepository.save(clientVendor);

        product.setInsertDateTime(LocalDateTime.now());
        product.setInsertUserId(1L);
        product.setIsDeleted(false);
        product.setLastUpdateDateTime(LocalDateTime.now());
        product.setLastUpdateUserId(1L);
        product.setName("HP Elite 800G1 Desktop Computer Package");
        product.setQuantityInStock(8);
        product.setLowLimitAlert(3);
        product.setProductUnit(ProductUnit.PCS);
        product.setCategory(category);
        productRepository.save(product);

        invoice.setInsertDateTime(LocalDateTime.now());
        invoice.setInsertUserId(1L);
        invoice.setIsDeleted(false);
        invoice.setLastUpdateDateTime(LocalDateTime.now());
        invoice.setInvoiceType(InvoiceType.PURCHASE);
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setLastUpdateUserId(1L);
        invoice.setInvoiceNo("INV123");
        invoice.setDate(LocalDate.now());
        invoice.setClientVendor(clientVendor);
        invoice.setCompany(company);
        invoiceRepository.save(invoice);

        category.setInsertDateTime(LocalDateTime.now());
        category.setInsertUserId(1L);
        category.setIsDeleted(false);
        category.setLastUpdateDateTime(LocalDateTime.now());
        category.setLastUpdateUserId(1L);
        category.setDescription("Computer");
        category.setCompany(company);
        categoryRepository.save(category);

        invoiceProduct.setInsertDateTime(LocalDateTime.now());
        invoiceProduct.setInsertUserId(1L);
        invoiceProduct.setIsDeleted(false);
        invoiceProduct.setLastUpdateDateTime(LocalDateTime.now());
        invoiceProduct.setLastUpdateUserId(1L);
        invoiceProduct.setPrice(BigDecimal.valueOf(100));
        invoiceProduct.setQuantity(2);
        invoiceProduct.setRemainingQuantity(8);
        invoiceProduct.setTax(10);
        invoiceProduct.setProfitLoss(BigDecimal.valueOf(0));
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setProduct(product);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Test
    void testFindById_ShouldReturnInvoiceDto() {
        InvoiceDto result = invoiceService.findById(invoice.getId());

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceNo()).isEqualTo("INV123");
    }

    @Test
    void testListPurchaseInvoices_ShouldReturnInvoiceDtoWithCalculatedPrices() {

        List<InvoiceDto> result = invoiceService.listInvoices(InvoiceType.PURCHASE);

        assertThat(result).isNotNull();
        InvoiceDto invoiceDto = result.get(0);

        assertThat(invoiceDto.getInvoiceNo()).isEqualTo("INV123");
        assertThat(invoiceDto.getPrice()).isEqualTo(BigDecimal.valueOf(200));  // 100 * 2 = 200
        assertThat(invoiceDto.getTax()).isEqualTo(BigDecimal.valueOf(20.0));     // 200 * 10% = 20.0
        assertThat(invoiceDto.getTotal()).isEqualTo(BigDecimal.valueOf(220.0));  // 200 + 20 = 220.0
    }

    @Test
    void testListSalesInvoices_ShouldReturnInvoiceDtoWithCalculatedPrices() {
        invoice.setInvoiceType(InvoiceType.SALES);
        invoiceRepository.save(invoice);
        List<InvoiceDto> result = invoiceService.listInvoices(InvoiceType.SALES);

        assertThat(result).isNotNull();
        InvoiceDto invoiceDto = result.get(0);

        assertThat(invoiceDto.getInvoiceNo()).isEqualTo("INV123");
        assertThat(invoiceDto.getPrice()).isEqualTo(BigDecimal.valueOf(200));  // 100 * 2 = 100
        assertThat(invoiceDto.getTax()).isEqualTo(BigDecimal.valueOf(20.0));     // 200 * 10% = 20.0
        assertThat(invoiceDto.getTotal()).isEqualTo(BigDecimal.valueOf(220.0));  // 200 + 20 = 120.0
    }

    @Test
    void testDeleteInvoice_ShouldRemoveInvoiceWithPendingApprovalStatus() throws Exception {
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceRepository.save(invoice);

        Long invoiceId = invoice.getId();
        assertThat(invoiceRepository.findById(invoiceId)).isPresent();

        invoiceService.deleteInvoice(invoiceId);

        assertThat(invoiceRepository.findById(invoiceId)).isPresent();
        assertThat(invoiceRepository.findById(invoiceId).get().getIsDeleted()).isTrue();
    }

    @Test
    void testDeleteInvoice_ShouldNotRemoveInvoiceWithApprovedStatus() {
        Long invoiceId = invoice.getId();
        assertThrows(Exception.class, () -> invoiceService.deleteInvoice(invoiceId),
                "APPROVED invoices can not be deleted");
    }

    @Test
    void testDeleteInvoice_ThrowsExceptionIfInvoiceIsNotExist() {
        Long invoiceId = 100L;
        assertThrows(Exception.class, () -> invoiceService.deleteInvoice(invoiceId),
                "Invoice with ID: 100 is not exist");
    }

    @Test
    void testApprovePurchaseInvoice_ShouldApproveInvoiceWithPendingApprovalStatus() throws Exception {
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setInvoiceType(InvoiceType.PURCHASE);
        invoiceRepository.save(invoice);

        Long invoiceId = invoice.getId();
        assertThat(invoiceRepository.findById(invoiceId)).isPresent();

        invoiceService.approvePurchaseInvoice(invoiceId);

        assertThat(invoiceRepository.findById(invoiceId)).isPresent();
        assertThat(invoice.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);
    }

    @Test
    void testApprovePurchaseInvoice_ShouldNotApproveInvoiceWithApprovedStatus() {
        Long invoiceId = invoice.getId();
        assertThrows(Exception.class, () -> invoiceService.approvePurchaseInvoice(invoiceId),
                "APPROVED invoices can not be approved again");
    }

    @Test
    void testSaveSaleInvoice_ShouldSaveInvoiceWithCorrectDetails() {

        invoiceDto.setInvoiceNo("INV356");
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setClientVendor(clientVendorDto);
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());

        clientVendorDto.setId(clientVendor.getId());
        companyDto.setId(company.getId());

        invoiceService.saveSaleInvoice(invoiceDto);

        List<Invoice> result = invoiceRepository.findAllByCompany_IdAndInvoiceTypeAndIsDeleted(company.getId(), InvoiceType.SALES, false);

        assertThat(result.stream().anyMatch(inv -> inv.getInvoiceType() == InvoiceType.SALES)).isTrue();
        assertThat(result.stream().anyMatch(inv -> inv.getInvoiceNo() == "INV356")).isTrue();
    }

    @Test
    void testSavePurchaseInvoice_ShouldSaveInvoiceWithCorrectDetails() {

        invoiceDto.setInvoiceNo("INV555");
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setClientVendor(clientVendorDto);
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());

        clientVendorDto.setId(clientVendor.getId());
        companyDto.setId(company.getId());

        invoiceService.savePurchaseInvoice(invoiceDto);

        List<Invoice> result = invoiceRepository.findAllByCompany_IdAndInvoiceTypeAndIsDeleted(company.getId(), InvoiceType.PURCHASE, false);

        assertThat(result.stream().anyMatch(inv -> inv.getInvoiceType() == InvoiceType.PURCHASE)).isTrue();
        assertThat(result.stream().anyMatch(inv -> inv.getInvoiceNo() == "INV555")).isTrue();
    }

    @Test
    void testFindInvoiceByClientVendorId_ShouldReturnInvoicesForClientVendor() {
        List<InvoiceDto> invoices = invoiceService.findInvoiceByClientVendorId(clientVendor.getId());
        assertThat(invoices.stream().anyMatch(inv -> inv.getClientVendor().getPhone() == "+1 (251) 321-4155")).isTrue();
    }

    @Test
    void testFindApprovedInvoiceAndCalculateSubtotalTaxGrandTotal() throws Exception {

        List<InvoiceProduct> invoiceProducts = new ArrayList<>();
        invoiceProducts.add(invoiceProduct);
        invoice.setInvoiceProducts(invoiceProducts);
        invoiceRepository.save(invoice);

        InvoiceDto result = invoiceService.findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(invoice.getId());

        BigDecimal expectedPrice = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(2)); // 100 * 2 = 200
        BigDecimal expectedTax = expectedPrice.multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100)); // 200 * 10% = 20
        BigDecimal expectedTotal = expectedPrice.add(expectedTax); // 200 + 20 = 220

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(expectedPrice);
        assertThat(result.getTax()).isEqualTo(expectedTax);
        assertThat(result.getTotal()).isEqualTo(expectedTotal);
    }

    @Test
    void testFindInvoiceProductsAndCalculateTotal_ShouldCalculateCorrectTotalWithTax() throws Exception {

        List<InvoiceProductDto> result = invoiceService.findInvoiceProductsAndCalculateTotal(invoice.getId());

        BigDecimal price = BigDecimal.valueOf(100);
        BigDecimal quantity = BigDecimal.valueOf(2);
        BigDecimal taxRate = BigDecimal.valueOf(10);
        BigDecimal expectedTax = price.multiply(quantity).multiply(taxRate).divide(BigDecimal.valueOf(100)); // 200 * 10% = 20
        BigDecimal expectedTotal = price.multiply(quantity).add(expectedTax); // 200 + 20 = 220

        InvoiceProductDto productDto = result.get(0);
        assertThat(productDto.getTotal()).isEqualTo(expectedTotal);
    }

    @Test
    void testListInvoiceByInvoiceNo_ShouldReturnCorrectInvoiceDto() {

        InvoiceDto result = invoiceService.listInvoiceByInvoiceNo("INV123");

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceNo()).isEqualTo("INV123");
        assertThat(result.getCompany().getId()).isEqualTo(company.getId());
    }

    @Test
    void testCreateInvoiceByInvoiceType_ShouldGenerateCorrectPurchaseInvoiceNo() {

        InvoiceDto result = invoiceService.createInvoiceByInvoiceType(InvoiceType.PURCHASE);

        assertThat(result).isNotNull();
        assertThat(result.getInvoiceType()).isEqualTo(InvoiceType.PURCHASE);
        assertThat(result.getInvoiceNo()).isEqualTo("P-002"); // Incremented from "P-001"
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testCreateInvoiceByInvoiceType_ShouldGenerateCorrectSalesInvoiceNo() {
        // Act
        InvoiceDto result = invoiceService.createInvoiceByInvoiceType(InvoiceType.SALES);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInvoiceType()).isEqualTo(InvoiceType.SALES);
        assertThat(result.getInvoiceNo()).isEqualTo("S-001"); // Incremented from "S-002"
        assertThat(result.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void testUpdateInvoice_ShouldUpdateClientVendor() {

        clientVendorDto.setId(clientVendor.getId());

        invoiceService.updateInvoice(invoice.getId(), clientVendorDto);

        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).orElse(null);
        assertThat(updatedInvoice).isNotNull();
        assertThat(updatedInvoice.getClientVendor().getClientVendorType()).isEqualTo(ClientVendorType.CLIENT);
        assertThat(updatedInvoice.getInvoiceNo()).isEqualTo("INV123");
    }

    @Test
    void approveSaleInvoice_ShouldApproveInvoice() throws Exception {
        // Set up stock for the sale and create purchase records
        product.setQuantityInStock(50);
        product.setLowLimitAlert(5);
        productRepository.save(product);

        // Set up purchase products with sufficient remaining quantities
        Invoice purchaseInvoice = new Invoice();
        purchaseInvoice.setInvoiceType(InvoiceType.PURCHASE);
        purchaseInvoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        purchaseInvoice.setInvoiceNo("PUR" + UUID.randomUUID());
        purchaseInvoice.setIsDeleted(false);
        purchaseInvoice.setDate(LocalDate.now().minusDays(1));
        purchaseInvoice.setCompany(company);
        purchaseInvoice.setClientVendor(clientVendor);
        invoiceRepository.save(purchaseInvoice);

        InvoiceProduct purchaseInvoiceProduct = new InvoiceProduct();
        purchaseInvoiceProduct.setInvoice(purchaseInvoice);
        purchaseInvoiceProduct.setProduct(product);
        purchaseInvoiceProduct.setQuantity(50);
        purchaseInvoiceProduct.setRemainingQuantity(50); // Sufficient for sale
        purchaseInvoiceProduct.setPrice(BigDecimal.valueOf(90)); // Purchase price
        purchaseInvoiceProduct.setTax(10);
        invoiceProductRepository.save(purchaseInvoiceProduct);

        invoiceProduct.setQuantity(2); // Quantity to sell
        invoiceProduct.setRemainingQuantity(50); // Matches product stock level
        invoiceProduct.setProduct(product);
        invoiceProductRepository.save(invoiceProduct);

        List<InvoiceProduct> invoiceProductList = new ArrayList<>();
        invoiceProductList.add(invoiceProduct);

        invoice.setInvoiceType(InvoiceType.SALES);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setLastUpdateUserId(1L);
        purchaseInvoice.setInvoiceNo("SALE" + UUID.randomUUID());
        invoice.setDate(LocalDate.now());
        invoice.setClientVendor(clientVendor);
        invoice.setCompany(company);
        invoice.setInvoiceProducts(invoiceProductList);
        invoiceRepository.save(invoice);

        invoiceService.approveSaleInvoice(invoice.getId());

        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();
        assertThat(updatedInvoice.getInvoiceStatus()).isEqualTo(InvoiceStatus.APPROVED);

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantityInStock()).isEqualTo(48); // Starting stock - quantity sold (50 - 2)
    }

}







