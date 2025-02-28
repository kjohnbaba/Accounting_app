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
@RequestMapping("/purchaseInvoices")
public class PurchasesInvoiceController {


    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;


    public PurchasesInvoiceController(InvoiceProductService invoiceProductService, InvoiceService invoiceService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceProductService = invoiceProductService;
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }



    @GetMapping("/list")
    public String listInvoice(Model model) {
        model.addAttribute("invoices", invoiceService.listInvoices(InvoiceType.PURCHASE));
        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id") Long id, Model model) throws Exception {

        InvoiceDto invoiceDto = invoiceService.findApprovedInvoiceAndCalculateSubtotalTaxGrandTotal(id);
        System.out.println("INVOICE :" + invoiceDto);
        model.addAttribute("invoice", invoiceDto);

        List<InvoiceProductDto> invoiceProducts = invoiceService.findInvoiceProductsAndCalculateTotal(id);
        model.addAttribute("invoiceProducts", invoiceProducts);
        model.addAttribute("company", invoiceDto.getCompany());

        return "/invoice/invoice_print";
    }

    @GetMapping ("/update/{id}")
    public String updatePurchaseInvoice(@PathVariable("id") Long id) {
        return "redirect:/purchaseInvoices/edit/" + invoiceService.findById(id).getInvoiceNo();
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) throws Exception {
        invoiceService.deleteInvoice(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable Long id) throws Exception {
        invoiceService.approvePurchaseInvoice(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model) {
        model.addAttribute("newPurchaseInvoice", invoiceService.createInvoiceByInvoiceType(InvoiceType.PURCHASE));
        model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
        return "/invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String postPurchaseInvoice(@Valid @ModelAttribute("newPurchaseInvoice") InvoiceDto invoiceDto, BindingResult bindingResult , Model model){

        if (bindingResult.hasErrors()) {
            model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
            return "/invoice/purchase-invoice-create";
        }
        invoiceService.savePurchaseInvoice(invoiceDto);
        return "redirect:/purchaseInvoices/edit/" + invoiceDto.getInvoiceNo();
    }

    @GetMapping("/edit/{invoiceNo}")
    public String editPurchaseInvoice(@PathVariable("invoiceNo") String invoiceNo, Model model) {
        InvoiceDto invoiceDto = invoiceService.listInvoiceByInvoiceNo(invoiceNo);
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
        model.addAttribute("products", productService.listAllProductsByUserCompanyId());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceDto.getId()));
        return "/invoice/purchase-invoice-update";
    }

    @PostMapping ("/update/{invoiceId}")
    public String updatePurchaseInvoice(@PathVariable("invoiceId") Long id, @ModelAttribute("clientVendor") ClientVendorDto clientVendor, Model model) {
        model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
        invoiceService.updateInvoice(id, clientVendor);
        return "redirect:/purchaseInvoices/edit/" + invoiceService.findById(id).getInvoiceNo();
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String postPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto dto, BindingResult bindingResult, Model model) {
        InvoiceDto invoiceDto = invoiceService.findById(invoiceId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.listInvoiceByInvoiceNo(invoiceDto.getInvoiceNo()));
            model.addAttribute("vendors", clientVendorService.listAllByClientVendorType(ClientVendorType.VENDOR));
            model.addAttribute("products", productService.listAllProductsByUserCompanyId());
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceIdAndCalculateTotalPrice(invoiceDto.getId()));
            return "/invoice/purchase-invoice-update";
        }
        dto.setInvoice(invoiceService.findById(invoiceId));
        invoiceProductService.saveInvoiceProduct(dto);
        return "redirect:/purchaseInvoices/edit/" + invoiceDto.getInvoiceNo();
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.deleteInvoiceProduct(invoiceId, invoiceProductId);
        return "redirect:/purchaseInvoices/edit/" + invoiceService.findById(invoiceId).getInvoiceNo();
    }

}
