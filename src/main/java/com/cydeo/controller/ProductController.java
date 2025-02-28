package com.cydeo.controller;

import com.cydeo.config.ProductValidator;
import com.cydeo.dto.ProductDto;
import com.cydeo.enums.ProductUnit;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    ProductService productService;
    ProductValidator productValidator;
    CategoryService categoryService;

    public ProductController(ProductService productService, ProductValidator productValidator, CategoryService categoryService) {
        this.productService = productService;
        this.productValidator = productValidator;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        List<ProductDto> products = productService.listAllProducts();
        model.addAttribute("products", products);
        return "product/product-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) throws Exception {
        ProductDto productDto = productService.findById(id);
        if (productDto == null) {
            return "redirect:/products/list";
        }

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(productDto, "product");
        productValidator.validate(productDto, bindingResult);

        if (bindingResult.hasErrors()) {
            List<ProductDto> products = productService.listAllProducts();
            model.addAttribute("products", products);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/product-list";
        }

        productService.deleteProduct(id);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("product", productDto);
        model.addAttribute("categories",categoryService.listCategoriesByUserCompany());
        model.addAttribute("productUnits", ProductUnit.values());
        return "product/product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute("product") ProductDto productDto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories",categoryService.listCategoriesByUserCompany());
            model.addAttribute("productUnits", ProductUnit.values());
            return "product/product-update";
        }
        productService.updateProduct(id,productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories",categoryService.listCategoriesByUserCompany());
        model.addAttribute("productUnits", ProductUnit.values());
        return "product/product-create";
    }

    @PostMapping ("/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories",categoryService.listCategoriesByUserCompany());
            model.addAttribute("productUnits", ProductUnit.values());
            return "product/product-create";
        }
        productService.createProduct(productDto);
        return "redirect:/products/list";
    }
}
