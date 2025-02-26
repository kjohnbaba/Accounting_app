package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {


    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final CompanyService companyService;

    public SalesInvoiceController(InvoiceService invoiceService,
                                  ClientVendorService clientVendorService,
                                  ProductService productService,
                                  InvoiceProductService invoiceProductService,
                                  CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.companyService = companyService;
    }


    @GetMapping("/list")
    String listSalesInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.listInvoices(InvoiceType.SALES));
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String createSalesInvoice(Model model) {
        model.addAttribute("newSalesInvoice", invoiceService.generateNewInvoiceDto(InvoiceType.SALES));
        model.addAttribute("clients", clientVendorService.listByClientVendorType(ClientVendorType.CLIENT));
        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String saveSalesInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "invoice/sales-invoice-create";
        }
        InvoiceDto newInvoiceDto = invoiceService.saveInvoice(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/update/" + newInvoiceDto.getId();
    }

    @GetMapping("/update/{invoice}")
    public String editSalesInvoice(@PathVariable("invoice") Long id, Model model) {
        model.addAttribute("clients", clientVendorService.listByClientVendorType(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id));
        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdateIntoSaleList(@Valid @ModelAttribute InvoiceDto invoiceDto, BindingResult bindingResult,
                                         @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientVendorService.listByClientVendorType(ClientVendorType.CLIENT));
            model.addAttribute("products", productService.listAllProducts());
            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("invoiceProducts", invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id));
            return "invoice/sales-invoice-update";
        }
        invoiceService.update(invoiceDto);
        return "redirect:/salesInvoices/update/" + id;

    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProductIntoProductList(@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto,
                                            BindingResult bindingResult, @PathVariable("invoiceId") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("invoiceProducts", invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id));
            return "invoice/sales-invoice-update";
        }
        Integer quantityInStock = productService.getProductById(invoiceProductDto.getProduct().getId()).getQuantityInStock();

        if (invoiceProductDto.getQuantity() > quantityInStock) {
            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("invoiceProducts", invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id));
            bindingResult.rejectValue("quantity", "error.quantity",
                    "Not enough " + invoiceProductDto.getProduct().getName() + " quantity to sell.");
            return "invoice/sales-invoice-update";
        }
        invoiceProductService.add(invoiceProductDto, id);
        return "redirect:/salesInvoices/update/" + id;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String deleteProductFromProductList(@PathVariable("invoiceId") Long id,
                                               @PathVariable("invoiceProductId") Long id2) {
        invoiceProductService.delete(id2);
        return "redirect:/salesInvoices/update/" + id;
    }

    @GetMapping("/approve/{id}")
    public String approveSalesInvoice(@PathVariable("id") Long id) {
        invoiceService.approve(id);
        invoiceProductService.checkForLowQuantityAlert(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSalesInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.printInvoice(id));
        model.addAttribute("company", companyService.getCompanyDtoByLoggedInUser());
        model.addAttribute("invoiceProducts", invoiceProductService.findAllByInvoiceIdAndCalculateTotalPrice(id));
        return "invoice/invoice_print";
    }

    @ModelAttribute
    private void commonAttributes(Model model) {
//        model.addAttribute("clients", clientVendorService.listByClientVendorType(ClientVendorType.CLIENT));
//        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("title", "Cydeo Accounting-Sales Invoice");
    }
}
