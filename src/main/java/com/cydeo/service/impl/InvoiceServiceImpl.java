package com.cydeo.service.impl;

import com.cydeo.dto.*;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.Product;
import com.cydeo.exceptions.InvoiceNotFoundException;
import com.cydeo.exceptions.ProductLowLimitAlertException;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.*;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final CompanyService companyService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;


    public InvoiceServiceImpl(InvoiceProductRepository invoiceProductRepository, InvoiceRepository invoiceRepository, ProductRepository productRepository, MapperUtil mapperUtil, SecurityService securityService, CompanyService companyService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.companyService = companyService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
    }

    @Override
    public InvoiceDto createInvoiceByInvoiceType(InvoiceType invoiceType) {
        Long id = companyService.getCompanyIdByLoggedInUser();
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setInvoiceType(invoiceType);
        if (invoiceType.getValue().equals(InvoiceType.PURCHASE.getValue())) {
            Integer count = invoiceRepository.countAllByInvoiceTypeAndCompany_Id(InvoiceType.PURCHASE, id);
            final DecimalFormat decimalFormat = new DecimalFormat("000");
            invoiceDto.setInvoiceNo("P-" + decimalFormat.format(count + 1));
        } else if (invoiceType.getValue().equals(InvoiceType.SALES.getValue())) {
            Integer count = invoiceRepository.countAllByInvoiceTypeAndCompany_Id(InvoiceType.SALES, id);
            final DecimalFormat decimalFormat = new DecimalFormat("000");
            invoiceDto.setInvoiceNo("S-" + decimalFormat.format(count + 1));
        }
        return invoiceDto;
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice invoiceEntity = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice with ID " + id + " not found."));
        InvoiceDto invoiceDto = mapperUtil.convert(invoiceEntity, new InvoiceDto());
        return invoiceDto;
    }

    @Override
    public InvoiceDto listInvoiceByInvoiceNo(String invoiceNo) {
        Long id = securityService.getLoggedInUser().getCompany().getId();
        Invoice item = invoiceRepository.findFirstByInvoiceNoAndCompany_Id(invoiceNo, id);
        return mapperUtil.convert(item, new InvoiceDto());
    }


    @Override
    public List<InvoiceDto> listInvoices(InvoiceType type) {
        Long id = companyService.getCompanyIdByLoggedInUser();
        return invoiceRepository.findAllByCompany_IdAndInvoiceTypeAndIsDeleted(id, type, false)
                .stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNo).reversed())
                .map(each -> {
                            InvoiceDto invoiceDto = mapperUtil.convert(each, new InvoiceDto());
                            calculatePricesAndTaxes(invoiceDto);
                            return invoiceDto;
                        }
                ).toList();
    }

    @Transactional
    @Override
    public void deleteInvoice(Long id) throws Exception {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);

        if (!optionalInvoice.isPresent()) {
            throw new InvoiceNotFoundException("Invoice with ID " + id + " not found.");
        }
        Invoice invoice = optionalInvoice.get();
        if (invoice.getIsDeleted() || invoice.getInvoiceStatus() != InvoiceStatus.AWAITING_APPROVAL) {
            throw new Exception("Invoice cannot be deleted: either it is already deleted or not in AWAITING_APPROVAL status.");
        }
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);

        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceAndIsDeletedFalse(invoice);
        for (InvoiceProduct invoiceProduct : invoiceProducts) {
            invoiceProduct.setIsDeleted(true);
            invoiceProductRepository.save(invoiceProduct);
        }
    }

    @Override
    public void approvePurchaseInvoice(Long id) throws Exception {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (!optionalInvoice.isPresent()) {
            throw new InvoiceNotFoundException("Invoice with ID " + id + " not found.");
        }
        Invoice invoice = optionalInvoice.get();
        if (invoice.getIsDeleted() || invoice.getInvoiceStatus() != InvoiceStatus.AWAITING_APPROVAL) {
            throw new Exception("Invoice cannot be approved: either it is deleted or not in AWAITING_APPROVAL status.");
        }

        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);

        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceAndIsDeletedFalse(invoice);
        for (InvoiceProduct invoiceProduct : invoiceProducts) {
            Product product = invoiceProduct.getProduct();
            int newQuantity = product.getQuantityInStock() + invoiceProduct.getQuantity();

            product.setQuantityInStock(newQuantity);
            productRepository.save(product);

            int updatedRemainingQuantity = invoiceProduct.getRemainingQuantity() + invoiceProduct.getQuantity();
            invoiceProduct.setRemainingQuantity(updatedRemainingQuantity);
            invoiceProductRepository.save(invoiceProduct);
        }
    }

    @Override
    public void savePurchaseInvoice(InvoiceDto invoiceDto) {
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());
        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
    }

    @Override
    public void saveSaleInvoice(InvoiceDto invoiceDto) {
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setDate(LocalDate.now());
        invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
    }

    @Override
    public void updateInvoice(Long id, ClientVendorDto clientVendor) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice != null) {
            ClientVendorDto dto = clientVendorService.findById(clientVendor.getId());
            ClientVendor clientVendor1 = mapperUtil.convert(dto, new ClientVendor());
            invoice.setClientVendor(clientVendor1);
            invoiceRepository.save(invoice);
        }
    }

    @Override
    public InvoiceDto findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(Long id) throws Exception {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice with ID " + id + " not found."));
        InvoiceDto invoiceDto = mapperUtil.convert(invoice, new InvoiceDto());

        BigDecimal price = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal total;

        for (InvoiceProduct invoiceProduct : invoice.getInvoiceProducts()) {
            BigDecimal productPrice = new BigDecimal(String.valueOf(invoiceProduct.getPrice()));
            BigDecimal productQuantity = new BigDecimal(invoiceProduct.getQuantity());
            BigDecimal productTotal = productPrice.multiply(productQuantity);
            BigDecimal productTax = productTotal.multiply(new BigDecimal(invoiceProduct.getTax())).divide(new BigDecimal("100"));

            price = price.add(productTotal);
            tax = tax.add(productTax);
        }
        total = price.add(tax);

        invoiceDto.setPrice(price);
        invoiceDto.setTax(tax);
        invoiceDto.setTotal(total);
        return invoiceDto;
    }

    @Override
    public List<InvoiceProductDto> findInvoiceProductsAndCalculateTotal(Long Id) throws Exception {
        Invoice invoice = invoiceRepository.findById(Id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceAndIsDeletedFalse(invoice);

        return invoiceProducts.stream().map(product -> {
            InvoiceProductDto productDto = mapperUtil.convert(product, new InvoiceProductDto());

            BigDecimal price = new BigDecimal(String.valueOf(product.getPrice()));
            BigDecimal quantity = new BigDecimal(product.getQuantity());
            BigDecimal taxRate = new BigDecimal(product.getTax());
            BigDecimal tax = price.multiply(quantity).multiply(taxRate).divide(new BigDecimal("100"));
            BigDecimal total = price.multiply(quantity).add(tax);
            productDto.setTotal(total);
            return productDto;
        }).collect(Collectors.toList());
    }

    public List<InvoiceDto> findInvoiceByClientVendorId(Long id) {
        return invoiceRepository.findAllByClientVendor_Id(id).stream().map(obj -> mapperUtil.convert(obj, new InvoiceDto())).collect(Collectors.toList());
    }

    private void calculatePricesAndTaxes(InvoiceDto dto) {

        List<InvoiceProductDto> invoices = invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(dto.getId());
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (InvoiceProductDto each : invoices) {
            totalPrice = totalPrice.add(each.getTotal()); // with tax included.
            price = price.add(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity())));
        }
        BigDecimal tax = totalPrice.subtract(price);
        dto.setTax(tax); // price of total tax
        dto.setPrice(price); // without tax
        dto.setTotal(totalPrice); //with tax included
    }

    @Override
    public void approveSaleInvoice(Long id) throws Exception {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Sales Invoice not found"));

        if (invoice.getInvoiceProducts().isEmpty()) {
            throw new Exception("Sales Invoice cannot be approved without products.");
        }

        if (invoice.getIsDeleted() || invoice.getInvoiceStatus() != InvoiceStatus.AWAITING_APPROVAL) {
            throw new Exception("Sales Invoice cannot be approved: either it is deleted or not in AWAITING_APPROVAL status.");
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (InvoiceProduct soldProduct : invoice.getInvoiceProducts()) {
            int quantityToSell = soldProduct.getQuantity();

            List<InvoiceProduct> purchaseProducts = invoiceProductRepository
                    .findPurchaseProductsByProductOrderedByApprovalDate(soldProduct.getProduct().getId());
            for (InvoiceProduct purchaseProduct : purchaseProducts) {
                if (quantityToSell <= 0) break;

                int availableQuantity = purchaseProduct.getRemainingQuantity();
                if (availableQuantity > 0) {
                    int quantityTaken = Math.min(availableQuantity, quantityToSell);
                    BigDecimal costPrice = purchaseProduct.getPrice();
                    BigDecimal purchaseTaxRate = BigDecimal.valueOf(purchaseProduct.getTax()).divide(new BigDecimal("100"));
                    BigDecimal costPriceIncludingTax = costPrice.add(costPrice.multiply(purchaseTaxRate));
                    purchaseProduct.setRemainingQuantity(availableQuantity - quantityTaken);

                    invoiceProductRepository.save(purchaseProduct);

                    BigDecimal costForThisQuantity = costPriceIncludingTax.multiply(new BigDecimal(quantityTaken));
                    BigDecimal revenueForThisQuantity = soldProduct.getPrice().multiply(new BigDecimal(quantityTaken));
                    BigDecimal taxForThisQuantity = revenueForThisQuantity.multiply(new BigDecimal(soldProduct.getTax()).divide(new BigDecimal("100")));
                    totalCost = totalCost.add(costForThisQuantity);
                    totalRevenue = totalRevenue.add(revenueForThisQuantity);
                    totalTax = totalTax.add(taxForThisQuantity);
                    BigDecimal profitLoss = (totalRevenue.add(totalTax)).subtract(totalCost);
                    BigDecimal totalProfitLoss = BigDecimal.ZERO;
                    totalProfitLoss = totalProfitLoss.add(profitLoss);
                    soldProduct.setProfitLoss(totalProfitLoss);

                    invoiceProductRepository.save(soldProduct);
                    quantityToSell -= quantityTaken;
                }
            }
            Product product = soldProduct.getProduct();
            if (quantityToSell > 0) {
                throw new ProductLowLimitAlertException("Stock of " + product.getName() + " is not enough to approve this invoice. Please update the invoice.");
            } else {
                int currentStock = product.getQuantityInStock();
                int newStock = currentStock - soldProduct.getQuantity();
                if (newStock < 0) {
                    throw new ProductLowLimitAlertException("Stock of " + product.getName() + " is not enough to approve this invoice. Please update the invoice.");
                }
                product.setQuantityInStock(newStock);
                if(product.getQuantityInStock() <= product.getLowLimitAlert() && product.getQuantityInStock() >= 0){
                    throw new ProductLowLimitAlertException("Stock of " + product.getName() + " decreased below low limit!");
                }
                productRepository.save(product);
            }
        }

        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }
}