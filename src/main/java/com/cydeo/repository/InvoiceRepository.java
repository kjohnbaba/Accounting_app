package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findById(Long id);
    List<Invoice> getAllByInvoiceStatusAndCompanyOrderByDateDesc(InvoiceStatus status, Company company);
    Integer countAllByInvoiceTypeAndCompany_Id(InvoiceType invoiceType, Long companyId);
    Invoice findFirstByInvoiceNoAndCompany_Id(String invoiceNo, Long id);
    List<Invoice> findAllByClientVendor_Id(Long id);
    List<Invoice> findAllByCompany_IdAndInvoiceTypeAndIsDeleted(Long companyId, InvoiceType invoiceType, Boolean isDeleted);

}