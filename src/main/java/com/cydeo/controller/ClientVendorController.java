package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.AddressService;
import com.cydeo.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {
    private final ClientVendorService clientVendorService;
    private final AddressService addressService;

    public ClientVendorController(ClientVendorService clientVendorService, AddressService addressService) {
        this.clientVendorService = clientVendorService;
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public String listAllClientVendor(Model model) {
        model.addAttribute("clientVendors", clientVendorService.listAllClientVendors());
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendor(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", addressService.getCachedCountriesDto());
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String insertClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult, Model model) {
        boolean isClientVendorNameExist = clientVendorService.isClientVendorExist(clientVendorDto);
        if (isClientVendorNameExist) {
            bindingResult.rejectValue("clientVendorName", " ", "This client-vendor already exists.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
            model.addAttribute("countries", addressService.getCachedCountriesDto());
            return "clientVendor/clientVendor-create";
        }
        // using jdk to find all countries
//        if (bindingResult.hasErrors()) {
//            List<String> countries = Arrays.stream(Locale.getISOCountries())
//                    .map(code -> new Locale("", code).getDisplayCountry())
//                    .sorted()
//                    .toList();
//            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
//            model.addAttribute("countries", countries);
//            return "clientVendor/clientVendor-create";
//        }
        clientVendorService.save(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.deleteClientVendorById(id);
        return "redirect:/clientVendors/list";


    }

    @GetMapping("/update/{id}")
    public String editClientVendor(@PathVariable Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", addressService.getCachedCountriesDto());
        return "clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, @Valid @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto,
                                     BindingResult bindingResult, Model model) {
        clientVendorDto.setId(id);
        boolean isClientVendorNameExist = clientVendorService.isClientVendorExist(clientVendorDto);
        if (isClientVendorNameExist) {
            bindingResult.rejectValue("clientVendorName", " ", "This client-vendor already exists.");

        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
            model.addAttribute("countries", addressService.getCachedCountriesDto());
            return "clientVendor/clientVendor-update";
        }
        clientVendorService.update(clientVendorDto);
        return "redirect:/clientVendors/list";
    }
}
