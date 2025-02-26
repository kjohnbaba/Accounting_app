package com.cydeo.repository;

import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findByInvoice_Id(Long id);

    @Query(value = "SELECT SUM(ip.quantity) FROM InvoiceProduct ip WHERE ip.invoice.id = ?1 AND ip.product.id=?2")
    Integer sumQuantityOfProducts(Long invoiceId, Long productId);

    @Query(value = "SELECT DISTINCT ip.product FROM InvoiceProduct ip WHERE ip.invoice.id = ?1")
    List<Product> getProductListByInvoiceId(Long invoiceId);

    @Query("SELECT i FROM InvoiceProduct i " +
            "WHERE i.invoice.company.id = :id AND i.invoice.invoiceStatus = 'APPROVED' " +
            "ORDER BY i.invoice.date DESC")
    List<InvoiceProduct> getInvoiceProductsByCompany(@Param("id") Long companyId);

    @Query(value = "SELECT ip FROM InvoiceProduct ip " +
            "WHERE ip.product.id = ?2 AND ip.remainingQuantity>0 " +
            "AND ip.invoice.invoiceStatus = 'APPROVED' AND ip.invoice.invoiceType = 'PURCHASE' " +
            "AND ip.invoice.company.id=?1 " +
            "order by ip.id asc ")
    List<InvoiceProduct> getApprovedPurchaseInvoiceProducts(Long companyId, Long productId);
}
