package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.exception.ProductLowLimitAlertException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository,
                                     MapperUtil mapperUtil,
                                     CompanyService companyService,
                                     @Lazy InvoiceService invoiceService,
                                     ProductService productService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.productService = productService;
    }

    @Override
    public InvoiceProductDto findById(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id)
                .orElseThrow(InvoiceProductNotFoundException::new);
        return mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> findAllByInvoiceIdAndCalculateTotalPrice(Long id) {
        return invoiceProductRepository.findByInvoice_Id(id).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto dto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    dto.setTotal(getTotalPriceWithTax(invoiceProduct));
                    return dto;
                })
                .toList();
    }

    @Override
    public InvoiceProductDto add(InvoiceProductDto invoiceProductDto, Long invoiceId) {
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        Invoice invoice = mapperUtil.convert(invoiceService.findById(invoiceId), new Invoice());
        invoiceProduct.setInvoice(invoice);
//        invoiceProduct.setRemainingQuantity(0);
        return mapperUtil.convert(invoiceProductRepository.save(invoiceProduct), new InvoiceProductDto());
    }

    @Override
    public InvoiceProductDto delete(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id)
                .orElseThrow(InvoiceProductNotFoundException::new);
        invoiceProduct.setIsDeleted(true);
        InvoiceProduct deleted = invoiceProductRepository.save(invoiceProduct);
        return mapperUtil.convert(deleted, new InvoiceProductDto());
    }

    @Override
    public void updateRemainingQuantityUponPurchaseApproval(Long invoiceId) {
        List<InvoiceProduct> list = invoiceProductRepository.findByInvoice_Id(invoiceId);
        for (InvoiceProduct each : list) {
            each.setRemainingQuantity(each.getQuantity());
            invoiceProductRepository.save(each);
        }
    }

    @Override
    public void updateQuantityInStockPurchase(Long invoiceId) {
        List<Product> products = invoiceProductRepository.getProductListByInvoiceId(invoiceId);
        for (Product product : products) {
            product.setQuantityInStock(product.getQuantityInStock()
                    + invoiceProductRepository.sumQuantityOfProducts(invoiceId, product.getId()));
            productService.save(mapperUtil.convert(product, new ProductDto()));
        }
    }

    @Override
    public void updateQuantityInStockSale(Long invoiceId) {
        List<Product> products = invoiceProductRepository.getProductListByInvoiceId(invoiceId);
        for (Product product : products) {
            final int stock = product.getQuantityInStock()
                    - invoiceProductRepository.sumQuantityOfProducts(invoiceId, product.getId());
            if (stock < 0)
                throw new ProductNotFoundException("Stock of " + product.getName()
                        + " is not enough to approve this invoice. Please update the invoice.");
            product.setQuantityInStock(stock);
            productService.save(mapperUtil.convert(product, new ProductDto()));
        }
    }

    @Override
    public void calculateProfitOrLoss(Long invoiceId) {
        List<InvoiceProduct> list = invoiceProductRepository.findByInvoice_Id(invoiceId);
        for (InvoiceProduct each : list) {
            Long productId = each.getProduct().getId();
            BigDecimal profitLoss = getTotalPriceWithTax(each)
                    .subtract(calculateCost(productId, each.getQuantity()));
            each.setProfitLoss(profitLoss);
            invoiceProductRepository.save(each);
        }
    }

    @Override
    public void checkForLowQuantityAlert(Long invoiceId) {
        List<Product> products = invoiceProductRepository.getProductListByInvoiceId(invoiceId);
        List<String> lowQuantityProductList = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantityInStock() < product.getLowLimitAlert()) {
                lowQuantityProductList.add(product.getName());
            }
        }
        if (!lowQuantityProductList.isEmpty()) {
            throw new ProductLowLimitAlertException("Stock of " + lowQuantityProductList + " decreased below low limit");
        }
    }

    @Override
    public List<InvoiceProductDto> findAllApprovedInvoiceProductsOfCompany() {
        Long id = companyService.getCompanyDtoByLoggedInUser().getId();
        return invoiceProductRepository.getInvoiceProductsByCompany(id).stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .toList();
    }

    private BigDecimal getTotalPriceWithTax(InvoiceProduct invoiceProduct) {
        BigDecimal totalPrice = invoiceProduct.getPrice().multiply(BigDecimal.valueOf(invoiceProduct.getQuantity()));
        BigDecimal totalTax = totalPrice.multiply(BigDecimal.valueOf(invoiceProduct.getTax() / 100d));
        return totalPrice.add(totalTax);
    }

    private BigDecimal calculateCost(long productId, int salesQuantity) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<InvoiceProduct> approvedPurchaseInvoiceProducts =
                invoiceProductRepository.getApprovedPurchaseInvoiceProducts(companyId, productId);
        BigDecimal totalCost = BigDecimal.ZERO;

        for (InvoiceProduct each : approvedPurchaseInvoiceProducts) {
            int remainingQty = each.getRemainingQuantity() - salesQuantity;
            if (remainingQty <= 0) {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(each.getRemainingQuantity()));
                BigDecimal taxAmount = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(taxAmount);
                salesQuantity = salesQuantity - each.getRemainingQuantity();
                each.setRemainingQuantity(0);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                if (remainingQty == 0) break;
            } else {
                BigDecimal costWithoutTax = each.getPrice().multiply(BigDecimal.valueOf(salesQuantity));
                BigDecimal taxAmount = costWithoutTax.multiply(BigDecimal.valueOf(each.getTax()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                BigDecimal costWithTax = costWithoutTax.add(taxAmount);
                each.setRemainingQuantity(remainingQty);
                totalCost = totalCost.add(costWithTax);
                invoiceProductRepository.save(each);
                break;
            }
        }
        return totalCost;
    }
}
