package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String listAllCompanies(Model model) {

        List<CompanyDto> companies = companyService.getAllCompanies();
        model.addAttribute("companies", companies);

        return "company/company-list";

    }

    @GetMapping("/create")
    public String createCompanyForm(Model model) {
        model.addAttribute("newCompany", new CompanyDto());
        return "company/company-create";
    }

    @PostMapping("/create")
    public String createCompany(@Valid @ModelAttribute("newCompany") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "company/company-create";
        }

        companyDto.setCompanyStatus(CompanyStatus.valueOf("ACTIVE"));

        companyService.createCompany(companyDto);
        return "redirect:/companies/list";
    }


    @GetMapping("/edit/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        CompanyDto company = companyService.findById(id);
        model.addAttribute("company", company);

        return "company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") Long id, @Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult result) {
        if (result.hasErrors()) {
            return "company/company-update";
        }
        companyService.updateCompany(id, companyDto);
        return "redirect:/companies/list";
    }


    @PostMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id) {
        companyService.activateCompany(id);
        return "redirect:/companies/list";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id) {
        companyService.deactivateCompany(id);
        return "redirect:/companies/list";
    }










}
