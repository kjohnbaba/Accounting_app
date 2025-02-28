package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.InvoiceType;
import java.util.List;


public interface InvoiceService {

    InvoiceDto findById(Long id);

    List<InvoiceDto> listInvoices(InvoiceType type);

    void deleteInvoice(Long id) throws Exception;

    void approvePurchaseInvoice(Long id) throws Exception;

    void approveSaleInvoice(Long id) throws Exception;

    void savePurchaseInvoice(InvoiceDto invoiceDto);

    void saveSaleInvoice(InvoiceDto invoiceDto);

    List<InvoiceDto> findInvoiceByClientVendorId(Long id);

    InvoiceDto findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(Long id) throws Exception;

    List<InvoiceProductDto> findInvoiceProductsAndCalculateTotal(Long Id) throws Exception;

    InvoiceDto listInvoiceByInvoiceNo(String invoiceNo);

    InvoiceDto createInvoiceByInvoiceType(InvoiceType invoiceType);

    void updateInvoice(Long invoiceId, ClientVendorDto clientVendor);
}

