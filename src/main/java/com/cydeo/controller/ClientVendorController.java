package com.cydeo.controller;


import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final InvoiceService invoiceService;

    public ClientVendorController(ClientVendorService clientVendorService, InvoiceService invoiceService) {
        this.clientVendorService = clientVendorService;
        this.invoiceService = invoiceService;
    }


    @GetMapping("/list")
    public String clientVendorList(Model model) {

        List<ClientVendorDto> clientVendors = clientVendorService.getClientVendorListByLoggedInUser();
        for (ClientVendorDto clientVendor : clientVendors) {
            boolean hasInvoice = !invoiceService.findInvoiceByClientVendorId(clientVendor.getId()).isEmpty();
            clientVendor.setHasInvoice(hasInvoice);
        }
        model.addAttribute("clientVendors", clientVendors);

        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", ClientVendorType.values());

        String[] countryCodes = Locale.getISOCountries();
        List<String> countries = new ArrayList<>();
        for (String code : countryCodes) {
            Locale locale = new Locale("", code);
            countries.add(locale.getDisplayCountry());
        }

        model.addAttribute("countries", countries);

        return "/clientVendor/clientVendor-create";

    }

    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult, Model model) {

        boolean isClientVendorNameExist = clientVendorService.isClientVendorExist(clientVendorDto);
        if(isClientVendorNameExist){
            bindingResult.rejectValue("clientVendorName", " ", "This client vendor already exist");

        }

        if (bindingResult.hasErrors()){
            List<String> countries = Arrays.stream(Locale.getISOCountries())
                    .map(code -> new Locale("", code).getDisplayCountry())
                    .sorted().toList();

            model.addAttribute("clientVendorTypes", ClientVendorType.values());
            model.addAttribute("countries", countries);
            return "clientVendor/clientVendor-create";
        }

        clientVendorService.save(clientVendorDto);

        return "redirect:/clientVendors/list";

    }

    @GetMapping("/update/{clientVendorId}")
    public String updateGetMapping(@PathVariable("clientVendorId") Long clientVendorId, Model model) {

        model.addAttribute("clientVendor", clientVendorService.findById(clientVendorId));
        model.addAttribute("clientVendorTypes", ClientVendorType.values());

        String[] countryCodes = Locale.getISOCountries();
        List<String> countries = new ArrayList<>();
        for (String code : countryCodes) {
            Locale locale = new Locale("", code);
            countries.add(locale.getDisplayCountry());
        }

        model.addAttribute("countries", countries);

        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{clientVendorId}")
    public String updateClientVendor(@PathVariable Long clientVendorId, @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto) {

        clientVendorService.update(clientVendorId, clientVendorDto);

        return "redirect:/clientVendors/list";
    }


    @GetMapping("/delete/{clientVendorId}")
    public String deleteClientVendor(@PathVariable("clientVendorId") Long clientVendorId) {
        System.out.println("Deleting client vendor with ID: " + clientVendorId);
        clientVendorService.delete(clientVendorId);

        return "redirect:/clientVendors/list";
    }


}
