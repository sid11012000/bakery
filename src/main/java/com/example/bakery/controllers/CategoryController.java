package com.example.bakery.controllers;

import com.example.bakery.entity.Category;
import com.example.bakery.entity.SubCategory;
import com.example.bakery.repository.CategoryRepository;
import com.example.bakery.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired private CategoryRepository categoryRepo;
    @Autowired private SubCategoryRepository subCategoryRepo;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("subCategories", subCategoryRepo.findAll());
        model.addAttribute("category", new Category());
        model.addAttribute("subCategory", new SubCategory());
        return "manage-categories";
    }

    @PostMapping("/save-category")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepo.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/save-subcategory")
    public String saveSubCategory(@RequestParam Long categoryId,
                                  @RequestParam String name) {

        Category cat = categoryRepo.findById(categoryId).orElseThrow();
        SubCategory sc = new SubCategory();
        sc.setName(name);
        sc.setCategory(cat);
        subCategoryRepo.save(sc);

        return "redirect:/admin/categories";
    }

    @GetMapping("/delete-category/{id}")
    @Transactional
    public String deleteCategory(@PathVariable Long id) {
        subCategoryRepo.deleteAllByCategoryId(id);
        categoryRepo.deleteById(id);
        return "redirect:/admin/categories";
    }
}
