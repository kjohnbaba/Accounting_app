package com.cydeo.repository;

import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    List<InvoiceProduct> findByInvoice(Invoice invoice);
    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndInvoice_Company_IdAndInvoice_IsDeletedOrderByInvoice_InvoiceNoDesc(
            InvoiceType invoiceType, Long companyId, boolean isDeleted);

    @Query( value = "select * from invoice_products P join invoices I on P.invoice_id = I.id where invoice_type='PURCHASE';", nativeQuery = true)
    List<InvoiceProduct> listAllInvoiceProductsByInvoiceTypePurchase();

    // find approved purchase
    @Query(value = "select * from invoice_products IP JOIN invoices I on IP.invoice_id = I.id " +
            " JOIN companies C on C.id = I.company_id where C.id =?1 and I.invoice_status = 'APPROVED' and I.invoice_type = 'PURCHASE'",nativeQuery = true)
    List<InvoiceProduct> listAllApprovedPurchaseInvoiceProduct(Long companyId);

    // find approved sales
    @Query(value = "select * from invoice_products IP JOIN invoices I on IP.invoice_id = I.id " +
            " JOIN companies C on C.id = I.company_id where C.id =?1 and I.invoice_status = 'APPROVED' and I.invoice_type = 'SALES'",nativeQuery = true)
    List<InvoiceProduct> listAllApprovedSaleInvoiceProduct(Long companyId);

    // find approved sales and purchase
    @Query(value = "select * from invoice_products IP JOIN invoices I on IP.invoice_id = I.id " +
            " JOIN companies C on C.id = I.company_id where C.id =?1 and I.invoice_status = 'APPROVED' and I.invoice_type in ('PURCHASE','SALES') order by I.date desc",nativeQuery = true)
    List<InvoiceProduct> finAllApprovedSaleAndPurchaseInvoiceProduct(Long companyId);


    List<InvoiceProduct> findByInvoiceAndIsDeletedFalse(Invoice invoice);

    List<InvoiceProduct> findAllByInvoiceId(Long id);

    List<InvoiceProduct> findByProductAndIsDeletedFalse(Product product);

    InvoiceProduct findByIdAndInvoiceId(Long invoiceProductId, Long invoiceId);

    @Query("SELECT ip FROM invoice_products ip WHERE ip.invoice.invoiceType = 'PURCHASE' AND ip.product.id = :productId AND ip.quantity > 0 ORDER BY ip.invoice.date ASC")
    List<InvoiceProduct> findPurchaseProductsByProductOrderedByApprovalDate(@Param("productId") Long productId);

    @Query("SELECT TO_CHAR(ip.invoice.lastUpdateDateTime, 'YYYY') || ' ' || UPPER(TO_CHAR(ip.invoice.lastUpdateDateTime, 'FMMonth')) as formattedDate, " +
            "SUM(ip.profitLoss) as totalProfitLoss FROM invoice_products ip WHERE ip.invoice.invoiceType = 'SALES' AND ip.invoice.company.id = :companyId " +
            "GROUP BY EXTRACT(YEAR FROM ip.invoice.lastUpdateDateTime), EXTRACT(MONTH FROM ip.invoice.lastUpdateDateTime), TO_CHAR(ip.invoice.lastUpdateDateTime, 'YYYY'), TO_CHAR(ip.invoice.lastUpdateDateTime, 'FMMonth') " +
            "ORDER BY EXTRACT(YEAR FROM ip.invoice.lastUpdateDateTime) DESC, EXTRACT(MONTH FROM ip.invoice.lastUpdateDateTime) DESC")
    List<Object[]> findMonthlyProfitLoss(@Param("companyId") Long companyId);







}
