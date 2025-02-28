package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;

    public SalesInvoiceController(InvoiceProductService invoiceProductService, InvoiceService invoiceService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceProductService = invoiceProductService;
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }



    @GetMapping("/list")
    public String listInvoice(Model model) {
        model.addAttribute("invoices", invoiceService.listInvoices(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id") Long id, Model model) throws Exception {

        InvoiceDto invoiceDto = invoiceService.findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(id);
        model.addAttribute("invoice", invoiceDto);
        List<InvoiceProductDto> invoiceProducts = invoiceService.findInvoiceProductsAndCalculateTotal(id);
        model.addAttribute("invoiceProducts", invoiceProducts);
        model.addAttribute("company", invoiceDto.getCompany());

        return "/invoice/invoice_print";
    }

    @GetMapping("/update/{id}")
    public String updateSaleInvoice(@PathVariable("id") Long id) {
        return "redirect:/salesInvoices/edit/" + invoiceService.findById(id).getInvoiceNo();
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id")Long id) throws Exception {
        invoiceService.deleteInvoice(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable Long id) throws Exception {
        invoiceService.approveSaleInvoice(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/create")
    public String createSaleInvoice( Model model) {
        model.addAttribute("newSalesInvoice", invoiceService.createInvoiceByInvoiceType(InvoiceType.SALES));
        model.addAttribute("clients", clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT));
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String invoicePost(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
            return "/invoice/sales-invoice-create";
        }
        invoiceService.saveSaleInvoice(invoiceDto);
        return "redirect:/salesInvoices/edit/" + invoiceDto.getInvoiceNo();
    }

    @GetMapping("/edit/{invoiceNo}")
    public String updateSaleInvoice(@PathVariable("invoiceNo") String invoiceNo, Model model) {
        InvoiceDto invoiceDto = invoiceService.listInvoiceByInvoiceNo(invoiceNo);
        model.addAttribute("invoice", invoiceService.listInvoiceByInvoiceNo(invoiceNo));
        model.addAttribute("clients", clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.listAllProductsByUserCompanyId());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceDto.getId()));
        return "/invoice/sales-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String updateSaleInvoice(@PathVariable("invoiceId") Long id, @ModelAttribute("clientVendor") ClientVendorDto clientVendor, Model model) {
        model.addAttribute("clients", clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT));
        invoiceService.updateInvoice(id, clientVendor);
        return "redirect:/salesInvoices/edit/" + invoiceService.findById(id).getInvoiceNo();
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String postPurchaseInvoice(@PathVariable("invoiceId") Long id, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto dto, BindingResult bindingResult, Model model) {
        InvoiceDto invoiceDto = invoiceService.findById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.listInvoiceByInvoiceNo(invoiceDto.getInvoiceNo()));
            model.addAttribute("clients", clientVendorService.listAllByClientVendorType(ClientVendorType.CLIENT));
            model.addAttribute("products", productService.listAllProductsByUserCompanyId());
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceDto.getId()));
            return "/invoice/sales-invoice-update";
        }
        dto.setInvoice(invoiceService.findById(id));
        invoiceProductService.saveInvoiceProduct(dto);
        return "redirect:/salesInvoices/edit/" + invoiceService.findById(id).getInvoiceNo();
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProduct(invoiceId, invoiceProductId);
        return "redirect:/salesInvoices/edit/" + invoiceService.findById(invoiceId).getInvoiceNo();
    }

}
