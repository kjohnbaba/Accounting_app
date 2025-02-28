package com.cydeo.config;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@RequestMapping("/products")
public class ProductValidator implements Validator {
    private final ProductRepository productRepository;
    private final InvoiceProductRepository invoiceProductRepository;

    public ProductValidator(ProductRepository productRepository, InvoiceProductRepository invoiceProductRepository) {
        this.productRepository = productRepository;
        this.invoiceProductRepository = invoiceProductRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDto productDto = (ProductDto) target;
        Product product = productRepository.findById(productDto.getId())
                .orElse(null);

        if (product == null) {
            errors.rejectValue("id", "product.not.found", "Product not found.");
            return;
        }

        if (product.getQuantityInStock() > 0) {
            errors.rejectValue("quantityInStock", "product.cannot.delete.stock", "Cannot delete product with stock remaining.");
        }

        if (productHasInvoices(product)) {
            errors.reject("product.cannot.delete.invoices", "Cannot delete product as it is associated with invoices.");
        }
    }

    private boolean productHasInvoices(Product product) {
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByProductAndIsDeletedFalse(product);
        return !invoiceProducts.isEmpty();
    }
}
