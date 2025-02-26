package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String listAllCategory(Model model) {

        model.addAttribute("categories", categoryService.listAllCategories());
        return "category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new CategoryDto());

        return "category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("newCategory") CategoryDto categoryDto, BindingResult bindingResult) {
        boolean isDescriptionExist = categoryService.isDescriptionExist(categoryDto.getDescription());
        if (isDescriptionExist) {
            bindingResult.rejectValue("description", " ", "This category description already exist");
        }
        if (bindingResult.hasErrors()) {
            return "category/category-create";
        }
        categoryService.save(categoryDto);

        return "redirect:/categories/list";
    }

    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));

        return "category/category-update";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult, @PathVariable Long id) {
        categoryDto.setId(id);
        boolean isDescriptionExist = categoryService.isDescriptionExist(categoryDto.getDescription());


        if (isDescriptionExist) {
            bindingResult.rejectValue("description", " ", "This category description already exist");
        }

        if (bindingResult.hasErrors()) {
            return "category/category-update";
        }
        categoryService.update(categoryDto);

        return "redirect:/categories/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories/list";
    }


}
