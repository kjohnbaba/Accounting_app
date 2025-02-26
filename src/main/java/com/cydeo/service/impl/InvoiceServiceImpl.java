package com.cydeo.service.impl;


import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceNotFoundException;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final CompanyService companyService;
    private final MapperUtil mapperUtil;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceProductService invoiceProductService, CompanyService companyService,
                              MapperUtil mapperUtil) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.companyService = companyService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listInvoices(InvoiceType invoiceType) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return invoiceRepository.findAllByCompany_IdAndInvoiceType(companyId, invoiceType).stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNo).reversed())
                .map(invoice -> {
                    InvoiceDto dto = mapperUtil.convert(invoice, new InvoiceDto());
                    calculatePricesAndTaxes(dto);
                    return dto;
                })
                .toList();
    }

    @Override
    public InvoiceDto generateNewInvoiceDto(InvoiceType invoiceType) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setInvoiceType(invoiceType);
        return invoiceDto;
    }

    @Override
    public InvoiceDto saveInvoice(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        invoiceDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setInvoiceType(invoiceType);
        Invoice invoice = invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto update(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(invoiceDto.getId()).orElseThrow(InvoiceNotFoundException::new);
        ClientVendor clientVendor = mapperUtil.convert(invoiceDto.getClientVendor(), new ClientVendor());
        invoice.setClientVendor(clientVendor);
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(InvoiceNotFoundException::new);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
        invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id)
                .forEach(invoiceProductDto -> invoiceProductService.delete(invoiceProductDto.getId()));
    }

    @Override
    public InvoiceDto printInvoice(Long id) {
        InvoiceDto dto = findById(id);
        calculatePricesAndTaxes(dto);
        return dto;
    }

    /**
     * We use this spring framework @Transactional annotation to be able to roll back all modifications if there is no enough stock while approving.
     * It is better to comment out it while debugging this method, otherwise we cannot see the immediate effect of saving to database.
     * @param invoiceId
     * @return
     */
    @Transactional
    @Override
    public InvoiceDto approve(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(InvoiceNotFoundException::new);
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        Invoice saved = invoiceRepository.save(invoice);
        if (invoice.getInvoiceType().equals(InvoiceType.SALES)) {
            invoiceProductService.updateQuantityInStockSale(invoiceId);
            invoiceProductService.calculateProfitOrLoss(invoiceId);
        } else {
            invoiceProductService.updateRemainingQuantityUponPurchaseApproval(invoiceId);
            invoiceProductService.updateQuantityInStockPurchase(invoiceId);
        }
        return mapperUtil.convert(saved, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> findLastThreeApprovedInvoice() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return invoiceRepository.findLastApproved3Invoices(companyId).stream()
                .map(invoice -> {
                    InvoiceDto dto = mapperUtil.convert(invoice, new InvoiceDto());
                    calculatePricesAndTaxes(dto);
                    return dto;
                })
                .toList();
    }

    @Override
    public BigDecimal sumTotal(InvoiceType invoiceType) {
        return listInvoices(invoiceType).stream()
                .filter(invoiceDto -> invoiceDto.getInvoiceStatus().equals(InvoiceStatus.APPROVED))
                .map(InvoiceDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumProfitLoss() {
        return invoiceRepository.findApprovedSalesInvoices(companyService.getCompanyDtoByLoggedInUser().getId())
                .stream()
                .map(invoice -> invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(invoice.getId()).stream()
                        .map(InvoiceProductDto::getProfitLoss).reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void calculatePricesAndTaxes(InvoiceDto dto) {
        List<InvoiceProductDto> invoiceProducts = invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(dto.getId());
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (InvoiceProductDto ip : invoiceProducts) {
            price = price.add(ip.getPrice().multiply(BigDecimal.valueOf(ip.getQuantity())));
            totalPrice = totalPrice.add(ip.getTotal());
        }
        BigDecimal tax = totalPrice.subtract(price);
        dto.setPrice(price);
        dto.setTax(tax);
        dto.setTotal(totalPrice);
    }

    private String generateInvoiceNo(InvoiceType invoiceType) {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        String invoiceNo = invoiceRepository.retrieveLastInvoiceNo(companyId, invoiceType.toString());
        long number = invoiceNo == null ? 0 : Long.parseLong(invoiceNo.substring(2));
        number++;
        String formatted = String.format("%03d", number);
        if (invoiceType.equals(InvoiceType.PURCHASE)) {
            return "P-" + formatted;
        }
        return "S-" + formatted;
    }
}