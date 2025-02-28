package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String listCategories(Model model) {
        List<CategoryDto> categories = categoryService.listCategoriesByUserCompany();
        model.addAttribute("categories", categories);
        return "category/category-list";
    }
    @GetMapping("/create")
    public String createCategory(Model model){
        model.addAttribute("newCategory",new CategoryDto());
        return "/category/category-create";
    }
    @PostMapping("/create")
    public String postCategory(CategoryDto categoryDto){
       categoryService.save(categoryDto);
       return "redirect:/categories/list";
    }
    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, Model model){
        model.addAttribute("category",categoryService.findByCategoryId(id));
        model.addAttribute("productCount",categoryService.findByCategoryId(id));
        return "/category/category-update";
    }
    @PostMapping("/update/{id}")
    public String postUpdateCategory(@ModelAttribute("category") CategoryDto category){
        categoryService.update(category);
        return "redirect:/categories/list";
    }

}
