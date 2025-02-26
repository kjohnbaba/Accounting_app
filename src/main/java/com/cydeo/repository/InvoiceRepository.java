package com.cydeo.repository;

import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // JPA doesn't allow limit keyword but it has Pageable to limit the results
    @Query(value = "SELECT i.invoice_no FROM invoices i WHERE i.company_id = ?1 AND i.invoice_type = ?2 " +
            "ORDER BY i.insert_date_time DESC LIMIT 1", nativeQuery = true)
    String retrieveLastInvoiceNo(long companyId, String invoiceType);

    // same with above
    Optional<Invoice> findFirstByCompany_IdAndInvoiceTypeOrderByInvoiceNoDesc(long companyId, InvoiceType type);

    List<Invoice> findAllByCompany_IdAndInvoiceType(long id, InvoiceType type);

    @Query(value = "SELECT i FROM Invoice i WHERE i.company.id = ?1 AND i.invoiceStatus = 'APPROVED' AND i.invoiceType = 'SALES'")
    List<Invoice> findApprovedSalesInvoices(long companyId);

    // JPA doesn't allow limit keyword but it has Pageable to limit the results
    @Query(value = "SELECT * FROM invoices i WHERE i.company_id = ?1 AND i.invoice_status = 'APPROVED' " +
            "ORDER BY i.insert_date_time DESC LIMIT 3", nativeQuery = true)
    List<Invoice> findLastApproved3Invoices(long companyId);

}


